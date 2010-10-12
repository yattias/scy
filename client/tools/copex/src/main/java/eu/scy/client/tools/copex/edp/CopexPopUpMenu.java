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
 * popup menu on a click on the tree
 * @author Marjolaine
 */
public class CopexPopUpMenu extends JPopupMenu implements ActionListener{
    /* owner */
    private EdPPanel edP;
    /* controller */
    private ControllerInterface controller;
    /* tree */
    private CopexTree tree;
    // mode : step/action or task  cf cst POPUPMENU_
    private char mode;
    // mode manipulation, step, action, task or multinode
    private char modeNodeSel;

    private boolean canEdit;
    private boolean canCopy;
    private boolean canPaste;
    private boolean canCut;
    private boolean canSuppr;
    private boolean canAddAfter;
    private boolean canAddIn;

    private String actionAddTask = "add_task";
    private String actionAddTaskUnder = "add_task_under";
    private String actionAddTaskIn = "add_task_in";
    private String actionAddStepUnder = "add_step_under";
    private String actionAddStepIn = "add_step_in";
    private String actionAddActionUnder = "add_action_under";
    private String actionAddActionIn = "add_action_in";
    private String actionAddStep = "add_step";
    private String actionAddAction = "ad_action";
    private String actionPasteIn = "paste_in";
    private String actionPasteUnder = "paste_under";
    private String actionEdit = "edit";
    private String actionDelete = "delete";
    private String actionCut = "cut";
    private String actionCopy = "copy";


