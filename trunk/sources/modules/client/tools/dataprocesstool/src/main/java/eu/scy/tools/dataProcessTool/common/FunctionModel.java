/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;

/**
 * modele de fonctions
 * @author Marjolaine
 */
public class FunctionModel implements Cloneable{
    private final static String TAG_FUNCTION = "function";
    private final static String TAG_DESCRIPTION = "description";
    private final static String TAG_COLOR = "color";
    private final static String TAG_COLOR_R = "color_red";
    private final static String TAG_COLOR_G = "color_green";
    private final static String TAG_COLOR_B = "color_blue";

    /* identifiant */
    private long dbKey ;
    /* description, suit f(x) = */
    private String description;
    /* couleur */
    private Color color ;
    /* liste des parametres */
    private ArrayList<FunctionParam> listParam;

    // CONSTRUCTOR
     public FunctionModel(long dbKey, String description, Color color, ArrayList<FunctionParam> listParam) {
        this.dbKey = dbKey ;
        this.description = description;
        this.color = color;
        this.listParam = listParam;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    // GETTER AND SETTER

     public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    // CLONE
    @Override
    public Object clone()  {
        try {
            FunctionModel fm = (FunctionModel)super.clone();
            String descriptionC = new String(this.description);
            Color colorC  = this.color ;
            fm.setDbKey(this.dbKey);
            fm.setDescription(descriptionC);
            fm.setColor(colorC);
            ArrayList l = new ArrayList();
            int n = listParam.size();
            for (int i=0; i<n; i++){
                l.add((FunctionParam)listParam.get(i).clone());
            }
            return fm;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public Element toXML(){
        Element element = new Element(TAG_FUNCTION);
        element.addContent(new Element(TAG_DESCRIPTION).setText(description));
        Element c = new Element(TAG_COLOR);
        c.addContent(new Element(TAG_COLOR_R).setText(Integer.toString(color.getRed())));
        c.addContent(new Element(TAG_COLOR_G).setText(Integer.toString(color.getGreen())));
        c.addContent(new Element(TAG_COLOR_B).setText(Integer.toString(color.getBlue())));
        element.addContent(c);
        for(Iterator<FunctionParam> p = listParam.iterator();p.hasNext();){
            element.addContent(p.next().toXML());
        }
        return element;
    }


}
