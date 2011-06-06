/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

/**
 * Utilities for importing ELOs, like Base64-Encoding
 * @author Sven
 */

public class ImportUtils {

    /**
     * File-to-Byte-Array-Conversion, so the file can be Base64-encoded
     * @param file The File to convert
     * @return Byte-Array of the File
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = null;
        try{
           is = new FileInputStream(file);
           long length = file.length();
           if (length > Integer.MAX_VALUE) {
               new Exception ("Maximum filesize reached.");
           }
           byte[] bytes = new byte[(int) length];
           int offset = 0;
           int numRead = 0;
           while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
               offset += numRead;
           }
           if (offset < bytes.length) {
               throw new IOException("Could not completely read file " + file.getName());
           }
           return bytes;
        }
        finally{
          if (is!=null){
             is.close();
          }
        }
    }

    public static void saveBytesToFile(byte[] bytes, File file) throws IOException{
       OutputStream out = null;
       try{
          out = new FileOutputStream(file);
          out.write(bytes);
       }
       finally{
          if (out!=null){
             out.close();
          }
       }
    }

    /**
     * Base64-Encoding of a Byte-Array
     * @param bytesFromFile The Byte-Array to encode, for example from a file
     * @return String (Base64 encoded)
     */
    public static String encodeBase64(byte[] bytesFromFile) {
        return new String(Base64.encodeBase64(bytesFromFile));
    }

    /**
     * Base64-Encoding of a File
     * @param file the File to encode
     * @return String (Base64 encoded) or an empty String if the File has thrown an IOException
     */
    public static String encodeBase64(File file){
        try {
            return encodeBase64(ImportUtils.getBytesFromFile(file));
        } catch (IOException ex) {
            Logger.getLogger(ImportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Base64-Decoding of a Byte-Array
     * @param base64Data The data to decode
     * @return A byte-Array of the decoded data
     */
    public static byte[] decodeBase64(byte[] base64Data){
        return Base64.decodeBase64(base64Data);
    }

}
