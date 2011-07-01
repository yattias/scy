/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import java.awt.Color;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *  an operation can be of different types: sum, avg, min, max
 * @author Marjolaine Bodin
 */
public class TypeOperation implements Cloneable {
    public final static String TAG_TYPE_OPERATION = "type";
    private final static String TAG_TYPE_OPERATION_ID = "id_type";
    private final static String TAG_TYPE_OPERATION_TYPE = "type";
    private final static String TAG_TYPE_OPERATION_CODE = "code";
    private final static String TAG_TYPE_OPERATION_COLOR_R = "color_r";
    private final static String TAG_TYPE_OPERATION_COLOR_G = "color_g";
    private final static String TAG_TYPE_OPERATION_COLOR_B = "color_b";

    /* db key identifier */
    protected long dbKey;
    /* type cf CONSTANTES OP_XXX */
    protected int type;
    /* code = name to show to user */
    protected String codeName;
    /* color */
    protected Color color;

    public TypeOperation(long dbKey, int type, String codeName, Color color) {
        this.dbKey = dbKey;
        this.type = type;
        this.codeName = codeName;
        this.color = color;
    }

    public TypeOperation(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_TYPE_OPERATION)) {
            dbKey = -1;
            try{
                dbKey = Long.parseLong(xmlElem.getChild(TAG_TYPE_OPERATION_ID).getText());
            }catch(NumberFormatException ex){
            }
            codeName = xmlElem.getChild(TAG_TYPE_OPERATION_CODE).getText();
            type = -1;
            try{
                type = Integer.parseInt(xmlElem.getChild(TAG_TYPE_OPERATION_TYPE).getText());
            }catch(NumberFormatException ex){
            }
            try{
                int colorR = Integer.parseInt(xmlElem.getChild(TAG_TYPE_OPERATION_COLOR_R).getText());
                int colorG = Integer.parseInt(xmlElem.getChild(TAG_TYPE_OPERATION_COLOR_G).getText());
                int colorB = Integer.parseInt(xmlElem.getChild(TAG_TYPE_OPERATION_COLOR_B).getText());
                this.color = new Color(colorR, colorG, colorB);
            }catch(NumberFormatException e){
                throw(new JDOMException("Function model expects COLOR as integer"));
            }
        }else {
            throw(new JDOMException("TypeOperation expects <"+TAG_TYPE_OPERATION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
    public Object clone()  {
        try {
            TypeOperation typeOp = (TypeOperation) super.clone() ;
            long dbKeyC = this.dbKey;
            int typeC = new Integer(this.type);
            String codeNameC = new String(this.codeName);
            Color colorC = this.color;
            
            typeOp.setDbKey(dbKeyC);
            typeOp.setType(typeC);
            typeOp.setCodeName(codeNameC);
            typeOp.setColor(colorC);
            
            return typeOp;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    public Element toXML(){
         Element e = new Element(TAG_TYPE_OPERATION);
         e.addContent(new Element(TAG_TYPE_OPERATION_ID).setText(Long.toString(this.dbKey)));
         e.addContent(new Element(TAG_TYPE_OPERATION_TYPE).setText(Integer.toString(this.type)));
         e.addContent(new Element(TAG_TYPE_OPERATION_CODE).setText(this.codeName));
         e.addContent(new Element(TAG_TYPE_OPERATION_COLOR_R).setText(Integer.toString(this.color.getRed())));
         e.addContent(new Element(TAG_TYPE_OPERATION_COLOR_G).setText(Integer.toString(this.color.getGreen())));
         e.addContent(new Element(TAG_TYPE_OPERATION_COLOR_B).setText(Integer.toString(this.color.getBlue())));
         return e;
    }
    
}
