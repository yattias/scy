/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;

/**
 * bordure arrondie du panel de detail du materiel
 * @author MBO
 */
public class RoundedBorder  implements Border {

    // ATTRIBUTS 
    private Color couleur;
    private int largeurArc;
    private int hauteurArc;

    
    public RoundedBorder(Color couleur, int largeurArc, int hauteurArc)  {
	this.couleur = couleur;
	this.largeurArc = largeurArc;
	this.hauteurArc = hauteurArc;
    }  

    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(couleur);
        int adjustXY = 1;//pour ajuster le dessin en x et y
        int adjustWH = 1;//idem pour width et height
        //pour eviter les escalier sur l'arrondi
        Graphics2D g2 = (Graphics2D)g;
        BasicStroke stroke = new BasicStroke(6,BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
        g2.setColor(getLoweredColor(couleur));
        g2.setStroke(stroke);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(x+adjustXY, y+adjustXY, width-adjustWH, height-adjustWH, this.largeurArc, this.hauteurArc);
        //g2.drawRoundRect(x+adjustXY+4, y+adjustXY+4, width-adjustWH-8, height-adjustWH-8, this.largeurArc-4, this.hauteurArc-4);
        //g2.drawRoundRect(x+adjustXY+3, y+adjustXY+3, width-adjustWH-6, height-adjustWH-6, this.largeurArc-3, this.hauteurArc-3);
        //g2.drawRoundRect(x+adjustXY+2, y+adjustXY+2, width-adjustWH-4, height-adjustWH-4, this.largeurArc-2, this.hauteurArc-2);
        //g2.drawRoundRect(x+adjustXY+1, y+adjustXY+1, width-adjustWH-2, height-adjustWH-2, this.largeurArc-1, this.hauteurArc-1);
        
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(0,0,0,0); 

    }

    public boolean isBorderOpaque() {
        return true; 

    }

    /* retourne une couleur + foncee que la reference */
    public Color getRaisedColor(Color ref){
        return ref.darker() ;
    }
    
    /* retourne une couleur + claire que la reference */
    public Color getLoweredColor(Color ref){
        return ref.brighter() ;
    }
}
