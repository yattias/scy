package eu.scy.core.service;

import eu.scy.core.model.Elo;

import java.util.List;

import roolo.api.IELO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2008
 * Time: 06:07:15
 * The elo container manager is responsible for connecting to the elo repository and handle elos there.
 */
public interface EloContainerManager {

    Elo getElo(String id);

    List<Elo> getUserElos(String token);

    /**
     * Do we need this one?
     * @param username
     * @param password
     */
    String getToken(String username, String password);

    Elo copyElo(Elo elo, String token);

    Elo createNewElo(String token);
}
