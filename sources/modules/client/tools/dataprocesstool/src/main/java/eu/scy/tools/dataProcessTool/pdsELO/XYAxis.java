/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * xy axis
 * @author Marjolaine
 */
public class XYAxis {
    /* TAG NAMES */
    public final static String TAG_VISUALIZATION_XY_AXIS= "xy_axis";
    public final static String TAG_VISUALIZATION_DEF_X_AXIS="x_axis";
    public final static String TAG_VISUALIZATION_DEF_Y_AXIS="y_axis";
    public final static String TAG_VISUALIZATION_DEF_X_NAME="x_name";
    public final static String TAG_VISUALIZATION_DEF_Y_NAME="y_name";

    /* id x_axis*/
    private int x_axis;
    /* id y_axis */
    private int y_axis;
    /* x_name */
    private String xName;
    /* y_name */
    private String yName;

    public XYAxis(int x_axis, int y_axis, String xName, String yName) {
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.xName = xName;
        this.yName = yName;
    }

    public XYAxis(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_VISUALIZATION_XY_AXIS)) {
            try{
                this.x_axis = Integer.parseInt(xmlElem.getChild(TAG_VISUALIZATION_DEF_X_AXIS).getText());
                this.y_axis = Integer.parseInt(xmlElem.getChild(TAG_VISUALIZATION_DEF_Y_AXIS).getText());
            }catch(NumberFormatException e){
                throw(new JDOMException("Graph visualization axis  expects param as Integer ."));
            }
            xName = xmlElem.getChildText(TAG_VISUALIZATION_DEF_X_NAME);
            yName = xmlElem.getChildText(TAG_VISUALIZATION_DEF_Y_NAME);
        }else {
            throw(new JDOMException("XYAxis expects <"+TAG_VISUALIZATION_XY_AXIS+"> as root element, but found <"+xmlElem.getName()+">."));
        }
    }

     public XYAxis(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
     }

    public String getxName() {
        return xName;
    }

    public void setxName(String xName) {
        this.xName = xName;
    }

    public int getX_axis() {
        return x_axis;
    }

    public void setX_axis(int x_axis) {
        this.x_axis = x_axis;
    }

    public String getyName() {
        return yName;
    }

    public void setyName(String yName) {
        this.yName = yName;
    }

    public int getY_axis() {
        return y_axis;
    }

    public void setY_axis(int y_axis) {
        this.y_axis = y_axis;
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_VISUALIZATION_XY_AXIS);
        element.addContent(new Element(TAG_VISUALIZATION_DEF_X_AXIS).setText(Integer.toString(this.x_axis)));
        element.addContent(new Element(TAG_VISUALIZATION_DEF_Y_AXIS).setText(Integer.toString(this.y_axis)));
        element.addContent(new Element(TAG_VISUALIZATION_DEF_X_NAME).setText(this.xName));
        element.addContent(new Element(TAG_VISUALIZATION_DEF_Y_NAME).setText(this.yName));
        return element;
    }
}
