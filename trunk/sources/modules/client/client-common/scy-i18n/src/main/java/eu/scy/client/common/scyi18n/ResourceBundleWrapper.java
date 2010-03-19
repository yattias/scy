package eu.scy.client.common.scyi18n;

import java.net.URL;
import java.net.URLClassLoader;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ResouceBundleWrapper allows you to retrieve the properties file
 * for a class.
 * Your properties files must be placed in src/main/resources/languages/.
 * @author Marjolaine
 */
public class ResourceBundleWrapper {
    private final static String languageDir = "languages";
    private final static Locale defaultLocale = new Locale("en");

    private URLClassLoader loader;
    private String baseName = null;
    private ResourceBundle bundle;
    private static final Logger logger = Logger.getLogger(ResourceBundleWrapper.class.getName());

    /** Creates a ResourceBundleWrapper from the specified class object */
//    public ResourceBundleWrapper(Object o) {
//        initBundle(o, getModuleName(o));
//    }

    /** Creates a ResourceBundleWrapper from the specified class object and moduleName */
    public ResourceBundleWrapper(Object o, String moduleName) {
        initBundle(o, moduleName);
    }

    /** Gets a string for a given key from this resource bundle,
     * can be null if the key is invalid or if no object for the given key can be found
     * @param key the specified key
     * @return the string for the given key
     */
    public String getString(String key){
        try{
            return this.bundle.getString(key);
        }catch(MissingResourceException e){
            logger.log(Level.SEVERE, "Error while retrieving bundle string with the key: "+key);
            return key;
        }catch(Exception e2){
            logger.log(Level.SEVERE, "Invalid bundle key "+key);
            return key;
        }
    }


    /** Gets a string for a given locale  and a number
     * @param locale the specified locale
     * @param number the specified number
     * @return the string for the given number and locale
     */
    public String getNumberToString(Locale locale, double number){
        return getNumberFormat(locale).format(number);
    }

    /** Gets a numberFormat for a given locale
     * @param locale the specified locale
     * @return the numberFormat for the given locale
     */
    public NumberFormat getNumberFormat(Locale locale){
        return NumberFormat.getNumberInstance(locale);
    }


    private void initBundle(Object o, String moduleName){
        baseName = languageDir+"/"+moduleName;
        URL urlList[] = {o.getClass().getResource(languageDir+"/")};
        loader = new URLClassLoader(urlList);
        try{
            this.bundle  = ResourceBundle.getBundle(baseName, Locale.getDefault(), loader);
        }catch(MissingResourceException e){
          try{
                // english by def.
                bundle = ResourceBundle.getBundle(baseName, defaultLocale, loader);
                logger.log(Level.SEVERE, "Bundle missing with locale "+Locale.getDefault().getLanguage()+", english bundle by default");
          }catch (MissingResourceException e2){
              logger.log(Level.SEVERE, "No bundle for baseName "+baseName);
          }
        }
    }

    
}
