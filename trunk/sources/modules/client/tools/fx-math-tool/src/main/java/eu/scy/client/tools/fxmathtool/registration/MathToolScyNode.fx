/*
 * MathToolNode.fx
 *
 * Created on 28.01.2010, 00:05:00
 */

package eu.scy.client.tools.fxmathtool.registration;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.api.IExtensionManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import javax.swing.JOptionPane;
import java.util.List;
import eu.scy.actionlogging.DevNullActionLogger;
import org.jdom.Element;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.controller.SCYMathToolController;

/**
 * @author kaido
 */

public class MathToolScyNode extends MathToolNode, ScyToolFX, EloSaverCallBack {
   def logger = Logger.getLogger("eu.scy.client.tools.fxmathtool.MathToolScyNode");
   def scyMathToolType = "scy/mathtool";
   def jdomStringConversion = new JDomStringConversion();
   def toolname = "MathTool";
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var toolBrokerAPI:ToolBrokerAPI;
   public var extensionManager:IExtensionManager;
   public var actionLogger:IActionLogger;
   public var awarenessService:IAwarenessService;
   public var dataSyncService:IDataSyncService;
   public var pedagogicalPlanService:PedagogicalPlanService;
   public var scyWindow:ScyWindow;
   public var authorMode:Boolean;
   // interval in milliseconds after what typed text is wrote
   // into action log - should be configurable from authoring tools
   public var typingLogIntervalMs = 30000;
   var elo:IELO;
   var technicalFormatKey: IMetadataKey;
   var selectFormattedText = ##"Select formatted text";
   var openLabel = ##"Open ELO";
   var saveLabel = ##"Save ELO";
   var saveAsLabel = ##"Save ELO as";
   def mathToolTagName = "MathToolTag";

   function setLoggerEloUri() {
      var myEloUri:String = (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI();
      if (myEloUri == null and scyWindow.eloUri != null)
         myEloUri = scyWindow.eloUri.toString();
    //  mathTool.setEloUri(myEloUri);
   }

   public override function initialize(windowContent:Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      if (actionLogger==null) {
         actionLogger = new DevNullActionLogger();
      }
      //mathTool.setRichTextEditorLogger(actionLogger,
        //toolBrokerAPI.getLoginUserName(), toolname, toolBrokerAPI.getMission(), "n/a",
       // "MathTool");
      setLoggerEloUri();
    }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   function doLoadElo(eloUri:URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
//         mathTool.getMathToolController().open(newElo.getContent().getXmlString());
         logger.info("elo mathtool loaded!");
         elo = newElo;
      }
      setLoggerEloUri();
   }

   function openElo() {
      var query:IQuery = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyMathToolType);
      var searchResults:List = repository.search(query);
      var mathToolTextUris:URI[];
      for (searchResult in searchResults)
         insert (searchResult as ISearchResult).getUri() into mathToolTextUris;
      var richTextUri:URI = JOptionPane.showInputDialog(null, selectFormattedText,
      selectFormattedText, JOptionPane.QUESTION_MESSAGE, null, mathToolTextUris, null) as URI;
      if (richTextUri != null) {
         loadElo(richTextUri);
      }
   }

   function doSaveElo(){
      elo.getContent().setXmlString(mathTool.getMathToolController().save());
      eloSaver.eloUpdate(getElo(),this);
   }

   function doSaveAsElo(){
     eloSaver.eloSaveAs(getElo(),this);
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        setLoggerEloUri();
    }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyMathToolType);
      }
      elo.getContent().setXmlString(mathTool.getMathToolController().save());
      return elo;
   }

   public override function create(): Node {
      mathTool = new MathTool(new SCYMathToolController());
     
     wrappedMathTool = ScySwingWrapper.wrap(mathTool.createMathTool(0, 0),true);

      resizeContent();
      FX.deferAction(resizeContent);
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:spacing;
               spacing:spacing;
               content:[
                  HBox{
                     translateX:spacing;
                     spacing:spacing;
                     content:[
                        /* 100301 Jakob, commented out open button, it should not be in the tool
                        Button {
                           text: openLabel
                           action: function() {
                               openElo();
                           }
                        }
                        */
                        Button {
                           text: saveLabel
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: saveAsLabel
                           action: function() {
    							doSaveAsElo();
                           }
                        }
                     ]
                  }
                  wrappedMathTool
               ]
            }
         ]
      };
   }

   override function postInitialize():Void {
   }

   public override function onQuit() {
      // mathTool.insertedTextToActionLog();
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (mathTool != null) {
         return eu.scy.client.desktop.scydesktop.utils.UiUtils.createThumbnail(mathTool.getMainPanel(), mathTool.getMainPanel().getSize(), new Dimension(width, height));
      } else {
         return null;
      }
   }

}
