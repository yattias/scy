/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.data;

import eu.scy.tools.copex.common.CopexUnit;
import eu.scy.tools.copex.common.PhysicalQuantity;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;

/**
 *
 * @author Marjolaine
 */
public class MyCopexData {
   
    /* chargement qtt physiques */
    public static CopexReturn getAllPhysicalQuantities(EdPPanel edP, ArrayList v){
        ArrayList<PhysicalQuantity> listQuant = new ArrayList();
        long id = 1;
        String name = null;
        do{
            name = edP.getBundleStringKey("PHYSICAL_QUANTITY_"+id) ;
            if (name != null){
                ArrayList v2 = new ArrayList();
                CopexReturn cr = getAllUnit(edP, id, v2);
                if(cr.isError())
                    return cr;
                ArrayList<CopexUnit> listUnit = (ArrayList<CopexUnit>)v2.get(0);
                PhysicalQuantity q = new PhysicalQuantity(id, name, listUnit);
                listQuant.add(q);
                id++;
            }
        }while(name != null);
        v.add(listQuant);
        return new CopexReturn();
    }

    /* chargement des unites*/
    private static CopexReturn getAllUnit(EdPPanel edP, long idQtt, ArrayList v){
        ArrayList<CopexUnit> listUnit = new ArrayList();
        int id = 1;
        String u = null;
        do{
            u = edP.getBundleStringKey("UNIT_"+idQtt+"_"+id);
            if(u != null){
                String symbol = edP.getBundleStringKey("UNIT_SYMB_"+idQtt+"_"+id);
                CopexUnit unit = new CopexUnit(id, u, symbol);
                listUnit.add(unit);
                id++;
            }
        }while (u != null);
        v.add(listUnit);
        return new CopexReturn();
    }

}
