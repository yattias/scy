/*
 * WindowStyler.fx
 *
 * Created on 30-jun-2009, 14:35:14
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.paint.Color;

import java.net.URI;


import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;

/**
 * @author sikkenj
 */

public mixin class WindowStyler {
   public var eloTypeControl: EloDisplayTypeControl;

   public function getScyColor(type:String):Color{
      return Color.GREEN;
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
      var scyColor = getScyColor(type);
      return scyColor;
   }

   public function style(window:ScyWindow,uri:URI){
      var type = eloTypeControl.getEloType(uri);
      var color = getScyColor(type);
      var eloIcon = getScyEloIcon(type);
      window.color = color;
      window.drawerColor = color;
      window.eloIcon = eloIcon;
   }

   public function style(window:ScyWindow){
      var color = getScyColor(window.eloType);
      var ch = getScyIconCharacter(window.eloType);
      var eloIcon = getScyEloIcon(window.eloType);
      window.color = color;
      window.drawerColor = color;
      window.eloIcon = eloIcon;
   }

}
