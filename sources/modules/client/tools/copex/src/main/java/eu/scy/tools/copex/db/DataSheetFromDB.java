/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.db;


import eu.scy.tools.copex.common.CopexData;
import eu.scy.tools.copex.common.DataSheet;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;

/**
 * Gestion de la feuille de donnees : table DATASHEET
 * @author MBO
 */
public class DataSheetFromDB {
    
    
    
    /* charge les datasheet d'un protocole */
    public static CopexReturn getAllDataSheetFromDB_xml(DataBaseCommunication dbC, ArrayList<LearnerProcedure> listProc,  ArrayList v) {
        int nbP = listProc.size();
        for (int p=0; p<nbP; p++){
            LearnerProcedure proc = listProc.get(p);
            long dbKeyProc = proc.getDbKey();
            // recuperation de la feuille de donnees associees au protocole
            String query = "SELECT D.ID_DATASHEET, D.NB_ROWS, D.NB_COL " +
                        "FROM DATASHEET D, LINK_DATASHEET_PROC L WHERE " +
                        "L.ID_PROC = "+dbKeyProc+" AND L.ID_DATASHEET = D.ID_DATASHEET ;";
               
            ArrayList v2 = new ArrayList();
            ArrayList<String> listFields = new ArrayList();
            listFields.add("D.ID_DATASHEET");
            listFields.add("D.NB_ROWS");
            listFields.add("D.NB_COL");
        
            CopexReturn cr = dbC.sendQuery(query, listFields, v2);
            if (cr.isError())
                return cr;
            int nbR = v2.size();
            for (int i=0; i<nbR; i++){
                ResultSetXML rs = (ResultSetXML)v2.get(i);
                if (rs == null)
                    System.out.println("rs null");
                String s = rs.getColumnData("D.ID_DATASHEET");
                if (s == null)
                    continue;
                long dbKey = Long.parseLong(s);
                s = rs.getColumnData("D.NB_ROWS");
                if (s == null)
                    continue;
                int nbRows = 0;
                try{
                    nbRows = Integer.parseInt(s);
                }catch(NumberFormatException e){
                    nbRows = 0;
                }
                s = rs.getColumnData("D.NB_COL");
                if (s == null)
                    continue;
                int nbCol = 0;
                try{
                    nbCol = Integer.parseInt(s);
                }catch(NumberFormatException e){
                    nbCol = 0;
                }
                DataSheet dataSheet = new DataSheet(dbKey, nbRows, nbCol);
                ArrayList v3 = new ArrayList();
                cr = getAllDataFromDB_xml(dbC, dbKey, nbRows, nbCol, v3);
                if (cr.isError())
                     return cr;
                 CopexData[][] data = (CopexData[][])v3.get(0);
                 dataSheet.setData(data);
                 proc.setDataSheet(dataSheet);
            } 
         }
            
         v.add(listProc);
         return new CopexReturn();
       
    }
    
    
    /* charge les donnees d'une feuille de donnees 
     * MBO le 27/10/08 : on ne charge que la premiere ligne pour le TP Clement
     */
    public static CopexReturn getAllDataFromDB_xml(DataBaseCommunication dbC, long dbKeyDataSheet, int nbRow, int nbCol,  ArrayList v) {
        CopexData[][] data = new CopexData[nbRow][nbCol];
        String query = "SELECT ID_DATA, ROW, COL, DATA " +
                        "FROM DATA WHERE " +
                        "ID_DATASHEET = "+dbKeyDataSheet+" AND ROW = 0;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_DATA");
        listFields.add("ROW");
        listFields.add("COL");
        listFields.add("DATA");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            if (rs == null)
                System.out.println("rs null");
            String s = rs.getColumnData("ID_DATA");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s); 
            s = rs.getColumnData("ROW");
            if (s == null)
                continue;
            int noRow =  0;
            try{
                 noRow = Integer.parseInt(s);
            }catch(NumberFormatException e){
                noRow = 0;
            }
            s = rs.getColumnData("COL");
            if (s == null)
                continue;
            int noCol = 0;
            try{
                noCol = Integer.parseInt(s);
            }catch(NumberFormatException e){
                noCol = 0;
            }
            String value = rs.getColumnData("DATA");
            if (value == null)
                continue;
            CopexData d = new CopexData(dbKey, value);
            data[noRow][noCol] = d;
        }
        v.add(data);
        return new CopexReturn();
    }
    
    
    /* creation d'une nouvelle feuille de donnees - en v[0] on met le nouvel id */
    public static CopexReturn createDataSheetInDB_xml(DataBaseCommunication dbC, long dbKeyProc, DataSheet datasheet, ArrayList v){
        String query = "INSERT INTO DATASHEET " +
                "(ID_DATASHEET, NB_ROWS, NB_COL) " +
                "VALUES (NULL, "+datasheet.getNbRows()+", "+datasheet.getNbColumns()+" );";
        String queryID = "SELECT max(last_insert_id(`ID_DATASHEET`))   FROM DATASHEET ;";  
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
       // creation lien avec le proc
        String queryLink = "INSERT INTO LINK_DATASHEET_PROC (ID_DATASHEET, ID_PROC) " +
                    "VALUES ("+dbKey+", "+dbKeyProc+") ;";
        String[]querys = new String[1];
        querys[0] = queryLink;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
       if (cr.isError())
           return cr;
        
        v.add(dbKey);
        return new CopexReturn();
        
    }
   
    /* mise a  jour d'une valeur d'une feuille de donnees */
    static public CopexReturn updateDataSheetInDB_xml(DataBaseCommunication dbC, long dbKeyDataSheet, long dbKeyData, String value, int noRow, int noCol, ArrayList v){
        value =  AccesDB.replace("\'",value,"''") ;
        String query = "";
        if (dbKeyData == -1){
            // CREATION
            query = "INSERT INTO DATA (ID_DATA, ID_DATASHEET, ROW, COL, DATA) " +
                        "VALUES (NULL, "+dbKeyDataSheet+", "+noRow+", "+noCol+", '"+value+"') ;";
            String queryID = "SELECT max(last_insert_id(`ID_DATA`))   FROM DATA ;";  
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
               return cr;
            dbKeyData = (Long)v2.get(0);
          }else{
               // MODIFICATION
               query = "UPDATE DATA SET DATA = '"+value+"' WHERE ID_DATA = "+dbKeyData+" ;";
               String[] querys = new String[1];
               querys[0] = query;
               ArrayList v2 = new ArrayList();
               CopexReturn cr = dbC.executeQuery(querys, v2);
               if (cr.isError())
                   return cr;
          }
            
        v.add(new CopexData(dbKeyData, value));
        return new CopexReturn();
    }
    
    
    
    /* suppression d'une feuille de donnees */
    static public CopexReturn deleteDataSheetFromDB_xml(DataBaseCommunication dbC, long dbKeyDataSheet){
        String queryLink = "DELETE FROM LINK_DATASHEET_PROC WHERE " +
                    "ID_DATASHEET = "+dbKeyDataSheet+" ;";
         // suppression des donnees
         String queryData = "DELETE FROM DATA WHERE " +
                    "ID_DATASHEET = "+dbKeyDataSheet+" ;";
         // suppression de la feuille
         String queryDel = "DELETE FROM DATASHEET WHERE " +
                    "ID_DATASHEET = "+dbKeyDataSheet+" ;";
         String[] querys = new String[3];
         querys[0] = queryLink ;
         querys[1] = queryData ;
         querys[2] = queryDel ;
         ArrayList v = new ArrayList();
         CopexReturn cr = dbC.executeQuery(querys, v);
         return cr;
    }
    
    
   
    
    /* suppression de donnees */
    static public CopexReturn removeDataInDB_xml(DataBaseCommunication dbC, long dbKeyData){
        String queryData = "DELETE FROM DATA WHERE " +
                    "ID_DATA = "+dbKeyData+" ;";
        
       String[] querys = new String[1];
       querys[0] = queryData ;
       ArrayList v = new ArrayList();
       CopexReturn cr = dbC.executeQuery(querys, v);
       return cr;
    }
    
    
    
    
    /* mise a  jour du nombre de lignes et de colonees */
    static public CopexReturn updateRowAndColInDB_xml(DataBaseCommunication dbC, long dbKeyDataSheet, int row, int col){
        String query = "UPDATE DATASHEET SET NB_ROWS = "+row +", " +
                    "NB_COL = "+col+" WHERE ID_DATASHEET = "+dbKeyDataSheet+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
}
