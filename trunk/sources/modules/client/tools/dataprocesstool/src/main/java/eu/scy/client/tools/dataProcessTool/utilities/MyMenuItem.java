/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

import java.awt.event.*;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

/**
 * items in the menu
 * @author 
 */
public class MyMenuItem extends JMenuItem implements  MouseListener {
    private ActionMenu action;
    private Image bg ;
    private Color bgColor;
    private ImageIcon img;
    private ImageIcon imgSurvol;
    private ImageIcon imgClic;
    private ImageIcon imgDisabled;
    private String toolTipText;
    private boolean isEnabled;

    public MyMenuItem(String toolTipText, Color bcolor, ImageIcon img, ImageIcon imgSurvol, ImageIcon imgClic, ImageIcon imgDisabled) {
        //super(null, img);
        this.bgColor = bcolor;
        this.bg = img.getImage();
        this.img = img;
        this.imgSurvol = imgSurvol;
        this.imgClic = imgClic;
        this.imgDisabled = imgDisabled;
        this.toolTipText = toolTipText;
        this.isEnabled = true;
        this.addMouseListener(this);
        //setSize(30, 26);
        setSize(30, 26);
        setPreferredSize(new Dimension(30,26));
        setOpaque(false);
        setToolTipText(toolTipText);
       /* UIManager.put("MenuItem.background", UIManager.getColor("Label.background"));
        UIManager.put("MenuItem.selectionBackground", UIManager.getColor("Label.background"));
        MyMenuItemUI myMenuItemUI = new MyMenuItemUI(UIManager.getColor("Label.background"));*/
        UIManager.put("MenuItem.background", SystemColor.control);
        UIManager.put("MenuItem.selectionBackground", SystemColor.control);
        MyMenuItemUI myMenuItemUI = new MyMenuItemUI(SystemColor.control);
        setUI(myMenuItemUI);
    }


    @Override
    protected void paintComponent(Graphics g) {
        //g.setColor(bgColor);
        super.paintComponent(g);
        //g.drawImage(this.bg, 0, 0, this.bgColor, null);
        if (!isEnabled)
            this.bg = this.imgDisabled.getImage();
        g.drawImage(bg, 0, 0, null);
    }

    public void addActionMenu(ActionMenu action){
        this.action = action;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isEnabled){
            bg = this.imgClic.getImage();
            repaint();
            if(action != null)
                action.actionClick(this);
            bg = this.img.getImage();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isEnabled){
            bg = this.img.getImage();
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isEnabled){
            bg = this.imgSurvol.getImage();
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (isEnabled){
            bg = this.img.getImage();
            repaint();
        }
    }


    @Override
    public void setEnabled(boolean b) {
       super.setEnabled(b);
        this.isEnabled = b;

        if (b){
            this.bg = this.img.getImage();
        }else{

            this.bg = this.imgDisabled.getImage();
        }
        repaint();
    }

    /* update icon */
    public void setItemIcon(ImageIcon newImg){
        this.img = newImg;
        this.bg = newImg.getImage();
        repaint();

    }
    public void setItemClicIcon(ImageIcon newImg){
        this.imgClic = newImg;
    }
    public void setItemRolloverIcon(ImageIcon newImg){
        this.imgSurvol = newImg;
    }

}
