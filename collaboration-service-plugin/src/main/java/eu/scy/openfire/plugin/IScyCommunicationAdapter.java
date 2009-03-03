package eu.scy.openfire.plugin;

import eu.scy.core.model.impl.ScyBaseObject;



public interface IScyCommunicationAdapter {

    void actionUponWrite(String username);
	void actionUponDelete(String username);
	
    public String create(ScyBaseObject sbo);
    public ScyBaseObject read(String id);
    public String update(ScyBaseObject sbo, String id);
    public ScyBaseObject delete(String id);
    
    public void addScyCommunicationListener(IScyCommunicationListener listener);
	 
}
