/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.pdsELO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * visualization type graph
 * @author Marjolaine
 */
public class GraphVisualization extends Visualization {
    /* TAG NAMES */
    public final static String TAG_VISUALIZATION_DEF_X_AXIS="x_axis";
    public final static String TAG_VISUALIZATION_DEF_Y_AXIS="y_axis";
    public final static String TAG_VISUALIZATION_DEF_X_NAME="x_name";
    public final static String TAG_VISUALIZATION_DEF_Y_NAME="y_name";
    public final static String TAG_VISUALIZATION_PARAM="param";
    public final static String TAG_VISUALIZATION_PARAM_X_MIN="xmin";
    public final static String TAG_VISUALIZATION_PARAM_X_MAX="xmax";
    public final static String TAG_VISUALIZATION_PARAM_DELTAX="deltaX";
    public final static String TAG_VISUALIZATION_PARAM_Y_MIN="ymin";
    public final static String TAG_VISUALIZATION_PARAM_Y_MAX="ymax";
    public final static String TAG_VISUALIZATION_PARAM_DELTAY="deltaY";
    public final static String TAG_VISUALIZATION_PARAM_COLOR_R="color_r";
    public final static String TAG_VISUALIZATION_PARAM_COLOR_G="color_g";
    public final static String TAG_VISUALIZATION_PARAM_COLOR_B="color_b";
    public final static String TAG_VISUALIZATION_FUNCTIONS_MODEL="functions";
    

    /* axis*/
    private List<XYAxis> axis;
    /* xmin*/
    private double xMin;
    /* xMax*/
    private double xMax;
    private double deltaX;
    private double yMin;
    private double yMax;
    private double deltaY;
    private int colorR;
    private int colorG;
    private int colorB;
    /* function model */
    private List<FunctionModel> listFunctionModel;

    public GraphVisualization(String type, String name, List<XYAxis> axis, double xMin, double xMax, double deltaX, double yMin, double yMax, double deltaY, int colorR, int colorG, int colorB, List<FunctionModel> listFunctionModel) {
        super(type, name);
        this.axis = axis;
        this.xMin = xMin;
        this.xMax = xMax;
        this.deltaX = deltaX;
        this.yMin = yMin;
        this.yMax = yMax;
        this.deltaY = deltaY;
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.listFunctionModel = listFunctionModel;
    }


