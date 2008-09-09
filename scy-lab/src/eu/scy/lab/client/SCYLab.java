/**
 * 
 */
package eu.scy.lab.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.scy.lab.client.login.Login;
import eu.scy.lab.client.usermanagement.CreateUser;

/**
 * @author Sven Manske
 * 
 */
public class SCYLab implements EntryPoint {

	public void onModuleLoad() {
		
		//new Login Panel
		Login login = new Login();
		
		//Positioning in Center
		//wrap Login-Panel in vertical Panel
		VerticalPanel verticalPanel  = new VerticalPanel();
		verticalPanel.setWidth("100%");
		verticalPanel.setHeight("100%");
		verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(login);
		
		//adding verticalPanel to RootPanel, horizontalPanel to verticalPanel, LoginPanel to horizontalPanel
		RootPanel.get().add(verticalPanel);
		verticalPanel.add(horizontalPanel);
		
		
		
	}

}
