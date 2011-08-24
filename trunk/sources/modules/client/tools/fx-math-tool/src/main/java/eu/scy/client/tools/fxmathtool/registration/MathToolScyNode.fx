/*
 * MathToolNode.fx
 *
 * Created on 28.01.2010, 00:05:00
 */

package eu.scy.client.tools.fxmathtool.registration;

import javafx.scene.Node;
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
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import roolo.search.IQuery;
import roolo.search.Query;
import roolo.search.ISearchResult;
import javax.swing.JOptionPane;
import java.util.List;
import eu.scy.actionlogging.DevNullActionLogger;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import roolo.search.MetadataQueryComponent;
import roolo.search.IQueryComponent;
import roolo.search.SearchOperation;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.controller.SCYMathToolController;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;

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
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }

   function setLoggerEloUri() {
      var myEloUri:String = (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI();
      if (myEloUri == null and scyWindow.eloUri != null)
         myEloUri = scyWindow.eloUri.toString();
         
         ( mathTool.getMathToolController() as SCYMathToolController).setEloUri(myEloUri);
   }

   public override function initialize(windowContent:Boolean):Void{
       repository = toolBrokerAPI.getRepository();
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       eloFactory = toolBrokerAPI.getELOFactory();
       extensionManager = toolBrokerAPI.getExtensionManager();
       actionLogger = toolBrokerAPI.getActionLogger();
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      if(actionLogger == null){
         actionLogger = new DevNullActionLogger();
      }
      //( mathTool.getMathToolController() as SCYMathToolController);
      //setActionLogger(actionLogger,toolBrokerAPI.getLoginUserName(), toolname, "n/a", MathTool");
      setLoggerEloUri();
    }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton
                 ]
      }
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   function doLoadElo(eloUri:URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {

         mathTool.getMathToolController().open(newElo.getContent().getXmlString(), false);

         logger.info("elo mathtool loaded!");
         elo = newElo;
      }
      setLoggerEloUri();
   }

   function openElo() {
      var metadataQueryComponent:IQueryComponent = new MetadataQueryComponent(technicalFormatKey,
         SearchOperation.EQUALS, scyMathToolType);
      var query:IQuery = new Query(metadataQueryComponent);
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
   }

   override function postInitialize():Void {
   }

   public override function onQuit() {
      // mathTool.insertedTextToActionLog();
       if (elo!=null){
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml==newContentXml){
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (mathTool != null) {
         return eu.scy.client.desktop.desktoputils.UiUtils.createThumbnail(mathTool.getMainPanel(), mathTool.getMainPanel().getSize(), new Dimension(width, height));
      } else {
         return null;
      }
   }

}
