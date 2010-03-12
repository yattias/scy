/*
 * MultiImageButton.fx
 *
 * Created on 11-mrt-2010, 9:49:54
 */

package eu.scy.client.desktop.scydesktop.uicontrols;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author sikken
 */

public class MultiImageButton extends CustomNode {

   public var imageName = "test" on replace {loadImages()};
   public var imageLocation = "{__DIR__}images/";
   public-init var extension = ".png";
   public var action:function():Void;
   public-read var errorsLoadingImage=false;
   public var turnedOn = false on replace{newImageState()};

   public override var disable on replace{newImageState()};

   def disabledName = "_disabled";
   def mouseOutName = "_mouseOut";
   def mouseOverName = "_mouseOver";
   def pressedMouseOverName = "_pressedOver";
   def pressedMouseOutName = "_pressedOut";

   public-read var disabledImage:Image;
   public-read var mouseOutImage:Image;
   public-read var mouseOverImage:Image;
   public-read var pressedMouseOverImage:Image;
   public-read var pressedMouseOutImage:Image;

   var imageView:ImageView;
   var mouseOver = false;
   var mousePressed = false;

   function loadImage(activeType:String):Image{
      var image:Image;
      if (imageName!="" and imageLocation!="" and extension!=""){
         image = Image{
            backgroundLoading:false;
            url:createUrl(activeType)
         }
         if (image.error){
            println("failed to load image {image.url}");
            errorsLoadingImage = true;
         }
      }
      return image;
   }

   function createUrl(activeType:String):String{
      "{imageLocation}{imageName}{activeType}{extension}"
   }

   function loadImages(){
      disabledImage = loadImage(disabledName);
      mouseOutImage = loadImage(mouseOutName);
      mouseOverImage = loadImage(mouseOverName);
      pressedMouseOverImage = loadImage(pressedMouseOverName);
      pressedMouseOutImage = loadImage(pressedMouseOutName);
      newImageState();
   }

   function newImageState():Void{
      if (disabled){
         imageView.image = disabledImage;
      }
      else if (turnedOn){
         imageView.image = pressedMouseOverImage;
      }
      else{
         if (mouseOver and mousePressed){
            imageView.image = pressedMouseOverImage;
         }
         else if (mouseOver and not mousePressed){
            imageView.image = mouseOverImage;
         }
         else if (not mouseOver and mousePressed){
            imageView.image = pressedMouseOutImage;
         }
         else if (not mouseOver and not mousePressed){
            imageView.image = mouseOutImage;
         }
      }
   }

   public override function create(): Node {
      loadImages();
      imageView = ImageView {
      }
      newImageState();
      return Group {
            content: imageView
            onMouseEntered: function (e: MouseEvent): Void {
               mouseOver = true;
               newImageState();
            }
            onMouseExited: function (e: MouseEvent): Void {
               mouseOver = false;
               newImageState();
            }
            onMousePressed: function (e: MouseEvent): Void {
               mousePressed = true;
               newImageState();
            }
            onMouseReleased: function (e: MouseEvent): Void {
               if (not turnedOn and mouseOver){
                  action();
               }
               mousePressed = false;
               newImageState();
            }
      };
   }

}
