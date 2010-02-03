/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import org.jdom.Element;

/**
 * pie chart or bar chart
 * @author Marjolaine
 */
public class SimpleVisualization extends Visualization{
    private final static String TAG_VIS_ID = "id";
    private int noCol;

    public SimpleVisualization(long dbKey, String name, TypeVisualization type, int noCol) {
        super(dbKey, name, type);
        this.noCol = noCol;
    }

    public int getNoCol() {
        return noCol;
    }

    public void setNoCol(int noCol) {
        this.noCol = noCol;
    }

    // CLONE
     @Override
    public Object clone()  {
        SimpleVisualization vis = (SimpleVisualization) super.clone() ;
        vis.setNoCol(new Integer(noCol));
        return vis;
    }

    @Override
     public Element toXMLLog(){
         Element e = super.toXMLLog();
         e.addContent(new Element(TAG_VIS_ID).setText(Integer.toString(noCol)));
         return e;
     }

     /* affichage console */
    @Override
     public String toString(){
         String toString = this.getName()+ " ("+this.getType().getCode()+") on col " + noCol+"\n";
         return toString;
     }

    @Override
    public boolean isOnNo(int no){
         return no==noCol;
     }
}
