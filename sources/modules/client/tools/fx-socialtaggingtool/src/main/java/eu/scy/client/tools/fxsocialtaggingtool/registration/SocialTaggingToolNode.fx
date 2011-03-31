/*
 * SocialTaggingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */
package eu.scy.client.tools.fxsocialtaggingtool.registration;

import java.net.URI;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import javafx.geometry.Insets;
import javafx.util.Math;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;

/**
 * @author sikkenj
 */
// place your code here
public class SocialTaggingToolNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

    def logger = Logger.getLogger(this.getClass());
    def scySocialTaggingType = "scy/socialtagging";
    def jdomStringConversion = new JDomStringConversion();
    public-init var scyWindow: ScyWindow;
    public var toolBrokerAPI: ToolBrokerAPI;
    public var eloFactory: IELOFactory;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository: IRepository;
    public override var width on replace { sizeChanged() };
    public override var height on replace { sizeChanged() };
    var wrappedWhiteboardPanel: Node;
    var technicalFormatKey: IMetadataKey;
    var elo: IELO;
    var nodeBox: VBox;
    var buttonBox: HBox;
    def spacing = 5.0;

//   def cached = bind scyWindow.cache on replace {
//         wrappedWhiteboardPanel.cache = cached;
//         println("changed wrappedWhiteboardPanel.cache to {wrappedWhiteboardPanel.cache}");
//      }
    public override function initialize(windowContent: Boolean): Void {
        metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
        repository = toolBrokerAPI.getRepository();
        eloFactory = toolBrokerAPI.getELOFactory();

        technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
    }

    public override function loadElo(uri: URI) {
        doLoadElo(uri);
    }

    public override function onQuit(): Void {
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

//   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
//        //return UiUtils.createThumbnail(whiteboardPanel, whiteboardPanel.getSize(), new Dimension(width, height));
//    }
    public override function create(): Node {
        //wrappedWhiteboardPanel = ScySwingWrapper.wrap(whiteboardPanel);
        //wrappedWhiteboardPanel.cache = true;
//        LayoutInfo {
//                    hfill: true
//                    vfill: true
//                       hgrow: Priority.ALWAYS
//                    vgrow: Priority.ALWAYS
//        }

        nodeBox = VBox {
                    managed: false
                    blocksMouse: true;
                    spacing: spacing;
                    content: [
                        buttonBox = HBox {
                                    padding: Insets {
                                        left: spacing
                                        top: spacing
                                        right: spacing
                                    }
                                    spacing: spacing;
                                    content: [
                                        Button {
                                            text: ##"Save"
                                            action: function() {
                                                doSaveElo();
                                            }
                                        }
                                        Button {
                                            text: ##"Save as"
                                            action: function() {
                                                doSaveAsElo();
                                            }
                                        }
                                    ]
                                }
                    //wrappedWhiteboardPanel
                    ]
                }
    }

    function doLoadElo(eloUri: URI) {
        logger.info("Trying to load elo {eloUri}");
        var newElo = repository.retrieveELO(eloUri);
        if (newElo != null) {
            //whiteboardPanel.deleteAllWhiteboardContainers();
            //whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent().getXmlString()));
            logger.info("elo loaded");
            elo = newElo;
        }
    }

    function doSaveElo() {
        eloSaver.eloUpdate(getElo(), this);
    }

    function doSaveAsElo() {
        eloSaver.eloSaveAs(getElo(), this);
    }

    function getElo(): IELO {
        if (elo == null) {
            elo = eloFactory.createELO();
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scySocialTaggingType);
        }
        //elo.getContent().setXmlString(jdomStringConversion.xmlToString(whiteboardPanel.getContentStatus()));
        return elo;
    }

    override public function eloSaveCancelled(elo: IELO): Void {
    }

    override public function eloSaved(elo: IELO): Void {
        this.elo = elo;
    }

   public override function getDrawerUIIndicator(): DrawerUIIndicator{
      return DrawerUIIndicator.SOCIAL_TAGGING;
   }

   function sizeChanged(): Void {
        Container.resizeNode(nodeBox, width, height);
    }

    public override function getPrefHeight(h: Number): Number {
        wrappedWhiteboardPanel.boundsInParent.minY + Container.getNodePrefHeight(wrappedWhiteboardPanel, h);
    }

    public override function getPrefWidth(w: Number): Number {
        wrappedWhiteboardPanel.boundsInParent.minX + Container.getNodePrefWidth(wrappedWhiteboardPanel, w);
    }

    public override function getMinHeight(): Number {
        wrappedWhiteboardPanel.boundsInParent.minY + Math.max(30, Container.getNodeMinHeight(wrappedWhiteboardPanel));
    }

    public override function getMinWidth(): Number {
        Math.max(buttonBox.getMinWidth(), Container.getNodeMinWidth(wrappedWhiteboardPanel))
    }

}
