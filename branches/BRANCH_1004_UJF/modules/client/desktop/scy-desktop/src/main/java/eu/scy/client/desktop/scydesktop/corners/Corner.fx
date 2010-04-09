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
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

public abstract class Corner extends CustomNode {
   public var content: Node on replace {newContent()};
   public var color:Paint;
   public var backgroundColor:Paint=Color.WHITE;
//   public var backgroundColor:Paint=Color.BLACK;
   public var resizeable:Boolean = true;

   protected var background:Group = Group{
           content: [],
           onMouseClicked:function(evt){
               if (evt.clickCount==2) {
                   println("doubleclick");
                       if (resizeable){
                            resizeContent();
                       }
               }
           }
      };

      protected var border:Group = Group{
           content: [],
           onMouseClicked:function(evt){
               if (evt.clickCount==2) {
                   println("border-doubleclick");
               }
           },
           blocksMouse:true;
      };

   protected var width = 20.0;
   protected var height = 20.0;
   
   protected def radius = 10.0;
   protected def strokeWidth = 3.0;
   def emptyBorderWidth = 5.0;
   def sceneWidth = bind scene.width on replace{placeInCorner()};
   def sceneHeight = bind scene.height on replace{placeInCorner()};

   def contentGroup = Group{
           blocksMouse:true
   };

   public override function create(): Node {
      newContent();
      return Group {
         content: [
//            getBackground(),
            contentGroup,
//            getBorder(),
         ]
      };
   }

   function resize(){
      var newWidth = 2*emptyBorderWidth;
      var newHeight = 2*emptyBorderWidth;
      if (content!=null){
         newWidth += content.layoutBounds.maxX;
         newHeight += content.layoutBounds.maxY;
         content.translateX = emptyBorderWidth;
         content.translateY = emptyBorderWidth;
      }
      width = newWidth;
      height = newHeight;
//      println("{this.getClass()}.resize(), width:{width} * height:{height}, clb:{content.layoutBounds}");
      placeInCorner();
   }

   function newContent(){
      delete contentGroup.content;
      if (content!=null){
         insert content into contentGroup.content;
         contentGroup.translateX -= content.boundsInLocal.minX;
         contentGroup.translateY -= content.boundsInLocal.minY;
      }

//      resize();
      // the resize has to be delayed, otherwise a wrong height (and width?) is used
      // it could have something to do, with not yet "layed out" or so
      FX.deferAction(resize);
   }




   protected function placeInCorner(){
   }

    protected abstract function resizeContent():Void;


//   protected abstract function getBorder():Group;
   protected function getBorder():Node{
       return border
       }
   

   protected function getBackground():Node{
       return background
       }
 


   
//   protected abstract function getBackground():Group;
}

