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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * parameters of the applications, for example units list
 * @author Marjolaine
 */
public class ParamFromDB {
     /** load all the physical quantities and the units
     * @param dbC database
     * @param locale
     * @param v v.get(0) contains ArrayList(PhysicalQuantity)
     * @return error code
     */
    public static CopexReturn getAllPhysicalQuantitiesFromDB(DataBaseCommunication dbC,  Locale locale, ArrayList v) {
        ArrayList<PhysicalQuantity> listPhysicalQuantities = new ArrayList();
        String l = "_"+locale.getLanguage();
        String query = "SELECT ID_PHYSICAL_QUANTITY, QUANTITY_NAME"+l+", QUANTITY_SYMBOL"+l+", ID_PHYSICAL_QUANTITY_REFERENCE, ID_UNIT_REFERENCE FROM PHYSICAL_QUANTITY ORDER BY QUANTITY_NAME"+l+" ;";
        ArrayList v2 = new ArrayList();
//        ArrayList<String> listFields = new ArrayList();
//        listFields.add("ID_PHYSICAL_QUANTITY");
//        listFields.add("QUANTITY_NAME");
        //CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        CopexReturn cr = dbC.sendQuery(query,  v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_PHYSICAL_QUANTITY");
            if (s==null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("QUANTITY_NAME"+l);
            String symbol = rs.getColumnData("QUANTITY_SYMBOL"+l);
            long dbKeyQttRef = -1;
            s= rs.getColumnData("ID_PHYSICAL_QUANTITY_REFERENCE");
            try{
                dbKeyQttRef = Long.parseLong(s);
            }catch(NumberFormatException e){

            }
            long dbKeyUnitRef = -1;
            s = rs.getColumnData("ID_UNIT_REFERENCE");
            try{
                dbKeyUnitRef = Long.parseLong(s);
            }catch(NumberFormatException e){
                
            }
            //units
            ArrayList v3 = new ArrayList();
            cr = getAllUnitFromDB(dbC, dbKey, locale, v3);
            if (cr.isError())
                return cr;
            List<CopexUnit> listUnit = (List<CopexUnit>)v3.get(0);
            // find the unit reference
            CopexUnit unitRef = getUnitRef(listUnit, dbKeyUnitRef);
            PhysicalQuantity p = null;
            if(dbKeyQttRef != -1){
                p = new PhysicalQuantity(dbKeyQttRef);
            }
            PhysicalQuantity quantity = new PhysicalQuantity(dbKey, CopexUtilities.getLocalText(name, locale), CopexUtilities.getLocalText(symbol, locale), listUnit, p, unitRef);
            listPhysicalQuantities.add(quantity);
        }
        // update physical qtt  ref
        for(Iterator<PhysicalQuantity> p = listPhysicalQuantities.iterator(); p.hasNext();){
            PhysicalQuantity qtt = p.next();
            PhysicalQuantity pref = qtt.getPhysicalQttRef();
            if(pref != null){
                qtt.setPhysicalQttRef(getPhysicalQuantityRef(listPhysicalQuantities, pref.getDbKey()));
            }
        }
        v.add(listPhysicalQuantities);
        return new CopexReturn();
    }

    private static CopexUnit getUnitRef(List<CopexUnit> listUnit, long dbKeyU){
        for(Iterator<CopexUnit> u = listUnit.iterator(); u.hasNext();){
            CopexUnit unit = u.next();
            if(unit.getDbKey() == dbKeyU)
                return unit;
        }
        return null;
    }
    private static PhysicalQuantity getPhysicalQuantityRef(List<PhysicalQuantity> list, long dbKey){
        for(Iterator<PhysicalQuantity> p = list.iterator(); p.hasNext();){
            PhysicalQuantity qtt = p.next();
            if(qtt.getDbKey() == dbKey)
                return qtt;
        }
        return null;
    }
     /** load all the unit for a physical quantity
     * @param dbC database
     * @param locale
     * @param dbKeyQ :id of the physical quantity
     * @param v v.get(0) contains List(CopexUnit)
     * @return error code
     */
    public static CopexReturn getAllUnitFromDB(DataBaseCommunication dbC,  long dbKeyQ, Locale locale, ArrayList v) {
        List<CopexUnit> listUnit = new LinkedList();
        String l = "_"+locale.getLanguage();
        String symbol = "SYMBOL"+l;
        String n = "UNIT_NAME"+l;
        String query = "SELECT U.ID_UNIT, U."+n+", U."+symbol+" , U.FACTOR FROM UNIT U, LINK_UNIT_QUANTITY L " +
                " WHERE L.ID_QUANTITY = "+dbKeyQ+" AND L.ID_UNIT = U.ID_UNIT ;"  ;
        ArrayList v2 = new ArrayList();
//        ArrayList<String> listFields = new ArrayList();
//        listFields.add("U.ID_UNIT");
//        listFields.add("U.UNIT_NAME");
//        listFields.add("U."+symbol);
//        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        CopexReturn cr = dbC.sendQuery(query,  v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("U.ID_UNIT");
            if (s==null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("U."+n);
            String symb = rs.getColumnData("U."+symbol);
            s = rs.getColumnData("U.FACTOR");
            double factor = Double.NaN;
            try{
                factor = Double.parseDouble(s);
            }catch(NumberFormatException e){
                
            }
            CopexUnit unit = new CopexUnit(dbKey, CopexUtilities.getLocalText(name, locale), CopexUtilities.getLocalText(symb, locale), factor) ;
            listUnit.add(unit);
        }
        v.add(listUnit);
        return new CopexReturn();
    }

    /** load the default type material
     * @param dbC database
     * @param locale
     * @param v v.get(0) contains TypeMaterial
     * @return error code
     */
    public static CopexReturn getDefaultTypeMaterialFromDB(DataBaseCommunication dbC, Locale locale, ArrayList v){
        TypeMaterial  type = null;
        String query = "SELECT ID_TYPE, TYPE_NAME FROM MATERIAL_TYPE WHERE DEFAULT_TYPE = 1 ;";
        ArrayList v2 = new ArrayList();
//        ArrayList<String> listFields = new ArrayList();
//        listFields.add("ID_TYPE");
//        listFields.add("TYPE_NAME");
//
//        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        CopexReturn cr = dbC.sendQuery(query,  v2);
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

    /** load all the material strategy
     * @param dbC database
     * @param locale
     * @param v v.get(0) contains ArrayList<MaterialStrategy>
     * @return error code
     */
    public static CopexReturn getAllStrategyMaterialFromDB(DataBaseCommunication dbC, Locale locale, ArrayList v){
        ArrayList<MaterialStrategy> listStrategy = new ArrayList();
        String query = "SELECT ID_STRATEGY, CODE, ITEM, ITEM_LIBELLE_"+locale.getLanguage()+", ADD_MAT, CHOOSE_MAT, COMMENTS_MAT FROM MATERIAL_STRATEGY ORDER BY CODE;";
        ArrayList v2 = new ArrayList();
//        ArrayList<String> listFields = new ArrayList();
//        listFields.add("ID_STRATEGY");
//        listFields.add("CODE");
//        listFields.add("ITEM");
//        listFields.add("ITEM_LIBELLE_"+locale.getLanguage());
//        listFields.add("ADD_MAT");
//        listFields.add("CHOOSE_MAT");
//        listFields.add("COMMENTS_MAT");
//        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        CopexReturn cr = dbC.sendQuery(query, v2);
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
