package eu.scy.openfire.plugin;



public interface IScyCommunicationAdapter {

    void actionUponWrite(String username);
	void actionUponDelete(String username);
	public String write(String username, String status);
	 
}
