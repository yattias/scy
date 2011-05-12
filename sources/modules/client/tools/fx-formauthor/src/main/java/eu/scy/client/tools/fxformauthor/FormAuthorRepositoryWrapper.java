/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;

import eu.scy.client.desktop.desktoputils.StringUtils;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import roolo.api.IRepository;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQueryComponent;
import roolo.search.SearchOperation;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

/**
 *
 * @author pg
 */
public class FormAuthorRepositoryWrapper {
    private static final Logger logger = Logger.getLogger(FormAuthorRepositoryWrapper.class.getName());
    public static final String scyFormAuthorType = "scy/formauthor";
    public static final String untitledDocName = "Untitled Form";

    private IRepository repository;
    private IMetadataTypeManager metadataTypeManager;
    private IELOFactory eloFactory;
    private IMetadataKey identifierKey;
    private IMetadataKey titleKey;
    private IMetadataKey technicalFormatKey;
    private IMetadataKey dateCreatedKey;
    private IMetadataKey authorKey;
    
    private String docName = untitledDocName;
    private ILoadXML target;
    private IELO elo = null;
    public FormAuthorRepositoryWrapper(ILoadXML target) {
        this.target = target;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }

    public void setDocName(String docName)
    {
        this.docName = docName;
        String windowTitle = "WebRessource: ";
        if (StringUtils.hasText(docName))
        {
            windowTitle += docName;
        }
        // setTitle(windowTitle);
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

    public void loadFormAction()
    {
        IQuery query = null;
        IQueryComponent metadataQuery = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, scyFormAuthorType);
        query = new Query(metadataQuery);
        List<ISearchResult> searchResults = repository.search(query);
        URI[] drawingUris = new URI[searchResults.size()];
        int i = 0;
        for (ISearchResult searchResult : searchResults)
        {
            drawingUris[i++] = searchResult.getUri();
        }
        URI webUri = (URI) JOptionPane.showInputDialog(null, "Select piece of Information", "Select piece of Information", JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
        if (webUri != null)
        {
            //System.out.println(webUri);
            loadElo(webUri);
        }
        else {
            //System.out.println("error. elo did not want to be loaded :(");
        }
    }

    public void loadElo(URI eloUri) {
        logger.info("Trying to load elo " + eloUri);
        IELO newElo = repository.retrieveELO(eloUri);
        if (newElo != null)
        {
            String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
            if (!scyFormAuthorType.equals(eloType) ) {
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            }
            IContent content = newElo.getContent();
            if(content != null) {
                //System.out.println("content loaded");
                String xml = content.getXmlString();
                if(xml != null) {
                   //t System.out.println("xml loaded.");
                   this.target.loadXML(xml);
                }
                else {
                    logger.warning("ERROR: could not load xml, xml == null");
                }
            }
            else {
                logger.warning("ERROR: could not load content, content == null");
            }
            //why is this line here!?
            //this.target.loadXML(newElo.getContent().getXmlString());
            elo = newElo;
        }
        else {
            logger.warning("ERROR: could not retrieve elo.");
        }
    }
    public void saveFormAction() {
        logger.fine("save formauthor");
        if (elo == null)
        {
            saveAsFormAction();
        }
        else
        {
            elo.getContent().setXmlString(target.getXML());
            IMetadata resultMetadata = repository.updateELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
        }
    }

    public void saveAsFormAction() {
        logger.fine("save as formauthor form");
        String webName = JOptionPane.showInputDialog("Enter FormAuthor form name:", docName);
        if (StringUtils.hasText(webName)) {
            setDocName(webName);
            elo = eloFactory.createELO();
            elo.setDefaultLanguage(Locale.ENGLISH);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.GERMANY);
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue("scy/formauthor");
            elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
            new Long(System.currentTimeMillis()));
            elo.getMetadata().getMetadataValueContainer(authorKey).setValue(
            new Contribute("my vcard", System.currentTimeMillis()));
            IContent content = eloFactory.createContent();
            content.setXmlString(target.getXML());
            elo.setContent(content);
            IMetadata resultMetadata = repository.addNewELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
            //sendEloLoadedChangedListener();
        }
        target.setTitle(webName);
    }

}
