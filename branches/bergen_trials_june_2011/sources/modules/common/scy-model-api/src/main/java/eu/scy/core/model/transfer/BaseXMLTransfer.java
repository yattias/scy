package eu.scy.core.model.transfer;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.jan.2011
 * Time: 12:45:35
 * To change this template use File | Settings | File Templates.
 */
public class BaseXMLTransfer {

    private String id;

    public BaseXMLTransfer() {
        generateId();

    }

    private void generateId() {
        if(id == null || id.equals("")) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public String getId() {
        if(this.id == null) generateId();
        return id;
    }


}
