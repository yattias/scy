package eu.scy.server.externalcomponents.agentframework;

import eu.scy.core.AgentService;
import info.collide.sqlspaces.commons.Configuration;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentStartConfiguration;
import eu.scy.agents.impl.manager.AgentManager;
import eu.scy.server.externalcomponents.ExternalComponentFailedException;
import eu.scy.server.externalcomponents.IExternalComponent;

public class AgentFrameworkComponent implements IExternalComponent, ApplicationContextAware, InitializingBean {

	private Logger log = Logger.getLogger("AgentFrameworkComponent.class");

	private AgentManager agentFramework;

	private Set<AgentStartConfiguration> initialAgents;

	private IRepository repository;

	private IMetadataTypeManager metadataTypeManager;

    private ApplicationContext applicationContext;

	@Override
	public void startComponent() throws ExternalComponentFailedException {
		log.info("Initializing AGENT FRAMEWORK");

		String tsHost = "localhost"; // will this ever be something different?
		int tsPort = Configuration.getConfiguration().getNonSSLPort();
		agentFramework = new AgentManager(tsHost, tsPort);
		agentFramework.setRepository(repository);
		agentFramework.setMetadataTypeManager(metadataTypeManager);

		for (AgentStartConfiguration conf : initialAgents) {
			Map<String, Object> params = conf.getProperties();
			if (!params.containsKey("tsHost")) {
				params.put("tsHost", tsHost);
			}
			if (!params.containsKey("tsPort")) {
				params.put("tsPort", tsPort);
			}
			try {
				log.info("Starting agent '" + conf.getClassName() + "'");
				agentFramework.startAgent(conf.getClassName(), params);
			} catch (AgentLifecycleException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stopComponent() throws ExternalComponentFailedException {
		log.info("-----> STOPPING: AGENT FRAMEWORK");
		agentFramework.cleanUp();
	}

	/**
	 * @return the initialAgents
	 */
	public Set<AgentStartConfiguration> getInitialAgents() {
		return initialAgents;
	}

	/**
	 * @param initialAgents the initialAgents to set
	 */
	public void setInitialAgents(Set<AgentStartConfiguration> initialAgents) {
		this.initialAgents = initialAgents;
	}

	public void setRepository(IRepository repo) {
		repository = repo;
	}

	public IMetadataTypeManager getMetadataTypeManager() {
		return metadataTypeManager;
	}

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager) {
		this.metadataTypeManager = metadataTypeManager;
	}

	public IRepository getRepository() {
		return repository;
	}

    @Override
    public void afterPropertiesSet() throws Exception {
        // log.info("INITIALIZING AGENT DATABASE. CHECKING FOR UNREGISTERED AGENTS!!");
        // AgentService agentService = (AgentService) applicationContext.getBean("agentService");
        // Iterator it = initialAgents.iterator();
        // while(it.hasNext()) {
        //     AgentStartConfiguration component = (AgentStartConfiguration) it.next();
        //     agentService.registerAgent(component.getClassName());
        // }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }
}
