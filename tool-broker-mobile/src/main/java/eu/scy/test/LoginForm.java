package eu.scy.test;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;

/**
 * Created: 28.jan.2009 11:02:55
 *
 * @author Bjørge
 */
public class LoginForm {
	private TextField usernameField;
	private TextField passwordField;
	private Form theForm;

	public LoginForm() {
		usernameField = new TextField("Username", "", 0, TextField.PLAIN);
		passwordField = new TextField("Password", "", 0, TextField.PASSWORD);

		theForm = new Form("Login", new Item[] {usernameField, passwordField});
	}

	public TextField getUsernameField() {
		return usernameField;
	}

	public TextField getPasswordField() {
		return passwordField;
	}

	public Form getTheForm() {
		return theForm;
	}
}
