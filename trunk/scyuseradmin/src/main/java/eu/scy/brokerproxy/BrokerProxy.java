package eu.scy.brokerproxy;

import java.util.Date;
import java.util.List;

import eu.scy.core.model.Elo;

public interface BrokerProxy {
    
    /** The Constant token. */
    public static final String token = null;
    
    /** The Constant code. */
    public static final int code = 0;
    
    /** The Constant eloUriList. */
    public static final List<String> eloUriList = null;
    
    /** The Constant elo. */
    public static final Elo elo = null;
    
    /** The Constant clonedElo. */
    public static final Elo clonedElo = null;
    
    /**
     * Gets the token.
     * 
     * @param username
     *            the username
     * @param password
     *            the password
     * @return the token
     */
    public String getToken(String username, String password);
    
    /**
     * Destroy token.
     * 
     * @param token
     *            the token
     * @return the int
     */
    public int destroyToken(String token);
    
    /**
     * Gets the elo.
     * 
     * @param eloURI
     *            the elo uri
     * @param token
     *            the token
     * @return the elo
     */
    public Elo getElo(String eloURI, String token);
    
    /**
     * Gets a list of available ELOs to choose from.
     * 
     * @param token
     *            the token
     * @return the user elos
     */
    public List<String> getUserElos(String token);
    
    /**
     * Update current elo. (save... function)
     * 
     * @param token
     *            the token
     * @return the int
     */
    public int updateElo(String token);
    
    /**
     * Creates a new elo. (new... function)
     * 
     * @param token
     *            the token
     * @return the elo
     */
    public Elo createNewElo(String token);
    
    /**
     * Copy an elo. (save as... function)
     * 
     * @param elo
     *            the elo
     * @param token
     *            the token
     * @return the elo
     */
    public Elo copyElo(Elo elo, String token);
    
    /**
     * Lock an elo.
     * 
     * @param eloUri
     *            the elo uri
     * @param token
     *            the token
     * @return the int
     */
    public int lockElo(String eloUri, String token);
    
    /**
     * Unlock an elo.
     * 
     * @param eloUri
     *            the elo uri
     * @param token
     *            the token
     * @return the int
     */
    public int unlockElo(String eloUri, String token);
    
    /**
     * Sets the permission on an elo.
     * 
     * @param eloId
     *            the elo id
     * @param permission
     *            the permission
     * @param token
     *            the token
     * @return the int
     */
    public int setPermission(String eloId, int permission, String token);
    
    /**
     * Retrieve all elos from/to.
     * 
     * @param minDate
     *            the min date
     * @param maxDate
     *            the max date
     * @param token
     *            the token
     * @return the list< string>
     */
    public List<String> retrieveAllElosAged(Date minDate, Date maxDate, String token);
    
}
