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
    private final static String TAG_VIS_HEADER_LABEL = "header_label";
    private int noCol;
    // if null => no row, else header (type string)
    private DataHeader headerLabel;

    public SimpleVisualization(long dbKey, String name, TypeVisualization type, int noCol, DataHeader headerLabel) {
        super(dbKey, name, type);
        this.noCol = noCol;
        this.headerLabel = headerLabel;
    }

    public int getNoCol() {
        return noCol;
    }

    public void setNoCol(int noCol) {
        this.noCol = noCol;
    }

    public DataHeader getHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(DataHeader headerLabel) {
        this.headerLabel = headerLabel;
    }

    // CLONE
     @Override
    public Object clone()  {
        SimpleVisualization vis = (SimpleVisualization) super.clone() ;
        vis.setNoCol(new Integer(noCol));
        if(headerLabel == null)
            vis.setHeaderLabel(null);
        else{
            vis.setHeaderLabel((DataHeader)headerLabel.clone());
        }
        return vis;
    }

    @Override
     public Element toXMLLog(){
         Element e = super.toXMLLog();
         e.addContent(new Element(TAG_VIS_ID).setText(Integer.toString(noCol)));
         if(headerLabel != null)
             e.addContent(new Element(TAG_VIS_HEADER_LABEL).setText(Integer.toString(headerLabel.getNoCol())));
         return e;
     }

     /* affichage console */
    @Override
     public String toString(){
        String s = "";
        if(headerLabel != null){
            s += " tagge par la colonne "+headerLabel.getNoCol()+" ";
        }
         String toString = this.getName()+ " ("+this.getType().getCode()+") on col " + noCol+s+"\n";
         return toString;
     }

    @Override
    public boolean isOnNo(int no){
         return no==noCol || (headerLabel != null && headerLabel.getNoCol() == no);
    }
}