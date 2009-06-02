/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.print;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;


/**
 * impression PDF
 * @author Marjolaine
 */
public class PrintPDF {
    
    private EdPPanel edP;
    private String fileName;
    private CopexUser user;
    private CopexMission mission;
    private ExperimentalProcedure proc;
    private boolean printComment;
    private boolean printDataSheet;

    private Document document ;

    public PrintPDF(EdPPanel edP, String fileName, CopexUser user,CopexMission mission,  ExperimentalProcedure proc, boolean printComments, boolean printDataSheet) {
        this.edP = edP;
        this.fileName = fileName;
        this.mission = mission;
        this.user = user;
        this.proc = proc;
        this.printComment = printComments ;
        this.printDataSheet = printDataSheet ;
    }


    /* print */
    public CopexReturn printDocument() {
        try {
            document = new Document() ;

            File temp;
            temp = File.createTempFile(fileName, ".pdf", new File(System.getProperty("java.io.tmpdir")));
            System.out.println("fichier temporaire : "+temp.getPath());
            String printDate = CopexUtilities.dateToSQL(CopexUtilities.getCurrentDate());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(temp));
            writer.setPageEvent(new MyPDFEvent(writer, printDate ));
            
            //Ajout du pied de page
            /*Chunk cDate = new Chunk(printDate, getFontHeaderItalic());
			Paragraph para = new Paragraph(cDate);
			HeaderFooter footer = new HeaderFooter(para, false);
			footer.setAlignment(HeaderFooter.ALIGN_LEFT);
			document.setFooter(footer);*/

            document.open();
            //CopexReturn cr;
            CopexReturn cr = setHeader();
            if (cr.isError())
                return cr;
            if (proc != null){
                cr = printProc(proc);
                if (cr.isError())
                return cr;
            }
           if(printDataSheet){
                cr = printDataSheet(proc.getDataSheet());
                if (cr.isError())
                    return cr;
            }
            document.close();
            cr = displayPDF(temp);
            if(cr.isError())
                return cr;
            return new CopexReturn();
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        }catch(IOException ex){
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }

   


   

