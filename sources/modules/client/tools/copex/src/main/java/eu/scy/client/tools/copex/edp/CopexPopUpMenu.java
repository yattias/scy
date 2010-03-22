/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;


/**
 * reprensente le menu apparaissant au clic droit sur l'arbre
 * @author MBO
 */
public class CopexPopUpMenu extends JPopupMenu implements ActionListener{
    /* fenetre mere */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* arbre */
    private CopexTree tree;
    // mode : etape, action ou tache  cf cst POPUPMENU_
    private char mode;
    // item 
    private JMenuItem itemCut = null;
    private JMenuItem itemCopy= null;
    private JMenuItem itemPaste= null;
    private JMenuItem itemSuppr= null;
    private JMenuItem itemEdit= null;
    private JMenuItem itemStepIn = null;
    private JMenuItem itemStepAfter = null;
    private JMenuItem itemActionIn = null;
    private JMenuItem itemActionAfter = null;

    // CONSTRUCTEURS 
    public CopexPopUpMenu(EdPPanel edP, ControllerInterface controller, CopexTree tree, char mode) {
        super();
        this.edP = edP;
        this.controller = controller;
        this.tree = tree;
        this.mode = mode;
        init();
    }

    
    // METHODES
    private void init(){
        switch (mode) {
            case MyConstants.POPUPMENU_STEP :
                itemStepIn = new JMenuItem(edP.getBundleString("MENU_STEP_IN"), KeyEvent.VK_M);
                itemStepIn.addActionListener(this);
                itemStepIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
                itemStepAfter = new JMenuItem(edP.getBundleString("MENU_STEP_AFTER"));
                itemStepAfter.addActionListener(this);
                itemActionIn = new JMenuItem(edP.getBundleString("MENU_ACTION_IN"), KeyEvent.VK_N);
                itemActionIn.addActionListener(this);
                itemActionIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
                itemActionAfter = new JMenuItem(edP.getBundleString("MENU_ACTION_AFTER"));
                itemActionAfter.addActionListener(this);
                this.add(itemStepIn);
                this.add(itemStepAfter);
                this.add(itemActionIn);
                this.add(itemActionAfter);
                this.addSeparator();
                break;
            case MyConstants.POPUPMENU_ACTION :
                this.itemStepAfter = new JMenuItem(edP.getBundleString("MENU_STEP_AFTER"), KeyEvent.VK_N);
                this.itemStepAfter.addActionListener(this);
                this.itemStepAfter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
                itemActionAfter = new JMenuItem(edP.getBundleString("MENU_ACTION_AFTER"));
                itemActionAfter.addActionListener(this);
                this.add(itemStepAfter);
                this.add(itemActionAfter);
                this.addSeparator();
                break;
            case MyConstants.POPUPMENU_TASK :
                itemStepIn = new JMenuItem(edP.getBundleString("MENU_TASK_IN"), KeyEvent.VK_M);
                itemStepIn.addActionListener(this);
                itemStepIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
                itemStepAfter = new JMenuItem(edP.getBundleString("MENU_TASK_AFTER"));
                itemStepAfter.addActionListener(this);
                this.add(itemStepIn);
                this.add(itemStepAfter);
                this.addSeparator();
                break;
            case MyConstants.POPUPMENU_UNDEF :
                break;
            default :
                break;
        }
        this.itemCut = new JMenuItem(edP.getBundleString("TOOLTIPTEXT_MENU_CUT"));
        this.itemCut.addActionListener(this);
        this.itemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        this.itemCopy = new JMenuItem(edP.getBundleString("TOOLTIPTEXT_MENU_COPY"));
        this.itemCopy.addActionListener(this);
        this.itemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        this.itemPaste = new JMenuItem(edP.getBundleString("TOOLTIPTEXT_MENU_PASTE"));
        this.itemPaste.addActionListener(this);
        this.itemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        this.itemSuppr = new JMenuItem(edP.getBundleString("TOOLTIPTEXT_MENU_SUPPR"));
        this.itemSuppr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        this.itemSuppr.addActionListener(this);
        this.itemEdit = new JMenuItem(edP.getBundleString("MENU_EDIT"));
        this.itemEdit.addActionListener(this);
        this.itemEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        this.add(this.itemCut);
        this.add(this.itemCopy);
        this.add(this.itemPaste);
        this.addSeparator();
        this.add(this.itemSuppr);
        this.addSeparator();
        this.add(this.itemEdit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.itemStepIn != null && e.getSource().equals(this.itemStepIn)){
            addStepIn();
        }else if(this.itemStepAfter != null && e.getSource().equals(this.itemStepAfter)){
            addStepAfter();
        }else if(this.itemActionIn != null && e.getSource().equals(this.itemActionIn)){
            addActionIn();
        }else if(this.itemActionAfter != null && e.getSource().equals(this.itemActionAfter)){
            addActionAfter();
        }else if (e.getSource().equals(this.itemCut)){
            cut();
        }else if (e.getSource().equals(this.itemCopy)){
            copy();
        }else if (e.getSource().equals(this.itemPaste)){
            paste();
        }else if (e.getSource().equals(this.itemSuppr)){
            suppr();
        }else if (e.getSource().equals(this.itemEdit)){
            edit();
        }
    }
    

    /* colle */
    private void paste() {
        this.edP.paste();
    }

    /* supprimer */
    private void suppr() {
        this.edP.suppr();
    }
    
    /* ajout d'une action*/
    private void addActionAfter() {
        this.edP.addActionAfter();
    }

    /* ajout d'une etape */
    private void addStepAfter() {
        this.edP.addStepAfter();
    }
    /* ajout d'une action*/
    private void addActionIn() {
        this.edP.addActionIn();
    }

    /* ajout d'une etape */
    private void addStepIn() {
        this.edP.addStepIn();
    }

    /* copier */
    private void copy() {
        edP.copy();
    }

    /* couper */
    private void cut() {
        edP.cut();
    }

    /*edition */
    private void edit() {
        this.tree.edit();
    }
    
    
    /* rend enabled l'item insert in */
    public void setInsertInEnabled(boolean enabled){
        if(this.itemStepIn != null)
            this.itemStepIn.setEnabled(enabled);
        if(this.itemActionIn != null)
            this.itemActionIn.setEnabled(enabled);
    }
    /* rend enabled l'item insert after */
    public void setInsertAfterEnabled(boolean enabled){
        if(this.itemStepAfter != null)
            this.itemStepAfter.setEnabled(enabled);
        if(this.itemActionAfter != null)
            this.itemActionAfter.setEnabled(enabled);
    }
    /* rend enabled l'item couper  */
    public void setCutItemEnabled(boolean enabled){
        this.itemCut.setEnabled(enabled);
    }
    /* rend enabled l'item copier  */
    public void setCopyItemEnabled(boolean enabled){
        this.itemCopy.setEnabled(enabled);
    }
    /* rend enabled l'item coller  */
    public void setPasteItemEnabled(boolean enabled){
        this.itemPaste.setEnabled(enabled);
    }
    /* rend enabled l'item suppr  */
    public void setSupprItemEnabled(boolean enabled){
        this.itemSuppr.setEnabled(enabled);
    }
    /* rend enabled l'item edit  */
    public void setEditItemEnabled(boolean enabled){
        this.itemEdit.setEnabled(enabled);
    }
}
