/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxvideo;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
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
 * @author pg
 */
public class EloVideoActionWrapper {
    private static final Logger logger = Logger.getLogger(EloVideoActionWrapper.class.getName());
    public static final String scyVideoType = "scy/video";
    public static final String untitledDocName = "untitled video";
    private IRepository repository;
    private IMetadataTypeManager metadataTypeManager;
    private IELOFactory eloFactory;
    private IMetadataKey identifierKey;
    private IMetadataKey titleKey;
    private IMetadataKey technicalFormatKey;
    private IMetadataKey dateCreatedKey;
//      private IMetadataKey missionKey;
    private IMetadataKey authorKey;
    private JDomStringConversion jdomStringConversion = new JDomStringConversion();
    private File lastUsedFile = null;
    private String docName = untitledDocName;
    private IELO elo = null;
    private ILoadXML target;
    public EloVideoActionWrapper(ILoadXML target) {
        this.target = target;
    }


    public IRepository getRepository()
    {
        return repository;
    }

    public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
    {
        this.metadataTypeManager = metadataTypeManager;
        identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
        logger.info("retrieved key " + identifierKey.getId());
        titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
        logger.info("retrieved key " + titleKey.getId());
        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
        logger.info("retrieved key " + technicalFormatKey.getId());
        dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
        logger.info("retrieved key " + dateCreatedKey.getId());
//              missionKey = metadataTypeManager.getMetadataKey("mission");
//              logger.info("retrieved key " + missionKey.getId());
        authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
        logger.info("retrieved key " + authorKey.getId());
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    public URI getEloUri() {
        if(elo == null) {
            return null;
        }
        else {
            return elo.getUri();
        }
    }

    public void setDocName(String docName)
    {
        this.docName = docName;
        String windowTitle = "videoRessource: ";
        if (StringUtils.hasText(docName))
        {
            windowTitle += docName;
        }
        // setTitle(windowTitle);
    }

    public String getDocName() {
        return docName;
    }

    public void newVideoAction() {
        //VideoPanel.newElo();
        elo = null;
        docName = untitledDocName;
        //sendEloLoadedChangedListener();
    }

    public void loadVideoAction()
    {
        //// System.out.println("loadVideoAction();");
        //// System.out.println(technicalFormatKey);
        //// System.out.println(scyVideoType);
        IQuery query = null;
        IQueryComponent metadataQuery = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, scyVideoType);
        query = new Query(metadataQuery);
        List<ISearchResult> searchResults = repository.search(query);
        URI[] drawingUris = new URI[searchResults.size()];
        int i = 0;
        for (ISearchResult searchResult : searchResults)
        {
            drawingUris[i++] = searchResult.getUri();
        }
        URI videoUri = (URI) JOptionPane.showInputDialog(null, "Select piece of Information", "Select piece of Information", JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
        if (videoUri != null)
        {
            //// System.out.println(videoUri);
            loadElo(videoUri);
        }
        else {
            // System.out.println("you should never read this.");
        }
    }


    public void loadElo(URI eloUri) {
        logger.info("Trying to load elo " + eloUri);
        IELO newElo = repository.retrieveELO(eloUri);
        if (newElo != null)
        {
            String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
            if (!scyVideoType.equals(eloType) ) {
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            }
            IMetadata metadata = newElo.getMetadata();
            IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
            // TODO fixe the locale problem!!!
            Object titleObject = metadataValueContainer.getValue();
            Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
            Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);
            setDocName(titleObject3.toString());
            //this.VideoPanel.loadElo(newElo.getContent().getXmlString());
            //// System.out.println("loading..");
            //// System.out.println(newElo.getContent().getXmlString());
            this.target.loadXML(newElo.getContent().getXmlString());
            elo = newElo;
            //sendEloLoadedChangedListener();
        }
        else {
            // System.out.println("loadElo() - you should never read this. oops.");
        }
    }

    public void saveVideoAction() {
        logger.fine("save Video");
        if (elo == null)
        {
            saveAsVideoAction();
        }
        else
        {
            IMetadata resultMetadata = repository.updateELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
        }
    }

    public void saveAsVideoAction() {
        logger.fine("save as VideoREZZOURZZEE");
        String videoName = JOptionPane.showInputDialog("Enter Video name:", docName);
        if (StringUtils.hasText(videoName)) {
            setDocName(videoName);
            elo = eloFactory.createELO();
            elo.setDefaultLanguage(Locale.ENGLISH);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.GERMANY);
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue("scy/text");
            elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
            new Long(System.currentTimeMillis()));
            elo.getMetadata().getMetadataValueContainer(authorKey).setValue(
            new Contribute("my vcard", System.currentTimeMillis()));
            IContent content = eloFactory.createContent();
            elo.setContent(content);
            IMetadata resultMetadata = repository.addNewELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
            //sendEloLoadedChangedListener();
        }
    }


    public void setRepository(IRepository repository) {
        this.repository = repository;
    }


}
