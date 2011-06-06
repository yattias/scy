/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;
import java.lang.IllegalStateException;

/**
 * @author SikkenJ
 */

public def saveActionId = "save";
public def saveAsActionId = "saveAs";

public class TitleBarButton {

   public-init var actionId: String;
   public-init var iconType: String;
   public var tooltip: String;
   public var enabled: Boolean = true;
   public var action: function(): Void;

   init{
      if (iconType==""){
         iconType = findDefaultIconType();
      }
      if (tooltip==""){
         tooltip = findDefaultTooltip();
      }
   }

   function findDefaultIconType():String{
      if (saveActionId==actionId){
         return "save";
      }
      if (saveAsActionId==actionId){
         return "save_as";
      }
      throw new IllegalStateException("iconType may not be null in TitleBarButton, with actionId: {actionId}")
   }

   function findDefaultTooltip(): String {
      if (saveActionId==actionId){
         return ##"save";
      }
      if (saveAsActionId==actionId){
         return ##"save as new ELO";
      }
      return "";
   }

   public override function toString():String{
      "actionId: {actionId}, iconType: {iconType}"
   }


}
