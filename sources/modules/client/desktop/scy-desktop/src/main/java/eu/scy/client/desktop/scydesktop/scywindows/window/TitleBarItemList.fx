/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * @author SikkenJ
 */

public class TitleBarItemList extends WindowElement {

   public var itemListChanged: function():Void;

   protected def itemWidth = 10.0;
   protected def itemHeight = 10.0;
   protected def itemSpacing = 9.0;

   protected def displayBox = HBox {
         spacing: itemSpacing
      }

   public override function create(): Node {
      updateItems();
      displayBox
   }

   protected function updateItems():Void{
   }

}
