/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.EdPPanel;
import java.util.ArrayList;
import org.jdom.Element;

/**
 * action de type manipulation
 * @author Marjolaine
 */
public class CopexActionManipulation extends CopexActionParam implements Cloneable{
    // PROPERTY
    /* liste du material prod */
    private ArrayList<Material> listMaterialProd;

    // CONSTRUCTOR
    public CopexActionManipulation(String description, String comments, InitialNamedAction namedAction, ActionParam[] tabParam, ArrayList<Material> listMaterialProd) {
        super(description, comments, namedAction, tabParam);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, ActionParam[] tabParam, ArrayList<Material> listMaterialProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, tabParam, taskRepeat);
        this.listMaterialProd = listMaterialProd;
    }

    public CopexActionManipulation(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, ActionParam[] tabParam, ArrayList<Material> listMaterialProd, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
        this.listMaterialProd = listMaterialProd;
    }

    // GETTER AND SETTER
    public ArrayList<Material> getListMaterialProd() {
        return listMaterialProd;
    }

    public void setListMaterialProd(ArrayList<Material> listMaterialProd) {
        this.listMaterialProd = listMaterialProd;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        CopexActionManipulation a = (CopexActionManipulation) super.clone() ;
        ArrayList<Material> listMaterialProdC = new ArrayList();
        int nbM = this.listMaterialProd.size() ;
        for (int i=0; i<nbM; i++){
            listMaterialProdC.add((Material)this.listMaterialProd.get(i).clone());
        }
        a.setListMaterialProd(listMaterialProdC);
        return a;
    }

    @Override
    public String toDescription(EdPPanel edP) {
        String s = super.toDescription(edP);
        int nbMatProd = listMaterialProd.size();
        for (int i=0; i<nbMatProd; i++){
            s += "\n "+((InitialActionManipulation)this.namedAction).getTextProd()+" : "+listMaterialProd.get(i).getName();
        }
        return s;
    }



}
