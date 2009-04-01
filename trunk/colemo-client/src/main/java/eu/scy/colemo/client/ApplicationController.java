package eu.scy.colemo.client;

import javax.swing.JApplet;

import eu.scy.colemo.client.scyconnectionhandler.SCYConnectionHandler;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import java.util.Iterator;
import java.util.Locale;
import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.api.*;
import roolo.elo.metadata.keys.Contribute;
import roolo.api.IRepository;

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

    private IELOFactory eloFactory;
    private IMetadataTypeManager metadataTypeManager;
    private IRepository repository;

    public static String TOOL_NAME = "SCYMapper"; 


    


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
        if(getEloFactory() != null) {
            IELO elo = getEloFactory().createELO();
            elo.setDefaultLanguage(Locale.ENGLISH);
            elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId())).setValue(new Long(System.currentTimeMillis()));


            try {
                elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.MISSION.getId())).setValue(new URI("roolo://somewhere/myMission.mission"));
                elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.AUTHOR.getId())).setValue(new Contribute("my vcard", System.currentTimeMillis()));
                elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId())).setValue("scy/conceptMap");
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            IContent content = eloFactory.createContent();
            content.setXmlString(ConceptMapExporter.getDefaultInstance().createXML());
            //content.
            elo.setContent(content);
            IMetadata<IMetadataKey> resultMetadata = getRepository().addELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);

        }
        
    }

    public IELOFactory getEloFactory() {
        return eloFactory;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    public IMetadataTypeManager getMetadataTypeManager() {
        return metadataTypeManager;
    }

    public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
        this.metadataTypeManager = metadataTypeManager;
    }

    public IRepository getRepository() {
        return repository;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }
}
