package eu.scy.scymapper.impl.ui;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.util.logging.Logger;

/**
 *
 * @author lars
 *
 * changed completely to make use of scy-i18n
 */
public class Localization {

	private static Localization instance = null;
	private static ResourceBundleWrapper bundle = null;
	private final static Logger debugLogger = Logger.getLogger(Localization.class.getName());

	// singleton pattern
	private Localization() {
		try {
			bundle = new ResourceBundleWrapper("scymapper");
		} catch (Exception ex) {
			debugLogger.warning(ex.toString());
		}
	}

	private static Localization getInstance() {
		if (instance == null) {
			instance = new Localization();
		}
		return instance;
	}

	public static String getString(String key) {
		return getInstance().bundle.getString(key);
	}

// ye old stuff...

//	private static final String BUNDLE_NAME = "languages.scymapper";
//
//	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
//			.getBundle(BUNDLE_NAME);
//
//	private Localization() {
//	}
//
//	public static String getString(String key) {
//		try {
//			return RESOURCE_BUNDLE.getString(key);
//		} catch (MissingResourceException e) {
//			return '!' + key + '!';
//		}
//	}
}
