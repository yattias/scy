/**
 * 
 */
package eu.scy.lab.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import eu.scy.lab.client.login.Login;

/**
 * @author Sven Manske
 * 
 */
public class SCYLab implements EntryPoint {
	

	public void onModuleLoad() {
		
		//new Login Panel
		Login loginPanel = new Login();
		RootPanel.get().add(loginPanel.getCentredLoginDialog());
		
	}
	
	

}