     public GraphVisualization(Element xmlElem) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_VISUALIZATION)) {
            this.type =xmlElem.getChild(TAG_VISUALIZATION_TYPE).getText() ;
            //DEFINITION
            Element elDef = xmlElem.getChild(TAG_VISUALIZATION_DEFINITION);
            this.name = elDef.getChild(TAG_VISUALIZATION_DEF_NAME).getText() ;
            axis = new LinkedList();
            for (Iterator<Element> variableElem = elDef.getChildren(XYAxis.TAG_VISUALIZATION_XY_AXIS).iterator(); variableElem.hasNext();) {
                axis.add(new XYAxis(variableElem.next()));
            }
            if(axis.size() == 0){
                int x_axis=-1;
                int y_axis = -1;
                String x_name = "";
                String y_name = "";
                Element elXAxis = elDef.getChild(TAG_VISUALIZATION_DEF_X_AXIS);
                if(elXAxis != null){
                    // conserve l'ancien format au  cas ou, pour compatibilite
                    Element el = elXAxis.getChild(TAG_VISUALIZATION_DEF_ID_COL);
                    try{
                        x_axis = Integer.parseInt(el.getText());
                    }catch(NumberFormatException e){
                        throw(new JDOMException("Graph visualization xAxis expects id as Integer ."));
                    }
                    Element elYAxis = elDef.getChild(TAG_VISUALIZATION_DEF_Y_AXIS);
                    el = elYAxis.getChild(TAG_VISUALIZATION_DEF_ID_COL);
                    try{
                        y_axis = Integer.parseInt(el.getText());
                    }catch(NumberFormatException e){
                        throw(new JDOMException("Graph visualization yAxis expects id as Integer ."));
                    }
                    x_name = elDef.getChild(TAG_VISUALIZATION_DEF_X_NAME).getText();
                    y_name = elDef.getChild(TAG_VISUALIZATION_DEF_Y_NAME).getText();
                }
                if(x_axis != -1 && y_axis!=-1){
                    axis.add(new XYAxis(x_axis, y_axis, x_name, y_name));
                }

            }
            //PARAMETRES 
            Element elParam = xmlElem.getChild(TAG_VISUALIZATION_PARAM);
            try{
                this.xMin = Double.parseDouble(elParam.getChild(TAG_VISUALIZATION_PARAM_X_MIN).getText());
                this.yMin = Double.parseDouble(elParam.getChild(TAG_VISUALIZATION_PARAM_Y_MIN).getText());
                this.xMax = Double.parseDouble(elParam.getChild(TAG_VISUALIZATION_PARAM_X_MAX).getText());
                this.yMax = Double.parseDouble(elParam.getChild(TAG_VISUALIZATION_PARAM_Y_MAX).getText());
                this.deltaX = Double.parseDouble(elParam.getChild(TAG_VISUALIZATION_PARAM_DELTAX).getText());
                this.deltaY = Double.parseDouble(elParam.getChild(TAG_VISUALIZATION_PARAM_DELTAY).getText());
            }catch(NumberFormatException e){
                throw(new JDOMException("Graph visualization parameters  expects param as Double ."));
            }
            try{
                this.colorR = Integer.parseInt(elParam.getChild(TAG_VISUALIZATION_PARAM_COLOR_R).getText());
                this.colorG = Integer.parseInt(elParam.getChild(TAG_VISUALIZATION_PARAM_COLOR_G).getText());
                this.colorB = Integer.parseInt(elParam.getChild(TAG_VISUALIZATION_PARAM_COLOR_B).getText());
            }catch(NumberFormatException e){
                throw(new JDOMException("Graph visualization parameters  expects param color  as Integer ."));
            }
            // liste des functions
            Element elFunction = xmlElem.getChild(TAG_VISUALIZATION_FUNCTIONS_MODEL);
            if (elFunction != null){
                this.listFunctionModel = new LinkedList<FunctionModel>() ;
                for (Iterator<Element> variableElem = elFunction.getChildren(FunctionModel.TAG_VISUALIZATION_FUNCTION_MODEL).iterator(); variableElem.hasNext();) {
                    this.listFunctionModel.add(new FunctionModel(variableElem.next()));
                }
            }

		} else {
			throw(new JDOMException("Visualization expects <"+TAG_VISUALIZATION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}


    public GraphVisualization(String xmlString) throws JDOMException {
		this(new JDomStringConversion().stringToXml(xmlString));
	}


    public int getColorB() {
        return colorB;
    }

    public int getColorG() {
        return colorG;
    }

    public int getColorR() {
        return colorR;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public List<FunctionModel> getListFunctionModel() {
        return listFunctionModel;
    }

    public double getXMax() {
        return xMax;
    }

    public double getXMin() {
        return xMin;
    }

    
    public double getYMax() {
        return yMax;
    }

    public double getYMin() {
        return yMin;
    }

    public List<XYAxis> getAxis() {
        return axis;
    }

    

     // toXML
    @Override
    public Element toXML(){
        Element element = new Element(TAG_VISUALIZATION);
        element.addContent(new Element(TAG_VISUALIZATION_TYPE).setText(this.type));
        //DEFINITION
        Element elDef = new Element(TAG_VISUALIZATION_DEFINITION);
        elDef.addContent(new Element(TAG_VISUALIZATION_DEF_NAME).setText(this.name));
        for(Iterator<XYAxis> a = axis.iterator();a.hasNext();){
            elDef.addContent(a.next().toXML());
        }
        element.addContent(elDef);
        // PARAMETRES
        Element elParam = new Element(TAG_VISUALIZATION_PARAM);
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_X_MIN).setText(Double.toString(this.xMin)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_X_MAX).setText(Double.toString(this.xMax)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_DELTAX).setText(Double.toString(this.deltaX)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_Y_MIN).setText(Double.toString(this.yMin)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_Y_MAX).setText(Double.toString(this.yMax)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_DELTAY).setText(Double.toString(this.deltaY)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_COLOR_R).setText(Integer.toString(this.colorR)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_COLOR_G).setText(Integer.toString(this.colorG)));
        elParam.addContent(new Element(TAG_VISUALIZATION_PARAM_COLOR_B).setText(Integer.toString(this.colorB)));
        element.addContent(elParam);
        // FUNCTIONS MODEL
        if (this.listFunctionModel != null && this.listFunctionModel.size() > 0){
            Element elFunctions = new Element(TAG_VISUALIZATION_FUNCTIONS_MODEL);
            for (Iterator<FunctionModel> f = listFunctionModel.iterator(); f.hasNext();) {
                elFunctions.addContent(f.next().toXML());
            }
            element.addContent(elFunctions);
        }

		return element;
    }
    

}
