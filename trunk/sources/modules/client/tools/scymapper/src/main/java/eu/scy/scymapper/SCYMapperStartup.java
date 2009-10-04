package eu.scy.scymapper;

import eu.scy.scymapper.impl.SCYMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import java.security.AccessControlException;
import java.io.FileNotFoundException;

/**
 * User: Bjoerge Naess
 * Date: 01.okt.2009
 * Time: 19:53:10
 */
public class SCYMapperStartup {
	private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/scymapper/rooloConfig.xml";
	private ApplicationContext appContext;

	public static void main(String[] args) {
		new SCYMapperStartup();
	}

	public SCYMapperStartup() {

		appContext = new ClassPathXmlApplicationContext(CONTEXT_CONFIG_CLASS_PATH_LOCATION);

		IRepository repo = (IRepository) appContext.getBean("repository");
		IELOFactory eloFactory = (IELOFactory) appContext.getBean("eloFactory");
		IMetadataTypeManager metadataTypeManager = (IMetadataTypeManager) appContext.getBean("metadataTypeManager");

		SCYMapper scyMapper = SCYMapper.getInstance();

		scyMapper.setRepository(repo);
		scyMapper.setEloFactory(eloFactory);
		scyMapper.setMetadataTypeManager(metadataTypeManager);
		scyMapper.setVisible(true);
	}
}
