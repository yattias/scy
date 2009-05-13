package eu.scy.actionlogging.logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import eu.scy.actionlogging.api.IAction;
import eu.scy.core.model.impl.ScyBaseObject;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = Action.TABLE_NAME)
public class Action extends ScyBaseObject implements IAction {
    
    @Transient
    private Properties properties = new Properties();    //data storage

    @Transient
    public final static String TABLE_NAME = "Action";

    public Action() {
    }

    /**
     * creates a new action from XML
     *
     * @param xml
     */
    public Action(String xml) {
        setPropertiesFromXML(xml);
    }

    /**
     * adds a new key->value pair to properties
     *
     * @param key
     * @param value
     */
    public void addProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * returns the Properties object
     */

    @Transient
    public Properties getProperties() {
        return this.properties;
    }

    /**
     * returns only one Property value (specified in key)
     *
     * @param key
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * returns XML
     */
    @Transient
    public String getXML() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            properties.storeToXML(os, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return os.toString();
    }

    /**
     * sets properties from XML string,
     * used in constructor
     *
     * @param xml
     */
    @Transient
    public void setPropertiesFromXML(String xml) {
        InputStream in = new ByteArrayInputStream(xml.getBytes());
        try {
            properties.loadFromXML(in);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
