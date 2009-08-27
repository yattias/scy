package eu.scy.client.tools.fxscydynamics.registration;

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
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
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
public class EloModelWrapper {

    private static final Logger logger = Logger.getLogger(EloModelWrapper.class.getName());
    public static final String untitledDocName = "untitled";
    public static final String scyDatasetType = "scy/dataset";
    public static final String scyModelType = "scy/model";
    private IRepository repository;
    private IMetadataTypeManager metadataTypeManager;
    private IELOFactory eloFactory;
    private IMetadataKey uriKey;
    private IMetadataKey titleKey;
    private IMetadataKey typeKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey missionKey;
    private IMetadataKey usesRelationKey;
    private IMetadataKey authorKey;
    private JDomStringConversion jdomStringConversion = new JDomStringConversion();
    private String docName = untitledDocName;
    private IELO eloDataSet = null;
    private IELO eloModel = null;
    private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();
    private IMetadataKey identifierKey;
    private IMetadataKey technicalFormatKey;

    public EloModelWrapper(ModelEditor editor) {}

    public void addELOLoadedChangedListener(ELOLoadedChangedListener eloLoadedChangedListener) {
        if (!eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.add(eloLoadedChangedListener);
        }
    }

    public void removeELOLoadedChangedListener(ELOLoadedChangedListener eloLoadedChangedListener) {
        if (eloLoadedChangedListeners.contains(eloLoadedChangedListener)) {
            eloLoadedChangedListeners.remove(eloLoadedChangedListener);
        }
    }

    private void sendELOLoadedChangedListener() {
        ELOLoadedChangedEvent eloLoadedChangedEvent = new ELOLoadedChangedEvent(this, eloDataSet);
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
        String windowTitle = "Model: ";
        if (StringUtils.hasText(docName)) {
            windowTitle += docName;
        }
    // setTitle(windowTitle);
    }

    public String getDocName() {
        return docName;
    }

    public void newAction() {
        // TODO
        //editor.newModel();
        eloModel = null;
        docName = untitledDocName;
        sendELOLoadedChangedListener();
    }

    public void loadModelAction() {
        IQuery query = null;
        IMetadataQuery metadataQuery = new BasicMetadataQuery(typeKey,BasicSearchOperations.EQUALS, scyModelType, null);
        query = metadataQuery;
        List<ISearchResult> searchResults = repository.search(query);
        URI[] simconfigUris = new URI[searchResults.size()];
        int i = 0;
        for (ISearchResult searchResult : searchResults) {
            simconfigUris[i++] = searchResult.getUri();
        }
        URI simconfigUri = (URI) JOptionPane.showInputDialog(null, "Select model", "Select model",
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
            if (!scyModelType.equals(eloType)) {
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            }
            IMetadata metadata = newElo.getMetadata();
            IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
            // TODO fixe the locale problem!!!
            Object titleObject = metadataValueContainer.getValue();
            Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
            Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

            setDocName(titleObject3.toString());
            //TODO
            //editor.loadModel(newElo.getContent().getXmlString());
            eloModel = newElo;
            sendELOLoadedChangedListener();
        }
    }

    public void saveDatasetAction() {
        logger.fine("save dataset");
        if (eloDataSet == null) {
            saveAsDatasetAction();
        } else {
            //TODO
            //eloDataSet.getContent().setXmlString(jdomStringConversion.xmlToString(editor.getDataSet().toXML()));
            IMetadata resultMetadata = repository.updateELO(eloDataSet);
            eloFactory.updateELOWithResult(eloDataSet, resultMetadata);
        }
    }

    public void saveAsDatasetAction() {
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
                if (eloModel!=null) {
                    eloDataSet.getMetadata().getMetadataValueContainer(usesRelationKey).setValue(eloModel.getUri());
                }
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "failed to create uri", e);
            }
            IContent content = eloFactory.createContent();
            //TODO
            //content.setXmlString(jdomStringConversion.xmlToString(editor.getDataSet().toXML()));
            eloDataSet.setContent(content);
            IMetadata resultMetadata = repository.addNewELO(eloDataSet);
            eloFactory.updateELOWithResult(eloDataSet, resultMetadata);
            // updateEloWithNewMetadata(elo, eloMetadata);
            // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
            sendELOLoadedChangedListener();
        }
    }

    public void saveModelAction() {
        logger.fine("save model");
        if (eloModel == null) {
            saveAsModelAction();
        } else {
            //TODO
            //eloModel.getContent().setXmlString(jdomStringConversion.xmlToString(editor.getModel().toXML()));
            IMetadata resultMetadata = repository.updateELO(eloModel);
            eloFactory.updateELOWithResult(eloModel, resultMetadata);
        }
    }

    public void saveAsModelAction() {
        logger.fine("save as model");
        String datasetName = JOptionPane.showInputDialog("Enter model name:", docName);
        if (StringUtils.hasText(datasetName)) {
            setDocName(datasetName);
            eloModel = eloFactory.createELO();
            eloModel.setDefaultLanguage(Locale.ENGLISH);
            eloModel.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
            eloModel.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
            eloModel.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/model");
            eloModel.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
                    new Long(System.currentTimeMillis()));
            try {
                eloModel.getMetadata().getMetadataValueContainer(missionKey).setValue(
                        new URI("roolo://somewhere/myMission.mission"));
                eloModel.getMetadata().getMetadataValueContainer(authorKey).setValue(
                        new Contribute("my vcard", System.currentTimeMillis()));
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "failed to create uri", e);
            }
            IContent content = eloFactory.createContent();
            //TODO
            //content.setXmlString(jdomStringConversion.xmlToString(editor.getModel().toXML()));
            eloModel.setContent(content);
            IMetadata resultMetadata = repository.addNewELO(eloModel);
            eloFactory.updateELOWithResult(eloModel, resultMetadata);
            sendELOLoadedChangedListener();
        }
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }
}
