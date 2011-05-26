package eu.scy.core.roolo;

import eu.scy.common.mission.*;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.AnchorEloTransfer;
import eu.scy.core.model.transfer.LasTransfer;
import eu.scy.core.model.transfer.MissionPlanTransfer;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;

import java.net.URI;
import java.util.List;
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
    public void clearMissionPlanning(MissionSpecificationElo missionSpecificationElo) {
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanForMission(missionSpecificationElo);
        pedagogicalPlanTransfer.setMissionPlan(null);
        savePedagogicalPlan(pedagogicalPlanTransfer, missionSpecificationElo);
    }

    @Override
    public void initializePedagogicalPlanWithLasses(MissionSpecificationElo missionSpecificationElo) {
        MissionSpecificationEloContent missionEloContent = missionSpecificationElo.getTypedContent();
        URI missionMapModelEloUri = missionEloContent.getMissionMapModelEloUri();
        MissionModelElo missionModelElo = MissionModelElo.loadElo(missionMapModelEloUri, this);
        MissionModel missionModel = missionModelElo.getMissionModel();
        List<Las> lasses = missionModel.getLasses();

        PedagogicalPlanTransfer pedagogicalPlan = getPedagogicalPlanForMission(missionSpecificationElo);
        if (pedagogicalPlan != null) {

            if (pedagogicalPlan.getMissionPlan().getLasTransfers() == null || pedagogicalPlan.getMissionPlan().getLasTransfers().size() == 0) {

                for (int i = 0; i < lasses.size(); i++) {
                    Las las = lasses.get(i);
                    LasTransfer lasTransfer = new LasTransfer();
                    lasTransfer.setOriginalLasId(las.getId());
                    lasTransfer.setName(las.getTitle());
                    lasTransfer.setLasType(las.getLasType().name());

                    loadLasInstructions(lasTransfer, las);

                    pedagogicalPlan.getMissionPlan().addLas(lasTransfer);

                    MissionAnchor missionAnchor = las.getMissionAnchor();
                    if (missionAnchor != null) {
                        ScyElo missionAnchorElo = ScyElo.loadLastVersionElo(missionAnchor.getEloUri(), this);
                        AnchorEloTransfer anchorEloTransfer = new AnchorEloTransfer();
                        anchorEloTransfer.setName(missionAnchorElo.getTitle());
                        anchorEloTransfer.setObligatoryInPortfolio(missionAnchorElo.getObligatoryInPortfolio());
                        lasTransfer.setAnchorElo(anchorEloTransfer);
                    }

                }


                savePedagogicalPlan(pedagogicalPlan, missionSpecificationElo);
            }


        }

    }

    private void loadLasInstructions(LasTransfer lasTransfer, Las las) {
        String descriptionURI = "/webapp/useradmin/LoadExternalPage.html?url=" + String.valueOf(las.getInstructionUri());
        lasTransfer.setInstructions(descriptionURI);
    }

    private void savePedagogicalPlan(PedagogicalPlanTransfer pedagogicalPlan, MissionSpecificationElo missionSpecificationElo) {
        URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, this);
        pedagogicalPlanELO.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlan));
        pedagogicalPlanELO.updateElo();
    }

    @Override
    public PedagogicalPlanTransfer getPedagogicalPlanForMission(MissionSpecificationElo missionSpecificationElo) {
        PedagogicalPlanTransfer transfer = null;
        URI uri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, this);
        if (scyElo != null) {
            String content = scyElo.getContent().getXmlString();
            if (content != null && content.length() > 0) {
                transfer = (PedagogicalPlanTransfer) getXmlTransferObjectService().getObject(content);
            }


        }

        return transfer;
    }
}
