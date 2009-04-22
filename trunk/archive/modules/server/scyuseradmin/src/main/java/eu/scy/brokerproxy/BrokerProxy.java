package eu.scy.brokerproxy;

import eu.scy.core.model.Elo;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;




@WebService(targetNamespace = "http://www.scy-net.eu/schemas/brokerproxy")
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
    @WebMethod(operationName = "getToken")
    public String getToken(String username, String password);
    
    /**
     * Destroy token.
     * 
     * @param token
     *            the token
     * @return the int
     */
    @WebMethod(operationName = "destroyToken")
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
    @WebMethod(operationName = "getELO")
    public Elo getElo(String eloURI, String token);
    
    /**
     * Gets a list of available ELOs to choose from.
     * 
     * @param token
     *            the token
     * @return the user elos
     */
    @WebMethod(operationName = "getUserElos")
    public List<Elo> getUserElos(String token);
    
    /**
     * Update current elo. (save... function)
     * 
     * @param token
     *            the token
     * @return the int
     */
    @WebMethod(operationName = "updateElo")
    public int updateElo(String token);
    
    /**
     * Creates a new elo. (new... function)
     * 
     * @param token
     *            the token
     * @param eloXML
     * @return the elo
     */
    @WebMethod(operationName = "createNewElo")
    public Elo createNewElo(String token, String eloXML);
    
    /**
     * Copy an elo. (save as... function)
     * 
     * @param elo
     *            the elo
     * @param token
     *            the token
     * @return the elo
     */
    @WebMethod(operationName = "copyElo")
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
    @WebMethod(operationName = "lockElo")
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
    @WebMethod(operationName = "unlockElo")
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
    @WebMethod(operationName = "setPermission")
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
    @WebMethod(operationName = "retrieveAllElosAged")
    public List<String> retrieveAllElosAged(Date minDate, Date maxDate, String token);
    
}
