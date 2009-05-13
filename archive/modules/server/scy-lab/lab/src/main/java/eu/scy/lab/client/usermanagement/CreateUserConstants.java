package eu.scy.lab.client.usermanagement;

/**
 * Interface to represent the constants contained in resource bundle:
 * /src/eu/scy/login/LoginConstants.properties'.
 */
public interface CreateUserConstants extends
		com.google.gwt.i18n.client.Constants {

	/**
	 * Translated "LoginConstants".
	 * 
	 * @return translated "LoginConstants"
	 */
	@DefaultStringValue("LoginConstants")
	String username();

	String password();

	String reEnterPassword();

	String title();

	String firstName();

	String surName();

	String lastName();

	String email();

	String birth();

	String register();

	String createUserTitle();

	String today();

	String logindata();

	String userdata();

	String selectTitle();

	String titleMale();

	String titleFemale();
	
	String loading();
	
	String other();

	String back();
}
