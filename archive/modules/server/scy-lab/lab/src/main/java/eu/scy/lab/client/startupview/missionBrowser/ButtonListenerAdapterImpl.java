package eu.scy.lab.client.startupview.missionBrowser;

import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.FxConfig;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

import eu.scy.lab.client.desktop.Desktop;
import eu.scy.lab.client.mission.Mission;
import eu.scy.lab.client.mission.MissionInfoWindow;

public class ButtonListenerAdapterImpl extends ButtonListenerAdapter {

	private String buttonName;
	private MissionBrowser missionBrowser;

	public ButtonListenerAdapterImpl(MissionBrowser missionBrowser,
			String buttonName) {
		this.missionBrowser = missionBrowser;
		this.buttonName = buttonName;
	}

	public void onClick(Button button, EventObject e) {
		if (buttonName.equals("start")) {
			start();
		} else if (buttonName.equals("info")) {
			showInfo();
		}
	}

	public void start() {
		final Mission mission = missionBrowser.getActualMission();
		if (mission == null) {
			MessageBox.alert("Please select a Mission at first!");
		} else {
			// Fading
			Function callback = new Function() {
				public void execute() {
					FxConfig configFadeIn = new FxConfig();
					configFadeIn.setDuration((float) 0.25);
					configFadeIn.setEndOpacity(1);
					RootPanel.get().clear();
					RootPanel.get().add(new Desktop(mission));
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

	public void showInfo() {
		final Mission mission = missionBrowser.getActualMission();
		if (mission == null) {
			MessageBox.alert("Please select a Mission at first!");
		} else {
			MissionInfoWindow info = new MissionInfoWindow(mission);
			info.show();
		}
	}
}
