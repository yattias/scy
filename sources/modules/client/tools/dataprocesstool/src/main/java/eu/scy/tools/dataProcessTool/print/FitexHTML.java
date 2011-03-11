/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.print;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.FunctionModel;
import eu.scy.tools.dataProcessTool.common.FunctionParam;
import eu.scy.tools.dataProcessTool.common.Graph;
import eu.scy.tools.dataProcessTool.common.SimpleVisualization;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.fitex.GUI.DrawPanel;
import eu.scy.tools.dataProcessTool.controller.FitexNumber;
import eu.scy.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.fitex.GUI.FitexPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Marjolaine
 */
public class FitexHTML {
    private  DataProcessToolPanel fitex;
    private  String fitexHtml;

    private Data[][] datas;
    private int nbRowDs  ;
    private int nbColDs;
    private ArrayList<Object> listGraph;

    private long dbKeyLabdoc;
    private URL fitexURL;

    public FitexHTML(DataProcessToolPanel fitex) {
        this.fitex = fitex;
        fitexHtml = "";
    }


    private void addString(String s){
        fitexHtml += s+"\n";
    }

    public  CopexReturn exportDatasetHTML(URL fitexURL, Dataset dataset,  DataTableModel dataTableModel, ArrayList<Object> listGraph, long dbKeyLabDoc,  ArrayList v){
        fitexHtml = "";
        this.fitexURL = fitexURL;
        this.listGraph = listGraph;
        this.dbKeyLabdoc = dbKeyLabDoc;
        int nbRows = 0;
        int nbCols = 0;
        datas = dataset.getData();
        DataHeader[] tabHeader = dataset.getListDataHeader() ;
        ArrayList<DataOperation> listOperationsOnCols = new ArrayList();
        ArrayList<DataOperation> listOperationsOnRows = new ArrayList();
        ArrayList<DataOperation> listOperation = dataset.getListOperation();
        if (listOperation != null){
            int nbOp = listOperation.size();
            for (int o=0; o<nbOp; o++){
                DataOperation op = listOperation.get(o);
                if (op.isOnCol()){
                    listOperationsOnCols.add(op);
                }else
                    listOperationsOnRows.add(op);
            }
        }
        nbRows = 2 + listOperationsOnCols.size();
        nbCols = 2 + listOperationsOnRows.size();
        if (datas != null){
            nbRows += datas.length;
            if (datas.length > 0)
                nbCols += datas[0].length;
            else{
                nbCols += tabHeader.length;
                nbRows += 1;
            }
        }else{
            nbCols += tabHeader.length;
            nbRows += 1;
        }
        nbRowDs = dataset.getNbMaxRowsData() ;
        nbColDs = dataset.getNbCol() ;
        int nbOR = listOperationsOnRows.size();
        int nbOC = listOperationsOnCols.size();


        addString("<table border='1' cellpadding='0'>");
            addString("<tr>");
                addString("<td>&nbsp;</td>");
                // => header
                for (int t=0; t<tabHeader.length; t++){
                    String[] h = new String[2];
                    h[0] = tabHeader[t] == null ? "" : tabHeader[t].getValue() ;
                    h[1] = tabHeader[t] == null ? "" : tabHeader[t].getUnit();
                    addString("<td>");
                    String c = "header";
                    if(tabHeader[t] != null && !tabHeader[t].isDouble()){
                        c = "headerText";
                    }
                    addString("<span class='"+c+"'>"+h[0]+((h[1] == null || h[1].equals("") || h[1].equals("null")) ? "": " ("+h[1]+")")+"</span>");
                    addString("</td>");
                }
                // => operations sur les lignes (donc en colonne)
                for (int j=0; j<nbOR; j++){
                    addString("<td>");
                    addString("<span class='"+listOperationsOnRows.get(j).getTypeOperation().getCodeName()+"'>"+listOperationsOnRows.get(j).getName()+"</span>");
                    addString("</td>");
                }
            addString("</tr>");


        for (int i=0; i<nbRowDs; i++){
            addString("<tr>");
                //numerotation ligne
                addString("<td>");
                addString("<span class='idrow'>"+(i+1)+"</span>");
                addString("</td>");
                //data
                for (int j=0; j<nbColDs; j++){
                    String s = "";
                    if(datas[i][j] != null && datas[i][j].isDoubleValue()){
                        s = FitexNumber.getFormat(datas[i][j].getValue(), dataset.isScientificNotation(j), dataset.getNbShownDecimals(j), dataset.getNbSignificantDigits(j), fitex.getLocale());
                        //s = fitex.getNumberFormat().format(datas[i][j].getDoubleValue());
                        if(Double.isNaN(datas[i][j].getDoubleValue()))
                            s = "";
                    }else if (datas[i][j] != null){
                        s = datas[i][j].getValue();
                    }
                    addString("<td>");
                    addString("<span class='dataset'>"+s+"</span>");
                    addString("</td>");
                }
                //ope result
                for (int j=0; j<nbOR; j++){
                    ArrayList<Integer> listNo = listOperationsOnRows.get(j).getListNo();
                    addString("<td>");
                    int id = listNo.indexOf(i);
                    String o = "";
                    if (id != -1 && !isIgnoredRow(i)){
                        //o= fitex.getNumberFormat().format(dataset.getListOperationResult(listOperationsOnRows.get(j)).get(id));
                        o= dataTableModel.getOperationValue(listOperationsOnRows.get(j), i, dataTableModel.getStringListValueRow(i), ""+dataset.getListOperationResult(listOperationsOnRows.get(j)).get(id));
                    }else{
                        o = "-";
                    }
                    addString("<span class='"+listOperationsOnRows.get(j).getTypeOperation().getCodeName()+"'>"+o+"</span>");
                    addString("</td>");
                }
            addString("</tr>");
        }
        // => operations sur les colonnes (donc en ligne)
        for(int i=0; i<nbOC; i++){
            addString("<tr>");
                addString("<td>");
                addString("<span class='"+listOperationsOnCols.get(i).getTypeOperation().getCodeName()+"'>"+listOperationsOnCols.get(i).getName()+"</span>");
                addString("</td>");
                // ope result
                for(int j=0; j<nbColDs; j++){
                    ArrayList<Integer> listNo = listOperationsOnCols.get(i).getListNo();
                    addString("<td>");
                    int id = listNo.indexOf(j);
                    String o = "";
                    if (id != -1 && !isIgnoredRow(j)){
                        //o= fitex.getNumberFormat().format(dataset.getListOperationResult(listOperationsOnCols.get(i)).get(id));
                        o= dataTableModel.getOperationValue(listOperationsOnCols.get(i), j, dataTableModel.getStringListValueCol(j), ""+dataset.getListOperationResult(listOperationsOnCols.get(i)).get(id));
                    }else{
                        o = "-";
                    }
                    addString("<span class='"+listOperationsOnCols.get(i).getTypeOperation().getCodeName()+"'>"+o+"</span>");
                    addString("</td>");
                }
                // spaces at the end
//                for(int j=nbColDs; j<nbCols; j++){
//                     addString("<td>&nbsp;</td>");
//                }
            addString("</tr>");
        }

        addString("</table>");
        int nbVis = dataset.getListVisualization().size();
        for(int i=0; i<nbVis; i++){
            if(i< listGraph.size())
                setVisualization(dataset.getListVisualization().get(i), listGraph.get(i), i);
        }
        v.add(fitexHtml);
        return new CopexReturn();
    }

