package eu.scy.core.model.transfer;

import eu.scy.common.scyelo.ScyElo;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    private String reflectionQuestion;

    private String grade;

    private RawData rawData;

    private String feedbackEloUrl;
    private String feedbackEloURI;

    private String snippeturl = "";


    public TransferElo() {
    }

    public TransferElo(ScyElo scyElo) {
        super();
        setMyname(scyElo.getTitle());
        setUri(scyElo.getUri().toString());
        if (scyElo.getDateLastModified() != null) {
            Date lastModified = new Date(scyElo.getDateLastModified());
            setLastModified(lastModified.toString());
            setModified(lastModified.toString());
        }
        if (scyElo.getDateCreated() != null) {
            Date createDate = new Date(scyElo.getDateCreated());
            setCreatedDate(createDate.toString());
        }

        List authors = scyElo.getAuthors();
        String authorString = "";
        for (int i = 0; i < authors.size(); i++) {
            String s = (String) authors.get(i);
            authorString += s + " ";

        }
        setCreatedBy(authorString.trim());
        setCatname(scyElo.getTitle());

        if (getRawData() == null) {
            setRawData(new RawData());
        }

        setTechnicalFormat(String.valueOf(scyElo.getTechnicalFormat()));
        setThumbnail("/webapp/components/resourceservice.html?eloURI=" + getUri());

        getRawData().setThumbnail("/webapp/components/resourceservice.html?eloURI=" + getUri());//Why go for less when you can have the double?
        getRawData().setFullScreen("/webapp/components/resourceservice.html?eloURI=" + getUri());
        if (getTechnicalFormat() != null) {
            if (getTechnicalFormat().contains("text") ||
                    getTechnicalFormat().contains("rtf")) {
                String rtfString = scyElo.getContent().getXmlString();
                if (rtfString != null && rtfString.length() > 0) {
                    rtfString = rtfString.replaceAll("<RichText>", "");
                    rtfString = rtfString.replaceAll("</RichText>", "");
                    rtfString = convertRtfToHtml(rtfString);
                }

                getRawData().setText(rtfString);
            }
        }


        setStudentDescription("stydentdesc");
        try {
            setSnippeturl("scy-lab.jnlp?singleEloUri=" + URLEncoder.encode(getUri(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public void setFeedbackELO(ScyElo scyElo) {

        String uri = scyElo.getUri().toString();
        try {
            uri = URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("SET FEEDBACK ELO: " + scyElo + " URI: " + uri);
        setFeedbackEloUrl("/webapp/app/feedback/xml/feedbackEloService.html?feedbackURI=" + uri);
        setFeedbackEloURI(uri);
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

    public String getFeedbackEloUrl() {
        return feedbackEloUrl;
    }

    public void setFeedbackEloUrl(String feedbackEloUrl) {
        this.feedbackEloUrl = feedbackEloUrl;
    }

    public String getFeedbackEloURI() {
        return feedbackEloURI;
    }

    public void setFeedbackEloURI(String feedbackEloURI) {
        this.feedbackEloURI = feedbackEloURI;
    }

    public static String convertRtfToHtml(final String txt) {
        return "";
        /*//System.out.println("CONVERTING RICH TEXT TO HTML!");
        try {
            final RTFEditorKit rtf_edit = new RTFEditorKit();
            final JTextPane jtp_rtf = new JTextPane();
            final JTextPane jtp_html = new JTextPane();
            final StyleContext rtf_context = new StyleContext();
            final DefaultStyledDocument rtf_doc = new DefaultStyledDocument(rtf_context);
            jtp_rtf.setEditorKit(rtf_edit);
            jtp_rtf.setContentType("text/rtf");
            jtp_html.setContentType("text/html");

            rtf_edit.read(new StringReader(txt), rtf_doc, 0);
            jtp_rtf.setDocument(rtf_doc);
            jtp_html.setText(rtf_doc.getText(0, rtf_doc.getLength()));
            HTMLDocument html_doc = null;
            for (int i = 0; i < rtf_doc.getLength(); i++) {
                AttributeSet a = rtf_doc.getCharacterElement(i).getAttributes();
                AttributeSet p = rtf_doc.getParagraphElement(i).getAttributes();
                String s = jtp_rtf.getText(i, 1);
                jtp_html.select(i, i + 1);
                jtp_html.replaceSelection(s);
                html_doc = (HTMLDocument) jtp_html.getDocument();
                html_doc.putProperty("", "");
                html_doc.setCharacterAttributes(i, 1, a, false);
                MutableAttributeSet attr = new SimpleAttributeSet(p);
                html_doc.setParagraphAttributes(i, 1, attr, false);
            }
            StringWriter writer = new StringWriter();
            final HTMLEditorKit html_edit = new HTMLEditorKit();
            html_edit.write(writer, html_doc, 0, html_doc.getLength());
            String raw =  writer.toString();
            //return URLEncoder.encode(raw, "UTF-8");
            System.out.println("RAW: " + raw);
            
            return raw;
        } catch (Exception ex) {
            return txt;
        }  */
    }

    public String getReflectionQuestion() {
        return reflectionQuestion;
    }

    public void setReflectionQuestion(String reflectionQuestion) {
        this.reflectionQuestion = reflectionQuestion;
    }

    public String getSnippeturl() {
        return snippeturl;
    }

    public void setSnippeturl(String snippeturl) {
        this.snippeturl = snippeturl;
    }
}
