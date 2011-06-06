package eu.scy.roolows.crypto;

import eu.scy.roolows.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.codec.binary.Hex;

/**
 * REST Web Service
 *
 * @author __SVEN__
 */
@Path("/mockPasswordService")
public class MockPasswordService {

    @Context
    private UriInfo context;
    private static final Beans beans = Beans.getInstance();
    private static final Logger log = Logger.getLogger(MockPasswordService.class.getName());
    private static final LoginManager loginManager = LoginManager.getInstance();
    private static final String MOCK_PASSWORD = "secret pass";

    /** Creates a new instance of ChallengeResponse */
    public MockPasswordService() {
    }

    /**
     * GET method for testing purpose: returns a "Hello World" html-doc
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/xml")
    public String echoAlive() {
        return "<html><body><h1>Hello World!</body></h1></html>";
    }

    /**
     * POST method for creating a new ELO in RoOLO (specified by config). 
     * This service consumes <b>JSON data</b> consisting of the following parameters:
     * <ul>
     * <li>  content: the content of the ELO
     * <li>  username: username for authentication
     * <li>  password: password of the user for authentication
     * <li>  language: language of the ELO (ISO-639)
     * <li>  title: title of the ELO
     * <li>  type: technical format of the ELO, e.g. "scy/webresourcer" for SCYLighter/Webresourcer ELOs
     * </ul>
     * and these optional parameters:
     * <ul>
     * <li>  uri: the URI of the ELO to update. If specified, not a new ELO will be created, but an existing one will be updated
     * <li>  country: country code (ISO-3166)
     * <li>  description: a short description of the ELO
     * <li>  dateCreated: The date of ELO creation - if this is not specified, it will be created.
     * </ul>
     * @param jsonData The data formed as json, of which the ELO will be created - or updated
     * @return the URI of the saved/updated ELO
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/xml")
    public String challengeRespsonse(MultivaluedMap queryParams) {
        try {
            String username = (String) queryParams.getFirst("username");
            String firstChallenge = (String) queryParams.getFirst("firstChallenge");
            String secondChallenge = (String) queryParams.getFirst("secondChallenge");
            if (firstChallenge == null || secondChallenge == null) {
                log.log(Level.WARNING, "Trying to create C/R-Hash, but one of the values is null for user {0}", username);
                return null;
            }
            String password = MOCK_PASSWORD;
            //submit hashed challenge-response-value instead of password;
            String hashedPass = generateMD5Hash(firstChallenge + secondChallenge + password);
            log.log(Level.INFO, "Generated C/R-Hash for user {0}: {1}", new Object[]{username, hashedPass});

            return buildXmlString(username, hashedPass);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MockPasswordService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MockPasswordService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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

    public String buildXmlString(String username, String hash) {
        StringBuilder str = new StringBuilder("<usercredentials><username><![CDATA[");
        str.append(username);
        str.append("]]></username><password><![CDATA[");
        str.append(hash);
        str.append("]]></password></usercredentials>");
        return str.toString();
    }
}
