package eu.scy.agents.authoring.workflow;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import eu.scy.agents.authoring.workflow.WorkflowItem.Type;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.LasTransfer;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;

import java.io.Writer;
import java.net.URI;

public class WorkflowCreator {

    private RooloServices rooloServices;

    public WorkflowCreator(RooloServices rooloServices) {
        this.rooloServices = rooloServices;
    }

    public Workflow createWorkflow(URI missionSpecificationUri) {
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo
                .loadElo(missionSpecificationUri, rooloServices);

//        MissionSpecificationEloContent missionEloContent = missionSpecificationElo
//                .getTypedContent();
//        URI missionMapModelEloUri = missionEloContent
//                .getMissionMapModelEloUri();
//        MissionModelElo missionModelElo = MissionModelElo.loadElo(
//                missionMapModelEloUri, rooloServices);
//        MissionModel missionModel = missionModelElo.getMissionModel();
//        List<Las> lasses = missionModel.getLasses();

        PedagogicalPlanTransfer pedPlan = getPedPlan(missionSpecificationElo);

        Workflow workflow = new Workflow();

        for (LasTransfer las : pedPlan.getMissionPlan().getLasTransfers()) {
            WorkflowItem workflowItem = new WorkflowItem(las.getOriginalLasId());
            workflowItem.setType(Type.LAS);
            workflowItem.setExpectedTimeInMinutes(las.getMinutesPlannedUsedInLas());
            workflow.addWorkflowItem(workflowItem);
        }

        return workflow;
    }

    private PedagogicalPlanTransfer getPedPlan(MissionSpecificationElo missionSpecificationElo) {
        PedagogicalPlanTransfer transfer = null;
        URI uri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, rooloServices);
        if (scyElo != null) {
            String content = scyElo.getContent().getXmlString();
            if (content != null && content.length() > 0) {
                transfer = (PedagogicalPlanTransfer) getObject(content);
            }
        }
        return transfer;
    }

    private Object getObject(String xml) {
        return getToObjectXStream().fromXML(xml);
    }

    XStream getToObjectXStream() {
        XStream xstream = new XStream(new XppDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    boolean cdata = false;

                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        if (name.equals("id")) {
                            cdata = false;
                        } else {
                            cdata = true;
                        }
                    }

                    protected void writeText(QuickWriter writer, String text) {
                        writer.write(text);
                    }
                };
            }
        }
        );
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);

        return xstream;
    }
}
