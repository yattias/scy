package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.mai.2011
 * Time: 10:50:10
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionQuestion extends BaseXMLTransfer {

    private String reflectionQuestion;
    private String anchorEloURI;
    private String anchorEloName;

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
}
