/*
 * Composer.fx
 *
 * Created on 22-mrt-2010, 17:33:19
 */

package eu.scy.client.desktop.scydesktop.utils.i18n;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.util.StringLocalizer;
import java.util.Locale;

/**
 * @author sikken
 */

// place your code here

def toLocalizeCode = "##";

public function localizeDesign(nodes: Node[]):Void{
   //println("localizeDesign in {Locale.getDefault().getLanguage()}");
   var stringLocalizer = StringLocalizer{};
   for (node in nodes){
      if (node instanceof Labeled){
         var labeled = node as Labeled;
         if (labeled.text.startsWith(toLocalizeCode)){
            stringLocalizer.key = labeled.text.substring(toLocalizeCode.length());
            labeled.text = stringLocalizer.localizedString
         }
      }
   }
}
