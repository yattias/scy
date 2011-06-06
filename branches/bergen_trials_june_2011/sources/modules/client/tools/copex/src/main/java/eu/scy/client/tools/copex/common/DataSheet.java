/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * datasheet of the question: list of prod data
 * @author MBO
 */
public class DataSheet implements Cloneable {

    private List<QData> listDataProd;

    public DataSheet(List<QData> listDataProd) {
        this.listDataProd = listDataProd;
    }

    
    public List<QData> getListDataProd() {
        return listDataProd;
    }

    public void setListDataProd(List<QData> listDataProd) {
        this.listDataProd = listDataProd;
    }

    @Override
    public Object clone()  {
        try {
            DataSheet ds = (DataSheet) super.clone() ;
            List<QData> listC = null;
            if(listDataProd != null){
                listC = new LinkedList();
                int nb = listDataProd.size();
                for (int i=0; i<nb; i++){
                    listC.add((QData)(listDataProd.get(i)).clone());
                }
            }
            ds.setListDataProd(listC);
            return ds;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public String toTreeString(Locale locale){
        String s = "";
        if(listDataProd == null)
            return s;
        int nb = listDataProd.size();
        for (int k=0; k<nb; k++){
            if(listDataProd.get(k) instanceof QData){
                s += ((QData)listDataProd.get(k)).getName(locale);
                if(k< nb-1){
                    s +="\n";
                }
            }
        }
        return s;
    }
    
    
}
