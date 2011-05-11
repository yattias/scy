/*
 * ImageEloIcon.fx
 *
 * Created on 14-jan-2010, 17:50:07
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.client.desktop.desktoputils.art.EloIcon;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Stack;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconBorder;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconBackground;

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
      imageView = ImageView {
          image: getCorrectImage();
      }

      scaleNode(imageView);
      return Stack {
            content: [
               EloIconBackground {
                  visible: bind selected
                  size: bind size
                  cornerRadius: cornerRadius
                  borderSize: borderSize
               }
               imageView,
               EloIconBorder {
                  visible: bind selected
                  size: bind size
                  cornerRadius: cornerRadius
                  borderSize: borderSize
                  borderColor: bind windowColorScheme.mainColor
               }
            ]
         };
   }

   public override function clone():EloIcon{
      ImageEloIcon{
         activeImage: activeImage
         inactiveImage: inactiveImage
         selected:selected
      }
   }

}

