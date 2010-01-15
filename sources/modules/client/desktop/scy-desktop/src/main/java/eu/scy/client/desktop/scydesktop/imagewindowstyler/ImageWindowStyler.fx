/*
 * ImageWindowStyler.fx
 *
 * Created on 14-jan-2010, 17:41:12
 */

package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;

/**
 * @author sikken
 */

// place your code here

public class ImageWindowStyler extends WindowStyler{
   public def drawingType = "scy/drawing";
   public def datasetType = "scy/dataset";
   public def simulationConfigType = "scy/simconfig";
   public def datasetProcessingType = "scy/pds";
   public def imageType = "scy/image";
   public def textType = "scy/text";
   public def mappingType = "scy/mapping";
   public def meloType = "scy/melo";
   public def urlType = "scy/url";
   public def videoType = "scy/video";
   public def interviewType = "scy/interview";
   public def xprocType = "scy/xproc";
   public def hypotheseType = "scy/xproc";
   public def modelEditorType = "scy/xproc";
   public def studentPlanningType = "scy/xproc";

   public def scyGreen = Color.web("#8db800");
   public def scyPurple = Color.web("#7243db");
   public def scyOrange = Color.web("#ff5400");
   public def scyPink = Color.web("#fb06a2");
   public def scyBlue = Color.web("#0042f1");
   public def scyMagenta = Color.web("#0ea7bf");

   def conceptMapImageSet = EloImageSet{
      name:"con_map"
   }
   def drawingImageSet = EloImageSet{
      name:"dr_tool"
   }
   def hypotheseImageSet = EloImageSet{
      name:"hypoth"
   }
   def modelEditorImageSet = EloImageSet{
      name:"mod_editor"
   }
   def simulatorImageSet = EloImageSet{
      name:"sim"
   }
   def studentPlanningToolImageSet = EloImageSet{
      name:"st_plan_tool"
   }
   def videoImageSet = EloImageSet{
      name:"video"
   }
   def webBrowserImageSet = EloImageSet{
      name:"web_br"
   }

   public override function getScyColor(type:String):Color{
      var scyColor = scyMagenta;
      if (type == drawingType)
         scyColor = scyGreen
      else if (type == datasetType)
         scyColor = scyPurple
      else if (type == simulationConfigType)
         scyColor = scyPink
      else if (type == mappingType)
         scyColor = scyBlue
      else if (type == imageType)
         scyColor = scyOrange
      else if (type == videoType)
         scyColor = scyOrange
      else if (type == meloType)
         scyColor = scyOrange
      else if (type == textType)
         scyColor = scyBlue
      else if (type == urlType)
         scyColor = scyOrange
      else if (type == urlType)
         scyColor = scyOrange
      else if (type == interviewType)
         scyColor = scyBlue
      else if (type == xprocType)
         scyColor = scyPink;
      return scyColor;
   }

   public override function getScyIconCharacter(type:String):String{
      var iconChar = "?";
      if (type == drawingType)
         iconChar = "D"
      else if (type == datasetType)
         iconChar = "V"
      else if (type == simulationConfigType)
         iconChar = "S"
      else if (type == datasetProcessingType)
         iconChar = "P"
      else if (type == textType)
         iconChar = "T"
      else if (type == imageType)
         iconChar = "I"
      else if (type == meloType)
         iconChar = "I"
      else if (type == mappingType)
         iconChar = "M"
      else if (type == urlType)
         iconChar = "W"
      else if (type == videoType)
         iconChar = "V"
      else if (type == interviewType)
         iconChar = "I"
      else if (type == xprocType)
         iconChar = "X";
      return iconChar;
   }

   public override function getScyEloIcon(type:String):EloIcon{
      var eloImageSet:EloImageSet;
      if (type == mappingType)
         eloImageSet = conceptMapImageSet
      else if (type == drawingType)
         eloImageSet = drawingImageSet
       else if (type == hypotheseType)
         eloImageSet = hypotheseImageSet
       else if (type == modelEditorType)
         eloImageSet = modelEditorImageSet
      else if (type == simulationConfigType)
         eloImageSet = simulatorImageSet
      else if (type == studentPlanningType)
         eloImageSet = studentPlanningToolImageSet
      else if (type == videoType)
         eloImageSet = videoImageSet
      else if (type == urlType)
         eloImageSet = webBrowserImageSet;
//      else if (type == textType)
//         eloImageSet = webBrowserImageSet;

      if (eloImageSet!=null){
         ImageEloIcon{
            activeImage:eloImageSet.activeImage;
            inactiveImage:eloImageSet.inactiveImage;
         }
      }
      else{
         CharacterEloIcon{
            iconCharacter:getScyIconCharacter(type);
            color:getScyColor(type)
         };
      }
   }
}
