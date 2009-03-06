package eu.scy.communications.adapter;

import eu.scy.communications.message.ScyMessage;


public interface IScyCommunicationAdapter {

    public void addScyCommunicationListener(IScyCommunicationListener listener);

    // callback methods
    void actionUponWrite(String username);
	void actionUponDelete(String username);
	
	// various CRUDs
    public String create(ScyMessage sm);
    public String createWithExpiration(ScyMessage sm, long expiration);
    public ScyMessage read(String id);
    public String update(ScyMessage sm, String id);
    public String updateWithExpiration(ScyMessage sm, String id, long expiration);
    public String delete(String id);
    public void sendCallBack(String something);
}
