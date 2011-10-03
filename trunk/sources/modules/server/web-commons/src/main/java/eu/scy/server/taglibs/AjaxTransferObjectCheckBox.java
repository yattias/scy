package eu.scy.server.taglibs;

import eu.scy.core.model.transfer.AnchorEloTransfer;
import eu.scy.core.model.transfer.BaseXMLTransfer;
import eu.scy.server.util.TransferObjectServiceCollection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2011
 * Time: 23:27:02
 * To change this template use File | Settings | File Templates.
 */
public class AjaxTransferObjectCheckBox extends TagSupport {


    private String transferEloURI;
    private String id;
    private String property;
    private BaseXMLTransfer transferObject;
    private TransferObjectServiceCollection transferObjectServiceCollection;


    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<form id=\"checkboxForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxTransferObjectCheckBoxController.html\">");
            //pageContext.getOut().write("<input id=\"ajaxCheckBox" + id + "\" name=\"value\" value=\"true\" dojoType=\"dijit.form.CheckBox\" onChange=\"postForm('checkboxForm"+ id + "');\" "  +  getChecked() + " >");
            pageContext.getOut().write("<input id=\"ajaxCheckBox" + id + "\" name=\"ajaxCheckBoxValue\" value=\"true\" dojoType=\"dijit.form.CheckBox\" onChange=\"document.getElementById('ajaxCheckBoxValue"+id+"').value = this.checked; postForm('checkboxForm"+ id + "');\" "  +  getChecked() + " >");

            pageContext.getOut().write("<input type=\"hidden\" name=\"value\" id=\"ajaxCheckBoxValue"+id+"\"/>");
            pageContext.getOut().write("<input type=\"hidden\" name=\"transferEloURI\" value=\"" + getTransferEloURI() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + getId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("</form>");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return EVAL_PAGE;
    }

    public String getChecked() {
        if(executeGetter()) return "checked";
        return "";
    }


    private Boolean executeGetter() {
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = getTransferObject().getClass().getMethod("get" + property);

            Boolean returnValue = (Boolean) method.invoke(getTransferObject(), null);

            if (returnValue == null) {
                returnValue = Boolean.FALSE;
            }

            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO - " + getTransferObject().getClass().getName() + " property: " + property);
    }


    public String getTransferEloURI() {
        return transferEloURI;
    }

    public void setTransferEloURI(String transferEloURI) {
        this.transferEloURI = transferEloURI;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public TransferObjectServiceCollection getTransferObjectServiceCollection() {
        return transferObjectServiceCollection;
    }

    public void setTransferObjectServiceCollection(TransferObjectServiceCollection transferObjectServiceCollection) {
        this.transferObjectServiceCollection = transferObjectServiceCollection;
    }

    public BaseXMLTransfer getTransferObject() {
        return transferObject;
    }

    public void setTransferObject(BaseXMLTransfer transferObject) {
        this.transferObject = transferObject;
    }


}
