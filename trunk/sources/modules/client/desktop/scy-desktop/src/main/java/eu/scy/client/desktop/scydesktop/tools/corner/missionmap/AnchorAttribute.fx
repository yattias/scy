/*
 * AnchorAttribute.fx
 *
 * Created on 26-mrt-2009, 15:36:51
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import java.lang.Object;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.desktoputils.StringUtils;

/**
 * @author sikkenj
 */

public class AnchorAttribute extends ScyWindowAttribute {
   public var anchorDisplay: AnchorDisplay;
   public var missionAnchor: MissionAnchorFX;
   public var mainAnchor = true;
   public var windowAction:function(MissionAnchorFX):Void;
   public var missionModel: MissionModelFX;
   public-init var tooltipManager: TooltipManager;
   override public var priority = 5;

   def size = 10.0;
   def mainAngleWidth = 80.0;
   def secondAngleWidth = 60.0;
   def mainStrokeWidth = 2.0;
   def secondStrokeWidth = 1.0;
   def defaultTitleColor = Color.WHITE;
   def defaultContentColor = Color.GRAY;

   var titleColor = defaultTitleColor;
   var contentColor = defaultContentColor;
   def selected = bind scyWindow.activated on replace {
      setColors()
   };

   public override function clone():AnchorAttribute{
      AnchorAttribute{
         anchorDisplay : anchorDisplay
         missionAnchor: missionAnchor
         mainAnchor: mainAnchor
         windowAction: windowAction
         priority: priority
         missionModel: missionModel
         tooltipManager: tooltipManager
      }
   }

   function setColors(){
      if (selected){
         titleColor = defaultTitleColor;
         contentColor = missionAnchor.windowColorScheme.mainColor;
      }
      else {
         titleColor = missionAnchor.windowColorScheme.mainColor;
         contentColor = defaultTitleColor;
      }
   }

   public override function create(): Node {
      def eloIconName = if (mainAnchor) "elo_anchor" else "elo_intermediate";
      def eloIcon = EloIconFactory{}.createEloIcon(eloIconName);
      eloIcon.windowColorScheme = missionAnchor.windowColorScheme;
      EloIconButton{
         size: itemSize
         mouseOverSize: mouseOverItemSize
         eloIcon: eloIcon
         tooltipFunction: tooltipFunction
         tooltipManager: tooltipManager
         hideBackground: true
         actionScheme: 1

         onMouseClicked: function( e: MouseEvent ):Void {
            anchorDisplay.selectionAction(anchorDisplay,missionAnchor);
            windowAction(missionAnchor);
         },
      }
   }

   function tooltipFunction():String{
      if (missionModel.activeLas!=missionAnchor.las){
         StringUtils.putInValues(##"product of other activity: %0%\nclick to open in activity",missionAnchor.las.title)
      }
      else if (scyWindow.isClosed){
         ##"product of this activity\nclick to open and center"
      }
      else{
         ##"product of this activity\nclick to center"
      }
   }

   public  function create2(): Node {
      var mainRadius = 3*size/3;
      return Group {
         cursor: Cursor.HAND;
         blocksMouse:true
         translateY: -size - mainStrokeWidth/2;
         content: [
//            EloContour{
//               width: size;
//               height: size;
//               controlLength: 5;
//               borderWidth: 2;
//					borderColor: bind missionAnchor.color;
//					fillColor: bind contentColor;
//            }
            Rectangle {
               x: 0, y: 0
               width: size, height: size+mainStrokeWidth
               fill: Color.TRANSPARENT
            }
            Line {
               startX: size / 2,
               startY: 0
               endX: size / 2,
               endY: size
               strokeWidth: mainStrokeWidth
               stroke: missionAnchor.windowColorScheme.mainColor
            }
            Arc {
               centerX: size / 2,
               centerY: mainRadius
               radiusX: mainRadius
               radiusY: mainRadius
               startAngle: 90 - mainAngleWidth / 2,
               length: mainAngleWidth
               type: ArcType.OPEN
               fill: Color.TRANSPARENT
               stroke: missionAnchor.windowColorScheme.mainColor
               strokeWidth: mainStrokeWidth
            }
            if (mainAnchor){
               Arc {
                  centerX: size / 2,
                  centerY: mainRadius + 5
                  radiusX: mainRadius
                  radiusY: mainRadius
                  startAngle: 90 - secondAngleWidth / 2,
                  length: secondAngleWidth
                  type: ArcType.OPEN
                  fill: Color.TRANSPARENT
                  stroke: missionAnchor.windowColorScheme.mainColor
                  strokeWidth: secondStrokeWidth
               }
            }
            else{
               null;
            }

         ]
         onMouseClicked: function( e: MouseEvent ):Void {
            anchorDisplay.selectionAction(anchorDisplay,missionAnchor);
            windowAction(missionAnchor);
         },
      };
   }
}

function run() {
   var missionAnchor = MissionAnchorFX{
//         color: Color.RED
      };
   var anchor1 = AnchorDisplay{
//      anchor: MissionAnchorFX{
//         iconCharacter: "1";
//         xPos: 20;
//         yPos: 20;
//         color: Color.RED
//      }
   }

   Stage {
      title: "AnchorAttribute"
      scene: Scene {
         width: 200
         height: 200
         content: [
            Group{
               translateX: 20;
               translateY: 40;
               content: [
                  Rectangle {
                     x: 0,
                     y: 0
                     width: 100,
                     height: 20
                     fill: Color.GRAY
                  }
                  AnchorAttribute{
                     translateX: 10;
                     translateY: 0;
                     missionAnchor: missionAnchor
                     mainAnchor:false
                  }
                  AnchorAttribute{
                     translateX: 30;
                     translateY: 0;
                     missionAnchor: missionAnchor
                     mainAnchor:true
                  }
               ]
            }

         ]
      }
   }
}
