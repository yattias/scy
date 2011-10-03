package eu.scy.server.controllers;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.AnchorEloTransfer;
import eu.scy.core.model.transfer.BaseXMLTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.util.TransferObjectMapService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;


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

    public static final String OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER = "OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER";

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = request.getParameter("id");
        String transferEloURI = request.getParameter("transferEloURI");
        String property = request.getParameter("property");
        String value = request.getParameter("value");

        transferEloURI = URLDecoder.decode(transferEloURI, "UTF-8");

        URI uri = new URI(transferEloURI);

        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());
        BaseXMLTransfer rootTransferObject = (BaseXMLTransfer) getXmlTransferObjectService().getObject(scyElo.getContent().getXmlString());
        BaseXMLTransfer transfer = (BaseXMLTransfer) getTransferObjectMapService().getObjectWithId(rootTransferObject, id);
        executeSetter(transfer, property, value);


        if(property.indexOf("bligatory") > 0) {
            logger.info("RESETTING THE ANCHOR ELOS IN THE SESSION");
            request.getSession().removeAttribute(OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER);
            if(scyElo.getObligatoryInPortfolio() == null) {
                scyElo.setObligatoryInPortfolio(false);
            }

            AnchorEloTransfer anchorEloTransfer = (AnchorEloTransfer) transfer;

            //scyElo.setObligatoryInPortfolio(anchorEloTransfer.getObligatoryInPortfolio());
            scyElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(rootTransferObject));

            IMetadata templateTrueMetadata = getMissionELOService().getELOFactory().createMetadata();
            IMetadataKey templateKey = getMissionELOService().getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.OBLIGATORY_IN_PORTFOLIO);

            templateTrueMetadata.getMetadataValueContainer(templateKey).setValue(anchorEloTransfer.getObligatoryInPortfolio().toString());
            URI u = scyElo.getUri();
            String ur = u.toString();
            getMissionELOService().getRepository().addMetadata(scyElo.getUri(),  templateTrueMetadata);


        } else {
            scyElo.updateElo();
        }





        





        if(property.indexOf("bligatory") > 0) {
            ScyElo newElo = ScyElo.loadLastVersionElo(scyElo.getUri(),  getMissionELOService());
            Boolean obli = newElo.getObligatoryInPortfolio();
            logger.info("OBLIGATORY: " + obli);
        }
        

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }


    private void executeSetter(BaseXMLTransfer transferObject, String property, String value) {
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();
            Boolean booleanValue = true;
            if(value.equals("false")) booleanValue = false;   //yes - we need to flip the value....

            property = firstLetter + property.substring(1, property.length());

            Method method = transferObject.getClass().getMethod("set" + property, Boolean.class);
            method.invoke(transferObject, booleanValue);
            return;
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
