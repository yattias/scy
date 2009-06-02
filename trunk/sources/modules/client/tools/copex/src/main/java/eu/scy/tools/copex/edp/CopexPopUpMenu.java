/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;


/**
 * reprensente le menu apparaissant au clic droit sur l'arbre
 * @author MBO
 */
public class CopexPopUpMenu extends JPopupMenu implements ActionListener{
    // ATTRIBUTS
    /* fenetre mere */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* arbre */
    private CopexTree tree;
    // item 
    private JMenuItem itemCut = null;
    private JMenuItem itemCopy= null;
    private JMenuItem itemPaste= null;
    private JMenuItem itemSuppr= null;
    private JMenuItem itemEdit= null;
    private JMenu menuItemInserer= null;
    private JMenuItem itemSsQ = null;
    private JMenuItem itemStep = null;
    private JMenuItem itemAction = null;

    // CONSTRUCTEURS 
    public CopexPopUpMenu(EdPPanel edP, ControllerInterface controller, CopexTree tree) {
        super();
        this.edP = edP;
        this.controller = controller;
        this.tree = tree;
        init();
    }

    
    // METHODES
    private void init(){
        this.itemSsQ = new JMenuItem(edP.getBundleString("MENU_SS_QUESTION"));
        this.itemSsQ.addActionListener(this);
        this.itemStep = new JMenuItem(edP.getBundleString("MENU_STEP"), KeyEvent.VK_M);
        this.itemStep.addActionListener(this);
        this.itemStep.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
	this.itemAction = new JMenuItem(edP.getBundleString("MENU_ACTION"), KeyEvent.VK_N);
        this.itemAction.addActionListener(this);
        this.itemAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	this.menuItemInserer = new JMenu(edP.getBundleString("MENU_ADD"));
        this.menuItemInserer.add(this.itemSsQ);
        this.menuItemInserer.add(this.itemStep);
        this.menuItemInserer.add(this.itemAction);
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
	this.add(this.menuItemInserer);
        this.addSeparator();
        this.add(this.itemCut);
        this.add(this.itemCopy);
        this.add(this.itemPaste);
        this.addSeparator();
        this.add(this.itemSuppr);
        this.addSeparator();
        this.add(this.itemEdit);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.itemSsQ){
            addSsQuestion();
        }else if (e.getSource() == this.itemStep){
            addStep();
        }else if (e.getSource() == this.itemAction){
            addAction();
        }else if (e.getSource() == this.itemCut){
            cut();
        }else if (e.getSource() == this.itemCopy){
            copy();
        }else if (e.getSource() == this.itemPaste){
            paste();
        }else if (e.getSource() == this.itemSuppr){
            suppr();
        }else if (e.getSource() == this.itemEdit){
            edit();
        }
    }
    
    /* insertion d'une sous question */
    private void addSsQuestion(){
        this.controller.addQuestion();
    }

    /* colle */
    private void paste() {
        CopexReturn cr = this.controller.paste();
        if (cr.isError()){
            this.edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* supprimer */
    private void suppr() {
        this.edP.suppr();
    }
    
    /* ajout d'une action*/
    private void addAction() {
        this.controller.addAction();
    }

    /* ajout d'une etape */
    private void addStep() {
        this.controller.addEtape();
    }

    /* copier */
    private void copy() {
        CopexReturn cr = this.controller.copy();
        if (cr.isError()){
            this.edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* couper */
    private void cut() {
        CopexReturn cr = this.controller.cut();
        if (cr.isError()){
            this.edP.displayError(cr, edP.getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /*edition */
    private void edit() {
        this.tree.edit();
    }
    
    /* rend enabled le menu ajouter */
    public void setAddMenuEnabled(boolean enabled){
        this.menuItemInserer.setEnabled(enabled);
    }
    
    /* rend enabled l'item ajout ss question  */
    public void setAddQEnabled(boolean enabled){
        this.itemSsQ.setEnabled(enabled);
    }
    /* rend enabled l'item ajout etape  */
    public void setAddSEnabled(boolean enabled){
        this.itemStep.setEnabled(enabled);
    }
    /* rend enabled l'item ajout action  */
    public void setAddAEnabled(boolean enabled){
        this.itemAction.setEnabled(enabled);
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
