package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2011
 * Time: 07:22:05
 * To change this template use File | Settings | File Templates.
 */
public class EloReflectionQuestionAnswers {

    private String eloURI;
    private String reflectionQuestionId;
    private String questionAnswer;

    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    public String getReflectionQuestionId() {
        return reflectionQuestionId;
    }

    public void setReflectionQuestionId(String reflectionQuestionId) {
        this.reflectionQuestionId = reflectionQuestionId;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
}
