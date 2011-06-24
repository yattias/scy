/*
 * RichTextEditorNode.fx
 *
 * Created on 28.01.2010, 00:05:00
 */

package eu.scy.client.tools.fxrichtexteditor.registration;

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
import org.jdom.Element;
import eu.scy.client.common.richtexteditor.RichTextEditor;

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
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.collaboration.api.CollaborationStartable;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import java.lang.System;

/**
 * @author kaido
 */

public class RichTextEditorScyNode extends INotifiable, RichTextEditorNode, ScyToolFX, EloSaverCallBack, CollaborationStartable {
   def logger = Logger.getLogger("eu.scy.client.tools.fxrichtexteditor.RichTextEditorNode");
   def scyRichTextEditorType = "scy/rtf";
   def jdomStringConversion = new JDomStringConversion();
   def toolname = "richtext";
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var toolBrokerAPI:ToolBrokerAPI;
//   public var extensionManager:IExtensionManager;
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
   def richTextTagName = "RichText";
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }
   public var syncSession: ISyncSession;
   var collaborative: Boolean = false;

   function setLoggerEloUri() {
      var myEloUri:String = (scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI();
      if (myEloUri == null and scyWindow.eloUri != null)
         myEloUri = scyWindow.eloUri.toString();
      richTextEditor.setEloUri(myEloUri);
   }

   public override function initialize(windowContent:Boolean):Void{
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       repository = toolBrokerAPI.getRepository();
       eloFactory = toolBrokerAPI.getELOFactory();
       actionLogger = toolBrokerAPI.getActionLogger();
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      if (actionLogger==null) {
         actionLogger = new DevNullActionLogger();
      }
      richTextEditor.setRichTextEditorLogger(actionLogger,
        toolBrokerAPI.getLoginUserName(), toolname, toolBrokerAPI.getMission(), "n/a",
        "richtext");
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
         eloContentXmlToRichText(newElo.getContent().getXmlString());
         logger.info("elo rtf loaded");
         elo = newElo;
      }
      setLoggerEloUri();
   }

   function openElo() {
      var metadataQueryComponent:IQueryComponent = new MetadataQueryComponent(technicalFormatKey,SearchOperation.EQUALS, scyRichTextEditorType);
      var query:IQuery = new Query(metadataQueryComponent);
      var searchResults:List = repository.search(query);
      var richTextUris:URI[];
      for (searchResult in searchResults)
         insert (searchResult as ISearchResult).getUri() into richTextUris;
      var richTextUri:URI = JOptionPane.showInputDialog(null, selectFormattedText,
      selectFormattedText, JOptionPane.QUESTION_MESSAGE, null, richTextUris, null) as URI;
      if (richTextUri != null) {
         loadElo(richTextUri);
      }
   }

   function doSaveElo(){
      elo.getContent().setXmlString(richTextToEloContentXml(richTextEditor.getRtfText()));
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
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyRichTextEditorType);
      }
      elo.getContent().setXmlString(richTextToEloContentXml(richTextEditor.getRtfText()));
      return elo;
   }

   function richTextToEloContentXml(text:String):String{
      var textElement= new Element(richTextTagName);
      textElement.setText(text);
      return jdomStringConversion.xmlToString(textElement);
   }

   function eloContentXmlToRichText(text:String) {
      var richTextElement=jdomStringConversion.stringToXml(text);
      if (richTextTagName != richTextElement.getName()){
         logger.error("wrong tag name, expected {richTextTagName}, but got {richTextElement.getName()}");
      }
      richTextEditor.setText(richTextElement.getText().trim());
    }

   public override function create(): Node {
      richTextEditor = new RichTextEditor(false, authorMode);
      richTextEditor.setTypingLogIntervalMs(typingLogIntervalMs);
      wrappedRichTextEditor = ScySwingWrapper.wrap(richTextEditor,true);
   }

   override function postInitialize():Void {
   }

   public override function onQuit() {
       richTextEditor.insertedTextToActionLog();
      if (elo != null) {
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (richTextEditor != null) {
         return eu.scy.client.desktop.desktoputils.UiUtils.createThumbnail(richTextEditor, richTextEditor.getSize(), new Dimension(width, height));
      } else {
         return null;
      }
   }

    public override function processNotification (notification: INotification) : Boolean {
        System.out.println(notification.getSender());
        System.out.println(notification.getMission());
        System.out.println(notification.getUserId());
        System.out.println(notification.getToolId());
        System.out.println(notification.getFirstProperty("message"));


        if (notification.getSender().equals("eu.scy.agents.hypothesis.HypothesisDecisionMakerAgent")) {
            var messageFromAgent = notification.getFirstProperty("message");
            if (not messageFromAgent.equals(""))
                javafx.stage.Alert.inform(messageFromAgent);
            return true;
        } else {
            return false;
        }

    }

    public override function startCollaboration(mucid: String) {
        if (not collaborative) {
            collaborative = true;
            FX.deferAction(function(): Void {
                def session : ISyncSession = toolBrokerAPI.getDataSyncService().joinSession(mucid, richTextEditor, RichTextEditor.TOOLNAME);
                richTextEditor.setSyncSession(session);
                session.addCollaboratorStatusListener(scyWindow.ownershipManager);
                session.refreshOnlineCollaborators();
                logger.debug("joined session, mucid: {mucid}");
            });
        }
    }

    public override function canAcceptDrop(object: Object): Boolean {
        logger.debug("canAcceptDrop of {object.getClass()}");
        if (object instanceof ContactFrame) {
            var c: ContactFrame = object as ContactFrame;
            if (not scyWindow.ownershipManager.isOwner(c.contact.name)) {
                return true;
            }
        }
        return false;
    }

    public override function acceptDrop(object: Object): Void {
        logger.debug("acceptDrop of {object.getClass()}");
        var c: ContactFrame = object as ContactFrame;
        logger.debug("acceptDrop user: {c.contact.name}");
        scyWindow.ownershipManager.addPendingOwner(c.contact.name);
        scyWindow.windowManager.scyDesktop.config.getToolBrokerAPI().proposeCollaborationWith("{c.contact.awarenessUser.getJid()}/Smack", scyWindow.eloUri.toString(), scyWindow.mucId);
        logger.debug("scyDesktop: {scyWindow.windowManager.scyDesktop}");
    }

}
