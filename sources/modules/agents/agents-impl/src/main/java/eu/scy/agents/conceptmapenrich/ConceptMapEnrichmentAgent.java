package eu.scy.agents.conceptmapenrich;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // TODO: change to protected
    public ConceptMapEnrichmentAgent(Map<String, Object> map) {
        super(ConceptMapEnrichmentAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        Arrays.sort(strategies);
        con = new SWATConnection();
//        con = new OntologyAgentConnection();
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

    public void findConnections(String term, String namespace) {
        try {
            String category = con.lookupEntityType(term, namespace);
            String[] newConcepts = new String[0];
            if (category.equals("individual")) {
                newConcepts = findConceptsOfInstance(term);
            } else if (category.equals("class")) {
                newConcepts = findConceptsOfClass(term);
            }
            if (newConcepts.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (String newConcept : newConcepts) {
                    sb.append("\n").append(newConcept);
                }
                JOptionPane.showMessageDialog(null, "Following connections have been found:" + sb.toString());
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    // TODO: change to private
    public Map<String, String> getOntologyTerms() {
        Map<String, String> term2Stem = new HashMap<String, String>();
        try {
            String[] terms = con.getOntologyTerms(ONTOLOGY_NS);
            for (String term : terms) {
                term2Stem.put(unCamelize(term, true), term);
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return term2Stem;
    }

    private String unCamelize(String nextToken, boolean stem) {
        Matcher m = Pattern.compile("\\p{Lu}").matcher(nextToken);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, " " + m.group());
        }
        m.appendTail(sb);
        String[] words = sb.toString().split(" ");
        sb.delete(0, sb.length());
        for (String s : words) {
            if (stem) {
                sb.append(Stemmer.stem(s)).append(" ");
            } else {
                sb.append(s).append(" ");
            }
        }
        return sb.toString().trim().toLowerCase();
    }

    public void findMatches(Map<String, String> ontologyTerms, Graph graph) {
        for (Node n : graph.getNodes()) {
            String term = ontologyTerms.get(n.getStemmedLabel());
            if (term != null) {
                findConnections(term, ONTOLOGY_NS);
            }
        }
    }
    
    private String[] findConceptsOfInstance(String entity) throws TupleSpaceException {
        ArrayList<String> concepts = new ArrayList<String>();
        String[] classes = null;
        String[] propValues = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            classes = con.getClassesOfInstance(entity, ONTOLOGY_NS);
            for (String clazz : classes) {
                concepts.add(unCamelize(entity, false) + " is a " + unCamelize(clazz, false));
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            propValues = con.getPropValuesOfInstance(entity, ONTOLOGY_NS);;
            for (String pv : propValues) {
                if (!pv.isEmpty()) {
                    String[] strings = pv.split(" ");
                    concepts.add(unCamelize(entity, false) + " " + unCamelize(strings[0], false) + " " + unCamelize(strings[1], false));
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
                                concepts.add("like " + unCamelize(entity, false) + ", " + unCamelize(i, false) + " is a " + unCamelize(clazz, false));
                            }
                        }
                    }
                }
                if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
                    String[] subClasses = con.getSubclassesOfClass(clazz, ONTOLOGY_NS);
                    if (subClasses.length > 0 && subClasses[0].length() > 0) {
                        for (String sc : subClasses) {
                            concepts.add(unCamelize(sc, false) + " is related to " + unCamelize(entity, false));
                        }
                    }
                }
            }
        }
        return (String[]) concepts.toArray(new String[concepts.size()]);
    }

    private String[] findConceptsOfClass(String entity) throws TupleSpaceException {
        ArrayList<String> concepts = new ArrayList<String>();
        String[] instances = null;
        String[] superClasses = null;
        String[] subClasses = null;
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUB) >= 0) {
            instances = con.getInstancesOfClass(entity, ONTOLOGY_NS);
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    concepts.add(unCamelize(i, false) + " is a " + entity);
                }
            }
            subClasses = con.getSubclassesOfClass(entity, ONTOLOGY_NS);
            if (subClasses.length > 0 && subClasses[0].length() > 0) {
                for (String sc : subClasses) {
                    concepts.add(unCamelize(sc, false) + " is a " + entity);
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.SUPER) >= 0) {
            superClasses = con.getSuperclassesOfClass(entity, ONTOLOGY_NS);
            if (superClasses.length > 0 && superClasses[0].length() > 0) {
                for (String sc : superClasses) {
                    concepts.add(entity + " is a " + unCamelize(sc, false));
                }
            }
        }
        if (Arrays.binarySearch(strategies, EnrichmentStrategy.RELATIONS) >= 0) {
            if (instances == null) {
                instances = con.getInstancesOfClass(entity, ONTOLOGY_NS);
            }
            if (instances.length > 0 && instances[0].length() > 0) {
                for (String i : instances) {
                    String[] propValues = con.getPropValuesOfInstance(i, ONTOLOGY_NS);
                    for (String pv : propValues) {
                        if (!pv.isEmpty()) {
                            String[] strings = pv.split(" ");
                            concepts.add("Some " + entity + " " + unCamelize(strings[0], false) + " " + unCamelize(strings[1], false));
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
                            concepts.add(unCamelize(subc, false) + " is related to " + entity);
                        }
                    }
                }
            }
        }
        return (String[]) concepts.toArray(new String[concepts.size()]);
    }

}