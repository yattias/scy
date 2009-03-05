package eu.scy.openfire.plugin;

import eu.scy.core.model.impl.ScyBaseObject;



public interface IScyCommunicationAdapter {

    public void addScyCommunicationListener(IScyCommunicationListener listener);

    // callback methods
    void actionUponWrite(String username);
	void actionUponDelete(String username);
	
	// various CRUDs
    public String create(ScyBaseObject sbo);
    public String createWithExpiration(ScyBaseObject sbo, long expiration);
    public ScyBaseObject read(String id);
    public String update(ScyBaseObject sbo, String id);
    public String updateWithExpiration(ScyBaseObject sbo, String id, long expiration);
    public String delete(String id);
    public void sendCallBack(String something);
}
