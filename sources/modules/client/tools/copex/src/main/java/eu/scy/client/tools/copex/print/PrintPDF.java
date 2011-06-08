/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.print;

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
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexTreeCellRenderer;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * PDF export
 * @author Marjolaine
 */
public class PrintPDF {
    
    private CopexPanel copex;
    private String fileName;
    private CopexGroup group;
    private CopexMission mission;
    private ExperimentalProcedure proc;
    private CopexTeacher teacher;

    private Document document ;

    public PrintPDF(CopexPanel copex, String fileName, CopexGroup group,CopexMission mission,  ExperimentalProcedure proc) {
        this.copex = copex;
        this.fileName = fileName;
        this.mission = mission;
        this.group = group;
        this.proc = proc;
        this.teacher = null;
    }


    public PrintPDF(CopexPanel copex, String fileName, CopexTeacher teacher,CopexMission mission,  ExperimentalProcedure proc) {
        this.copex = copex;
        this.fileName = fileName;
        this.mission = mission;
        this.group = null;
        this.teacher = teacher;
        this.proc = proc;
        this.teacher = null;
    }


    /* print */
    public CopexReturn printDocument() {
        try {
            document = new Document() ;

            File temp;
            temp = File.createTempFile(fileName, ".pdf", new File(System.getProperty("java.io.tmpdir")));
            //// System.out.println("fichier temporaire : "+temp.getPath());
            String printDate = CopexUtilities.dateToSQL(CopexUtilities.getCurrentDate());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(temp));
            writer.setPageEvent(new MyPDFEvent(writer, printDate ));
            
            // add the footer
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
           document.close();
            cr = displayPDF(temp);
            if(cr.isError())
                return cr;
            return new CopexReturn();
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }catch(IOException ex){
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }

