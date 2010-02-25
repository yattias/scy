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
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.FunctionalTypes;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

// place your code here

public class ImageWindowStyler extends WindowStyler{
   public-init var impagesPath = "{__DIR__}images/";
   public var repository:IRepository;
   public var metadataTypeManager:IMetadataTypeManager;
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
   public def modelEditorType = "scy/model";
   public def studentPlanningType = "scy/xproc";
   public def presentationType = "scy/ppt";

   public def scyGreen = Color.web("#8db800");
   public def scyPurple = Color.web("#7243db");
   public def scyOrange = Color.web("#ff5400");
   public def scyPink = Color.web("#fb06a2");
   public def scyBlue = Color.web("#0042f1");
   public def scyMagenta = Color.web("#0ea7bf");
   public def scyBrown = Color.web("#9F8B55");

   def functionalTypeKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE);

   def conceptMapImageSet = EloImageSet{
      path:impagesPath;
      name:"con_map"
      color:scyBlue
   }
   def datasetImageSet = EloImageSet{
      path:impagesPath;
      name:"dataset"
      color:scyGreen
   }
   def documentImageSet = EloImageSet{
      path:impagesPath;
      name:"doc"
      color:scyPink
   }
   def drawingImageSet = EloImageSet{
      path:impagesPath;
      name:"drawing"
      color:scyBlue
   }
   def experimentalDesignImageSet = EloImageSet{
      path:impagesPath;
      name:"exp_design"
      color:scyOrange
   }
   def hypotheseImageSet = EloImageSet{
      path:impagesPath;
      name:"hypoth"
      color:scyOrange
   }
   def assignmentImageSet = EloImageSet{
      path:impagesPath;
      name:"info"
      color:scyMagenta
   }
   def informationImageSet = EloImageSet{
      path:impagesPath;
      name:"quest"
      color:scyBrown
   }
   def interviewImageSet = EloImageSet{
      path:impagesPath;
      name:"interview"
      color:scyOrange
   }
   def modelEditorImageSet = EloImageSet{
      path:impagesPath;
      name:"mod_editor"
      color:scyBlue
   }
   def simulatorImageSet = EloImageSet{
      path:impagesPath;
      name:"sim"
      color:scyOrange
   }
   def studentPlanningToolImageSet = EloImageSet{
      path:impagesPath;
      name:"pl_tool"
      color:scyGreen
   }
   def presentaionToolImageSet = EloImageSet{
      path:impagesPath;
      name:"pres"
      color:scyPink
   }
//   def videoImageSet = EloImageSet{
//      path:impagesPath;
//      name:"video"
//   }
//   def webBrowserImageSet = EloImageSet{
//      path:impagesPath;
//      name:"web_br"
//   }

   public override function getScyColor(type:String):Color{
      var scyColor = scyMagenta;
      var eloImageSet:EloImageSet = getEloImageSet(type);
      if (eloImageSet!=null)
         scyColor = eloImageSet.color
      else if (type == drawingType)
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
      var eloImageSet:EloImageSet = getEloImageSet(type);

      if (eloImageSet!=null and not eloImageSet.errorsLoadingImage){
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

   public override function style(window:ScyWindow,uri:URI){
      var color:Color;
      var eloIcon:EloIcon;
      if (isAssignment(uri)){
         color = assignmentImageSet.color;
         eloIcon = createEloIcon(assignmentImageSet);
      }
      else{
         var type = eloInfoControl.getEloType(uri);
         color = getScyColor(type);
         eloIcon = getScyEloIcon(type);
      }
      window.color = color;
      window.drawerColor = color;
      window.eloIcon = eloIcon;
   }

   public override function getScyEloIcon(uri:URI):EloIcon{
      if (isAssignment(uri)){
         return createEloIcon(assignmentImageSet);
      }

      var type = eloInfoControl.getEloType(uri);
      return getScyEloIcon(type);
   }

   public override function getScyColor(uri:URI):Color{
      if (isAssignment(uri)){
         return assignmentImageSet.color;
      }

      var type = eloInfoControl.getEloType(uri);
      var scyColor = getScyColor(type);
      return scyColor;
   }

   function isAssignment(uri:URI):Boolean{
      var type = eloInfoControl.getEloType(uri);
      //println("isAssignment({uri}), type={type}");
      if (type==urlType){
         var metadata = repository.retrieveMetadata(uri);
         if (metadata!=null){
            var functionalType = metadata.getMetadataValueContainer(functionalTypeKey).getValue() as String;
            //println("isAssignment({uri}), functionalType={functionalType} -> {FunctionalTypes.ASSIGMENT.equals(functionalType)}");
            return FunctionalTypes.ASSIGMENT.equals(functionalType);
         }
      }
      return false;
   }

   function createEloIcon(eloImageSet:EloImageSet):EloIcon{
      ImageEloIcon{
         activeImage:eloImageSet.activeImage;
         inactiveImage:eloImageSet.inactiveImage;
      }
   }


   function getEloImageSet(type:String):EloImageSet{
      var eloImageSet:EloImageSet;
      if (type == mappingType)
         eloImageSet = conceptMapImageSet
      else if (type == datasetType)
         eloImageSet = datasetImageSet
      else if (type == textType)
         eloImageSet = documentImageSet
      else if (type == drawingType)
         eloImageSet = drawingImageSet
      else if (type == xprocType)
         eloImageSet = experimentalDesignImageSet
      else if (type == hypotheseType)
         eloImageSet = hypotheseImageSet
      else if (type == urlType)
         eloImageSet = informationImageSet
      else if (type == videoType)
         eloImageSet = informationImageSet
      else if (type == imageType)
         eloImageSet = informationImageSet
      else if (type == meloType)
         eloImageSet = informationImageSet
      else if (type == interviewType)
         eloImageSet = interviewImageSet
      else if (type == modelEditorType)
         eloImageSet = modelEditorImageSet
      else if (type == simulationConfigType)
         eloImageSet = simulatorImageSet
      else if (type == studentPlanningType)
         eloImageSet = studentPlanningToolImageSet
      else if (type == presentationType)
         eloImageSet = presentaionToolImageSet;

      return eloImageSet;
   }

}
