package eu.scy.colemo.client;

import eu.scy.colemo.client.actions.AddConceptAction;

import javax.swing.*;
import java.util.logging.Logger;
import java.util.*;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:01:08
 * To change this template use File | Settings | File Templates.
 */
public class PopupMenuController {

    private static PopupMenuController defaultInstance = null;
    private Logger log = Logger.getLogger("PopupMenuController.class");

    public static PopupMenuController getDefaultinstance() {
        if(defaultInstance == null) defaultInstance = new PopupMenuController();
        return defaultInstance;
    }

    private PopupMenuController() {

    }

    public void showPopupDialog(Object userObject, int x, int y, MouseEvent event) {
        Component caller = (Component) event.getSource();
        Point point = event.getPoint();
        SwingUtilities.convertPointToScreen(point, caller);
        log.info("Showing popup for: " + userObject + " at x: " +x + " and y: " + y + " caller: " + caller.getClass().getName());
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLocation(point);//caller.getX() + x, caller.getY() + y);

        List<Action> actions = ActionController.getDefaultInstance().getActions(userObject);
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);
            JMenuItem item = new JMenuItem(action);
            popupMenu.add(item);
        }
        
        popupMenu.show(ApplicationController.getDefaultInstance().getGraphicsDiagram(), (int)point.getX(), (int)point.getY());
    }


    

}
