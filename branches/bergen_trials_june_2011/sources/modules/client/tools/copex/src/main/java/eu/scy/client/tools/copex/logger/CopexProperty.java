/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.logger;

import org.jdom.Content;

/**
 * property for the log
 * @author Marjolaine
 */
public class CopexProperty {
    private String name;
    private String value;
    /* can be null*/
    private Content subElement;
    private long dbKey;

    public CopexProperty(String name, String value, Content subElement) {
        this.name = name;
        this.value = value;
        this.subElement = subElement;
        this.dbKey = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Content getSubElement() {
        return subElement;
    }

    public void setSubElement(Content subElement) {
        this.subElement = subElement;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

}
