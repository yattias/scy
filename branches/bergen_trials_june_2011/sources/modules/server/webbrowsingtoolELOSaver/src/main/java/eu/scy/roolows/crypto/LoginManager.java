/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.roolows.crypto;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sven
 */
public class LoginManager {

    private Map<String,ChallengeEntity> pendingChallenges;

    private LoginManager() {
        pendingChallenges = new HashMap<String, ChallengeEntity>();
    }

    //returns the value for the challenge, which is actually the timestamp
    public String addChallenge(String username, String passwordServiceURL){
        final ChallengeEntity challengeEntity = new ChallengeEntity(username, passwordServiceURL);
        pendingChallenges.put(username, challengeEntity);
        return challengeEntity.getSc();
    }

    public ChallengeEntity getChallenge(String username){
        return pendingChallenges.get(username);
    }

    public static LoginManager getInstance() {
        return LoginManagerHolder.INSTANCE;
    }

    public void removeChallenge(String username) {
        this.pendingChallenges.remove(username);
    }

    private static class LoginManagerHolder {
        private static final LoginManager INSTANCE = new LoginManager();
    }
 }
