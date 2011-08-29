package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;

public abstract class AbstractGuidanceObject {

    protected String id; // usually it is defined as the elo_uri

    protected String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected String getCode() {
        return id.substring(id.lastIndexOf("/") + 1, id.lastIndexOf("."));
    }

    protected String loadELO(String eloUri) throws TupleSpaceException {
        String eloAsXML = null;
        try {
            String id = new VMID().toString();
            ProcessGuidanceAgent.getCommandSpace().write(new Tuple(id, "roolo-agent", "elo", eloUri));
            Tuple responseTuple = ProcessGuidanceAgent.getCommandSpace().waitToTakeFirst(new Tuple(id, "roolo-response", String.class));
            eloAsXML = responseTuple.getField(2).getValue().toString();

        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in TupleSpace while load an object in roolo");
        }
        return eloAsXML;
    }
}