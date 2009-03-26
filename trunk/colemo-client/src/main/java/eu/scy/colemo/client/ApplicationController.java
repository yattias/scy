package eu.scy.colemo.client;

import javax.swing.JApplet;

import eu.scy.colemo.client.scyconnectionhandler.SCYConnectionHandler;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:29:12
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationController {

    private static ApplicationController defaultInstance;

    private ToolBrokerAPI toolBrokerAPI;

    private GraphicsDiagram graphicsDiagram;

    private ColemoPanel colemoPanel = null;

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

    public ToolBrokerAPI getToolBrokerAPI() {
        return toolBrokerAPI;
    }

    public void setToolBrokerAPI(ToolBrokerAPI toolBrokerAPI) {
        this.toolBrokerAPI = toolBrokerAPI;
    }

    public void connect() {
        //connectionHandler = new ConnectionHandlerSqlSpaces();
        connectionHandler = new SCYConnectionHandler();
        try {
            connectionHandler.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ColemoPanel getColemoPanel() {
        return colemoPanel;
    }

    public void setColemoPanel(ColemoPanel colemoPanel) {
        this.colemoPanel = colemoPanel;
    }

    public void saveELO() {
        ConceptMapExporter.getDefaultInstance().createXML();
        
    }
}
