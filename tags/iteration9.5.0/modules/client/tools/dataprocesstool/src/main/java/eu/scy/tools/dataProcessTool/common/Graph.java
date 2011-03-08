/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import java.awt.Color;
import java.util.ArrayList;

/**
 * visualisation sous forme de graphe
 * @author Marjolaine Bodin
 */
public class Graph extends Visualization implements Cloneable {
    // PROPERTY
    /* parametre du graphe */
    private ParamGraph paramGraph ;
    /* liste des functions model */
    private ArrayList<FunctionModel> listFunctionModel;

    // CONSTRUCTOR
    public Graph(long dbKey, String name, TypeVisualization type, int[] tabNo, boolean isOnCol, ParamGraph paramGraph, ArrayList<FunctionModel> listFunctionModel) {
        super(dbKey, name, type, tabNo, isOnCol);
        this.paramGraph = paramGraph;
        this.listFunctionModel = listFunctionModel ;
    }

   // GETTER AND SETTER
    public ParamGraph getParamGraph() {
        return paramGraph;
    }

    public void setParamGraph(ParamGraph paramGraph) {
        this.paramGraph = paramGraph;
    }

    public ArrayList<FunctionModel> getListFunctionModel() {
        return listFunctionModel;
    }

    public void setListFunctionModel(ArrayList<FunctionModel> listFunctionModel) {
        this.listFunctionModel = listFunctionModel;
    }


    @Override
    public Object clone()  {
        Graph graph = (Graph) super.clone() ;
        if (this.paramGraph != null){
            ParamGraph p = (ParamGraph)this.paramGraph.clone();
            graph.setParamGraph(p);
        }
        if (this.listFunctionModel != null){
            ArrayList<FunctionModel> listFunctionModelC = new ArrayList();
            for (int i=0; i<this.listFunctionModel.size(); i++){
                listFunctionModelC.add((FunctionModel)this.listFunctionModel.get(i).clone());
            }
        }else
            graph.setListFunctionModel(null);
        return graph;
    }
    

    //METHOD
    /* retourne la fonction model de cette couleur, null sinon */
    public FunctionModel getFunctionModel(Color fColor){
       int id = getIdFunctionModel(fColor);
       if (id == -1)
           return null;
       else
           return listFunctionModel.get(id);
    }

    /* retourne l'indice la focntion model de cette couleur, -1 sinon */
    public int getIdFunctionModel(Color fColor){
        if (listFunctionModel != null){
            int nb = listFunctionModel.size();
            for (int i=0; i<nb; i++){
                if (listFunctionModel.get(i).getColor().equals(fColor))
                    return i;
            }
            return -1;
        }else
            return -1;
    }

    /* ajoute une fonction */
    public void addFunctionModel(FunctionModel fm){
        if(this.listFunctionModel == null)
            this.listFunctionModel = new ArrayList();
        this.listFunctionModel.add(fm);
    }

    /* supprime une fonction */
    public void deleteFunctionModel(Color c){
        int id = getIdFunctionModel(c);
        if (id != -1)
            listFunctionModel.remove(id);
    }
}