package eu.scy.client.common.scyi18n;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * ResouceBundleWrapper allows you to retrieve the properties file
 * for a class.
 * Your properties files must be placed in src/main/resources/languages/.
 * @author Marjolaine
 */
public class ResourceBundleWrapper {

    private final static String propertyFileEncoding = "UTF-8";
    private final static String languageDir = "languages";
    private final static Locale defaultLocale = new Locale("en");
    private ClassLoader loader;
    private String baseName = null;
    private final String moduleName;
    private ResourceBundle bundle;
    private static final Logger logger = Logger.getLogger(ResourceBundleWrapper.class.getName());

    /** Creates a ResourceBundleWrapper from the specified class object,
     * the name of the package starts with eu.scy.client.xxx.moduleName */
    public ResourceBundleWrapper(Object o) {
        this.moduleName = getModuleName(o);
        initBundle(moduleName);
    }

    /** Creates a ResourceBundleWrapper from the specified moduleName */
    public ResourceBundleWrapper(String moduleName) {
        this.moduleName = moduleName;
        initBundle(moduleName);
    }

    /** Gets a string for a given key from this resource bundle,
     * return the key if the key is invalid or if no object for the given key can be found
     * @param key the specified key
     * @return the string for the given key
     */
    public String getString(String key) {
        if (bundle==null){
           return key;
        }
        try {
            return this.bundle.getString(key);
        } catch (MissingResourceException e) {
            logger.log(Level.WARN, "Can't find the key: '"+ key + "' in module " + moduleName);
            return key;
        } catch (Exception e2) {
            logger.log(Level.ERROR, "Problems with retrieving value for bundle key: '"+ key + "' in module " + moduleName, e2);
            return key;
        }
    }

    /** Gets a string for a given number
     * @param number the specified number
     * @return the string for the given number
     */
    public String getNumberToString(double number) {
        return getNumberFormat().format(number);
    }

    /** Gets a numberFormat
     * @return the numberFormat for the given locale
     */
    public NumberFormat getNumberFormat() {
        return NumberFormat.getNumberInstance(Locale.getDefault());
    }

    private void initBundle(String moduleName) {
        if (moduleName == null) {
            logger.log(Level.WARN, "failed to find the module name");
            return;
        }
        baseName = languageDir + "/" + moduleName;
//        URL urlList[] = {o.getClass().getClassLoader().getResource(languageDir+"/")};
//        loader = new URLClassLoader(urlList);
        loader = Thread.currentThread().getContextClassLoader();

        try {
            this.bundle = ResourceBundle.getBundle(baseName, Locale.getDefault(), loader);
        } catch (MissingResourceException e) {
            try {
                // english by def.
                bundle = ResourceBundle.getBundle(baseName, defaultLocale, loader);
                logger.log(Level.INFO, "Bundle missing with locale " + Locale.getDefault().getLanguage() + ", english bundle by default for " + moduleName);
            } catch (MissingResourceException e2) {
                logger.log(Level.WARN, "No bundle for baseName " + baseName);
            }
        } catch (Exception e3) {
            logger.log(Level.ERROR, "Failed to load resource bundle (e.g. language properties) for module: " +  moduleName, e3);
        }
    }

    private ResourceBundle getResourceBundle(String name, Locale locale, ClassLoader classLoader){
       String completeName = "" + name + "_" + locale.getLanguage() + ".properties";
       try {
          InputStream inputStream = classLoader.getResourceAsStream(completeName);
          if (inputStream!=null){
             Reader reader = new InputStreamReader(inputStream,propertyFileEncoding);
             ResourceBundle resourceBundle = new PropertyResourceBundle(reader);
             return resourceBundle;
          }
       } catch (Exception e){
          logger.log(Level.INFO,"problems with reading PropertyResourceBundle for "+ completeName,e);
       }
       throw new MissingResourceException(completeName,classLoader.toString(),locale.getLanguage());
    }

    private String getModuleName(Object o) {
        if (o != null) {
            Class clas = o.getClass();
            if (o instanceof Class){
               clas = (Class)o;
            }
            if (clas.getPackage()!=null){
               final String moduleName = getModuleName(clas.getPackage().getName());
               if (moduleName!=null){
                  return moduleName;
               }
            }
        }
        logger.log(Level.ERROR, "failed to find module name for: " + o.getClass().getName());
        return null;
    }
    private final static String[] commonPackageNames = {"eu.scy.client.tools.", "eu.scy.client.desktop.", "eu.scy.client.common."};

    protected String getModuleName(String packageName) {
        for (String commonPackageName : commonPackageNames) {
            if (packageName.startsWith(commonPackageName)) {
                String longModuleName = packageName.substring(commonPackageName.length());
                if (longModuleName.length() > 0) {
                    int pointPos = longModuleName.indexOf('.');
                    if (pointPos >= 0) {
                        return longModuleName.substring(0, pointPos);
                    }
                    return longModuleName;
                }
            }
        }
        return null;
    }
}
