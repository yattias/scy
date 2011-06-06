/*
 * Composer.fx
 *
 * Created on 22-mrt-2010, 17:33:19
 */

package eu.scy.client.desktop.desktoputils.i18n;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.util.StringLocalizer;
import javafx.scene.Group;
import javafx.scene.layout.Container;

/**
 * @author sikken
 */

// place your code here

def toLocalizeCode = "##";

public function localizeDesign(nodes: Node[], stringLocalizer: StringLocalizer):Void{
   for (node in nodes){
      if (node instanceof Labeled){
         var labeled = node as Labeled;
         if (labeled.text.startsWith(toLocalizeCode)){
            stringLocalizer.key = labeled.text.substring(toLocalizeCode.length());
            labeled.text = stringLocalizer.localizedString
         }
      }
      else if (node instanceof Group){
         def group = node as Group;
         localizeDesign(group.content, stringLocalizer)
      }
      else if (node instanceof Container){
         def container = node as Container;
         localizeDesign(container.content, stringLocalizer)
      }

   }
}
