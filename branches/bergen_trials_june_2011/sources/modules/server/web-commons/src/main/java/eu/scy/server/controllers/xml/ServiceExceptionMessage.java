package eu.scy.server.controllers.xml;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 06:37:24
 * To change this template use File | Settings | File Templates.
 */
public class ServiceExceptionMessage {

    private String exceptionMessage;

    public ServiceExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
