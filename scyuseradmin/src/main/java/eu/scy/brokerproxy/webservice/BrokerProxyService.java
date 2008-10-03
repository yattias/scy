package eu.scy.brokerproxy.webservice;

import eu.scy.brokerproxy.BrokerProxy;
import eu.scy.core.model.Elo;
import eu.scy.core.service.EloContainerManager;
import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

// import javax.jws.WebMethod;
// import javax.jws.WebService;

/**
 * Created by Intermedia User: Jeremy / Thomas Date: 11.aug.2008 Time: 10:58:01
 * Implementation of the BrokerProxy
 */

@WebService(endpointInterface = "eu.scy.brokerproxy.BrokerProxy", serviceName = "BrokerProxy")
public class BrokerProxyService implements BrokerProxy {

    private static Logger log = Logger.getLogger(BrokerProxyService.class);

    private EloContainerManager eloContainerManager;


    public EloContainerManager getEloContainerManager() {
        return eloContainerManager;
    }

    /** Injected via spring */
    public void setEloContainerManager(EloContainerManager eloContainerManager) {
        this.eloContainerManager = eloContainerManager;
    }

    @WebMethod
    public Elo copyElo(Elo elo, String token) {
        return getEloContainerManager().copyElo(elo, token);
    }

    @WebMethod
    public Elo createNewElo(@WebParam(name="token") String token, @WebParam(name="eloXML")String eloXML) {
        log.info("****************************** MESSAGE RECEIVED! I AM SOOO EXITED! **********************************");
        log.info("****************************** MESSAGE RECEIVED! I AM SOOO EXITED! **********************************");
        log.info("****************************** MESSAGE RECEIVED! I AM SOOO EXITED! **********************************");
        log.info("****************************** MESSAGE RECEIVED! I AM SOOO EXITED! **********************************");
        log.info("****************************** MESSAGE RECEIVED! I AM SOOO EXITED! **********************************");
        log.info("token : " + token);
        log.info("XML : " + eloXML);
        log.info("TOKEN: " + token);
        if(token == null ) throw new NullPointerException("TOKEN IS NULL");
        if(eloXML == null) throw new NullPointerException("ELO XML IS NULL!!!");
        return (Elo) getEloContainerManager().createNewElo(token);
    }

    @WebMethod
    public int destroyToken(String token) {
        // TODO Auto-generated method stub
        return 0;
    }                                                                                         

    @WebMethod
    public Elo getElo(@WebParam(name="eloURI")String eloURI, @WebParam(name="token")String token) {
        return (Elo) getEloContainerManager().getElo(eloURI);
    }

    @WebMethod
    public String getToken(String username, String password) {
        return getEloContainerManager().getToken(username, password);
    }

    @WebMethod
    public List<Elo> getUserElos(String token) {
        return getEloContainerManager().getUserElos(token);
    }

    @WebMethod
    public int lockElo(String eloUri, String token) {
        // TODO Auto-generated method stub
        return 0;
    }

    @WebMethod
    public List<String> retrieveAllElosAged(Date minDate, Date maxDate, String token) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public int setPermission(String eloId, int permission, String token) {
        // TODO Auto-generated method stub
        return 0;
    }

    @WebMethod
    public int unlockElo(String eloUri, String token) {
        // TODO Auto-generated method stub
        return 0;
    }

    @WebMethod
    public int updateElo(String token) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
