package eu.scy.core.service.impl;

import eu.scy.core.service.EloContainerManager;
import eu.scy.core.model.Elo;
import eu.scy.core.persistence.UserDAO;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Collections;
import java.net.URI;
import java.net.URISyntaxException;

import roolo.api.IRepository;
import roolo.api.IELO;

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
        try {

            if(!isInitialized()){
                initializeHack();
                setInitialized(true);
            }
            //log.info("URI IS NOW: " + coolElo.getUri());
            log.info("GETTING ELO! " + id);

            URI uri = new URI(id);
            IELO returned = getRepository().retrieveELO(uri);
            return (Elo) returned;
            /*log.debug("CREATING SILLY ELO.... at least something is returne....");
            Elo elo = new Elo();
            elo.setEloName("Henrik is cool!");
            elo.setEloContent("<xml><sometag>hee haa</sometag></xml>");
            return elo;
            */
        } catch (URISyntaxException e) {
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
}
