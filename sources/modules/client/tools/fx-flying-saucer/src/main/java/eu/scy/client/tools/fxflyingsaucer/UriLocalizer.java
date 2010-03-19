/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author sikken
 */
public class UriLocalizer
{

   private static final Logger logger = Logger.getLogger(UriLocalizer.class);
   private final String localeLanguage = "_" + Locale.getDefault().getLanguage();
   private final Pattern languageInPathPattern = Pattern.compile("_[a-z]2/");
   private final Pattern languageInNamePattern = Pattern.compile("_[a-z]2\\.[\\w]+$");

   public UriLocalizer()
   {
   }

   public URI localizeUri(URI uri)
   {
      String localizedPath = localizePath(uri.getPath());
      try
      {
         return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), localizedPath, uri.getQuery(), uri.getFragment());
      }
      catch (URISyntaxException ex)
      {
         logger.error("problems localizing uri: " + uri, ex);
      }
      return uri;
   }

   public String localizeUri(String uri)
   {
      try
      {
         return localizeUri(new URI(uri)).toString();
      }
      catch (URISyntaxException ex)
      {
         logger.error("problems localizing uri: " + uri, ex);
      }
      return uri;
   }

   public String localizePath(String path)
   {
      logger.debug("path: " + path);
      String localizedPath = path;
      Matcher languageInPathMatch = languageInPathPattern.matcher(path);
      if (languageInPathMatch.matches())
      {
         int startMatchPos = languageInPathMatch.start();
         int endMatchPos = languageInPathMatch.end();
         localizedPath = path.substring(0, startMatchPos) + localeLanguage + "/" + path.substring(endMatchPos);
      }
      else
      {
         Matcher languageInNameMatch = languageInNamePattern.matcher(path);
         if (languageInNameMatch.matches())
         {
            int startMatchPos = languageInNameMatch.start();
            int endMatchPos = languageInNameMatch.end();
            localizedPath = path.substring(0, startMatchPos) + localeLanguage + "." + path.substring(endMatchPos);
         }
      }
      return localizedPath;
   }
}
