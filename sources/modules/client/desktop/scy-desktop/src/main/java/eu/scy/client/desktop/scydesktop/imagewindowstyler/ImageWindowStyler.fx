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
import javafx.scene.image.Image;

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

   public def scyGreen = Color.web("#8db800");
   public def scyPurple = Color.web("#7243db");
   public def scyOrange = Color.web("#ff5400");
   public def scyPink = Color.web("#fb06a2");
   public def scyBlue = Color.web("#0042f1");
   public def scyMagenta = Color.web("#0ea7bf");

   def conceptMapImageAct = Image{
      url:"{__DIR__}/images/con_map_inact.png"
   };
   def conceptMapImageInact = Image{
      url:"{__DIR__}/images/con_map_inact.png"
   };
   def drawingImageAct = Image{
      url:"{__DIR__}/images/dr_tool_act.png"
   };
   def drawingImageInact = Image{
      url:"{__DIR__}/images/dr_tool_inact.png"
   };
   def hypotheseImageAct = Image{
      url:"{__DIR__}/images/hypoth_act.png"
   };
   def hypotheseImageInact = Image{
      url:"{__DIR__}/images/hypoth_inact.png"
   };
   def modelEditorImageAct = Image{
      url:"{__DIR__}/images/mod_editor_act.png"
   };
   def modelEditorImageInact = Image{
      url:"{__DIR__}/images/mod_editor_inact.png"
   };
   def simulatorImageAct = Image{
      url:"{__DIR__}/images/sim_act.png"
   };
   def simulatorImageInact = Image{
      url:"{__DIR__}/images/sim_inact.png"
   };
   def studentPlanningImageAct = Image{
      url:"{__DIR__}/images/st_plan_tool_act.png"
   };
   def studentPlanningImageInact = Image{
      url:"{__DIR__}/images/st_plan_tool_inact.png"
   };
   def videoImageAct = Image{
      url:"{__DIR__}/images/video_act.png"
   };
   def videoImageInact = Image{
      url:"{__DIR__}/images/video_inact.png"
   };
   def webBrowserImageAct = Image{
      url:"{__DIR__}/images/web_br_act.png"
   };
   def webBrowserImageInact = Image{
      url:"{__DIR__}/images/web_br_inact.png"
   };


   public override function getScyColor(type:String):Color{
      var scyColor = scyMagenta;
      if (type == drawingType)
         scyColor = scyGreen
      else if (type == datasetType)
         scyColor = scyPurple
      else if (type == simulationConfigType)
         scyColor = scyPink
      else if (type == mappingType)
         scyColor = scyGreen
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
      var eloIcon:EloIcon;
      if (eloIcon==null){
         eloIcon = CharacterEloIcon{
            iconCharacter:getScyIconCharacter(type);
            color:getScyColor(type)
         };
      }
      return eloIcon;
   }
}