    private Font getNormalFont(){
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont("c:\\WINDOWS\\fonts\\times.ttf", BaseFont.IDENTITY_H, true);
            Font font = new Font(baseFont);
            return font;
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        }


        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, Color.BLACK) ;
    }
    private Font getCommentFont(){
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont("c:\\WINDOWS\\fonts\\times.ttf", BaseFont.IDENTITY_H, true);
            Font font = new Font(baseFont);
            return font;
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.ITALIC, Color.BLACK) ;
    }
    private Font getTitleFont(){
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont("c:\\WINDOWS\\fonts\\times.ttf", BaseFont.IDENTITY_H, true);
            Font font = new Font(baseFont);
            return font;
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, CopexTreeCellRenderer.TITLE_NODE_COLOR) ;
    }

     private Font getFontHeaderItalic(){
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont("c:\\WINDOWS\\fonts\\times.ttf", BaseFont.IDENTITY_H, true);
            Font font = new Font(baseFont);
            return font;
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
         return FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, Color.BLACK) ;
    }
     private Font getDataSheetFont(){
       BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont("c:\\WINDOWS\\fonts\\times.ttf", BaseFont.IDENTITY_H, true);
            Font font = new Font(baseFont);
            return font;
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
         return FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, Color.BLACK) ;
    }
    

    /* logo copex*/
    private CopexReturn setHeader(){
        String name = "";
        if(group != null){
            for(Iterator<CopexLearner> l = group.getLearners().iterator();l.hasNext();){
                CopexLearner learner = l.next();
                name += (learner.getUserFirstName() == null ?"":learner.getUserFirstName()+" ")+learner.getUserName()+";";
            }
            if(name.length() > 0){
                name = name.substring(0, name.length()-1);
            }
        }else if (teacher != null){
            name = (teacher.getUserFirstName() == null ?"":teacher.getUserFirstName()+" ")+teacher.getUserName() ;
        }
        String missionName = copex.getBundleString("LABEL_MISSION")+" "+mission.getName() ;
        String procName = copex.getBundleString("LABEL_PROC")+" "+proc.getName(copex.getLocale()) ;
        try {
            String img = "logo-copex.gif" ;
            Image image = Image.getInstance(getClass().getResource( "/Images/" +img));
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
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }

    
    /* pdf a procedure */
    private CopexReturn printProc(ExperimentalProcedure proc){
        try {
            Paragraph p0 = new Paragraph("\n");
            document.add(p0);
            // question
            CopexReturn cr= setQuestion();
            if(cr.isError())
                return cr;
            cr = setHypothesis();
            if(cr.isError())
                return cr;
            cr = setGeneralPrinciple();
            if(cr.isError())
                return cr;
            cr = setMaterial();
            if(cr.isError())
                return cr;
            cr = setManipulation();
            if(cr.isError())
                return cr;
            cr = setDatasheet();
            if(cr.isError())
                return cr;
            cr = setEvaluation();
            if(cr.isError())
                return cr;
        } catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } 

        return new CopexReturn();
    }

    private CopexReturn setQuestion(){
//        try{
//            Chunk q = new Chunk(proc.getQuestion().getDescription(copex.getLocale()),getNormalFont());
//            String comments =proc.getQuestion().getComments(copex.getLocale());
//            if(comments == null)
//                comments = "";
//            boolean isComment = comments != null&& comments.length() > 0;
//            Chunk c = new Chunk(comments, getCommentFont());
//            Paragraph p1 = new Paragraph(q);
//            Paragraph p2 = new Paragraph(c);
//
//            Paragraph p = new Paragraph();
//            p.add(p1);
//            if(isComment)
//                p.add(p2);
//            p.setSpacingAfter(0);
//            p.setSpacingBefore(0);
//            document.add(p);
//            return new CopexReturn();
//        }catch (DocumentException ex) {
//            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
//            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
//        }
        return setText("icone_AdT_question.png", copex.getBundleString("TREE_QUESTION"),proc.getQuestion().getDescription(copex.getLocale()), proc.getQuestion().getComments(copex.getLocale()));
   }

    private CopexReturn setHypothesis(){
        boolean isHypothesis = proc.getHypothesis() != null && !proc.getHypothesis().isHide();
        if(isHypothesis){
            return setText("icone_AdT_hypothese.png", copex.getBundleString("TREE_HYPOTHESIS"),proc.getHypothesis().getHypothesis(copex.getLocale()), proc.getHypothesis().getComment(copex.getLocale()));
        }else 
            return new CopexReturn();
    }
    
    private CopexReturn setGeneralPrinciple(){
        boolean isPrinciple = proc.getGeneralPrinciple() != null && !proc.getGeneralPrinciple().isHide();
        if(isPrinciple){
            return setText("icone_AdT_principe.png", copex.getBundleString("TREE_GENERAL_PRINCIPLE"),proc.getGeneralPrinciple().getPrinciple(copex.getLocale()),proc.getGeneralPrinciple().getComment(copex.getLocale()));
        }else 
            return new CopexReturn();
    }

    private CopexReturn setEvaluation(){
        boolean isEvaluation = proc.getEvaluation() != null && !proc.getEvaluation().isHide();
        if(isEvaluation){
            return setText("icone_AdT_eval.png", copex.getBundleString("TREE_EVALUATION"),proc.getEvaluation().getEvaluation(copex.getLocale()),proc.getEvaluation().getComment(copex.getLocale()));
        }else
            return new CopexReturn();
    }


    private CopexReturn setDatasheet(){
        boolean isDatasheet  = proc.getDataSheet() != null;
        if(isDatasheet){
            return setText("icone_AdT_datasheet.png", copex.getBundleString("TREE_DATASHEET"), proc.getDataSheet().toTreeString(copex.getLocale()), null);
        }else
            return new CopexReturn();
    }

    private CopexReturn setMaterial(){
        boolean isMaterial = proc.getMaterials() != null;
        if(isMaterial){
            //return setText("icone_AdT_material.png",copex.getBundleString("TREE_MATERIAL"),  proc.getMaterials().toTreeString(copex.getLocale()));
            CopexReturn cr = setText("icone_AdT_material.png",copex.getBundleString("TREE_MATERIAL"),  "", null);
            if(cr.isError())
                return cr;
            cr = setList(proc.getMaterials().getListTree(copex.getLocale()));
            return cr;
        }else{
            return new CopexReturn();
        }
    }

    private CopexReturn setManipulation(){
        String icone = "icone_AdT_manip.png";
        if(proc.isTaskProc())
            icone = "icone_AdT_manip_tasks.png";
        setText(icone, copex.getBundleString("TREE_MANIPULATION"), "", null);
        CopexReturn cr = setChilds(proc, proc.getQuestion(),1);
        return cr;
    }

    private CopexReturn setList(List<String> list){
       try{
           Table table = new Table(5);
           table.setBorderWidth(0);
           table.setAlignment(Element.ALIGN_LEFT);
           
           int nb = list.size();
           int nbRow = 0;
           if(nb%2 == 1){
               nbRow =nb/4+1;
           }else
               nbRow = nb/4;

           for(int i=0; i<nbRow; i++){
               Cell indentCell=  new Cell("");
                indentCell.setBorder(0);
                table.addCell(indentCell);
               for (int j=1; j<5; j++){
                       int k = i+(j-1)*nbRow;
                       Chunk c;
                       if(k < nb){
                           c = new Chunk(list.get(k), getNormalFont());
                       }else{
                           c = new Chunk("", getNormalFont());
                       }
                       Paragraph p = new Paragraph(c);
                       Cell aCell = new Cell();
                       aCell.setBorder(0);
                       aCell.add(p);
                       table.addCell(aCell);
               }
           }
           float[] widths = new float[5];
           widths[0] = 13;
           widths[1] = 85;
           widths[2] =85;
           widths[3] = 85;
           widths[4] =85;
           table.setWidths(widths);
           table.setWidth(90);
           Paragraph p = new Paragraph();
           p.add(table);
           p.setSpacingAfter(0);
           p.setSpacingBefore(0);
           document.add(p);
           return new CopexReturn();

       }catch (DocumentException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }


    private CopexReturn setText(String img,String title, String text){
        try{
            Image image = Image.getInstance(getClass().getResource( "/Images/" +img));
            image.setAlignment(Image.TEXTWRAP);
            Chunk q = new Chunk(title,getCommentFont());
            Chunk c = new Chunk(text, getNormalFont());
            Paragraph p1 = new Paragraph(q);
            Paragraph p2 = new Paragraph(c);
            Table taskTable = new Table(3);
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

            Cell indentCell=  new Cell("");
            indentCell.setBorder(0);
            taskTable.addCell(indentCell);
            taskTable.addCell(imgCell);
            taskTable.addCell(descCell);

            indentCell=  new Cell("");
            indentCell.setBorder(0);
            taskTable.addCell(indentCell);
            taskTable.addCell(aCell);
            taskTable.addCell(commentCell);
            float[] widths = new float[3];
            widths[0] = 10;
            widths[1] = 12;
            widths[2] =190-15-10;
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
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }


    private CopexReturn setText(String img,String title, String text, String comment){
        if(comment == null)
            comment = "";
        boolean isComment = comment != null&& comment.length() > 0;
        try{
            Image image = Image.getInstance(getClass().getResource( "/Images/" +img));
            image.setAlignment(Image.TEXTWRAP);
            Chunk ti = new Chunk(title,getTitleFont());
            Chunk te = new Chunk(text, getNormalFont());
            Chunk co  = new Chunk(comment, getCommentFont());
            Paragraph p1 = new Paragraph(ti);
            Paragraph p2 = new Paragraph(te);
            Paragraph p3 = new Paragraph(co);
            Table taskTable = new Table(2);
            taskTable.setBorderWidth(0);
            taskTable.setAlignment(Element.ALIGN_LEFT);

            Cell imgCell = new Cell();
            imgCell.setBorder(0);
            imgCell.add(image);
            Cell aCell=  new Cell("");
            aCell.setBorder(0);
            Cell titleCell = new Cell();
            titleCell.add(p1);
            titleCell.setBorder(0);
            Cell textCell = new Cell();
            textCell.setBorder(0);
            textCell.add(p2);
            Cell commentCell = new Cell();
            commentCell.setBorder(0);
            commentCell.add(p3);

            taskTable.addCell(imgCell);
            taskTable.addCell(titleCell);

//            indentCell=  new Cell("");
//            indentCell.setBorder(0);
//            taskTable.addCell(indentCell);
            taskTable.addCell(aCell);
            taskTable.addCell(textCell);
            if(isComment){
                taskTable.addCell(aCell);
                taskTable.addCell(commentCell);
            }

            float[] widths = new float[2];
            widths[0] = 12;
            widths[1] = 190-15;
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
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }

    

    private CopexReturn setTask(CopexTask task, int nbTab){
        try{
            Image image = Image.getInstance(getClass().getResource( "/Images/" +getTaskImg(task)));
            image.setAlignment(Image.TEXTWRAP);
            Chunk q = new Chunk(getTaskDescription(task),getNormalFont());
            String comments =getTaskComment(task);
            if(comments == null)
                comments = "";
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
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        } catch (IOException ex) {
            Logger.getLogger(PrintPDF.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }


    /* returns the text of the description of a task */
    private String getTaskDescription(CopexTask task){
        if (task instanceof CopexAction ){
            return ((CopexAction)task).toDescription(copex);
        }else
            return task.getDescription(copex.getLocale()) ;
    }

    
    private String getTaskComment(CopexTask task){
        String comm = "";
        comm = (task.getComments(copex.getLocale()) == null || task.getComments(copex.getLocale()).length() == 0) ? "" : task.getComments(copex.getLocale())+" \n" ;
        return comm;
    }

    /* returns the task image */
    private String getTaskImg(CopexTask task){
        String img = "";
        if (task == null){
            // System.out.println("ATTENTION TACHE NULL");
            return "";
        }
        if (task.isStep()){
            if(proc.isTaskProc()){
                if (task.getEditRight() == MyConstants.NONE_RIGHT ||task.getDeleteRight() == MyConstants.NONE_RIGHT ||task.getCopyRight() == MyConstants.NONE_RIGHT ||task.getMoveRight() == MyConstants.NONE_RIGHT ||task.getParentRight() ==MyConstants.NONE_RIGHT )
                    img = "Icone-AdT_tache_lock.png";
                else{
                 img = "Icone-AdT_tache.png";
                }
            }else{
                if (task.getDbKeyChild() == -1)
                    img = "Icone-AdT_etape_warn.png";
                else if (task.getEditRight() == MyConstants.NONE_RIGHT ||task.getDeleteRight() == MyConstants.NONE_RIGHT ||task.getCopyRight() == MyConstants.NONE_RIGHT ||task.getMoveRight() == MyConstants.NONE_RIGHT ||task.getParentRight() ==MyConstants.NONE_RIGHT )
                    img = "Icone-AdT_etape_lock.png";
                else
                    img = "Icone-AdT_etape.png";
            }
        }else if (task.isAction()){
            if (task.getEditRight() == MyConstants.NONE_RIGHT)
                img = "Icone-AdT_action_lock.png";
            else
                img = "Icone-AdT_action.png";
        }
        return img;
    }


    /* print the children of a task */
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


    /* returns the list of the children of a task  */
    private ArrayList<CopexTask> getTaskListChild(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> childs = new ArrayList();
        // first child
        long dbKeyFirstChild = task.getDbKeyChild();
        if (dbKeyFirstChild != -1){
            CopexTask t = getTask(proc.getListTask(), dbKeyFirstChild);
            if (t != null){
                 childs.add(t);
                // gets brothers of t
                ArrayList<CopexTask> brothers = getTaskBrother(proc, t);
                int nbB = brothers.size();
                for (int j=0; j<nbB; j++){
                    childs.add(brothers.get(j));
                }
            }
        }
        return childs;
    }

    /* returns the brothers of a task */
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

    /* returns the task with the specified id, null otherwise */
    private CopexTask getTask(List<CopexTask> listTask, long dbKey){
        int nbT = listTask.size();
        for(int i=0; i<nbT; i++){
            if (listTask.get(i).getDbKey() == dbKey)
                return listTask.get(i);
        }
        return null;
    }

   
    public CopexReturn displayPDF(File file){
        Process p = null;
        String command = "";
        String fileNamePDF = file.getPath() ;
        //// System.out.println("displayPDF : "+fileNamePDF);
        //open with  Acrobat Reader
       /* try {
            command = "AcroRd32 "+fileNamePDF;
            p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start",command});

        }catch (IOException e) {
            try{
                p = Runtime.getRuntime().exec("start "+command);
            }catch (IOException e2) {
                return new CopexReturn ("AcroRd32 n'est pas accessible  : "+command+
								"!\nVeuillez verifier que le logiciel a ete installe sur la machine !",false) ;
			
            }
        }
*/
        // TODO mac
        try{
            p = Runtime.getRuntime() .exec("rundll32 url.dll,FileProtocolHandler "+fileNamePDF);
        }catch(IOException e){
            return new CopexReturn ("AcroRd32 n'est pas accessible  : "+command+
								"!\nVeuillez verifier que le logiciel a ete installe sur la machine !",false) ;
        }
        // we can wait ... but we are not obliged
        try {
            p.waitFor() ;
        }
        catch (InterruptedException e) {
            return new CopexReturn("Affichage interrompu " + e, false);
        }
        return new CopexReturn();
    }
    
}
