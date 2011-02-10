package eu.scy.core.roolo;

import eu.scy.core.XMLTransferObjectService;

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
}
