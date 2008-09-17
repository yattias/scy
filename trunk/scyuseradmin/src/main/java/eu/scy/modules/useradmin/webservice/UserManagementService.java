package eu.scy.modules.useradmin.webservice;

import eu.scy.core.model.User;

import javax.jws.WebService;
import javax.jws.WebParam;
import javax.jws.WebMethod;
import java.util.List;

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
    String loginUser(@WebParam(name="userName")String userName, @WebParam(name="password")String password);

    @WebMethod (operationName = "getBuddies")
    List<User> getBuddies(@WebParam (name="userName") String userName);

}
