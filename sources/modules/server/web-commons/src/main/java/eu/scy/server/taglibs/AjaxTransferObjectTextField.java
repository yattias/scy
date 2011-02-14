package eu.scy.server.taglibs;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.BaseXMLTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLTransferObjectServiceImpl;
import eu.scy.server.util.TransferObjectMapService;
import eu.scy.server.util.TransferObjectServiceCollection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.feb.2011
 * Time: 05:51:06
 * To change this template use File | Settings | File Templates.
 */
public class AjaxTransferObjectTextField extends TagSupport {

    private String transferEloURI;
    private String id;
    private String property;
    private BaseXMLTransfer transferObject;
    private TransferObjectServiceCollection transferObjectServiceCollection;


    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<form id=\"ajaxTextFieldForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxTransferObjectTextFieldController.html\">");
            pageContext.getOut().write("<span dojoType=\"dijit.InlineEditBox\" onchange=\"document.getElementById('ajaxTextField" + id + "').value = this.value;postForm('ajaxTextFieldForm" + id + "');\" autoSave='true'  >" + executeGetter() + "</span>");


            pageContext.getOut().write("<input type=\"hidden\" id=\"ajaxTextField" + id + "\" name=\"value\" value=\"\" + executeGetter(getModel(), getProperty()) + \"\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"transferEloURI\" value=\"" + getTransferEloURI() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + getId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("</form>");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return EVAL_PAGE;
    }

    private String executeGetter() {
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = getTransferObject().getClass().getMethod("get" + property);

            String returnValue = (String) method.invoke(getTransferObject(), null);

            if (returnValue == null) {
                returnValue = "Edit";
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
