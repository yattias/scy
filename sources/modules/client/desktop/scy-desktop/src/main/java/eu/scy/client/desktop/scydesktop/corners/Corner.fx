/*
 * Corner.fx
 *
 * Created on 30-jun-2009, 16:15:37
 */

package eu.scy.client.desktop.scydesktop.corners;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.paint.Paint;

/**
 * @author sikkenj
 */

public abstract class Corner extends CustomNode {
   public var content: Node on replace {newContent()};
   public var color:Paint;

   protected var width = 20.0;
   protected var height = 20.0;

   protected def radius = 10.0;
   protected def strokeWidth = 3.0;
   def emptyBorderWidth = 5.0;
   def sceneWidth = bind scene.width on replace{placeInCorner()};
   def sceneHeight = bind scene.height on replace{placeInCorner()};

   def contentGroup = Group{};

   public override function create(): Node {
      newContent();
      return Group {
         content: [
            contentGroup,
            getCornerElements()
         ]
      };
   }

   function resize(){
      var newWidth = 2*emptyBorderWidth;
      var newHeight = 2*emptyBorderWidth;
      if (content!=null){
         newWidth += content.layoutBounds.width;
         newHeight += content.layoutBounds.height;
         content.translateX = emptyBorderWidth;
         content.translateY = emptyBorderWidth;
      }
      width = newWidth;
      height = newHeight;
      placeInCorner();
   }

   function newContent(){
      resize();
      delete contentGroup.content;
      insert content into contentGroup.content;
   }


   protected function placeInCorner(){
   }


   protected abstract function getCornerElements():Group;
}

