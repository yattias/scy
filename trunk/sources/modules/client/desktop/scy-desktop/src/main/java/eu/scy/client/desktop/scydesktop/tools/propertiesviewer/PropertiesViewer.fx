/*
 * PropertiesViewer.fx
 *
 * Created on 16-jan-2010, 12:59:52
 */

package eu.scy.client.desktop.scydesktop.tools.propertiesviewer;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextBox;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import java.lang.Runtime;

/**
 * @author sikken
 */

// place your code here
public class PropertiesViewer extends CustomNode, ScyToolFX {
   
   public var scyWindow:ScyWindow;


   def valueOffset = 70.0;
   def spacing = 3.0;
   def border = 5.0;

   var cacheCheckbox:CheckBox;
   var layoutXValue:TextBox;
   var layoutYValue:TextBox;

//   def cacheProp = bind cacheCheckbox.selected on replace {cacheChanged(scyWindow)};
//
//   function cacheChanged(node: Node):Void{
//      println("changed cache of {node.getClass()} from {node.cache} to {cacheProp}");
//      node.cache = cacheProp;
//      if (node instanceof Group){
//         var group = node as Group;
//         for (subNode in group.content){
//            cacheChanged(subNode);
//         }
//      }
//   }


   public override function create(): Node {
      var propertiesDisplay = createPropertiesDisplay();
      return Group {
                 content: [
                    Rectangle {
                        x: 0, y: 0
                        width: propertiesDisplay.layoutBounds.width + 2*border,
                        height: propertiesDisplay.layoutBounds.height+2*border-spacing
                        fill: Color.YELLOW
                     }
                    propertiesDisplay
                 ]
              };
   }

   function createPropertiesDisplay(): Node{
      def window = scyWindow;
      println("window: {window}");
      VBox{
         layoutX:border;
         layoutY:border;
         spacing:spacing
         content:[
            Group{
               content:[
                  Label {
                     text: "cache"
                  }
                  cacheCheckbox = CheckBox {
                     layoutX:valueOffset;
                     text: ""
                     allowTriState: false
                     selected: bind window.cache with inverse
                  }
               ]
            }
            Group{
               content:[
                  Label {
                     text: "layoutX"
                  }
                  layoutXValue = TextBox {
                     layoutX:valueOffset;
                     text: bind "{window.layoutX}"
                     columns: 12
                     selectOnFocus: true
                     editable:false
                  }
               ]
            }
            Group{
               content:[
                  Label {
                     text: "layoutY"
                  }
                  layoutYValue = TextBox {
                     layoutX:valueOffset;
                     text:  bind "{window.layoutY}"
                     columns: 12
                     selectOnFocus: true
                     editable:false
                  }
               ]
            }
            Button {
               text: "GC"
               action: function() {
                  Runtime.getRuntime().gc();
               }
            }
         ]
      }

   }

}

function run(){
   Stage {
	title : "Test properties viewer"
	scene: Scene {
		width: 200
		height: 200
		content: [
         PropertiesViewer{
            
         }

      ]
	}
}

}
