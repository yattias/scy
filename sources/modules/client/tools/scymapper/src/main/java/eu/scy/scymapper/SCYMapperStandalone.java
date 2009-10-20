package eu.scy.scymapper;

import eu.scy.scymapper.impl.SCYMapperPanel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import javax.swing.*;
import java.awt.*;

/**
 * User: Bjoerge Naess
 * Date: 01.okt.2009
 * Time: 19:53:10
 */
public class SCYMapperStandalone extends JFrame {
	private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/scymapper/rooloConfig.xml";
	private ApplicationContext appContext;

	public static void main(String[] args) {
		new SCYMapperStandalone();
	}

	public SCYMapperStandalone() {

		appContext = new ClassPathXmlApplicationContext(CONTEXT_CONFIG_CLASS_PATH_LOCATION);

		IRepository repo = (IRepository) appContext.getBean("repository");
		IELOFactory eloFactory = (IELOFactory) appContext.getBean("eloFactory");
		IMetadataTypeManager metadataTypeManager = (IMetadataTypeManager) appContext.getBean("metadataTypeManager");

		SCYMapperPanel scyMapperPanel = new SCYMapperPanel();

		scyMapperPanel.setRepository(repo);
		scyMapperPanel.setEloFactory(eloFactory);
		scyMapperPanel.setMetadataTypeManager(metadataTypeManager);

		setLayout(new BorderLayout());
		getContentPane().add(scyMapperPanel);
//		getContentPane().add(new JButton("Hello"));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(1400, 900);

		setVisible(true);


	}
}
