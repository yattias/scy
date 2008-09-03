package eu.scy.modules.useradmin.wsclient;



import javax.xml.namespace.QName;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 01.sep.2008
 * Time: 12:44:18
 * To change this template use File | Settings | File Templates.
 */
public class WSTestClient {

    private URL url = null;
    QName qname = null;
    UserManagement userManagement = null;
    ObjectFactory factory = null;


    public WSTestClient() {
        userManagement = new UserManagement();

    }

    public String login(String userName, String password) {
        return userManagement.getUserManagementServiceImplPort().loginUser(userName, password);

    }

    public static void main(String [] argh) {
        WSTestClient client = new WSTestClient();
        String token = client.login("scy", "scy");
        System.out.println("token: " + token);

    }


}
