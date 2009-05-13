package eu.scy.colemo.contributions.cmap;

import eu.scy.colemo.contributions.Contribution;
import eu.scy.colemo.network.Person;

import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.nov.2008
 * Time: 11:56:54
 * To change this template use File | Settings | File Templates.
 */
public class ConceptNode extends AbstractCMapNode {

    private String name;
    private String description;


    public ConceptNode(String name, InetAddress iNetAddress, Person person) {
        this.name = name;
        setIp(iNetAddress);
        setPerson(person);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
