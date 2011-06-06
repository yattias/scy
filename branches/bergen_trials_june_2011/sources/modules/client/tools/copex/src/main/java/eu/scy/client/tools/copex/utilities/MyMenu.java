/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package eu.scy.client.tools.copex.utilities;


import eu.scy.client.tools.copex.edp.EdPPanel;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


/**
 * surcharge du menu
 * @author MBO
 */
public class MyMenu extends JMenu implements MouseListener {
    // ATTRIBUTS
    private EdPPanel edP;
    private Image bg;
    private ImageIcon img;
    private ImageIcon imgSurvol;
    private ImageIcon imgClic;
    private ImageIcon imgDisabled;
    private String toolTipText;
    private Color bgColor;

    // CONSTRUCTEURS
    public MyMenu(EdPPanel edP, String toolTipText, Color bcolor, ImageIcon img, ImageIcon imgSurvol, ImageIcon imgClic, ImageIcon imgDisabled) {
        super("");
        this.edP = edP;
        this.bg = img.getImage();
        this.img = img;
        this.imgSurvol = imgSurvol;
        this.imgClic = imgClic;
        this.imgDisabled = imgDisabled;
        this.toolTipText = toolTipText;
        this.addMouseListener(this);
        setSize(30, 26);
        this.bgColor = bcolor;
        setOpaque(false);
        setToolTipText(toolTipText);
        /*UIManager.put("MenuBar.background", UIManager.getColor("Label.background"));
        UIManager.put("Menu.background", UIManager.getColor("Label.background"));
        UIManager.put("PopupMenu.background", UIManager.getColor("Label.background"));
        UIManager.put("MenuItem.background", UIManager.getColor("Label.background"));
      // UIManager.put("MenuItem.borderPainted", false);
        UIManager.put("MenuItem.selectionBackground", UIManager.getColor("Label.background"));
        UIManager.put("Menu.useMenuBarBackgroundForTopLevel", true);
        MyMenuUI myMenuUI = new MyMenuUI(UIManager.getColor("Label.background"));*/
        UIManager.put("MenuBar.background", SystemColor.control);
        UIManager.put("Menu.background", SystemColor.control);
        UIManager.put("PopupMenu.background", SystemColor.control);
        UIManager.put("MenuItem.background", SystemColor.control);
      // UIManager.put("MenuItem.borderPainted", false);
        UIManager.put("MenuItem.selectionBackground", SystemColor.control);
        UIManager.put("Menu.useMenuBarBackgroundForTopLevel", true);
        MyMenuUI myMenuUI = new MyMenuUI(SystemColor.control);
        setUI(myMenuUI);
    }

    
    
    @Override
    protected void paintComponent(Graphics g) {
       super.paintComponent(g);
        //g.drawImage(this.bg, 0, 0, this.bgColor, null);
        g.drawImage(bg, 0, 0, null);
        

        
    }
    // EVENEMENTS SOURIS
    public void mouseClicked(MouseEvent e) {
        if (isEnabled()){
            bg = this.imgClic.getImage();
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        if (isEnabled()){ 
            bg = this.img.getImage();
            repaint();
        }
    }

    public void mouseEntered(MouseEvent e) {
        if (isEnabled()){
            bg = this.imgSurvol.getImage();
            repaint();
        }
    }

    public void mouseExited(MouseEvent e) {
        if (isEnabled()){
            bg = this.img.getImage();
            repaint();
        }
    }

    

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (b){
            bg = this.img.getImage();
        }else{
            bg = this.imgDisabled.getImage();
        }
        repaint();
    }
    // OPERATIONS
    
    /* mise a jour de l'icone */
    public void setImage(ImageIcon img){
        this.img = img;
        this.bg = img.getImage();
    }
    /* mise a jour de l'icone */
    public void setImageSurvol(ImageIcon img){
        this.imgSurvol = img;
    }
    /* mise a jour de l'icone */
    public void setImageClic(ImageIcon img){
        this.imgClic = img;
    }
    /* mise a jour de l'icone */
    public void setImageDisabled(ImageIcon img){
        this.imgDisabled = img;
    }
}
