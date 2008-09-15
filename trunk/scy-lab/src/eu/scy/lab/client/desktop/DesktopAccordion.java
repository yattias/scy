package eu.scy.lab.client.desktop;

import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import eu.scy.lab.client.desktop.buddies.Buddies;
import eu.scy.lab.client.desktop.tasks.Tasks;
import eu.scy.lab.client.desktop.tools.ToolsTreeNavigation;
import eu.scy.lab.client.desktop.workspace.TabbedWorkspace;

public class DesktopAccordion extends Panel {
    
    private Panel mainPanel;
    
    private Panel navigationPanel;
    
    private Panel workspacePanel;
    
    public DesktopAccordion() {
        
        setLayout(new FitLayout());
        
        mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        
        navigationPanel = createAccordionPanel();
        navigationPanel.setTitle("SCY-Lab");
        navigationPanel.setHeight(250);
        navigationPanel.setWidth(200);
        navigationPanel.setCollapsible(true);
        
        BorderLayoutData southData = new BorderLayoutData(RegionPosition.WEST);
        southData.setSplit(true);
        southData.setMinSize(175);
        southData.setMaxSize(400);
        southData.setMargins(new Margins(0, 0, 5, 0));
        mainPanel.add(navigationPanel, southData);
        
        workspacePanel = createWorkspace();
        workspacePanel.setTitle("Workspace");
        
        mainPanel.add(workspacePanel, new BorderLayoutData(RegionPosition.CENTER));
        
        add(mainPanel);
        
        Viewport viewPort = new Viewport(this);
    }
    
    private Panel createWorkspace() {
        TabbedWorkspace workspace = new TabbedWorkspace();
        Panel panel = new Panel();
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