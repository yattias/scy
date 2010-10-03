package eu.scy.agents.conceptmap.model;

import eu.scy.agents.conceptmap.Graph;


public interface ConceptMapAdapter<T> {
    
    public Graph transformToGraph(T graphRepresentation);

}
