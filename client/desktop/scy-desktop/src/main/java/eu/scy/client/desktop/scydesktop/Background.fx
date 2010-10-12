/*
 * Background.fx
 *
 * Created on 12-mrt-2010, 10:10:00
 */
package eu.scy.client.desktop.scydesktop;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Scene;

/**
 * @author sikken
 */
public class Background extends CustomNode {

   public-init var defaultBackgroundImage:Image;
   public-init var useScene:Scene;
   public var displayWith = 100.0;
   public var displayHeight = 100.0;

   def sceneWidth = bind useScene.width on replace{
      println("new sceneWidth: {sceneWidth}")
   }


   def backgroundImageView = ImageView {
         image: defaultBackgroundImage
         fitWidth: bind displayWith
         fitHeight: bind displayHeight
         preserveRatio: false
         cache: true
      }

   init{
      println("init: scene: {scene}");
      println("init: useScene: {useScene}");
      println("init: sceneWidth: {sceneWidth}");
      println("init: defaultBackgroundImage: {defaultBackgroundImage}");
      println("init: backgroundImageView: {backgroundImageView.layoutBounds}");
   }

   public override function create(): Node {
      println("useScene: {useScene}");
      println("scene: {scene}");
      println("use: backgroundImageView: {backgroundImageView.layoutBounds}");
      backgroundImageView
   }

}
