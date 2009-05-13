package eu.scy.lab.client.usermanagement;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.FxConfig;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

import eu.scy.lab.client.UserManagement;
import eu.scy.lab.client.UserManagementAsync;
import eu.scy.lab.client.login.Login;

public class ButtonListenerAdapterImpl extends ButtonListenerAdapter {

	private String buttonName;
	private CreateUser createUserPanel;

	public ButtonListenerAdapterImpl(CreateUser createUserPanel,
			String buttonName) {
		this.createUserPanel = createUserPanel;
		this.buttonName = buttonName;
	}

	public void onClick(Button button, EventObject e) {
		if (buttonName.equals("register")) {
			register();
		} else if (buttonName.equals("back")) {
			back();
		}
	}

	public void back() {

		// Fading
		Function callback = new Function() {
			public void execute() {
				FxConfig configFadeIn = new FxConfig();
				configFadeIn.setDuration((float) 0.25);
				configFadeIn.setEndOpacity(1);
				RootPanel.get().clear();
				Login loginPanel = new Login();
				RootPanel.get().add(loginPanel.getCentredLoginDialog());
				Ext.getBody().fadeIn(configFadeIn);
			}
		};
		FxConfig configFadeOut = new FxConfig();
		configFadeOut.setDuration((float) 0.25);
		configFadeOut.setEndOpacity(0);
		configFadeOut.setCallback(callback);
		Ext.getBody().fadeOut(configFadeOut);

	}

	public void register() {
		UserManagementAsync userManagement = UserManagement.Util.getInstance();
		final String username = createUserPanel.getUsername();
		final String password = createUserPanel.getPassword();
		userManagement.register(username, password, createUserPanel
				.getUserTitle(), createUserPanel.getFirstName(),
				createUserPanel.getLastName(),
				createUserPanel.getDateOfBirth(), createUserPanel.getEmail(),
				new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						MessageBoxConfig mbc = new MessageBoxConfig();
						mbc.setTitle("System Failure"); // TODO: i18n
						mbc.setMsg(caught.getLocalizedMessage());
						mbc.setIconCls(MessageBox.ERROR);
						mbc.setButtons(MessageBox.OK);
						MessageBox.show(mbc);
					}

					public void onSuccess(Boolean result) {
						if (result) {
							// Fading
							Function callback = new Function() {

								public void execute() {
									FxConfig configFadeIn = new FxConfig();
									configFadeIn.setDuration((float) 0.25);
									configFadeIn.setEndOpacity(1);
									RootPanel.get().clear();
									Login loginPanel = new Login(username,
											password);
									RootPanel.get().add(
											loginPanel.getCentredLoginDialog());
									Ext.getBody().fadeIn(configFadeIn);
								}
							};
							FxConfig configFadeOut = new FxConfig();
							configFadeOut.setDuration((float) 0.25);
							configFadeOut.setEndOpacity(0);
							configFadeOut.setCallback(callback);
							Ext.getBody().fadeOut(configFadeOut);
						}
					}
				});

	}

}
