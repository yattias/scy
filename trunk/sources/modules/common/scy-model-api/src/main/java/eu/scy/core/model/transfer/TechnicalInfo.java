package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2011
 * Time: 05:54:40
 * To change this template use File | Settings | File Templates.
 */
public class TechnicalInfo extends BaseXMLTransfer {

    private List jnlpProperties;

    public List getJnlpProperties() {
        if(jnlpProperties == null) setJnlpProperties(new LinkedList());
        return jnlpProperties;
    }

    public void setJnlpProperties(List jnlpProperties) {
        this.jnlpProperties = jnlpProperties;
    }
}
