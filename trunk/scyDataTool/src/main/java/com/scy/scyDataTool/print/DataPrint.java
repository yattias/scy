/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.print;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.scy.scyDataTool.common.Data;
import com.scy.scyDataTool.common.DataHeader;
import com.scy.scyDataTool.common.DataOperation;
import com.scy.scyDataTool.common.Dataset;
import com.scy.scyDataTool.common.Graph;
import com.scy.scyDataTool.common.Visualization;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * main class to print PDF
 * @author Marjolaine
 */
public class DataPrint {
    /* list dataset to print */
    private ArrayList<Dataset> listDatasetToPrint ;
    /* impression de toutes les vis */
    private boolean printAllVis;

    private Document document ;

    public DataPrint(ArrayList listDatasetToPrint, boolean printAllVis) {
        this.listDatasetToPrint = listDatasetToPrint;
        this.printAllVis = printAllVis;
    }


    /* print */
    public void dataPrint() {
        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:\\dataPrint.pdf"));
            document.open();
            int nb = listDatasetToPrint.size();
            for (int i=0; i<nb; i++){
                printDataset(listDatasetToPrint.get(i));
            }
            
            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }catch(FileNotFoundException e){

        }
    }

    /* impression d'un dataset */
    private void printDataset(Dataset ds){
        try {
            document.add(new Paragraph(ds.getName()));
            ArrayList<DataOperation> listOpOnRows = ds.getListOperationOnRows();
            int nbOpRows = listOpOnRows.size();
            ArrayList<DataOperation> listOpOnCols = ds.getListOperationOnCols();
            int nbOpOnCol = ds.getListOperationOnCols().size() ;
            int nbColDs = ds.getNbCol();
            int nbRowDs = ds.getNbRows() ;
            int nbCol = nbColDs+nbOpRows+1 ;
            int nbRow = nbRowDs+nbOpOnCol+1 ;
            String[][] tabValue = new String[nbRow][nbCol];
            PdfPTable table = new PdfPTable(nbCol);
            /// header
            DataHeader[] tabHeader = ds.getListDataHeader();
            tabValue[0][0] = "";
            for (int i=0; i<tabHeader.length; i++){
                tabValue[0][i+1] = tabHeader[i] == null ? "" : tabHeader[i].getValue();
            }
            // header nom des operations
            for (int i=0; i<nbOpRows; i++){
                tabValue[0][i+nbColDs] =listOpOnRows.get(i).getName();
            }
            ///data
            Data[][] data = ds.getData() ;
            for (int i=0; i<nbRowDs; i++){
                int id = i+1;
                tabValue[id][0] = ""+id;
                for (int j=0; j<nbColDs; j++){
                    tabValue[id][j+1] = data[i][j] == null ? "" : ""+data[i][j].getValue() ;
                }
            }
            // tabValues = >table
            for (int i=0; i<nbRow; i++){
                for (int j=0; j<nbCol; j++){
                    table.addCell(tabValue[i][j]);
                }
            }
            document.add(table);
            // Visualization
            // Save to file
            int nbVis = ds.getListVisualization().size();
            for (int i=0; i<nbVis; i++){
                printVisualization(ds.getListVisualization().get(i));
            }
            
        } catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* impression visualization */
    private void printVisualization(Visualization vis){
        if (vis instanceof Graph){
            printGraph((Graph)vis);
            return;
        }
        String filePath = "";
       //  ChartUtilities.saveChartAsPNG(new java.io.File(filePath), chart, width, height);
            Image jpg = null;
            try {
                jpg = Image.getInstance("otsoe.jpg");
            } catch (BadElementException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            if (jpg != null)
                document.add(jpg);
        } catch (DocumentException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* impression d'un graphe */
    private void printGraph(Graph graph){

    }
}