    public CopexPopUpMenu(EdPPanel edP, ControllerInterface controller, CopexTree tree, char mode,char modeNodeSel) {
        super();
        this.edP = edP;
        this.controller = controller;
        this.tree = tree;
        this.mode = mode;
        this.modeNodeSel = modeNodeSel;
    }

    
    public void init(){
        switch (modeNodeSel) {
            case MyConstants.POPUPMENU_MANIPULATION :
                if(mode == MyConstants.POPUPMENU_TASK){
                    if(canAddIn){
                        this.add(getMenuItem(edP.getBundleString("MENU_ADD_TASK"), edP.getCopexImage("add_task_in.png"),-1, -1, actionAddTask));
                    }
                    if(canPaste){
                        this.add(getMenuItem(edP.getBundleString("MENU_PASTE_IN"), edP.getCopexImage("paste_in.png"),-1, -1, actionPasteIn));
                    }
                }else{
                    if(canAddIn){
                        this.add(getMenuItem(edP.getBundleString("MENU_ADD_STEP"), edP.getCopexImage("add_step_in.png"),-1, -1, actionAddStep));
                        this.add(getMenuItem(edP.getBundleString("MENU_ADD_ACTION"), edP.getCopexImage("add_action_in.png"),-1, -1, actionAddAction));
                    }
                    if(canPaste){
                        this.add(getMenuItem(edP.getBundleString("MENU_PASTE_IN"), edP.getCopexImage("paste_in.png"),KeyEvent.VK_V, ActionEvent.CTRL_MASK, actionPasteIn));
                    }
                }
                break;
             case MyConstants.POPUPMENU_TASK :
                 if(canAddIn){
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_TASK_IN"), edP.getCopexImage("add_task_in.png"),-1, -1, actionAddTaskIn));
                 }
                 if(canAddAfter){
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_TASK_UNDER"), edP.getCopexImage("add_task_under.png"),-1, -1, actionAddTaskUnder));
                 }
                 if(canEdit){
                     this.add(getMenuItem(edP.getBundleString("MENU_EDIT"), edP.getCopexImage("edit.png"),-1, -1, actionEdit));
                 }
                 if(canSuppr){
                     this.add(getMenuItem(edP.getBundleString("MENU_DELETE"), edP.getCopexImage("delete.png"),KeyEvent.VK_DELETE, 0, actionDelete));
                 }
                 if(canCut){
                     this.add(getMenuItem(edP.getBundleString("MENU_CUT"), edP.getCopexImage("cut.png"),KeyEvent.VK_X, ActionEvent.CTRL_MASK, actionCut));
                 }
                 if(canCopy){
                     this.add(getMenuItem(edP.getBundleString("MENU_COPY"), edP.getCopexImage("copy.png"),KeyEvent.VK_C, ActionEvent.CTRL_MASK, actionCopy));
                 }
                 if(canPaste){
                     this.add(getMenuItem(edP.getBundleString("MENU_PASTE_UNDER"), edP.getCopexImage("paste_under.png"),KeyEvent.VK_V, ActionEvent.CTRL_MASK, actionPasteUnder));
                     this.add(getMenuItem(edP.getBundleString("MENU_PASTE_IN"), edP.getCopexImage("paste_in.png"),-1,-1, actionPasteIn));
                 }
                 break;
             case MyConstants.POPUPMENU_STEP :
                 if(canAddIn){
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_STEP_IN"), edP.getCopexImage("add_step_in.png"),-1, -1, actionAddStepIn));
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_ACTION_IN"), edP.getCopexImage("add_action_in.png"),-1, -1, actionAddActionIn));
                 }
                 if(canAddAfter){
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_STEP_UNDER"), edP.getCopexImage("add_step_under.png"),-1, -1, actionAddStepUnder));
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_ACTION_UNDER"), edP.getCopexImage("add_action_under.png"),-1, -1, actionAddActionUnder));
                 }
                 if(canEdit){
                     this.add(getMenuItem(edP.getBundleString("MENU_EDIT"), edP.getCopexImage("edit.png"),-1, -1, actionEdit));
                 }
                 if(canSuppr){
                     this.add(getMenuItem(edP.getBundleString("MENU_DELETE"), edP.getCopexImage("delete.png"),KeyEvent.VK_DELETE, 0, actionDelete));
                 }
                 if(canCut){
                     this.add(getMenuItem(edP.getBundleString("MENU_CUT"), edP.getCopexImage("cut.png"),KeyEvent.VK_X, ActionEvent.CTRL_MASK, actionCut));
                 }
                 if(canCopy){
                     this.add(getMenuItem(edP.getBundleString("MENU_COPY"), edP.getCopexImage("copy.png"),KeyEvent.VK_C, ActionEvent.CTRL_MASK, actionCopy));
                 }
                 if(canPaste){
                     this.add(getMenuItem(edP.getBundleString("MENU_PASTE_UNDER"), edP.getCopexImage("paste_under.png"),KeyEvent.VK_V, ActionEvent.CTRL_MASK, actionPasteUnder));
                     this.add(getMenuItem(edP.getBundleString("MENU_PASTE_IN"), edP.getCopexImage("paste_in.png"),-1,-1, actionPasteIn));
                 }
                 break;
             case MyConstants.POPUPMENU_ACTION :
                 if(canAddAfter){
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_STEP_UNDER"), edP.getCopexImage("add_step_under.png"),-1, -1, actionAddStepUnder));
                     this.add(getMenuItem(edP.getBundleString("MENU_ADD_ACTION_UNDER"), edP.getCopexImage("add_action_under.png"),-1, -1, actionAddActionUnder));
                 }
                 if(canEdit){
                     this.add(getMenuItem(edP.getBundleString("MENU_EDIT"), edP.getCopexImage("edit.png"),-1, -1, actionEdit));
                 }
                 if(canSuppr){
                     this.add(getMenuItem(edP.getBundleString("MENU_DELETE"), edP.getCopexImage("delete.png"),KeyEvent.VK_DELETE, 0, actionDelete));
                 }
                 if(canCut){
                     this.add(getMenuItem(edP.getBundleString("MENU_CUT"), edP.getCopexImage("cut.png"),KeyEvent.VK_X, ActionEvent.CTRL_MASK, actionCut));
                 }
                 if(canCopy){
                     this.add(getMenuItem(edP.getBundleString("MENU_COPY"), edP.getCopexImage("copy.png"),KeyEvent.VK_C, ActionEvent.CTRL_MASK, actionCopy));
                 }
                 if(canPaste){
                     this.add(getMenuItem(edP.getBundleString("MENU_PASTE_UNDER"), edP.getCopexImage("paste_under.png"),KeyEvent.VK_V, ActionEvent.CTRL_MASK, actionPasteUnder));
                 }
                 break;
             case MyConstants.POPUPMENU_MULTINODE :
                 if(canSuppr){
                     this.add(getMenuItem(edP.getBundleString("MENU_DELETE"), edP.getCopexImage("delete.png"),KeyEvent.VK_DELETE, 0, actionDelete));
                 }
                 if(canCut){
                     this.add(getMenuItem(edP.getBundleString("MENU_CUT"), edP.getCopexImage("cut.png"),KeyEvent.VK_X, ActionEvent.CTRL_MASK, actionCut));
                 }
                 if(canCopy){
                     this.add(getMenuItem(edP.getBundleString("MENU_COPY"), edP.getCopexImage("copy.png"),KeyEvent.VK_C, ActionEvent.CTRL_MASK, actionCopy));
                 }
                 break;
             default:
                break;
        }
        //this.itemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK))
        //this.itemSuppr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }

    private JMenuItem getMenuItem(String text, ImageIcon icon, int keyCode, int modifiers, String actionCommand){
        JMenuItem menuItem = new JMenuItem(text, icon);
        if(keyCode != -1){
            menuItem.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        }
        menuItem.addActionListener(this);
        menuItem.setActionCommand(actionCommand);
        return menuItem;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(actionCopy)){
            copy();
        }else if(e.getActionCommand().equals(actionCut)){
            cut();
        }else if(e.getActionCommand().equals(actionEdit)){
            edit();
        }else if(e.getActionCommand().equals(actionDelete)){
            suppr();
        }else if(e.getActionCommand().equals(actionAddAction)){
            addActionIn();
        }else if(e.getActionCommand().equals(actionAddActionIn)){
            addActionIn();
        }else if(e.getActionCommand().equals(actionAddActionUnder)){
            addActionAfter();
        }else if(e.getActionCommand().equals(actionAddStep)){
            addStepIn();
        }else if(e.getActionCommand().equals(actionAddStepIn)){
            addStepIn();
        }else if(e.getActionCommand().equals(actionAddStepUnder)){
            addStepAfter();
        }else if(e.getActionCommand().equals(actionAddTask)){
            addStepIn();
        }else if(e.getActionCommand().equals(actionAddTaskIn)){
            addStepIn();
        }else if(e.getActionCommand().equals(actionAddTaskUnder)){
            addStepAfter();
        }else if(e.getActionCommand().equals(actionPasteIn)){
            pasteIn();
        }else if(e.getActionCommand().equals(actionPasteUnder)){
            pasteUnder();
        }
    }
    

    /* colle */
    private void pasteIn() {
        this.edP.pasteIn();
    }

    /* colle */
    private void pasteUnder() {
        this.edP.pasteUnder();
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
        this.tree.edit(false);
    }
    
    /* rend enabled l'item insert in */
    public void setInsertInEnabled(boolean enabled){
        this.canAddIn = enabled;
    }
    /* rend enabled l'item insert after */
    public void setInsertAfterEnabled(boolean enabled){
        this.canAddAfter = enabled;
    }
    /* rend enabled l'item couper  */
    public void setCutItemEnabled(boolean enabled){
        this.canCut = enabled;
    }
    /* rend enabled l'item copier  */
    public void setCopyItemEnabled(boolean enabled){
        this.canCopy = enabled;
    }
    /* rend enabled l'item coller  */
    public void setPasteItemEnabled(boolean enabled){
        this.canPaste = enabled;
    }
    /* rend enabled l'item suppr  */
    public void setSupprItemEnabled(boolean enabled){
        this.canSuppr = enabled;
    }
    /* rend enabled l'item edit  */
    public void setEditItemEnabled(boolean enabled){
        this.canEdit = enabled;
    }
}
