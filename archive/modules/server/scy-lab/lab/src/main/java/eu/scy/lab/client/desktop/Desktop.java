package eu.scy.lab.client.desktop;

import com.google.gwt.user.client.History;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;

import eu.scy.lab.client.connectivity.Connectivity;
import eu.scy.lab.client.connectivity.ConnectiviySwitcher;
import eu.scy.lab.client.desktop.buddies.Buddies;
import eu.scy.lab.client.desktop.north.NorthPanel;
import eu.scy.lab.client.desktop.tasks.Tasks;
import eu.scy.lab.client.desktop.tools.ToolsPanel;
import eu.scy.lab.client.desktop.workspace.TabbedWorkspace;
import eu.scy.lab.client.desktop.workspace.elobrowser.EloBrowser;
import eu.scy.lab.client.mission.Mission;

public class Desktop extends Panel {

    private Panel navigationPanel;

    private Panel workspacePanel;

    private TabbedWorkspace workspace;

    public Desktop() {
        buildGui();
        History.newItem("desktop");
        @SuppressWarnings("unused")
        Viewport viewPort = new Viewport(this);
    }

    public Desktop(Mission mission) {
        buildGui();
        History.newItem("desktop:"+mission.getTitle());
        // TODO connect to real missions-outline
        BorderLayoutData northData = new BorderLayoutData(RegionPosition.NORTH);
        NorthPanel north = new NorthPanel(mission);
        add(north, northData);
        
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
        navigationPanel.setMonitorResize(true);

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
        
        Toolbar toolbar = new Toolbar();
        ToolbarButton modeButton = new ToolbarButton();
        modeButton.addListener( new ConnectiviySwitcher() );
        toolbar.addButton(modeButton);
        Connectivity.setModeButton(modeButton);
        
        BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
        toolbar.setHeight(25);
        add(toolbar, southData);
        
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
        setMonitorResize(true);

        // Adding the tools, buddies, etc to navigation
        ToolsPanel tools = new ToolsPanel(this);
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
        panelTools.setAutoScroll(true);
        panelTools.add(tools, new RowLayoutData());

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
        
        //FIXME Dirty workaround: scrollbars are not visible in Navigation-Panel
        panelTasks.collapse();
        panelTasks.expand();
        
        return navigationPanel;
    }
}
