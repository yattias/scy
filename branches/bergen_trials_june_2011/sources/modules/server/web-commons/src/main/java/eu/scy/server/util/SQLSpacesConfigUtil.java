package eu.scy.server.util;

import eu.scy.common.configuration.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.feb.2011
 * Time: 06:54:49
 * To change this template use File | Settings | File Templates.
 */
public class SQLSpacesConfigUtil {

    private int port;
    private String ip;
    private String space;

    public int getPort() {
        return Configuration.getInstance().getSQLSpacesServerPort();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return Configuration.getInstance().getSQLSpacesServerHost();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSpace() {
        return "actions";
    }

    public void setSpace(String space) {
        this.space = space;
    }
}
