/**
 * 
 */
package eu.scy.lab.client.history;

import java.util.Vector;

import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.MessageBox;

import eu.scy.lab.client.desktop.Desktop;
import eu.scy.lab.client.login.Login;
import eu.scy.lab.client.mission.Mission;
import eu.scy.lab.client.startupview.StartupView;
import eu.scy.lab.client.usermanagement.CreateUser;

/**
 * @author Sven
 * 
 */
public class HistoryListenerImpl implements HistoryListener {

    private Vector<String> initTokens;
    
    public HistoryListenerImpl(){
        initTokens = new Vector<String>();
    }

    public void onHistoryChanged(String historyToken) {
        //This is gwt-buggy. onHistoryChanged is called when new item is added to history (should only be when forward or back clicked).
        //New HistoryTokens are added to the Vector initTokens
        if (initTokens.contains(historyToken)) {
            if (historyToken.equals("initial")) {
                RootPanel.get().clear();
                Login loginPanel = new Login();
                RootPanel.get().add(loginPanel.getCentredLoginDialog());
            } else if (historyToken.equals("startup")) {
                // XXX This is only permitted if user is already logged in
                // FIXME Check if user is logged in!!!
                RootPanel.get().clear();
                StartupView startupPanel = new StartupView();
                RootPanel.get().add(startupPanel.getCentredPanel());
            } else if (historyToken.equals("register")) {
                RootPanel.get().clear();
                RootPanel.get().add(new CreateUser().getCentredCreateUserDialog());
            } else if (historyToken.startsWith("desktop:")) {
                // FIXME add error correction if mission doesnt exist
                String[] token = historyToken.split(":");
                String missionString = token[1];
                Mission mission = Mission.GetMissionByName(missionString);
                RootPanel.get().clear();
                RootPanel.get().add(new Desktop(mission));
            } else if (historyToken.startsWith("desktop")) {
                RootPanel.get().clear();
                RootPanel.get().add(new Desktop());
            } else {
                // It seems as if its not necessary to do this (changes automatically to initial)
                MessageBox.alert("wrong History-Token: state cannot be resolved");
            }
        } else {
            initTokens.add(historyToken);
        }
    }
}
