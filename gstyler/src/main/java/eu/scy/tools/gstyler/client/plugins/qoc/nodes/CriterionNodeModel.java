package eu.scy.tools.gstyler.client.plugins.qoc.nodes;


public class CriterionNodeModel extends QOCNodeModel {

    private int relevance;
    
    public CriterionNodeModel() {
        setTitle("Criterion");
        relevance = 100;
    }
    
    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    @Override
    public CriterionNodeModel createClone() {
        CriterionNodeModel clone = new CriterionNodeModel();
        clone.setDescription(getDescription());
        clone.setRelevance(getRelevance());
        return clone;
    }
}
