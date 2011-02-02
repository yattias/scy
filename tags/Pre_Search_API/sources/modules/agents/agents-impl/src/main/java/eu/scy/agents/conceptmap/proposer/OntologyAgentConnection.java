package eu.scy.agents.conceptmap.proposer;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Callback.Command;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.server.UID;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import eu.scy.agents.impl.AgentProtocol;

public class OntologyAgentConnection implements OntologyConnection {

    private TupleSpace ts;
    private Tuple lastTuple;
    private Object lastQuery;

    public OntologyAgentConnection() {
        try {
            ts = new TupleSpace(new User("ConceptEnrichmentAgent"), "scy.collide.info", 2525, AgentProtocol.COMMAND_SPACE_NAME);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        TupleSpace ts = new TupleSpace(new User("Debugger"), "scy.collide.info", 2525, false, false, AgentProtocol.COMMAND_SPACE_NAME);
        Callback cback = new Callback() {
            
            @Override
            public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
                String string = cmd + ": " + afterTuple + " / " + beforeTuple;
                if (!string.contains("alive")) {
                    // System.out.println(string);
                }
            }
        };
        ts.eventRegister(Command.ALL, new Tuple(), cback, true);
    }
    
    @Override
    public String[] getOntologyTerms(String namespace) throws TupleSpaceException {
        Tuple result = queryOntoAgent("entities", namespace);
        String list = result.getField(2).getValue().toString();
        String[] terms = list.split(",");
        return terms;
    }


    private Tuple queryOntoAgent(String service, String namespace, String... params) throws TupleSpaceException {
        String query = service + namespace + Arrays.toString(params);
        if (query.equals(lastQuery)) {
            return lastTuple;
        }
        lastQuery = query;
        String id = new UID().toString();
        Tuple t = new Tuple(id, "onto", service, namespace);
        for (String param : params) {
            t.add(param);
        }
        // System.out.println("Querying onto for " + service + " with tuple: " + t);
        ts.write(t);
        Tuple result = ts.waitToTake(new Tuple(id, "response", Field.createWildCardField()), 10 * 1000);
        if (result == null) {
            System.err.println("PROBLEM: Tuple with id '" + id + "' not found!");
        }
        lastTuple = result;
        return result;
    }

    @Override
    public String[] getClassesOfInstance(String entity, String namespace) throws TupleSpaceException {
        Tuple result = queryOntoAgent("instance info", namespace, entity);
        String[] classes = result.getField(2).getValue().toString().split(",");
        return classes;
    }

    @Override
    public String[][] getPropValuesOfInstance(String entity, String namespace) throws TupleSpaceException {
        Tuple result = queryOntoAgent("instance info", namespace, entity);
        String[] propValues = result.getField(3).getValue().toString().split(",");
        String[][] output = new String[propValues.length][];
        for (int i = 0; i < propValues.length; i++) {
            output[i] = new String[2]; 
            output[i][0] = propValues[i].substring(0, propValues[i].indexOf(' '));
            output[i][1] = propValues[i].substring(propValues[i].indexOf(' ') + 1);
        }
        return output;
    }
    
    @Override
    public String[] getSubclassesOfClass(String entity, String namespace) throws TupleSpaceException {
        Tuple result = queryOntoAgent("class info", namespace, entity);
        String[] subClasses = result.getField(4).getValue().toString().split(",");
        return subClasses;
    }

    @Override
    public String[] getSuperclassesOfClass(String entity, String namespace) throws TupleSpaceException {
        Tuple result = queryOntoAgent("class info", namespace, entity);
        String[] superClasses = result.getField(3).getValue().toString().split(",");
        return superClasses;
    }
    
    @Override
    public String[] getInstancesOfClass(String entity, String namespace) throws TupleSpaceException {
        Tuple result = queryOntoAgent("class info", namespace, entity);
        String[] instances = result.getField(2).getValue().toString().split(",");
        return instances;
    }

    @Override
    public String lookupEntityType(String entity, String namespace) throws TupleSpaceException {
        Tuple result = queryOntoAgent("lookup", namespace, entity);
        String category = result.getField(2).getValue().toString();
        return category;
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public String getOntologyNamespace() {
        // TODO
        return null;
    }

    @Override
    public String getLanguage() {
        // TODO
        return null;
    }

    @Override
    public Map<String, Set<String>> getOntologyClouds(String namespace) throws TupleSpaceException {
        // TODO
        return null;
    }

    @Override
    public Map<String, Set<String>> getRelationHierarchy() throws TupleSpaceException {
        // TODO
        return null;
    }

}
