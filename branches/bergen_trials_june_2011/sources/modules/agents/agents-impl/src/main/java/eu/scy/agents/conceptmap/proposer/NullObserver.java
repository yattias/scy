package eu.scy.agents.conceptmap.proposer;

import eu.scy.agents.conceptmap.Graph;

public class NullObserver implements CMProposerObserver {

    @Override
    public void setCMText(String text) {}

    @Override
    public void foundTextKeyword(String keyword) {}

    @Override
    public void foundOntoConcept(String concept) {}

    @Override
    public void foundOntoRelation(String from, String to, String label) {}

    @Override
    public void foundStudentsMap(Graph g) {}

    @Override
    public void setStatusText(String statusText) {}

    @Override
    public void clearState() {}

    @Override
    public void markConceptAsMatching(String concept) {}

    @Override
    public void markConceptAsProposal(String concept) {}

    @Override
    public void foundTextCloudKeyword(String term) {}

    @Override
    public void markRelationAsProposal(String relation) {}

}
