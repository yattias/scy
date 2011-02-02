package eu.scy.server.taglibs;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2010
 * Time: 12:35:43
 * To change this template use File | Settings | File Templates.
 */
public class AjaxELOSlider extends BaseAjaxELOComponent{
     private List sliderValues;
    private String defaultValue;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<form id=\"ajaxSliderForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxELOSlider.html\">");
            pageContext.getOut().write("<div id=\"horizontalSlider"+ id + "\" dojoType=\"dijit.form.HorizontalSlider\" value=\""+executeGetter(getEloURI(), getProperty())+"\"\n" +
                    "minimum=\"0\" maximum=\"" + (sliderValues.size()-1) + "\" discreteValues=\"" + sliderValues.size() + "\" intermediateChanges=\"true\"\n" +
                    "showButtons=\"false\" style=\"width:400px;\" onChange=\"updateAjaxSlider('" + id + "', this);\">\n" +
                    "    <ol dojoType=\"dijit.form.HorizontalRuleLabels\" container=\"topDecoration\"\n" +
                    "    style=\"height:1.5em;font-size:75%;color:gray;\">");
            for (int i = 0; i < sliderValues.size(); i++) {
                Object o = sliderValues.get(i);
                pageContext.getOut().write("<li>" + o.toString() + "</li>");
            }
            pageContext.getOut().write("<div dojoType=\"dijit.form.HorizontalRule\" container=\"bottomDecoration\"\n" +
                    "    count=" + sliderValues.size() + " style=\"height:5px;\">\n" +
                    "    </div>\n" +
                    "    <ol dojoType=\"dijit.form.HorizontalRuleLabels\" container=\"bottomDecoration\"\n" +
                    "    style=\"height:1em;font-size:75%;color:gray;\">" +
                    "</ol></div>");
            pageContext.getOut().write("<input type=\"text\" name=\"value\" id=\"sliderValue" + id + "\" dojoType=\"dijit.form.TextBox\" style=\"display:none;\"/>");
            pageContext.getOut().write("<input type=\"hidden\" name=\"uri\" value=\"" + getEloURI() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("</form>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }



    public List getSliderValues() {
        return sliderValues;
    }

    public void setSliderValues(List sliderValues) {
        this.sliderValues = sliderValues;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
