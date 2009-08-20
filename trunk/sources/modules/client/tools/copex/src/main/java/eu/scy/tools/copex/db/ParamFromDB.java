/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.db;

import eu.scy.tools.copex.common.CopexUnit;
import eu.scy.tools.copex.common.PhysicalQuantity;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;
import java.util.Locale;

/**
 * parametres de l'applications par exemple liste des unites
 * @author Marjolaine
 */
public class ParamFromDB {
    /* charge les grandeurs gerees dans COPEX */
    public static CopexReturn getAllPhysicalQuantitiesFromDB(DataBaseCommunication dbC,  Locale locale, ArrayList v) {
        ArrayList<PhysicalQuantity> listPhysicalQuantities = new ArrayList();
        String query = "SELECT ID_PHYSICAL_QUANTITY, QUANTITY_NAME FROM PHYSICAL_QUANTITY ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_PHYSICAL_QUANTITY");
        listFields.add("QUANTITY_NAME");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_PHYSICAL_QUANTITY");
            if (s==null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("QUANTITY_NAME");
            // unites correspondantes :
            ArrayList v3 = new ArrayList();
            cr = getAllUnitFromDB(dbC, dbKey, locale, v3);
            if (cr.isError())
                return cr;
            ArrayList<CopexUnit> listUnit = (ArrayList<CopexUnit>)v3.get(0);
            PhysicalQuantity quantity = new PhysicalQuantity(dbKey, name, listUnit);
            listPhysicalQuantities.add(quantity);
        }
        v.add(listPhysicalQuantities);
        return new CopexReturn();
    }

     /* charge les unites pour une grandeur gerees dans COPEX */
    public static CopexReturn getAllUnitFromDB(DataBaseCommunication dbC,  long dbKeyQ, Locale locale, ArrayList v) {
        ArrayList<CopexUnit> listUnit = new ArrayList();
        String symbol = "SYMBOL_"+locale.getLanguage() ;
        String query = "SELECT U.ID_UNIT, U.UNIT_NAME, U."+symbol+" FROM UNIT U, LINK_UNIT_QUANTITY L " +
                " WHERE L.ID_QUANTITY = "+dbKeyQ+" AND L.ID_UNIT = U.ID_UNIT ;"  ;
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("U.ID_UNIT");
        listFields.add("U.UNIT_NAME");
        listFields.add("U."+symbol);
       
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("U.ID_UNIT");
            if (s==null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("U.UNIT_NAME");
            String symb = rs.getColumnData("U."+symbol);
            CopexUnit unit = new CopexUnit(dbKey, name, symb) ;
            listUnit.add(unit);
        }
        v.add(listUnit);
        return new CopexReturn();
    }
}
