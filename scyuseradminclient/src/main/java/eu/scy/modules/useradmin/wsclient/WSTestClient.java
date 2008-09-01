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
    UserManagementService service = null;
    ObjectFactory factory = null;


    public WSTestClient() {

        try {
            url = new URL("http://localhost:8080/scyadmin/ws/userManagement.wsdl");
            qname = new QName("http://www.scy-net.eu/schemas", "UserManagementService");
            service = new UserManagementService(url, qname);
            factory = new ObjectFactory();

        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }

    private String login(String userName, String password) {
        LoginRequest request = new LoginRequest();
        request.setUserName(getStringType(userName));
        request.setPassword(getStringType(password));

        LoginResponse response = service.getUserManagementPort().login(request);
        return response.getLoginResponse().getMessage();

    }

    public static void main(String [] argh) {
        WSTestClient client = new WSTestClient();
        String token = client.login("scy", "scy");
        System.out.println("token: " + token);

    }

    private StringType getStringType(String name) {
        StringType type = factory.createStringType();
        type.setName(name);
        return type;


    }

}
