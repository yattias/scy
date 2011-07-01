/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * the function is defined with a description
 * the description can contain some parameters.
 * the function may be f(x)= or f(y)=
 * @author Marjolaine
 */
public class FunctionModel implements Cloneable{
    public final static String TAG_FUNCTION = "function";
    private final static String TAG_FUNCTION_ID = "id_function";
    private final static String TAG_DESCRIPTION = "description";
    private final static String TAG_TYPE = "type";
    private final static String TAG_COLOR_R = "color_red";
    private final static String TAG_COLOR_G = "color_green";
    private final static String TAG_COLOR_B = "color_blue";
    private final static String TAG_ID_FUNCTION_PREDEF = "id_predef_function";

    /* db key identifier */
    private long dbKey ;
    /* description of the function (after the f(x/y)=..)  */
    private String description;
    /* color of the function on the graph */
    private Color color ;
    /* parameters list */
    private ArrayList<FunctionParam> listParam;
    /* type (x/y) */
    private char type;
    /* eventually id predefined function */
    private String idPredefFunction;
    

     public FunctionModel(long dbKey, String description, char type, Color color, ArrayList<FunctionParam> listParam, String idPredefFunction) {
        this.dbKey = dbKey ;
        this.description = description;
        this.type = type;
        this.color = color;
        this.listParam = listParam;
        this.idPredefFunction = idPredefFunction;
    }

     public FunctionModel(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_FUNCTION)) {
            dbKey = -1;
            try{
                dbKey = Long.parseLong(xmlElem.getChild(TAG_FUNCTION_ID).getText());
            }catch(NumberFormatException ex){
            }
            description = xmlElem.getChild(TAG_DESCRIPTION).getText();
            type = xmlElem.getChild(TAG_TYPE).getText().equals(DataConstants.F_Y) ?DataConstants.FUNCTION_TYPE_Y_FCT_X : DataConstants.FUNCTION_TYPE_X_FCT_Y;
            try{
                int colorR = Integer.parseInt(xmlElem.getChild(TAG_COLOR_R).getText());
                int colorG = Integer.parseInt(xmlElem.getChild(TAG_COLOR_G).getText());
                int colorB = Integer.parseInt(xmlElem.getChild(TAG_COLOR_B).getText());
                this.color = new Color(colorR, colorG, colorB);
            }catch(NumberFormatException e){
                throw(new JDOMException("Function model expects COLOR as integer"));
            }
            listParam = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren(FunctionParam.TAG_FUNCTION_PARAM).iterator(); variableElem.hasNext();) {
                listParam.add(new FunctionParam(variableElem.next()));
            }
            idPredefFunction = null;
            if(xmlElem.getChild(TAG_ID_FUNCTION_PREDEF) != null){
                idPredefFunction = xmlElem.getChild(TAG_ID_FUNCTION_PREDEF).getText();
            }
        }else {
            throw(new JDOMException("FunctionModel expects <"+TAG_FUNCTION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public String getIdPredefFunction() {
        return idPredefFunction;
    }

    public void setIdPredefFunction(String idPredefFunction) {
        this.idPredefFunction = idPredefFunction;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }


     public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<FunctionParam> getListParam() {
        return listParam;
    }

    public void setListParam(ArrayList<FunctionParam> listParam) {
        this.listParam = listParam;
    }

    @Override
    public Object clone()  {
        try {
            FunctionModel fm = (FunctionModel)super.clone();
            String descriptionC = new String(this.description);
            Color colorC  = this.color ;
            fm.setDbKey(this.dbKey);
            fm.setDescription(descriptionC);
            fm.setType(new Character(this.type));
            fm.setColor(colorC);
            ArrayList l = new ArrayList();
            int n = listParam.size();
            for (int i=0; i<n; i++){
                l.add((FunctionParam)listParam.get(i).clone());
            }
            if(idPredefFunction == null){
                fm.setIdPredefFunction(null);
            }else{
                fm.setIdPredefFunction(new String(this.idPredefFunction));
            }
            return fm;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public Element toXML(){
        Element element = new Element(TAG_FUNCTION);
        element.addContent(new Element(TAG_FUNCTION_ID).setText(Long.toString(dbKey)));
        element.addContent(new Element(TAG_DESCRIPTION).setText(description));
        element.addContent(new Element(TAG_TYPE)).setText(type== DataConstants.FUNCTION_TYPE_Y_FCT_X ? DataConstants.F_Y : DataConstants.F_X);
        element.addContent(new Element(TAG_COLOR_R).setText(Integer.toString(color.getRed())));
        element.addContent(new Element(TAG_COLOR_G).setText(Integer.toString(color.getGreen())));
        element.addContent(new Element(TAG_COLOR_B).setText(Integer.toString(color.getBlue())));
        for(Iterator<FunctionParam> p = listParam.iterator();p.hasNext();){
            element.addContent(p.next().toXML());
        }
        if(idPredefFunction != null){
            element.addContent(new Element(TAG_ID_FUNCTION_PREDEF).setText(idPredefFunction));
        }
        return element;
    }


}
