package eu.scy.colemo.contributions.cmap;

import eu.scy.colemo.contributions.Contribution;
import eu.scy.colemo.network.Person;

import java.net.InetAddress;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.nov.2008
 * Time: 11:57:20
 * To change this template use File | Settings | File Templates.
 */
public class AbstractCMapNode implements Contribution, Serializable {

    private InetAddress ip;
    private Person person;


    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
