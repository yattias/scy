package eu.scy.client.tools.formauthor;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localizer {
	static String baseName = "eu.scy.client.tools.formauthor.strings.strings";

	public static String getString(String key) {
		String result = "";
		try {
			Configuration config = new Configuration();
			String langID = null;
			if (config.getLanguage().equals("en"))
				langID = "en_EN";
			if (config.getLanguage().equals("de"))
				langID = "de_DE";
			if (langID == null) langID = "de_DE";
			ResourceBundle bundle = ResourceBundle.getBundle(baseName,
					new Locale(langID));
			result = (bundle.getString(key));
		} catch (MissingResourceException e) {
			System.err.println(e);
		}
		return result;
	}

}
