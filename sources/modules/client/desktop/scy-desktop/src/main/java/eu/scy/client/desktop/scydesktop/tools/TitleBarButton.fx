/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;

/**
 * @author SikkenJ
 */
public class TitleBarButton {

   public-init var actionId: String;
   public-init var iconType: String;
   public var tooltip: String;
   public var enabled: Boolean = true;
   public var action: function(): Void;

   public override function toString():String{
      "actionId: {actionId}, iconType: {iconType}"
   }

}
