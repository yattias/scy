/*
 * LanguageSelector.fx
 *
 * Created on 17-mrt-2010, 17:10:32
 */
package eu.scy.client.desktop.scydesktop.uicontrols;

import eu.scy.client.desktop.desktoputils.art.ImageLoader;
import org.apache.log4j.Logger;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.desktoputils.log4j.InitLog4JFX;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Sequences;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

/**
 * @author sikken
 */
public class LanguageSelector extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public-init var languages: String[];
   public-init var imageLoader: ImageLoader;
   public-init var imageExtension = ".png";
   public var language:String on replace{newExternalLanguage();};

   var flagViews: FlagDisplay[];
   var avaiableLanguages: String[];

   def spacing = 5.0;

   var selectedIndex = -1;
   var flagsLoaded = false;


   function loadFlags(): Void {
      if (imageLoader == null) {
         imageLoader = ImageLoader.getImageLoader();
      }
      for (language in languages) {
         var flagImage = imageLoader.getImage("flags/{language}{imageExtension}");
         if (not flagImage.error) {
            var flagView = FlagDisplay {
               flagImage: flagImage
               onMouseClicked: function (e: MouseEvent): Void {
                  var flagIndex = indexof language;
                  selectFlag(flagIndex);
               }
            }
            insert flagView into flagViews;
            insert language into avaiableLanguages;
         } else {
            logger.warn("no flag found for language {language}");
         }
      }
      flagsLoaded = true;
   }

   function selectFlag(index:Integer){
      if (index!=selectedIndex){
         //println("new selection, from {selectedIndex} to {index}");
         if (selectedIndex>=0){
            flagViews[selectedIndex].selected = false;
         }
         selectedIndex = index;
         flagViews[selectedIndex].selected = true;
         language = avaiableLanguages[selectedIndex];
      }
   }

   function newExternalLanguage():Void{
      if (not flagsLoaded) return;
      //println("newExternalLanguage: {language}");
      var index = Sequences.indexOf(avaiableLanguages,language);
      selectFlag(index);
      if (index<0){
         if (sizeof avaiableLanguages>0){
            language = avaiableLanguages[0];
         }
         else{
            language = "";
         }
      }
   }

   public override function create(): Node {
      loadFlags();
      newExternalLanguage();
      var flagsGroup = HBox {
            spacing:spacing;
            nodeVPos:VPos.CENTER
            content: flagViews
         };
      Group{
         content:[
            flagsGroup
         ]
      }
   }

}

class FlagDisplay extends CustomNode {

   public-init var flagImage:Image;
   public var selected = false;

   def selectedColor = Color.LIGHTGREEN;
   def notSelectednColor = Color.TRANSPARENT;
   def borderSize = 5.0;

   def flagImageView = ImageView {
      layoutX:borderSize
      layoutY:borderSize
      image: flagImage
   }
   def boder = Rectangle {
      x: 0, y: 0
      width: flagImageView.layoutBounds.width+2*borderSize
      height: flagImageView.layoutBounds.height+2*borderSize
      fill: bind if (selected) selectedColor else notSelectednColor
   }

	public override function create(): Node {
		return Group {
			content: [
            boder,
            flagImageView
         ]
		};
	}
}


function run(){
   InitLog4JFX.initLog4J();
   var languageSelector:LanguageSelector;
   Stage {
      title : "Test Language selector"
      scene: Scene {
         width: 300
         height: 200
         content: [
            languageSelector = LanguageSelector{
               languages:["nl","en","et","fr","xx"]
               language: "en"
            }
            Label {
               layoutX:10
               layoutY:70
               text: bind languageSelector.language
            }

         ]
      }
   }
}


