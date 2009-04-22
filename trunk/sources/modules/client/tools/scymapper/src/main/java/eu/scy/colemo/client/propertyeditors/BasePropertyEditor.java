package eu.scy.colemo.client.propertyeditors;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2009
 * Time: 10:37:00
 * To change this template use File | Settings | File Templates.
 */
public class BasePropertyEditor extends JPanel {

    private Object model = null;

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
