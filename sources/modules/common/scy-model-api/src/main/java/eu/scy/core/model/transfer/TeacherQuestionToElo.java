package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2011
 * Time: 20:55:34
 * To change this template use File | Settings | File Templates.
 */
public class TeacherQuestionToElo extends BaseXMLTransfer {

    private String eloURI;
    private String questionTitle;
    private String question;
    private String questionType;

    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
