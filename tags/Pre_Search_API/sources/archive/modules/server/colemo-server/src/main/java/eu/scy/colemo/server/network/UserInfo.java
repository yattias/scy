/*
 * Created on 02.aug.2005
 *
 * 
 */
package eu.scy.colemo.server.network;
import java.net.*;

/**
 * @author Øystein
 *
 */
public class UserInfo {
    private String name;
    private InetAddress ip;
    
    public UserInfo(String name, InetAddress ip){
        this.name=name;
        this.ip=ip;
    }
    
    
    /**
     * @return Returns the ip.
     */
    public InetAddress getIp() {
        return ip;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
}
