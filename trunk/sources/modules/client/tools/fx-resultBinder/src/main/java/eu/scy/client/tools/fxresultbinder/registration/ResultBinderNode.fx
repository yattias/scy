/*
 * ResultBinderNode.fx
 *
 * Created on 13 janv. 2010, 17:20:24
 */

package eu.scy.client.tools.fxresultbinder.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.Container;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.client.desktop.scydesktop.utils.UiUtils;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import roolo.elo.api.IELOFactory;
import roolo.api.IRepository;
import eu.scy.common.configuration.Configuration;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;


/**
 * @author Marjolaine
 */

public class ResultBinderNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {
   def logger = Logger.getLogger(this.getClass());
   def scyResultCardType = "scy/resultcard";
   def jdomStringConversion = new JDomStringConversion();

   public-init var scyResultBinderPanel:ScyResultBinderPanel;
   public-init var scyWindow: ScyWindow;
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var toolBrokerAPI: ToolBrokerAPI;

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedResultBinderPanel:Node;
   var technicalFormatKey: IMetadataKey;

   var elo:IELO;

   def spacing = 5.0;

   var bundle:ResourceBundleWrapper;

   def config:Configuration=Configuration.getInstance();
   def filestreamerServer:String = config.getFilestreamerServer();
   def filestreamerContext:String = config.getFilestreamerContext();
   def filestreamerPort:String = config.getFilestreamerPort();
   //this should look like:
   //"http://scy.collide.info:8080/webapp/common/filestreamer.html";
   public def IMAGE_BASE_DIR = "http://{filestreamerServer}:{filestreamerPort}/{filestreamerContext}";
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }

   public override function initialize(windowContent: Boolean):Void{
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       repository = toolBrokerAPI.getRepository();
       eloFactory = toolBrokerAPI.getELOFactory();

      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      scyResultBinderPanel.setTBI(toolBrokerAPI);
      scyResultBinderPanel.setEloUri((scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
      scyResultBinderPanel.initActionLogger();
      scyResultBinderPanel.setUserName();
      scyResultBinderPanel.setPicture("{IMAGE_BASE_DIR}?username={toolBrokerAPI.getLoginUserName()}");
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

   public override function create(): Node {
      bundle = new ResourceBundleWrapper(this);
      wrappedResultBinderPanel = ScySwingWrapper.wrap(scyResultBinderPanel);
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
                        Button {
                           text: getBundleString("FX-RESULTBINDER.MENU_SAVE");
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: getBundleString("FX-RESULTBINDER.MENU_SAVE_AS");
                           action: function() {
                              doSaveAsElo();
                           }
                        }
//                        Button {
//                           text: "test thumbnail"
//                           action: function () {
//                                testThumbnail();
//                           }
//                        }
                     ]
                  }
                  wrappedResultBinderPanel
               ]
            }
         ]
      };
   }

   function doLoadElo(eloUri:URI)
   {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         scyResultBinderPanel.setEloUri(eloUri.toString());
         scyResultBinderPanel.loadELO(newElo.getContent().getXmlString());
         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      eloSaver.eloUpdate(getElo(),this);
      scyResultBinderPanel.setEloUri(elo.getUri().toString());
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyResultCardType);
      }
      elo.getContent().setXmlString(jdomStringConversion.xmlToString(scyResultBinderPanel.getResultCard()));
      return elo;
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        scyResultBinderPanel.setEloUri(elo.getUri().toString());
    }

   function resizeContent(){
      Container.resizeNode(wrappedResultBinderPanel,width,height-wrappedResultBinderPanel.boundsInParent.minY-spacing);
   }

   public override function getPrefHeight(height: Number) : Number{
      return Container.getNodePrefHeight(wrappedResultBinderPanel, height)+wrappedResultBinderPanel.boundsInParent.minY+spacing;
   }

   public override function getPrefWidth(width: Number) : Number{
      return Container.getNodePrefWidth(wrappedResultBinderPanel, width);
   }


   public override function getMinHeight() : Number{
      return 350;
   }

   public override function getMinWidth() : Number{
      return 550;
   }

   public function getBundleString(key:String) : String{
       return bundle.getString(key);
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      if (scyResultBinderPanel != null) {
        return UiUtils.createThumbnail(scyResultBinderPanel.getInterfacePanel(), scyResultBinderPanel.getRealSize(), new Dimension(width, height));
      } else {
        return null;
      }
    }

    public function testThumbnail(): Void {
        var thumbnail = getThumbnail(64, 64);
        var icon = new ImageIcon(thumbnail);
        JOptionPane.showMessageDialog(null,
            "Look at this!",
            "thumbnail test",
            JOptionPane.INFORMATION_MESSAGE,
            icon);
    }

    public override function onQuit():Void{
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


}
