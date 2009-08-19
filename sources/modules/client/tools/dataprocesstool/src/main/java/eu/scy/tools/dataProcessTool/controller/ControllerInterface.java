/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.controller;

import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.tools.dataProcessTool.dnd.SubData;
import java.util.ArrayList;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.awt.Color;
import java.io.File;
import java.util.Vector;
import org.jdom.Element;

/**
 * Interface Controller
 * @author Marjolaine Bodin
 */
public interface ControllerInterface {
    /* chargement des données  */
    public CopexReturn load();
    /* chargement d'un ELO */
    public CopexReturn loadELO(String xmlContent);
    /*sauvegarde de l'elo*/
    public Element getPDS(Dataset ds);
    
    /* merge d'un ELO avec l'elo courant */
    public CopexReturn mergeELO(Element elo);
    /*change le statut valeur ignorée - retourne en v[0] le nouveau dataset  */
    public CopexReturn setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData, ArrayList v);

    /* creation d'une nouvelle operation - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    public CopexReturn createOperation(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo, ArrayList v);
    /* creation d'une nouvelle operation parametree - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    public CopexReturn createOperationParam(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo,String[] tabValue,  ArrayList v);

    /* mise à jour d'une valeur : titre header */
    public CopexReturn updateDataHeader(Dataset ds, int colIndex, String title, String unit, ArrayList v);

    /* mise à jour d'une valeur : titre operation */
    public CopexReturn updateDataOperation(Dataset ds, DataOperation operation, String title, ArrayList v);

    /* mise à jour d'une valeur : donnee dataset */
    public CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, Double value, ArrayList v);

    /* fermeture de la visualization d'une ds */
    public CopexReturn closeVisualization(Dataset ds, Visualization vis);

    /* suppression de la visualization d'une la feuille de données */
    public CopexReturn deleteVisualization(Dataset ds, Visualization vis);

    /* creation d'une visualization - renvoit en v[0] le nouveua dataset et en v[1] l'objet visualization */
    public CopexReturn createVisualization(Dataset ds, Visualization vis, boolean findAxisParam, ArrayList v) ;

    /* update nom graphe */
    public CopexReturn updateVisualizationName(Dataset ds, Visualization vis, String newName);

   

    /* suppression de données et ou d'operations dans un dataset */
    public CopexReturn deleteData(boolean confirm, Dataset ds, ArrayList<Data> listData, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol);

    /* ajout ou modification d'une fonction modeme */
    public CopexReturn setFunctionModel(Dataset ds, Visualization vis, String description, Color fColor, ArrayList v);

    /* insertion de lignes ou de colonnes */
    public CopexReturn  insertData(Dataset ds,  boolean isOnCol, int nb, int idBefore, ArrayList v) ;

    /* impression */
    public CopexReturn printDataset(DataTableModel model, ArrayList<Object> listGraph);

    /* drag and drop de colonnes */
    public CopexReturn moveSubData(SubData subDataToMove, int noColInsertBefore, ArrayList v);

    /* mise à jour dataset apres sort */
    public CopexReturn updateDatasetRow(Dataset ds, Vector exchange, ArrayList v);

    /* creation d'un dataset avec l'en tete - 1 ligne de données */
    public CopexReturn createDataset(String name, String[] headers, String[] units, ArrayList v);

    /* ajout d'une ligne de données */
    public CopexReturn addData(long dbKeyDs, Double[] values, boolean autoScale, ArrayList v);
    /*mise à jour des param */
    public CopexReturn setParamGraph(long dbKeyDs, long dbKeyVis, ParamGraph pg, ArrayList v);
    /* maj autoscale*/
    public CopexReturn setAutoScale(long dbKeyDs, long dbKeyVis, boolean autoScale, ArrayList v);
    /* copie-colle */
    public CopexReturn paste(long dbKeyDs, Dataset subData, int[] selCell, ArrayList v);
    /* lecture de fichier cvs  */
    public CopexReturn importCSVFile(File file, ArrayList v);
    /*suppression du dataset */
    public CopexReturn deleteDataset(Dataset ds);
    /*creation dataset par default */
    public CopexReturn createDefaultDataset(ArrayList v);
}
