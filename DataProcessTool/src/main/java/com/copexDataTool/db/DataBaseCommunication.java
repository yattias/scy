/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.copexDataTool.db;

import com.scy.scyDataTool.utilities.CopexReturn;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * permet de transmettre les données aux serveurs
 * @author MBO
 */
public class DataBaseCommunication {
    // CONSTANTES 
    /* requete de selection avec envoit du resultat */
    public final static int MODE_SELECT = 1;
    /* requete simple */
    public final static int MODE_SIMPLE = 2;
    /* insertion puis recuperation du dernier indice*/
    public final static int MODE_INSERT = 3;
    /* nom du champ pour recupere le dernier indice */
    public final static String LAST_ID = "LAST ID";
    
 
    // ATTRIBUTS
    /* applet */
    private ScyApplet applet ;
    private long idMission;
    private long idUser;
    /* base par défaut cd constantes DB_COPEX */
    private int db;

    /* nom du fichier */
    private String fileName;
    private HttpURLConnection urlCon;
    private ObjectOutputStream out;
    private String dataBD = "";

    public DataBaseCommunication(ScyApplet applet, int db, long idMission, long idUser) {
        this.applet = applet;
        this.dataBD = "";
        this.db = db;
        this.idMission = idMission;
        this.idUser = idUser;
        this.fileName = "db"+idMission+"-"+idUser+".xml";
    }
    
    // envoie une requete de selection 
    public CopexReturn sendQuery(String query, ArrayList listFields, ArrayList v){
        System.out.println("send Query : "+query+ " ("+db+")");
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
    //envoie d'une requete simple (par exemple delete)
    public CopexReturn executeQuery(String[] querys, ArrayList v){
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
    
    // envoie d'une requete d'insertion puis lecture du champ insere
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
    
    
    // connection au serveur 
    private CopexReturn openConnectionServer(int mode, long lenght) {
        //System.out.println("openConnectionServer");
        String file = "";
        if (mode == MODE_SELECT)
            file = "sendQuerySelect.php" ;
        else if (mode == MODE_SIMPLE)    
            file = "sendQuery.php";
        else if (mode == MODE_INSERT)
            file = "sendQueryInsert.php";
        try{
            URL urlDB ;
            urlDB = new URL(applet.getCodeBase(), applet.getDirectoryPhp()+file);
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
            System.out.println("IOException in HTMLFile : "+ioe.getMessage());
            System.out.println(ioe);
            return new CopexReturn(ioe.getMessage(), false);
        }
    }
    
    // envoi des données
    private CopexReturn sendData(){
        //System.out.println("sendData");
        try{
           this.out.writeObject(this.dataBD);
           BufferedReader reader = new BufferedReader(new InputStreamReader(this.urlCon.getInputStream(), "utf-8"));
           String ligne;
           while ((ligne = reader.readLine()) != null) {
               System.out.println(ligne);
           } 
           reader.close();
           this.out.flush();
           this.out.close();
           this.urlCon.disconnect();
           return new CopexReturn();
       }catch(IOException e){
           System.out.println("ERROR lors de l'ecriture du fichier : "+e);
           return new CopexReturn(e.getMessage(), false);
       }
    }
    
    // lecture de la réponse
    private CopexReturn receiveResponse(ArrayList v){
        try{
            URL urlDB;
            urlDB = new URL(applet.getCodeBase(), applet.getDirectoryData()+fileName);
            File fileToUpload = new File(applet.getDirectoryData()+fileName);
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
               //System.out.println("ligne lue : "+ligne);
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
        //System.out.println("getData : "+s);
        s = s.substring(0, s.length() - (balise.length()+3));
        //System.out.println("fin getData : "+s);
        return s;
    }
    
    /* requete qui permet l'insertion puis permet de recuperer l'id */
    public CopexReturn getNewIdInsertInDB(String query, String queryID,ArrayList v){
            ArrayList v2 = new ArrayList();
            String querys[] = new String[2];
            querys[0] = query;
            querys[1] = queryID ;
            CopexReturn cr  = insertQuery(querys, v2);
            if (cr.isError())
                return cr;
            long dbKey = -1;
            // recupere l'id :
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

    /* change la connexion */
    public CopexReturn updateDb(int db){
        this.db = db;
        return new CopexReturn();
    }
    /* retourne la base */
    public int getDb(){
        return this.db;
    }
}
