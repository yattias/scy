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
import eu.scy.agents.conceptmap.Edge;
import eu.scy.agents.conceptmap.Graph;
import eu.scy.agents.conceptmap.Node;
import eu.scy.agents.conceptmap.RankedKeywordList;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class CMProposerAgent extends AbstractThreadedAgent {

    public enum EnrichmentStrategy {
        SUB,
        SUPER,
        SIBLING,
        RELATIONS;
    }

    public enum Relation {
        isA,
        hasRelationTo,
        dependsOn,
        hasInfluenceOn,
        decreases,
        increases,
        includes;
        
        public String getLabel(@SuppressWarnings("unused") String language) {
            switch (this) {
                case isA:
                    return "ist ein";
                case hasRelationTo:
                    return "hat Verbindung zu";
                case dependsOn:
                    return "beruht auf";
                case hasInfluenceOn:
                    return "hat Einfluss auf";
                case decreases:
                    return "verringert";
                case increases:
                    return "erhöht";
                case includes:
                    return "bezieht ein";
            }
            return "";
        }
        
        public static boolean isKnown(String relation) {
            for (Relation r : Relation.values()) {
                if (r.name().equalsIgnoreCase(relation)) {
                    return true;
                }
            }
            return false;
        }
        
    }
    
    private static final String TEXT = "Als globale Erwärmung bezeichnet man den in den vergangenen Jahrzehnten beobachteten Anstieg der Durchschnittstemperatur der erdnahen Atmosphäre und der Meere sowie deren künftig erwartete Erwärmung. Zwischen 1906 und 2005 hat sich die durchschnittliche Außentemperatur in Bodennähe um 0,74 °C (+/- 0,18 °C) erhöht. Das Jahrzehnt von 2000 bis 2009 war mit Abstand das wärmste je gemessene, gefolgt von den 1990er Jahren, die wiederum wärmer waren als die 1980er Jahre. Nach gegenwärtigem wissenschaftlichen Verständnis ist hierfür \"sehr wahrscheinlich\" die Verstärkung des natürlichen Treibhauseffektes durch menschliches Einwirken ursächlich. Die menschengemachte Erwärmung entsteht durch Verbrennen fossiler Brennstoffe, durch weltumfassende Entwaldung sowie Land- und Viehwirtschaft. Dadurch wird das Treibhausgas Kohlendioxid (CO2) sowie weitere Treibhausgase wie Methan und Lachgas in der Erdatmosphäre angereichert. Diese CO2-Emissionen führen dazu, dass weniger Energie der Sonneneinstrahlung von der Erdoberfläche in das Weltall abgestrahlt werden kann. Bis zum Jahr 2100 wird, abhängig vom künftigen Treibhausgasausstoß und der tatsächlichen Reaktion des Klimasystems darauf (=Klimasensitivität), eine Erwärmung um 1,1 bis 6,4 °C erwartet. Dies hätte eine Reihe von Folgen: Verstärkte Gletscherschmelze, steigende Meeresspiegel, veränderte Niederschlagsmuster, zunehmende Wetterextreme, u.a. Die Vielzahl der Konsequenzen, die sich je nach Ausmaß der Veränderung des Klimas ergeben, ist jedoch kaum abschätzbar. Nationale und internationale Klimapolitik (siehe Kyoto-Protokoll) zielt sowohl auf die Vermeidung des Klimawandels wie auch auf die Anpassung an die zu erwartende Erwärmung ab. Der wissenschaftliche Erkenntnisstand zur globalen Erwärmung wird durch den Intergovernmental Panel on Climate Change (IPCC, im Deutschen oft als \"Weltklimarat\" bezeichnet) diskutiert und zusammengefasst. Die Analysen des IPCC, dessen Vierter Sachstandsbericht 2007 veröffentlicht wurde, bilden den Forschungsstand über menschliche Einflussnahmen auf das Klimasystem der Erde ab. Sie sind eine Hauptgrundlage der politischen und wissenschaftlichen Diskussion des Themas wie auch der Aussagen dazu in diesem Artikel. Die IPCC-Darstellung und die daraus zu ziehenden Folgerungen stehen zugleich im Mittelpunkt der Kontroverse um die globale Erwärmung.";

    private OntologyConnection con;

    private EnrichmentStrategy[] strategies = new EnrichmentStrategy[] { EnrichmentStrategy.SUPER, EnrichmentStrategy.RELATIONS, EnrichmentStrategy.SIBLING };

    private TupleSpace commandSpace;

    private CMProposerObserver observer;

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
        if (map.get("observer") != null) {
            this.observer = (CMProposerObserver) map.get("observer");
        } else {
            this.observer = new NullObserver();
        }
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
            observer.setStatusText("Agent running");
            Tuple returnTuple = commandSpace.waitToTake(new Tuple(String.class, "CMProposer", "cm proposal", String.class, String.class, Integer.class, String.class), 5000);
            if (returnTuple != null) {
                observer.clearState();
                observer.setStatusText("Received request ...");
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
        Field[] proposals = getProposals(user, elouri, amount, centralityAlgorithm);
        Tuple t = new Tuple(id, "response");
        for (Field proposal : proposals) {
            t.add(proposal);
        }
        observer.setStatusText("Responding with proposals");
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

    private Field[] getProposals(String user, String elouri, int amount, String centralityAlgorithm) {
        String ontologyNS = getOntologyNamespace(elouri);
        String lang = determineLanguage(elouri);

        observer.setStatusText("Retrieving text");
        String text = getText();
        observer.setCMText(text);

        observer.setStatusText("Determining keywords");
        Map<String, Double> keywordsFromText = getKeywordsFromText(text, ontologyNS);

        observer.setStatusText("Retrieving student concept map");
        Graph userGraph = getUserGraph(elouri, user);
        observer.foundStudentsMap(userGraph);

        if (!con.getLanguage().equals(lang) || !con.getOntologyNamespace().equals(ontologyNS)) {
            con.close();
            con = new SWATConnection(lang, ontologyNS);
        }

        Graph ontologyGraph = findOntologySubgraph(keywordsFromText.keySet(), ontologyNS);

        RankedKeywordList rankedStrings = new RankedKeywordList();
        if (centralityAlgorithm.equals("degree")) {
            TreeMap<Double, Set<Node>> rankedNodes = ontologyGraph.getNodeDegree();
            for (Entry<Double, Set<Node>> e : rankedNodes.entrySet()) {
                for (Node n : e.getValue()) {
                    double ranking = e.getKey() * keywordsFromText.get(n.getLabel());
                    rankedStrings.put(ranking, n.getLabel());
                }
            }
        }

        List<String> conceptProposals = getConceptProposals(userGraph, rankedStrings);
        if (conceptProposals.size() > amount) {
            conceptProposals = conceptProposals.subList(0, amount);
        }

        RankedKeywordList relationProposals = getRelationProposals(ontologyGraph, userGraph, rankedStrings);

        ArrayList<Field> result = new ArrayList<Field>();
        for (String s : conceptProposals) {
            observer.markConceptAsProposal(s);
            result.add(new Field("concept_proposal=" + s));
        }
        List<String> relationProposalList = relationProposals.getKeywords();
        for (int i = 0; i < amount && i < relationProposalList.size(); i++) {
            String s = relationProposalList.get(i);
            if (relationProposals.getWeightFor(s) > 0) {
                observer.markRelationAsProposal(s);
                result.add(new Field("relation_proposal=" + s));
            }
        }
        for (int i = 0; i < amount && i < relationProposalList.size(); i++) {
            String s = relationProposalList.get(relationProposalList.size() - 1 - i);
            if (relationProposals.getWeightFor(s) < 0) {
                observer.markRelationAsProposal(s);
                result.add(new Field("relation_specification_proposal=" + s));
            }
        }

        return (Field[]) result.toArray(new Field[result.size()]);
    }

    private RankedKeywordList getRelationProposals(Graph ontologyGraph, Graph userGraph, RankedKeywordList rankedStrings) {
        observer.setStatusText("Find relation proposals");
        RankedKeywordList list = new RankedKeywordList();
        try {
            HashSet<String> userNodes = new HashSet<String>();
            for (Node n : userGraph.getNodes()) {
                userNodes.add(n.getLabel().toLowerCase());
            }
            HashSet<String> foundRelations = new HashSet<String>();
            Map<String, Set<String>> relationHierarchy = con.getRelationHierarchy();
            for (Edge e : ontologyGraph.getEdges()) {
                String fromLabel = e.getFromNode().getLabel();
                String toLabel = e.getToNode().getLabel();
                if (userNodes.contains(fromLabel) && userNodes.contains(toLabel)) {
                    Edge e1 = userGraph.getEdgeForLabels(fromLabel, toLabel, true);
                    Edge e2 = userGraph.getEdgeForLabels(toLabel, fromLabel, true);
                    if (e1 == null && e2 == null) {
                        if (fromLabel.compareTo(toLabel) > 0) {
                            String tmp = toLabel;
                            toLabel = fromLabel;
                            fromLabel = tmp;
                        }
                        if (!foundRelations.contains(fromLabel + "," + toLabel)) {
                            list.put(rankedStrings.getWeightFor(fromLabel) + rankedStrings.getWeightFor(toLabel), fromLabel + "," + toLabel);
                            foundRelations.add(fromLabel + "," + toLabel);
                        }
                    } else {
                        Edge foundEdge = (e1 != null) ? e1 : e2;
                        String userRelation = foundEdge.getLabel();
                        String ontologyRelation = e.getLabel();
                        if (relationHierarchy.get(userRelation).contains(ontologyRelation)) {
                            if (!foundRelations.contains(fromLabel + "," + toLabel)) {
                                list.put(-rankedStrings.getWeightFor(fromLabel) - rankedStrings.getWeightFor(toLabel), fromLabel + "," + toLabel);
                                foundRelations.add(fromLabel + "," + toLabel);
                            }
                        }
                    }
                }
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> getConceptProposals(Graph userGraph, RankedKeywordList rankedStrings) {
        observer.setStatusText("Find concept proposals");
        List<String> ontologyConcepts = new ArrayList<String>();
        ontologyConcepts.addAll(rankedStrings.getKeywords());
        for (Node n : userGraph.getNodes()) {
            ontologyConcepts.remove(n.getLabel().toLowerCase());
            observer.markConceptAsMatching(n.getLabel());
        }
        return ontologyConcepts;
    }

    private String getText() {
        return TEXT;
    }

    private Graph getUserGraph(String elouri, String user) {
        Graph g = new Graph();
        try {
            String nid = new VMID().toString();
            String eid = new VMID().toString();
            commandSpace.write(new Tuple(nid, "userconceptmapagent", "conceptmap", user, elouri, "nodes"));
            commandSpace.write(new Tuple(eid, "userconceptmapagent", "conceptmap", user, elouri, "edges"));
            Tuple userNodesTuple = commandSpace.waitToTake(new Tuple(nid, "response", Field.createWildCardField()));
            Tuple userEdgesTuple = commandSpace.waitToTake(new Tuple(eid, "response", Field.createWildCardField()));
            Field[] nodeFields = new Field[userNodesTuple.getNumberOfFields() - 2];
            System.arraycopy(userNodesTuple.getFields(), 2, nodeFields, 0, nodeFields.length);
            Field[] edgesFields = new Field[userEdgesTuple.getNumberOfFields() - 2];
            System.arraycopy(userEdgesTuple.getFields(), 2, edgesFields, 0, edgesFields.length);
            g.fillFromFields(edgesFields, nodeFields);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
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

        Map<String, String> ontologyTerms = getOntologyTerms(namespace);
        HashMap<String, Set<String>> ontologySingleTerms = new HashMap<String, Set<String>>();
        for (String stemmedTerm : ontologyTerms.keySet()) {
            if (stemmedTerm.contains(" ")) {
                for (String s : stemmedTerm.split(" ")) {
                    if (!stopwords.contains(s)) {
                        Set<String> set = ontologySingleTerms.get(s);
                        if (set == null) {
                            set = new HashSet<String>();
                            ontologySingleTerms.put(s, set);
                        }
                        set.add(ontologyTerms.get(stemmedTerm));
                    }
                }
            } else {
                Set<String> set = ontologySingleTerms.get(stemmedTerm);
                if (set == null) {
                    set = new HashSet<String>();
                    ontologySingleTerms.put(stemmedTerm, set);
                }
                set.add(ontologyTerms.get(stemmedTerm));
            }
        }

        Map<String, Set<String>> ontologyClouds = getOntologyClouds(namespace);
        Map<String, Double> keywords = new HashMap<String, Double>();
        for (String term : text.split(" ")) {
            String stemmed = Stemmer.stem(term);
            if (ontologyClouds.containsKey(stemmed)) {
                for (String keyword : ontologyClouds.get(stemmed)) {
                    keywords.put(keyword.toLowerCase(), 0.2);
//                    System.out.println("cloud: " + keyword);
                    observer.foundTextKeyword(term);
                }
            }
            if (ontologySingleTerms.containsKey(stemmed)) {
                for (String keyword : ontologySingleTerms.get(stemmed)) {
                    keywords.put(keyword.toLowerCase(), 0.6);
//                    System.out.println("ontology-stemmed: " + keyword);
                }
                observer.foundTextKeyword(term);
            }
        }
        for (String oTerm : ontologyTerms.values()) {
            if (text.toLowerCase().contains(oTerm.toLowerCase())) {
                keywords.put(oTerm.toLowerCase(), 1.0);
                observer.foundTextKeyword(oTerm.toLowerCase());
//                System.out.println("ontology-full: " + oTerm);
            }
        }
        
        
        return keywords;
    }

    private Graph findConnections(List<String> terms, String namespace) {
        Graph g = new Graph();
        for (String term : terms) {
            try {
                String category = con.lookupEntityType(term, namespace);
                if (category.equals("individual")) {
                    findConceptsOfInstance(term.toLowerCase(), g, terms, namespace);
                } else if (category.equals("class")) {
                    findConceptsOfClass(term.toLowerCase(), g, terms, namespace);
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
        observer.setStatusText("Retrieving ontology conceps");
        Map<String, String> ontologyTerms = getOntologyTerms(ontologyNamespace);
        List<String> termsFound = new ArrayList<String>();
        for (String term : terms) {
            String stemmedTerm = ontologyTerms.get(Stemmer.stemWordWise(term));
            if (stemmedTerm != null) {
                termsFound.add(term);
                observer.foundOntoConcept(term);
            }
        }
        observer.setStatusText("Retrieving ontology relations");
        Graph g = findConnections(termsFound, ontologyNamespace);
        for (Edge e : g.getEdges()) {
            observer.foundOntoRelation(e.getFromNode().getLabel(), e.getToNode().getLabel(), e.getLabel());
        }
        return g;
    }

    private void findConceptsOfInstance(String entity, Graph g, List<String> terms, String ontologyNamespace) throws TupleSpaceException {
        String[] classes = null;
        String[][] propValues = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            classes = con.getClassesOfInstance(entity, ontologyNamespace);
            for (String clazz : classes) {
                if (terms.contains(clazz.toLowerCase())) {
                    g.addNodeAndEdges(entity, Relation.isA.getLabel(con.getLanguage()), clazz.toLowerCase());
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            propValues = con.getPropValuesOfInstance(entity, ontologyNamespace);
            for (String[] pv : propValues) {
                if (pv != null && pv.length == 2 && terms.contains(pv[1].toLowerCase()) && Relation.isKnown(pv[0])) {
                    g.addNodeAndEdges(entity, Relation.valueOf(pv[0]).getLabel(con.getLanguage()), pv[1].toLowerCase());
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
                            if (!i.equalsIgnoreCase(entity) && terms.contains(i.toLowerCase())) {
                                g.addNodeAndEdges(entity, Relation.hasRelationTo.getLabel(con.getLanguage()), i.toLowerCase());
                            }
                        }
                    }
                }
//                if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
//                    String[] subClasses = con.getSubclassesOfClass(clazz, ontologyNamespace);
//                    if (subClasses.length > 0 && subClasses[0].length() > 0) {
//                        for (String sc : subClasses) {
//                            if (terms.contains(sc.toLowerCase())) {
//                                g.addNodeAndEdges(entity, "hasSubSibling", sc.toLowerCase());
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    private void findConceptsOfClass(String entity, Graph g, List<String> terms, String ontologyNamespace) throws TupleSpaceException {
        String[] instances = null;
        String[] superClasses = null;
        String[] subClasses = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
            instances = con.getInstancesOfClass(entity, ontologyNamespace);
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    if (terms.contains(i.toLowerCase())) {
                        g.addNodeAndEdges(entity, "hasExample", i.toLowerCase());
                    }
                }
            }
            subClasses = con.getSubclassesOfClass(entity, ontologyNamespace);
            if (subClasses.length > 0 && subClasses[0].length() > 0) {
                for (String sc : subClasses) {
                    if (terms.contains(sc.toLowerCase())) {
                        g.addNodeAndEdges(entity, "has subclass", sc.toLowerCase());
                    }
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            superClasses = con.getSuperclassesOfClass(entity, ontologyNamespace);
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    if (terms.contains(sc.toLowerCase())) {
                        g.addNodeAndEdges(entity, Relation.isA.getLabel(con.getLanguage()), sc.toLowerCase());
                    }
                }
            }
        }
//        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
//            if (instances == null) {
//                instances = con.getInstancesOfClass(entity, ontologyNamespace);
//            }
//            if (instances.length > 0 && instances[0].length() > 0) {
//                for (String i : instances) {
//                    String[][] propValues = con.getPropValuesOfInstance(i, ontologyNamespace);
//                    for (String[] pv : propValues) {
//                        if (pv != null && pv.length == 2 && terms.contains(pv[1].toLowerCase())) {
//                            g.addNodeAndEdges(entity, "maybe " + pv[0], pv[1].toLowerCase());
//                        }
//                    }
//                }
//            }
//        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    subClasses = con.getSubclassesOfClass(sc, ontologyNamespace);
                    for (String subc : subClasses) {
                        if (!subc.equals(entity) && terms.contains(subc.toLowerCase())) {
                            g.addNodeAndEdges(entity, Relation.hasRelationTo.getLabel(con.getLanguage()), subc.toLowerCase());
                        }
                    }
                }
            }
        }
    }

}