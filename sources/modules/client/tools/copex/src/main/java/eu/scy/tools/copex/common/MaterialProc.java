/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.utilities.CopexUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * list material used
 * @author Marjolaine
 */
public class MaterialProc implements Cloneable {
    private List<MaterialUsed> listMaterialUsed;

    public MaterialProc(List<MaterialUsed> listMaterialUsed) {
        this.listMaterialUsed = listMaterialUsed;
    }

    public List<MaterialUsed> getListMaterialUsed() {
        return listMaterialUsed;
    }

    public void setListMaterialUsed(List<MaterialUsed> listMaterialUsed) {
        this.listMaterialUsed = listMaterialUsed;
    }


    @Override
    public Object clone()  {
        try {
            MaterialProc m = (MaterialProc) super.clone() ;
            ArrayList<MaterialUsed> l = null;
            if(this.listMaterialUsed !=null){
                l = new ArrayList();
                int nb = listMaterialUsed.size();
                for (int i=0; i<nb; i++){
                    l.add((MaterialUsed)listMaterialUsed.get(i).clone());
                }
            }
            m.setListMaterialUsed(l);
            return m;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public String toTreeString(Locale locale){
        String s = "";
        int nb = listMaterialUsed.size();
        for (int k=0; k<nb; k++){
            if(listMaterialUsed.get(k).isUsed()){
                s += CopexUtilities.getText(listMaterialUsed.get(k).getMaterial().getListName(), locale);
                if(k< nb-1){
                    s +="\n";
                }
            }
        }
        return s;
    }

}
