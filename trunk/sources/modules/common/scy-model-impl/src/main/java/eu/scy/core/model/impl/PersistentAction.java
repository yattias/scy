package eu.scy.core.model.impl;

import eu.scy.actionlogging.api.IAction;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jdom.Content;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.aug.2009
 * Time: 11:14:15
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "buddyconnection")
public class PersistentAction extends ScyBaseObject implements IAction {

    @Override
    public void addContext(String key, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getContext(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addAttribute(String key, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addAttribute(String name, String value, String subElement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addAttribute(String name, String value, Content subElement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttribute(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transient
    public String getXMLString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transient
    public Element getXML() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFromXML(String xml) throws JDOMException, IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFromXML(Element element) throws JDOMException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
