package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.node.NodeModel;


public class OptionNodeModel extends NodeModel {

    private String option;
    private int score;
    
    public OptionNodeModel() {
        setTitle("Option");
        option = "";
        score = 0;
    }
    
    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public OptionNodeModel createClone() {
        OptionNodeModel clone = new OptionNodeModel();
        clone.setOption(getOption());
        clone.setScore(getScore());
        return clone;
    }

}
