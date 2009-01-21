package eu.scy;

import roolo.api.IRepository;
import roolo.api.IExtensionManager;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.notification.api.INotificationService;

/**
 * Hello world!
 *
 */
public class ToolBrokerMobileImpl implements ToolBrokerMobileAPI {

	public IRepository getRepository() {
		System.out.println("Hey! Get your hands off my repository!");
		return null;
	}

	public IMetadataTypeManager getMetaDataTypeManager() {
		System.out.println("Hey! Get your hands off my MetadataTypeManager!");
		return null;
	}

	public IExtensionManager getExtensionManager() {
		System.out.println("Hey! Get your hands off my ExtensionManager!");
		return null;
	}

	public SessionManager getUserSession(String s, String s1) {
		return null;
	}

	public IActionLogger getActionLogger() {
		return null;
	}

	public INotificationService getNotificationService() {
		return null;
	}
}
