/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.common;

/**
 * Possible Type of visual
 * @author Marjolaine Bodin
 */
public class TypeVisualization implements Cloneable{
    // PROPERTY
    /* identifiant bd*/
    private long dbKey;
    /*code cf constantes => VIS_*/
    private int code ;
    /*nom */
    private String name;
    /* nombre de parametres (colonnes )*/
    private int nbColParam;

    // CONSTRUCTOR
    public TypeVisualization(long dbKey, int code, String name, int nbColParam) {
        this.dbKey = dbKey ;
        this.code = code;
        this.name = name;
        this.nbColParam = nbColParam;
    }

    // GETTER AND SETTER
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
    
    // CLONE
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
