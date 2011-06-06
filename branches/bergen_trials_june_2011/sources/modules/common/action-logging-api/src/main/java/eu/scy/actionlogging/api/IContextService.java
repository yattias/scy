package eu.scy.actionlogging.api;

import java.util.Set;

/**
 * The context service gathers all contextual information of one client. This is done on the one
 * hand by parsing the action logs and on the other hand by calling setters from the TBI.
 * 
 * @author weinbrenner
 * 
 */
public interface IContextService {

    /**
     * @return the username of the user that is currently logged in
     */
    public String getUsername();

    /**
     * @return the URI of the mission specification
     */
    public String getMissionSpecificationURI();

    /**
     * @return the name of the LAS, where the user is currently in
     */
    public String getCurrentLAS();

    /**
     * @return the unique ID of the current session, which is newly created after each login
     */
    public String getSession();

    /**
     * @return the URIs of all ELOs that are currently opened. These do not include ELOs that are
     *         open in other LAS, which are currently not visible.
     */
    public Set<String> getCurrentlyOpenedELOs();

    /**
     * @param username
     *            the name of the user that is logged in
     */
    void setUsername(String username);

    /**
     * @param missionSpecificationURI
     *            the URI to the mission specification
     */
    void setMissionSpecificationURI(String missionSpecificationURI);

}