package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.node.NodeModel;


public class QuestionNodeModel extends NodeModel{

    private String question;
    
    public QuestionNodeModel() {
        setTitle("Question");
        question = "";
    }
    
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    
    @Override
    public QuestionNodeModel createClone() {
        QuestionNodeModel clone = new QuestionNodeModel();
        clone.setQuestion(getQuestion());
        return clone;
    }
}
