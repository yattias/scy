/*
 * ScyToolViewer.fx
 *
 * Created on 12-jan-2010, 15:34:49
 */
package eu.scy.client.desktop.scydesktop.tools.scytoolviewer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import java.net.URI;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Container;
import java.text.SimpleDateFormat;
import java.util.Date;
import roolo.api.IRepository;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.api.IExtensionManager;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELOFactory;
import eu.scy.client.common.datasync.IDataSyncService;
import javafx.scene.control.TextBox;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.util.Math;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoManager;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;

/**
 * @author sikken
 */
// place your code here
public class ScyToolViewer extends CustomNode, Resizable, ScyToolFX {

   public override var width on replace { sizeChanged() };
   public override var height on replace { sizeChanged() };
   public var toolBrokerAPI: ToolBrokerAPI on replace { serviceSet("toolBrokerAPI", toolBrokerAPI) };
   public var repository: IRepository on replace { serviceSet("repository", repository) };
   public var extensionManager: IExtensionManager on replace { serviceSet("extensionManager", extensionManager) };
   public var metadataTypeManager: IMetadataTypeManager on replace { serviceSet("metadataTypeManager", metadataTypeManager) };
   public var eloFactory: IELOFactory on replace { serviceSet("eloFactory", eloFactory) };
   public var actionLogger: IActionLogger on replace { serviceSet("actionLogger", actionLogger) };
   public var awarenessService: IAwarenessService on replace { serviceSet("awarenessService", awarenessService) };
   public var dataSyncService: IDataSyncService on replace { serviceSet("dataSyncService", dataSyncService) };
   public var pedagogicalPlanService: PedagogicalPlanService on replace { serviceSet("pedagogicalPlanService", pedagogicalPlanService) };
   public var scyWindow: ScyWindow on replace { addMessage("scyWindow set ({scyWindow.title})") };
   public var authorMode: Boolean on replace { addMessage("authorMode set ({authorMode})") };
   public var moreInfoManager: MoreInfoManager on replace { addMessage("moreInfoManager set ({moreInfoManager})") };
   var uri = "?????";
   var location = "?";
   var assignmentUri: URI;
   var resourcesUri: URI;
   def spacing = 5.0;
   def dateTimeFormat = new SimpleDateFormat("HH:mm:ss");
   def minimumWidth = 250;
   def textBox: TextBox = TextBox {
         multiline: true
         lines: 3
         editable: false
         layoutInfo: LayoutInfo {
            hfill: true
            vfill: true
            hgrow: Priority.ALWAYS
            vgrow: Priority.ALWAYS
         }
      }
   var nodeBox: VBox;

   init {
      addMessage("class init");
   }

   postinit {
      addMessage("class postinit");
   }

   public override function create(): Node {
      addMessage("CustomNode.create");
      return nodeBox = VBox {
               managed: false
               spacing: spacing;
               padding: Insets {
                  top: spacing
               }
               content: [
                  Text {
                     font: Font {
                        size: 12
                     }
                     x: 0,
                     y: 0
                     content: bind "EU - {uri}"
                  }
                  Text {
                     font: Font {
                        size: 12
                     }
                     x: 0,
                     y: 0
                     content: bind "A - {assignmentUri}"
                  }
                  Text {
                     font: Font {
                        size: 12
                     }
                     x: 0,
                     y: 0
                     content: bind "R - {resourcesUri}"
                  }
                  Text {
                     font: Font {
                        size: 12
                     }
                     x: 0,
                     y: 0
                     content: bind "Location - {location}"
                  }
                  textBox
               ]
            };
   }

   function serviceSet(name: String, service: Object) {
      var message = "service {name} set";
      if (service == null) {
         message = "{message}, but it is null!!!";
      }
      addMessage(message);
   }

   function addMessage(message: String) {
      var newLine = "";
      var messages = textBox.text;
      if (messages.length() > 0) {
         newLine = "\n";
      }
      textBox.text = "{textBox.text}{newLine}{dateTimeFormat.format(new Date())} - {message}";
   }

   public override function initialize(windowContent: Boolean): Void {
      addMessage("initialize({windowContent})");
      location = if (windowContent) "window" else "drawer";
   }

   public override function postInitialize(): Void {
      addMessage("postInitialize");
   }

   public override function newElo(): Void {
      addMessage("newElo");
      uri = "no yet set";
   }

   public override function loadElo(eloUri: URI): Void {
      addMessage("loadElo {eloUri}");
      setEloInfo(eloUri);
   }

   function setEloInfo(eloUri: URI): Void {
      var scyElo: ScyElo;
      if (eloUri != null) {
         scyElo = ScyElo.loadElo(eloUri, toolBrokerAPI);
      }
      uri = "{eloUri}";
      assignmentUri = scyElo.getAssignmentUri();
      resourcesUri = scyElo.getResourcesUri();
   }

   public override function loadedEloChanged(eloUri: URI): Void {
      addMessage("loadedEloChanged({eloUri})");
      setEloInfo(eloUri);
   }

   public override function onGotFocus(): Void {
      addMessage("onGotFocus");
   }

   public override function onLostFocus(): Void {
      addMessage("onLostFocus");
   }

   public override function onMinimized(): Void {
      addMessage("onMinimized");
   }

   public override function onUnMinimized(): Void {
      addMessage("onUnMinimized");
   }

   public override function onOpened(): Void {
      addMessage("onOpened");
   }

   public override function onClosed(): Void {
      addMessage("onClosed");
   }

   public override function aboutToClose(): Boolean {
      addMessage("aboutToClose");
      return true;
   }

   public override function onQuit(): Void {
      addMessage("onQuit");
   }

   public override function setEloSaver(eloSaver: EloSaver): Void {
   }

   public override function setMyEloChanged(myEloChanged: MyEloChanged): Void {
   }

   public override function canAcceptDrop(object: Object): Boolean {
      addMessage("canAcceptDrop of {object.getClass()}");
      return true;
   }

   public override function acceptDrop(object: Object): Void {
      addMessage("acceptDrop of {object.getClass()}");
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      addMessage("setTitleBarButtonManager({titleBarButtonManager},{windowContent})");
   }

   public override function getDrawerUIIndicator(): DrawerUIIndicator {
      addMessage("getDrawerUIIndicator");
      return DrawerUIIndicator.SCY_TOOL_VIEWER;
   }

   function sizeChanged(): Void {
      Container.resizeNode(nodeBox, width, height);
   //      println("ScyToolViewer: size changed to {width}*{height}");
   }

   public override function getPrefHeight(h: Number): Number {
      textBox.boundsInParent.minY + textBox.getPrefHeight(h);
   }

   public override function getPrefWidth(w: Number): Number {
      Math.max(minimumWidth, textBox.boundsInParent.minX + textBox.getPrefWidth(w));
   }

   public override function getMinHeight(): Number {
      textBox.boundsInParent.minY + textBox.getMinHeight();
   }

   public override function getMinWidth(): Number {
      Math.max(minimumWidth, textBox.getMinWidth());
   }

   public override function getMaxHeight(): Number {
      textBox.boundsInParent.minY + textBox.getMaxHeight();
   }

   public override function getMaxWidth(): Number {
      Math.max(minimumWidth, textBox.getMaxWidth());
   }

}
