/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * separe les panneaux à droite : materiels, feuilles de données 
 * @author MBO
 */
public class MySeparator extends JSeparator{

    // CONSTANTES
    public final static int HEIGHT_SEP = 3;
    private static Color COLOR_SEP = Color.WHITE;

    // CONSTRUCTEUR
    public MySeparator(int width) {
        super(SwingConstants.HORIZONTAL);
        setBackground(COLOR_SEP);
        setSize(width, HEIGHT_SEP);
    }
    
    
}
