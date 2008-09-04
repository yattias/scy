package eu.scy.brokerproxy.webservice;

import java.util.Date;
import java.util.List;

import eu.scy.brokerproxy.BrokerProxy;
import eu.scy.core.model.Elo;

// import javax.jws.WebMethod;
// import javax.jws.WebService;

/**
 * Created by Intermedia User: Jeremy / Thomas Date: 11.aug.2008 Time: 10:58:01
 * An attempt at a BrokerProxy WS
 */

// WebService
public class BrokerProxyService implements BrokerProxy {
    
    // WebMethod (operationName = "getServiceName" )
    public String getLogin(String s) {
        System.out.println("ServiceName!!");
        return "We are online!";
    }
    
    // WebMethod (operationName = "getDonkeyName" )
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
    
    public Elo getElo(String eloURI, String token) {
        // TODO Auto-generated method stub
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
