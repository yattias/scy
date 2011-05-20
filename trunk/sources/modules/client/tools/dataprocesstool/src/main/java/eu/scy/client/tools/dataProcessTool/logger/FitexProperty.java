/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.logger;

import org.jdom.Content;

/**
 * property for the log
 * @author Marjolaine
 */
public class FitexProperty {
    private String name;
    private String value;
    /* can be null*/
    private Content subElement;

    public FitexProperty(String name, String value, Content subElement) {
        this.name = name;
        this.value = value;
        this.subElement = subElement;
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


}
