package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.UserCredentials;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 13:22:16
 * To change this template use File | Settings | File Templates.
 */
public class UserCredentialsController extends XMLStreamerController{
    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        UserCredentials userCredentials = new UserCredentials();

        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            System.out.println("PARAM " + enumeration.nextElement());
        }

        String username = request.getParameter("username");
        if(username  == null) {
            username = getCurrentUserName(request);
        }
        userCredentials.setUsername(username);
        User currentUser = getUserService().getUser(username);
        userCredentials.setPassword(currentUser.getUserDetails().getPassword());

        return userCredentials;
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.
        xStream.aliasPackage("eu.scy.core.model.transfer", "");
        xStream.alias("usercredentials", UserCredentials.class);
    }
}
