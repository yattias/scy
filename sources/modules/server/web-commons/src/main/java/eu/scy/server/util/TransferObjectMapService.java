package eu.scy.server.util;

import eu.scy.core.model.transfer.AssessmentSetupTransfer;
import eu.scy.core.model.transfer.BaseXMLTransfer;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.feb.2011
 * Time: 06:07:23
 * To change this template use File | Settings | File Templates.
 */
public class TransferObjectMapService {

    public Object getObjectWithId(BaseXMLTransfer transfer, String id) {
        Map map = createMap(transfer);
        return map.get(id);
    }

    public Map createMap(BaseXMLTransfer transfer) {
        if(transfer instanceof PedagogicalPlanTransfer) {
            return getPedagogicalPlanMap((PedagogicalPlanTransfer)transfer);
        }
        return null;
    }

    private Map getPedagogicalPlanMap(PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        Map returnMap = new HashMap();

        returnMap.put(pedagogicalPlanTransfer.getId(), pedagogicalPlanTransfer);
        AssessmentSetupTransfer assessmentSetupTransfer = pedagogicalPlanTransfer.getAssessmentSetup();
        if(assessmentSetupTransfer != null) returnMap.put(assessmentSetupTransfer.getId(), assessmentSetupTransfer);

        return returnMap;

    }

}
