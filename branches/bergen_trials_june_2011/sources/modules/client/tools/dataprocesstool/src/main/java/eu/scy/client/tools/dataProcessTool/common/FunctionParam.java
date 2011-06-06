/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import org.jdom.Element;

/**
 * parameter of a function, name/value
 * @author Marjolaine
 */
public class FunctionParam implements Cloneable{
    private final static String TAG_FUNCTION_PARAM = "function_param";
    private final static String TAG_PARAM  = "param";
    private final static String TAG_VALUE = "value";
    /* dbkey identifier */
    private long dbKey ;
    /* name of the parameter */
    private String param;
    /* value of the parameter */
    private double value;

    public FunctionParam(long dbKey, String param, double value) {
        this.dbKey = dbKey;
        this.param = param;
        this.value = value;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    @Override
    public Object clone()  {
        try {
            FunctionParam fp = (FunctionParam)super.clone();
            fp.setDbKey(dbKey);
            fp.setParam(new String(this.param));
            fp.setValue(new Double(this.value));

            return fp;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public Element toXML(){
        Element element = new Element(TAG_FUNCTION_PARAM);
        element.addContent(new Element(TAG_PARAM).setText(param));
        element.addContent(new Element(TAG_VALUE).setText(Double.toString(value)));
        return element;
    }

    @Override
    public String toString(){
        return param+" : "+Double.toString(value);
    }

}
