/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;

/**
 * Possible Type of visualization: pie chart, bar chart, graph
 * @author Marjolaine Bodin
 */
public class TypeVisualization implements Cloneable{
    /* db key identifier*/
    private long dbKey;
    /*code cf constantes => VIS_*/
    private int code ;
    /*name */
    private String name;
    /* nb of param (nb columns)*/
    private int nbColParam;

   
    public TypeVisualization(long dbKey, int code, String name, int nbColParam) {
        this.dbKey = dbKey ;
        this.code = code;
        this.name = name;
        this.nbColParam = nbColParam;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbColParam() {
        return nbColParam;
    }

    public void setNbColParam(int nbColParam) {
        this.nbColParam = nbColParam;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public boolean isChart(){
        return this.code == DataConstants.VIS_BAR || this.code == DataConstants.VIS_HISTO || this.code == DataConstants.VIS_PIE ;
    }

    public boolean isCurve(){
        return this.code == DataConstants.VIS_GRAPH;
    }
    
     @Override
    public Object clone()  {
        try {
            TypeVisualization type = (TypeVisualization) super.clone() ;
            long dbKeyC = this.dbKey ;
            int codeC = this.code;
            String nameC = new String(this.name);
            int nbColParamC = this.nbColParam ;
            type.setDbKey(dbKeyC);
            type.setCode(codeC);
            type.setName(nameC);
            type.setNbColParam(nbColParamC);
            
            return type;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
}
