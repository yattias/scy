package eu.scy.tools.planning;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "eu.scy.tools.planning.messages"; //$NON-NLS-1$

//	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
//			.getBundle(BUNDLE_NAME);
	
	static ResourceBundle RESOURCE_BUNDLE;

	public Messages() {
		
		try {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch(MissingResourceException mre){
			Locale.setDefault(new Locale("en", "EN"));
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		}
	}

	public static String getString(String key) {
		try {	
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
