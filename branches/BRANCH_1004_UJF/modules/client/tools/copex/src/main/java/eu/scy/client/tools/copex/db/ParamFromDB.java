/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import eu.scy.client.tools.copex.common.CopexUnit;
import eu.scy.client.tools.copex.common.MaterialStrategy;
import eu.scy.client.tools.copex.common.PhysicalQuantity;
import eu.scy.client.tools.copex.common.TypeMaterial;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
            List<CopexUnit> listUnit = (List<CopexUnit>)v3.get(0);
            PhysicalQuantity quantity = new PhysicalQuantity(dbKey, CopexUtilities.getLocalText(name, locale), listUnit);
            listPhysicalQuantities.add(quantity);
        }
        v.add(listPhysicalQuantities);
        return new CopexReturn();
    }

     /* charge les unites pour une grandeur gerees dans COPEX */
    public static CopexReturn getAllUnitFromDB(DataBaseCommunication dbC,  long dbKeyQ, Locale locale, ArrayList v) {
        List<CopexUnit> listUnit = new LinkedList();
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
            CopexUnit unit = new CopexUnit(dbKey, CopexUtilities.getLocalText(name, locale), CopexUtilities.getLocalText(symb, locale)) ;
            listUnit.add(unit);
        }
        v.add(listUnit);
        return new CopexReturn();
    }

    /* charge le type de material par default*/
    public static CopexReturn getDefaultTypeMaterialFromDB(DataBaseCommunication dbC, Locale locale, ArrayList v){
        TypeMaterial  type = null;
        String query = "SELECT ID_TYPE, TYPE_NAME FROM MATERIAL_TYPE WHERE DEFAULT_TYPE = 1 ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TYPE");
        listFields.add("TYPE_NAME");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TYPE");
            if (s==null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("TYPE_NAME");
            type = new TypeMaterial(dbKey, CopexUtilities.getLocalText(name, locale));
        }
        v.add(type);
        return new CopexReturn();
    }

    /* charge les strategies de materiel */
    public static CopexReturn getAllStrategyMaterialFromDB(DataBaseCommunication dbC, Locale locale, ArrayList v){
        ArrayList<MaterialStrategy> listStrategy = new ArrayList();
        String query = "SELECT ID_STRATEGY, CODE, ITEM, ITEM_LIBELLE_"+locale.getLanguage()+", ADD_MAT, CHOOSE_MAT, COMMENTS_MAT FROM MATERIAL_STRATEGY ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_STRATEGY");
        listFields.add("CODE");
        listFields.add("ITEM");
        listFields.add("ITEM_LIBELLE_"+locale.getLanguage());
        listFields.add("ADD_MAT");
        listFields.add("CHOOSE_MAT");
        listFields.add("COMMENTS_MAT");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_STRATEGY");
            if (s==null)
                continue;
            long dbKey = Long.parseLong(s);
            String code  = rs.getColumnData("CODE");
            s = rs.getColumnData("ITEM");
            boolean isItem = s.equals("1");
            String item = rs.getColumnData("ITEM_LIBELLE_"+locale.getLanguage());
            s = rs.getColumnData("ADD_MAT");
            boolean addMat = s.equals("1");
            s = rs.getColumnData("CHOOSE_MAT");
            boolean chooseMat = s.equals("1");
            s = rs.getColumnData("COMMENTS_MAT");
            boolean commentsMat = s.equals("1");
            MaterialStrategy strategy = new MaterialStrategy(dbKey, code, isItem, CopexUtilities.getLocalText(item, locale), addMat, chooseMat, commentsMat);
            listStrategy.add(strategy);
        }
        v.add(listStrategy);
        return new CopexReturn();
    }
}
