package eu.scy.server.taglibs;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.feb.2011
 * Time: 23:18:04
 * To change this template use File | Settings | File Templates.
 */
public class AgentParameterTextField extends TagSupport {

    private String missionURI;
    private String agentId;
    private AgentParameterAPI agentParameterAPI;
    private String parameterName;
    private String las;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<form id=\"ajaxTextFieldForm" + id + "\" method=\"post\" action=\"/webapp/components/agentParameterTextFieldController.html\">");
            pageContext.getOut().write("<span dojoType=\"dijit.InlineEditBox\" onchange=\"document.getElementById('ajaxTextField" + id + "').value = this.value;postForm('ajaxTextFieldForm" + id + "');\" autoSave='true'  >" + executeGetter() + "</span>");


            pageContext.getOut().write("<input type=\"hidden\" id=\"ajaxTextField" + id + "\" name=\"value\" value=\"\" + executeGetter(getModel(), getProperty()) + \"\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"missionURI\" value=\"" + getMissionURI() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"agentId\" value=\"" + getAgentId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"las\" value=\"" + getLas() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"parameterName\" value=\"" + getParameterName() + "\">");
            pageContext.getOut().write("</form>");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return EVAL_PAGE;
    }

    public String executeGetter() {
        AgentParameter agentParameter = new AgentParameter();
        agentParameter.setMission(getMissionURI());
        agentParameter.setParameterName(getParameterName());
        //getAgentParameterAPI().getParameter(getAgentId(), agentParameter);
        return getAgentId() + " :: " + getParameterName() + " :: FOR MISSION: " + getMissionURI();
    }

    public String getMissionURI() {
        return missionURI;
    }

    public void setMissionURI(String missionURI) {
        this.missionURI = missionURI;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public AgentParameterAPI getAgentParameterAPI() {
        return agentParameterAPI;
    }

    public void setAgentParameterAPI(AgentParameterAPI agentParameterAPI) {
        this.agentParameterAPI = agentParameterAPI;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getLas() {
        return las;
    }

    public void setLas(String las) {
        this.las = las;
    }
}
