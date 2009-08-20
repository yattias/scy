/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import eu.scy.tools.copex.common.CopexTask;
import eu.scy.tools.copex.common.ExperimentalProcedure;
import eu.scy.tools.copex.common.MaterialUseForProc;
import eu.scy.tools.copex.edp.CopexApplet;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
/**
 * protocole sous forme de xml
 * @author MBO
 */
public class XMLProcFile {
    // ATTRIBUTS
    /* applet */
    private CopexApplet applet;
    private ExperimentalProcedure proc;
    /* nom du fichier */
    private String fileName;
    private HttpURLConnection urlCon;
    private ObjectOutputStream out;
    private String dataXML = "";
    /* xml */
    private Document document;
    
    
    
    // CONSTRUCTEURS
    public XMLProcFile(CopexApplet applet, String fileName, ExperimentalProcedure proc) throws IOException{
        this.applet = applet;
        this.proc = proc;
        this.fileName = fileName;
        this.dataXML = "";
        try{
           URL urlPrint ; 
           urlPrint = new URL(applet.getCodeBase(), applet.getDirectorySaveXML()+"writeObject.php");
             
             //URL urlPrint = new URL(edP.getCodeBase(), "writeObject.php");
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
        setProtocole();
        closeFile();
    }

    public XMLProcFile(URL copexURL, String fileName, ExperimentalProcedure proc) throws IOException{
        this.proc = proc;
        this.fileName = fileName;
        this.dataXML = "";
        try{
           URL urlPrint ;
           urlPrint = new URL(copexURL, getDirectorySaveXML()+"writeObject.php");

             //URL urlPrint = new URL(edP.getCodeBase(), "writeObject.php");
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
        setProtocole();
        closeFile();
    }

     private String getDirectorySaveXML() {
       return "../editeurProtocole/proc/";
    }

    // METHODES
    /* ecriture */
    public void addString(String s) {
       dataXML += s+"\n" ;
    }
    
     /* fermeture fichier */
    public void closeFile(){
        try{
           addString(fileName);
           this.out.writeObject(this.dataXML);
           XMLOutputter fmt = new XMLOutputter();
            try {
                fmt.output(document, System.out);
                fmt.output(document, out);
            } catch (IOException ex) {
                Logger.getLogger(SaveXmlShowCase.class.getName()).log(Level.SEVERE, null, ex);
            }
           //this.out.writeObject(this.dataXML);
           BufferedReader reader = new BufferedReader(new InputStreamReader(this.urlCon.getInputStream(), "utf-8"));
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
    // ecrit le protocole
    public void setProtocole(){
        Procedure p = getProcedure();
        DataSheet ds = getDataSheet() ;
        List<MaterialUse> listMaterialUse = getListMaterialUse();
        System.out.println("construciton exp");
        ExperimentalProcedureXML expP = new ExperimentalProcedureXML(proc.getName(), ""+proc.getDbKey(), p, ds, listMaterialUse);
        System.out.println("creation document");
        document = new Document(expP.toXML());
    }

    // construction protocole
    private Procedure getProcedure(){
        LinkedList<Task> listSubTask = getSubTask(proc.getQuestion());

        Question question = new Question(""+proc.getQuestion().getDbKey(),
                proc.getQuestion().getName(), proc.getQuestion().getDescription(),
                proc.getQuestion().getComments(), proc.getQuestion().getTaskImage(),
                proc.getQuestion().getHypothesis(), proc.getQuestion().getGeneralPrinciple(),
                listSubTask);
        return new Procedure(question);
    }



    //construction feuille de donnees
    private DataSheet getDataSheet(){
        DataSheet ds = null;
        if(proc.getDataSheet() != null){
            LinkedList<Row> listRow = new LinkedList();
            int nbR = proc.getDataSheet().getNbRows() ;
            int nbC = proc.getDataSheet().getNbColumns();
            for (int i=0; i<nbR; i++){
                LinkedList<Column> listColumns = new LinkedList();
                for (int j=0; j<nbC; j++){
                    String s = proc.getDataSheet().getDataAt(i, j) == null ? "" : proc.getDataSheet().getDataAt(i, j).getData();
                    Column c = new Column(s);
                    listColumns.add(c);
                }
                Row r =new Row(listColumns);
                listRow.add(r);
            }
            ds = new DataSheet(""+proc.getDataSheet().getDbKey(), ""+proc.getDataSheet().getNbRows(), ""+proc.getDataSheet().getNbColumns(), listRow);
        }
        return ds;
    }

    //construction  liste material
    private LinkedList<MaterialUse> getListMaterialUse(){
        LinkedList<MaterialUse> listMaterialUse = null;
        ArrayList<MaterialUseForProc> listM = proc.getListMaterialUse();
        if (listM != null){
            int nb = listM.size();
            listMaterialUse = new LinkedList();
            for (int i=0; i<nb; i++){
                MaterialUse m = new MaterialUse(""+listM.get(i).getMaterial().getDbKey(),listM.get(i).getMaterial().getName(), listM.get(i).getJustification() );
                listMaterialUse.add(m);
            }
        }
        return listMaterialUse ;
    }

    // retourne la liste des enfants d'une tache
    private LinkedList<Task> getSubTask(CopexTask task){
        LinkedList<Task> listT = null;
        boolean hasChild = task.getDbKeyChild() != -1 ;
        if (hasChild){
            listT = new LinkedList();
            ArrayList<CopexTask> listChild = getChilds(task);
            int nbC = listChild.size();
            for (int i=0; i<nbC; i++){
                listT.add(getTask(listChild.get(i)));
            }

        }
        return listT;
    }

    // tache en tache XML
    private Task getTask(CopexTask task){
        String nature = "";
        String hyp = null;
        String princ = null;
        if (task.isAction())
            nature = "action";
        else if (task.isQuestion()){
            nature = "question";
            hyp = ((eu.scy.tools.copex.common.Question)task).getHypothesis() ;
            princ = ((eu.scy.tools.copex.common.Question)task).getGeneralPrinciple() ;
        }else if (task.isStep())
            nature = "step";
        LinkedList<Task> listSubTask = getSubTask(task);
        Task t = new Task(""+task.getDbKey(), nature, task.getName(), task.getDescription(),
                task.getComments(), task.getTaskImage(),hyp, princ, listSubTask );
        return t;
    }

    // retourne la liste des enfants
    private ArrayList<CopexTask> getChilds(CopexTask task){
        ArrayList<CopexTask> listT = new ArrayList();
        long dbKeyC = task.getDbKeyChild() ;
        if(dbKeyC != -1){
            CopexTask firstC = getTask(dbKeyC);
            if(firstC != null)
                listT.add(firstC);
            ArrayList<CopexTask> listB = getBrothers(firstC);
            int nbB = listB.size() ;
            for (int i=0; i<nbB; i++){
                listT.add(listB.get(i));
            }
        }
        return listT;
    }

    // retourne la tache avec ce dbKey
    private CopexTask getTask(long dbKey){
        int nbT = proc.getListTask().size() ;
        for (int i=0; i<nbT; i++){
            if(proc.getListTask().get(i).getDbKey() == dbKey)
                return proc.getListTask().get(i);
        }
        return null;
    }

    // retourne la liste des freres
    private ArrayList<CopexTask> getBrothers(CopexTask task){
        ArrayList<CopexTask> listB = new ArrayList();
        long dbKeyB = task.getDbKeyBrother() ;
        if (dbKeyB != -1){
            CopexTask t = getTask(dbKeyB);
            if(t!=null){
                listB.add(t);
                ArrayList<CopexTask> l = getBrothers(t);
                int nb = l.size() ;
                for (int i=0; i<nb; i++){
                    listB.add(l.get(i));
                }
            }
        }
        return listB ;
    }
    
    
   
}
