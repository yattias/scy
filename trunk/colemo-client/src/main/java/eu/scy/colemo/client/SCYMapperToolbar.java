package eu.scy.colemo.client;

import eu.scy.colemo.client.actions.BaseAction;
import eu.scy.colemo.client.actions.SCYMapperAction;

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
        List commonActions = ActionController.getDefaultInstance().getCommonActions();
        addCommonActions(commonActions);
    }

    public void selectionPerformed(Object selected) {
        removeAll();
        List commonActions = ActionController.getDefaultInstance().getCommonActions();
        List baseActions = ActionController.getDefaultInstance().getActions(selected);


        addCommonActions(commonActions);

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

    private void addCommonActions(List commonActions) {
        for (int i = 0; i < commonActions.size(); i++) {
            SCYMapperAction scyMapperAction = (SCYMapperAction) commonActions.get(i);
            JButton button = new JButton(scyMapperAction);
            button.revalidate();
            add(button);
        }
    }


}
