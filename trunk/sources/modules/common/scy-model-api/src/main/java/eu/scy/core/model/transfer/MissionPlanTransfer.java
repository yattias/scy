package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2011
 * Time: 20:35:48
 * To change this template use File | Settings | File Templates.
 */
public class MissionPlanTransfer extends BaseXMLTransfer {

    private List<LasTransfer> lasTransfers;

    public void addLas(LasTransfer lasTransfer) {
        if(lasTransfers == null) setLasTransfers(new LinkedList());
        getLasTransfers().add(lasTransfer);
    }

    public List<LasTransfer> getLasTransfers() {
        return lasTransfers;
    }

    public void setLasTransfers(List<LasTransfer> lasTransfers) {
        this.lasTransfers = lasTransfers;
    }
}
