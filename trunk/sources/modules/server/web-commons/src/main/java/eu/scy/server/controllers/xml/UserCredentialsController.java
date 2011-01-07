package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.core.model.User;
import eu.scy.server.controllers.xml.transfer.UserCredentials;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        userCredentials.setUsername(getCurrentUserName(request));

        User currentUser = getCurrentUser(request);
        userCredentials.setPassword(currentUser.getUserDetails().getPassword());

        return userCredentials;
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.
        xStream.aliasPackage("eu.scy.server.controllers.xml.transfer", "");
        xStream.alias("usercredentials", UserCredentials.class);
    }
}
