/**
 * 
 */
package eu.scy.lab.client.usermanagement;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Sven
 *
 */
public class CreateUser {
	
	public CreateUser(){
		//new Login Panel
		CreateUserPanel createUserPanel = new CreateUserPanel();
		
		//Positioning in Center
		//wrap Login-Panel in vertical Panel
		VerticalPanel verticalPanel  = new VerticalPanel();
		verticalPanel.setWidth("100%");
		verticalPanel.setHeight("100%");
		verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(createUserPanel);
		
		//adding verticalPanel to RootPanel, horizontalPanel to verticalPanel, LoginPanel to horizontalPanel
		RootPanel.get().add(verticalPanel);
		verticalPanel.add(horizontalPanel);
	}

}
