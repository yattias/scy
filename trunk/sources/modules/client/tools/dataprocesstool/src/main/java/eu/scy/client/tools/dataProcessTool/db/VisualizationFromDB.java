/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.db;

import eu.scy.client.tools.dataProcessTool.common.FunctionParam;
import eu.scy.client.tools.dataProcessTool.common.Graph;
import eu.scy.client.tools.dataProcessTool.common.ParamGraph;
import eu.scy.client.tools.dataProcessTool.common.PlotXY;
import eu.scy.client.tools.dataProcessTool.common.SimpleVisualization;
import eu.scy.client.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.client.tools.dataProcessTool.common.Visualization;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * visualizations in database
 * @author Marjolaine
 */
public class VisualizationFromDB {
    /* load visualization types in v[0] */
    public static CopexReturn getAllTypeVisualizationFromDB(DataBaseCommunication dbC, Locale locale, ArrayList v){
        ArrayList<TypeVisualization> listVis = new ArrayList();
        TypeVisualization[] tabTypeVis = null;
        int nbVis = 0;
        String lib = "LIBELLE_"+locale.getLanguage();
        String query = "SELECT ID_TYPE_VISUALIZATION, CODE, "+lib+", NB_COL_PARAM FROM TYPE_VISUALIZATION ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TYPE_VISUALIZATION");
        listFields.add("CODE");
        listFields.add(lib);
        listFields.add("NB_COL_PARAM");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TYPE_VISUALIZATION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("CODE");
            if (s == null)
                continue;
            int code = -1;
            try{
                code = Integer.parseInt(s);
            }catch(NumberFormatException e){
                
            }
            String name = rs.getColumnData(lib);
            int nbColParam = 1;
            s = rs.getColumnData("NB_COL_PARAM");
            if (s == null)
                continue;
            try{
                nbColParam = Integer.parseInt(s);
            } catch(NumberFormatException e){

            }
            
