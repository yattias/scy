package eu.scy.brokerproxy.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Intermedia
 * User: Jeremy / Thomas
 * Date: 11.aug.2008
 * Time: 10:58:01
 * An attempt at a BrokerProxy WS
 */

@WebService
public class BrokerProxyTest {


    @WebMethod  (operationName = "getServiceName" )
    public String getServiceName(String s) {
        System.out.println("ServiceName!!");
        return "We are online!";
    }


    @WebMethod  (operationName = "getDonkeyName" )
    public String getDonkeyName(String name) {
        System.out.println("donkey!");
        return "Burro burro, " + name;
    }

    
}
