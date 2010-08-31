package eu.scy.agents.conceptmapenrich;


public interface ConceptMapAdapter<T> {
    
    public Graph transformToGraph(T graphRepresentation);

}