            TypeVisualization vis = new TypeVisualization(dbKey, code,name, nbColParam);
            listVis.add(vis);
            nbVis++;
        }
        // array
        tabTypeVis = new TypeVisualization[nbVis];
        for (int i=0; i<nbVis; i++){
            tabTypeVis[i] = listVis.get(i);
        }
        v.add(tabTypeVis);
        return new CopexReturn();
    }

     /* update visualization title  */
    public static CopexReturn updateVisualizationTitleInDB(DataBaseCommunication dbC, long dbKeyVis, String title){
        title = MyUtilities.replace("\'",title,"''") ;
        String query = "UPDATE DATA_VISUALIZATION SET VIS_NAME = '"+title+"' WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String[] querys = new String[1];
        querys[0] = query;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }
    
    /* create new visualization - v[0] new id*/
    public static CopexReturn createVisualizationInDB(DataBaseCommunication dbC, long dbKeyDs, Visualization visualization, Color[] plotsColor,ArrayList v){
        String name = visualization.getName() ;
        name  = MyUtilities.replace("\'",name,"''") ;
        String query = "INSERT INTO DATA_VISUALIZATION (ID_DATA_VISUALIZATION, VIS_NAME) VALUES (NULL, '"+name+"') ;";
        String queryID = "SELECT max(last_insert_id(`ID_DATA_VISUALIZATION`))   FROM DATA_VISUALIZATION ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);

        //links
        String query1 = "INSERT INTO LINK_DATASET_VISUALIZATION (ID_DATASET, ID_DATA_VISUALIZATION) VALUES ("+dbKeyDs+", "+dbKey+") ;";
        String query2 = "INSERT INTO LINK_VISUALIZATION_TYPE (ID_DATA_VISUALIZATION, ID_TYPE_VISUALIZATION) VALUES ("+dbKey+", "+visualization.getType().getDbKey()+") ;";
        int nb = 0 ;
        if(visualization instanceof SimpleVisualization){
            nb = 1 ;
        }
        int nbQ = nb+2 ;
        String[] querys = new String[nbQ];
        int i=0;
        querys[i++] = query1;
        querys[i++] = query2;
        if(visualization instanceof SimpleVisualization){
            for (int k=0; k<nb; k++){
                long dbKeyHeader = ((SimpleVisualization)visualization).getHeader().getDbKey();
                String headerLabel = "NULL";
                if(((SimpleVisualization)visualization).getHeaderLabel() != null){
                    headerLabel = Long.toString(((SimpleVisualization)visualization).getHeaderLabel().getDbKey());
                }
                String query3 = "INSERT INTO PIE_BAR_VISUALIZATION (ID_DATA_VISUALIZATION, ID_HEADER, ID_HEADER_LABEL) VALUES ("+dbKey+", "+dbKeyHeader+", "+headerLabel+"); ";
                querys[i++] = query3;
            }
        }else if(visualization instanceof Graph){
            // graph
            ArrayList v3 = new ArrayList();
            cr = createGraphInDB(dbC, dbKey, (Graph)visualization, plotsColor, v3);
            if(cr.isError())
                return cr;
            visualization = (Graph)v3.get(0);
        }
        
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        v.add(visualization);
        return cr;
    }

    /* creation graph */
    private static CopexReturn createGraphInDB(DataBaseCommunication dbC, long dbKeyVis,Graph  graph , Color[] plotsColor, ArrayList v){
        ParamGraph pg = graph.getParamGraph();
        int deltaFixed = pg.isDeltaFixedAutoscale() ? 1 : 0;
        String query = "INSERT INTO PARAM_VIS_GRAPH (ID_DATA_VISUALIZATION, X_MIN, X_MAX, Y_MIN, Y_MAX, DELTA_X, DELTA_Y, DELTA_FIXED_AUTOSCALE)" +
                "VALUES ("+dbKeyVis+", "+pg.getX_min()+", "+pg.getX_max()+", "+pg.getY_min()+", "+pg.getY_max()+", "+pg.getDeltaX()+", "+pg.getDeltaY()+", "+deltaFixed+"); ";
        int nbPlot = pg.getPlots().size();
        String[] querys = new String[1+nbPlot];
        int i = 0;
        querys[i++] = query;
        for(Iterator<PlotXY> p = pg.getPlots().iterator();p.hasNext();){
            PlotXY plot = p.next();
            int idPlotColor = getIdPlotColor(plot.getPlotColor(), plotsColor);
            String queryPlot = "INSERT INTO PLOT_XY_GRAPH (ID_PLOT, ID_HEADER_X, ID_HEADER_Y, ID_PLOT_COLOR) " +
                    "VALUES (NULL,"+plot.getHeaderX().getDbKey()+", "+plot.getHeaderY().getDbKey()+", "+idPlotColor+") ;";
            String queryID = "SELECT max(last_insert_id(`ID_PLOT`))   FROM PLOT_XY_GRAPH ;";
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(queryPlot, queryID, v2);
            if (cr.isError())
                return cr;
            long dbKeyPlot = (Long)v2.get(0);
            String queryPlotLink = "INSERT INTO LINK_GRAPH_PLOT (ID_DATA_VISUALIZATION, ID_PLOT) VALUES ("+dbKeyVis+", "+dbKeyPlot+");";
            querys[i++] = queryPlotLink;
            plot.setDbKey(dbKeyPlot);
        }
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        if(cr.isError())
            return cr;
        v.add(graph);
        return new CopexReturn();
    }

    private static int getIdPlotColor(Color pColor, Color[] plotsColor){
        for(int i=0; i<plotsColor.length; i++){
            if(plotsColor[i].equals(pColor))
                return i;
        }
        return -1;
    }

    /* remove a visualization  */
    public static CopexReturn deleteVisualizationFromDB(DataBaseCommunication dbC, long dbKeyVis){
        ArrayList v = new ArrayList();
        String[] querys = new String[9];
        CopexReturn cr = deletePlotsXYInDB(dbC, dbKeyVis);
        if(cr.isError())
            return cr;
        // remove visualizations
        String queryDelVisType = "DELETE FROM LINK_VISUALIZATION_TYPE WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String queryDelVisNo = "DELETE FROM PIE_BAR_VISUALIZATION WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String queryDelParamGraph = "DELETE FROM PARAM_VIS_GRAPH WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String queryDelFunctionParam = "DELETE FROM FUNCTION_PARAM WHERE ID_FUNCTION_PARAM IN (SELECT ID_FUNCTION_PARAM FROM LINK_FUNCTION_PARAM WHERE ID_FUNCTION_MODEL IN (SELECT ID_FUNCTION_MODEL FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+"));";
        String queryDelFunctionParamLink = "DELETE FROM LINK_FUNCTION_PARAM WHERE ID_FUNCTION_MODEL IN (SELECT ID_FUNCTION_MODEL FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+");";
        String queryDelFunction = "DELETE FROM FUNCTION_MODEL WHERE ID_FUNCTION_MODEL IN (SELECT ID_FUNCTION_MODEL FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+");";
        String queryDelFunctionLink  = "DELETE FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+";";
        String queryDelVis = "DELETE FROM DATA_VISUALIZATION WHERE ID_DATA_VISUALIZATION  = "+dbKeyVis+" ;";
        String queryDelLinkVis = "DELETE FROM LINK_DATASET_VISUALIZATION WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";

        //querys
        int i=0;
        querys[i++] = queryDelVisType;
        querys[i++] = queryDelVisNo ;
        querys[i++] = queryDelParamGraph ;
        querys[i++] = queryDelFunctionParam;
        querys[i++] = queryDelFunctionParamLink;
        querys[i++] = queryDelFunction;
        querys[i++] = queryDelFunctionLink;
        querys[i++] = queryDelVis ;
        querys[i++] = queryDelLinkVis ;

        cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* remove  plots */
    private static CopexReturn deletePlotsXYInDB(DataBaseCommunication dbC, long dbKeyVis){
        ArrayList v = new ArrayList();
        String[] querys = new String[2];
        String query = "DELETE FROM PLOT_XY_GRAPH WHERE ID_PLOT IN (SELECT ID_PLOT FROM LINK_GRAPH_PLOT WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+") ; ";
        String queryDel = "DELETE FROM LINK_GRAPH_PLOT WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        int i=0;
        querys[i++] = query;
        querys[i++] = queryDel ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }


    /* add a function model , v[0] new id  */
    public static CopexReturn createFunctionModelInDB(DataBaseCommunication dbC, long dbKeyGraph, String description, char type, int idFunctionColor, ArrayList<FunctionParam> listParam,String idPredefFunction,ArrayList v){
        String desc  = MyUtilities.replace("\'",description,"''") ;
        String pf = "NULL";
        if(idPredefFunction != null ){
            pf = "'"+idPredefFunction+"'";
        }
        String query = "INSERT INTO FUNCTION_MODEL (ID_FUNCTION_MODEL, DESCRIPTION, F_TYPE, ID_FUNCTION_COLOR, ID_PREDEF_FUNCTION) VALUES (NULL, '"+desc+"','"+type+"', "+idFunctionColor+", "+pf+" );";
        String queryID = "SELECT max(last_insert_id(`ID_FUNCTION_MODEL`))   FROM FUNCTION_MODEL ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);

        // link
        String queryLink = "INSERT INTO LINK_GRAPH_FUNCTION_MODEL (ID_DATA_VISUALIZATION, ID_FUNCTION_MODEL) VALUES ("+dbKeyGraph+", "+dbKey+") ;";
        String[] querys = new String[1];
        querys[0] = queryLink;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        // creation params
        int nbParam = listParam.size();
        for(int i=0; i<nbParam; i++){
            v2 = new ArrayList();
            cr = createFunctionParamInDB(dbC, dbKey, listParam.get(i), v2);
            if(cr.isError())
                return cr;
            listParam.set(i, (FunctionParam)v2.get(0));
        }
        v.add(dbKey);
        v.add(listParam);
        return new CopexReturn();
    }

    /*creation of a param for a function*/
    public static CopexReturn createFunctionParamInDB(DataBaseCommunication dbC, long dbKeyFunction, FunctionParam param, ArrayList v){
        String name  = MyUtilities.replace("\'",param.getParam(),"''") ;
        String query = "INSERT INTO FUNCTION_PARAM (ID_FUNCTION_PARAM, PARAM_NAME, PARAM_VALUE) VALUES (NULL, '"+name+"',"+param.getValue()+"  ) ;";
        String queryID = "SELECT max(last_insert_id(`ID_FUNCTION_PARAM`))   FROM FUNCTION_PARAM ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // lien
        String queryLink = "INSERT INTO LINK_FUNCTION_PARAM (ID_FUNCTION_MODEL, ID_FUNCTION_PARAM) VALUES ("+dbKeyFunction+", "+dbKey+") ;";
        String[] querys = new String[1];
        querys[0] = queryLink;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        param.setDbKey(dbKey);
        v.add(param);
        return new CopexReturn();

    }
    /* delete a function model */
    public static CopexReturn deleteFunctionModelFromDB(DataBaseCommunication dbC, long dbKey){
        CopexReturn cr = deleteFunctionParamFromDB(dbC, dbKey);
        if(cr.isError())
            return cr;
        String queryLink = "DELETE FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_FUNCTION_MODEL = "+dbKey;
        String queryDel = "DELETE FROM FUNCTION_MODEL WHERE ID_FUNCTION_MODEL = "+dbKey;
        String[] querys = new String[2];
        querys[0] = queryLink ;
        querys[1] = queryDel  ;
        ArrayList v = new ArrayList();
        cr = dbC.executeQuery(querys, v);
        return cr;
    }

     /*delete parameters for a function model*/
    public static CopexReturn deleteFunctionParamFromDB(DataBaseCommunication dbC, long dbKeyFunction){
        String queryParam = "DELETE FROM FUNCTION_PARAM WHERE ID_FUNCTION_PARAM IN (SELECT ID_FUNCTION_PARAM FROM LINK_FUNCTION_PARAM WHERE ID_FUNCTION_MODEL = "+dbKeyFunction+") ;";
        String queryLinkParam = "DELETE FROM LINK_FUNCTION_PARAM WHERE ID_FUNCTION_MODEL = "+dbKeyFunction+" ;";
        String[] querys = new String[2];
        querys[0] = queryParam;
        querys[1] = queryLinkParam;
        ArrayList v = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

     /* update a function model */
    public static CopexReturn updateFunctionModelInDB(DataBaseCommunication dbC, long dbKey, String description, char type, ArrayList<FunctionParam> listParam, String idPredefFunction, ArrayList v){
        // remove and create param
        CopexReturn cr = deleteFunctionParamFromDB(dbC, dbKey);
        if(cr.isError())
            return cr;
        int nbP = listParam.size();
        for(int k=0; k<nbP; k++){
            ArrayList v2 = new ArrayList();
            cr = createFunctionParamInDB(dbC, dbKey, listParam.get(k), v2);
            if(cr.isError())
                return cr;
            listParam.set(k, (FunctionParam)v2.get(0));
        }
        String desc  = MyUtilities.replace("\'",description,"''") ;
        String predef = "NULL";
        if(idPredefFunction != null && idPredefFunction.length()> 0 && !idPredefFunction.equals("NULL")){
            predef = "'"+idPredefFunction+"'";
        }
        String query = "UPDATE FUNCTION_MODEL SET DESCRIPTION = '"+desc+"' , F_TYPE = '"+type+"', ID_PREDEF_FUNCTION = "+predef+" WHERE ID_FUNCTION_MODEL = "+dbKey+" ;";
        String[] querys = new String[1];
        querys[0] = query ;
        ArrayList v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        if(cr.isError())
            return cr;
        v.add(listParam);
        return new CopexReturn();
    }

     /* update function model : list  param*/
    public static CopexReturn updateFunctionModelParamInDB(DataBaseCommunication dbC,ArrayList<FunctionParam> listParam){
        int nbP = listParam.size();
        String[] querys = new String[nbP];
        for(int k=0; k<nbP; k++){
            String query = "UPDATE FUNCTION_PARAM SET PARAM_VALUE = "+listParam.get(k).getValue()+"  WHERE ID_FUNCTION_PARAM = "+listParam.get(k).getDbKey()+" ;";
            querys[k] = query;
        }
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* delete some visualizations */
    public static CopexReturn deleteVisualizationFromDB(DataBaseCommunication dbC, ArrayList<Visualization> listVis){
        int nbVis = listVis.size();
        for (int i=0; i<nbVis; i++){
            CopexReturn cr = deleteVisualizationFromDB(dbC, listVis.get(i).getDbKey());
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    /* update axes */
    public static CopexReturn updateGraphParamInDB(DataBaseCommunication dbC, long dbKeyGraph, ParamGraph pg){
        int deltaFixed = pg.isDeltaFixedAutoscale() ? 1 : 0;
        String query = "UPDATE PARAM_VIS_GRAPH SET  X_MIN = "+pg.getX_min()+", X_MAX = "+pg.getX_max()+", " +
                "Y_MIN = "+pg.getY_min()+", Y_MAX = "+pg.getY_max()+", " +
                "DELTA_X = "+pg.getDeltaX()+", DELTA_Y = "+pg.getDeltaY()+", DELTA_FIXED_AUTOSCALE = " +deltaFixed + " "+
                "WHERE ID_DATA_VISUALIZATION = "+dbKeyGraph+ " ; ";
        String[] querys = new String[1];
        querys[0] = query ;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /*delete list plots */
    public static CopexReturn deletePlotsFromDB(DataBaseCommunication dbC, ArrayList<Long> listPlots){
        int nbPlots = listPlots.size();
        if (nbPlots == 0)
            return new CopexReturn();
        String listDbKey = ""+listPlots.get(0);
        for (int i=1; i<nbPlots; i++){
            long dbKey = listPlots.get(i) ;
            listDbKey += ", "+dbKey;
        }
        String queryPlot = "DELETE FROM PLOT_XY_GRAPH WHERE ID_PLOT IN ( "+listDbKey+" ); ";
        String queryLink = "DELETE FROM LINK_GRAPH_PLOT WHERE ID_PLOT IN ( "+listDbKey+" ); ";
        String[] querys = new String[2];
        querys[0] = queryPlot;
        querys[1] = queryLink;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* create plot */
    public static CopexReturn createPlotInDB(DataBaseCommunication dbC, long dbKeyGraph, PlotXY plot, Color[] plotsColor, ArrayList v){
            int idPlotColor = getIdPlotColor(plot.getPlotColor(), plotsColor);
            String queryPlot = "INSERT INTO PLOT_XY_GRAPH (ID_PLOT, ID_HEADER_X, ID_HEADER_Y, ID_PLOT_COLOR) " +
                    "VALUES (NULL,"+plot.getHeaderX().getDbKey()+", "+plot.getHeaderY().getDbKey()+", "+idPlotColor+") ;";
            String queryID = "SELECT max(last_insert_id(`ID_PLOT`))   FROM PLOT_XY_GRAPH ;";
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(queryPlot, queryID, v2);
            if (cr.isError())
                return cr;
            long dbKeyPlot = (Long)v2.get(0);
            v.add(dbKeyPlot);
            String queryPlotLink = "INSERT INTO LINK_GRAPH_PLOT (ID_DATA_VISUALIZATION, ID_PLOT) VALUES ("+dbKeyGraph+", "+dbKeyPlot+");";
            String[] querys = new String[1];
            querys[0] = queryPlotLink;
            v2 = new ArrayList();
            cr = dbC.executeQuery(querys, v2);
            return cr;
    }

    /* remove plot */
    public static CopexReturn removePlotFromDB(DataBaseCommunication dbC, long dbKeyPlot){
        String query = "DELETE FROM PLOT_XY_GRAPH WHERE ID_PLOT = "+dbKeyPlot+" ;";
        String queryDelLink = "DELETE FROM LINK_GRAPH_PLOT WHERE ID_PLOT = "+dbKeyPlot+" ;";
        String[] querys = new String[2];
        querys[0] = query;
        querys[1] = queryDelLink;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* update plot */
    public static CopexReturn updatePlotInDB(DataBaseCommunication dbC, long dbKeyPlot, PlotXY plot, Color[] plotsColor){
        int idPlotColor = getIdPlotColor(plot.getPlotColor(), plotsColor);
        String query = "UPDATE PLOT_XY_GRAPH SET ID_HEADER_X = "+plot.getHeaderX().getDbKey()+", " +
                "ID_HEADER_Y = "+plot.getHeaderX().getDbKey()+", ID_PLOT_COLOR = "+idPlotColor+" WHERE ID_PLOT = "+dbKeyPlot+" ;";
        String[] querys = new String[1];
        querys[0] = query;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }
}
