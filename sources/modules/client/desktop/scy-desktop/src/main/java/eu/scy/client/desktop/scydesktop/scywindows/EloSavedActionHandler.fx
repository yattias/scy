/*
 * EloSavedActionHandler.fx
 *
 * Created on 29-sep-2009, 16:35:03
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import eu.scy.client.desktop.scydesktop.hacks.EloSavedListener;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.ScyDesktop;


/**
 * @author sikkenj
 */

// place your code here

public class EloSavedActionHandler extends EloSavedListener{

   def logger = Logger.getLogger(this.getClass());

   //public var scyWindowControl:ScyWindowControl;
   public-init var scyDesktop:ScyDesktop;

   public override function newEloSaved(eloUri : URI):Void{
      logger.info("EloSavedActionHandler.newEloSaved({eloUri})");
      reportNewEloSaved(eloUri);
   }

   public override function forkedEloSaved(eloUri : URI):Void{
      logger.info("EloSavedActionHandler.forkedEloSaved({eloUri})");
      reportNewEloSaved(eloUri);
   }

   public override function eloUpdated(eloUri : URI):Void{
      logger.info("EloSavedActionHandler.eloUpdated({eloUri})");
   }

   function reportNewEloSaved(eloUri:URI):Void{
      FX.deferAction(getNewEloSaved(eloUri));
   }

   function getNewEloSaved(eloUri : URI):function(){
      function(){
         scyDesktop.scyWindowControl.newEloSaved(eloUri);
      }
   }

}
