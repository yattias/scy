package eu.scy.scymapper;

import eu.scy.scymapper.impl.SCYMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 * User: Bjoerge Naess
 * Date: 01.okt.2009
 * Time: 19:53:10
 */
public class SCYMapperStartup {

	public static void main(String[] args) {
		new SCYMapperStartup();
	}

	public SCYMapperStartup() {
		BeanFactory factory = new XmlBeanFactory(new UrlResource(getClass().getResource("rooloConfig.xml")));

		IRepository repo = (IRepository) factory.getBean("repository");
		IELOFactory eloFactory = (IELOFactory) factory.getBean("eloFactory");
		IMetadataTypeManager metadataTypeManager = (IMetadataTypeManager) factory.getBean("metadataTypeManager");

		SCYMapper scyMapper = SCYMapper.getInstance();
		scyMapper.setRepository(repo);
		scyMapper.setEloFactory(eloFactory);
		scyMapper.setMetadataTypeManager(metadataTypeManager);
		scyMapper.setVisible(true);
	}
}
