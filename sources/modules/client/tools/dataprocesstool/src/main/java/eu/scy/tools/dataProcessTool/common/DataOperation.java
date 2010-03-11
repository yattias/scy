/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;

/**
 * operation on a dataset
 * @author Marjolaine Bodin
 */
public class DataOperation implements Cloneable {
    private final static String TAG_OPERATION = "operation";
    private final static String TAG_OPERATION_TYPE = "type";
    private final static String TAG_OPERATION_ON_COL = "is_on_col";
    private final static String TAG_OPERATION_ID = "id";
    /* identifiant base de donnees */
    protected long dbKey;
    /* nom donne par l'utilisateur */
    protected String name;
    /* type */
    protected TypeOperation typeOperation;
    /* porte sur la colonne ? (si false : ligne) */
    protected boolean isOnCol;
    /* liste des no Row/ Col sur lesquelles l'operation s'applique */
    protected ArrayList<Integer> listNo;

    // CONSTRUCTOR
    public DataOperation(long dbKey, String name, TypeOperation typeOperation, boolean isOnCol, ArrayList<Integer> listNo) {
        this.dbKey = dbKey;
        this.name = name;
        this.typeOperation = typeOperation;
        this.isOnCol = isOnCol;
        this.listNo = listNo;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public boolean isOnCol() {
        return isOnCol;
    }

    public void setIsOnCol(boolean isOnCol) {
        this.isOnCol = isOnCol;
    }

    public ArrayList<Integer> getListNo() {
        return listNo;
    }

    public void setListNo(ArrayList<Integer> listNo) {
        this.listNo = listNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    
    public boolean isOnCol(int noCol){
        if(this.isOnCol()){
            for(Iterator<Integer> c = listNo.iterator(); c.hasNext();){
                if(c.next() == noCol)
                    return true;
            }
        }
        return false;
    }
    // CLONE
    @Override
    public Object clone()  {
        try {
            DataOperation operation = (DataOperation) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String (this.name);
            TypeOperation typeC = (TypeOperation)this.typeOperation.clone();
            boolean isOnColC = new Boolean(this.isOnCol);
            ArrayList<Integer> listNoC = null;
            if (listNo != null){
                listNoC = new ArrayList();
                int nb = this.listNo.size();
                for (int i=0; i<nb; i++){
                    listNoC.add(new Integer(listNo.get(i)));
                }
            }
            
            operation.setDbKey(dbKeyC);
            operation.setName(nameC);
            operation.setTypeOperation(typeC);
            operation.setIsOnCol(isOnColC);
            operation.setListNo(listNoC);
            
            return operation;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    /*suppression d'un numero */
    public void removeNo(int no){
        this.listNo.remove(no);
    }


    /*affichage console */
    @Override
    public String toString(){
        String toString = this.getName() + " ("+this.getTypeOperation().getCodeName()+") on "+(this.isOnCol ?  "col " : "row ")+"\n";
        for (int i=0; i<this.listNo.size(); i++){
            toString += this.listNo.get(i)+ " / ";
        }
        toString += "\n";
        return toString ;
    }

    public Element toXMLLog(){
         Element e = new Element(TAG_OPERATION);
         e.addContent(new Element(TAG_OPERATION_TYPE).setText(typeOperation.getCodeName()));
         e.addContent(new Element(TAG_OPERATION_ON_COL).setText(isOnCol?"true":"false"));
         for(Iterator<Integer> i=listNo.iterator();i.hasNext();){
             e.addContent(new Element(TAG_OPERATION_ID).setText(Integer.toString(i.next())));
         }
         return e;
    }
    
}
