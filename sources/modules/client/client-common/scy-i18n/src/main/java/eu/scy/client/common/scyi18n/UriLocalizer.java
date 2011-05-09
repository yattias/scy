/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.common.scyi18n;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Class for localizing a url, uri or path.
 * 
 * Localizing is done by replacing the language part by the default language. A language part is /
 * *_en* / or * /en/ * or *_en.*. In place of en any two lower case characters will be recognized as
 * language. Only the first occurence will be replaced.
 * 
 * @author sikken
 */
public class UriLocalizer
{

   private static final Logger logger = Logger.getLogger(UriLocalizer.class);
   private final String localeLanguage;
   private final String definitionLocaleLanguage;
   private final Pattern languageInPathPattern = Pattern.compile("_[a-z]{2}/");
   private final Pattern languageOnlyNameInPathPattern = Pattern.compile("/[a-z]{2}/");
   private final Pattern languageOnlyNameAtBeginPathPattern = Pattern.compile("^[a-z]{2}/");
   private final Pattern languageInNamePattern = Pattern.compile("_[a-z]{2}\\.[\\w]+$");
   public static LocalUriReplacement[] localUriReplacements;

   public static LocalUriReplacement[] createLocalUriReplacementFromString(String string)
   {
      if (string == null)
      {
         return null;
      }
      StringTokenizer tokenizer = new StringTokenizer(string, ";");
      LocalUriReplacement[] newLocalUriReplacements = new LocalUriReplacement[tokenizer.countTokens()];
      int i = 0;
      while (tokenizer.hasMoreTokens())
      {
         String specification = tokenizer.nextToken();
         newLocalUriReplacements[i] = new LocalUriReplacement(specification);
         ++i;
      }
      return newLocalUriReplacements;
   }

   public UriLocalizer()
   {
      localeLanguage = Locale.getDefault().getLanguage();
      definitionLocaleLanguage = Locale.ENGLISH.getLanguage();
   }

   /**
    * Replaces the language specification by the default language. If the localized url does not
    * exists, the languages part will be replaced by the definition language en (English). If that
    * localized url also not exists, the original url will be returned.
    * 
    * @param url
    * @return localized url
    */
   public URL localizeUrlwithChecking(URL url)
   {
      if (url == null)
      {
         return null;
      }
      URL localizedUrl = localizeUrl(url);
      if (urlExisits(localizedUrl))
      {
         return localizedUrl;
      }
      logger.info("failed to find url: " + localizedUrl.toString());
      localizedUrl = localizeUrl(url, definitionLocaleLanguage);
      if (urlExisits(localizedUrl))
      {
         return localizedUrl;
      }
      logger.info("failed to find url: " + localizedUrl.toString());
      return url;
   }

   private boolean urlExisits(URL url)
   {
      try
      {
         URLConnection connection = url.openConnection();
         InputStream inputStream = connection.getInputStream();
         inputStream.close();
         return true;
      }
      catch (FileNotFoundException ex)
      {
      }
      catch (IOException ex)
      {
      }
      return false;
   }

   /**
    * Replaces the first language specification in the url by the default language. There are no
    * checks performed if the localized url exists or not.
    * 
    * @param url
    * @return localized url
    */
   public URL localizeUrl(URL url)
   {
      return localizeUrl(url, localeLanguage);
   }

   private URL localizeUrl(URL url, String targetLanguage)
   {
      if (url == null)
      {
         return null;
      }
      try
      {
         return localizeUri(url.toURI(), targetLanguage).toURL();
      }
      catch (Exception ex)
      {
         logger.error("problems localizing url: " + url, ex);
      }
      return url;
   }

   /**
    * Replaces the language specification by the default language. If the localized uri does not
    * exists, the languages part will be replaced by the definition language en (English). If that
    * localized uri also not exists, the original uri will be returned.
    * 
    * @param uri
    * @return localized uri
    */
   public URI localizeUriWithChecking(URI uri)
   {
      if (uri == null)
      {
         return null;
      }
      try
      {
         URL url = uri.toURL();
         URL localizedUrl = localizeUrlwithChecking(url);
         return localizedUrl.toURI();
      }
      catch (MalformedURLException e)
      {
         logger.error("problems localizing uri: " + uri, e);
      }
      catch (URISyntaxException e)
      {
         logger.error("problems localizing uri: " + uri, e);
      }
      return uri;
   }

   /**
    * Replaces the language specification by the default language. If the localized uri does not
    * exists, the languages part will be replaced by the definition language en (English). If that
    * localized uri also not exists, the original uri will be returned.
    * 
    * @param uri
    * @return localized uri
    */
   public String localizeUriWithChecking(String uri)
   {
      if (uri == null)
      {
         return null;
      }
      try
      {
         URI realUri = new URI(uri);
         return localizeUriWithChecking(realUri).toString();
      }
      catch (URISyntaxException e)
      {
         logger.error("problems localizing uri: " + uri, e);
      }
      return uri;
   }

