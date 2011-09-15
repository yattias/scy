package eu.scy.server.controllers;

import eu.scy.core.model.transfer.RubricForElo;
import eu.scy.core.model.transfer.TeacherQuestionToElo;
import eu.scy.core.model.transfer.TransferElo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2011
 * Time: 21:07:06
 * To change this template use File | Settings | File Templates.
 */
public class AnchorEloReflectionQuestionFOrTeacherTransporter {

    private TransferElo anchorElo;
    private List<TeacherQuestionToElo> teacherQuestionToElos = new LinkedList<TeacherQuestionToElo>();
    private List<RubricForElo> rubricForElos = new LinkedList<RubricForElo>();

    public TransferElo getAnchorElo() {
        return anchorElo;
    }

    public void setAnchorElo(TransferElo anchorElo) {
        this.anchorElo = anchorElo;
    }

    public List<TeacherQuestionToElo> getTeacherQuestionToElos() {
        return teacherQuestionToElos;
    }

    public void setTeacherQuestionToElos(List<TeacherQuestionToElo> teacherQuestionToElos) {
        this.teacherQuestionToElos = teacherQuestionToElos;
    }

    public List<RubricForElo> getRubricForElos() {
        return rubricForElos;
    }

    public void setRubricForElos(List<RubricForElo> rubricForElos) {
        this.rubricForElos = rubricForElos;
    }
}
