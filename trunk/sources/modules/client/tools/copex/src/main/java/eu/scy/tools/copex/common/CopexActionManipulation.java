/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.CopexPanel;
import java.util.ArrayList;
import org.jdom.Element;

/**
 * action de type manipulation
 * @author Marjolaine
 */
public class CopexActionManipulation extends CopexActionParam implements Cloneable{
    // PROPERTY
    /* liste du material prod : Material ou list de Material */
    private ArrayList<Object> listMaterialProd;

    // CONSTRUCTOR
    public CopexActionManipulation(String description, String comments, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listMaterialProd) {
        super(description, comments, namedAction, tabParam);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listMaterialProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, tabParam, taskRepeat);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, ArrayList<Object> listMaterialProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
        this.listMaterialProd = listMaterialProd;
    }

    // GETTER AND SETTER
    public ArrayList<Object> getListMaterialProd() {
        return listMaterialProd;
    }

    public void setListMaterialProd(ArrayList<Object> listMaterialProd) {
        this.listMaterialProd = listMaterialProd;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        CopexActionManipulation a = (CopexActionManipulation) super.clone() ;
        ArrayList<Object> listMaterialProdC = new ArrayList();
        int nbM = this.listMaterialProd.size() ;
        for (int i=0; i<nbM; i++){
            if (this.listMaterialProd.get(i) instanceof Material){
                listMaterialProdC.add((Material)((Material)this.listMaterialProd.get(i)).clone());
            }else if (this.listMaterialProd.get(i) instanceof ArrayList){
                ArrayList<Material> l = new ArrayList();
                int n = ((ArrayList)this.listMaterialProd.get(i)).size();
                for (int j=0; j<n; j++){
                    l.add((Material)((ArrayList<Material>)this.listMaterialProd.get(i)).get(j).clone());
                }
                listMaterialProdC.add(l);
            }
        }
        a.setListMaterialProd(listMaterialProdC);
        return a;
    }

    @Override
    public String toDescription(CopexPanel edP) {
        String s = super.toDescription(edP);
        int nbMatProd = listMaterialProd.size();
        for (int i=0; i<nbMatProd; i++){
            if (this.listMaterialProd.get(i) instanceof Material){
                s += "\n"+((InitialActionManipulation)this.namedAction).getListOutput().get(i).getTextProd()+" : "+((Material)listMaterialProd.get(i)).getName();
            }else if (this.listMaterialProd.get(i) instanceof ArrayList){
                int n = ((ArrayList)this.listMaterialProd.get(i)).size();
                s += "\n"+((InitialActionManipulation)this.namedAction).getListOutput().get(i).getTextProd()+" : ";
                for (int j=0; j<n; j++){
                    s += ((ArrayList<Material>)this.listMaterialProd.get(i)).get(j).getName()+" | ";
                }
            }
        }
        return s;
    }



}
