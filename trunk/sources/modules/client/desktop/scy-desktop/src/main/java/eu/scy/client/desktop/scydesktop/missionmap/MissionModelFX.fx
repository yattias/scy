/*
 * MissionModel.fx
 *
 * Created on 19-mrt-2009, 10:51:09
 */

package eu.scy.client.desktop.scydesktop.missionmap;

import java.util.HashMap;

import java.util.List;

import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

public class MissionModelFX {
   public var anchors:AnchorFX[];
   public var activeAnchor:AnchorFX=null;
};

public function createMissionModelFX(missonModel:MissionModel): MissionModelFX{
   var anchorsFXMap = createAnchorsFXMap(missonModel.getAnchors());
   var missionModelFX = MissionModelFX{
      activeAnchor: anchorsFXMap.get(missonModel.getActiveAnchor()) as AnchorFX;
   }
   var iterator = missonModel.getAnchors().iterator();
   while (iterator.hasNext())
   {
      var anchor = iterator.next() as Anchor;
      var nextAnchorFX = anchorsFXMap.get(anchor) as AnchorFX;
      insert nextAnchorFX into missionModelFX.anchors;
   }
   return missionModelFX;
}

function createAnchorsFXMap(anchors: List): HashMap{
   var anchorsFXMap = new HashMap();
   var iterator = anchors.iterator();
   while (iterator.hasNext())
   {
      var anchor = iterator.next() as Anchor;
      var anchorFX = createAnchorFX(anchor);
      anchorsFXMap.put(anchor,anchorFX);
   }
   // now fill the nextAnchor sequence of the anchorFXs
   iterator = anchors.iterator();
   while (iterator.hasNext())
   {
      var anchor = iterator.next() as Anchor;
      var anchorFX = anchorsFXMap.get(anchor) as AnchorFX;
      var anchorFXs:AnchorFX[];
      var nextAnchorIterator = anchor.getNextAnchors().iterator();
      while (nextAnchorIterator.hasNext())
      {
         var nextAnchor = nextAnchorIterator.next() as Anchor;
         var nextAnchorFX = anchorsFXMap.get(anchor) as AnchorFX;
         insert nextAnchorFX into anchorFX.nextAnchors;
      }
   }
   return anchorsFXMap;
}

function createAnchorFX(anchor:Anchor):AnchorFX{
   AnchorFX{
      eloUri:anchor.getEloUr();
      title:anchor.getTitle();
      color:createColor(anchor.getColor());
      xPos:anchor.getXPos();
      yPos:anchor.getYPos();
      relationNames: stringListToSequence(anchor.getRelationNames());
   }
}

function createColor(color:java.awt.Color):Color{
   var red = color.getRed();
   var green = color.getGreen();
   var blue = color.getBlue();
   var transparency = color.getTransparency();
   var fxColor = Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getTransparency());
   return fxColor;
}

function stringListToSequence(list:List):String[]{
   var sequence:String[];
   var iterator = list.iterator();
   while (iterator.hasNext())
   {
      var string = iterator.next() as String;
      insert string into sequence;
   }
   return sequence;
}

