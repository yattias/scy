/**
 * 
 */
package eu.scy.lab.client.login;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

import eu.scy.lab.client.usermanagement.CreateUser;

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
		int keyCode=112;
		
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
		ButtonListenerAdapter buttonListener = new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (button.equals(login)) {
					login();
				} else if (button.equals(register)) {
					//remove child (=LoginPanel) from RootPanel
					RootPanel.get().remove(0);
					CreateUser cu = new CreateUser();
				} else if (button.equals(passwordForgotten)) {
					
				}

			}

		};

		login = new Button(constants.login(), buttonListener);
		register = new Button(constants.register(), buttonListener);
		passwordForgotten = new Button(constants.pwForgotten(), buttonListener);

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

	// TODO Implement login-procedure
	public void login() {
		Window.alert("Login durchgeführt");
	}

	// TODO Implement register-procedure
	public void register() {
		Window.alert("Login durchgeführt");
	}

	// TODO Implement passwordForgotten-procedure
	public void passwordForgotten() {
		Window.alert("Login durchgeführt");
	}
}
