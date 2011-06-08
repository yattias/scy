/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.utilities;

import eu.scy.client.tools.copex.edp.EdPPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * represents the arrow to hide/show elements 
 * @author Marjolaine
 */
public class ButtonArrowPanel extends JPanel implements MouseListener{
    /* copex editor */
    private EdPPanel edP;
    /* owner */
    private CopexPanelHideShow owner;
    /* background image */
    private Image bg;
    /* arrow hide image */
    private ImageIcon bgHide;
    /* arrow show image*/
    private ImageIcon bgShow;
    /* mode */
    private boolean modeHide;
    /* enabled */
    private boolean buttonEnabled;

   
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
    
     
     
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg == null){
            bg = bgHide.getImage();
        }
         boolean d  =g.drawImage(bg, 0, 0, null) ;
        while (!d){
           d =  g.drawImage(bg, 0, 0, null);
        }
    }
    
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

    @Override
    public void mouseClicked(MouseEvent e) {
       if(!buttonEnabled)
           return ;
       if (modeHide)
           setShow();
       else
           setHide();
       owner.setHide(modeHide);
    }

    @Override
    public void mousePressed(MouseEvent e) {
      
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    public void setButtonEnabled(boolean enabled){
        this.buttonEnabled = enabled ;
    }

    public void setImgHide(ImageIcon img){
        this.bgHide = img;
        bg = bgHide.getImage();
        repaint();
    }

    public void setImgShow(ImageIcon img){
        this.bgShow = img;
        repaint();
    }
}
