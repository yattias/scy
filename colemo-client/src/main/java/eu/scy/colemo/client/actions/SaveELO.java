package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.apr.2009
 * Time: 20:47:50
 * To change this template use File | Settings | File Templates.
 */
public class SaveELO extends SCYMapperAction{

    public SaveELO() {
        setTitle("Save");
    }

    protected void performAction(ActionEvent e) {
        log.info("SAVING ELO");

        ApplicationController.getDefaultInstance().saveELO();
    }
}
