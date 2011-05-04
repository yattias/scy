package eu.scy.agents.authoring.workflow;

import java.net.URI;
import java.util.List;

import eu.scy.agents.authoring.workflow.WorkflowItem.Type;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.MissionSpecificationEloContent;
import eu.scy.common.scyelo.RooloServices;

public class WorkflowCreator {

	private RooloServices rooloServices;

	public WorkflowCreator(RooloServices rooloServices) {
		this.rooloServices = rooloServices;
	}

	public Workflow createWorkflow(URI missionSpecificationUri) {
		MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo
				.loadElo(missionSpecificationUri, rooloServices);

		MissionSpecificationEloContent missionEloContent = missionSpecificationElo
				.getTypedContent();
		URI missionMapModelEloUri = missionEloContent
				.getMissionMapModelEloUri();
		MissionModelElo missionModelElo = MissionModelElo.loadElo(
				missionMapModelEloUri, rooloServices);
		MissionModel missionModel = missionModelElo.getMissionModel();
		List<Las> lasses = missionModel.getLasses();

		Workflow workflow = new Workflow();

		for (Las las : lasses) {
			WorkflowItem workflowItem = new WorkflowItem(las.getId());
			workflowItem.setType(Type.LAS);
			workflow.addWorkflowItem(workflowItem);
		}

		return workflow;
	}
}
