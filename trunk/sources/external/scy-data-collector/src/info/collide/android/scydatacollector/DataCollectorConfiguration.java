package info.collide.android.scydatacollector;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataCollectorConfiguration {
    
    private static final String USERNAME_KEY = "username";

    private static final String PASSWORD_KEY = "password";

    private static final String GROUP_KEY = "group";

    private static final String URL_KEY = "url";

    private SharedPreferences preferences;

    private Editor editor;
    
    public DataCollectorConfiguration(Activity application) {
        preferences = application.getSharedPreferences("Configuration", Context.MODE_PRIVATE);
    }
    
    public String getUserName() {
        return preferences.getString(USERNAME_KEY, null);
    }

    public String getGroupname() {
        return preferences.getString(GROUP_KEY, null);
    }

    public String getServerUrl() {
        return preferences.getString(URL_KEY, "http://scy.collide.info:8080/roolo-ws/");
    }

    public String getPassword() {
        return preferences.getString(PASSWORD_KEY, null);
    }

    public void setUserName(String userName) {
        editor = preferences.edit();
        editor.putString(USERNAME_KEY, userName);
        editor.commit();
    }

    public void setServerUrl(String serverUrl) {
        editor = preferences.edit();
        editor.putString(URL_KEY, serverUrl);
        editor.commit();
    }

    public void setPassword(String password) {
        editor = preferences.edit();
        editor.putString(PASSWORD_KEY, password);
        editor.commit();
    }

    public void setGroupname(String groupname) {
        editor = preferences.edit();
        editor.putString(GROUP_KEY, groupname);
        editor.commit();
    }
    
    
    public boolean isComplete() {
        boolean complete = true;
        
        String serverUrl = getServerUrl();
        complete &= serverUrl != null && serverUrl.length() > 0;
        
        String userName = getUserName();
        complete &= userName != null && userName.length() > 0;
        
        String password = getPassword();
        complete &= password != null && password.length() > 0;
        
        String groupname = getGroupname();
        complete &= groupname != null && groupname.length() > 0;
        
        return complete;
    }
}