    /* retourne vrai si tous les elements d'une ligne sont a ignorer */
    private boolean isIgnoredRow(int id){
        boolean allIgnored = true;
        for (int i=0; i<nbColDs; i++){
            if (datas[id][i] != null &&  !datas[id][i].isIgnoredData()){
                allIgnored = false ;
                break;
            }
        }
        return allIgnored ;
    }

    private void setVisualization(Visualization vis, Object o, int noVis){
        addString("<p class=\"visualization_name\">"+vis.getName()+"</p>");
        if(vis instanceof SimpleVisualization){
            setChart((SimpleVisualization)vis, (JFreeChart)o, noVis);
        }else if(vis instanceof Graph){
            setFitexGraph((Graph)vis, (DrawPanel)o, noVis);
        }
    }

    private void setChart(SimpleVisualization vis, JFreeChart chart, int noVis){
        File temp = null;
        try {
            temp = File.createTempFile("dataVis", ".png", new File(System.getProperty("java.io.tmpdir")));
             ChartUtilities.saveChartAsPNG(temp, chart, 400, 300);
             Image jpg = null;
            try {
                jpg = Image.getInstance(temp.toURI().toURL());
            } catch (BadElementException ex) {
                Logger.getLogger(FitexHTML.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FitexHTML.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(DataPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
        String fileName = "labdoc-draw-"+dbKeyLabdoc+"-"+noVis+".png";
        URL imgURL;
        try {
            imgURL = new URL(fitexURL, "../tools_utilities/InterfaceServer/writeObject.php");
            HttpURLConnection urlCon = (HttpURLConnection)imgURL.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.connect();
            ObjectOutputStream out = new ObjectOutputStream(urlCon.getOutputStream());
            out.writeObject(fileName);
            // lit le fichier temp pour ecrire dans fichier serveur
//            InputStreamReader fileReader = null;
//            fileReader = new InputStreamReader(new FileInputStream(temp), "utf-8");
//            BufferedReader reader = new BufferedReader(fileReader);
//
            ByteArrayOutputStream boss = new ByteArrayOutputStream();
            BufferedImage bi = ImageIO.read(temp);
            ImageIO.write(bi, "png", boss);
            out.writeObject(boss.toByteArray());
            //
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "utf8"));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
            }
            out.flush();
            out.close();
            urlCon.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(FitexHTML.class.getName()).log(Level.SEVERE, null, ex);
        }catch(IOException ioex){
            Logger.getLogger(FitexHTML.class.getName()).log(Level.SEVERE, null, ioex);
        }
        String preview = "<img src=\"../tools_utilities/InterfaceServer/labdoc/"+fileName+"\" alt=\"Graphe\" style=\"width: 100%;\">";
        addString(preview);
    }

    private void setFitexGraph(Graph graph, DrawPanel drawPanel, int noVis){
        if(graph == null )
            return;
        BufferedImage outImage=new BufferedImage(drawPanel.getWidth(),drawPanel.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics=outImage.createGraphics();
        drawPanel.paint(graphics);
        String fileName = "labdoc-draw-"+dbKeyLabdoc+"-"+noVis+".png";
        URL imgURL;
        try {
            imgURL = new URL(fitexURL, "../tools_utilities/InterfaceServer/writeObject.php");
            HttpURLConnection urlCon = (HttpURLConnection)imgURL.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.connect();
            ObjectOutputStream out = new ObjectOutputStream(urlCon.getOutputStream());
            out.writeObject(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outImage, "png", baos);
            byte[] bytesOut = baos.toByteArray();
            out.writeObject(bytesOut);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "utf8"));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
            }
            out.flush();
            out.close();
            urlCon.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(FitexHTML.class.getName()).log(Level.SEVERE, null, ex);
        }catch(IOException ioex){
            Logger.getLogger(FitexHTML.class.getName()).log(Level.SEVERE, null, ioex);
        }catch(Throwable t){
            Logger.getLogger(FitexHTML.class.getName()).log(Level.SEVERE, null, t);
        }
        int nbF = 0;
        if(graph.getListFunctionModel() != null){
            nbF = graph.getListFunctionModel().size();
        }
        if(nbF >0){
            addString("<table  border='0' cellpadding='0'  style=\"width: 100%;\">");
            addString("<tr><td>&nbsp;</td>");
            for(Iterator<FunctionModel> fm = graph.getListFunctionModel().iterator(); fm.hasNext();){
                FunctionModel function = fm.next();
                String color = getHtmlColor(function.getColor());
                addString("<td class=\"visualization_function\" style=\"color: "+color+"\">"+((function.getType() == DataConstants.FUNCTION_TYPE_Y_FCT_X)? "f(x)" : "f(y)")+" = "+function.getDescription()+"</td>");
            }
            addString("</tr>");
            addString("<tr><td>&nbsp;</td>");
            for(Iterator<FunctionModel> fm = graph.getListFunctionModel().iterator(); fm.hasNext();){
                FunctionModel function = fm.next();
                String color = getHtmlColor(function.getColor());
                int nbparam = function.getListParam().size();
                addString("<td>");
                if(nbparam > 0){
                    addString("<table  border='0' cellpadding='0'><tr>");
                    for(Iterator<FunctionParam> p = function.getListParam().iterator(); p.hasNext();){
                        FunctionParam param = p.next();
                        addString("<td class=\"visualization_function_param\" style=\"color: "+color+"\">"+param.getParam()+" = "+FitexNumber.getNumberFormat(fitex.getLocale()).format(param.getValue())+"</td>");
                    }
                    addString("</tr></table>");
                }
                addString("</td>");
            }
            addString("</tr>");
            addString("</table>");
        }
        String preview = "<img src=\"../tools_utilities/InterfaceServer/labdoc/"+fileName+"\" alt=\"Graphe\" style=\"width: 100%;\">";
        addString(preview);
    }

    private String getHtmlColor(Color color){
        String s = Integer.toHexString( color.getRGB() & 0x00ffffff );
        while(s.length() < 6){
            s ="0"+s;
        }
        s = "#"+s;
        return s;
    }
}
