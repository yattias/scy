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
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import java.lang.Exception;
import eu.scy.client.desktop.desktoputils.ExceptionCatcher;
import java.lang.Thread;

/**
 * @author sikken
 */
public class ScyToolsList extends ScyToolFX {

   def logger = Logger.getLogger(this.getClass());
   public-init var exceptionCatcher: ExceptionCatcher;
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
      for (node in nodes) {
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

   function applyOnScyTools(f: function(scyTool: ScyTool)): Void {
      for (scyTool in scyToolList) {
         try {
            f(scyTool);
         } catch (e: Exception) {
            exceptionCatcher.showAndLogUncaughtException(Thread.currentThread(), e)
         }
      }
   }

   public function initialize(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.initialize(scyTool == windowContentTool);
      });
   }

   override public function initialize(windowContent: Boolean): Void {
      logger.error("Should not be called");
   }

   override public function postInitialize(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.postInitialize()
      });
   }

   override public function newElo(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.newElo()
      });
   }

   override public function loadElo(eloUri: URI): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.loadElo(eloUri)
      });
   }

   override public function loadedEloChanged(eloUri: URI): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.loadedEloChanged(eloUri)
      });
   }

   override public function onGotFocus(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.onGotFocus()
      });
   }

   override public function onLostFocus(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.onLostFocus()
      });
   }

   override public function onMinimized(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.onMinimized()
      });
   }

   override public function onUnMinimized(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.onUnMinimized()
      });
   }

   override public function aboutToClose(): Boolean {
      var acceptClose = true;
      applyOnScyTools(function(scyTool: ScyTool) {
         if (not scyTool.aboutToClose()) {
            acceptClose = false;
         }
      });
      return acceptClose;
   }

   override public function onOpened(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.onOpened()
      });
   }

   override public function onClosed(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.onClosed()
      });
   }

   override public function onQuit(): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.onQuit()
      });
   }

   override public function setEloSaver(eloSaver: EloSaver): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.setEloSaver(eloSaver)
      });
   }

   override public function setMyEloChanged(myEloChanged: MyEloChanged): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.setMyEloChanged(myEloChanged)
      });
   }

   override public function canAcceptDrop(object: Object): Boolean {
      for (scyTool in scyToolList) {
         try {
            if (scyTool.canAcceptDrop(object)) {
               return true;
            }
         } catch (e: Exception) {
            exceptionCatcher.showAndLogUncaughtException(Thread.currentThread(), e)
         }
      }
      return false;
   }

   override public function acceptDrop(object: Object): Void {
      for (scyTool in scyToolList) {
         try {
            if (scyTool.canAcceptDrop(object)) {
               scyTool.acceptDrop(object);
               return;
            }
         } catch (e: Exception) {
            exceptionCatcher.showAndLogUncaughtException(Thread.currentThread(), e)
         }
      }
      throw new IllegalStateException("could not find a tool, which accepted the drop of {object.getClass()}");
   }

   override public function setRuntimeSettingsRetriever(runtimeSettingsRetriever: RuntimeSettingsRetriever): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.setRuntimeSettingsRetriever(runtimeSettingsRetriever)
      });
   }

   override public function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (windowContentTool instanceof ScyTool) {
         try {
            return (windowContentTool as ScyTool).getThumbnail(width, height);
         } catch (e: Exception) {
            exceptionCatcher.showAndLogUncaughtException(Thread.currentThread(), e)
         }
      }
      return null;
   }

   override public function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      logger.error("Should not be called");
   }

   public function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         if (scyTool instanceof ScyToolFX) {
            (scyTool as ScyToolFX).setTitleBarButtonManager(titleBarButtonManager, scyTool == windowContentTool);
         }
      });
   }

   public override function setReadOnly(readOnly: Boolean): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         scyTool.setReadOnly(readOnly)
      });
   }

   public override function setWindowColorScheme(windowColorScheme: WindowColorScheme): Void {
      applyOnScyTools(function(scyTool: ScyTool) {
         if (scyTool instanceof ScyToolFX) {
            (scyTool as ScyToolFX).setWindowColorScheme(windowColorScheme);
         }
      });
   }

}
