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
        if(getId() == null) {
            this.id = UUID.randomUUID().toString();
        }

    }

    public String getId() {
        return id;
    }


}
