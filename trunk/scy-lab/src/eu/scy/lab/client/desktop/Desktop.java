package eu.scy.lab.client.desktop;

import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.scy.lab.client.desktop.buddies.Buddies;
import eu.scy.lab.client.desktop.tasks.Tasks;
import eu.scy.lab.client.desktop.tools.ToolsTreeNavigation;
import eu.scy.lab.client.desktop.workspace.TabbedWorkspace;

public class Desktop extends VerticalPanel{
	
	private HorizontalSplitPanel mainPanel;
	
	private VerticalPanel navigation;
	
	private VerticalPanel workspace;
	
	public Desktop(){
		
		RootPanel.get().clear();
	
	mainPanel = new HorizontalSplitPanel();
	navigation = createNavigation();
	workspace = createWorkspace();
	mainPanel.setSize("100%", "100%");
	mainPanel.setSplitPosition("220px");
//	css-style doesnt exist
	mainPanel.setLeftWidget(navigation);
	mainPanel.setRightWidget(workspace);
	
	RootPanel.get().add(mainPanel);
		
	}
	
	private VerticalPanel createNavigation() {
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("220px");
		ToolsTreeNavigation tools = new ToolsTreeNavigation();
		Buddies buddies = new Buddies();
		Tasks tasks = new Tasks();
		panel.add(tools.getPanel());
		panel.add(buddies.getPanel());
		panel.add(tasks.getPanel());
		return panel;
	}

	private VerticalPanel createWorkspace() {
		TabbedWorkspace workspace = new TabbedWorkspace();
		VerticalPanel panel = new VerticalPanel();
		panel.add(workspace.getMainPanel());
		return panel;
	}

	public Desktop createDesktop(){
		return this;
	}
	

}

