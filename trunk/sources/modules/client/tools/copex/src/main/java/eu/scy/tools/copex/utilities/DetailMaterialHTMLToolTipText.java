/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.Parameter;
import java.util.ArrayList;

/**
 * code html peremttant d'afficher le detail d'un materiel
 * @author MBO
 */
public class DetailMaterialHTMLToolTipText {
    // ATTRIBUTS
    /* material */
    private Material material;
    /* texte HTML correspondant */
    private String htmlMaterial;

    // CONSTRUCTEUR
    public DetailMaterialHTMLToolTipText(Material material) {
        this.material = material;
        this.htmlMaterial = "";
        init();
    }
    
    private void init(){
        htmlMaterial = "<html><table width='300' border='2' bgcolor='#FFFFFF' bordercolor='#00FFFF'><tr> <table width='300' border='0' bgcolor='#FFFFFF' ><tr><strong>"+material.getName()+"</strong></tr>";
        htmlMaterial += "<tr>"+material.getTypeToDisplay()+"</tr>";
        htmlMaterial += "<tr>"+material.getDescription()+"</tr>";
        ArrayList<Parameter> listP = material.getListParameters();
        int nbP = listP.size();
        for (int i=0; i<nbP; i++){
            Parameter p = listP.get(i);
            htmlMaterial += "<tr>"+p.getName()+ " = "+p.getValue()+" "+p.getUnit().getSymbol()+ "</tr>" ;
        }
        htmlMaterial += "</table></tr></table></html>";
    }
    
    public String getHtmlMaterial(){
        return this.htmlMaterial;
    }
    
}
