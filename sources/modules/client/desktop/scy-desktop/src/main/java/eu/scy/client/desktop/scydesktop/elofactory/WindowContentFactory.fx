/*
 * WindowContentFactory.fx
 *
 * Created on 3-jul-2009, 15:25:10
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;


import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.utils.UriUtils;

import org.apache.log4j.Logger;

import eu.scy.client.desktop.scydesktop.tools.ScyTool;

import java.lang.IllegalStateException;

import java.lang.Exception;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditor;
import eu.scy.client.desktop.scydesktop.dummy.DummyScyToolWindowContentCreator;

import java.io.CharArrayWriter;

import java.io.PrintWriter;

import javafx.ext.swing.SwingComponent;

/**
 * @author sikkenj
 */

def logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentFactory");

public class WindowContentFactory extends ContentFactory {
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;

   def defaultWindowContentCreator:WindowContentCreatorFX = DummyScyToolWindowContentCreator{};

   public function fillWindowContent(eloUri:URI,scyWindow:ScyWindow,id:String){
     return fillWindowContent(eloUri,scyWindow,id,getType(eloUri));
   }

   public function fillWindowContent(scyWindow:ScyWindow,id:String){
      return fillWindowContent(null,scyWindow,id,scyWindow.eloType);
   }

   function fillWindowContent(eloUri:URI,scyWindow:ScyWindow,id:String, type:String){
      var windowContentCreator = windowContentCreatorRegistryFX.getWindowContentCreatorFX(id);
      if (windowContentCreator==null){
         logger.error("couldn't find a WindowContentCreatorFX for id {id}, now using the default");
         windowContentCreator = defaultWindowContentCreator;
      }
      checkIfServicesInjected(windowContentCreator);
      if (not windowContentCreator.supportType(type)){
         throw new IllegalStateException("windowContentCreator id:{id} does not supports content type:{type}");
      }
      try{
         var contentNode;
         if (windowContentCreator instanceof ScyToolWindowContentCreatorFX){
            contentNode = windowContentCreator.getScyWindowContentNew(scyWindow);
//            if (contentNode instanceof ScyTool){
//               scyWindow.scyTool = contentNode as ScyTool;
//               if (eloUri!=null){
//                  scyWindow.scyTool.loadElo(eloUri);
//               }
//               else{
//                  scyWindow.scyTool.newElo();
//               }
//            }
         }
         else{
            if (eloUri!=null){
               contentNode = windowContentCreator.getScyWindowContent(eloUri, scyWindow);
            }
            else{
               contentNode = windowContentCreator.getScyWindowContentNew(scyWindow);
            }
         }
         scyWindow.scyContent = contentNode;
      }
      catch (e:Exception){
         scyWindow.scyContent = getErrorNode(e,eloUri,id,type,windowContentCreator);
      }

      voidInspectContent(scyWindow);

      if (scyWindow.scyTool!=null){
         if (eloUri!=null){
            scyWindow.scyTool.loadElo(eloUri);
         }
         else{
            scyWindow.scyTool.newElo();
         }
      }

   }

   function getType(eloUri:URI):String{
      if (config.getExtensionManager()!=null){
         return config.getExtensionManager().getType(eloUri);
      }
      return UriUtils.getExtension(eloUri);
   }

   function voidInspectContent(scyWindow:ScyWindow){
      var scyContent = scyWindow.scyContent;
      if (scyContent instanceof ScyTool){
         scyWindow.scyTool = scyWindow.scyContent as ScyTool;
      }
      checkIfServicesInjected(scyContent);
   }

   function getErrorNode(e:Exception,eloUri:URI,id:String,type:String,windowContentCreator:WindowContentCreatorFX):Node{
      var textEditor = new TextEditor();
      textEditor.setEditable(false);
      textEditor.setText(getErrorMessage(e,eloUri,id,type,windowContentCreator));
      textEditor.resetScrollbars();
      return SwingComponent.wrap(textEditor);
   }

   function getErrorMessage(e:Exception,eloUri:URI,id:String,type:String,windowContentCreator:WindowContentCreatorFX):String{
      var charArrayWriter:CharArrayWriter;
      var printWriter:PrintWriter;
      try{
         charArrayWriter = new CharArrayWriter();
         printWriter = new PrintWriter(charArrayWriter);
         printWriter.println("An exception occured during the creation of the window content.");
         printWriter.println("ELO uri: {eloUri}");
         printWriter.println("ELO type: {type}");
         printWriter.println("Content creator id: {id}");
         printWriter.println("Content creator class: {windowContentCreator.getClass().getName()}");
         printWriter.println("Exception type: {e.getClass().getName()}");
         printWriter.println("Exception message: {e.getMessage()}");
         printWriter.println("Strack trace:");
         e.printStackTrace(printWriter);
         return charArrayWriter.toString();
      }
      finally{
         if (printWriter!=null){
            try{
               printWriter.close();
            }
            catch(e1){
               // what to do ???
            }
         }
         if (charArrayWriter!=null){
            try{
               charArrayWriter.close();
            }
            catch(e1){
               // what to do ???
            }
         }
      }
   }


}
