package eu.scy.lab.client.desktop;


import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;

import eu.scy.lab.client.desktop.buddies.Buddies;
import eu.scy.lab.client.desktop.tasks.Tasks;
import eu.scy.lab.client.desktop.tools.ToolsTreeNavigation;
import eu.scy.lab.client.desktop.workspace.TabbedWorkspace;
import eu.scy.lab.client.desktop.workspace.elobrowser.EloBrowser;
import eu.scy.lab.client.startupview.lastMission.Mission;

public class Desktop extends Panel {

	private Panel navigationPanel;

	private Panel workspacePanel;
	
	private TabbedWorkspace workspace;

	public Desktop() {

		setLayout(new BorderLayout());

		navigationPanel = createNavigationPanel();
		// navigationPanel.setAutoWidth(true);
		navigationPanel.setIconCls("scylogo16x16");
		navigationPanel.setTitle("SCY-Lab");
		navigationPanel.setHeight(250);
		navigationPanel.setWidth(202);
		navigationPanel.setCollapsible(true);
		navigationPanel.setAutoScroll(true);
		navigationPanel.setBorder(true);

		BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
		// westData.setSplit(true);
		// westData.setMinSize(202);
		// westData.setMaxSize(400);
		westData.setMargins(new Margins(0, 0, 5, 0));
		westData.setFloatable(true);
		add(navigationPanel, westData);

		workspacePanel = getWorkspace();
		workspacePanel.setTitle("Workspace");

		add(workspacePanel, new BorderLayoutData(RegionPosition.CENTER));

		@SuppressWarnings("unused")
		Viewport viewPort = new Viewport(this);
	}
	
	public Desktop(Mission mission) {
		this();
		
		//TODO connect to real missions-outline
		Panel panel = new Panel("Mission "+ mission.getTitle());
		panel.add(new Label("Missionsname: "+ mission.getTitle()));
		panel.add(new Label("Last Visited: "+ mission.getLastVisitedDate()));
//		workspace.setActiveTab((workspace.getItems().length-1));
		
		workspace.add(panel);
		workspace.setActiveTab(workspace.getItems().length-1);
		System.out.println(workspace.getItems().length);
		
	}

	public TabbedWorkspace getWorkspace() {
		if (workspace == null) {
			workspace = new TabbedWorkspace();
			workspace.add(new EloBrowser());
		}
		return workspace;
	}
	
	private Panel createNavigationPanel() {

		// Adding the tools, buddies, etc to navigation
		ToolsTreeNavigation tools = new ToolsTreeNavigation(this);
		Buddies buddies = new Buddies();
		Tasks tasks = new Tasks();

		// Setting the Layout of the navigation-panel content
		Panel navigationPanel = new Panel();

		navigationPanel.setLayout(new RowLayout());
		// navigationPanel.addListener(panelListener);

		// navigationPanel.setAutoScroll(true);

		// BaseItemListenerAdapter baseListener = new BaseItemListenerAdapter(){
		// public void onActivate(BaseItem item){
		// expand();
		// }
		// public void onDeactivate(BaseItem item){
		// collapse();
		// }
		// };


		// You have to set fixed width to suppress auto-resize while collapsing
		// panels
		Panel panelTools = new Panel("Tools", "");

		panelTools.setCollapsible(true);
		panelTools.setWidth(200);
		panelTools.setIconCls("settings-icon");
		navigationPanel.add(panelTools);
		panelTools.setAutoScroll(true);
		panelTools.add(tools.getPanel(), new RowLayoutData(150));

//		ListenerConfig clickListenerConfig = new ListenerConfig();
//		
//		EventManager.addListener(panelTools.getElement(), "mousedown",
//				new EventCallback() {
//			public void execute(EventObject e) {
//				Window.alert("mousedown fired");
//			}
//		}, clickListenerConfig);
		
		
		final Panel panelBuddies = new Panel("Buddies", "");
		panelBuddies.setIconCls("folder-icon");
		panelBuddies.setCollapsible(true);
		panelBuddies.setWidth(200);
//		panelBuddies.setCollapsed(true);
		navigationPanel.add(panelBuddies);
		panelBuddies.setAutoScroll(true);
		panelBuddies.add(buddies.getPanel(), new RowLayoutData(150));
		
//		panelBuddies.getTopToolbar().addEvent("click");
//		panelBuddies.addListener("mouseDown", new Function(){
//			public void execute(){
//				Window.alert("click performed");
//			}
//		});
		
//		Function function = new Function(){
//			public void execute() {
//				// TODO Auto-generated method stub
//				panelBuddies.collapse();
//				Window.alert("event fired");
//			}
//		};
//		Toolbar toolbar = new Toolbar();
//		toolbar.addFill();
//		toolbar.addListener("click", function);
//		panelBuddies.setTopToolbar(toolbar);
		
		
		

		Panel panelTasks = new Panel("Tasks", "");
		panelTasks.setIconCls("user-add-icon");
		panelTasks.setCollapsible(true);
		panelTasks.setWidth(200);
//		panelTasks.setCollapsed(true);
		navigationPanel.add(panelTasks);
		panelTasks.setAutoScroll(true);
		panelTasks.add(tasks.getPanel(), new RowLayoutData(150));

		return navigationPanel;
	}
}
