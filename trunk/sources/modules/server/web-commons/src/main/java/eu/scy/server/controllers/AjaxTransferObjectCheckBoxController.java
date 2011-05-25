package eu.scy.server.controllers;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.BaseXMLTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.util.TransferObjectMapService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mai.2011
 * Time: 00:03:56
 * To change this template use File | Settings | File Templates.
 */
public class AjaxTransferObjectCheckBoxController extends AbstractController {

    private XMLTransferObjectService xmlTransferObjectService;
    private MissionELOService missionELOService;
    private TransferObjectMapService transferObjectMapService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = request.getParameter("id");
        String transferEloURI = request.getParameter("transferEloURI");
        String property = request.getParameter("property");
        String value = request.getParameter("value");

        URI uri = new URI(transferEloURI);

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
            Boolean booleanValue = true;
            if(value.equals("true")) booleanValue = false;   //yes - we need to flip the value....

            property = firstLetter + property.substring(1, property.length());

            Method method = transferObject.getClass().getMethod("set" + property, Boolean.class);
            logger.info("METHOD IS: " + method + " method name: " + method.getName());

            Boolean returnValue = (Boolean) method.invoke(transferObject, booleanValue);
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

}
