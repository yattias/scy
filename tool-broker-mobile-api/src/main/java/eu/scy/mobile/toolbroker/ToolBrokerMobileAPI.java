package eu.scy.mobile.toolbroker;

import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.model.User;

/**
 * Hello world!
 *
 */
public interface ToolBrokerMobileAPI {
	ELO getELO(int id);
	void updateELO(ELO elo);
	User getUser(int id);
}
