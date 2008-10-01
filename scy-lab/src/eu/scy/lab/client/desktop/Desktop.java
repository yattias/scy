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
        buildGui();
        @SuppressWarnings("unused")
        Viewport viewPort = new Viewport(this);
    }

    public Desktop(Mission mission) {
        buildGui();
        // TODO connect to real missions-outline
        Panel panel = new Panel("Mission " + mission.getTitle());
        panel.add(new Label("Missionsname: " + mission.getTitle()));
        panel.add(new Label("Last Visited: " + mission.getLastVisitedDate()));

        BorderLayoutData northData = new BorderLayoutData(RegionPosition.NORTH);
        panel.setIconCls("scylogo16x16");
        panel.setHeight(55);
        panel.setCollapsible(true);
        panel.setBorder(true);
        add(panel, northData);

        @SuppressWarnings("unused")
        Viewport viewPort = new Viewport(this);
    }

    private void buildGui() {
        setLayout(new BorderLayout());

        navigationPanel = createNavigationPanel();
        navigationPanel.setIconCls("scylogo16x16");
        navigationPanel.setTitle("SCY-Lab");
        navigationPanel.setCollapsible(true);
        navigationPanel.setAutoScroll(false);
        navigationPanel.setBorder(true);

        BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
        westData.setSplit(true);
        westData.setMinWidth(150);
        westData.setMinSize(202);
        westData.setMaxSize(400);
        westData.setMargins(new Margins(0, 0, 5, 0));
        westData.setFloatable(true);
        add(navigationPanel, westData);

        workspacePanel = getWorkspace();
        workspacePanel.setTitle("Workspace");

        add(workspacePanel, new BorderLayoutData(RegionPosition.CENTER)); 
    }

    public TabbedWorkspace getWorkspace() {
        if (workspace == null) {
            workspace = new TabbedWorkspace();
            workspace.setPaddings(0);
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
        navigationPanel.setWidth(202);
        navigationPanel.setAutoScroll(false);

        navigationPanel.setLayout(new RowLayout());

        // You have to set fixed width to suppress auto-resize while collapsing panels
        Panel panelTools = new Panel("Tools", "");

        panelTools.setCollapsible(true);
        panelTools.setIconCls("settings-icon");
        navigationPanel.add(panelTools);
        panelTools.setAutoScroll(false);
        panelTools.add(tools.getPanel(), new RowLayoutData());

        final Panel panelBuddies = new Panel("Buddies", "");
        panelBuddies.setIconCls("folder-icon");
        panelBuddies.setCollapsible(true);
        navigationPanel.add(panelBuddies);
        panelBuddies.setAutoScroll(true);
        panelBuddies.setBorder(false);
        panelBuddies.setTopToolbar(buddies.createToolbar());
        panelBuddies.add(buddies.getPanel(), new RowLayoutData());

        Panel panelTasks = new Panel("Tasks", "");
        panelTasks.setIconCls("user-add-icon");
        panelTasks.setCollapsible(true);
        panelTasks.setAutoScroll(true);
        panelTasks.setTopToolbar(tasks.getToolbar());
        navigationPanel.add(panelTasks);
        panelTasks.add(tasks.getGrid(), new RowLayoutData());
        
        return navigationPanel;
    }
}
