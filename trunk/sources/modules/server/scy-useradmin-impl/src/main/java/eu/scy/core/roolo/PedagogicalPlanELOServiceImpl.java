package eu.scy.core.roolo;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.feb.2011
 * Time: 21:42:03
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanELOServiceImpl extends BaseELOServiceImpl implements PedagogicalPlanELOService {

    private static Logger log = Logger.getLogger("PedagogicalPlanELOServiceImpl.class");

    private XMLTransferObjectService xmlTransferObjectService;

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    @Override
    public PedagogicalPlanTransfer getPedagogicalPlanForMission(MissionSpecificationElo missionSpecificationElo) {
        PedagogicalPlanTransfer transfer = null;
        URI uri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, this);
        if(scyElo != null) {
            String content = scyElo.getContent().getXmlString();
            if(content != null && content.length() > 0) {
                transfer = (PedagogicalPlanTransfer) getXmlTransferObjectService().getObject(content);
            }


        }

        return transfer;
    }
}
