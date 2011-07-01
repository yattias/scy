/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * simple operation on a dataset (sum, avg, min, max)
 * An operation is defined by its type and the columns/rows where it applies
 * @author Marjolaine Bodin
 */
public class DataOperation implements Cloneable {
    public final static String TAG_OPERATION = "operation";
    private final static String TAG_OPERATION_ON_COL = "is_on_col";
    private final static String TAG_OPERATION_ID = "id";
    private final static String TAG_OPERATION_NAME = "name";

    /* dbkey identifier */
    protected long dbKey;
    /* name, it can be changed by the user */
    protected String name;
    /* type */
    protected TypeOperation typeOperation;
    /* on column ? if false, on row */
    protected boolean isOnCol;
    /* list of the no row/column on the operation applies */
    protected ArrayList<Integer> listNo;

    public DataOperation(long dbKey, String name, TypeOperation typeOperation, boolean isOnCol, ArrayList<Integer> listNo) {
        this.dbKey = dbKey;
        this.name = name;
        this.typeOperation = typeOperation;
        this.isOnCol = isOnCol;
        this.listNo = listNo;
    }

    public DataOperation(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_OPERATION)) {
            dbKey = -1;
            try{
                dbKey = Long.parseLong(xmlElem.getChild(TAG_OPERATION_ID).getText());
            }catch(NumberFormatException ex){
            }
            name = xmlElem.getChild(TAG_OPERATION_NAME).getText();
            typeOperation = new TypeOperation(xmlElem.getChild(TypeOperation.TAG_TYPE_OPERATION));
            this.isOnCol = xmlElem.getChild(TAG_OPERATION_ON_COL).getText().equals("true") ? true : false;
            this.listNo = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_OPERATION_ID).iterator(); variableElem.hasNext();) {
                int no=  new Integer(variableElem.next().getText());
                listNo.add(no);
            }
        }else {
            throw(new JDOMException("DataOperation expects <"+TAG_OPERATION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

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

    /* remove a no (row/column) on which the operation applies */
    public void removeNo(int no){
        this.listNo.remove(no);
    }


    /* to write on console for debug*/
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
         e.addContent(new Element(TAG_OPERATION_ID).setText(Long.toString(dbKey)));
         e.addContent(typeOperation.toXML());
         e.addContent(new Element(TAG_OPERATION_ON_COL).setText(isOnCol?"true":"false"));
         for(Iterator<Integer> i=listNo.iterator();i.hasNext();){
             e.addContent(new Element(TAG_OPERATION_ID).setText(Integer.toString(i.next())));
         }
         return e;
    }
    
}
