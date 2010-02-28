package eu.scy.core.model.impl;

import eu.scy.core.model.Server;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.feb.2010
 * Time: 11:58:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "server")
public class ServerImpl extends ScyBaseObject implements Server {

    private String serverCSS;

    @Override
    public String getServerCSS() {
        return serverCSS;
    }

    @Override
    public void setServerCSS(String serverCSS) {
        this.serverCSS = serverCSS;
    }
}
