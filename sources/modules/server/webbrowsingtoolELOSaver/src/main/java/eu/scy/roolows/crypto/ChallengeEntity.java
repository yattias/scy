/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.roolows.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

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
    private String password;
    private String username;

    public ChallengeEntity(String username) {
        sc = String.valueOf(System.currentTimeMillis());
        this.username = username;
        password = lookupPassword(username);
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
        try {
            //sr = hash(sc + cc + secret)
            sr = generateMD5Hash(sc + cc + password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ChallengeEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChallengeEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sr;
    }

    public String calculateExpectedCr() {
        try {
            //sr = hash(sc + cc + secret)
            expectedCr = generateMD5Hash(cc + sc + password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ChallengeEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChallengeEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        final ChallengeEntity challengeEntity = new ChallengeEntity("bla");
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
        if(expectedCr.equals(cr)){
            return true;
        }
        return false;
    }
}
