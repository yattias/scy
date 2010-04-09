/*
 * ImageEloIcon.fx
 *
 * Created on 14-jan-2010, 17:50:07
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author sikken
 */
// place your code here
public class ImageEloIcon extends EloIcon {

   public-init var activeImage: Image;
   public-init var inactiveImage: Image;

   public override var selected on replace {setCorrectImage()};

   var imageView: ImageView;

   function setCorrectImage(){
      imageView.image = getCorrectImage();
   }

   function getCorrectImage(): Image{
      if (selected) activeImage else inactiveImage;
   }


   public override function create(): Node {

      return Group {
                 content: [
                        imageView = ImageView {
                            image: getCorrectImage();
                        }
                    ]
              };
   }
}

