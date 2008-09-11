/**
 * 
 */
package eu.scy.lab.client.login;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

/**
 * @author Sven Manske
 * 
 */
public class Login extends FormPanel {

	private Button login;
	private Button register;
	private Button passwordForgotten;
	private LoginConstants constants;

	public Login() {
		// Localization
		constants = (LoginConstants) GWT.create(LoginConstants.class);
		createFields();
		createButtons();
	}

	public void createFields() {

		setFrame(true);
		setTitle(constants.loginTitle());

		TextField username = new TextField(constants.userName(), "username");
		username.setAllowBlank(false);
		add(username);

		// FIXME KeyLister doesnt work
//		int keyCode=112;
		
		username.addListener(new TextFieldListenerAdapter(){
			
		});

		TextField password = new TextField(constants.password(), "password");
		password.setAllowBlank(false);
		password.setInputType("password");
		add(password);
		
	}

	public void createButtons() {

		// Listener-Adapter for the Buttons
		// TODO Insert real actions

		login = new Button(constants.login(), new ButtonListenerAdapterImpl("login"));
		register = new Button(constants.register(), new ButtonListenerAdapterImpl("register"));
		passwordForgotten = new Button(constants.pwForgotten(), new ButtonListenerAdapterImpl("passwordForgotten"));

		// storing Buttons in Array to set them as FormPanel-Buttons
		Button[] buttons = new Button[3];
		buttons[0] = login;
		buttons[1] = register;
		buttons[2] = passwordForgotten;

		setButtons(buttons);
		// setMinButtonWidth(50);
		// setDraggable(true);

		setShadow(true);
		setPaddings(5);
		
	}
	
	public VerticalPanel getCentredLoginDialog(){
		
		//Positioning in Center
		//wrap Login-Panel in vertical Panel
		VerticalPanel verticalPanel  = new VerticalPanel();
		verticalPanel.setWidth("100%");
		verticalPanel.setHeight("100%");
		verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.add(this);
		
		return verticalPanel;
	}

	
}
