/*
 * ImageLoader.fx
 *
 * Created on 16-mrt-2010, 14:35:44
 */

package eu.scy.client.desktop.desktoputils.art;
import javafx.scene.image.Image;

/**
 * @author sikken
 */

public mixin class ImageLoader {

   public abstract function getImage(fileName:String):Image;
}

var imageLoader:ImageLoader;

public function getImageLoader():ImageLoader{
   if (imageLoader==null){
      imageLoader = BitmapImageLoader{
      }
   }
   imageLoader
}
