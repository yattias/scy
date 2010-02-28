/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import org.jdom.Element;

/**
 *  Plot for XYGraph
 * @author Marjolaine
 */
public class PlotXY implements Cloneable{
    private final static String TAG_PLOT_XY = "plot_xy";
    private final static String TAG_HEADER_X = "header_x";
    private final static String TAG_HEADER_Y = "header_y";
    /* axe x*/
    private DataHeader headerX;
    /* axe y */
    private DataHeader headerY;

    public PlotXY(DataHeader headerX, DataHeader headerY) {
        this.headerX = headerX;
        this.headerY = headerY;
    }

    public DataHeader getHeaderX() {
        return headerX;
    }

    public void setHeaderX(DataHeader headerX) {
        this.headerX = headerX;
    }

    public DataHeader getHeaderY() {
        return headerY;
    }

    public void setHeaderY(DataHeader headerY) {
        this.headerY = headerY;
    }


    @Override
    public Object clone()  {
        try {
            PlotXY plot = (PlotXY) super.clone() ;
            DataHeader hX = (DataHeader)this.headerX.clone();
            DataHeader hY = (DataHeader)this.headerY.clone();

            plot.setHeaderX(hX);
            plot.setHeaderY(hY);

            return plot;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    public Element toXML(){
        Element element = new Element(TAG_PLOT_XY);
        element.addContent(new Element(TAG_HEADER_X).setText(Integer.toString(headerX.getNoCol())));
        element.addContent(new Element(TAG_HEADER_Y).setText(Integer.toString(headerY.getNoCol())));
        return element;
    }

    public boolean isOnNo(int no){
        return (headerX.getNoCol() == no || headerY.getNoCol() == no);
    }

    public void updateNoCol(int no, int delta){
        int n = headerX.getNoCol();
        if(n > no)
            headerX.setNoCol(n+delta);
        n = headerY.getNoCol();
        if(n > no)
            headerY.setNoCol(n+delta);
    }
}
