/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;

/**
 * parametres d'un graphe
 * @author Marjolaine
 */
public class ParamGraph implements Cloneable{
    private final static String TAG_PARAM_GRAPH = "graph_param";
    private final static String TAG_X_MIN = "x_min";
    private final static String TAG_X_MAX = "x_max";
    private final static String TAG_DELTA_X = "delta_x";
    private final static String TAG_Y_MIN = "y_min";
    private final static String TAG_Y_MAX = "y_max";
    private final static String TAG_DELTA_Y = "delta_y";
    private final static String TAG_AUTOSCALE = "autoscale";
    /*axes */
    private ArrayList<PlotXY> plots;
    /* x min*/
    private double x_min;
    /* x max */
    private double x_max;
    /* y min */
    private double y_min ;
    /* y max */
    private double y_max;
    /*deltaX */
    private double deltaX ;
    /* delta Y */
    private double deltaY;
    /*autoscale*/
    private boolean autoscale;

    // CONSTRUCTOR 
    public ParamGraph(PlotXY plot,  double x_min, double x_max, double y_min, double y_max, double deltaX, double deltaY, boolean autoscale) {
        this.plots = new ArrayList();
        this.plots.add(plot);
        this.x_min = x_min;
        this.x_max = x_max;
        this.y_min = y_min;
        this.y_max = y_max;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.autoscale = autoscale;
    }
    public ParamGraph(ArrayList<PlotXY> plots,  double x_min, double x_max, double y_min, double y_max, double deltaX, double deltaY, boolean autoscale) {
        this.plots = plots;
        this.x_min = x_min;
        this.x_max = x_max;
        this.y_min = y_min;
        this.y_max = y_max;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.autoscale = autoscale;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public double getX_max() {
        return x_max;
    }

    public void setX_max(double x_max) {
        this.x_max = x_max;
    }

    public double getX_min() {
        return x_min;
    }

    public void setX_min(double x_min) {
        this.x_min = x_min;
    }

    

    public double getY_max() {
        return y_max;
    }

    public void setY_max(double y_max) {
        this.y_max = y_max;
    }

    public double getY_min() {
        return y_min;
    }

    public void setY_min(double y_min) {
        this.y_min = y_min;
    }

   
    public boolean isAutoscale() {
        return autoscale;
    }

    public void setAutoscale(boolean autoscale) {
        this.autoscale = autoscale;
    }

    public ArrayList<PlotXY> getPlots() {
        return plots;
    }

    public void setPlots(ArrayList<PlotXY> plots) {
        this.plots = plots;
    }

    public PlotXY getPlot1() {
        if(plots.size() > 0)
            return plots.get(0);
        else return null;
    }

   public void setPlot1(PlotXY plot){
       plots = new ArrayList();
       plots.add(plot);
   }
   public void updatePlot1(PlotXY plot){
       if(plots == null)
           setPlot1(plot);
       if(plots.size() > 0)
           plots.set(0, plot);
       else
           plots.add(plot);
   }

    // CLONE
    @Override
    public Object clone()  {
        try {
            ParamGraph param = (ParamGraph) super.clone() ;
            ArrayList plotsC = new ArrayList();
            for(Iterator<PlotXY> p = plots.iterator();p.hasNext();){
                plotsC.add((PlotXY)p.next().clone());
            }
            double x_minC = this.x_min ;
            double x_maxC = this.x_max ;
            double y_minC = this.y_min ;
            double y_maxC = this.y_max ;
            double deltaXC = this.deltaX ;
            double deltayC = this.deltaY ;

            param.setPlots(plotsC);
            param.setX_min(x_minC);
            param.setY_min(y_minC);
            param.setX_max(x_maxC);
            param.setY_max(y_maxC);
            param.setDeltaX(deltaXC);
            param.setDeltaY(deltayC);
            param.setAutoscale(this.autoscale);

            return param;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }



    public Element toXML(){
        Element element = new Element(TAG_PARAM_GRAPH);
        for(Iterator<PlotXY> p = plots.iterator();p.hasNext();){
            element.addContent(p.next().toXML());
        }
        element.addContent(new Element(TAG_X_MIN).setText(Double.toString(x_min)));
        element.addContent(new Element(TAG_X_MAX).setText(Double.toString(x_max)));
        element.addContent(new Element(TAG_DELTA_X).setText(Double.toString(deltaX)));
        element.addContent(new Element(TAG_Y_MIN).setText(Double.toString(y_min)));
        element.addContent(new Element(TAG_Y_MAX).setText(Double.toString(y_max)));
        element.addContent(new Element(TAG_DELTA_Y).setText(Double.toString(deltaY)));
        element.addContent(new Element(TAG_AUTOSCALE).setText(autoscale ? "true":"false"));
        return element;
    }

    public boolean removePlotWithNo(int no){
        boolean remove = false;
        int nb = plots.size();
        for (int i=nb-1; i>=0; i--){
            if(plots.get(i).isOnNo(no)){
                plots.remove(i);
                remove = true;
            }
        }
        return remove;
    }

    public void updateNoCol(int no, int delta){
        for(Iterator<PlotXY> plot = plots.iterator();plot.hasNext();){
            plot.next().updateNoCol(no, delta);
        }
    }
    

}
