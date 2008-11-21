package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.actions.BaseAction;
import eu.scy.colemo.client.SelectionController;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.nov.2008
 * Time: 14:13:01
 * To change this template use File | Settings | File Templates.
 */
public class AddConnectionAction extends AbstractAction {
    public void actionPerformed(ActionEvent e) {
        if(SelectionController.getDefaultInstance().getSelectModus().equals(SelectionController.DOUBLE_SELECT_MODUS)) {
            SelectionController.getDefaultInstance().setSelectModus(SelectionController.SINGLE_SELECT_MODUS);
        } else {
            SelectionController.getDefaultInstance().setSelectModus(SelectionController.DOUBLE_SELECT_MODUS);    
        }

    }
}
