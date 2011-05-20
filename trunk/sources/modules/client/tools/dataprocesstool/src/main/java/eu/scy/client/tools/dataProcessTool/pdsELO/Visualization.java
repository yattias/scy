/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.pdsELO;

import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * A visualization describes : 
 * - the type :  GRAPH, PIE CHART, BAR CHART
 * - the definition :
 *      for a grahp : name, x_axis, y_axis, x_name, y_name
 *      for a pie /bar chart : id column/row
 * - parameters : for a graph : xmin, xmax, deltaX, ymin, ymax, deltaY, color
 * - function model : for a graph : description, color
 * @author Marjolaine
 */
public class Visualization {
    /* TAG NAMES */
    public final static String TAG_VISUALIZATION = "graph";
    public final static String TAG_VISUALIZATION_TYPE = "type";
    public final static String TAG_VISUALIZATION_DEFINITION = "definition";
    public final static String TAG_VISUALIZATION_DEF_NAME="name";
    public final static String TAG_VISUALIZATION_DEF_ID_COL="column";
    public final static String TAG_VISUALIZATION_LABEL_HEADER = "label_header";
    

    /* type */
    protected String type;
    protected String name;

    public Visualization(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Visualization(Element xmlElem) throws JDOMException {
		
    }


    public Visualization(String xmlString) throws JDOMException {
		this(new JDomStringConversion().stringToXml(xmlString));
	}


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_VISUALIZATION);
        element.addContent(new Element(TAG_VISUALIZATION_TYPE).setText(this.type));
        return element;
    }




}