    private Font getNormalFont(){
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, Color.BLACK) ;
    }
    private Font getCommentFont(){
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.ITALIC, Color.BLACK) ;
    }
     private Font getFontHeaderItalic(){
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, Color.BLACK) ;
    }
     private Font getDataSheetFont(){
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, Color.BLACK) ;
    }
    

    /* logo copex*/
    private CopexReturn setHeader(){
        String name = (user.getUserFirstName() == null ?"" : user.getUserFirstName()+" ")+user.getUserName() ;
        String missionName = edP.getBundleString("LABEL_MISSION")+" : "+mission.getName() ;
        String procName = edP.getBundleString("LABEL_PROC")+" : "+proc.getName() ;
        try {
            String img = "logo-copex.gif" ;
            Image image = Image.getInstance(getClass().getResource( "/" +img));
            Table headerTable = new Table(2);
            headerTable.setBorderWidth(0);
            Cell imgCell = new Cell(image);
            imgCell.setBorder(0);
            Cell txtCell = new Cell();
            txtCell.setBorder(0);
            Chunk c1 = new Chunk(name+"\n",getNormalFont());
            Chunk c2 = new Chunk(missionName+"\n", getNormalFont());
            Chunk c3 = new Chunk(procName, getNormalFont());
            txtCell.add(c1);
            txtCell.add(c2);
            txtCell.add(c3);
            txtCell.setBorder(0);
            txtCell.setHorizontalAlignment(Element.ALIGN_RIGHT) ;
            headerTable.addCell(imgCell) ;
            headerTable.addCell(txtCell);
            document.add(headerTable);
            return new CopexReturn();
        } catch (BadElementException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        }catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }

    
    /* impression d'un protocole */
    private CopexReturn printProc(ExperimentalProcedure proc){
        try {
            Paragraph p0 = new Paragraph("\n");
            document.add(p0);
            // question
            Question question = proc.getQuestion() ;
            CopexReturn cr = setTask(question,0);
            if(cr.isError())
                return cr;
            cr = setChilds(proc, question,1);
            if (cr.isError())
                return cr;
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        } 

        return new CopexReturn();
    }

    private CopexReturn setTask(CopexTask task, int nbTab){
        try{
            
            //Image image = Image.getInstance(getTaskImg(task));
            Image image = Image.getInstance(getClass().getResource( "/" +getTaskImg(task)));
            image.setAlignment(Image.TEXTWRAP);
            Chunk q = new Chunk(getTaskDescription(task),getNormalFont());
            String comments =getTaskComment(task);
            boolean isComment = comments.length() > 0;
            Chunk c = new Chunk(comments, getCommentFont());
            Paragraph p1 = new Paragraph(q);
            Paragraph p2 = new Paragraph(c);
            Table taskTable = new Table(2+nbTab);
            taskTable.setBorderWidth(0);
            taskTable.setAlignment(Element.ALIGN_LEFT);


            Cell imgCell = new Cell();
            imgCell.setBorder(0);
            imgCell.add(image);
            Cell aCell=  new Cell("");
            aCell.setBorder(0);
            Cell descCell = new Cell();
            descCell.add(p1);
            descCell.setBorder(0);
            Cell commentCell = new Cell();
            commentCell.setBorder(0);
            commentCell.add(p2);

            for (int i=0; i<nbTab; i++){
                Cell indentCell=  new Cell("");
                indentCell.setBorder(0);
                taskTable.addCell(indentCell);
            }
            taskTable.addCell(imgCell);
            taskTable.addCell(descCell);
            if (isComment){
                for (int i=0; i<nbTab; i++){
                    Cell indentCell=  new Cell("");
                    indentCell.setBorder(0);
                    taskTable.addCell(indentCell);
                }
                taskTable.addCell(aCell);
                taskTable.addCell(commentCell);
            }
            float[] widths = new float[2+nbTab];
            for (int i=0; i<nbTab; i++){
                widths[i] = 10;
            }
            int id = nbTab ;
            widths[id] = 12;
            id++;
            widths[id] =190-15-(nbTab*10);
            taskTable.setWidths(widths);
            taskTable.setWidth(90);
            Paragraph p = new Paragraph();
            p.add(taskTable);
            p.setSpacingAfter(0);
            p.setSpacingBefore(0);
            document.add(p);
            return new CopexReturn();
        }catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }


    /* retourne le texte de la description de la tache */
    private String getTaskDescription(CopexTask task){
        if (task instanceof CopexAction ){
            return ((CopexAction)task).toDescription(edP);
        }else
            return task.getDescription() ;
    }

    
    private String getTaskComment(CopexTask task){
        String comm = "";
        if(!printComment)
            return comm;
        comm = (task.getComments() == null || task.getComments().length() == 0) ? "" : task.getComments()+" \n" ;
        if (task.isQuestion()){
            String hyp = ((Question)task).getHypothesis() ;
            if (hyp != null && hyp.length() > 0){
                comm += edP.getBundleString("LABEL_HYPOTHESIS")+" "+hyp+" \n";
            }
            String princ = ((Question)task).getGeneralPrinciple() ;
            if (princ != null && princ.length() > 0){
                comm += edP.getBundleString("LABEL_GENERAL_PRINCIPLE")+" : "+princ+" \n";
            }
         }
        return comm;
    }

    /* retourne l'image associée à la tache */
    private String getTaskImg(CopexTask task){
        String img = "";
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


    /* impression des enfants d'une tache */
    private CopexReturn setChilds(ExperimentalProcedure proc, CopexTask task, int level){
        ArrayList<CopexTask> listTask = getTaskListChild(proc, task);
        int nbT = listTask.size();
        int level2 = level+1;
        if (nbT > 0){
            for (int i=0; i<nbT; i++){
                CopexReturn cr= setTask(listTask.get(i), level);
                if (cr.isError())
                    return cr;
                cr = setChilds(proc, listTask.get(i), level2);
                if (cr.isError())
                    return cr;
            }
        }
        return new CopexReturn();
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

    /* impression de la feuille de données */
    private CopexReturn printDataSheet(DataSheet ds){
        if (ds == null)
            return new CopexReturn();
        try{
            int nbCols = ds.getNbColumns() ;
            int nbRows  = ds.getNbRows() ;
            
            PdfPTable dsTable = new PdfPTable(nbCols);
            //dsTable.setBorderWidth(1);
            for (int i=0; i<nbRows; i++){
                for (int j=0; j<nbCols; j++){
                    PdfPCell aCell = new PdfPCell();
                    if(i==0)
                        aCell.setBackgroundColor(new Color(247,204,102));
                    aCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    aCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    String s = ds.getDataAt(i, j) == null ? "\n" : ds.getDataAt(i, j).getData() ;
                    Chunk c = new Chunk(s, getDataSheetFont());
                    aCell.addElement(c);
                    dsTable.addCell(aCell);
                }
            }
            Chunk c = new Chunk("\n"+edP.getBundleString("LABEL_DATASHEET")+"\n",getNormalFont());
            document.add(c);
            document.add(dsTable);
            return new CopexReturn();
        }catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
        } 
    }

    public CopexReturn displayPDF(File file){
        Process p = null;
        String command = "";
        String fileNamePDF = file.getPath() ;
        System.out.println("displayPDF : "+fileNamePDF);
        //Ouverture par Acrobat Reader
       /* try {
            command = "AcroRd32 "+fileNamePDF;
            p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start",command});

        }catch (IOException e) {
            try{
                p = Runtime.getRuntime().exec("start "+command);
            }catch (IOException e2) {
                return new CopexReturn ("AcroRd32 n'est pas accessible  : "+command+
								"!\nVeuillez vérifier que le logiciel a été installé sur la machine !",false) ;
			
            }
        }
*/
        // TODO mac
        try{
            p = Runtime.getRuntime() .exec("rundll32 url.dll,FileProtocolHandler "+fileNamePDF);
        }catch(IOException e){
            return new CopexReturn ("AcroRd32 n'est pas accessible  : "+command+
								"!\nVeuillez vérifier que le logiciel a été installé sur la machine !",false) ;
        }
        // Il faut attendre...mais on n'est pas obligé
        try {
            p.waitFor() ;
        }
        catch (InterruptedException e) {
            return new CopexReturn("Affichage interrompu " + e, false);
        }
        return new CopexReturn();
    }
    
}
