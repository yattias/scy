package eu.scy.server.util;

import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLTransferObjectServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.feb.2011
 * Time: 06:46:22
 * To change this template use File | Settings | File Templates.
 */
public class TransferObjectServiceCollection {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private TransferObjectMapService transferObjectMapService;

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public TransferObjectMapService getTransferObjectMapService() {
        return transferObjectMapService;
    }

    public void setTransferObjectMapService(TransferObjectMapService transferObjectMapService) {
        this.transferObjectMapService = transferObjectMapService;
    }
}
