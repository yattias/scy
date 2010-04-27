package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategyType;
import eu.scy.core.model.pedagogicalplan.TeacherRoleType;
import eu.scy.core.model.pedagogicalplan.WorkArrangementType;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.jsp.JspException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 10:11:59
 */
public class AjaxCombobox extends AjaxBaseComponent {


    private List comboBoxValues;

    public int doEndTag() throws JspException {
        try {
            Object selected = executeGetter(getModel(), getProperty());

            double id = Math.random();
            pageContext.getOut().write("<form id=\"ajaxComboboxForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxCombobox.html\" >");
            pageContext.getOut().write("<select name=\"value\" value=\"" + selected + "\"onchange=\"postForm('ajaxComboboxForm" + id + "');\">");
            for (int i = 0; i < comboBoxValues.size(); i++) {
                Object o = comboBoxValues.get(i);
                if (o.equals(selected)) {
                    pageContext.getOut().write("<option value=\"" + o + "\" selected>" + getLocalizedText(o) + "</option>");
                } else {
                    pageContext.getOut().write("<option value=\"" + o + "\">" + getLocalizedText(o) + "</option>");
                }
            }
            pageContext.getOut().write("</select>");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase) getModel()).getId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"setterClass\" value=\"" + selected.getClass().getName() + "\">");
            pageContext.getOut().write("</form>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getLocalizedText(Object o) {
        if (o.equals(TeacherRoleType.ACTIVATOR)) {
            return "Activator";
        } else if (o.equals(TeacherRoleType.FACILITATOR)) {
            return "Facilitator";
        } else if (o.equals(TeacherRoleType.OBSERVER)) {
            return "Observer";
        } else if (o.equals(WorkArrangementType.INDIVIDUAL)) {
            return "Individual";
        } else if (o.equals(WorkArrangementType.GROUP)) {
            return "Group";
        } else if (o.equals(WorkArrangementType.PEER_TO_PEER)) {
            return "Peer to peer";
        } else if (o.equals(AssessmentStrategyType.PEER_TO_PEER)) {
            return "Peer to peer";
        } else if (o.equals(AssessmentStrategyType.SINGLE)) {
            return "Single";
        } else if (o.equals(AssessmentStrategyType.TEACHER)) {
            return "Teacher";
        }

        return o.toString();
    }

    public List getComboBoxValues() {
        return comboBoxValues;
    }

    public void setComboBoxValues(List comboBoxValues) {
        this.comboBoxValues = comboBoxValues;
    }

    private Object executeGetter(Object object, String property) {
        if (object != null) {
            try {
                String firstLetter = property.substring(0, 1);
                firstLetter = firstLetter.toUpperCase();

                property = firstLetter + property.substring(1, property.length());

                Method method = object.getClass().getMethod("get" + property);

                Object returnValue = method.invoke(object, null);
                return returnValue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new RuntimeException("NOOO");
        } else {
            return null;
        }
    }
}
