package eu.scy.colemo.client;

import eu.scy.colemo.network.Client;
import eu.scy.colemo.client.groups.ConnectionHandlerJGroups;
import eu.scy.colemo.client.sqlspacesimpl.ConnectionHandlerSqlSpaces;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:29:12
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationController {

    private static ApplicationController defaultInstance;

    private GraphicsDiagram graphicsDiagram;
    private MainFrame mainFrame;

    private ConnectionHandler connectionHandler = null;
    private JApplet applet;


    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    
    public static ApplicationController getDefaultInstance() {
        if(defaultInstance == null) defaultInstance = new ApplicationController();
        return defaultInstance;
    }



    private ApplicationController() {

    }

    public JApplet getApplet() {
        return applet;
    }

    public void setApplet(JApplet applet) {
        this.applet = applet;
    }

    public GraphicsDiagram getGraphicsDiagram() {
        return graphicsDiagram;
    }

    public void setGraphicsDiagram(GraphicsDiagram graphicsDiagram) {
        this.graphicsDiagram = graphicsDiagram;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public Client getClient() {
        return getMainFrame().getClient();
    }

    public void connect() {
        connectionHandler = new ConnectionHandlerSqlSpaces();
        //connectionHandler = new ConnectionHandlerJGroups();
        try {
            connectionHandler.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionHandler.sendMessage("Henrik ROCKS!");

    }
}
