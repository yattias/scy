/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

/**
 * parametres d'un graphe
 * @author Marjolaine
 */
public class ParamGraph implements Cloneable{
    // PRPOERTY
    /* axe x*/
    private DataHeader headerX;
    /* axe y */
    private DataHeader headerY;
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
    public ParamGraph(DataHeader headerX, DataHeader headerY,  double x_min, double x_max, double y_min, double y_max, double deltaX, double deltaY, boolean autoscale) {
        this.headerX = headerX;
        this.headerY = headerY;
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

    // CLONE
    @Override
    public Object clone()  {
        try {
            ParamGraph param = (ParamGraph) super.clone() ;
            DataHeader hX = (DataHeader)this.headerX.clone();
            DataHeader hY = (DataHeader)this.headerY.clone();
            double x_minC = this.x_min ;
            double x_maxC = this.x_max ;
            double y_minC = this.y_min ;
            double y_maxC = this.y_max ;
            double deltaXC = this.deltaX ;
            double deltayC = this.deltaY ;

            param.setHeaderX(hX);
            param.setHeaderY(hY);
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

}
