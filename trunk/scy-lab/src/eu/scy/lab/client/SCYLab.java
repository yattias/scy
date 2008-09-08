/**
 * 
 */
package eu.scy.lab.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

/**
 * @author Sven
 * 
 */
public class SCYLab implements EntryPoint {

	public void onModuleLoad() {
		// TODO Auto-generated method stub
		Window.alert("Test");

		FormPanel p = new FormPanel();
		TextField field = new TextField("Username", "username");
		p.add(field);
		field = new TextField("Surename", "surename");
		p.add(field);
		field = new TextField("EMail", "email");
		p.add(field);

		RootPanel.get().add(p);
	}

}
