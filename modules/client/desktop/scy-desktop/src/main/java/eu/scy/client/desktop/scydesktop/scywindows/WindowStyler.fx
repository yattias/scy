/*
 * WindowStyler.fx
 *
 * Created on 30-jun-2009, 14:35:14
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.paint.Color;

import java.net.URI;


import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikkenj
 */

public mixin class WindowStyler {
   public var eloTypeControl: EloDisplayTypeControl;

   public function getScyColor(type:String):Color{
      return Color.GREEN;
   }

   public function getScyColors(type:String):ScyColors{
      return ScyColors.green;
   }

   public function getWindowColorScheme(type:String):WindowColorScheme{
      return WindowColorScheme.getWindowColorScheme(ScyColors.green);
   }

   public function getScyIconCharacter(type:String):String{
      return "?";
   }

   public function getScyEloIcon(type:String):EloIcon{
      return CharacterEloIcon{
         iconCharacter:"?";
         color:getScyColor(type)
      };
   }

   public function getScyEloIcon(uri:URI):EloIcon{
      var type = eloTypeControl.getEloType(uri);
      return getScyEloIcon(type);
   }

   public function getScyColor(uri:URI):Color{
      var type = eloTypeControl.getEloType(uri);
      return getScyColor(type);
   }

   public function getScyColors(uri:URI):ScyColors{
      var type = eloTypeControl.getEloType(uri);
      var scyColors = getScyColors(type);
      return scyColors;
   }

   public function getWindowColorScheme(uri:URI):WindowColorScheme{
      var type = eloTypeControl.getEloType(uri);
      var windowColorScheme = getWindowColorScheme(type);
      return windowColorScheme;
   }

   public function style(window:ScyWindow,uri:URI){
      var type = eloTypeControl.getEloType(uri);
      var windowColorScheme = getWindowColorScheme(type);
      var eloIcon = getScyEloIcon(type);
      window.windowColorScheme.assign(windowColorScheme);
      window.eloIcon = eloIcon;
   }

   public function style(window:ScyWindow){
      var windowColorScheme = getWindowColorScheme(window.eloType);
      var ch = getScyIconCharacter(window.eloType);
      var eloIcon = getScyEloIcon(window.eloType);
      window.windowColorScheme.assign(windowColorScheme);
      window.eloIcon = eloIcon;
   }

}
