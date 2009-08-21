/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.awt.Color;

/**
 * types d'operations 
 * @author Marjolaine Bodin
 */
public class TypeOperation implements Cloneable {
    // PROPERTY 
    /* identifiant */
    protected long dbKey;
    /* type cf CONSTANTES OP_XXX */
    protected int type;
    /* code = nom a afficher */
    protected String codeName;
    /* couleur */
    protected Color color;

    // CONSTUCTEUR 
    public TypeOperation(long dbKey, int type, String codeName, Color color) {
        this.dbKey = dbKey;
        this.type = type;
        this.codeName = codeName;
        this.color = color;
    }

    // GETTER AND SETTER
    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    // CLONE
    @Override
    public Object clone()  {
        try {
            TypeOperation typeOp = (TypeOperation) super.clone() ;
            long dbKeyC = this.dbKey;
            int typeC = new Integer(this.type);
            String codeNameC = new String(this.codeName);
            Color colorC = this.color;
            
            typeOp.setDbKey(dbKeyC);
            typeOp.setType(typeC);
            typeOp.setCodeName(codeNameC);
            typeOp.setColor(colorC);
            
            return typeOp;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
    
}
