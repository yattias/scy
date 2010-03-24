package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 11:02:27
 * To change this template use File | Settings | File Templates.
 */
public class AjaxSlider extends AjaxBaseComponent{

    private List sliderValues;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<form id=\"ajaxComboboxForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxCombobox.html\">");
            pageContext.getOut().write("SLIDER HERE");
            for (int i = 0; i < sliderValues.size(); i++) {
                Object o = sliderValues.get(i);
                pageContext.getOut().write("[" + o.toString() + "] ");
            }
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
}
