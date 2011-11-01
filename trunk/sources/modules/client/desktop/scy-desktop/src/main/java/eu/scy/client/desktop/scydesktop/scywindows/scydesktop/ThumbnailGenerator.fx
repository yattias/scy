/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.geometry.BoundingBox;
import eu.scy.client.desktop.desktoputils.ImageUtils;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import java.awt.image.BufferedImage;
import java.lang.Exception;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author SikkenJ
 */
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ThumbnailGenerator");

public function generateThumbnail(window: ScyWindow): BufferedImage {
   var thumbnailImage: BufferedImage;
   if (window.isClosed) {
      thumbnailImage = window.thumbnail
   } else {
      def contentScyTool = ScyToolFX.getScyTool(window.scyContent);
      if (contentScyTool != null) {
         try {
            thumbnailImage = contentScyTool.getThumbnail(ArtSource.thumbnailWidth, ArtSource.thumbnailHeight);
         } catch (e: Exception) {
            logger.error("exception in getThumbnail() from window.scyContent {window.scyContent.getClass().getName()}", e);
         }
      }
      if (thumbnailImage == null and window.scyContent != null) {
         if (window.scyContent.layoutBounds.width > 0 and window.scyContent.layoutBounds.height > 0) {
            def bounds = BoundingBox {
                       width: ArtSource.thumbnailWidth
                       height: ArtSource.thumbnailHeight
                    }
            try {
               thumbnailImage = ImageUtils.nodeToImage(window.scyContent, bounds);
               window.requestLayout();
            } catch (e: Exception) {
               logger.error("failed to create thumbnail image from window.scyContent {window.scyContent.getClass().getName()}", e);
            }
         }
      }
   }
   thumbnailImage
}

