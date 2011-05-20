/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import java.awt.Color;
import org.jdom.Element;

/**
 *  Plot for XYGraph
 * @author Marjolaine
 */
public class PlotXY implements Cloneable{
    private final static String TAG_PLOT_XY = "plot_xy";
    private final static String TAG_HEADER_X = "header_x";
    private final static String TAG_HEADER_Y = "header_y";
    private final static String TAG_PLOT_COLOR = "plot_color";
    private final static String TAG_PLOT_COLOR_RED = "plot_color_red";
    private final static String TAG_PLOT_COLOR_GREEN = "plot_color_green";
    private final static String TAG_PLOT_COLOR_BLUE = "plot_color_blue";

    private long dbKey;
    private Color plotColor;
    /* axe x*/
    private DataHeader headerX;
    /* axe y */
    private DataHeader headerY;

    public PlotXY(long dbKey,DataHeader headerX, DataHeader headerY, Color plotColor) {
        this.dbKey = dbKey;
        this.headerX = headerX;
        this.headerY = headerY;
        this.plotColor = plotColor;
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

    public Color getPlotColor() {
        return plotColor;
    }

    public void setPlotColor(Color plotColor) {
        this.plotColor = plotColor;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }


    @Override
    public Object clone()  {
        try {
            PlotXY plot = (PlotXY) super.clone() ;
            DataHeader hX = (DataHeader)this.headerX.clone();
            DataHeader hY = (DataHeader)this.headerY.clone();
            Color c = new Color(plotColor.getRGB());
            plot.setDbKey(new Long(dbKey));
            plot.setHeaderX(hX);
            plot.setHeaderY(hY);
            plot.setPlotColor(c);

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
        Element elC = new Element(TAG_PLOT_COLOR);
        elC.addContent(new Element(TAG_PLOT_COLOR_RED).setText(Integer.toString(plotColor.getRed())));
        elC.addContent(new Element(TAG_PLOT_COLOR_GREEN).setText(Integer.toString(plotColor.getGreen())));
        elC.addContent(new Element(TAG_PLOT_COLOR_BLUE).setText(Integer.toString(plotColor.getBlue())));
        element.addContent(elC);
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
