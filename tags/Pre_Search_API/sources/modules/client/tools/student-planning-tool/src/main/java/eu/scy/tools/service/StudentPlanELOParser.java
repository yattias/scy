package eu.scy.tools.service;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.aug.2010
 * Time: 08:39:32
 * To change this template use File | Settings | File Templates.
 */
public class StudentPlanELOParser {

    public static final String MEMBERS = "members";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String START_DATE = "startdate";
    public static final String END_DATE = "enddate";
    public static final String NOTES = "notes";
    public static final String DESCRIPTION = "description";
    public static final String ANCHORELO = "anchorelo";

    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static String parseToXML(StudentPlanELO studentPlanELO) {
        Element rootElement = new Element("root");
        rootElement.setAttribute(ID, studentPlanELO.getId());
        rootElement.getChildren().add(createElement(NAME, studentPlanELO.getName()));

        Element activities = createElement("studentPlannedActivities", "");
        rootElement.getChildren().add(activities);
        for (int i = 0; i < studentPlanELO.getStudentPlannedActivities().size(); i++) {
            StudentPlannedActivity activity = studentPlanELO.getStudentPlannedActivities().get(i);
            Element studentPlannedActivityElement = createElement("studentPlannedActivity", "");

            studentPlannedActivityElement.setAttribute(ID, activity.getId());
            // System.out.println("ACTIVITY ID:  " + activity.getId());
            studentPlannedActivityElement.getChildren().add(createElement(NAME, activity.getName()));
            if (activity.getStartDate() != null)
                studentPlannedActivityElement.getChildren().add(createElement(START_DATE, dateFormat.format(activity.getStartDate())));
            if (activity.getEndDate() != null)
                studentPlannedActivityElement.getChildren().add(createElement(END_DATE, dateFormat.format(activity.getEndDate())));
            studentPlannedActivityElement.getChildren().add(createElement(NOTES, activity.getNote()));
            studentPlannedActivityElement.getChildren().add(createElement(DESCRIPTION, activity.getDescription()));

            activities.getChildren().add(studentPlannedActivityElement);

            addMembers(studentPlannedActivityElement, activity);
            addAnchorELOs(studentPlannedActivityElement, activity);
        }


        Document doc = new Document(rootElement);
        XMLOutputter outp = new XMLOutputter();
        try {
            StringWriter writer = new StringWriter();
            outp.output(doc.getRootElement(), writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc.getRootElement().getText();
    }

    private static void addAnchorELOs(Element studentPlannedActivityElement, StudentPlannedActivity activity) {
        AnchorELO anchorElo = activity.getAssoicatedELO();
        if (anchorElo != null) {
            Element anchorEloElement = createElement(ANCHORELO, "");
            anchorEloElement.getChildren().add(createElement(NAME, anchorElo.getName()));
            anchorEloElement.getChildren().add(createElement(ID, anchorElo.getId()));

            studentPlannedActivityElement.getChildren().add(anchorEloElement);
        }


    }

    private static void addMembers(Element studentPlannedActivityElement, StudentPlannedActivity activity) {
        Element membersElement = createElement(MEMBERS, "");
        for (int i = 0; i < activity.getMembers().size(); i++) {
            SCYUserImpl scyUser = (SCYUserImpl) activity.getMembers().get(i);
            Element memberElement = createElement("member", "");
            if (scyUser != null) {
                memberElement.getChildren().add(createElement(USERNAME, scyUser.getUserDetails().getUsername()));
                memberElement.getChildren().add(createElement(PASSWORD, scyUser.getUserDetails().getPassword()));
                membersElement.getChildren().add(memberElement);
            }

        }

        studentPlannedActivityElement.getChildren().add(membersElement);

    }

    private static Element createElement(String nodeName, String name) {
        Element element = new Element(nodeName);
        element.setText(name);
        return element;
    }

    public static StudentPlanELO parseFromXML(String studentPlanELOXML) {
        StudentPlanELO studentPlanELO = new StudentPlanELOImpl();
        if (studentPlanELOXML != null && studentPlanELOXML.length() > 0) {

            SAXBuilder builder = new SAXBuilder(false);
            StringReader stringReader = new StringReader(studentPlanELOXML);
            try {
                Document doc = builder.build(stringReader);
                Element rootElement = doc.getRootElement();
                String name = rootElement.getChild(NAME).getText();
                String id = rootElement.getAttribute(ID).getValue();
                studentPlanELO.setName(name);
                studentPlanELO.setId(id);

                Element studentPlannedActivities = rootElement.getChild("studentPlannedActivities");

                List activities = studentPlannedActivities.getChildren("studentPlannedActivity");
                for (int i = 0; i < activities.size(); i++) {
                    Element element = (Element) activities.get(i);
                    StudentPlannedActivity studentPlannedActivity = new StudentPlannedActivityImpl();

                    studentPlannedActivity.setId(element.getAttributeValue(ID));
                    studentPlannedActivity.setName(getElementValue(element, NAME));
                    studentPlannedActivity.setNote(getElementValue(element, NOTES));
                    studentPlannedActivity.setDescription(getElementValue(element, DESCRIPTION));

                    Element startDateElement = element.getChild(START_DATE);
                    Element endDateElement = element.getChild(END_DATE);


                    if (startDateElement != null) {
                        java.util.Date uDate = dateFormat.parse(startDateElement.getText());
                        studentPlannedActivity.setStartDate(new Date(uDate.getTime()));
                    }

                    if (endDateElement != null) {
                        java.util.Date uDate = dateFormat.parse(endDateElement.getText());
                        studentPlannedActivity.setEndDate(new Date(uDate.getTime()));
                    }


                    studentPlanELO.addStudentPlannedActivity(studentPlannedActivity);


                    parseMembers(element, studentPlannedActivity);
                    parseAnchorELO(element, studentPlannedActivity);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
        return studentPlanELO;

    }

    private static String getElementValue(Element element, String name) {
        if (element.getChild(name) != null) return element.getChild(name).getText();
        return null;
    }

    private static void parseAnchorELO(Element element, StudentPlannedActivity studentPlannedActivity) {
        Element anchorELOElement = element.getChild(ANCHORELO);
        if (anchorELOElement != null) {
            AnchorELO anchorELO = new AnchorELOImpl();
            anchorELO.setId(anchorELOElement.getChild(ID).getText());
            anchorELO.setName(anchorELOElement.getChild(NAME).getText());
            studentPlannedActivity.setAssoicatedELO(anchorELO);
        }
    }

    private static void parseMembers(Element studentPlannedActivityElement, StudentPlannedActivity studentPlannedActivity) {
        Element membersElement = studentPlannedActivityElement.getChild(MEMBERS);
        if (membersElement != null) {
            for (int i = 0; i < membersElement.getChildren().size(); i++) {
                Element memberElement = (Element) membersElement.getChildren().get(i);
                studentPlannedActivity.addMember(parseUser(memberElement));

            }

        }
    }

    private static User parseUser(Element memberElement) {
        SCYUserImpl scyUser = new SCYUserImpl();
        SCYUserDetails scyUserDetails = new SCYUserDetails();
        scyUser.setUserDetails(scyUserDetails);
        scyUserDetails.setUsername(memberElement.getChild(USERNAME).getText());
        scyUserDetails.setPassword(memberElement.getChild(PASSWORD).getText());
        return scyUser;
    }

}
