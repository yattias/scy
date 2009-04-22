package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.apr.2009
 * Time: 12:36:13
 * To change this template use File | Settings | File Templates.
 */
public class LoadEloAction extends SCYMapperAction{

    public LoadEloAction() {
        setTitle("Load ELO");
    }

    protected void performAction(ActionEvent e) {
        ApplicationController.getDefaultInstance().load();
    }
}
