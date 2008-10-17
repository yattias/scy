package eu.scy.lab.client.desktop.tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

import eu.scy.lab.client.desktop.Desktop;
import eu.scy.tools.co2sim.client.CO2Sim;
import eu.scy.tools.map.client.MapTool;

class Tools extends TreePanel {
    
    protected static final String DRAWTOOL_ID = "draw-tool";
    protected static final String DRAWTOOL_PATH = "applet/drawTool.html";
    
    
    public Tools(final Desktop desktop) {
        
        TreeNode root = new TreeNode("SCY Toolbox");
        
        TreeNode drawing = new TreeNode("Drawing");
        drawing.setExpanded(true);
        
        TreeNode jDT = new TreeNode("Drawing Tool");
        jDT.addListener(new TreeNodeListenerAdapter() {
            
            public void onDblClick(Node node, EventObject e) {
                if (!desktop.getWorkspace().containsComponentID(DRAWTOOL_ID)) {
                    Panel panel = new Panel("Draw Tool");
                    panel.setClosable(true);
                    panel.setId(DRAWTOOL_ID);
                    panel.setHtml("<applet archive=\"applet/libs/clientDrawTool.jar,applet/libs/appFramework-1.0.jar,applet/libs/spring.jar,applet/libs/commons-logging-1.1.1.jar,applet/libs/jdom-1.1.jar,applet/libs/whiteboard.jar,applet/libs/roolo-api.jar,applet/libs/roolo-mock.jar\" code=\"eu.scy.client.tools.drawing.DrawingApplet\" width=\"600\" height=\"500\"></applet>");
                    desktop.getWorkspace().add(panel);
                }
                desktop.getWorkspace().activate(DRAWTOOL_ID);
            }
        });
        jDT.setExpanded(true);
        
        drawing.appendChild(jDT);
        
        TreeNode simulation = new TreeNode("Simulation");
        simulation.setExpanded(true);
        
        TreeNode co2sim = new TreeNode("CO2-Simulation");
        co2sim.addListener(new TreeNodeListenerAdapter() {
            public void onDblClick(Node node, EventObject e) {
                if (!desktop.getWorkspace().containsComponentID(CO2Sim.TOOL_ID)) {
                    desktop.getWorkspace().add( new CO2Sim() );
                }
                desktop.getWorkspace().activate(CO2Sim.TOOL_ID);
            }
        });
        co2sim.setExpanded(true);
        simulation.appendChild(co2sim);
        
        TreeNode mapTool = new TreeNode("Map");
        mapTool.addListener(new TreeNodeListenerAdapter() {
            
            public void onDblClick(Node node, EventObject e) {
                if (!desktop.getWorkspace().containsComponentID(MapTool.TOOL_ID)) {
                    GWT.log("Activating tool: MapTool", null);
                    final MapTool map = new MapTool();
                    desktop.getWorkspace().add(map);
                } else {
                    GWT.log("Tool already loaded: MapTool", null);
                }
                desktop.getWorkspace().activate(MapTool.TOOL_ID);
            }
        });
        
        root.appendChild(drawing);
        root.appendChild(simulation);
        root.appendChild(mapTool);
        
        setRootVisible(false);
        
        setTitle("Tools");
        // setWidth(200);
        // setHeight(400);
        setRootNode(root);
        root.setExpanded(true);
    }
}