/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.print;

import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.MyConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;


/**
 * fichier HTML pour impression
 * TODO MBO : encodage des caracteres
 * @author MBO
 */

public class HTMLFile {
    // ATTRIBUTS
    /* edP */
    private EdPPanel edP;
    /* nom du fichier */
    private String fileName;
    private HttpURLConnection urlCon;
    private ObjectOutputStream out;
    private String dataPrint = "";
    
    
    
    // CONSTRUCTEURS
    public HTMLFile(EdPPanel edP, String fileName) throws IOException{
        this.edP = edP;
        this.fileName = fileName;
        this.dataPrint = "";
       /*try{
           URL urlPrint ; 
           urlPrint = new URL(edP.getCodeBase(), edP.getDirectoryPrint()+"writeObject.php");
             
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
        }*/
    }
    
    // METHODES
    
    
    /* ecriture */
    public void addString(String s) {
       dataPrint += s+"\n" ;
    }
    
    /*encodage caracteres */
    private String encode(String s){
        return s;
    }
    
    
    /* en tete */
    public void setHead(){
        // ajoute au debut le nom du fichier
        addString(fileName);
        addString("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'");
        addString("'http://www.w3.org/TR/html4/loose.dtd'>");
        addString("<HTML>");
        addString("<HEAD>");
        addString("<TITLE>COPEX PRINT</TITLE>");
        addString("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
        setCss();
        addString("</HEAD>");
        addString("<BODY   onLoad='window.print()' >");
        addString("<div id='print' class='print' >");
        setIcon();
    }
    
    /* fermeture body */
    public void closeBody(){
        addString("</div>");
        addString("</BODY>");
        addString("</HTML>");
    }
    /* fermeture fichier */
    public void closeFile(){
        try{
           this.out.writeObject(this.dataPrint);
           BufferedReader reader = new BufferedReader(new InputStreamReader(this.urlCon.getInputStream(), "utf8"));
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
   
    /*icone */
    public void setIcon(){
       addString("<p><img src='../../Images/logo-copex.gif' width='215' height='58'></p>");
       addString("<p>&nbsp;</p>");
    }
    
    /* nom et prénom de l'utilisateur */
    public void setUser(CopexUser copexUser){
        String firstName = "";
        if (copexUser.getUserFirstName() != null)
            firstName = copexUser.getUserFirstName()+" "; 
        addString("<p class='Style3'>"+firstName+copexUser.getUserName()+"</p>");
        addString("<p>&nbsp;</p>");
    }
    
    
    /* mise en place de la mission */
    public void setMission(CopexMission mission){
        String code = "";
        if (mission.getCode() != null)
            code = " "+mission.getCode();
        addString("<p class='Style3'>"+edP.getBundleString("PRINT_MISSION") +code+" :</p>");
        addString("<p class='Style5'>"+mission.getName() == null ? "" : mission.getName()+"</p>");// nom de la mission
        addString("<p class='Style5'>"+mission.getSumUp() == null ? "" : mission.getSumUp()+"</p>");
        
    }
    
    /* affichage de la liste du materiel */
    public void setListMaterial(ArrayList<Material> listMaterial){
        addString("<p class='Style3'>"+edP.getBundleString("PRINT_MATERIAL")+"</p> ");
        int nbM = listMaterial.size();
        for (int i=0; i<nbM; i++){
            setMaterial(listMaterial.get(i));
        }
    }
    /* materiel */
    private void setMaterial(Material material){
        addString("<img src='");
        if (material.isAdvisedLearner())
            addString("../../Images/Puce-mat_enseignant.png");
        else
            addString("../../Images/Puce-mat_ronde.png"); // Image 
        addString("' width='7' height='8'> " +material.toDisplay());
        addString("<p class='Style5'>"+material.getDescription()+"</p>");
        ArrayList<Parameter> listP = material.getListParameters();
        int nbP = listP.size();
        for (int i=0; i<nbP; i++){
            Parameter p = listP.get(i);
            addString(p.getName()+ " = "+p.getValue()+ " "+p.getUnit().getSymbol());
        }
        addString("<p>&nbsp;</p>");
    }
    
    
    /* affichage des protocoles */
    public void setProcedures(ArrayList<ExperimentalProcedure> listProc){
        int nbP = listProc.size();
        addString("<pp class='Style3'>"+edP.getBundleString("PRINT_PROCEDURE")+" </p> ");
        for (int i=0; i<nbP; i++){
            setProc(listProc.get(i));
        }
    }
    
    /* affichage d'un protocole */
    private void setProc(ExperimentalProcedure proc){
        addString("<p>&nbsp;</p>");
        addString("<p class='Style4'>"+proc.getName()); // NOM DU PROC
        setMaterialUsed(proc);
        addString("<p class='Style5'><img src='../../Images/Icone-AdT_question.png' width='24' height='19'>"+proc.getQuestion().getDescription()+"</p>"); // QUESTION 
        //addString("<blockquote>");
        setChilds(proc, proc.getQuestion());
        //addString("</blockquote>");
        setDataSheet(proc.getDataSheet());
    }
    
    // afficahge du matériel utilisé 
    private void setMaterialUsed(ExperimentalProcedure proc){
        addString("<p class='Style5'>");
        ArrayList<MaterialUseForProc> listMaterialUse = proc.getListMaterialUse();
        int nb = listMaterialUse.size();
        if (nb > 0)
            addString(edP.getBundleString("PRINT_MATERIAL_USE"));
        addString("<ul>");
        for (int i=0; i<nb; i++){
            String just = "";
            if (listMaterialUse.get(i).getJustification() != null && listMaterialUse.get(i).getJustification().length() > 0)
                just = " : "+listMaterialUse.get(i).getJustification();
            addString("<li style='list-style-type:none'>"+listMaterialUse.get(i).getMaterial().getName()+just+"</li>");
        }
        addString("</ul>");
        addString("</p>");
        addString("<p>&nbsp;</p>");
    }
    
    
    // affiche les enfants d'une tache 
    private void setChilds(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> listTask = getTaskListChild(proc, task);
        int nbT = listTask.size();
        if (nbT > 0){
            addString("<ul>");
            for (int i=0; i<nbT; i++){
                addString("<li style='list-style-type:none'>");
                String img = getTaskImg(listTask.get(i));
                String comm = getTaskComm(listTask.get(i));
                String taskimg = getTaskDraw(listTask.get(i)) ;
                addString("<img src='"+img+"' width='24' height='19'> "+getTaskDescription(listTask.get(i)));
                if (comm.length() > 0)
                    addString(comm);
                if (taskimg.length() > 0)
                    addString(taskimg);
                setChilds(proc, listTask.get(i));
                addString("</li>");
            }
            addString("</ul >");
        }
    }

    /* retourne le texte de la description de la tache */
    private String getTaskDescription(CopexTask task){
        if (task instanceof CopexAction ){
            return ((CopexAction)task).toDescription(edP) ;
        }else
            return task.getDescription() ;
    }
    /* retourne le commentaire de la tache */
    private String getTaskComm(CopexTask task){
        String comm = "";
        if (task.getComments() != null && task.getComments().length() > 0){
            String comment = task.getComments() ;
            if (task.isQuestion()){
                String hyp = ((Question)task).getHypothesis() ;
                if (hyp != null && hyp.length() > 0){
                   comment += "\n"+edP.getBundleString("LABEL_HYPOTHESIS")+" "+hyp;
               }
               String princ = ((Question)task).getGeneralPrinciple() ;
               if (princ != null && princ.length() > 0){
                   comment += "\n"+edP.getBundleString("LABEL_GENERAL_PRINCIPLE")+" : "+princ;
               }
            }
            comm = "</br><em>"+encode(comment)+"</em>";
        }
        return comm;
    }
    /* retourne le dessin associe à la tache */
    private String getTaskDraw(CopexTask task){
        String draw = "";
        if (task.getTaskImage() != null && task.getTaskImage().length() > 0){
            draw = "<img src='../../TaskImages/"+task.getTaskImage()+"' >" ;
        }
        return draw;
    }
    
    
    /* retourne l'image associée à la tache */
    private String getTaskImg(CopexTask task){
        String img = "../../Images/";
        if (task == null){
            System.out.println("ATTENTION TACHE NULL");
            return "";
        }
        if (task.isQuestion())
            img += "Icone-AdT_question.png";
        else if (task.isStep()){
            if (task.getDbKeyChild() == -1)
                img += "Icone-AdT_etape_warn.png";
            else if (task.getEditRight() == MyConstants.NONE_RIGHT ||task.getDeleteRight() == MyConstants.NONE_RIGHT ||task.getCopyRight() == MyConstants.NONE_RIGHT ||task.getMoveRight() == MyConstants.NONE_RIGHT ||task.getParentRight() ==MyConstants.NONE_RIGHT )
                img += "Icone-AdT_etape_lock.png";
            else
                img += "Icone-AdT_etape.png";
            
        }else if (task.isAction()){
            if (task.getEditRight() == MyConstants.NONE_RIGHT)
                img += "Icone-AdT_action_lock.png";
            else
                img += "Icone-AdT_action.png";
            
        }
        return img;
    }
    
    
    /* retourne la liste des taches enfants de la tache */
    private ArrayList<CopexTask> getTaskListChild(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> childs = new ArrayList();
        // premier enfant 
        long dbKeyFirstChild = task.getDbKeyChild();
        if (dbKeyFirstChild != -1){
            CopexTask t = getTask(proc.getListTask(), dbKeyFirstChild);
            if (t != null){
                 childs.add(t);
                // recupere les freres de t
                ArrayList<CopexTask> brothers = getTaskBrother(proc, t);
                int nbB = brothers.size();
                for (int j=0; j<nbB; j++){
                    childs.add(brothers.get(j));
                }
            }
        }
        return childs;
    }
    
    
    /* retourne la liste des freres de la tache */
    private ArrayList<CopexTask> getTaskBrother(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> brothers = new ArrayList();
        long idb = task.getDbKeyBrother();
        if (idb != -1){
            CopexTask tb = getTask(proc.getListTask(), idb);
            if (tb != null){
                brothers.add(tb);
                ArrayList<CopexTask> lb = getTaskBrother(proc, tb);
                int n = lb.size();
                for (int k=0; k<n; k++){
                    brothers.add(lb.get(k));
                }
            }
        }
        return brothers;
    }
    
    /* retourne la tache avec le bon id - null sinon */
    private CopexTask getTask(ArrayList<CopexTask> listTask, long dbKey){
        int nbT = listTask.size();
        for(int i=0; i<nbT; i++){
            if (listTask.get(i).getDbKey() == dbKey)
                return listTask.get(i);
        }
        return null;
    }
    
    
     /* mise en place du css */
    public void setCss(){
        addString("<style type='text/css'>");
        addString("<!--");
        addString(".Style3 {color: #FB9100 ;" + "font-size:16px;}");
        addString(".Style4 {color: #DB3C24};" + "font-size:14px;}");
        addString(".Style5 {color: #000000};" + "font-size:14px;}");
        addString(".print {font-size:14px;width=190px;}");
        addString("-->");
        addString("</style>");
    }
    
    private void setDataSheet(DataSheet dataSheet){
        if (dataSheet != null){
            addString("<p>&nbsp;</p>");
            addString("<p class='Style4'>"+edP.getBundleString("PRINT_DATASHEET")+" </p>"); // FEUILLE DE DONNEES 
            int nbR = dataSheet.getNbRows();
            int nbC = dataSheet.getNbColumns();
            if (nbR > 0){
                addString("<table border='1'>");
                for (int i=0; i<nbR; i++){
                    addString("<tr>");
                    for (int j=0; j<nbC; j++){
                        String data = "&nbsp;";
                        if (dataSheet.getDataAt(i, j) != null)
                            data = dataSheet.getDataAt(i, j).getData();
                        addString("<td>"+data+"</td>");
                    }
                    addString("</tr>");
                }
                addString("</table>");
            }
        }
    }
}
