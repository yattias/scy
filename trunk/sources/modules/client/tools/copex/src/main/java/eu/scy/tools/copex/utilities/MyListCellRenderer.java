/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import java.awt.Component;
import javax.swing.*;

/**
 * represente l'apparence d'une liste (MyList) 
 * permet d'afficher une icone et le texte
 * @author MBO
 */
public class MyListCellRenderer extends DefaultListCellRenderer{

    // ATTRIBUTS
    private MyList myList;

    public MyListCellRenderer(MyList myList) {
        super();
        this.myList = myList;
    }
    
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c =  super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Icon icon = myList.getMaterialIcon();
        setIcon(icon);
        return c;
    }

    
}
