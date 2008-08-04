package eu.scy.modules.useradmin.webservice;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.Group;

import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:03:08
 * A quick start for using the user dao from a web service.... simple stuff really - annotate methods as WebMethod and boom you go!
 */
@WebService
public class UserManagement {

    private UserDAOHibernate userDAO;


    @WebMethod  (operationName = "getServiceName" )
    public String getServiceName() {
        System.out.println("ServiceName!");
        return "UserManagement";
    }

    public UserDAOHibernate getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
    }

    @WebMethod (operationName ="createGroup")
    public Group createGroup(String name, Group parent) {
        return getUserDAO().createGroup(name, parent);
    }

    @WebMethod(operationName = "getRootGroup")
    public Group getRootGroup() {
        return getUserDAO().getRootGroup();
    }

}
