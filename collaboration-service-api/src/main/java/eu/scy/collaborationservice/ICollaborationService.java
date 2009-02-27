package eu.scy.collaborationservice;



public interface ICollaborationService {

    void actionUponWrite(String username);
	void actionUponDelete(String username);
	public String write(String username, String status);
	 
}
