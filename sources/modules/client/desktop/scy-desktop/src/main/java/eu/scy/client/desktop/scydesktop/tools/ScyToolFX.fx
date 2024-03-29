/*
 * scyToolFX.fx
 *
 * Created on 30-nov-2009, 15:41:16
 */
package eu.scy.client.desktop.scydesktop.tools;

import java.net.URI;
import java.lang.IllegalStateException;
import java.awt.image.BufferedImage;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;
import javafx.scene.Node;

/**
 * @author sikken
 */
public function getScyTool(node: Node): ScyTool {
   var scyTool: ScyTool;
   if (node instanceof ScyTool) {
      scyTool = node as ScyTool
   } else if (node instanceof ScyToolGetter) {
      scyTool = (node as ScyToolGetter).getScyTool();
   }
   scyTool
}

public mixin class ScyToolFX extends ScyTool {

   public-read protected var eloSaver: EloSaver;
   public-read protected var myEloChanged: MyEloChanged;
   public-read protected var runtimeSettingsRetriever: RuntimeSettingsRetriever;
   public-read protected var readOnly: Boolean;
   public-read protected var windowColorScheme: WindowColorScheme;

   public override function initialize(windowContent: Boolean): Void {
   }

   public override function postInitialize(): Void {
   }

   public override function newElo(): Void {
   }

   public override function loadElo(eloUri: URI): Void {
   }

   public override function loadedEloChanged(eloUri: URI): Void {
   }

   public override function onGotFocus(): Void {
   }

   public override function onLostFocus(): Void {
   }

   public override function onMinimized(): Void {
   }

   public override function onUnMinimized(): Void {
   }

   public override function onOpened(): Void {
   }

   public override function onClosed(): Void {
   }

   public override function aboutToClose(): Boolean {
      return true;
   }

   public override function onQuit(): Void {
   }

   public override function setEloSaver(eloSaver: EloSaver): Void {
      this.eloSaver = eloSaver;
   }

   public override function setMyEloChanged(myEloChanged: MyEloChanged): Void {
      this.myEloChanged = myEloChanged;
   }

   public override function canAcceptDrop(object: Object): Boolean {
      return false;
   }

   public override function acceptDrop(object: Object): Void {
      throw new IllegalStateException("cannot accept drop object");
   }

   public override function setRuntimeSettingsRetriever(runtimeSettingsRetriever: RuntimeSettingsRetriever): Void {
      this.runtimeSettingsRetriever = runtimeSettingsRetriever;
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      return null;
   }

   public function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
   }

   public override function getDrawerUIIndicator(): DrawerUIIndicator {
      return null;
   }

   public override function getBubbleKey(): BubbleKey {
      return null;
   }

   public override function setReadOnly(readOnly: Boolean): Void {
      this.readOnly = readOnly;
   }

   public function setWindowColorScheme(windowColorScheme: WindowColorScheme): Void {
      this.windowColorScheme = windowColorScheme;
   }

}
