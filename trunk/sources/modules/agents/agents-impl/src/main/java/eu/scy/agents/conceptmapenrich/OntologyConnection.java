package eu.scy.agents.conceptmapenrich;

import info.collide.sqlspaces.commons.TupleSpaceException;

public interface OntologyConnection {

    public String[] getOntologyTerms(String namespace) throws TupleSpaceException;

    public String[] getClassesOfInstance(String entity, String namespace) throws TupleSpaceException;

    public String[][] getPropValuesOfInstance(String entity, String namespace) throws TupleSpaceException;

    public String lookupEntityType(String entity, String namespace) throws TupleSpaceException;

    public String[] getSubclassesOfClass(String entity, String namespace) throws TupleSpaceException;

    public String[] getSuperclassesOfClass(String entity, String namespace) throws TupleSpaceException;

    public String[] getInstancesOfClass(String entity, String namespace) throws TupleSpaceException;

    public void close();

}
