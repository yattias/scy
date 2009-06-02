/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * represente un type de materiel
 * @author MBO
 */
public class TypeMaterial implements Cloneable {
    // ATTRIBUTS
    private long dbKey;
    private String type;

    // CONSTRUCTEURS
    public TypeMaterial(long dbKey, String type) {
        this.dbKey = dbKey;
        this.type = type;
    }

   

    // GETTER AND SETTER
     public long getDbKey() {
        return dbKey;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    @Override
    public String toString() {
        return getType();
    }
    
    @Override
    public Object clone()  {
        try {
            TypeMaterial t = (TypeMaterial) super.clone() ;
            long dbKeyC = this.dbKey;
            String typeC = new String(this.type);
            
            t.setDbKey(dbKeyC);
            t.setType(typeC);
            
            return t;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    /* retourne le type de materiel dans une liste - null sinon */
    public static TypeMaterial getTypeMaterial(ArrayList<TypeMaterial> listT, long dbKey){
         for (Iterator iter=listT.iterator();iter.hasNext(); ){
             TypeMaterial t = (TypeMaterial)iter.next();
             if (t.getDbKey() == dbKey)
                 return t;
         }
         return null;
    }
    
}
