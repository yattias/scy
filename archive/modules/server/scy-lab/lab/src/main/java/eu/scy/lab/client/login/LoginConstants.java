package eu.scy.lab.client.login;

/**
 * Interface to represent the constants contained in resource  bundle:
 * 	/src/eu/scy/login/LoginConstants.properties'.
 */
public interface LoginConstants extends com.google.gwt.i18n.client.Constants {
  
  /**
   * Translated "LoginConstants".
   * 
   * @return translated "LoginConstants"
  
   */
  @DefaultStringValue("LoginConstants")
  String password();
  String userName();
  String loginTitle();
  String login();
  String register();
  String pwForgotten();
}
