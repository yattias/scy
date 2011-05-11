package eu.scy.client.desktop.desktoputils.art.eloicons;

import eu.scy.client.desktop.desktoputils.art.EloIcon;
import javafx.scene.Node;
import javafx.scene.layout.Stack;

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

