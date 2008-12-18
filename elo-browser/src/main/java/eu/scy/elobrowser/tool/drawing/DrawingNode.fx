/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.elobrowser.tool.drawing;

import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.drawing.EloDrawingActionWrapper;
import java.awt.Dimension;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * @author sikkenj
 */

 // place your code here
public class DrawingNode extends CustomNode {
   var whiteboardPanel:WhiteboardPanel;
   var eloDrawingActionWrapper:EloDrawingActionWrapper;


   public override function create(): Node {
      return Group {
         content: [SwingComponent.wrap(whiteboardPanel)]
      };
   }
}

public function createDrawingNode():DrawingNode{
   var whiteboardPanel= new WhiteboardPanel();
   whiteboardPanel.setPreferredSize(new Dimension(2000,2000));
   var eloDrawingActionWrapper= new EloDrawingActionWrapper(whiteboardPanel);
   return DrawingNode{
      whiteboardPanel:whiteboardPanel;
      eloDrawingActionWrapper:eloDrawingActionWrapper;
   }
}

