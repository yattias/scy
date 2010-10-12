
package eu.scy.mobile.toolbroker.demo.client;

import eu.scy.mobile.toolbroker.demo.SCYMobileDemo;
import eu.scy.mobile.toolbroker.demo.client.stubs.RepositoryService;
import eu.scy.mobile.toolbroker.demo.client.stubs.RepositoryService_Stub;
import eu.scy.mobile.toolbroker.demo.model.EloConverter;
import eu.scy.mobile.toolbroker.demo.model.ImageELO;

import java.rmi.RemoteException;

public class ELOUploader extends Thread {
	private SCYMobileDemo parent;
	private ImageELO elo;

	public ELOUploader(SCYMobileDemo parent, ImageELO elo) {
		this.parent = parent;
		this.elo = elo;
	}

	public void run() {
		RepositoryService repositoryService = new RepositoryService_Stub();
		SCYWebserviceClientResponse res = new SCYWebserviceClientResponse();
		try {
			res.setCode(SCYWebserviceClientResponse.RESPONSE_SUCCESS);
			res.setMessage(repositoryService.saveELO(EloConverter.convert(elo)));
		} catch (RemoteException e) {
			res.setCode(SCYWebserviceClientResponse.RESPONSE_FAULT);
			res.setMessage(e.getMessage());
		}
		parent.responseRecieved(res);
	}
}
