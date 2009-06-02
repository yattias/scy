/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.CopexTree;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * permet d'ecouter les evenements souris de selection sur l'arbre
 * @author MBO
 */
public class MyTreeSelectionListener implements TreeSelectionListener{

    // ATTRIBUTS
    private CopexTree tree;

    // CONSTRUCTEURS
    public MyTreeSelectionListener(CopexTree tree) {
        this.tree = tree;
    }
    
    
    public void valueChanged(TreeSelectionEvent e) {
        TreePath[] selPath = tree.getSelectionPaths();
        if (selPath == null || selPath.length == 0)
               return;
        // recupere les objets selectionnes :
        tree.selectNode();
    }

}
