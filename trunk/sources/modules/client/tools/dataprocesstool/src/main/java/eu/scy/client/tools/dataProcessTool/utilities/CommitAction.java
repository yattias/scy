/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Marjolaine
 */
public class CommitAction extends AbstractAction {
    private MyTextFieldCompletion field;

    public CommitAction(MyTextFieldCompletion field) {
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (field.getMode() == MyTextFieldCompletion.Mode.COMPLETION) {
                int pos = field.getSelectionEnd();
                //field.insert(" ", pos);
                //field.setCaretPosition(pos + 1);
                field.setCaretPosition(pos);
                field.setMode(MyTextFieldCompletion.Mode.INSERT);
            } else {
                field.replaceSelection("\n");
            }

    }

}
