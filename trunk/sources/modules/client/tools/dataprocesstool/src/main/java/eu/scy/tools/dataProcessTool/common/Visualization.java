/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

/**
 * visualization of  data
 * @author Marjolaine Bodin
 */
public class Visualization implements Cloneable {
    // PROPERTY
    /* identifiant*/
    protected long dbKey;
    /* nom */
    protected String name;
    /* type de visuliazation */
    protected TypeVisualization type;
    /*liste des colonnes/ lignes surlesquelles s'applique */
    protected int[] tabNo;
    /* indique si en colonne ou non  */
    protected boolean isOnCol;
    /* indique si ouvert ou non */
    protected boolean isOpen;

    // CONSTRUCTOR
    public Visualization(long dbKey,String name, TypeVisualization type, int[] tabNo, boolean isOnCol) {
        this.dbKey = dbKey;
        this.name = name;
        this.type = type;
        this.tabNo = tabNo ;
        this.isOnCol = isOnCol;
        this.isOpen = true;
    }

    // GETTER AND SETTER
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public int[] getTabNo() {
        return tabNo;
    }

    public void setTabNo(int[] tabNo) {
        this.tabNo = tabNo;
    }

    public boolean isOnCol() {
        return isOnCol;
    }

    public void setOnCol(boolean isOnCol) {
        this.isOnCol = isOnCol;
    }

    public TypeVisualization getType() {
        return type;
    }

    public void setType(TypeVisualization type) {
        this.type = type;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    
    // CLONE
     @Override
    public Object clone()  {
        try {
            Visualization vis = (Visualization) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            TypeVisualization typeC = (TypeVisualization)this.type.clone();
            int[] tabNoC = new int[this.tabNo.length];
            for (int i=0; i<tabNo.length; i++){
                tabNoC[i] = tabNo[i];
            }
            boolean isOnColC = this.isOnCol ;
            vis.setDbKey(dbKeyC);
            vis.setName(nameC);
            vis.setType(typeC);
            vis.setTabNo(tabNoC);
            vis.setOnCol(isOnColC);
            vis.setOpen(isOpen);
            return vis;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

     //METHOD
     /* retourne vrai si la visualization porte sur un  numero donne */
     public boolean isOnNo(int no){
         for (int i=0; i<tabNo.length; i++){
             if (tabNo[i] == no)
                 return true;
         }
         return false;
     }

     /* affichage console */
    @Override
     public String toString(){
         String s = "";
         for (int i=0; i<tabNo.length; i++)
             s += tabNo[i] + " / ";
         String toString = this.getName()+ " ("+this.getType().getCode()+") on "+(this.isOnCol ? "col " : "row ") + s+"\n";
         return toString;
     }
    
}
