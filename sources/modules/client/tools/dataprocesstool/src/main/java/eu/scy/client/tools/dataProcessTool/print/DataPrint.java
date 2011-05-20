/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.print;

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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.common.Graph;
import eu.scy.client.tools.dataProcessTool.common.Group;
import eu.scy.client.tools.dataProcessTool.common.Mission;
import eu.scy.client.tools.dataProcessTool.common.ToolUser;
import eu.scy.client.tools.dataProcessTool.common.Visualization;
import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.fitex.GUI.DrawPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * main class to print PDF
 * @author Marjolaine
 */
public class DataPrint {
    private DataProcessToolPanel owner;
    private Group group;
    private Mission mission;
    private Dataset dataset;
    private DataTableModel tableModel;
    private String fileName;
    private Document document ;
    private PdfWriter writer;
    private boolean printDataset;
    private ArrayList<Visualization> listVis;
    private ArrayList<Object> listGraph;

    public DataPrint(DataProcessToolPanel owner,Mission mission, Group group, Dataset dataset,DataTableModel tableModel,boolean printDataset, ArrayList<Visualization> listVis,  ArrayList<Object> listGraph, String fileName) {
        this.owner = owner;
        this.mission = mission;
        this.tableModel = tableModel;
        this.group = group;
        this.dataset = dataset;
        this.fileName = fileName;
        this.printDataset = printDataset;
        this.listVis = listVis;
        this.listGraph = listGraph;
    }


    public CopexReturn displayPDF(File file){
        Process p = null;
        String command = "";
        String fileNamePDF = file.getPath() ;
        //open Acrobat Reader

        // TODO - only windows
        try{
            p = Runtime.getRuntime() .exec("rundll32 url.dll,FileProtocolHandler "+fileNamePDF);
        }catch(IOException e){
            return new CopexReturn ("AcroRd32 n'est pas accessible  : "+command+
								"!\nVeuillez verifier que le logiciel a ete installe sur la machine !",false) ;
        }
        // can wait
        try {
            p.waitFor() ;
        }
        catch (InterruptedException e) {
            return new CopexReturn("Affichage interrompu " + e, false);
        }
        return new CopexReturn();
    }

