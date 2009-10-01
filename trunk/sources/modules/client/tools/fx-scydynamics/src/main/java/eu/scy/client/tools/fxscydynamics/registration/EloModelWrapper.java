package eu.scy.client.tools.fxscydynamics.registration;

import colab.um.xml.model.JxmModel;
import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
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
    public static final String scyModelType = "scy/model";
    public static final String untitledDocName = "untitled";
    private IRepository repository;
    private IMetadataTypeManager metadataTypeManager;
    private IELOFactory eloFactory;
    private IMetadataKey identifierKey;
    private IMetadataKey titleKey;
    private IMetadataKey technicalFormatKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey authorKey;
    private JDomStringConversion jdomStringConversion = new JDomStringConversion();
    private String docName = untitledDocName;
    private IELO eloModel = null;
    private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();
    private final ModelEditor editor;

    public EloModelWrapper(ModelEditor editor) {
        this.editor = editor;
    }

    public void addELOLoadedChangedListener(ELOLoadedChangedListener eloLoadedChangedListener) {
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
                this, eloModel);
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
        authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        logger.info("retrieved key " + authorKey.getId());
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    public URI getEloUri() {
        if (eloModel == null) {
            return null;
        } else {
            return eloModel.getUri();
        }
    }

    public String getELOTitle() {
        if (eloModel == null) {
            return null;
        } else {
            return (String) eloModel.getMetadata().getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH);
        }
    }

    public void setDocName(String docName) {
        this.docName = docName;
        String windowTitle = "Model: ";
        if (StringUtils.hasText(docName)) {
            windowTitle += docName;
        }
    }

    public String getDocName() {
        return docName;
    }

    public void newAction() {
        editor.setNewModel();
        eloModel = null;
        docName = untitledDocName;
        sendELOLoadedChangedListener();
    }

    public void loadModelAction() {
        IQuery query = null;
        IMetadataQuery metadataQuery = new BasicMetadataQuery(technicalFormatKey,
                BasicSearchOperations.EQUALS, scyModelType, null);
        query = metadataQuery;
        List<ISearchResult> searchResults = repository.search(query);
        URI[] modelUris = new URI[searchResults.size()];
        int i = 0;
        for (ISearchResult searchResult : searchResults) {
            modelUris[i++] = searchResult.getUri();
        }
        URI modelUri = (URI) JOptionPane.showInputDialog(null, "Select model", "Select model",
                JOptionPane.QUESTION_MESSAGE, null, modelUris, null);
        if (modelUri != null) {
            loadModelElo(modelUri);
        }
    }

    public void loadModelElo(URI eloUri) {
        logger.info("Trying to load model elo " + eloUri);
        IELO newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
            if (!scyModelType.equals(eloType)) {
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            }
            IMetadata metadata = newElo.getMetadata();
            IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
            // TODO fixe the locale problem!!!
            Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);
            setDocName(titleObject3.toString());
            JxmModel xmlModel = xmlModel = JxmModel.readStringXML(newElo.getContent().getXmlString());
	    editor.setXmModel(xmlModel);
            eloModel = newElo;
            sendELOLoadedChangedListener();
        }
    }

    public void saveModelAction() {
        logger.fine("saveas model");
        if (eloModel == null) {
            saveAsModelAction();
        } else {
            eloModel.getContent().setXmlString(editor.getModelXML());
            IMetadata resultMetadata = repository.updateELO(eloModel);
            eloFactory.updateELOWithResult(eloModel, resultMetadata);
        }
    }

    public void saveAsModelAction() {
        logger.fine("save as model");
        String modelName = JOptionPane.showInputDialog("Enter model name:", docName);
        if (StringUtils.hasText(modelName)) {
            setDocName(modelName);
            eloModel = eloFactory.createELO();
            eloModel.setDefaultLanguage(Locale.ENGLISH);
            eloModel.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
            eloModel.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.ENGLISH);
            eloModel.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue("scy/model");
            eloModel.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Long(System.currentTimeMillis()));
            eloModel.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute("my vcard", System.currentTimeMillis()));
            eloModel.getContent().setXmlString(editor.getModelXML());
            IMetadata resultMetadata = repository.addNewELO(eloModel);
            eloFactory.updateELOWithResult(eloModel, resultMetadata);
            sendELOLoadedChangedListener();
        }
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public static Element findTag(Element root, String tag)
			throws IllegalArgumentException {
		if (root == null)
			throw new IllegalArgumentException("Element is null.");
		List enumChilds = root.getChildren();
		Iterator iter = enumChilds.iterator();
		while (iter.hasNext()) {
			Element childElement = (Element) iter.next();
			if (childElement.getName().equals(tag)) {
				return childElement;
			}
			try {
				Element foundElement = findTag(childElement, tag);
				if (foundElement != null)
					return foundElement;
			} catch (IllegalArgumentException e) {
			}
		}
		return null;
	}

}
