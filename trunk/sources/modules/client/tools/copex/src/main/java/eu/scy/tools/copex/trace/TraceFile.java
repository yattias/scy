/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import eu.scy.tools.copex.edp.CopexApplet;
import eu.scy.tools.copex.edp.EdPPanel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * fichier trace
 * @author MBO
 */
public class TraceFile {
    // ATTRIBUTS
    /* applet */
    private CopexApplet applet;
    /* nom du fichier */
    private String fileName;
    private HttpURLConnection urlCon;
    private ObjectOutputStream out;
    private String dataTrace = "";
    
    // CONSTRUCTEURS
    public TraceFile(CopexApplet applet, String fileName) throws IOException{
        this.applet = applet;
        this.fileName = fileName;
        this.dataTrace = "";
       try{
            URL urlPrint = new URL(applet.getCodeBase(), applet.getDirectoryTrace()+"writeTrace.php");
            urlCon = (HttpURLConnection)urlPrint.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.connect();
            this.out = new ObjectOutputStream(urlCon.getOutputStream());
            
            
           
        }catch(IOException ioe){
            System.out.println("IOException in HTMLFile : "+ioe.getMessage());
            System.out.println(ioe);
            throw ioe;
        }
    }
    
    // METHODES
    
    
    /* ecriture */
    private void addString(String s) {
       dataTrace += s+"\n" ;
    }
    
    /* ajout d'une action */
    public void addAction(String actionXML){
        addString(fileName);
        addString(actionXML);
    }
    
    /* fermeture fichier */
    public void closeFile(){
        try{
           this.out.writeObject(this.dataTrace);
           BufferedReader reader = new BufferedReader(new InputStreamReader(this.urlCon.getInputStream()));
           String ligne;
           while ((ligne = reader.readLine()) != null) {
               System.out.println(ligne);
           } 
           this.out.flush();
           this.out.close();
           this.urlCon.disconnect();
       }catch(IOException e){
           System.out.println("ERROR lors de l'ecriture du fichier : "+e);
       }
    }
}
