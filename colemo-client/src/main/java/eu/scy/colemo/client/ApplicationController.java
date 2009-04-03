package eu.scy.colemo.client;

import javax.swing.*;

import eu.scy.colemo.client.scyconnectionhandler.SCYConnectionHandler;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import java.util.Iterator;
import java.util.Locale;
import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.api.*;
import roolo.elo.metadata.keys.Contribute;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:29:12
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationController {

    private static final Logger log = Logger.getLogger(ApplicationController.class.getName());

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

    public static final String SCYMAPPER_TYPE = "scy/scymapping";


    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }


    public static ApplicationController getDefaultInstance() {
        if (defaultInstance == null) defaultInstance = new ApplicationController();
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
        String name = JOptionPane.showInputDialog(this, "Please type name of ELO :");
        if (name != null) {
            if (getEloFactory() != null) {
                IELO elo = getEloFactory().createELO();
                elo.setDefaultLanguage(Locale.ENGLISH);
                elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId())).setValue(new Long(System.currentTimeMillis()));


                try {
                    elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.TITLE.getId())).setValue(name);
                    elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.MISSION.getId())).setValue(new URI("roolo://somewhere/myMission.mission"));
                    elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.AUTHOR.getId())).setValue(new Contribute("my vcard", System.currentTimeMillis()));
                    elo.getMetadata().getMetadataValueContainer(metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId())).setValue("scy/scymapping");
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
    }

    public void loadElo(URI eloUri) {
        IMetadataKey typeKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());
        log.info("Trying to load elo " + eloUri);
        IELO<IMetadataKey> newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            String eloType = newElo.getMetadata().getMetadataValueContainer(typeKey).getValue().toString();
            if (!SCYMAPPER_TYPE.equals(eloType))
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            IMetadata metadata = newElo.getMetadata();
            String xml = newElo.getContent().getXmlString();

            List concepts = ConceptMapLoader.getDefaultInstance().parseConcepts(xml);
            List links = ConceptMapLoader.getDefaultInstance().parseLinks(xml);

            getGraphicsDiagram().clearAll();

            for (int i = 0; i < concepts.size(); i++) {
                UmlClass umlClass = (UmlClass) concepts.get(i);
                getGraphicsDiagram().addClass(umlClass);
            }

            for (int i = 0; i < links.size(); i++) {
                UmlLink umlLink = (UmlLink) links.get(i);
                getGraphicsDiagram().addLink(umlLink);
            }


            log.info("XML:" + xml);
            /*IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
            // TODO fixe the locale problem!!!
            Object titleObject = metadataValueContainer.getValue();
            Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
            Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);
            */
            /*
            setDocName(titleObject3.toString());
            whiteboardPanel.deleteAllWhiteboardContainers();
            whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent()
                    .getXmlString()));
            elo = newElo;
            sendELOLoadedChangedListener();
            */
        }

    }

    public void load() {
        if (metadataTypeManager != null) {
            IMetadataKey typeKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());

            IQuery query = null;
            IMetadataQuery<IMetadataKey> metadataQuery = new BasicMetadataQuery<IMetadataKey>(typeKey,
                    BasicSearchOperations.EQUALS, SCYMAPPER_TYPE, null);
            query = metadataQuery;
            List<ISearchResult> searchResults = repository.search(query);
            URI[] drawingUris = new URI[searchResults.size()];
            int i = 0;
            for (ISearchResult searchResult : searchResults)
                drawingUris[i++] = searchResult.getUri();
            URI drawingUri = (URI) JOptionPane.showInputDialog(null, "Select concept map", "Select concept map",
                    JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
            if (drawingUri != null) {
                loadElo(drawingUri);
            }
        } else {
            log.info("LOAD ELO FROM DISK!");
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
