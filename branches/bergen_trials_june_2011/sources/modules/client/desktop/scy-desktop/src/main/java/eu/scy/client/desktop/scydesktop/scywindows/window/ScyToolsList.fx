/*
 * ScyToolsList.fx
 *
 * Created on 11-jan-2010, 11:26:30
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import eu.scy.client.desktop.scydesktop.tools.ScyToolGetter;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Void;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.tools.RuntimeSettingsRetriever;
import java.awt.image.BufferedImage;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;

/**
 * @author sikken
 */
public class ScyToolsList extends ScyToolFX {

   def logger = Logger.getLogger(this.getClass());
   public var windowContentTool: Node on replace { buildToolList() };
   public var topDrawerTool: Node on replace { buildToolList() };
   public var rightDrawerTool: Node on replace { buildToolList() };
   public var bottomDrawerTool: Node on replace { buildToolList() };
   public var leftDrawerTools: Node[] on replace { buildToolList() };
   public var actionLoggerTool: Node on replace { buildToolList() };
   var scyToolList: ScyTool[];

   function buildToolList(): Void {
      delete  scyToolList;
      insertIfScyTool(windowContentTool);
      insertIfScyTool(topDrawerTool);
      insertIfScyTool(rightDrawerTool);
      insertIfScyTool(bottomDrawerTool);
      insertIfScyTool(leftDrawerTools);
      insertIfScyTool(actionLoggerTool);
   //      logger.info("nr of scyTools {sizeof scyToolList}");
   }

   function insertIfScyTool(nodes: Node[]): Void {
      for (node in nodes){
         insertIfScyTool(node)
      }
   }


   function insertIfScyTool(node: Node): Void {
      //      logger.info("insertIfScyTool({node.getClass()})");
      if (node instanceof ScyTool) {
         insert (node as ScyTool) into scyToolList;
      //         logger.debug("add node as ScyTool {sizeof scyToolList}");
      } else if (node instanceof ScyToolGetter) {
         insert (node as ScyToolGetter).getScyTool() into scyToolList;
      //         logger.debug("add node.getScyTool as ScyTool {sizeof scyToolList}");
      }
   }

   public function initialize(): Void {
      for (scyTool in scyToolList) {
         scyTool.initialize(scyTool == windowContentTool);
      }
   }

   override public function initialize(windowContent: Boolean): Void {
      logger.error("Should not be called");
   }

   override public function postInitialize(): Void {
      for (scyTool in scyToolList) {
         scyTool.postInitialize();
      }
   }

   override public function newElo(): Void {
      for (scyTool in scyToolList) {
         scyTool.newElo();
      }
   }

   override public function loadElo(eloUri: URI): Void {
      for (scyTool in scyToolList) {
         scyTool.loadElo(eloUri);
      }
   }

   override public function loadedEloChanged(eloUri: URI): Void {
      for (scyTool in scyToolList) {
         scyTool.loadedEloChanged(eloUri);
      }
   }

   override public function onGotFocus(): Void {
      for (scyTool in scyToolList) {
         scyTool.onGotFocus();
      }
   }

   override public function onLostFocus(): Void {
      for (scyTool in scyToolList) {
         scyTool.onLostFocus();
      }
   }

   override public function onMinimized(): Void {
      for (scyTool in scyToolList) {
         scyTool.onMinimized();
      }
   }

   override public function onUnMinimized(): Void {
      for (scyTool in scyToolList) {
         scyTool.onUnMinimized();
      }
   }

   override public function aboutToClose(): Boolean {
      for (scyTool in scyToolList) {
         if (not scyTool.aboutToClose()) {
            return false;
         }
      }
      return true;
   }

   override public function onOpened(): Void {
      for (scyTool in scyToolList) {
         scyTool.onOpened();
      }
   }

   override public function onClosed(): Void {
      for (scyTool in scyToolList) {
         scyTool.onClosed();
      }
   }

   override public function onQuit(): Void {
      for (scyTool in scyToolList) {
         scyTool.onQuit();
      }
   }

   override public function setEloSaver(eloSaver: EloSaver): Void {
      for (scyTool in scyToolList) {
         scyTool.setEloSaver(eloSaver);
      }
   }

   override public function setMyEloChanged(myEloChanged: MyEloChanged): Void {
      for (scyTool in scyToolList) {
         scyTool.setMyEloChanged(myEloChanged);
      }
   }

   override public function canAcceptDrop(object: Object): Boolean {
      for (scyTool in scyToolList) {
         if (scyTool.canAcceptDrop(object)) {
            return true;
         }
      }
      return false;
   }

   override public function acceptDrop(object: Object): Void {
      for (scyTool in scyToolList) {
         if (scyTool.canAcceptDrop(object)) {
            scyTool.acceptDrop(object);
            return;
         }
      }
      throw new IllegalStateException("could not find a tool, which accepted the drop of {object.getClass()}");
   }

   override public function setRuntimeSettingsRetriever(runtimeSettingsRetriever: RuntimeSettingsRetriever): Void {
      for (scyTool in scyToolList) {
         scyTool.setRuntimeSettingsRetriever(runtimeSettingsRetriever);
      }
   }

   override public function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (windowContentTool instanceof ScyTool) {
         return (windowContentTool as ScyTool).getThumbnail(width, height);
      }
      return null;
   }

   override public function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      logger.error("Should not be called");
   }

   public function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager): Void {
      for (scyTool in scyToolList) {
         if (scyTool instanceof ScyToolFX){
            (scyTool as ScyToolFX).setTitleBarButtonManager(titleBarButtonManager,scyTool==windowContentTool);
         }
      }
   }

}
