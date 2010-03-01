/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.tools.copex.utilities;


import eu.scy.tools.copex.edp.CopexTree;
import javax.swing.event.*;
import javax.swing.tree.*;
/**
 *
 * @author Marjolaine
 */
public class CopexTreeSelectionListener implements TreeSelectionListener {
    private CopexTree tree;

    public CopexTreeSelectionListener(CopexTree tree) {
        this.tree = tree;
    }


    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath[] selPath = tree.getSelectionPaths();
        if (selPath == null || selPath.length == 0)
               return;
        // recupere les objets selectionnes :
        tree.selectNode();
    }
}
