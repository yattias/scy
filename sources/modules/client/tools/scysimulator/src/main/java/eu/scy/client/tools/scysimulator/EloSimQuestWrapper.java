package eu.scy.client.tools.scysimulator;

import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

/**
 *
 * @author Lars Bollen
 */
public class EloSimQuestWrapper {

    private static final Logger logger = Logger.getLogger(EloSimQuestWrapper.class.getName());
    public static final String untitledDocName = "untitled";
    public static final String scyDatasetType = "scy/dataset";
    public static final String scySimConfigType = "scy/simconfig";
    private IRepository repository;
    private IMetadataTypeManager metadataTypeManager;
    private IELOFactory eloFactory;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey missionKey;
    private IMetadataKey usesRelationKey;
    private IMetadataKey authorKey;
    private JDomStringConversion jdomStringConversion = new JDomStringConversion();
    private DataCollector dataCollector;
    private String docName = untitledDocName;
    private IELO eloDataSet = null;
    private IELO eloSimConfig = null;
    private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();
    private IMetadataKey identifierKey;
    private IMetadataKey technicalFormatKey;

    public EloSimQuestWrapper() {
    }

    public void setDataCollector(DataCollector dc) {
        this.dataCollector = dc;
    }

    public DataCollector getDataCollector() {
        return dataCollector;
    }

    public void addELOLoadedChangedListener(
            ELOLoadedChangedListener eloLoadedChangedListener) {
        if (!eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.add(eloLoadedChangedListener);
        }
    }

    public void removeELOLoadedChangedListener(
            ELOLoadedChangedListener eloLoadedChangedListener) {
        if (eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.remove(eloLoadedChangedListener);
        }
    }

    private void sendELOLoadedChangedListener() {
        ELOLoadedChangedEvent eloLoadedChangedEvent = new ELOLoadedChangedEvent(
                this, eloDataSet);
        for (ELOLoadedChangedListener eloLoadedChangedListener : eloLoadedChangedListeners) {
            eloLoadedChangedListener.eloLoadedChanged(eloLoadedChangedEvent);
        }
    }

    public IRepository getRepository() {
        return repository;
    }

