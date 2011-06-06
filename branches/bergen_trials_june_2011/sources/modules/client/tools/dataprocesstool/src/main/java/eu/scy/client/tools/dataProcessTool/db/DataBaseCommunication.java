/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.db;

import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * communication with the database (can not use jdbc)
 * @author 
 */
public class DataBaseCommunication {
    /* select query, with the result */
    public final static int MODE_SELECT = 1;
    /* simple query */
    public final static int MODE_SIMPLE = 2;
    /* insertion and retrieve the last id*/
    public final static int MODE_INSERT = 3;
    /* filed name to retrive the last id */
    public final static String LAST_ID = "LAST ID";


    /* URL */
    private URL codeBase;
    /* data directory */
    private String directoryData;
    /* php directory */
    private String directoryPhP;
    private long idMission;
    private String idUser;
    /* database cf constantes DB_COPEX */
    private int db;

    /*file name */
    private String fileName;
    private HttpURLConnection urlCon = null;
    private ObjectOutputStream out = null;
    private String dataBD = "";


    public DataBaseCommunication(URL copexURL,   int db, long idMission, String idUser) {
        this.codeBase = copexURL;
        this.directoryData = getDirectoryData();
        this.directoryPhP = getDirectoryPhp() ;
        this.dataBD = "";
        this.db = db;
        this.idMission = idMission;
        this.idUser = idUser;
        this.fileName = "db"+idMission+"-"+idUser+".xml";
    }


    private String getDirectoryPhp(){
        return "../tools_utilities/InterfaceServer/";
      }

