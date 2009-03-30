package eu.scy.colemo.client;

import eu.scy.colemo.client.actions.BaseAction;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2009
 * Time: 06:44:51
 * To change this template use File | Settings | File Templates.
 */
public class SCYMapperToolbar extends JToolBar implements SelectionControllerListener{

    public SCYMapperToolbar() {
        setPreferredSize(new Dimension(200,40));
    }

    public void selectionPerformed(Object selected) {
        removeAll();
        List baseActions = ActionController.getDefaultInstance().getActions(selected);
        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        for (int i = 0; i < baseActions.size(); i++) {
            BaseAction baseAction = (BaseAction) baseActions.get(i);
            JButton button = new JButton(baseAction);
            button.revalidate();
            add(button);
        }

        invalidate();
        validate();
        repaint();

    }


}
