/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxscymapper.registration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.impl.DiagramModel;
import eu.scy.scymapper.impl.model.DefaultConceptMap;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQueryComponent;
import roolo.search.ISearchResult;
import roolo.search.SearchOperation;
import roolo.elo.api.*;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class ScyMapperRepositoryWrapper {

    private static final Logger logger = Logger.getLogger(ScyMapperRepositoryWrapper.class.getName());
    public static final String scyMapperType = "scy/mapping";
    public static final String untitledDocName = "Untitled";
    private IRepository repository;
    private IMetadataTypeManager metadataTypeManager;
    private IELOFactory eloFactory;
    private IMetadataKey identifierKey;
    private IMetadataKey titleKey;
    private IMetadataKey technicalFormatKey;
    private IMetadataKey dateCreatedKey;
    //	private IMetadataKey missionKey;
    private IMetadataKey authorKey;

    public ScyMapperRepositoryWrapper() {
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

    public void setDocName(String docName) {
        String windowTitle = "Concept map: ";
        if (StringUtils.hasText(docName)) {
            windowTitle += docName;
        }
        // setTitle(windowTitle);
    }

    public URI[] findElos() {
        IQueryComponent metadataQueryComponent = new MetadataQueryComponent(technicalFormatKey,SearchOperation.EQUALS,scyMapperType);
        IQuery metadataQuery = new Query(metadataQueryComponent);
        List<ISearchResult> searchResults = repository.search(metadataQuery);
        URI[] uris = new URI[searchResults.size()];
        int i = 0;
        for (ISearchResult searchResult : searchResults) {
            uris[i++] = searchResult.getUri();
        }
        return uris;
    }

    public IELO loadELO(URI eloUri) {
        logger.info("Trying to load elo " + eloUri);
        IELO foundElo = repository.retrieveELO(eloUri);
        if (foundElo != null) {
            String eloType = foundElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
            if (!scyMapperType.equals(eloType)) {
                throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
            }

            IMetadata metadata = foundElo.getMetadata();
            IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
            // TODO fixe the locale problem!!!
            Object titleObject = metadataValueContainer.getValue(Locale.getDefault());
            if (titleObject == null) {
                titleObject = metadataValueContainer.getValue();
            }

            setDocName(titleObject.toString());
            return foundElo;
        }
        return null;
    }

    public IConceptMap getELOConceptMap(IELO elo) {
        logger.info("Decoding concept map from elo content " + elo.getUri());

        XStream xstream = new XStream(new DomDriver());
        IConceptMap cmap = (IConceptMap) xstream.fromXML(elo.getContent().getXmlString());
        return cmap;
    }

    public void setELOConceptMap(IELO elo, IConceptMap cmap) {
        logger.info("Encoding concept map as elo content " + elo.getUri());
        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(cmap);
        elo.getContent().setXmlString(xml);
    }

    public IELO updateELO(IELO elo) {
        if (elo.getUri() == null) {
            return saveELO(elo);
        }


        // TODO: This should be cached
        IConceptMap conceptMap = getELOConceptMap(elo);
        elo.setDefaultLanguage(Locale.ENGLISH);
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(conceptMap.getName());
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(conceptMap.getName(), Locale.CANADA);
        IMetadata resultMetadata = repository.updateELO(elo);

        eloFactory.updateELOWithResult(elo, resultMetadata);
        return elo;
    }

    public IELO saveELO(IELO elo) {
        logger.fine("saving new concept map as elo");

        // TODO: This should be cached
        IConceptMap conceptMap = getELOConceptMap(elo);
        elo.setDefaultLanguage(Locale.ENGLISH);
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(conceptMap.getName());
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(conceptMap.getName(), Locale.CANADA);
        elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyMapperType);
        elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Long(System.currentTimeMillis()));

        elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute("my vcard", System.currentTimeMillis()));

        IMetadata resultMetadata = repository.addNewELO(elo);
        eloFactory.updateELOWithResult(elo, resultMetadata);

        return elo;

    }

    public IELO createELO() {
        logger.fine("creating new concept map elo");

        IELO elo = eloFactory.createELO();

        IContent content = eloFactory.createContent();

        IDiagramModel diagram = new DiagramModel();
        IConceptMap conceptMap = new DefaultConceptMap(untitledDocName, diagram);

        elo.setDefaultLanguage(Locale.ENGLISH);
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(conceptMap.getName());
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(conceptMap.getName(), Locale.CANADA);
        elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyMapperType);
        elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(new Long(System.currentTimeMillis()));
        elo.getMetadata().getMetadataValueContainer(authorKey).setValue(new Contribute("my vcard", System.currentTimeMillis()));

        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(conceptMap);
        content.setXmlString(xml);
        elo.setContent(content);

        return elo;

    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public IRepository getRepository() {
        return repository;
    }

    public void setEloFactory(IELOFactory eloFactory) {
        this.eloFactory = eloFactory;
    }
}
