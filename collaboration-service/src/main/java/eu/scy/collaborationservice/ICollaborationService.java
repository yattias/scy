package eu.scy.collaborationservice;

import eu.scy.core.model.impl.ScyBaseObject;


public interface ICollaborationService {
    
    public static final String SERVER_IP = "129.240.212.15"; //TODO needs to be a properties file
    public static final int SERVER_PORT = 2525; //TODO needs to be a properties file
    
    public static final String COLLABORATION_SERVICE_SPACE = "COLLABORATION_SERVICE_SPACE";
    public static final String AWARENESS_SERVICE_SPACE = "AWARENESS_SERVICE_SPACE";
    
    void actionUponWrite(String username);
	void actionUponDelete(String username);
	public String write(String username, String status);
	 
}
