/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import java.util.ArrayList;

/**
 * grandeur physique qui permet de décrire les différentes unités
 * @author Marjolaine
 */
public class PhysicalQuantity implements Cloneable{
    // PROPERTY
    /* identifiant */
    private long dbKey ;
    /* nom */
    private String name;
    /* liste des différentes unités possibles */
    private ArrayList<CopexUnit> listUnit ;

    // CONSTRUCTOR
    public PhysicalQuantity(long dbKey, String name, ArrayList<CopexUnit> listUnit) {
        this.dbKey = dbKey;
        this.name = name;
        this.listUnit = listUnit;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public ArrayList<CopexUnit> getListUnit() {
        return listUnit;
    }

    public void setListUnit(ArrayList<CopexUnit> listUnit) {
        this.listUnit = listUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // OVERRIDE
    @Override
    public Object clone()  {
       try {
            PhysicalQuantity grandeur = (PhysicalQuantity) super.clone() ;

            grandeur.setDbKey(this.dbKey);
            grandeur.setName(new String(this.name));
            ArrayList<CopexUnit> listUnitC = null;
            if (this.listUnit != null){
                listUnitC = new ArrayList();
                int nb = this.listUnit.size();
                for (int i=0; i<nb; i++){
                    listUnitC.add(this.listUnit.get(i));
                }
            }
            grandeur.setListUnit(listUnitC);
            
            return grandeur;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
        }
    }

    // METHOD
    /* retourne l'unite qui correspond au dbKey, null sinon */
    public CopexUnit getUnit(long dbKeyU){
        int nbu = listUnit.size();
        for (int i=0; i<nbu; i++){
            if (listUnit.get(i).getDbKey() == dbKeyU){
                return listUnit.get(i);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String s = this.name +" : \n";
        for (int i=0; i<this.listUnit.size(); i++){
            s += this.listUnit.get(i).getName()+" ("+this.listUnit.get(i).getSymbol()+")";
        }
        return s;
    }

}
