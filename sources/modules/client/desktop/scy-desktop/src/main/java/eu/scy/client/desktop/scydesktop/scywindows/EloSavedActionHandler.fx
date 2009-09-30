/*
 * EloSavedActionHandler.fx
 *
 * Created on 29-sep-2009, 16:35:03
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import eu.scy.client.desktop.scydesktop.hacks.EloSavedListener;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.ScyDesktop;

/**
 * @author sikkenj
 */

// place your code here

public class EloSavedActionHandler extends EloSavedListener{
   public var scyDesktop:ScyDesktop;

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
         scyDesktop.addScyWindow(eloUri);
      }
   }

}
