/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * panel avec un bord
 * @author Marjolaine Bodin
 */
public class BorderPanel extends JPanel {
    // CONSTANTES 
    private static int BORDURE = 3;
    public static Color borderColor = new Color(153,204,255);
    
    // ATTRIBUTS

    // CONSTRUCTEURS
    public BorderPanel() {
        super();
        //this.borderColor = new Color(153, 204, 255) ;
        this.setLayout(new BorderLayout());
        setOpaque(true);
        setSize(800, 600);
        setName("BorderPanel");
    }

    
    
    // METHODES
    public static int getBorderSize() {
	return BORDURE;
    }
    /**
    * Retourne une couleur + foncee par rapport a la reference
    * @param ref java.awt.Color
    */
    public Color getLoweredColor(Color ref) {
	return ref.brighter() ;	
    }
    /**
    * Retourne une couleur + foncee par rapport a la reference
    * @param ref java.awt.Color
    */
    public Color getRaisedColor(Color ref) {
	return ref.darker() ;	
    }
    /** 
    * Methode heritee du JPanel :
    * On change l'ordre dans lequel les elements sont redessines de facon a ce que la bordure
    * soit toujours visible.
    */
//    @Override
//    public void paint(Graphics g) {
//	super.paint(g) ;
//	paintBorder(g) ;
//    }
    
    /* On repaint ici le fond du Panel */
//    @Override
//    public void paintComponent(Graphics g) {
//        if(isOpaque()) {
//            g.setColor(getBackground()) ;
//            g.fillRect(0,0,getWidth(),getHeight()) ;
//	}
//    }
    
    /** <BR>
    * Cette methode construit la bordure qui caracterise le BooPanel afin
    * de personnaliser les fenetres de l'appli.
    */
    public void setBorder() {
//        this.borderColor =  Color.decode("#B6D8DB");
//        if (this.borderColor == null){
//            this.borderColor = new Color(153, 204, 255) ;
//        }
//
//	Border raisedbevel = BorderFactory.createBevelBorder(1, getRaisedColor(this.borderColor), getLoweredColor(this.borderColor));
//	Border loweredbevel = BorderFactory.createBevelBorder(0, getRaisedColor(this.borderColor), getLoweredColor(this.borderColor));
//	Border compound1, compound2;
//	Border line = BorderFactory.createLineBorder(this.borderColor, BORDURE);
//	compound1 = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
//	compound2 = BorderFactory.createCompoundBorder(line, compound1);
//	super.setBorder(compound2);
    }
    
    
    
//    @Override
//    public void setBorder(Border bord){
//	this.setBorder() ;
//    }
}
