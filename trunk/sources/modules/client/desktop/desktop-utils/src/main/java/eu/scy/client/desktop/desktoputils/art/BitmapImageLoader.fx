/*
 * BitmapImageLoader.fx
 *
 * Created on 16-mrt-2010, 14:22:06
 */

package eu.scy.client.desktop.desktoputils.art;
import javafx.scene.image.Image;
import org.apache.log4j.Logger;

/**
 * @author sikken
 */

public class BitmapImageLoader extends ImageLoader {
   def logger = Logger.getLogger(this.getClass());
   public var sourceLocation = ArtSource.artSource;
   public-init var backgroundLoading=false;

   function loadImage(fileName:String):Image{
      var image = Image{
         backgroundLoading:backgroundLoading;
         url:"{sourceLocation}{fileName}"
      }
      if (image.error){
         logger.error("failed to load image {image.url}");
      }
      return image;
   }

   override public function getImage(fileName:String):Image{
      return loadImage(fileName)
   }

}

