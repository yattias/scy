package eu.scy.colemo.client.actions;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:18:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseAction extends SCYMapperAction {

    public BaseAction(String name) {
        setTitle(name);
    }


    public abstract Class getOperateson();

}
