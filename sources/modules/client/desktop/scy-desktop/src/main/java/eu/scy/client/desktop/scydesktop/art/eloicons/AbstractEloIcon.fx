package eu.scy.client.desktop.scydesktop.art.eloicons;

import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.Node;
import javafx.scene.layout.Stack;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.EloIconBackground;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.EloIconBorder;

/**
 * @author lars
 */
public abstract class AbstractEloIcon extends EloIcon {

   def content = createNode();

   protected override function sizeChanged(): Void {
      scaleNode(content);
   }

   public override function create(): Node {
      scaleNode(content);
      return Stack {
                 content: [
                    EloIconBackground {
                       visible: bind selected
                       size: bind size
                       cornerRadius: cornerRadius
                       borderSize: borderSize
                    }
                    content,
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

   public abstract function createNode(): Node;

}

