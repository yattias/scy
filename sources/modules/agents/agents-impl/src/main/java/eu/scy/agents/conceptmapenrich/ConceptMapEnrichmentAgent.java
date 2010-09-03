package eu.scy.agents.conceptmapenrich;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ConceptMapEnrichmentAgent extends AbstractThreadedAgent {

    public enum EnrichmentStrategy {
        SUB,
        SUPER,
        SIBLING,
        RELATIONS;
    }

    public static final String ONTOLOGY_NS = "http://www.scy.eu/co2house#";

    private OntologyConnection con;

    private EnrichmentStrategy[] strategies = new EnrichmentStrategy[] { EnrichmentStrategy.SUPER, EnrichmentStrategy.SUB, EnrichmentStrategy.RELATIONS, EnrichmentStrategy.SIBLING };

    private String requestId = "later this will be a request ID";

    // TODO: change to protected
    public ConceptMapEnrichmentAgent(Map<String, Object> map) {
        super(ConceptMapEnrichmentAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        Arrays.sort(strategies);
        con = new SWATConnection("de");
//         con = new SWATConnection("en");
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

    public Set<Tuple> findConnections(String term, String namespace) {
        Set<Tuple> newConcepts = new HashSet<Tuple>();
        try {
            String category = con.lookupEntityType(term, namespace);
            if (category.equals("individual")) {
                newConcepts = findConceptsOfInstance(term);
            } else if (category.equals("class")) {
                newConcepts = findConceptsOfClass(term);
            }
            if (newConcepts.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Tuple newConcept : newConcepts) {
                    sb.append("\n").append("Proposal: " + newConcept.getField(2).getValue() + " "+ newConcept.getField(3).getValue() + " " + newConcept.getField(4).getValue());
                }
                JOptionPane.showMessageDialog(null, "Following connections have been found:" + sb.toString());
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return newConcepts;
    }

    // TODO: change to private
    public Map<String, String> getOntologyTerms() {
        Map<String, String> term2Stem = new HashMap<String, String>();
        try {
            String[] terms = con.getOntologyTerms(ONTOLOGY_NS);
            for (String term : terms) {
                // term2Stem.put(unCamelize(term, true), term);
                term2Stem.put(Stemmer.stemWordWise(term), term);
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return term2Stem;
    }

    private String unCamelize(String nextToken, boolean stem) {
        return nextToken;
        // Matcher m = Pattern.compile("\\p{Lu}").matcher(nextToken);
        // StringBuffer sb = new StringBuffer();
        // while (m.find()) {
        // m.appendReplacement(sb, " " + m.group());
        // }
        // m.appendTail(sb);
        // String[] words = sb.toString().split(" ");
        // sb.delete(0, sb.length());
        // for (String s : words) {
        // if (stem) {
        // sb.append(Stemmer.stem(s)).append(" ");
        // } else {
        // sb.append(s).append(" ");
        // }
        // }
        // return sb.toString().trim().toLowerCase();
    }

    public void findMatches(Map<String, String> ontologyTerms, Graph graph) {
        for (Node n : graph.getNodes()) {
            String term = ontologyTerms.get(n.getStemmedLabel());
            if (term != null) {
                findConnections(term, ONTOLOGY_NS);
            }
        }
    }

    private Set<Tuple> findConceptsOfInstance(String entity) throws TupleSpaceException {
        Set<Tuple> concepts = new HashSet<Tuple>();
        String[] classes = null;
        String[][] propValues = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            classes = con.getClassesOfInstance(entity, ONTOLOGY_NS);
            for (String clazz : classes) {
                concepts.add(new Tuple("concept match", requestId, entity, "is a", clazz));
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            propValues = con.getPropValuesOfInstance(entity, ONTOLOGY_NS);
            for (String[] pv : propValues) {
                if (pv != null && pv.length == 2) {
                    concepts.add(new Tuple("concept match", requestId, entity, pv[0], pv[1]));
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0 || Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
            if (classes == null) {
                classes = con.getClassesOfInstance(entity, ONTOLOGY_NS);
            }
            for (String clazz : classes) {
                if (Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
                    String[] instances = con.getInstancesOfClass(clazz, ONTOLOGY_NS);
                    if (instances.length > 0 && instances[0].length() > 0) {
                        for (String i : instances) {
                            if (!i.equals(entity)) {
                                concepts.add(new Tuple("concept match", requestId, entity, "sibling of", i));
                            }
                        }
                    }
                }
                if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
                    String[] subClasses = con.getSubclassesOfClass(clazz, ONTOLOGY_NS);
                    if (subClasses.length > 0 && subClasses[0].length() > 0) {
                        for (String sc : subClasses) {
                            concepts.add(new Tuple("concept match", requestId, entity, "has sub-sibling", sc));
                        }
                    }
                }
            }
        }
        return concepts;
    }

    private Set<Tuple> findConceptsOfClass(String entity) throws TupleSpaceException {
        Set<Tuple> concepts = new HashSet<Tuple>();
        String[] instances = null;
        String[] superClasses = null;
        String[] subClasses = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
            instances = con.getInstancesOfClass(entity, ONTOLOGY_NS);
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    concepts.add(new Tuple("concept match", requestId, entity, "has example", i));
                }
            }
            subClasses = con.getSubclassesOfClass(entity, ONTOLOGY_NS);
            if (subClasses.length > 0 && subClasses[0].length() > 0) {
                for (String sc : subClasses) {
                    concepts.add(new Tuple("concept match", requestId, entity, "has subclass", sc));
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            superClasses = con.getSuperclassesOfClass(entity, ONTOLOGY_NS);
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    concepts.add(new Tuple("concept match", requestId, entity, "has superclass", sc));
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            if (instances == null) {
                instances = con.getInstancesOfClass(entity, ONTOLOGY_NS);
            }
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    String[][] propValues = con.getPropValuesOfInstance(i, ONTOLOGY_NS);
                    for (String[] pv : propValues) {
                        if (pv != null && pv.length == 2) {
                            concepts.add(new Tuple("concept match", requestId, entity, "maybe " + pv[0], pv[1]));
                        }
                    }
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SIBLING) >= 0) {
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    subClasses = con.getSubclassesOfClass(sc, ONTOLOGY_NS);
                    for (String subc : subClasses) {
                        if (!subc.equals(entity)) {
                            concepts.add(new Tuple("concept match", requestId, entity, "has sibling", subc));
                        }
                    }
                }
            }
        }
        return concepts;
    }

}