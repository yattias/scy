/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import org.jdom.Element;

/**
 * donnees du tableau 
 * ce sont forcement des reels
 * @author Marjolaine Bodin
 */
public class Data implements Cloneable {
    public final static String TAG_DATA = "data";
    private final static String TAG_DATA_VALUE = "value";
    private final static String TAG_DATA_NO_ROW = "no_row";
    private final static String TAG_DATA_NO_COL = "no_col";
    private final static String TAG_DATA_IGNORED = "is_ignored";


    /* identifiant base */
    private long dbKey;
    /* valeur */
    private double value;
    /* numero de ligne */
    private int noRow;
    /* numero de colonne */
    private int noCol;
    /* donnee est elle ignoree ?*/
    private boolean isIgnoredData;

    // CONSTRUCTEURS
    public Data(long dbKey, double value, int noRow, int noCol, boolean isIgnoredData) {
        this.dbKey = dbKey;
        this.value = value;
        this.noRow = noRow;
        this.noCol = noCol;
        this.isIgnoredData = isIgnoredData;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public boolean isIgnoredData() {
        return isIgnoredData;
    }

    public void setIsIgnoredData(boolean isIgnoredData) {
        this.isIgnoredData = isIgnoredData;
    }

    public int getNoCol() {
        return noCol;
    }

    public void setNoCol(int noCol) {
        this.noCol = noCol;
    }

    public int getNoRow() {
        return noRow;
    }

    public void setNoRow(int noRow) {
        this.noRow = noRow;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    // CLONE
     @Override
    public Object clone()  {
        try {
            Data data = (Data) super.clone() ;
            long dbKeyC = this.dbKey;
            double valueC = new Double(this.value);
            int noRowC = new Integer(this.noRow);
            int noColC = new Integer(this.noCol);
            boolean isIgnoredDataC = new Boolean(this.isIgnoredData);
            
            data.setDbKey(dbKeyC);
            data.setValue(valueC);
            data.setNoRow(noRowC);
            data.setNoCol(noColC);
            data.setIsIgnoredData(isIgnoredDataC);
            return data;
        } catch (CloneNotSupportedException e) { 
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

     public Element toXMLLog(){
         Element e = new Element(TAG_DATA);
         //e.addContent(new Element(TAG_DATA_VALUE).setText(Double.toString(value)));
         e.addContent(new Element(TAG_DATA_NO_ROW).setText(Integer.toString(noRow)));
         e.addContent(new Element(TAG_DATA_NO_COL).setText(Integer.toString(noCol)));
         e.addContent(new Element(TAG_DATA_IGNORED).setText(isIgnoredData ? "true" : "false"));
         return e;

     }
}
