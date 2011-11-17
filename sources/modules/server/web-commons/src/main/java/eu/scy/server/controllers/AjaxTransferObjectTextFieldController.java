package eu.scy.server.controllers;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.BaseXMLTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLTransferObjectServiceImpl;
import eu.scy.server.util.TransferObjectMapService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import roolo.elo.api.IELO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.feb.2011
 * Time: 06:02:07
 * To change this template use File | Settings | File Templates.
 */
public class AjaxTransferObjectTextFieldController extends AbstractController {

    private XMLTransferObjectService xmlTransferObjectService;
    private MissionELOService missionELOService;
    private TransferObjectMapService transferObjectMapService;
    private AgentParameterAPI agentParameterAPI;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = request.getParameter("id");
        String transferEloURI = request.getParameter("transferEloURI");
        String property = request.getParameter("property");
        String value = request.getParameter("value");

        transferEloURI = URLDecoder.decode(transferEloURI, "UTF-8");
        
        URI uri = new URI(transferEloURI);


        if(property.equalsIgnoreCase("groupingAgentMinimumUsers")) {
            doTheCrazyHackMinGroupSize(uri.toString(), value);
        } else if(property.equalsIgnoreCase("groupingAgentMaximumUsers"))  {
            doTheCrazyHackMaxGroupSize(uri.toString(), value);
        } else if(property.equalsIgnoreCase("groupingAgentPercent")) {
            doTheCrazyHackPercentageAvailable(uri.toString(), value);
        }



        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());
        BaseXMLTransfer rootTransferObject = (BaseXMLTransfer) getXmlTransferObjectService().getObject(scyElo.getContent().getXmlString());

        logger.info("ID: " + id + " URI: " + transferEloURI + " property:" + property);

        BaseXMLTransfer transfer = (BaseXMLTransfer) getTransferObjectMapService().getObjectWithId(rootTransferObject, id);
        logger.info("Got " + transfer.getId() + " " + transfer.getClass().getName());

        executeSetter(transfer, property, value);

        scyElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(rootTransferObject));
        scyElo.updateElo();

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }


    private Boolean executeSetter(BaseXMLTransfer transferObject, String property, String value) {
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = transferObject.getClass().getMethod("set" + property, String.class);
            logger.info("METHOD IS: " + method + " method name: " + method.getName());

            Boolean returnValue = (Boolean) method.invoke(transferObject, value);
            return returnValue;

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not set " + property + " on object of type " + transferObject.getClass().getName());

    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public TransferObjectMapService getTransferObjectMapService() {
        return transferObjectMapService;
    }

    public void setTransferObjectMapService(TransferObjectMapService transferObjectMapService) {
        this.transferObjectMapService = transferObjectMapService;
    }

    private void doTheCrazyHackPercentageAvailable(String uri, String value) {
        try {
            uri = URLDecoder.decode(uri, "utf-8");
            java.net.URI _uri = new java.net.URI(uri);
            ScyElo someELo = ScyElo.loadElo(_uri, getMissionELOService());
            URI missionRuntimeURI = someELo.getMissionRuntimeEloUri();
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
            URI misselouri = missionRuntimeElo.getMissionSpecificationEloUri();
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(misselouri, getMissionELOService());
            AgentParameter agentParameter = new AgentParameter(missionSpecificationElo.getTitle(), "PercentageAvailable");
            agentParameter.setParameterValue(value);
            getAgentParameterAPI().setParameter("GroupFormationAgent", agentParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doTheCrazyHackMinGroupSize(String uri, String value) {
        try {
            uri = URLDecoder.decode(uri, "utf-8");
            java.net.URI _uri = new java.net.URI(uri);
            ScyElo someELo = ScyElo.loadElo(_uri, getMissionELOService());
            URI missionRuntimeURI = someELo.getMissionRuntimeEloUri();
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
            URI misselouri = missionRuntimeElo.getMissionSpecificationEloUri();
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(misselouri, getMissionELOService());
            AgentParameter agentParameter = new AgentParameter(missionSpecificationElo.getTitle(), "MinGroupSize");
            agentParameter.setParameterValue(value);
            getAgentParameterAPI().setParameter("GroupFormationAgent", agentParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doTheCrazyHackMaxGroupSize(String uri, String value) {
        try {
            uri = URLDecoder.decode(uri, "utf-8");
            java.net.URI _uri = new java.net.URI(uri);
            ScyElo someELo = ScyElo.loadElo(_uri, getMissionELOService());
            URI missionRuntimeURI = someELo.getMissionRuntimeEloUri();
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
            URI misselouri = missionRuntimeElo.getMissionSpecificationEloUri();
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(misselouri, getMissionELOService());
            AgentParameter agentParameter = new AgentParameter(missionSpecificationElo.getTitle(), "MaxGroupSize");
            agentParameter.setParameterValue(value);
            getAgentParameterAPI().setParameter("GroupFormationAgent", agentParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AgentParameterAPI getAgentParameterAPI() {
        return agentParameterAPI;
    }

    public void setAgentParameterAPI(AgentParameterAPI agentParameterAPI) {
        this.agentParameterAPI = agentParameterAPI;
    }



}