    /* print */
    public CopexReturn  printDocument() {
        try {
            document = new Document() ;

            File temp;
            temp = File.createTempFile(fileName, ".pdf", new File(System.getProperty("java.io.tmpdir")));
            //// System.out.println("fichier temporaire : "+temp.getPath());
            String printDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis()).toString();
            writer = PdfWriter.getInstance(document, new FileOutputStream(temp));
            writer.setPageEvent(new MyPDFEvent(writer, printDate ));
            document.open();
            //CopexReturn cr;
            CopexReturn cr = setHeader();
            if (cr.isError())
                return cr;
            if (dataset != null){
                cr = printDataset(dataset, tableModel);
                if (cr.isError())
                    return cr;
                int nb = listVis.size() ;
                for (int i=0; i<nb; i++){
                    if(listVis.get(i) instanceof Graph){
                        cr = printFitex((Graph)listVis.get(i), (DrawPanel)listGraph.get(i));
                    }else{
                        cr = printVisualization(listVis.get(i), (JFreeChart)listGraph.get(i));
                    }
                    if(cr.isError())
                        return cr;
                }
            }
           
            document.close();
            cr = displayPDF(temp);
            if(cr.isError())
                return cr;
            return new CopexReturn();
        } catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(owner.getBundleString("MSG_ERROR_PRINT"), false);
        }catch(IOException ex){
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(owner.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }


    private CopexReturn setHeader(){
        String name = "";
        for(Iterator<ToolUser> l = group.getLearners().iterator();l.hasNext();){
            ToolUser learner = l.next();
            name += (learner.getUserFirstName() == null ?"":learner.getUserFirstName()+" ")+learner.getUserName()+";";
        }
        if(name.length() > 0){
            name = name.substring(0, name.length()-1);
        }
        String missionName = owner.getBundleString("LABEL_MISSION")+" "+mission.getName() ;
        try {
            Table headerTable = new Table(1);
            headerTable.setBorderWidth(0);
            Cell txtCell = new Cell();
            txtCell.setBorder(0);
            Chunk c1 = new Chunk(name+"\n",getNormalFont());
            Chunk c2 = new Chunk(missionName+"\n", getNormalFont());
            String d = dataset == null ? "" : dataset.getName();
            Chunk c3 = new Chunk(d+"\n", getNormalFont());
            txtCell.add(c1);
            txtCell.add(c2);
            txtCell.add(c3);
            txtCell.setBorder(0);
            txtCell.setHorizontalAlignment(Element.ALIGN_RIGHT) ;
            headerTable.addCell(txtCell);
            document.add(headerTable);
            return new CopexReturn();
        } catch (BadElementException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(owner.getBundleString("MSG_ERROR_PRINT"), false);
        }catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(owner.getBundleString("MSG_ERROR_PRINT"), false);
        }
    }

    private Font getNormalFont(){
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont("c:\\WINDOWS\\fonts\\times.ttf", BaseFont.IDENTITY_H, true);
            Font font = new Font(baseFont);
            return font;
        } catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }


        return FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL, Color.BLACK) ;
    }
    
    /* print dataset */
    private CopexReturn printDataset(Dataset ds, DataTableModel model){
        if(!printDataset)
            return new CopexReturn();
        try {
            document.add(new Paragraph("\n"));
            int nbCol = model.getColumnCount();
            int nbRow = model.getRowCount() ;
            PdfPTable table = new PdfPTable(nbCol);
            for (int i=0; i<nbRow; i++){
                for (int j=0; j<nbCol; j++){
                    String s = "";
                    PdfPCell aCell = new PdfPCell();
                    if (model.getValueAt(i, j) != null){
                        if (model.getValueAt(i, j) instanceof String[]){
                            s = ((String[])model.getValueAt(i, j))[0]+"("+((String[])model.getValueAt(i, j))[1]+")";
                        }else
                            s = model.getValueAt(i, j).toString();
                    }
                    Chunk c = new Chunk(s, getNormalFont());
                    aCell.addElement(c);
                    table.addCell(aCell);
                }
            }
            document.add(table);
            
        } catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(owner.getBundleString("MSG_ERROR_PRINT"), false);
        }
        return new CopexReturn();
    }

    /* print visualization */
    private CopexReturn  printVisualization(Visualization vis, JFreeChart chart){
        File temp;
        try {
            temp = File.createTempFile("dataVis", ".jpg", new File(System.getProperty("java.io.tmpdir")));
             ChartUtilities.saveChartAsPNG(temp, chart, 400, 300);
             Image jpg = null;
            try {
                jpg = Image.getInstance(temp.toURI().toURL());
            } catch (BadElementException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            Chunk c = new Chunk(vis.getName()+"\n", getNormalFont());
            document.add(new Paragraph(c));
            if (jpg != null)
                document.add(jpg);
        } catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            return new CopexReturn(owner.getBundleString("MSG_ERROR_PRINT"), false);
        }
             
        } catch (IOException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new CopexReturn();
    }

    /* print fitex graph  */
    private CopexReturn printFitex(Graph fitexVis, DrawPanel fitexGraph){
        try {
            float dw = document.getPageSize().getWidth();
            BufferedImage outImage=new BufferedImage(fitexGraph.getWidth(),fitexGraph.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics=outImage.createGraphics();
            fitexGraph.paint(graphics);
            File outFiletemp = File.createTempFile("out", ".png", new File(System.getProperty("java.io.tmpdir")));
            try {
                 boolean isok = ImageIO.write(outImage,"png",outFiletemp);
                    // System.out.println("Format d'ecriture non pris en charge" );
           } catch (Exception e) {
                // System.out.println("erreur dans l'enregistrement de l'image :" );
              //  e.printStackTrace();
            }

            Image img = Image.getInstance(outFiletemp.getPath());
            if(fitexGraph.getWidth() > dw){
                float p =dw*100/fitexGraph.getWidth() ;
                img.scalePercent(p);
            }
            Chunk c = new Chunk(fitexVis.getName()+"\n", getNormalFont());
            document.add(new Paragraph(c));
            document.add(img);

        } catch (BadElementException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }catch(DocumentException ex){
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new CopexReturn();
    }
}
