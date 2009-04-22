package eu.scy.lab.client.login;

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
import eu.scy.lab.client.startupview.StartupView;
import eu.scy.lab.client.usermanagement.CreateUser;
import eu.scy.lab.client.usermanagement.User;

public class ButtonListenerAdapterImpl extends ButtonListenerAdapter {
    
    private String buttonName;
    private Login loginPanel;
    
    public ButtonListenerAdapterImpl(Login loginPanel, String buttonName) {
        this.loginPanel = loginPanel;
        this.buttonName = buttonName;
    }
    
    public void onClick(Button button, EventObject e) {
        if (buttonName.equals("login")) {
            login();
        } else if (buttonName.equals("register")) {
            register();
        } else if (buttonName.equals("passwordForgotten")) {
            passwordForgotten();
        }
    }
    
    public void login() {
        UserManagementAsync userManagement = UserManagement.Util.getInstance();
        userManagement.login(loginPanel.getUsername(), loginPanel.getPassword(), new AsyncCallback<Boolean>() {
            
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
                    User.getInstance().setUsername(loginPanel.getUsername());
                    Function callback = new Function() {
                        
                        public void execute() {
                            FxConfig configFadeIn = new FxConfig();
                            configFadeIn.setDuration((float) 0.25);
                            configFadeIn.setEndOpacity(1);
                            RootPanel.get().clear();
                            
                            RootPanel.get().add(new StartupView().getCentredPanel());
                            // TODO integrate this in StartupView
                            // RootPanel.get().add(new
                            // Desktop().createDesktop());
                            Ext.getBody().fadeIn(configFadeIn);
                        }
                    };
                    FxConfig configFadeOut = new FxConfig();
                    configFadeOut.setDuration((float) 0.25);
                    configFadeOut.setEndOpacity(0);
                    configFadeOut.setCallback(callback);
                    Ext.getBody().fadeOut(configFadeOut);
                } else {
                    MessageBoxConfig mbc = new MessageBoxConfig();
                    mbc.setTitle("Login Failure"); // TODO: i18n
                    mbc.setMsg("The username and/or password is wrong! Please try again.");
                    mbc.setIconCls(MessageBox.ERROR);
                    mbc.setButtons(MessageBox.OK);
                    MessageBox.show(mbc);
                }
            }
        });
    }
    
    public void register() {
        Function callback = new Function() {
            
            public void execute() {
                FxConfig configFadeIn = new FxConfig();
                configFadeIn.setDuration((float) 0.25);
                configFadeIn.setEndOpacity(1);
                RootPanel.get().clear();
                RootPanel.get().add(new CreateUser().getCentredCreateUserDialog());
                Ext.getBody().fadeIn(configFadeIn);
            }
        };
        FxConfig configFadeOut = new FxConfig();
        configFadeOut.setDuration((float) 0.25);
        configFadeOut.setEndOpacity(0);
        configFadeOut.setCallback(callback);
        Ext.getBody().fadeOut(configFadeOut);
    }
    
    public void passwordForgotten() {
        // TODO Auto-generated method stub
        
    }
}
