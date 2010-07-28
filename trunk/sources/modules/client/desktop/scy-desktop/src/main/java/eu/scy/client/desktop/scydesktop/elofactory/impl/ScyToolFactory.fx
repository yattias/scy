/*
 * ScyToolFactory.fx
 *
 * Created on 11-jan-2010, 15:09:21
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.Node;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.tools.ScyToolGetter;
import java.lang.Exception;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditor;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.scene.layout.Container;

/**
 * @author sikken
 */
public class ScyToolFactory extends ContentFactory {

   def logger = Logger.getLogger(this.getClass());
   public var scyToolCreatorRegistryFX: ScyToolCreatorRegistryFX;
   public var drawerContentCreatorRegistryFX: DrawerContentCreatorRegistryFX;
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
   public var newTitleGenerator: NewTitleGenerator;

   public function createNewScyToolNode(id: String, type: String, eloUri: URI, scyWindow: ScyWindow, drawer: Boolean): Node {
      if (id == null) {
         // no tool specified
         return null;
      }
      var startNanos = System.nanoTime();
      var toolTypeCreated = "?";
      var toolNode: Node;

      var scyToolCreator = scyToolCreatorRegistryFX.getScyToolCreatorFX(id);
      if (scyToolCreator != null) {
         try {
            checkIfServicesInjected(scyToolCreator);
            toolNode = scyToolCreator.createScyToolNode(type,id,scyWindow, not drawer);
            if (not drawer){
               scyWindow.desiredContentWidth = Container.getNodePrefWidth(toolNode);
               scyWindow.desiredContentHeight = Container.getNodePrefHeight(toolNode);
//               println("desired content size set to {scyWindow.desiredContentWidth}*{scyWindow.desiredContentHeight}");
            }
            toolTypeCreated = "ScyTool";
         } catch (e: Exception) {
            toolNode = createErrorNode(getErrorMessage(e, eloUri, id, type, drawer, scyToolCreator),scyWindow);
            toolTypeCreated = "ErrorScyTool";
         }
      }
      if (toolNode == null) {
         if (drawer) {
            var drawerContentCreator = drawerContentCreatorRegistryFX.getDrawerContentCreatorFX(id);
            if (drawerContentCreator != null) {
               checkIfServicesInjected(drawerContentCreator);
               try {
                  toolNode = drawerContentCreator.getDrawerContent(eloUri, scyWindow);
                  toolTypeCreated = "DrawerTool";
               } catch (e: Exception) {
                  toolNode = createErrorNode(getErrorMessage(e, eloUri, id, type, true, drawerContentCreator),scyWindow);
                  toolTypeCreated = "ErrorDrawerTool";
               }
            }
         } else {
            var windowContentCreator = windowContentCreatorRegistryFX.getWindowContentCreatorFX(id);
            if (windowContentCreator != null) {
               checkIfServicesInjected(windowContentCreator);
               try {
                  if (windowContentCreator instanceof ScyToolWindowContentCreatorFX) {
                     toolNode = (windowContentCreator as ScyToolWindowContentCreatorFX).createScyToolWindowContent();
                     toolTypeCreated = "ScyToolWindow";
                  } else {
                     if (eloUri != null) {
                        toolNode = windowContentCreator.getScyWindowContent(eloUri, scyWindow);
                        toolTypeCreated = "WindowToolExisting";
                     } else {
                        toolNode = windowContentCreator.getScyWindowContentNew(scyWindow);
                        toolTypeCreated = "WindowToolNew";
                     }
                  }
               } catch (e: Exception) {
                  toolNode = createErrorNode(getErrorMessage(e, eloUri, id, type, false, windowContentCreator),scyWindow);
                  toolTypeCreated = "ErrorWindowTool";
               }
            }
         }
      }

      if (toolNode == null) {
         toolNode = createErrorNode("Cannot find creator for {if (drawer) "drawer" else "window"} tool: {id}\nElo uri: {eloUri}\nEloType: {type}",scyWindow);
         toolTypeCreated = "NotFoundTool";
      }

      checkIfServicesInjected(toolNode);
      servicesInjector.injectServiceIfWanted(toolNode, scyWindow.getClass(), "scyWindow", scyWindow);
      if (toolNode instanceof ScyToolGetter) {
         var scyTool = (toolNode as ScyToolGetter).getScyTool();
         if (scyTool!=null){
            checkIfServicesInjected(scyTool);
            servicesInjector.injectServiceIfWanted(scyTool, scyWindow.getClass(), "scyWindow", scyWindow);
         }
      }
      var nanosUsed = System.nanoTime()-startNanos;
      println("Created {toolTypeCreated} for id {id} in {if (drawer) 'drawer' else 'content'} in {nanosUsed/1e6} ms");
      return toolNode;
   }

   function createErrorNode(errorMessage: String, window:ScyWindow): Node {
      var textEditor = new TextEditor();
      textEditor.setEditable(false);
      textEditor.setText(errorMessage);
      textEditor.resetScrollbars();

      return ScySwingWrapper.wrap(textEditor);
   }

   function getErrorMessage(e: Exception, eloUri: URI, id: String, type: String, drawer: Boolean, creator: Object): String {
      logger.error("An exception occured during creation of a tool");
      var charArrayWriter: CharArrayWriter;
      var printWriter: PrintWriter;
      try {
         charArrayWriter = new CharArrayWriter();
         printWriter = new PrintWriter(charArrayWriter);
         printWriter.println("An exception occured during the creation of a {if (drawer) "drawer" else "window"} tool.");
         printWriter.println("ELO uri: {eloUri}");
         printWriter.println("ELO type: {type}");
         printWriter.println("Content creator id: {id}");
         printWriter.println("Content creator class: {creator.getClass().getName()}");
         printWriter.println("Exception type: {e.getClass().getName()}");
         printWriter.println("Exception message: {e.getMessage()}");
         printWriter.println("Strack trace:");
         e.printStackTrace(printWriter);
         return charArrayWriter.toString();
      } finally {
         if (printWriter != null) {
            try {
               printWriter.close();
            } catch (e1) {
               // what to do ???
            }
         }
         if (charArrayWriter != null) {
            try {
               charArrayWriter.close();
            } catch (e1) {
               // what to do ???
            }
         }
      }
   }
}
