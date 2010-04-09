package eu.scy.mobile.toolbroker.model.impl;

import eu.scy.mobile.toolbroker.IELOService;
import eu.scy.mobile.toolbroker.IUserService;
import eu.scy.mobile.toolbroker.IMobileToolBroker;
import eu.scy.mobile.toolbroker.client.EloServiceClient;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 11:14:27
 */
public class MobileToolBrokerImpl implements IMobileToolBroker {
	private final String ELO_SERVICE_URL = "http://129.177.24.191:9998";
    private static IELOService eloService;

    public IELOService getELOService() {
        if (eloService == null)
            eloService = new EloServiceClient(ELO_SERVICE_URL);
        return eloService;
    }

    public IUserService getUserService() {
        return null;
    }
}
