package eu.scy.agents.agenda;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.agenda.evaluation.ActivityFinishedEvaluationAgent;
import eu.scy.agents.agenda.evaluation.ActivityModifiedEvaluationAgent;
import eu.scy.agents.agenda.guidance.PedagogicalGuidanceAgent;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class AgendaStarter {

//	private static final String HOST = "localhost";
	private static final String HOST = "scy.collide.info";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {

        try {
        	Logger rootLogger = Logger.getRootLogger();
        	rootLogger.setLevel(Level.DEBUG);
        	PatternLayout layout = new PatternLayout( "%d{ISO8601} %-5p [%t] %c: %m%n" );
        	FileAppender fileAppender = new FileAppender(layout, "pga.log");
        	rootLogger.addAppender(fileAppender);
        	
        	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/beans.xml");
//            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("resources/beans.xml");
            IMetadataTypeManager metadataTypeManager = (IMetadataTypeManager) context.getBean("metadataTypeManager");
            IRepository repository = (IRepository) context.getBean("repository");

        	// Activity Finished
        	HashMap<String, Object> afeMap = new HashMap<String, Object>();
        	afeMap.put(AgentProtocol.PARAM_AGENT_ID, "activity finished evaluation id");
        	afeMap.put(AgentProtocol.TS_HOST, HOST);
        	afeMap.put(AgentProtocol.TS_PORT, 2525);
        	ActivityFinishedEvaluationAgent afeAgent = new ActivityFinishedEvaluationAgent(afeMap);

        	// Activity Modified
			HashMap<String, Object> ameMap = new HashMap<String, Object>();
			ameMap.put(AgentProtocol.PARAM_AGENT_ID, "activity modified evaluation id");
			ameMap.put(AgentProtocol.TS_HOST, HOST);
			ameMap.put(AgentProtocol.TS_PORT, 2525);
			ActivityModifiedEvaluationAgent ameAgent = new ActivityModifiedEvaluationAgent(ameMap);
			
			// PGA
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(AgentProtocol.PARAM_AGENT_ID, "pedagogical guidance id");
			map.put(AgentProtocol.TS_HOST, HOST);
			map.put(AgentProtocol.TS_PORT, 2525);
			PedagogicalGuidanceAgent pgaAgent = new PedagogicalGuidanceAgent(map);
			pgaAgent.setMetadataTypeManager(metadataTypeManager);
			pgaAgent.setRepository(repository);

			// Start
			pgaAgent.start();
			afeAgent.start();
			ameAgent.start();
		} catch (AgentLifecycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
