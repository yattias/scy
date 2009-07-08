/*
 * NewScyWindowTool.fx
 *
 * Created on 7-jul-2009, 17:01:29
 */

package eu.scy.client.desktop.scydesktop.corners.tools;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;

import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javax.swing.JOptionPane;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikkenj
 */

// place your code here
public class NewScyWindowTool extends CustomNode {

   public var scyDesktop: ScyDesktop;

   var newWindowCounter = 0;

   public override function create(): Node {
      return Group {
         content: [
            Button {
               text: "New"
               action: createNewScyWindow
            }
         ]
      };
   }

   function createNewScyWindow(){
      var eloTypeName = getNewEloTypeName();
      if (eloTypeName!=null){
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType!=null){
            var title = "new {eloTypeName} {++newWindowCounter}";
            var window:ScyWindow = ScyWindow{
               title:title
               eloType:eloType;
               id:"new://{title}"
               allowClose: true;
               allowResize: true;
               allowRotate: true;
               allowMinimize: true;
            }
            scyDesktop.addScyWindow(window);
         }
      }
   }

   function getNewEloTypeName():String{
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      if (typeNames.length>0){
         var choice = JOptionPane.showInputDialog(null, "Select type", "Create new ELO", JOptionPane.QUESTION_MESSAGE, null, typeNames, null) as String;
         return choice;
      }
      return null;
   }

   function getNewEloType():String{
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      if (typeNames.length>0){
         var choice = JOptionPane.showInputDialog(null, "Select type", "Create new ELO", JOptionPane.QUESTION_MESSAGE, null, typeNames, null) as String;
         if (choice!=null){
            return scyDesktop.newEloCreationRegistry.getEloType(choice);
         }
      }
      return null;
   }

}