   /**
    * Replaces the first language specification in the uri by the default language. There are no
    * checks performed if the localized uri exists or not.
    * 
    * @param uri
    * @return localized uri
    */
   public URI localizeUri(URI uri)
   {
      return localizeUri(uri, localeLanguage);
   }

   private URI localizeUri(URI uri, String targetLanguage)
   {
      if (uri == null)
      {
         return null;
      }
      String localizedPath = localizePath(uri.getPath(), targetLanguage);
      try
      {
         URI localizedUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
            localizedPath.replace(' ', '+'), uri.getQuery(), uri.getFragment());
         String localLocalizedUri = makePathLocalIfSpecified(localizedUri.toString());
         return new URI(localLocalizedUri);
      }
      catch (URISyntaxException ex)
      {
         logger.error("problems localizing uri: " + uri, ex);
      }
      return uri;
   }

   /**
    * Replaces the first language specification in the uri by the default language.
    * 
    * @param uri
    * @return localized uri
    */
   public String localizeUri(String uri)
   {
      return localizeUri(uri, localeLanguage);
   }

   private String localizeUri(String uri, String targetLanguage)
   {
      if (uri == null)
      {
         return null;
      }
      try
      {
         return localizeUri(stringToUri(uri), targetLanguage).toString();
      }
      catch (URISyntaxException ex)
      {
         logger.error("problems localizing uri: " + uri, ex);
      }
      return uri;
   }

   /**
    * Replaces the first language specification in the path by the default language.
    * 
    * @param path
    * @return localized path
    */
   public String localizePath(String path)
   {
      String localizedPath = localizePath(path, localeLanguage);
      String localLocalizedPath = makePathLocalIfSpecified(localizedPath);
      return localLocalizedPath;
   }

   private String localizePath(String path, String targetLanguage)
   {
      if (path == null)
      {
         return null;
      }
      Matcher languageInPathMatch = languageInPathPattern.matcher(path);
      if (languageInPathMatch.find())
      {
         return replaceMatchedPattern(path, languageInPathMatch, "_" + targetLanguage + "/");
      }
      Matcher languageOnlyNameInPathMatch = languageOnlyNameInPathPattern.matcher(path);
      if (languageOnlyNameInPathMatch.find())
      {
         return replaceMatchedPattern(path, languageOnlyNameInPathMatch, "/" + targetLanguage + "/");
      }
      Matcher languageOnlyNameAtBeginPathMatch = languageOnlyNameAtBeginPathPattern.matcher(path);
      if (languageOnlyNameAtBeginPathMatch.find())
      {
         return replaceMatchedPattern(path, languageOnlyNameAtBeginPathMatch, targetLanguage + "/");
      }
      Matcher languageInNameMatch = languageInNamePattern.matcher(path);
      if (languageInNameMatch.find())
      {
         int startMatchPos = languageInNameMatch.start();
         return path.substring(0, startMatchPos) + "_" + targetLanguage + "."
            + path.substring(startMatchPos + targetLanguage.length() + 2);
      }
      return path;
   }

   private String replaceMatchedPattern(String path, Matcher matcher, String replacement)
   {
      int startMatchPos = matcher.start();
      int endMatchPos = matcher.end();
      return path.substring(0, startMatchPos) + replacement + path.substring(endMatchPos);
   }

   String makePathLocalIfSpecified(String path)
   {
      if (localUriReplacements == null || path == null)
      {
         return path;
      }
      String localPath = path;
      for (LocalUriReplacement localUriReplacement : localUriReplacements)
      {
         localPath = doLocalUriReplacement(localUriReplacement, localPath);
      }
      return localPath;
   }

   String doLocalUriReplacement(LocalUriReplacement localUriReplacement, String path)
   {
      if (path.startsWith(localUriReplacement.getSource()))
      {
         return localUriReplacement.getReplacement() + path.substring(localUriReplacement.getSource().length());
      }
      // do a check for a possible replacement, when the case is ignored
      if (path.length() >= localUriReplacement.getSource().length())
      {
         if (path.substring(0, localUriReplacement.getSource().length()).equalsIgnoreCase(localUriReplacement.getSource()))
         {
            logger.warn("possible case problem in path: " + path);
         }
      }
      return path;
   }
   private static final String encoding = "UTF-8";

   private URI stringToUri(String string) throws URISyntaxException
   {
      return new URI(string.replace(' ', '+'));
   }
}
