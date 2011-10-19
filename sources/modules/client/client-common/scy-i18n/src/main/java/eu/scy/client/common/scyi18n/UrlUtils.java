/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.common.scyi18n;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author SikkenJ
 */
public class UrlUtils
{

   private static final Logger logger = Logger.getLogger(UrlUtils.class);
   private static final Map<String, Boolean> urlExistsMap = new ConcurrentHashMap<String, Boolean>();

   public static boolean urlExisits(URL url)
   {
      boolean exists = false;
      String utlString = url.toString();
      Boolean cachedValue = urlExistsMap.get(utlString);
      if (cachedValue != null)
      {
         return cachedValue;
      }
      try
      {
         URLConnection connection = url.openConnection();
         if (connection instanceof HttpURLConnection)
         {
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("HEAD");
            httpUrlConnection.connect();
            int responseCode = httpUrlConnection.getResponseCode();
            exists = responseCode == HttpURLConnection.HTTP_OK;
         }
         else
         {
            InputStream inputStream = connection.getInputStream();
            inputStream.close();
            exists = true;
         }
      }
      catch (FileNotFoundException ex)
      {
      }
      catch (IOException ex)
      {
      }
      urlExistsMap.put(utlString, exists);
      return exists;
   }

   public static boolean uriExisits(URI uri)
   {
      try
      {
         return urlExisits(uri.toURL());
      }
      catch (MalformedURLException ex)
      {
         logger.debug("the uri (" + uri + ") is not a vaild url, " + ex.getMessage());
      }
      return false;
   }
}
