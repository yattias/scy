/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoManager;
import javafx.scene.Node;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import eu.scy.client.desktop.scydesktop.scywindows.ShowInfoUrl;

/**
 * @author SikkenJ
 */
public class MoreInfoManagerImpl extends MoreInfoManager {

   def logger = Logger.getLogger(this.getClass());
   public override var activeLas on replace { activeLasChanged() };
   public-init var scene: Scene;
   public var windowStyler: WindowStyler;
   public var moreInfoToolFactory: MoreInfoToolFactory;
   def noLasColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   var colorScheme = noLasColorScheme;
   def moreInfoControl = InstructionWindowControl {
         windowColorScheme: bind colorScheme
         clickAction: showInstructionWindow
         layoutX: bind scene.width / 2.0
         layoutY: 0
      }
   def relativeWindowScreenBoder = 0.2;
   def instructionWindow: MoreInfoWindow = MoreInfoWindow {
         title: "Instruction"
         closeAction: hideInstructionWindow
//         content: Rectangle {
//            width: 1000
//            height: 1000
//            fill: Color.YELLOW
//         }
      }
   var instructionTool:ShowInfoUrl;

   public override function getControlNode(): Node {
      return moreInfoControl
   }

   function activeLasChanged() {
      logger.info("active las changed: {activeLas.id}");
      if (activeLas == null) {
         colorScheme = noLasColorScheme;
      } else {
         colorScheme = windowStyler.getWindowColorScheme(activeLas.mainAnchor.eloUri);
      }
   }

   function showInstructionWindow(): Void {
      initInstructionWindow();
      instructionWindow.windowColorScheme = colorScheme;
      instructionWindow.width = (1 - 2 * relativeWindowScreenBoder) * scene.width;
      instructionWindow.height = (1 - 1 * relativeWindowScreenBoder) * scene.height;
      instructionWindow.layoutY = 0.0;
      instructionWindow.layoutX = relativeWindowScreenBoder * scene.width;
      instructionTool.showInfoUrl(activeLas.instructionUri.toURL());
      insert instructionWindow into scene.content;
   }

   function hideInstructionWindow(): Void {
      delete instructionWindow from scene.content;
   }

   function initInstructionWindow():Void{
      if (instructionWindow.content==null){
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool();
         if (instructionWindow.content instanceof ShowInfoUrl){
            instructionTool = instructionWindow.content as ShowInfoUrl
         }
      }
   }

}
