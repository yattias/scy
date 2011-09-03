package eu.scy.server.controllers;

import eu.scy.core.model.transfer.AnchorEloTransfer;
import eu.scy.core.model.transfer.ReflectionQuestion;
import eu.scy.core.model.transfer.TransferElo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2011
 * Time: 20:21:13
 * To change this template use File | Settings | File Templates.
 */
public class AnchorEloReflectionQuestionTransporter {

    private TransferElo anchorElo;
    private List<ReflectionQuestion> reflectionQuestions;


    public TransferElo getAnchorElo() {
        return anchorElo;
    }

    public void setAnchorElo(TransferElo anchorElo) {
        this.anchorElo = anchorElo;
    }

    public List<ReflectionQuestion> getReflectionQuestions() {
        return reflectionQuestions;
    }

    public void setReflectionQuestions(List<ReflectionQuestion> reflectionQuestions) {
        this.reflectionQuestions = reflectionQuestions;
    }

    public void addReflectionQuestion(ReflectionQuestion reflectionQuestion) {
        getReflectionQuestions().add(reflectionQuestion);
    }
}
