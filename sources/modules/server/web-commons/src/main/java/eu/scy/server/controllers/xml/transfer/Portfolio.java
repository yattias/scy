package eu.scy.server.controllers.xml.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:46:35
 * To change this template use File | Settings | File Templates.
 */
public class Portfolio {

    private String student;
    private String missionName;
    private String reflectionMission;
    private String reflectionCollaboration;
    private String reflectionInquiry;
    private String reflectionEffort;
    private Boolean assessed = Boolean.FALSE;

    private List<TransferElo> elos;

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getReflectionMission() {
        return reflectionMission;
    }

    public void setReflectionMission(String reflectionMission) {
        this.reflectionMission = reflectionMission;
    }

    public String getReflectionCollaboration() {
        return reflectionCollaboration;
    }

    public void setReflectionCollaboration(String reflectionCollaboration) {
        this.reflectionCollaboration = reflectionCollaboration;
    }

    public String getReflectionInquiry() {
        return reflectionInquiry;
    }

    public void setReflectionInquiry(String reflectionInquiry) {
        this.reflectionInquiry = reflectionInquiry;
    }

    public String getReflectionEffort() {
        return reflectionEffort;
    }

    public void setReflectionEffort(String reflectionEffort) {
        this.reflectionEffort = reflectionEffort;
    }

    public Boolean getAssessed() {
        return assessed;
    }

    public void setAssessed(Boolean assessed) {
        this.assessed = assessed;
    }

    public List<TransferElo> getElos() {
        return elos;
    }

    public void setElos(List<TransferElo> elos) {
        this.elos = elos;
    }

    public void addElo(TransferElo transferElo) {
        if(getElos() == null) setElos(new LinkedList());
        getElos().add(transferElo);
    }
}
