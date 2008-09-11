package eu.scy.lab.client.desktop;

import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AccordionLayout;

import eu.scy.lab.client.desktop.buddies.Buddies;
import eu.scy.lab.client.desktop.tasks.Tasks;
import eu.scy.lab.client.desktop.tools.ToolsTreeNavigation;
import eu.scy.lab.client.desktop.workspace.TabbedWorkspace;

public class DesktopAccordion extends VerticalPanel {

	private HorizontalSplitPanel mainPanel;

	private VerticalPanel navigation;

	private VerticalPanel workspace;

	public DesktopAccordion() {

		RootPanel.get().clear();

		Panel accordionPanel = createAccordionPanel();
		accordionPanel.setTitle("SCY-Lab");
		accordionPanel.setHeight(250);  
		accordionPanel.setWidth(200);  

		mainPanel = new HorizontalSplitPanel();
		navigation = createNavigation();
		workspace = createWorkspace();
		mainPanel.setSize("100%", "100%");
		mainPanel.setSplitPosition("210px");
		// css-style doesnt exist
		mainPanel.setLeftWidget(accordionPanel);
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

	public DesktopAccordion createDesktop() {
		return this;
	}

	private Panel createAccordionPanel() {
		
		ToolsTreeNavigation tools = new ToolsTreeNavigation();
		Buddies buddies = new Buddies();
		Tasks tasks = new Tasks();
		
		Panel accordionPanel = new Panel();
		accordionPanel.setLayout(new AccordionLayout(true));

		Panel panelOne = new Panel("Tools", "");
		panelOne.setIconCls("settings-icon");
		accordionPanel.add(panelOne);
		panelOne.add(tools.getPanel());

		Panel panelTwo = new Panel("Buddies", "");
		panelTwo.setIconCls("folder-icon");
		accordionPanel.add(panelTwo);
		panelTwo.add(buddies.getPanel());
	

		Panel panelThree = new Panel("Tasks", "");
		panelThree.setIconCls("user-add-icon");
		accordionPanel.add(panelThree);
		panelThree.add(tasks.getPanel());

		return accordionPanel;
	}

}
