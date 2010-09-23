/*
 * CopexNode.fx
 *
 * Created on 13 janv. 2010, 17:20:24
 */

package eu.scy.client.tools.fxcopex.registration;

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
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.awt.Dimension;


/**
 * @author Marjolaine
 */

public class CopexNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {
   def logger = Logger.getLogger(this.getClass());
   def scyCopexType = "scy/xproc";
   def jdomStringConversion = new JDomStringConversion();

   public-init var scyCopexPanel:ScyCopexPanel;
   public-init var scyWindow: ScyWindow;
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var toolBrokerAPI: ToolBrokerAPI;

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedCopexPanel:Node;
   var technicalFormatKey: IMetadataKey;

   var elo:IELO;

   def spacing = 5.0;

   var bundle:ResourceBundleWrapper;

   public override function initialize(windowContent: Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      scyCopexPanel.setTBI(toolBrokerAPI);
      scyCopexPanel.setEloUri((scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
      scyCopexPanel.initActionLogger();
      scyCopexPanel.initCopex();
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   public override function create(): Node {
      bundle = new ResourceBundleWrapper(this);
      wrappedCopexPanel = ScySwingWrapper.wrap(scyCopexPanel);
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
                           text: getBundleString("FX-COPEX.MENU_SAVE");
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: getBundleString("FX-COPEX.MENU_SAVE_AS");
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
                  wrappedCopexPanel
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
         scyCopexPanel.setEloUri(eloUri.toString());
         scyCopexPanel.loadELO(newElo.getContent().getXmlString());
         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      eloSaver.eloUpdate(getElo(),this);
      scyCopexPanel.setEloUri(elo.getUri().toString());
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyCopexType);
      }
      elo.getContent().setXmlString(jdomStringConversion.xmlToString(scyCopexPanel.getExperimentalProcedure()));
      return elo;
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        scyCopexPanel.setEloUri(elo.getUri().toString());
    }

   function resizeContent(){
      Container.resizeNode(wrappedCopexPanel,width,height-wrappedCopexPanel.boundsInParent.minY-spacing);
   }

   public override function getPrefHeight(height: Number) : Number{
      return Container.getNodePrefHeight(wrappedCopexPanel, height)+wrappedCopexPanel.boundsInParent.minY+spacing;
   }

   public override function getPrefWidth(width: Number) : Number{
      return Container.getNodePrefWidth(wrappedCopexPanel, width);
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
      if (scyCopexPanel != null) {
        return UiUtils.createThumbnail(scyCopexPanel.getInterfacePanel(), scyCopexPanel.getRealSize(), new Dimension(width, height));
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
}
