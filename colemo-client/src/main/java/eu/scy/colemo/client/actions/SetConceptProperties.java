package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.actions.BaseAction;
import eu.scy.colemo.client.propertyeditors.ConceptPropertyEditor;
import eu.scy.colemo.client.SelectionController;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.core.model.impl.ScyBaseObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:50:11
 * To change this template use File | Settings | File Templates.
 */
public class SetConceptProperties extends BaseAction {
    public SetConceptProperties(String name) {
        super(name);
    }

    public Class getOperateson() {
        return UmlClass.class;
    }

    protected void performAction(ActionEvent e) {

        ConceptPropertyEditor editor = new ConceptPropertyEditor();
        editor.setModel(SelectionController.getDefaultInstance().getCurrentPrimarySelection());

        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setPreferredSize(new Dimension(500,500));
        dialog.getContentPane().add(editor);
        dialog.pack();
        dialog.setVisible(true);


    }
}
