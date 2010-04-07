package eu.scy.client.tools.drawing;

import java.io.FileNotFoundException;
import java.net.URL;
import java.security.AccessControlException;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;


public class SpringRoloEloDrawingPanel extends EloDrawingPanel
{
	private static final long serialVersionUID = 9216512501669020987L;
	private static final Logger logger = Logger.getLogger(SpringRoloEloDrawingPanel.class.getName());
	private final String CONTEXT_CONFIG_FILE_LOCATION = "";
	private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/client/tools/drawing/rooloConfig.xml";

	private ApplicationContext springApplicationContext;

	public void setupSpringRoolo(URL codeBase)
	{
		if (codeBase != null)
			setupServerAddress(codeBase);
		springApplicationContext = getSpringApplicationContext();
		if (springApplicationContext == null)
			throw new IllegalStateException("failed to find spring context");
		setRepository((IRepository) getSpringBean("repository"));
		setEloFactory((IELOFactory) getSpringBean("eloFactory"));
		setMetadataTypeManager((IMetadataTypeManager) getSpringBean("metadataTypeManager"));
	}

	private void setupServerAddress(URL codeBase)
	{
		logger.info("getting roolo server properties from " + codeBase);
		Properties properties = null;
		try
		{
			properties = System.getProperties();
		}
		catch (AccessControlException e)
		{
			logger.info("not allowed to modify system properties, using the static way");
			properties = MyPropertyPlaceholderConfigurer.properties;
		}
		properties.put("serverName", codeBase.getHost());
		properties.put("httpPort", "" + codeBase.getPort());
		properties.put("contextPath", getContextPath(codeBase));
	}

	private String getContextPath(URL codeBase)
	{
		String path = codeBase.getPath();
		String contextPath = path;
		int startSearchPos = 0;
		if (path.length()>0 && path.charAt(0)=='/')
			startSearchPos = 1;
		int slashPos = path.indexOf('/',startSearchPos);
		if (slashPos >= 0)
			contextPath = path.substring(0, slashPos);
		return contextPath;
	}

	protected ApplicationContext getSpringApplicationContext()
	{
		ApplicationContext springContext = null;
		try
		{
			springContext = new FileSystemXmlApplicationContext(CONTEXT_CONFIG_FILE_LOCATION);
		}
		catch (AccessControlException e)
		{
			logger.info("Could not access file " + CONTEXT_CONFIG_FILE_LOCATION
						+ ", trying on class path");
		}
		catch (BeanDefinitionStoreException e)
		{
			if (!(e.getRootCause() instanceof FileNotFoundException))
				throw e;
			logger.info("Could not find file " + CONTEXT_CONFIG_FILE_LOCATION
						+ ", trying on class path");
		}
		if (springContext == null)
			springContext = new ClassPathXmlApplicationContext(CONTEXT_CONFIG_CLASS_PATH_LOCATION);
		return springContext;
	}

	private Object getSpringBean(String name)
	{
		try
		{
			logger.info("ltrying to load bean named " + name);
			Object bean = springApplicationContext.getBean(name);
			logger.info("loaded bean named " + name);
			return bean;
		}
		catch (NoSuchBeanDefinitionException e)
		{
			throw new IllegalStateException("failed to find repository");
		}
	}

}
