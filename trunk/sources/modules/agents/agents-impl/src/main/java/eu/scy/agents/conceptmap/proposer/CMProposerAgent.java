package eu.scy.agents.conceptmap.proposer;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.conceptmap.Graph;
import eu.scy.agents.conceptmap.Node;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class CMProposerAgent extends AbstractThreadedAgent {

    public enum EnrichmentStrategy {
        SUB,
        SUPER,
        SIBLING,
        RELATIONS;
    }

    private static final String TEXT = "Als globale Erwärmung bezeichnet man den in den vergangenen Jahrzehnten beobachteten Anstieg der Durchschnittstemperatur der erdnahen Atmosphäre und der Meere sowie deren künftig erwartete Erwärmung. Zwischen 1906 und 2005 hat sich die durchschnittliche Außentemperatur in Bodennähe um 0,74 °C (+/- 0,18 °C) erhöht. Das Jahrzehnt von 2000 bis 2009 war mit Abstand das wärmste je gemessene, gefolgt von den 1990er Jahren, die wiederum wärmer waren als die 1980er Jahre. Nach gegenwärtigem wissenschaftlichen Verständnis ist hierfür \"sehr wahrscheinlich\" die Verstärkung des natürlichen Treibhauseffektes durch menschliches Einwirken ursächlich. Die menschengemachte Erwärmung entsteht durch Verbrennen fossiler Brennstoffe, durch weltumfassende Entwaldung sowie Land- und Viehwirtschaft. Dadurch wird das Treibhausgas Kohlendioxid (CO2) sowie weitere Treibhausgase wie Methan und Lachgas in der Erdatmosphäre angereichert. Diese CO2-Emissionen führen dazu, dass weniger Energie der Sonneneinstrahlung von der Erdoberfläche in das Weltall abgestrahlt werden kann. Bis zum Jahr 2100 wird, abhängig vom künftigen Treibhausgasausstoß und der tatsächlichen Reaktion des Klimasystems darauf (=Klimasensitivität), eine Erwärmung um 1,1 bis 6,4 °C erwartet. Dies hätte eine Reihe von Folgen: Verstärkte Gletscherschmelze, steigende Meeresspiegel, veränderte Niederschlagsmuster, zunehmende Wetterextreme, u.a. Die Vielzahl der Konsequenzen, die sich je nach Ausmaß der Veränderung des Klimas ergeben, ist jedoch kaum abschätzbar. Nationale und internationale Klimapolitik (siehe Kyoto-Protokoll) zielt sowohl auf die Vermeidung des Klimawandels wie auch auf die Anpassung an die zu erwartende Erwärmung ab. Der wissenschaftliche Erkenntnisstand zur globalen Erwärmung wird durch den Intergovernmental Panel on Climate Change (IPCC, im Deutschen oft als \"Weltklimarat\" bezeichnet) diskutiert und zusammengefasst. Die Analysen des IPCC, dessen Vierter Sachstandsbericht 2007 veröffentlicht wurde, bilden den Forschungsstand über menschliche Einflussnahmen auf das Klimasystem der Erde ab. Sie sind eine Hauptgrundlage der politischen und wissenschaftlichen Diskussion des Themas wie auch der Aussagen dazu in diesem Artikel. Die IPCC-Darstellung und die daraus zu ziehenden Folgerungen stehen zugleich im Mittelpunkt der Kontroverse um die globale Erwärmung.";

    private OntologyConnection con;

    private EnrichmentStrategy[] strategies = new EnrichmentStrategy[] { EnrichmentStrategy.SUPER, EnrichmentStrategy.SUB, EnrichmentStrategy.RELATIONS, EnrichmentStrategy.SIBLING };

    private TupleSpace commandSpace;

    public static void main(String[] args) throws AgentLifecycleException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AgentProtocol.PARAM_AGENT_ID, "proposer id");
        map.put(AgentProtocol.TS_HOST, "localhost");
        map.put(AgentProtocol.TS_PORT, 2525);
        CMProposerAgent agent = new CMProposerAgent(map);
        agent.start();
    }

    public CMProposerAgent(Map<String, Object> map) {
        super(CMProposerAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }

        Arrays.sort(strategies);
        // default, but will adapt at each call
        con = new SWATConnection("de", "http://www.scy.eu/co2house#");
        // con = new SWATConnection("en");
        // con = new OntologyAgentConnection();
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        while (status == Status.Running) {
            sendAliveUpdate();
            Tuple returnTuple = commandSpace.waitToTake(new Tuple(String.class, "CMProposer", "cm proposal", String.class, String.class, Integer.class, String.class), 5000);
            if (returnTuple != null) {
                commandSpace.write(generateResponse(returnTuple));
            }
        }
    }

    private Tuple generateResponse(Tuple returnTuple) {
        String id = returnTuple.getField(0).getValue().toString();
        String user = returnTuple.getField(3).getValue().toString();
        String elouri = returnTuple.getField(4).getValue().toString();
        int amount = (Integer) returnTuple.getField(5).getValue();
        String centralityAlgorithm = returnTuple.getField(6).getValue().toString();
        String[] proposals = getProposals(user, elouri, amount, centralityAlgorithm);
        Tuple t = new Tuple(id, "response");
        for (String proposal : proposals) {
            t.add(proposal);
        }
        return t;
    }

    @Override
    public void doStop() throws AgentLifecycleException {
        con.close();
    }

    @Override
    protected Tuple getIdentifyTuple(String queryId) {
        return null;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    private String[] getProposals(String user, String elouri, int amount, String centralityAlgorithm) {
        List<String> ontologyConcepts = new ArrayList<String>();
        try {
            String ontologyNS = getOntologyNamespace(elouri);
            String lang = determineLanguage(elouri);
            Map<String, Double> keywordsFromText = getKeywordsFromText(TEXT, ontologyNS);
            Graph userGraph = getUserGraph(elouri, user);

            if (!con.getLanguage().equals(lang) || !con.getOntologyNamespace().equals(ontologyNS)) {
                con.close();
                con = new SWATConnection(lang, ontologyNS);
            }
            Graph ontologyGraph = findOntologySubgraph(keywordsFromText.keySet(), ontologyNS);

            TreeMap<Double, String> rankedStrings = new TreeMap<Double, String>();
            if (centralityAlgorithm.equals("degree")) {
                Map<Integer, Set<Node>> rankedNodes = ontologyGraph.getDegree();
                for (Entry<Integer, Set<Node>> e : rankedNodes.entrySet()) {
                    for (Node n : e.getValue()) {
                        double ranking = e.getKey() * keywordsFromText.get(n.getLabel());
                        rankedStrings.put(ranking, n.getLabel());
                    }
                }
                ontologyConcepts.addAll(rankedStrings.values());
            }

            for (Node n : userGraph.getNodes()) {
                ontologyConcepts.remove(n.getLabel().toLowerCase());
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }

        List<String> subList = ontologyConcepts.subList(0, amount);
        return (String[]) subList.toArray(new String[subList.size()]);
    }

    private Graph getUserGraph(String elouri, String user) throws TupleSpaceException {
        String id = new VMID().toString();
        commandSpace.write(new Tuple(id, "userconceptmapagent", "conceptmap", user, elouri, "nodes"));
        Tuple userNodesTuple = commandSpace.waitToTake(new Tuple(id, "response", Field.createWildCardField()));
        Graph g = new Graph();
        Field[] nodeFields = new Field[userNodesTuple.getNumberOfFields() - 2];
        System.arraycopy(userNodesTuple.getFields(), 2, nodeFields, 0, nodeFields.length);
        g.fillFromFields(new Field[0], nodeFields);
        return g;
    }

    private String getOntologyNamespace(@SuppressWarnings("unused") String mission) {
        // TODO fetch from SCY ontology
        return "http://www.scy.eu/co2house#";
    }

    private String determineLanguage(@SuppressWarnings("unused") String elouri) {
        // TODO fetch ELO from Roolo and look at metadata
        return "de";
    }

    public Map<String, Double> getKeywordsFromText(String text, String namespace) {
        // TODO ask OntologyKeyword agent
        HashSet<String> stopwords = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/german_stopWords.txt")));
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                stopwords.add(Stemmer.stem(buffer));
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Set<String>> ontologyClouds = getOntologyClouds(namespace);

        Map<String, String> ontologyTerms = getOntologyTerms(namespace);
        HashMap<String, String> ontologySingleTerms = new HashMap<String, String>();
        for (String stemmedTerm : ontologyTerms.keySet()) {
            if (stemmedTerm.contains(" ")) {
                for (String s : stemmedTerm.split(" ")) {
                    if (!stopwords.contains(s)) {
                        ontologySingleTerms.put(s, ontologyTerms.get(stemmedTerm));
                    }
                }
            } else {
                ontologySingleTerms.put(stemmedTerm, ontologyTerms.get(stemmedTerm));
            }
        }
        Map<String, Double> keywords = new HashMap<String, Double>();
        for (String term : text.split(" ")) {
            String stemmed = Stemmer.stem(term);
            if (ontologyClouds.containsKey(stemmed)) {
                for (String keyword : ontologyClouds.get(stemmed)) {
                    keywords.put(keyword, 0.5);
                }
            }
            // TODO maybe consider if single or complete? 
            if (ontologySingleTerms.containsKey(stemmed)) {
                keywords.put(ontologySingleTerms.get(stemmed), 1.0);
            }
        }
        return keywords;
    }

    private Graph findConnections(List<String> terms, String namespace) {
        Graph g = new Graph();
        List<String> stemmedTerms = new ArrayList<String>();
        for (String term : terms) {
            stemmedTerms.add(Stemmer.stemWordWise(term));
        }
        for (String term : terms) {
            try {
                String category = con.lookupEntityType(term, namespace);
                if (category.equals("individual")) {
                    findConceptsOfInstance(term.toLowerCase(), g, stemmedTerms, namespace);
                } else if (category.equals("class")) {
                    findConceptsOfClass(term.toLowerCase(), g, stemmedTerms, namespace);
                }
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return g;
    }

    private Map<String, String> getOntologyTerms(String ontologyNamespace) {
        Map<String, String> term2Stem = new HashMap<String, String>();
        try {
            String[] terms = con.getOntologyTerms(ontologyNamespace);
            for (String term : terms) {
                term2Stem.put(Stemmer.stemWordWise(term), term);
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return term2Stem;
    }

    private Map<String, Set<String>> getOntologyClouds(String namespace) {
        Map<String, Set<String>> keyword2Concept = new HashMap<String, Set<String>>();
        try {
            Map<String, Set<String>> unstemmedKeywords2Clouds = con.getOntologyClouds(namespace);
            for (Entry<String, Set<String>> e : unstemmedKeywords2Clouds.entrySet()) {
                String keyword = Stemmer.stemWordWise(e.getKey());
                Set<String> clouds = keyword2Concept.get(keyword);
                if (clouds == null) {
                    clouds = new HashSet<String>();
                    keyword2Concept.put(keyword, clouds);
                }
                clouds.addAll(e.getValue());
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return keyword2Concept;
    }

    public Graph findOntologySubgraph(Set<String> terms, String ontologyNamespace) {
        Map<String, String> ontologyTerms = getOntologyTerms(ontologyNamespace);
        List<String> termsFound = new ArrayList<String>();
        for (String term : terms) {
            String stemmedTerm = ontologyTerms.get(Stemmer.stemWordWise(term));
            if (stemmedTerm != null) {
                termsFound.add(term);
            }
        }
        Graph g = findConnections(termsFound, ontologyNamespace);
        return g;
    }

    private void findConceptsOfInstance(String entity, Graph g, List<String> stemmedTerms, String ontologyNamespace) throws TupleSpaceException {
        String[] classes = null;
        String[][] propValues = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            classes = con.getClassesOfInstance(entity, ontologyNamespace);
            for (String clazz : classes) {
                if (stemmedTerms.contains(Stemmer.stemWordWise(clazz))) {
                    g.addNodeAndEdges(entity, "is a", clazz.toLowerCase());
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            propValues = con.getPropValuesOfInstance(entity, ontologyNamespace);
            for (String[] pv : propValues) {
                if (pv != null && pv.length == 2 && stemmedTerms.contains(Stemmer.stemWordWise(pv[1]))) {
                    g.addNodeAndEdges(entity, pv[0], pv[1].toLowerCase());
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0 || Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
            if (classes == null) {
                classes = con.getClassesOfInstance(entity, ontologyNamespace);
            }
            for (String clazz : classes) {
                if (Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
                    String[] instances = con.getInstancesOfClass(clazz, ontologyNamespace);
                    if (instances.length > 0 && instances[0].length() > 0) {
                        for (String i : instances) {
                            if (!i.equalsIgnoreCase(entity) && stemmedTerms.contains(Stemmer.stemWordWise(i))) {
                                g.addNodeAndEdges(entity, "sibling of", i.toLowerCase());
                            }
                        }
                    }
                }
                if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
                    String[] subClasses = con.getSubclassesOfClass(clazz, ontologyNamespace);
                    if (subClasses.length > 0 && subClasses[0].length() > 0) {
                        for (String sc : subClasses) {
                            if (stemmedTerms.contains(Stemmer.stemWordWise(sc))) {
                                g.addNodeAndEdges(entity, "has sub-sibling", sc.toLowerCase());
                            }
                        }
                    }
                }
            }
        }
    }

    private void findConceptsOfClass(String entity, Graph g, List<String> stemmedTerms, String ontologyNamespace) throws TupleSpaceException {
        String[] instances = null;
        String[] superClasses = null;
        String[] subClasses = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
            instances = con.getInstancesOfClass(entity, ontologyNamespace);
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    if (stemmedTerms.contains(Stemmer.stemWordWise(i))) {
                        g.addNodeAndEdges(entity, "has example", i.toLowerCase());
                    }
                }
            }
            subClasses = con.getSubclassesOfClass(entity, ontologyNamespace);
            if (subClasses.length > 0 && subClasses[0].length() > 0) {
                for (String sc : subClasses) {
                    if (stemmedTerms.contains(Stemmer.stemWordWise(sc))) {
                        g.addNodeAndEdges(entity, "has subclass", sc.toLowerCase());
                    }
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            superClasses = con.getSuperclassesOfClass(entity, ontologyNamespace);
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    if (stemmedTerms.contains(Stemmer.stemWordWise(sc))) {
                        g.addNodeAndEdges(entity, "has superclass", sc.toLowerCase());
                    }
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            if (instances == null) {
                instances = con.getInstancesOfClass(entity, ontologyNamespace);
            }
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    String[][] propValues = con.getPropValuesOfInstance(i, ontologyNamespace);
                    for (String[] pv : propValues) {
                        if (pv != null && pv.length == 2 && stemmedTerms.contains(Stemmer.stemWordWise(pv[1]))) {
                            g.addNodeAndEdges(entity, "maybe " + pv[0], pv[1].toLowerCase());
                        }
                    }
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    subClasses = con.getSubclassesOfClass(sc, ontologyNamespace);
                    for (String subc : subClasses) {
                        if (!subc.equals(entity) && stemmedTerms.contains(Stemmer.stemWordWise(subc))) {
                            g.addNodeAndEdges(entity, "has sibling", subc.toLowerCase());
                        }
                    }
                }
            }
        }
    }

}