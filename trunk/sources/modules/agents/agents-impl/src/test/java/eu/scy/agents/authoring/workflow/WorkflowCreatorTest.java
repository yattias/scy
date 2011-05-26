package eu.scy.agents.authoring.workflow;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.TestRooloServiceImpl;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.MissionSpecificationEloContent;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.LasTransfer;
import eu.scy.core.model.transfer.MissionPlanTransfer;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class WorkflowCreatorTest extends AbstractTestFixture {

    private WorkflowCreator workflowCreator;
    private TestRooloServiceImpl rooloServices;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();


        rooloServices = new TestRooloServiceImpl(repository);
        rooloServices.setMetadataTypeManager(typeManager);
        workflowCreator = new WorkflowCreator(rooloServices);
    }

    @Override
    @After
    public void tearDown() throws AgentLifecycleException {
        super.tearDown();
    }

    @Test
    public void testCreateWorkflow() throws URISyntaxException {
        Workflow workflow = workflowCreator.createWorkflow(new URI(MISSION1));
    }

    @Ignore
    @Test
    public void test() throws URISyntaxException {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo
                .loadElo(new URI(MISSION1), rooloServices);
        MissionSpecificationEloContent missionEloContent = missionSpecificationElo.getTypedContent();
        URI missionMapModelEloUri = missionEloContent.getMissionMapModelEloUri();
        MissionModelElo missionModelElo = MissionModelElo.loadElo(missionMapModelEloUri, rooloServices);
        MissionModel missionModel = missionModelElo.getMissionModel();
        List<Las> lasses = missionModel.getLasses();

        PedagogicalPlanTransfer pedagogicalPlan = new PedagogicalPlanTransfer();// getPedagogicalPlanForMission(missionSpecificationElo);
        pedagogicalPlan.setMissionPlan(new MissionPlanTransfer());
        if (pedagogicalPlan != null) {

            if (pedagogicalPlan.getMissionPlan().getLasTransfers() == null || pedagogicalPlan.getMissionPlan().getLasTransfers().size() == 0) {

                for (int i = 0; i < lasses.size(); i++) {
                    Las las = lasses.get(i);
                    LasTransfer lasTransfer = new LasTransfer();
                    lasTransfer.setOriginalLasId(las.getId());
                    lasTransfer.setName(las.getTitle());
                    lasTransfer.setLasType(las.getLasType().name());
                    lasTransfer.setMinutesPlannedUsedInLas(120);

                    pedagogicalPlan.getMissionPlan().addLas(lasTransfer);

//                    MissionAnchor missionAnchor = las.getMissionAnchor();
//                    if (missionAnchor != null) {
//                        ScyElo missionAnchorElo = ScyElo.loadLastVersionElo(missionAnchor.getEloUri(), rooloServices);
//                        AnchorEloTransfer anchorEloTransfer = new AnchorEloTransfer();
//                        anchorEloTransfer.setName(missionAnchorElo.getTitle());
//                        anchorEloTransfer.setObligatoryInPortfolio(missionAnchorElo.getObligatoryInPortfolio());
//                        lasTransfer.setAnchorElo(anchorEloTransfer);
//                    }

                }
                savePedagogicalPlan(pedagogicalPlan, missionSpecificationElo);
            }
        }
    }

    public PedagogicalPlanTransfer getPedagogicalPlanForMission(MissionSpecificationElo missionSpecificationElo) {
        PedagogicalPlanTransfer transfer = null;
        URI uri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, rooloServices);
        if (scyElo != null) {
            String content = scyElo.getContent().getXmlString();
            if (content != null && content.length() > 0) {
                transfer = (PedagogicalPlanTransfer) workflowCreator.getToObjectXStream().fromXML(content);
            }


        }

        return transfer;
    }

    private void savePedagogicalPlan(PedagogicalPlanTransfer pedagogicalPlan, MissionSpecificationElo missionSpecificationElo) {
        URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
//        ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, rooloServices);
        System.out.println(workflowCreator.getToObjectXStream().toXML(pedagogicalPlan));
//        #
    }

}
