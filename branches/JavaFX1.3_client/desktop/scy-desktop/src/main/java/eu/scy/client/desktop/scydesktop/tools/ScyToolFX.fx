/*
 * scyToolFX.fx
 *
 * Created on 30-nov-2009, 15:41:16
 */

package eu.scy.client.desktop.scydesktop.tools;

import java.net.URI;
import java.lang.IllegalStateException;

/**
 * @author sikken
 */

public mixin class ScyToolFX extends ScyTool {

   public-read var eloSaver:EloSaver;
   public-read var myEloChanged:MyEloChanged;

   public override function initialize(windowContent: Boolean):Void{
   }

   public override function postInitialize():Void{
   }

   public override function newElo():Void{
   }

   public override function loadElo(eloUri:URI):Void{
   }

   public override function loadedEloChanged(eloUri:URI):Void{
   }

   public override function onGotFocus():Void{
   }

   public override function onLostFocus():Void{
   }

   public override function onMinimized():Void{
   }

   public override function onUnMinimized():Void{
   }

   public override function onClosed():Void{
   }

   public override function aboutToClose():Boolean{
      return true;
   }

   public override function setEloSaver(eloSaver:EloSaver):Void{
      this.eloSaver = eloSaver;
   }

   public override function setMyEloChanged(myEloChanged:MyEloChanged):Void{
      this.myEloChanged = myEloChanged;
   }

   public override function canAcceptDrop(object:Object):Boolean{
      return false;
   }

   public override function acceptDrop(object:Object):Void{
      throw new IllegalStateException("cannot accept drop object");
   }

}