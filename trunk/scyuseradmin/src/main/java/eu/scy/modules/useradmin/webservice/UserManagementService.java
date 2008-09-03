package eu.scy.modules.useradmin.webservice;

import javax.jws.WebService;
import javax.jws.WebParam;
import javax.jws.WebMethod;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.aug.2008
 * Time: 11:50:09
 * To change this template use File | Settings | File Templates.
 */

@WebService  (targetNamespace = "http://www.scy-net.eu/schemas")
public interface UserManagementService {

    @WebMethod (operationName = "loginUser")
    String loginUser(String userName, String password);

}
