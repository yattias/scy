package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2011
 * Time: 05:55:45
 * To change this template use File | Settings | File Templates.
 */
public class PropertyTransfer extends BaseXMLTransfer{

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
