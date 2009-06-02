/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.EdPPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * represente la fleche pour cacher  / montrer des elements
 * @author MBO
 */
public class ButtonArrowPanel extends JPanel implements MouseListener{
    //ATTRIBUTS 
    /* fenetre mere */
    private EdPPanel edP;
    /* owner */
    private CopexPanelHideShow owner;
    /* image de fond */
    private Image bg;
    /* image fleche cachee */
    private ImageIcon bgHide;
    /* image fleche montre */
    private ImageIcon bgShow;
    /* mode */
    private boolean modeHide;
    /* enabled */
    private boolean buttonEnabled;

   
    // CONSTRUCTEURS 
     public ButtonArrowPanel(EdPPanel edP, CopexPanelHideShow owner) {
         super();
         this.buttonEnabled = true;
         this.edP = edP;
         this.owner = owner;
         bgHide = edP.getCopexImage("Bouton-mat_triangle_right.png");
         bgShow = edP.getCopexImage("Bouton-mat_triangle_down.png");        
         setSize(12, 12);
         addMouseListener(this);
         setPreferredSize(new Dimension(12, 12));
         setHide();
    }
    
     
     
    // OVERRIDE METHOD  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg == null){
            System.out.println("image null");
            bg = bgHide.getImage();
        }
         boolean d  =g.drawImage(bg, 0, 0, null) ;
        while (!d){
           d =  g.drawImage(bg, 0, 0, null);
        }
    }
    
    // METHODE 
    public void setHide(){
        modeHide = true;
        bg = bgHide.getImage();
        repaint();
    }
    public void setShow(){
        modeHide = false;
        bg = bgShow.getImage();
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
       if(!buttonEnabled)
           return ;
       if (modeHide)
           setShow();
       else
           setHide();
       owner.setHide(modeHide);
    }

    public void mousePressed(MouseEvent e) {
      
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }

    public void setButtonEnabled(boolean enabled){
        this.buttonEnabled = enabled ;
    }
}
