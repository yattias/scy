/*
 * EloSavedActionHandler.fx
 *
 * Created on 29-sep-2009, 16:35:03
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import eu.scy.client.desktop.scydesktop.hacks.EloSavedListener;

import java.net.URI;


/**
 * @author sikkenj
 */

// place your code here

public class EloSavedActionHandler extends EloSavedListener{
   public var scyWindowControl:ScyWindowControl;

   public override function newEloSaved(eloUri : URI):Void{
      println("EloSavedActionHandler.newEloSaved({eloUri})");
      FX.deferAction(getAddScyWindow(eloUri));
   }

   public override function forkedEloSaved(eloUri : URI):Void{
      println("EloSavedActionHandler.forkedEloSaved({eloUri})");
//      scyDesktop.addScyWindow(eloUri);
      FX.deferAction(getAddScyWindow(eloUri));
   }

   public override function eloUpdated(eloUri : URI):Void{
      println("EloSavedActionHandler.eloUpdated({eloUri})");
   }

   function getAddScyWindow(eloUri : URI):function(){
      function(){
         scyWindowControl.addOtherScyWindow(eloUri);
      }
   }

}
