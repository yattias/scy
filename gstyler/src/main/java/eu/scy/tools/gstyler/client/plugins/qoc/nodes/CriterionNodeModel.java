package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.node.NodeModel;


public class CriterionNodeModel extends NodeModel {

    private String criterion;
    private int relevance;
    
    public CriterionNodeModel() {
        setTitle("Criterion");
        criterion = "";
        relevance = 100;
    }
    
    public String getCriterion() {
        return criterion;
    }
    
    public void setCriterion(String criterion) {
        this.criterion = criterion;
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
        clone.setCriterion(getCriterion());
        clone.setRelevance(getRelevance());
        return clone;
    }
}
