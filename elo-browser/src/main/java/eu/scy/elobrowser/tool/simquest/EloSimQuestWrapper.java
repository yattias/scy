/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.tool.simquest;

import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.elobrowser.tool.simquest.DataCollector;

/**
 *
 * @author sikkenj
 */
public class EloSimQuestWrapper {

    private static final Logger logger = Logger.getLogger(EloSimQuestWrapper.class.getName());
    public static final String untitledDocName = "untitled";
    private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
    private IMetadataTypeManager<IMetadataKey> metadataTypeManager;
    private IELOFactory<IMetadataKey> eloFactory;
    private IMetadataKey uriKey;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey missionKey;
    private IMetadataKey authorKey;
    private JDomStringConversion jdomStringConversion = new JDomStringConversion();
    private DataCollector dataCollector;
    private String docName = untitledDocName;
    private IELO<IMetadataKey> elo = null;
    private CopyOnWriteArrayList<ELOLoadedChangedListener<IMetadataKey>> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener<IMetadataKey>>();

    public EloSimQuestWrapper(DataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    public void addELOLoadedChangedListener(
            ELOLoadedChangedListener<IMetadataKey> eloLoadedChangedListener) {
        if (!eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.add(eloLoadedChangedListener);
        }
    }

    public void removeELOLoadedChangedListener(
            ELOLoadedChangedListener<IMetadataKey> eloLoadedChangedListener) {
        if (eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.remove(eloLoadedChangedListener);
        }
    }

    private void sendELOLoadedChangedListener() {
        ELOLoadedChangedEvent<IMetadataKey> eloLoadedChangedEvent = new ELOLoadedChangedEvent<IMetadataKey>(
                this, elo);
        for (ELOLoadedChangedListener<IMetadataKey> eloLoadedChangedListener : eloLoadedChangedListeners) {
            eloLoadedChangedListener.eloLoadedChanged(eloLoadedChangedEvent);
        }
    }

    public IRepository<IELO<IMetadataKey>, IMetadataKey> getRepository() {
        return repository;
    }

    public void setMetadataTypeManager(IMetadataTypeManager<IMetadataKey> metadataTypeManager) {
        this.metadataTypeManager = metadataTypeManager;
        uriKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.URI.getId());
        logger.info("retrieved key " + uriKey.getId());
        titleKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TITLE.getId());
        logger.info("retrieved key " + titleKey.getId());
        typeKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());
        logger.info("retrieved key " + typeKey.getId());
        dateCreatedKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId());
        logger.info("retrieved key " + dateCreatedKey.getId());
        missionKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.MISSION.getId());
        logger.info("retrieved key " + missionKey.getId());
        authorKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.AUTHOR.getId());
        logger.info("retrieved key " + authorKey.getId());
    }

    public void setEloFactory(IELOFactory<IMetadataKey> eloFactory) {
        this.eloFactory = eloFactory;
    }

    public URI getEloUri() {
        if (elo == null) {
            return null;
        } else {
            return elo.getUri();
        }
    }

    public String getELOTitle() {
        if (elo == null) {
            return null;
        } else {
            return (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH);
        }
    }

    private void setDocName(String docName) {
        this.docName = docName;
        String windowTitle = "Dataset: ";
        if (StringUtils.hasText(docName)) {
            windowTitle += docName;
        }
    // setTitle(windowTitle);
    }

    public String getDocName() {
        return docName;
    }

    public void newDrawingAction() {
        //TODO: adapt to simquest
        //whiteboardPanel.deleteAllWhiteboardContainers();
        dataCollector.newELO();
        elo = null;
        docName = untitledDocName;
        sendELOLoadedChangedListener();
    }

    public void loadDrawingAction() {
        //IQuery query = null;
        //IMetadataQuery<IMetadataKey> metadataQuery = new BasicMetadataQuery<IMetadataKey>(typeKey,
        //        BasicSearchOperations.EQUALS, scyDrawType, null);
        //query = metadataQuery;
        //List<ISearchResult> searchResults = repository.search(query);
        //URI[] drawingUris = new URI[searchResults.size()];
        //int i = 0;
        //for (ISearchResult searchResult : searchResults) {
        //    drawingUris[i++] = searchResult.getUri();
        //}
        //URI drawingUri = (URI) JOptionPane.showInputDialog(null, "Select drawing", "Select drawing",
        //        JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
        //if (drawingUri != null) {
        //    loadElo(drawingUri);
        //}
    }

    public void loadElo(URI eloUri) {
        //logger.info("Trying to load elo " + eloUri);
        //IELO<IMetadataKey> newElo = repository.retrieveELO(eloUri);
        //if (newElo != null) {
        //    String eloType = newElo.getMetadata().getMetadataValueContainer(typeKey).getValue().toString();
        //    if (!scyDrawType.equals(eloType)) {
        //        throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
        //    }
        //    IMetadata metadata = newElo.getMetadata();
        //    IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
            // TODO fixe the locale problem!!!
        //    Object titleObject = metadataValueContainer.getValue();
        //    Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
        //    Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

        //    setDocName(titleObject3.toString());
            //TODO: adapt to simquest
            //whiteboardPanel.deleteAllWhiteboardContainers();
            //whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent()
            //			.getXml()));
       //     elo = newElo;
       //    sendELOLoadedChangedListener();
        //}
    }

    public void saveDrawingAction() {
        logger.fine("save dataset");
        if (elo == null) {
            saveAsDrawingAction();
        } else {
            elo.getContent().setXml(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
            IMetadata<IMetadataKey> resultMetadata = repository.updateELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
        }
    }

    public void saveAsDrawingAction() {
        logger.fine("save as dataset");
        String drawingName = JOptionPane.showInputDialog("Enter dataset name:", docName);
        if (StringUtils.hasText(drawingName)) {
            setDocName(drawingName);
            elo = eloFactory.createELO();
            elo.setDefaultLanguage(Locale.ENGLISH);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
            elo.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/dataset");
            elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
                    new Long(System.currentTimeMillis()));
            try {
                elo.getMetadata().getMetadataValueContainer(missionKey).setValue(
                        new URI("roolo://somewhere/myMission.mission"));
                elo.getMetadata().getMetadataValueContainer(authorKey).setValue(
                        new Contribute("my vcard", System.currentTimeMillis()));
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "failed to create uri", e);
            }
            IContent content = eloFactory.createContent();
            content.setXml(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
            elo.setContent(content);
            IMetadata<IMetadataKey> resultMetadata = repository.addELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
            // updateEloWithNewMetadata(elo, eloMetadata);
            // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
            sendELOLoadedChangedListener();
        }
    }

    // @Action
    // public void closeDrawingAction()
    // {
    // this.dispose();
    // }
    //
    public void setRepository(IRepository<IELO<IMetadataKey>, IMetadataKey> repository) {
        this.repository = repository;
    }

}
