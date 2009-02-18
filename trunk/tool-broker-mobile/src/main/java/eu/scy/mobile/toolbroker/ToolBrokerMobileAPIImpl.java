package eu.scy.mobile.toolbroker;

import eu.scy.mobile.toolbroker.client.EloServiceClient;
import eu.scy.mobile.toolbroker.model.ELO;
import eu.scy.mobile.toolbroker.model.User;

/**
 * Created: 13.feb.2009 12:00:17
 *
 * @author Bjørge Næss
 */
public class ToolBrokerMobileAPIImpl implements ToolBrokerMobileAPI {

	private final String ELO_SERVICE_URL = "http://scyzophrenia.ath.cx:9998";
	private final String USER_SERVICE_URL = "http://scyzophrenia.ath.cx:9998";

	private EloServiceClient eloService;

	public ToolBrokerMobileAPIImpl() {
		eloService = new EloServiceClient(ELO_SERVICE_URL);
	}

	public ELO getELO(int id) {
		System.out.println("id = " + id);
		return eloService.getELO(id);
	}
	public void updateELO(ELO elo) {
		eloService.updateELO(elo);
	}

	public User getUser(int id) {
		//return userService.getUser(id);
		return null;
	}
}
