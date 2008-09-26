package eu.scy.core.service.impl;

import eu.scy.core.service.EloContainerManager;
import eu.scy.core.model.Elo;
import eu.scy.core.persistence.UserDAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import roolo.api.*;
import roolo.api.metadata.MetadataSingleUniversalValueAccessor;
import roolo.api.metadata.MetadataListLanguagesValueAccessor;
import roolo.api.metadata.MetadataListUniversalValueAccessor;
import roolo.api.metadata.MetadataSingleLanguageValueAccessor;
import roolo.api.basic.BasicELO;
import roolo.api.basic.BasicMetadata;
import roolo.cms.metadata.keys.Contribute;
import roolo.cms.elo.JDomBasicELOFactory;
import roolo.cms.elo.JDomStringConversion;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2008
 * Time: 06:07:46
 * Just a mock implementation of the elo container manager. Real one will connect to Roolo...
 */
public class EloContainerManagerMock implements EloContainerManager {


    private static Logger log = Logger.getLogger(EloContainerManagerMock.class);

    private UserDAO userDAO;

    private boolean isInitialized;

    private IRepository repository;

    private void initializeHack() {
         log.warn("*********************************************************");
        log.warn("*********************************************************");
        log.warn("*********************************************************");
        log.warn("SETTING UP DUMMY DATA!");
        log.warn("*********************************************************");
        log.warn("*********************************************************");
        log.warn("*********************************************************");
        log.warn("*********************************************************");

        Elo coolElo = new Elo();
        coolElo.setEloName("Henrik is a rabbagast!");
        getRepository().addELO(coolElo);
    }

    //TODO: REMOVE FASTER THAN LIGHTNING.... MEGA HACK
    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public IRepository getRepository() {
        return repository;
    }

    public void setRepository(IRepository repository) {
        this.repository = repository;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Elo getElo(String id) {
        log.info("Starting to get elo.....") ;
        //JDomBasicELOFactory jdomBasicELOFactory = null;
        //BasicELO elo = new BasicELO();
        if(jdomBasicELOFactory == null) {
            log.warn("FACTORY IS NULL!!!");
        }
        IELO<IMetadataKey> elo = jdomBasicELOFactory.createELO();
        log.info("Adding elo with id " + id) ;
        try {
            getRepository().addELO(elo);
        } catch(Exception e) {
            e.printStackTrace();
        }
        log.info("Added elo");

        log.info("URI: " + elo.getUri());




        try {

            URI uri = elo.getUri();

            IELO returned = getRepository().retrieveELO(uri);
            Elo returnElo = new Elo();
            returnElo.setEloContent(returned.getXml());
            return (Elo) returned;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public List<Elo> getUserElos(String token) {
        return Collections.EMPTY_LIST;
    }

    public String getToken(String username, String password) {
        return "JABBA DABBAH DOOOO!";
    }

    public Elo copyElo(Elo elo, String token) {
        // oooh - hacky deluxe!
        Elo newElo = new Elo();
        //newElo.setEloName(elo.getEloName() + "-copy");
        //newElo.setEloContent(elo.getEloContent());
        return newElo;
    }

    public Elo createNewElo(String token) {
        Elo newElo = new Elo();
        newElo.setEloName("I am a new one - please be gentle");
        newElo.setEloContent("<SomeXML/>");
        return newElo;
    }



    JDomStringConversion jdomStringConversion = new JDomStringConversion();

	@Autowired
	IMetadataTypeManager metadataTypeManager;
	@Resource(name = "uriKey")
	IMetadataKey uriKey;
	@Resource(name = "titleKey")
	IMetadataKey titleKey;
	@Resource(name = "formatKey")
	IMetadataKey formatKey;
	@Resource(name = "dateCreatedKey")
	IMetadataKey dateCreatedKey;
	@Resource(name = "missionKey")
	IMetadataKey missionKey;
	@Resource(name = "authorKey")
	IMetadataKey authorKey;
	@Resource(name = "testStringListKey")
	IMetadataKey testStringListKey;

	@Autowired
	IELOFactory jdomBasicELOFactory;

	IMetadata<IMetadataKey> metadata = null;
    

    IMetadata<IMetadataKey> createMetadata2() throws Exception
	{
		IMetadata<IMetadataKey> metadata = new BasicMetadata<IMetadataKey>();
		metadata.setDefaultLanguage(Locale.getDefault());
		MetadataSingleLanguageValueAccessor<String> titleValue = new MetadataSingleLanguageValueAccessor<String>(
					metadata, titleKey);
		titleValue.setValue("default name");
		titleValue.setValue("canada name", Locale.CANADA);
		new MetadataSingleUniversalValueAccessor<String>(metadata, formatKey).setValue("drawing");
		new MetadataSingleUniversalValueAccessor<Long>(metadata, dateCreatedKey).setValue(System
					.currentTimeMillis());
		new MetadataSingleUniversalValueAccessor<URI>(metadata, missionKey).setValue(new URI(
					"roolo://somewhere/myMission.mission"));
		new MetadataListUniversalValueAccessor<Contribute>(metadata, authorKey)
					.addValue(new Contribute("my vcard", System.currentTimeMillis()));
		MetadataListLanguagesValueAccessor<String> testStringListValue = new MetadataListLanguagesValueAccessor<String>(
					metadata, testStringListKey);
		testStringListValue.addValue("een", new Locale("NL"));
		testStringListValue.addValue("twee", new Locale("NL"));
		testStringListValue.addValue("one", new Locale("EN"));
		testStringListValue.addValue("two", new Locale("EN"));
		return metadata;
	}

}
