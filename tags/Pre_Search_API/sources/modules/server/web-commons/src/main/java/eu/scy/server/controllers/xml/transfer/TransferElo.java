package eu.scy.server.controllers.xml.transfer;

import eu.scy.common.scyelo.ScyElo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:49:17
 * To change this template use File | Settings | File Templates.
 */
public class TransferElo extends BaseXMLTransfer {

    private String uri;
    private String catname;
    private String thumbnail;
    private String fullsize;
    private String myname;
    private String modified;
    private String studentDescription;
    private String technicalFormat;

    private List generalLearningGoals = new LinkedList();
    private List specificLearningGoals = new LinkedList();

    private String studentReflection;
    private String studentInquiry;
    private Boolean assessed = Boolean.FALSE;
    private String createdDate;
    private String lastModified;
    private String createdBy;
    private String eloId;

    private String assessmentComment;
    private String reflectionComment;

    private Boolean hasBeenReflectedOn = Boolean.FALSE;
    private Boolean hasBeenSelectedForSubmit = Boolean.FALSE;
    private String inquiryQuestion;

    private String grade;

    private RawData rawData;


    public TransferElo() {
    }

    public TransferElo(ScyElo scyElo) {
        super();
        setMyname(scyElo.getTitle());
        setUri(scyElo.getUri().toString());
        Date lastModified = new Date(scyElo.getDateLastModified());
        Date createDate = new Date(scyElo.getDateCreated());
        setLastModified(lastModified.toString());
        setModified(lastModified.toString());
        setCreatedDate(createDate.toString());
        List authors = scyElo.getAuthors();
        String authorString = "";
        for (int i = 0; i < authors.size(); i++) {
            String s = (String) authors.get(i);
            authorString += s + " ";

        }
        setCreatedBy(authorString);
        setCatname(scyElo.getTitle());

        if (getRawData() == null) {
            setRawData(new RawData());
        }

        setTechnicalFormat(scyElo.getTechnicalFormat());
        setThumbnail("/webapp/components/resourceservice.html?eloURI=" + getUri());

        getRawData().setThumbnail("/webapp/components/resourceservice.html?eloURI=" + getUri());//Why go for less when you can have the double?
        getRawData().setFullScreen("/webapp/components/resourceservice.html?eloURI=" + getUri());
        if (getTechnicalFormat() != null) {
            if (getTechnicalFormat().contains("text") ||
                    getTechnicalFormat().contains("rtf")) {
                getRawData().setText(scyElo.getContent().getXmlString());
            }
        }


        setStudentDescription("stydentdesc");


    }


    public String getUri() {
        try {
            return URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            return uri;
        }
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getThumbnail() {
         try {
            return URLEncoder.encode(thumbnail, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            return thumbnail;
        }
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFullsize() {
         try {
            return URLEncoder.encode(fullsize, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            return fullsize;
        }
    }

    public void setFullsize(String fullsize) {
        this.fullsize = fullsize;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getStudentDescription() {
        return studentDescription;
    }

    public void setStudentDescription(String studentDescription) {
        this.studentDescription = studentDescription;
    }


    public void addGeneralLearningGoal(LearningGoal learningGoal) {
        getGeneralLearningGoals().add(learningGoal);
    }

    public List getGeneralLearningGoals() {
        return generalLearningGoals;
    }

    public void setGeneralLearningGoals(List generalLearningGoals) {
        this.generalLearningGoals = generalLearningGoals;
    }

    public void addSpecificLearningGoal(LearningGoal learningGoal) {
        getSpecificLearningGoals().add(learningGoal);
    }

    public List getSpecificLearningGoals() {
        return specificLearningGoals;
    }

    public void setSpecificLearningGoals(List specificLearningGoals) {
        this.specificLearningGoals = specificLearningGoals;
    }

    public String getStudentReflection() {
        return studentReflection;
    }

    public void setStudentReflection(String studentReflection) {
        this.studentReflection = studentReflection;
    }

    public String getStudentInquiry() {
        return studentInquiry;
    }

    public void setStudentInquiry(String studentInquiry) {
        this.studentInquiry = studentInquiry;
    }

    public Boolean getAssessed() {
        return assessed;
    }

    public void setAssessed(Boolean assessed) {
        this.assessed = assessed;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAssessmentComment() {
        return assessmentComment;
    }

    public void setAssessmentComment(String assessmentComment) {
        this.assessmentComment = assessmentComment;
    }

    public String getReflectionComment() {
        return reflectionComment;
    }

    public void setReflectionComment(String reflectionComment) {
        this.reflectionComment = reflectionComment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String cratedDate) {
        this.createdDate = cratedDate;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEloId() {
        return getId();
    }

    public Boolean getHasBeenReflectedOn() {
        return hasBeenReflectedOn;
    }

    public void setHasBeenReflectedOn(Boolean hasBeenReflectedOn) {
        this.hasBeenReflectedOn = hasBeenReflectedOn;
    }

    public Boolean getHasBeenSelectedForSubmit() {
        return hasBeenSelectedForSubmit;
    }

    public void setHasBeenSelectedForSubmit(Boolean hasBeenSelectedForSubmit) {
        this.hasBeenSelectedForSubmit = hasBeenSelectedForSubmit;
    }

    public String getInquiryQuestion() {
        return inquiryQuestion;
    }

    public void setInquiryQuestion(String inquiryQuestion) {
        this.inquiryQuestion = inquiryQuestion;
    }

    public String getTechnicalFormat() {
        return technicalFormat;
    }

    public void setTechnicalFormat(String technicalFormat) {
        this.technicalFormat = technicalFormat;
    }

    public RawData getRawData() {
        return rawData;
    }

    public void setRawData(RawData rawData) {
        this.rawData = rawData;
    }
}
