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
    public String getServiceName() {
        System.out.println("ServiceName!!");
        return "BrokerProxyTest.";
    }


}
