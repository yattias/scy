/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * pie chart or bar chart
 * @author Marjolaine
 */
public class SimpleVisualization extends Visualization{
    private final static String TAG_VIS_HEADER = "header";
    private final static String TAG_VIS_HEADER_LABEL = "header_label";
    private DataHeader header;
    // if null => no row, else header (type string)
    private DataHeader headerLabel;

    public SimpleVisualization(long dbKey, String name, TypeVisualization type, DataHeader header, DataHeader headerLabel) {
        super(dbKey, name, type);
        this.header = header;
        this.headerLabel = headerLabel;
    }

    public SimpleVisualization(Element xmlElem) throws JDOMException {
        super(xmlElem);
        this.header = new DataHeader(xmlElem.getChild(TAG_VIS_HEADER));
        this.headerLabel = null;
        if(xmlElem.getChild(TAG_VIS_HEADER_LABEL) != null){
            this.headerLabel = new DataHeader(xmlElem.getChild(TAG_VIS_HEADER_LABEL));
        }
    }

    public DataHeader getHeader() {
        return header;
    }

    public void setHeader(DataHeader header) {
        this.header = header;
    }

    

    public DataHeader getHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(DataHeader headerLabel) {
        this.headerLabel = headerLabel;
    }

     @Override
    public Object clone()  {
        SimpleVisualization vis = (SimpleVisualization) super.clone() ;
        vis.setHeader((DataHeader)header.clone());
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
         e.addContent(header.toXMLLog());
         if(headerLabel != null){
             e.addContent(headerLabel.toXMLLog());
        }
         return e;
     }

     /* to string debug */
    @Override
     public String toString(){
        String s = "";
        if(headerLabel != null){
            s += " with the header tag "+headerLabel.getNoCol()+" ";
        }
         String toString = this.getName()+ " ("+this.getType().getCode()+") on col " + header.getNoCol()+s+"\n";
         return toString;
     }

    @Override
    public boolean isOnNo(int no){
         return no==header.getNoCol() || (headerLabel != null && headerLabel.getNoCol() == no);
    }

    public int getNoCol(){
        return header.getNoCol();
    }
}
