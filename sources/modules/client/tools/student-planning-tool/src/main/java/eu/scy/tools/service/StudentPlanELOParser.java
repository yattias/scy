package eu.scy.tools.service;

import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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

    public static String parseToXML(StudentPlanELO studentPlanELO) {
        Element rootElement = new Element("root");
        rootElement.setAttribute("id", studentPlanELO.getId());
        rootElement.getChildren().add(createElement("name", studentPlanELO.getName()));

        Element activities = createElement("studentPlannedActivities", "");
        rootElement.getChildren().add(activities);
        for (int i = 0; i < studentPlanELO.getStudentPlannedActivities().size(); i++) {
            StudentPlannedActivity activity = studentPlanELO.getStudentPlannedActivities().get(i);
            Element studentPlannedActivity = createElement("studentPlannedActivity", "");
            studentPlannedActivity.setAttribute("id", activity.getId());
            studentPlannedActivity.getChildren().add(createElement("name", activity.getName()));
            activities.getChildren().add(studentPlannedActivity);
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
                String name = rootElement.getChild("name").getText();
                String id = rootElement.getAttribute("id").getValue();
                studentPlanELO.setName(name);
                studentPlanELO.setId(id);

                Element studentPlannedActivities = rootElement.getChild("studentPlannedActivities");

                List activities= studentPlannedActivities.getChildren("studentPlannedActivity");
                for (int i = 0; i < activities.size(); i++) {
                    Element element = (Element) activities.get(i);
                    StudentPlannedActivity studentPlannedActivity = new StudentPlannedActivityImpl();
                    studentPlannedActivity.setId(element.getAttributeValue("id"));
                    studentPlannedActivity.setName(element.getChild("name").getValue());
                    studentPlanELO.addStudentPlannedActivity(studentPlannedActivity);
                }

            }
            catch (Exception e) {
                return null;
            }



        }
        return studentPlanELO;

    }

}
