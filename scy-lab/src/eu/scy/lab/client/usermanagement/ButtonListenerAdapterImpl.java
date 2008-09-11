package eu.scy.lab.client.usermanagement;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.FxConfig;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

import eu.scy.lab.client.SCYLab;
import eu.scy.lab.client.login.Login;
import eu.scy.lab.client.usermanagement.CreateUser;

public class ButtonListenerAdapterImpl extends ButtonListenerAdapter {
	
	private String buttonName;
	
	public ButtonListenerAdapterImpl(String buttonName){
	
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

		//Fading
		Function callback = new Function(){
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
//		Function callback = new Function(){
//			public void execute() {
//				FxConfig configFadeIn = new FxConfig();
//				configFadeIn.setDuration((float) 0.1);
//				configFadeIn.setEndOpacity(1);
//				RootPanel.get().clear();
//				RootPanel.get().add(new CreateUser().getCentredCreateUserDialog());
//				Ext.getBody().fadeIn(configFadeIn);
//			}
//		};
//		FxConfig configFadeOut = new FxConfig();
//		configFadeOut.setDuration((float) 0.1);
//		configFadeOut.setEndOpacity(0);
//		configFadeOut.setCallback(callback);
//		Ext.getBody().fadeOut(configFadeOut);
		Window.alert("register gedrückt");
	}

	
}
