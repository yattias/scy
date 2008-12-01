package eu.scy.lab.client.desktop.tools;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

import eu.scy.lab.client.desktop.Desktop;
import eu.scy.tools.co2sim.client.CO2Sim;
import eu.scy.tools.drawing.client.DrawingTool;
import eu.scy.tools.map.client.MapTool;
import eu.scy.tools.youtube.client.VideoInput;

class Tools extends TreePanel {
    
    
    public Tools(final Desktop desktop) {
        
        TreeNode root = new TreeNode("SCY Toolbox");
        
        TreeNode drawing = new TreeNode("Drawing");
        drawing.setExpanded(true);
        
        TreeNode drawingTool = new TreeNode("Drawing Tool");
        drawingTool.addListener(new TreeNodeListenerAdapter() {
            
            public void onDblClick(Node node, EventObject e) {
                if (!desktop.getWorkspace().containsComponentID(DrawingTool.DRAWINGTOOL_ID)) {
                    desktop.getWorkspace().add( new DrawingTool() );
//                    Panel panel = new Panel("Draw Tool");
//                    panel.setClosable(true);
//                    panel.setId(DRAWTOOL_ID);
//                    panel.setHtml("<applet archive=\"applet/libs/clientDrawTool.jar,applet/libs/appFramework-1.0.jar,applet/libs/spring.jar,applet/libs/commons-logging-1.1.1.jar,applet/libs/jdom-1.1.jar,applet/libs/whiteboard.jar,applet/libs/roolo-api.jar,applet/libs/roolo-mock.jar\" code=\"eu.scy.client.tools.drawing.DrawingApplet\" width=\"600\" height=\"500\"></applet>");
//                    desktop.getWorkspace().add(panel);
                }
                desktop.getWorkspace().activate(DrawingTool.DRAWINGTOOL_ID);
            }
        });
        drawingTool.setExpanded(true);
        
        drawing.appendChild(drawingTool);
        
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
        
        TreeNode videoTool = new TreeNode("Video");
        videoTool.addListener(new TreeNodeListenerAdapter() {
            
            public void onDblClick(Node node, EventObject e) {
                if (!desktop.getWorkspace().containsComponentID(VideoInput.TOOL_ID)) {
                    GWT.log("Activating tool: Video", null);
                    final VideoInput videoTool = new VideoInput();
                    desktop.getWorkspace().add(videoTool);
                } else {
                    GWT.log("Tool already loaded: Video", null);
                }
                desktop.getWorkspace().activate(VideoInput.TOOL_ID);
            }
        });
        
        root.appendChild(drawing);
        root.appendChild(simulation);
        root.appendChild(mapTool);
        root.appendChild(videoTool);
        
        setRootVisible(false);
        
        setTitle("Tools");
        // setWidth(200);
        // setHeight(400);
        setRootNode(root);
        root.setExpanded(true);
    }
}