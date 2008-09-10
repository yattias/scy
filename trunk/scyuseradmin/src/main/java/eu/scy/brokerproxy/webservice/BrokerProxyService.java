package eu.scy.brokerproxy.webservice;

import java.util.Date;
import java.util.List;

import eu.scy.brokerproxy.BrokerProxy;
import eu.scy.core.model.Elo;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import org.apache.log4j.Logger;

// import javax.jws.WebMethod;
// import javax.jws.WebService;

/**
 * Created by Intermedia User: Jeremy / Thomas Date: 11.aug.2008 Time: 10:58:01
 * An attempt at a BrokerProxy WS
 */

@WebService(endpointInterface = "eu.scy.brokerproxy.BrokerProxy", serviceName = "BrokerProxy")
public class BrokerProxyService implements BrokerProxy {

    private static Logger log = Logger.getLogger(BrokerProxyService.class);

    // WebMethod (operationName = "getServiceName" )
    public String getLogin(String s) {
        System.out.println("ServiceName!!");
        return "We are online!";
    }
    

    public String getDonkeyName(String name) {
        System.out.println("donkey!");
        return "Burro burro, " + name;
    }
    
    public Elo copyElo(Elo elo, String token) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Elo createNewElo(String token) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public int destroyToken(String token) {
        // TODO Auto-generated method stub
        return 0;
    }

    @WebMethod
    public Elo getElo(@WebParam(name="eloURI")String eloURI, @WebParam(name="token")String token) {
        log.debug("Getting elo: "+ eloURI + " I AM A REAL ROCKER!");
        return null;
    }
    
    public String getToken(String username, String password) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public List<String> getUserElos(String token) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public int lockElo(String eloUri, String token) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public List<String> retrieveAllElosAged(Date minDate, Date maxDate, String token) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public int setPermission(String eloId, int permission, String token) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public int unlockElo(String eloUri, String token) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public int updateElo(String token) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
