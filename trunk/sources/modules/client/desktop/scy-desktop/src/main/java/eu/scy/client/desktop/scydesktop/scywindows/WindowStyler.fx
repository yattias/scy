/*
 * WindowStyler.fx
 *
 * Created on 30-jun-2009, 14:35:14
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.paint.Color;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;

/**
 * @author sikkenj
 */

public mixin class WindowStyler {
   public var eloInfoControl: EloInfoControl;

   public function getScyColor(type:String):Color{
      return Color.GREEN;
   }

   public function getScyIconCharacter(type:String):String{
      return "?";
   }

   public function getScyColor(uri:URI):Color{
      var type = eloInfoControl.getEloType(uri);
      var scyColor = getScyColor(type);
      return scyColor;
   }

   public function style(window:ScyWindow,uri:URI){
      var type = eloInfoControl.getEloType(uri);
      var color = getScyColor(type);
      var ch = getScyIconCharacter(type);
      window.color = color;
      window.iconCharacter = ch;
   }

   public function style(window:ScyWindow){
      var color = getScyColor(window.eloType);
      var ch = getScyIconCharacter(window.eloType);
      window.color = color;
      window.iconCharacter = ch;
   }

}
