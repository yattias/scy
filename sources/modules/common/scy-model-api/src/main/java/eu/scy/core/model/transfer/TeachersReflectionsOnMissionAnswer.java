package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.sep.2011
 * Time: 01:08:17
 * To change this template use File | Settings | File Templates.
 */
public class TeachersReflectionsOnMissionAnswer extends BaseXMLTransfer {

    private String teacherQuestionToMissionId;
    private String teacherQuestionToMission;
    private String answer;

    public String getTeacherQuestionToMissionId() {
        return teacherQuestionToMissionId;
    }

    public void setTeacherQuestionToMissionId(String teacherQuestionToMissionId) {
        this.teacherQuestionToMissionId = teacherQuestionToMissionId;
    }

    public String getTeacherQuestionToMission() {
        return teacherQuestionToMission;
    }

    public void setTeacherQuestionToMission(String teacherQuestionToMission) {
        this.teacherQuestionToMission = teacherQuestionToMission;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
