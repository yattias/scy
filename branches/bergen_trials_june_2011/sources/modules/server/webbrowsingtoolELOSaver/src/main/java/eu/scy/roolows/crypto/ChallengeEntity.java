/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.roolows.crypto;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.transform.dom.DOMSource;
import org.apache.commons.codec.binary.Hex;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Server sends a unique challenge value sc to the client
 * Client generates unique challenge value cc
 * Client computes cr = hash(cc + sc + secret)
 * Client sends cr and cc to the server
 * Server calculates the expected value of cr and ensures the client responded correctly
 * Server computes sr = hash(sc + cc + secret)
 * Server sends sr
 * Client calculates the expected value of sr and ensures the server responded correctly
 *
 * where
 *
 * sc is the server generated challenge
 * cc is the client generated challenge
 * cr is the client response
 * sr is the server response

 * @author sven
 */
public class ChallengeEntity {

    private static final Logger logger = Logger.getLogger(ChallengeEntity.class.getName());
    //client generated challenge
    private String cc;
    //server generated challenge
    private String sc;
    //client computes cr, expectedCr will be cr = hash(cc + sc + secret)
    private String cr;
    private String expectedCr;
    //server response sr = hash(sc + cc + secret)
    private String sr;
    //the secret
    private String username;
    private final String passwordServiceURL;

    public ChallengeEntity(String username, String passwordServiceURL) {
        logger.log(Level.INFO, "PasswordServiceURL: {0}", passwordServiceURL);
        sc = String.valueOf(System.currentTimeMillis());
        this.username = username;
        this.passwordServiceURL = passwordServiceURL;

    }

    private String lookupPassword(String username) {
        return "secret pass";
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
        sr = calculateSr();
    }

    public String getExpectedCr() {
        return expectedCr;
    }

    public void setExpectedCr(String expectedCr) {
        this.expectedCr = expectedCr;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String calculateSr() {
        //sr = hash(sc + cc + secret)
        //webservice call
        //String firstChallenge = request.getParameter("firstChallenge");
        //String secondChallenge = request.getParameter("secondChallenge");
        sr = getPasswordHash(sc, cc);
//            sr = generateMD5Hash(sc + cc + password);
        return sr;
    }

    public String calculateExpectedCr() {
        //sr = hash(sc + cc + secret)
        //webservice call
        expectedCr = getPasswordHash(cc, sc);
//            expectedCr = generateMD5Hash(cc + sc + password);
        return expectedCr;
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

    public static void main(String[] args) {
        final ChallengeEntity challengeEntity = new ChallengeEntity("bla", "localhost");
        try {
            String generateMD5Hash = challengeEntity.generateMD5Hash("Hello World");
            System.out.println("String MD5:" + generateMD5Hash);
            System.out.println("----------:b10a8db164e0754105b7a99be72e3fe5");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ChallengeEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChallengeEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public boolean isAuthorized() {
        //matching of cr and expected cr
        calculateExpectedCr();
        if (expectedCr.equals(cr)) {
            return true;
        }
        return false;
    }

    public String getPasswordHash(String firstChallenge, String secondChallenge) {
        String hash = null;
        Client client = Client.create();
        WebResource webResource = client.resource(passwordServiceURL);

        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("username", username);
        queryParams.add("firstChallenge", firstChallenge);
        queryParams.add("secondChallenge", secondChallenge);
//        ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, queryParams);
        DOMSource response = webResource.queryParams(queryParams).get(DOMSource.class);
        NodeList childNodes = response.getNode().getChildNodes();
        Node userCredentials = childNodes.item(0);
        Node userNode = userCredentials.getChildNodes().item(1);
        String user = userNode.getTextContent();
        if (!user.equals(username)) {
            logger.log(Level.SEVERE, "Usernames from both services are different! This user: {0}; username from PasswordService: {1}", new Object[]{username, user});
        }
        hash = userCredentials.getChildNodes().item(3).getTextContent();
        logger.log(Level.INFO, "received hash: {0}, for user: {1}", new Object[]{hash, user});
        return hash;
    }
}
