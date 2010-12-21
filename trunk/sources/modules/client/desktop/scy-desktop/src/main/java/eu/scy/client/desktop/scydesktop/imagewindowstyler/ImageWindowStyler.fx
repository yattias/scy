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
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikken
 */
// place your code here
// technical types
public def datasetProcessingType = "scy/pds";
public def datasetType = "scy/dataset";
public def drawingType = "scy/drawing";
public def googleSketchupType = "scy/skp";
public def imageType = "scy/image";
public def interviewType = "scy/interview";
public def mappingType = "scy/mapping";
public def meloType = "scy/melo";
public def modelEditorType = "scy/model";
public def presentationType = "scy/ppt";
public def richTextType = "scy/rtf";
public def simulationConfigType = "scy/simconfig";
public def studentPlanningType = "scy/studentplanningtool";
public def textType = "scy/text";
public def urlType = "scy/url";
public def videoType = "scy/video";
public def wordType = "scy/doc";
public def xprocType = "scy/xproc";
public def generalNew = "general/new";
public def generalSearch = "general/search";
public def generalNavigation = "general/navigation";
// scy colors
public def scyGreen = Color.web("#8db800");
public def scyPurple = Color.web("#7243db");
public def scyOrange = Color.web("#ff5400");
public def scyPink = Color.web("#fb06a2");
public def scyBlue = Color.web("#0042f1");
public def scyMagenta = Color.web("#0ea7bf");
public def scyBrown = Color.web("#9F8B55");
public def scyDarkBlue = Color.web("#00015F");
public def scyDarkRed = Color.web("#9F1938");

public class ImageWindowStyler extends WindowStyler {

   public-init var impagesPath = "{__DIR__}images/";
   public var repository: IRepository;
   public var metadataTypeManager: IMetadataTypeManager;
   public var imageLoader: ImageLoader;
   def imgageEloIconMap = new HashMap();

   init {
      if (imageLoader==null){
         imageLoader = ImageLoader.getImageLoader();
      }

      for (eloImageInformation in EloImageInformation.values()) {
         var activeImage = imageLoader.getImage("{eloImageInformation.iconName}_act.png");
         var inactiveImage = imageLoader.getImage("{eloImageInformation.iconName}_inact.png");
         if (not activeImage.error and not inactiveImage.error) {
            var eloImageSet = EloImageSet {
                  activeImage: activeImage
                  inactiveImage: inactiveImage
               }
            imgageEloIconMap.put(eloImageInformation.type, eloImageSet);
         }
      }
   }

   public override function getScyColor(type:String):Color{
      var scyColors = EloImageInformation.getScyColors(type);
      var colorName = ScyColors.darkGray.mainColorName;
      if (scyColors != null) {
         colorName = scyColors.mainColorName;
      }
      return Color.web(colorName);
   }

   public override function getScyColors(type:String):ScyColors{
      var scyColors = EloImageInformation.getScyColors(type);
      return scyColors;
   }

   public override function getWindowColorScheme(type:String):WindowColorScheme{
      var scyColors = EloImageInformation.getScyColors(type);
      if (scyColors==null){
         scyColors = ScyColors.darkGray;
      }
      return WindowColorScheme.getWindowColorScheme(scyColors);
   }

   public override function getScyIconCharacter(type: String): String {
      var iconChar = "?";
      if (type == drawingType)
         iconChar = "D" else if (type == datasetType)
         iconChar = "V" else if (type == simulationConfigType)
         iconChar = "S" else if (type == datasetProcessingType)
         iconChar = "P" else if (type == textType)
         iconChar = "T" else if (type == imageType)
         iconChar = "I" else if (type == meloType)
         iconChar = "I" else if (type == mappingType)
         iconChar = "M" else if (type == urlType)
         iconChar = "W" else if (type == videoType)
         iconChar = "V" else if (type == interviewType)
         iconChar = "I" else if (type == xprocType)
         iconChar = "X";
      return iconChar;
   }

   public override function getScyEloIcon(type: String): EloIcon {
      var eloImageSet = imgageEloIconMap.get(type) as EloImageSet;
      if (eloImageSet!=null){
         return createEloIcon(eloImageSet);
      }
      CharacterEloIcon {
         iconCharacter: getScyIconCharacter(type);
         color: getScyColor(type)
      };
   }

//   public override function style(window: ScyWindow, uri: URI)  {
//      var type = eloTypeControl.getEloType(uri);
//      var windowColorScheme = getWindowColorScheme(type);
//      var eloIcon = getScyEloIcon(type);
//      window.windowColorScheme = windowColorScheme;
//      window.eloIcon = eloIcon;
//   }
//
//   public override function getScyEloIcon(uri: URI): EloIcon {
//      var type = eloTypeControl.getEloType(uri);
//      return getScyEloIcon(type);
//   }

   function createEloIcon(eloImageSet: EloImageSet): EloIcon {
      ImageEloIcon {
         activeImage: eloImageSet.activeImage;
         inactiveImage: eloImageSet.inactiveImage;
      }
   }
}
