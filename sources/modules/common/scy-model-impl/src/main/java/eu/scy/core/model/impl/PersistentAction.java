package eu.scy.core.model.impl;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IContext;

import javax.persistence.Entity;
import javax.persistence.Table;


import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.aug.2009
 * Time: 11:14:15
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "persistentActions")
public class PersistentAction extends ScyBaseObject implements IAction {
    /*
    private Map<String, String> attributes; 
    private Map<String, String> context;


    @CollectionOfElements
    @JoinTable(name="persistentActionAttributes")
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    //CollectionOfElements
    //JoinTable(name="persistentActionContext")
    public Map<String, String> getContext() {
        return context;
    }

    @Override
    public void setUser(String user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUser() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTime(String time) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setType(String type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDataType(String dataType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDataType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setData(String data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    @Override
    public void addContext(String key, String value) {
        if(getContext()== null) setContext(new HashMap());
        getContext().put(key, value);
    }

    @Override
    public String getContext(String key) {
        return getContext().get(key);
    }

    @Override
    public void addContext(ContextConstants constant, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getContext(ContextConstants constant) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addAttribute(String key, String value) {
        if(getAttributes() == null) setAttributes(new HashMap());
        getAttributes().put(key, value);
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
        return getAttributes().get(key);
    }

    @Override
    public void setContext(IContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
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
    */

    @Override
    public void addContext(ContextConstants constant, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getContext(ContextConstants constant) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addAttribute(String key, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttribute(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setContext(IContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IContext getContext() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setUser(String user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUser() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

  /*  @Override
    @Deprecated
    public void setTime(String time) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Deprecated
    public String getTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }*/
    @Override
    public void setTimeInMillis(long timeInMillis) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
    @Override
    public long getTimeInMillis() {
        return -1l;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setType(String type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

 /*   @Override
    public void setDataType(String dataType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDataType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setData(String data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }*/

	@Override
	public Map<String, String> getAttributes() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
