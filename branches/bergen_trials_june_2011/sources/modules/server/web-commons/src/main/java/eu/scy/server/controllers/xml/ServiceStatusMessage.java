package eu.scy.server.controllers.xml;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 07:18:13
 * To change this template use File | Settings | File Templates.
 */
public class ServiceStatusMessage {

    private String serviceMessage;


    public ServiceStatusMessage(String serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    public String getServiceMessage() {
        return serviceMessage;
    }

    public void setServiceMessage(String serviceMessage) {
        this.serviceMessage = serviceMessage;
    }
}
