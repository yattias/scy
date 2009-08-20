/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.edp.MaterialPanel;
import java.awt.Color;
import javax.swing.*;

/**
 * liste d'elements qui permet d'afficher une liste avec des icones
 * @author MBO
 */
public class MyList extends JList  {

    // ATTRIBUTS
    private EdPPanel edP;
    private MaterialPanel owner;

    public MyList(EdPPanel edP, MaterialPanel owner, String[] data) {
        super(data);
        this.edP = edP;
        this.owner = owner;
        setBackground(Color.WHITE);
    }
    
    
    public Icon getMaterialIcon(){
        ImageIcon i = edP.getCopexImage("Puce-mat_ronde.png");
        return i;
    }
   
}
