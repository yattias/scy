package eu.scy.client.tools.drawing;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer
{
	public static final Properties properties = new Properties();

	@Override
	protected String resolveSystemProperty(String arg0)
	{
		String value = properties.getProperty(arg0);
		if (value != null)
			return value;
		return super.resolveSystemProperty(arg0);
	}

}
