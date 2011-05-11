/*
 * EloImageSet.fx
 *
 * Created on 15-jan-2010, 9:58:20
 */

package eu.scy.client.desktop.scydesktop.imagewindowstyler;
import javafx.scene.image.Image;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;

/**
 * @author sikken
 */

public class EloImageSet {
   def logger = Logger.getLogger(this.getClass());

//   public-init var name:String;
//   public-init var imageLoader:ImageLoader;
//   public-init var path = "{__DIR__}images/";
//   public-init var extension = ".png";
//   public-init var activeName = "_act";
//   public-init var inactiveName = "_inact";
   public-init var color:Color;
   public-init var activeImage:Image;
   public-init var inactiveImage:Image;
//   public-read var errorsLoadingImage=false;

//   init{
//      activeImage = loadImage(activeName);
//      inactiveImage = loadImage(inactiveName);
//   }
//
//   function loadImage(activeType:String):Image{
//      imageLoader.getImage("{name}{activeType}{extension}");
////      var image = Image{
////         backgroundLoading:false;
////         url:createUrl(activeType)
////      }
////      if (image.error){
////         logger.error("failed to load image {image.url}");
////         errorsLoadingImage = true;
////      }
////      return image;
//   }
//
//   function createUrl(activeType:String):String{
//      "{path}{name}{activeType}{extension}"
//   }
//
}
