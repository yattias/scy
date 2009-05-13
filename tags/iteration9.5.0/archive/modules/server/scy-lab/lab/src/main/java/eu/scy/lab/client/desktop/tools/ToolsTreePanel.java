package eu.scy.lab.client.desktop.tools;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

import eu.scy.lab.client.desktop.Desktop;
import eu.scy.tools.co2sim.client.CO2Sim;
import eu.scy.tools.map.client.MapTool;
import eu.scy.tools.simquestviewer.client.SimQuestViewer;
import eu.scy.tools.youtube.client.VideoInput;

/**
 * Tree of tools.
 * Add new Tools here using {@link ToolsTreeNode}
 */
class ToolsTreePanel extends TreePanel {
    
    
    public ToolsTreePanel(final Desktop desktop) {
        setTitle("Tools");
        setRootVisible(false);
        
        TreeNode root = new TreeNode("SCY Toolbox");
        root.setExpanded(true);
        
//        drawing.appendChild( new ToolsTreeNode("Drawing Tool", DrawingTool.DRAWINGTOOL_ID, desktop) {
//            protected Widget createTool() {
//                return new DrawingTool();
//            }
//        });

// Old code without gwt-ai
//                    Panel panel = new Panel("Draw Tool");
//                    panel.setClosable(true);
//                    panel.setId(DRAWTOOL_ID);
//                    panel.setHtml("<applet archive=\"applet/libs/clientDrawTool.jar,applet/libs/appFramework-1.0.jar,applet/libs/spring.jar,applet/libs/commons-logging-1.1.1.jar,applet/libs/jdom-1.1.jar,applet/libs/whiteboard.jar,applet/libs/roolo-api.jar,applet/libs/roolo-mock.jar\" code=\"eu.scy.client.tools.drawing.DrawingApplet\" width=\"600\" height=\"500\"></applet>");
//                    desktop.getWorkspace().add(panel);
        
        
        TreeNode simulation = new TreeNode("Simulation");
        simulation.setExpanded(true);
        root.appendChild(simulation);
        
        simulation.appendChild(new ToolsTreeNode("CO2-Simulation", CO2Sim.TOOL_ID, desktop) {
            protected Widget createTool() {
                return new CO2Sim();
            }
        });
        
        simulation.appendChild(new ToolsTreeNode("SimQuestViewer", SimQuestViewer.SIMQUESTVIEWER_ID, desktop) {
            protected Widget createTool() {
                return new SimQuestViewer();
            }
        });
        
        root.appendChild(new ToolsTreeNode("Map", MapTool.TOOL_ID, desktop) {
            protected Widget createTool() {
                return new MapTool();
            }
        });
        
        root.appendChild(new ToolsTreeNode("Video", VideoInput.TOOL_ID, desktop) {
            protected Widget createTool() {
                return new VideoInput();
            }
        });

        setRootNode(root);
    }
}