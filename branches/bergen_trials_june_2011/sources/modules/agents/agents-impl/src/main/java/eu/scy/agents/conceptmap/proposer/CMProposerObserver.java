package eu.scy.agents.conceptmap.proposer;

import eu.scy.agents.conceptmap.Graph;

public interface CMProposerObserver {

    public void setCMText(String text);

    public void foundOntoConcept(String concept);

    public void foundOntoRelation(String from, String to, String label);
    
    public void foundStudentsMap(Graph g);
    
    public void setStatusText(String statusText);

    public void clearState();

    public void foundTextKeyword(String term);
    
    public void markConceptAsMatching(String concept);
    
    public void markConceptAsProposal(String concept);

    public void foundTextCloudKeyword(String term);

    public void markRelationAsProposal(String relation);
}
