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
    /* nom axe x*/
    private String x_name;
    /* nom axe y */
    private String y_name;
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

    // CONSTRUCTOR 
    public ParamGraph(String x_name, String y_name, double x_min, double x_max, double y_min, double y_max, double deltaX, double deltaY) {
        this.x_name = x_name;
        this.y_name = y_name;
        this.x_min = x_min;
        this.x_max = x_max;
        this.y_min = y_min;
        this.y_max = y_max;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
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

    public String getX_name() {
        return x_name;
    }

    public void setX_name(String x_name) {
        this.x_name = x_name;
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

    public String getY_name() {
        return y_name;
    }

    public void setY_name(String y_name) {
        this.y_name = y_name;
    }

    // CLONE
    @Override
    public Object clone()  {
        try {
            ParamGraph param = (ParamGraph) super.clone() ;
            String x_nameC = new String(this.x_name);
            String y_nameC = new String(this.y_name);
            double x_minC = this.x_min ;
            double x_maxC = this.x_max ;
            double y_minC = this.y_min ;
            double y_maxC = this.y_max ;
            double deltaXC = this.deltaX ;
            double deltayC = this.deltaY ;

            param.setX_name(x_nameC);
            param.setY_name(y_nameC);
            param.setX_min(x_minC);
            param.setY_min(y_minC);
            param.setX_max(x_maxC);
            param.setY_max(y_maxC);
            param.setDeltaX(deltaXC);
            param.setDeltaY(deltayC);

            return param;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

}
