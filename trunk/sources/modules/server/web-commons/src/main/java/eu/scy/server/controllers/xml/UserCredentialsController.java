package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.UserCredentials;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import org.apache.commons.codec.binary.Hex;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 13:22:16
 * To change this template use File | Settings | File Templates.
 */
public class UserCredentialsController extends XMLStreamerController {

    private static final Logger log = Logger.getLogger(UserCredentialsController.class.getName());

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            UserCredentials userCredentials = new UserCredentials();
            Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                log.log(Level.INFO, "PARAM {0}", enumeration.nextElement());
            }
            String username = request.getParameter("username");
            if (username == null) {
                username = getCurrentUserName(request);
            }
            userCredentials.setUsername(username);
            User currentUser = getUserService().getUser(username);
            String firstChallenge = request.getParameter("firstChallenge");
            String secondChallenge = request.getParameter("secondChallenge");
            if(firstChallenge==null || secondChallenge == null){
                log.log(Level.WARNING, "Trying to create C/R-Hash, but one of the values is null for user {0}", currentUser.getUserDetails().getUsername());
                return null;
            }
            String password = currentUser.getUserDetails().getPassword();
            //submit hashed challenge-response-value instead of password;
            String hashedPass = generateMD5Hash(firstChallenge + secondChallenge + password);
            log.log(Level.INFO, "Generated C/R-Hash for user {0}: {1}", new Object[]{currentUser.getUserDetails().getUsername(), hashedPass});
            userCredentials.setPassword(hashedPass);
            return userCredentials;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserCredentialsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserCredentialsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);
        xStream.aliasPackage("eu.scy.core.model.transfer", "");
        xStream.alias("usercredentials", UserCredentials.class);
    }

    public byte[] generateMD5HashAsByte(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = str.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(bytesOfMessage);
        return hash;
    }

    public String generateHexString(byte[] bytes) {
        String hexString = new String(Hex.encodeHex(bytes));
        return hexString;
    }

    public String generateMD5Hash(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return generateHexString(generateMD5HashAsByte(str));
    }
}
