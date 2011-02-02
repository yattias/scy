/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxwebresourcer;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Locale;
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
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

/**
 *
 * @author pg
 */
public class EloWebResourceActionWrapper {
    private static final Logger logger = Logger.getLogger(EloWebResourceActionWrapper.class.getName());
    public static final String scyWebType = "scy/webresourcer";
    public static final String untitledDocName = "untitled webresource";
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
    private WebResourcePanel webPanel;
    private File lastUsedFile = null;
    private String docName = untitledDocName;
    private IELO elo = null;
    private ILoadXML target;
    public EloWebResourceActionWrapper(ILoadXML target) {
        this.target = target;
    }



    /*
    public EloWebRessourceActionWrapper(WebRessourcePanel webPanel) {
        this.webPanel = webPanel;
    }*/
    
/*
    public void addEloLoadedChangedListener(ELOLoadedChangedListener eloLoadedChangedListener)
    {
        if (!eloLoadedChangedListeners.contains(eloLoadedChangedListener))
        {
            eloLoadedChangedListeners.add(eloLoadedChangedListener);
        }
    }

    public void removeEloLoadedChangedListener(ELOLoadedChangedListener eloLoadedChangedListener)
    {
        if (eloLoadedChangedListeners.contains(eloLoadedChangedListener))
        {
            eloLoadedChangedListeners.remove(eloLoadedChangedListener);
        }
    }

    private void sendEloLoadedChangedListener()
    {
        ELOLoadedChangedEvent eloLoadedChangedEvent = new ELOLoadedChangedEvent(this, elo);
        for (ELOLoadedChangedListener eloLoadedChangedListener : eloLoadedChangedListeners)
        {
            eloLoadedChangedListener.eloLoadedChanged(eloLoadedChangedEvent);
        }
    }
*/
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
        String windowTitle = "WebRessource: ";
        if (StringUtils.hasText(docName))
        {
            windowTitle += docName;
        }
        // setTitle(windowTitle);
    }

    public String getDocName() {
        return docName;
    }

    public void newWebAction() {
        //webPanel.newElo();
        elo = null;
        docName = untitledDocName;
        //sendEloLoadedChangedListener();
    }

    public void loadWebAction()
    {
        //System.out.println("loadWebAction();");
        //System.out.println(technicalFormatKey);
        //System.out.println(scyWebType);
        IQuery query = null;
        IQueryComponent metadataQuery = new MetadataQueryComponent(technicalFormatKey, SearchOperation.EQUALS, scyWebType);
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
            if (!scyWebType.equals(eloType) ) {
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

    public void saveWebAction() {
        logger.fine("save web");
        if (elo == null)
        {
            saveAsWebAction();
        }
        else
        {
            elo.getContent().setXmlString(target.getXML());
            IMetadata resultMetadata = repository.updateELO(elo);
            eloFactory.updateELOWithResult(elo, resultMetadata);
        }
    }

    public void saveAsWebAction() {
        logger.fine("save as webresource");
        String webName = JOptionPane.showInputDialog("Enter WebResouceR name:", docName);
        if (StringUtils.hasText(webName)) {
            setDocName(webName);
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
            content.setXmlString(target.getXML());
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