     private String getDirectoryData(){
        return "../tools_utilities/InterfaceServer/data/";
      }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
        this.fileName = "db"+idMission+"-"+idUser+".xml";
    }

    // send a selection query
    public CopexReturn sendQuery(String query, ArrayList listFields, ArrayList v){
        dataBD = fileName+"\n";
        dataBD += "<data>"+this.db;
        dataBD += "<query>"+query+"</query>";
        int nbF = listFields.size();
        for (int k=0; k<nbF; k++){
            dataBD += "<field><name>'"+(String)listFields.get(k)+"'</name></field>";
        }
        CopexReturn cr = openConnectionServer(MODE_SELECT, dataBD.length());
        if (cr.isError())
            return cr;

        cr = sendData();
        if (cr.isError())
            return cr;
        cr = receiveResponse(v);
        return cr;
    }

     /** send a  query (delete)
     * @param query the specified query
     * @return error code
     */
    public CopexReturn executeQuery(String query){
        String[] querys = new String[1];
        querys[0] = query;
        return executeQuery(querys);
    }

    //send a simple query (eg delete)
    public CopexReturn executeQuery(String[] querys, ArrayList v){
        return executeQuery(querys);
    }
    //send a simple query (eg delete)
    public CopexReturn executeQuery(String[] querys){
         dataBD = fileName+"\n";
         dataBD += "<data>"+this.db;
        for (int i=0; i<querys.length; i++){
            dataBD += "<exe>";
            dataBD += "<query>"+querys[i]+"</query>";
             dataBD += "</exe>";
        }
        CopexReturn cr = openConnectionServer(MODE_SIMPLE, dataBD.length());
        if (cr.isError())
            return cr;
        cr = sendData();
        return cr;
    }

    // send insert query & read last id
    public CopexReturn insertQuery(String[] query, ArrayList v){
        dataBD = fileName+"\n";
        dataBD += "<data>"+this.db;
        for (int k=0; k<query.length; k++){
            dataBD += "<exe>";
            dataBD += "<query>"+query[k]+"</query>";
            dataBD += "</exe>";
        }
        CopexReturn cr = openConnectionServer(MODE_INSERT, dataBD.length());
        if (cr.isError())
            return cr;

        cr = sendData();
        if (cr.isError())
            return cr;
        cr = receiveResponse(v);
        return cr;
    }


    // server connection
    private CopexReturn openConnectionServer(int mode, long lenght) {
        //// System.out.println("openConnectionServer");
        String file = "";
        if (mode == MODE_SELECT)
            file = "sendQuerySelect.php" ;
        else if (mode == MODE_SIMPLE)
            file = "sendQuery.php";
        else if (mode == MODE_INSERT)
            file = "sendQueryInsert.php";
        try{
            URL urlDB ;
            urlDB = new URL(codeBase, directoryPhP+file);
            urlCon = (HttpURLConnection)urlDB.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("Content-Length", Long.toString(lenght));
            urlCon.connect();
            this.out = new ObjectOutputStream(urlCon.getOutputStream());
            return new CopexReturn();

        }catch(IOException ioe){
            // System.out.println("IOException in HTMLFile : "+ioe.getMessage());
            // System.out.println(ioe);
            return new CopexReturn(ioe.getMessage(), false);
        }
    }

    // send data
    private CopexReturn sendData(){
        //// System.out.println("sendData");
        try{
           this.out.writeObject(this.dataBD);
           BufferedReader reader = new BufferedReader(new InputStreamReader(this.urlCon.getInputStream(), "utf-8"));
           String ligne;
           while ((ligne = reader.readLine()) != null) {
              // // System.out.println(ligne);
           }
           reader.close();
           this.out.flush();
           this.out.close();
           this.urlCon.disconnect();
           return new CopexReturn();
       }catch(IOException e){
           // System.out.println("ERROR lors de l'ecriture du fichier : "+e);
           return new CopexReturn(e.getMessage(), false);
       }
    }

    // read result
    private CopexReturn receiveResponse(ArrayList v){
        try{
            URL urlDB;
            urlDB = new URL(codeBase, directoryData+fileName);
            File fileToUpload = new File(directoryData+fileName);
           urlCon = (HttpURLConnection)urlDB.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("Content-Length", Long.toString(fileToUpload.length()));
            urlCon.setRequestProperty("filename", fileName); // send the filename through HTTP header
            urlCon.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.urlCon.getInputStream(), "utf-8"));
            String ligne;
            ResultSetXML rs  = null;
            String name = "";
            String value = "";
           while ((ligne = reader.readLine()) != null) {
               //// System.out.println("ligne lue : "+ligne);
               if (ligne.equals("<res>")){
                    rs = new ResultSetXML();
               }else if (ligne.equals("</res>")){
                   v.add(rs);
                   rs = null;
               }else if (ligne.startsWith("<name>")){
                   name = getName(ligne);
               }else if (ligne.startsWith("<value>")){
                   if (ligne.endsWith("</value>"))
                        value = getValue(ligne);
                   else{
                       value = ligne.substring(7);
                   }
                }else if (ligne.equals("</col>")){
                   ColumnData data = new ColumnData(name, value);
                    rs.addData(data);
                }else {
                   if (ligne.endsWith("</value>")){
                       value += " \n"+ligne.substring(0, ligne.length() - 8);
                   }else
                       value += " \n"+ligne;
                }
           }
            reader.close();
            urlCon.disconnect();
            return new CopexReturn();
        }catch(IOException e){
            return new CopexReturn(e.getMessage(), false);
        }
    }

    private String getName(String ligne){
        return getData(ligne, "name");
    }
    private String getValue(String ligne){
        return getData(ligne, "value");
    }
    private String getData(String ligne, String balise){
        String s = ligne.substring(balise.length()+2);
        //// System.out.println("getData : "+s);
        s = s.substring(0, s.length() - (balise.length()+3));
        //// System.out.println("fin getData : "+s);
        return s;
    }

    /* insert and get the last id */
    public CopexReturn getNewIdInsertInDB(String query, String queryID,ArrayList v){
            ArrayList v2 = new ArrayList();
            String querys[] = new String[2];
            querys[0] = query;
            querys[1] = queryID ;
            CopexReturn cr  = insertQuery(querys, v2);
            if (cr.isError())
                return cr;
            long dbKey = -1;
            // get id
            int nbR = v2.size();
            for (int j=0; j<nbR; j++){
                ResultSetXML rs = (ResultSetXML)v2.get(j);
                String s = rs.getColumnData(DataBaseCommunication.LAST_ID);
                if (s == null)
                    continue;
                try{
                    dbKey = Long.parseLong(s);
                }catch(NumberFormatException e){

                }
             }
            v.add(dbKey);
            return new CopexReturn();
    }

    /* update the connexion */
    public CopexReturn updateDb(int db){
        this.db = db;
        return new CopexReturn();
    }
    /* returns the database */
    public int getDb(){
        return this.db;
    }
}
