/**
 * 
 */
package eu.scy.lab.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.TextField;

/**
 * @author Sven
 */
public class SCYLab implements EntryPoint {
    
    public void onModuleLoad() {
        Window p = new Window();
        p.setTitle("Login");
        
        TextField field = new TextField("Username", "username");
        p.add(field);
        field = new TextField("Surename", "surename");
        p.add(field);
        field = new TextField("EMail", "email");
        p.add(field);
        
        p.setFrame(true);
        p.setWidth(250);
        p.setFloating(true);
        
        Button[] buttons = new Button[2];
        buttons[0] = new Button("Login");
        buttons[1] = new Button("Reset");
        p.setButtons(buttons);
        
        RootPanel.get().add(p);
        
        p.show();
        p.setPosition(200, 200);
    }
}
