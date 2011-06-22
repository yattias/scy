package eu.scy.scymapper.impl.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localization {
	private static final String BUNDLE_NAME = "languages.scymapper";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Localization() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
