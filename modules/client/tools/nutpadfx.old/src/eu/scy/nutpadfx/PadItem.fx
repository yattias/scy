package eu.scy.nutpadfx;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.input.MouseEvent;


/**
 * @author anthonjp
 */
 
 
 /**
  * View to display Item in Status List
  */
 
 public def height = 150;
 
 public class PadItem extends CustomNode { 
     
     public var listView : PadView;
     public var canDelete = false;
     public var onDelete: function(id : String) = null;
     public var width = 250;
     
     var text = Text {
         x: 5
         y: 5
         font: Font { name: "dialog" size: 12 }
         fill: Color.YELLOW
         content: ""
        //	 wrappingWidth: 148
         textOrigin: TextOrigin.TOP
     };
     
     var bgRect = Rectangle {
         x: 2
         y: 2
         width: bind width
         height: height
         fill: bind (if(hover) { Color.GREEN } else { Color.BLUE })
         stroke: bind (if(hover) { Color.GRAY } else { Color.BLUE })
         strokeWidth: 0.5
         arcWidth: 5
         arcHeight: 5
     }
 
     var topLine = Line {
         startX: 0
         startY: height - 1
         endX: 213
         endY: height - 1
         stroke: Color.GRAY
     };
 
     var bottomLine = Line {
         startX: 0
         startY: height
         endX: 213
         endY: height
         stroke: Color.WHITE
     };
     
     override function create():Node {
         Group { 
             content: [ 
                 bgRect, text
             ]
         };
     }
     
     override var onMousePressed = function(e : MouseEvent) {
         listView.selectedListItem = this;
     }
     
        public function setStatus(s :String) : Void {
            
            if(s == null) {
                visible = false;
                return;
            }
            
            
            text.content = trimString("{s}".trim(), 120);
            
    		text.content = "#\nTimestamp:\n Event: ------- create session -------\nSessionId: {s}\nToolId:\nContentId:\nFrom:\nTo:\nPersistenceId:\nExpiration:\n";
    						
    
            visible = true;
        }
        
        
 }
 
 // Trim the string if length is greater than specified length
 public function trimString(string:String, length:Integer) : String {
 
     if(string == null) return "";
 
     if(string.length() > length) {
         return "{string.substring(0, length).trim()}...";
     } else {
         return string;
     }
 }
