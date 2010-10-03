package eu.scy.agents.conceptmap.proposer;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

    private OntologyConnection con;

    private EnrichmentStrategy[] strategies = new EnrichmentStrategy[] { EnrichmentStrategy.SUPER, EnrichmentStrategy.SUB, EnrichmentStrategy.RELATIONS, EnrichmentStrategy.SIBLING };

    private TupleSpace commandSpace;

    public static void main(String[] args) throws AgentLifecycleException {
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put(AgentProtocol.PARAM_AGENT_ID, "a nice little id should be inserted here");
        m.put(AgentProtocol.TS_HOST, "scy.collide.info");
        m.put(AgentProtocol.TS_PORT, 2525);
        CMProposerAgent agent = new CMProposerAgent(m);
        Graph g = agent.findOntologySubgraph(new String[] { "menschlicher Einfluss", "co2-emissionen", "treibhauseffekt", "Kohlekraft", "globale erwärmung" }, "http://www.scy.eu/co2house#");
        System.out.println(g);
        agent.doStop();
    }

    protected CMProposerAgent(Map<String, Object> map) {
        super(CMProposerAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        try {
            commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }

        // (<ID>:String, "CMProposer":String, "cm proposal":String, <User>:String, <ELOURI>:String,
        // <NumberOfProposals>:Integer, <CentralityAlgorithm>:String) --> (<ID>:String,
        // "response":String, <?>)
        Arrays.sort(strategies);
        con = new SWATConnection("de", "http://www.scy.eu/co2house#"); // default, but will adapt at
                                                                       // each call
        // con = new SWATConnection("en");
        // con = new OntologyAgentConnection();
    }

    @Override
    protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
        while (status == Status.Running) {
            sendAliveUpdate();
            Thread.sleep(5000);
        }
    }

    @Override
    protected void doStop() throws AgentLifecycleException {
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

    private String[] getProposals(String user, String elouri, String mission, int amount, String centralityAlgorithm) {
        String ontologyNS = getOntologyNamespace(mission);
        String lang = determineLanguage(elouri);
        String[] keywordsFromText = getKeywordsFromText(""); // TODO
        Graph userGraph = getUserGraph(elouri, user);

        if (!con.getLanguage().equals(lang) || !con.getOntologyNamespace().equals(ontologyNS)) {
            con.close();
            con = new SWATConnection(lang, ontologyNS);
        }
        Graph ontologyGraph = findOntologySubgraph(keywordsFromText, ontologyNS);

        List<String> ontologyConcepts = new ArrayList<String>();
        TreeMap<Integer, String> rankedStrings = new TreeMap<Integer, String>();
        if (centralityAlgorithm.equals("degree")) {
            Map<Node, Integer> rankedNodes = ontologyGraph.getDegree();
            for (Entry<Node, Integer> e : rankedNodes.entrySet()) {
                rankedStrings.put(e.getValue(), e.getKey().getLabel());
            }
            ontologyConcepts.addAll(rankedStrings.values());
        }
        
        for (Node n : userGraph.getNodes()) {
            rankedStrings.remove(n.getLabel());
        }
        
        List<String> subList = ontologyConcepts.subList(0, amount);
        return (String[]) subList.toArray(new String[subList.size()]);
    }

    private Graph getUserGraph(String elouri, String user) {
        // TODO
        return null;
    }

    private String getOntologyNamespace(String mission) {
        // TODO fetch from SCY ontology
        return "http://www.scy.eu/co2house#";
    }

    private String determineLanguage(String elouri) {
        // TODO fetch ELO from Roolo and look at metadata
        return "de";
    }

    private String[] getKeywordsFromText(String text) {
        // TODO ask OntologyKeyword agent
        return new String[0];
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

    public Graph findOntologySubgraph(String[] terms, String ontologyNamespace) {
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