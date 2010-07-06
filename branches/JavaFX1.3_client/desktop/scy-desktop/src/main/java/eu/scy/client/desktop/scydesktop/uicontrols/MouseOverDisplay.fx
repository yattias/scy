/*
 * WindowTitleBarMouseOverDisplay.fx
 *
 * Created on 20-apr-2010, 11:06:25
 */
package eu.scy.client.desktop.scydesktop.uicontrols;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import eu.scy.client.desktop.scydesktop.art.AnimationTiming;
import javafx.animation.Interpolator;
import javafx.scene.Cursor;

/**
 * @author sikken
 */
public class MouseOverDisplay {

   public-init var createMouseOverDisplay: function(): Node;
   public-init var mySourceNode:Node;
   public-init var offsetX = 0.0;
   public-init var offsetY = 0.0;
   public-init var showImmediate = false;

   def contentGroup = Group{
      opacity:0.0
   }
   def myScene = mySourceNode.scene;

   var disappearing = false;
//   var startMillis = System.currentTimeMillis();
   def animation:Timeline = Timeline {
      repeatCount: 1
      keyFrames : [
         KeyFrame {
            time : 0s
            canSkip : true
            values: contentGroup.opacity => 0.0;
         }
         KeyFrame {
            time : AnimationTiming.startAppearingTime
            canSkip : false
            values: contentGroup.opacity => 0.0
            action:function():Void{
               displayMe();
//               println("mouse over added at {System.currentTimeMillis()-startMillis} ({animation.time}=?={AnimationTiming.startAppearingTime})")
            }
         }
         KeyFrame {
            time : AnimationTiming.fullAppearingTime
            canSkip : true
            values: contentGroup.opacity => 1.0 tween Interpolator.EASEBOTH;
            action: function():Void{
               contentGroup.cursor = Cursor.NONE;
//               println("mouse over 100 at {System.currentTimeMillis()-startMillis} ({animation.time}=?={AnimationTiming.fullAppearingTime})")
            }

         }
         KeyFrame {
            time : AnimationTiming.startDisappearingTime
            canSkip : false
            values: contentGroup.opacity => 1.0
            action: function():Void{
               disappearing = true;
//               println("mouse over start disappearing at {System.currentTimeMillis()-startMillis} ({animation.time}=?={AnimationTiming.startDisappearingTime})")
            }
         }
         KeyFrame {
            time : AnimationTiming.fullDisappearingTime
            canSkip : false
            values: contentGroup.opacity => 0.0 tween Interpolator.EASEBOTH;
             action: function():Void{
                removeMe();
//              println("mouse over removed at {System.currentTimeMillis()-startMillis} ({animation.time}=?={AnimationTiming.fullDisappearingTime})")
            }
        }
      ]
   };

   init{
      if (showImmediate){
          displayMe();
          contentGroup.opacity = 1.0;
      }
      else{
         animation.play();
      }
   }

   function displayMe():Void{
      contentGroup.content = createMouseOverDisplay();
      var sceneContent = myScene.content;
      insert contentGroup into sceneContent;
      myScene.content = sceneContent;
      positionMe();
   }

   function positionMe(){
      contentGroup.rotate = getMySourceNodeRotation();
      var mySourceScenePoint = mySourceNode.localToScene(mySourceNode.layoutBounds.minX, mySourceNode.layoutBounds.minY);
      var myScenePoint = contentGroup.localToScene(contentGroup.layoutBounds.minX-offsetX, contentGroup.layoutBounds.minY-offsetX);
      contentGroup.layoutX = mySourceScenePoint.x - myScenePoint.x;
      contentGroup.layoutY = mySourceScenePoint.y - myScenePoint.y;
//      var myNewScenePoint = contentGroup.localToScene(contentGroup.layoutBounds.minX-offsetX, contentGroup.layoutBounds.minY-offsetY);
//      println("mySourceScenePoint: {mySourceScenePoint}, myScenePoint: {myScenePoint}");
//      println("after adjustment: {myNewScenePoint}");
//      println("mySourceNode.layoutX: {mySourceNode.layoutX}, mySourceNode.layoutY: {mySourceNode.layoutY}, mySourceNode.rotate: {mySourceNode.rotate}");
//      println("contentGroup.layoutX: {contentGroup.layoutX}, contentGroup.layoutY: {contentGroup.layoutY}, contentGroup.rotate: {contentGroup.rotate}");
   }

   function getMySourceNodeRotation():Number{
      var node = mySourceNode;
      var sourceNodeRoation = node.rotate;
      while (node.parent!=null){
         node = node.parent;
         sourceNodeRoation += node.rotate;
      }
      sourceNodeRoation
   }

   function removeMe():Void{
      var sceneContent = myScene.content;
      delete contentGroup from sceneContent;
      myScene.content = sceneContent;
      contentGroup.content = null;
   }

   public function stop(){
      if (animation.time<AnimationTiming.startAppearingTime){
         animation.stop();
      }
      else {
         animation.time = AnimationTiming.startDisappearingTime;
      }
   }

   public function abort(){
      animation.stop();
      if (contentGroup.content!=null){
         removeMe();
      }
   }
}