    public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
      this.metadataTypeManager = metadataTypeManager;
      identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
      logger.info("retrieved key " + identifierKey.getId());
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
      logger.info("retrieved key " + titleKey.getId());
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
      logger.info("retrieved key " + technicalFormatKey.getId());
      dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
      logger.info("retrieved key " + dateCreatedKey.getId());
//		missionKey = metadataTypeManager.getMetadataKey("mission");
//		logger.info("retrieved key " + missionKey.getId());
      authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
      logger.info("retrieved key " + authorKey.getId());
   }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    public URI getEloUri() {
        if (eloDataSet == null) {
            return null;
        } else {
            return eloDataSet.getUri();
        }
    }

    public String getELOTitle() {
        if (eloDataSet == null) {
            return null;
        } else {
            return (String) eloDataSet.getMetadata().getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH);
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

    public void newAction() {
        dataCollector.newELO();
        eloDataSet = null;
        docName = untitledDocName;
        sendELOLoadedChangedListener();
    }

    public void loadSimConfigAction() {
        IQuery query = null;
        IMetadataQuery metadataQuery = new BasicMetadataQuery(typeKey,
                BasicSearchOperations.EQUALS, scySimConfigType, null);
        query = metadataQuery;
        List<ISearchResult> searchResults = repository.search(query);
        URI[] simconfigUris = new URI[searchResults.size()];
        int i = 0;
        for (ISearchResult searchResult : searchResults) {
            simconfigUris[i++] = searchResult.getUri();
        }
        URI simconfigUri = (URI) JOptionPane.showInputDialog(null, "Select simconfig", "Select simconfig",
                JOptionPane.QUESTION_MESSAGE, null, simconfigUris, null);
        if (simconfigUri != null) {
            loadElo(simconfigUri);
        }
    }

    public void loadElo(URI eloUri) {
        logger.info("Trying to load elo " + eloUri);
        IELO newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            String eloType = newElo.getMetadata().getMetadataValueContainer(typeKey).getValue().toString();
            if (!scySimConfigType.equals(eloType)) {
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            }
            IMetadata metadata = newElo.getMetadata();
            IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
            // TODO fixe the locale problem!!!
            Object titleObject = metadataValueContainer.getValue();
            Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
            Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

            setDocName(titleObject3.toString());
            //TODO: adapt to simquest
            //whiteboardPanel.deleteAllWhiteboardContainers();
            dataCollector.setSimConfig(newElo.getContent().getXmlString());
            //whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent()
            //			.getXml()));
            eloSimConfig = newElo;
            sendELOLoadedChangedListener();
        }
    }

    public void saveDataSetAction() {
        logger.fine("save dataset");
        if (eloDataSet == null) {
            saveAsDataSetAction();
        } else {
            eloDataSet.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
            IMetadata resultMetadata = repository.updateELO(eloDataSet);
            eloFactory.updateELOWithResult(eloDataSet, resultMetadata);
        }
    }

    public void saveAsDataSetAction() {
        logger.fine("save as dataset");
        String datasetName = JOptionPane.showInputDialog("Enter DataSet name:", docName);
        if (StringUtils.hasText(datasetName)) {
            setDocName(datasetName);
            eloDataSet = eloFactory.createELO();
            eloDataSet.setDefaultLanguage(Locale.ENGLISH);
            eloDataSet.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
            eloDataSet.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
            eloDataSet.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/dataset");
            eloDataSet.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
                    new Long(System.currentTimeMillis()));
            try {
                eloDataSet.getMetadata().getMetadataValueContainer(missionKey).setValue(
                        new URI("roolo://somewhere/myMission.mission"));
                eloDataSet.getMetadata().getMetadataValueContainer(authorKey).setValue(
                        new Contribute("my vcard", System.currentTimeMillis()));
                if (eloSimConfig!=null) {
                    eloDataSet.getMetadata().getMetadataValueContainer(usesRelationKey).setValue(eloSimConfig.getUri());
                }
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "failed to create uri", e);
            }

            

            IContent content = eloFactory.createContent();
            content.setXmlString(jdomStringConversion.xmlToString(dataCollector.getDataSet().toXML()));
            eloDataSet.setContent(content);
            IMetadata resultMetadata = repository.addNewELO(eloDataSet);
            eloFactory.updateELOWithResult(eloDataSet, resultMetadata);
            // updateEloWithNewMetadata(elo, eloMetadata);
            // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
            sendELOLoadedChangedListener();
        }
    }

    public void saveSimConfigAction() {
        logger.fine("save simconfig");
        if (eloSimConfig == null) {
            saveAsSimConfigAction();
        } else {
            eloSimConfig.getContent().setXmlString(jdomStringConversion.xmlToString(dataCollector.getSimConfig().toXML()));
            IMetadata resultMetadata = repository.updateELO(eloSimConfig);
            eloFactory.updateELOWithResult(eloSimConfig, resultMetadata);
        }
    }

    public void saveAsSimConfigAction() {
        logger.fine("save as simconfig");
        String datasetName = JOptionPane.showInputDialog("Enter SimConfig name:", docName);
        if (StringUtils.hasText(datasetName)) {
            setDocName(datasetName);
            eloSimConfig = eloFactory.createELO();
            eloSimConfig.setDefaultLanguage(Locale.ENGLISH);
            eloSimConfig.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
            eloSimConfig.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
            eloSimConfig.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/simconfig");
            eloSimConfig.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
                    new Long(System.currentTimeMillis()));
            try {
                eloSimConfig.getMetadata().getMetadataValueContainer(missionKey).setValue(
                        new URI("roolo://somewhere/myMission.mission"));
                eloSimConfig.getMetadata().getMetadataValueContainer(authorKey).setValue(
                        new Contribute("my vcard", System.currentTimeMillis()));
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "failed to create uri", e);
            }
            IContent content = eloFactory.createContent();
            content.setXmlString(jdomStringConversion.xmlToString(dataCollector.getSimConfig().toXML()));
            eloSimConfig.setContent(content);
            IMetadata resultMetadata = repository.addNewELO(eloSimConfig);
            eloFactory.updateELOWithResult(eloSimConfig, resultMetadata);
            // updateEloWithNewMetadata(elo, eloMetadata);
            // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
            sendELOLoadedChangedListener();
        }
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }
}
