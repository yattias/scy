package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.mai.2011
 * Time: 10:50:10
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionQuestion extends BaseXMLTransfer {

    private String reflectionQuestionTitle;
    private String reflectionQuestion;
    private String anchorEloURI;
    private String anchorEloName;
    private String type;

    public String getReflectionQuestion() {
        return reflectionQuestion;
    }

    public void setReflectionQuestion(String reflectionQuestion) {
        this.reflectionQuestion = reflectionQuestion;
    }

    public String getAnchorEloURI() {
        return anchorEloURI;
    }

    public void setAnchorEloURI(String anchorEloURI) {
        this.anchorEloURI = anchorEloURI;
    }

    public String getAnchorEloName() {
        return anchorEloName;
    }

    public void setAnchorEloName(String anchorEloName) {
        this.anchorEloName = anchorEloName;
    }

    public String getReflectionQuestionTitle() {
        return reflectionQuestionTitle;
    }

    public void setReflectionQuestionTitle(String reflectionQuestionTitle) {
        this.reflectionQuestionTitle = reflectionQuestionTitle;
    }

    public String getType() {
        if(type == null || type.equals("")) return "text";
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
