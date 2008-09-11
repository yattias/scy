package eu.scy.lab.client.desktop.tasks;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;

public class Tasks {

	Panel panel;

	public Tasks() {
		panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(15);
		panel.setCollapsible(true);
		panel.setTitle("Tasks");

		Panel subPanel = new Panel();

		VerticalPanel verticalPanel = new VerticalPanel();

		Label lb1 = new Label("Homeworks");
		Label lb2 = new Label("get new costume");
		Label lb3 = new Label("Fix bike");
		Label lb4 = new Label("Save the world");

		Button addTask = new Button("Add Task...");

		verticalPanel.add(lb1);
		verticalPanel.add(lb2);
		verticalPanel.add(lb3);
		verticalPanel.add(lb4);

		subPanel.add(verticalPanel);
		panel.add(subPanel);
		panel.add(addTask);
		panel.collapse();

	}

	public Panel getPanel() {
		return this.panel;
	}

}