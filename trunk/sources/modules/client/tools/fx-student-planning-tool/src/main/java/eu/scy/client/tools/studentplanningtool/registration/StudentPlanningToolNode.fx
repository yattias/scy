/**
 * Student Planning Tool
 */

package eu.scy.client.tools.studentplanningtool.registration;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import javafx.ext.swing.SwingComponent;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import javafx.ext.swing.SwingComponent;
import eu.scy.client.tools.studentplanningtool.*;
import javafx.scene.Group;
import javafx.scene.Node;



/**
 * @@author aperritano
 */

 // place your code here
public class StudentPlanningToolNode extends CustomNode,ScyToolFX{

    public-init var eloChatActionWrapper:EloStudenPlanningActionWrapper;


    public var wrappedSPTPanel:SwingComponent;
    public var studentPlanningController:StudentPlanningController;
    public var studentPlanningTool:StudentPlanningTool;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
    };


    function setScyWindowTitle(){
        if (scyWindow == null) {
            return;
        }

        scyWindow.title = "Student Planning Tool: {eloChatActionWrapper.getDocName()}";
        var eloUri = eloChatActionWrapper.getEloUri();
        if (eloUri != null) {
            scyWindow.id = eloUri.toString();
        }
        else {
            scyWindow.id = "";
        }
    };

   public override function loadElo(eloUri:URI):Void{
        eloChatActionWrapper.loadElo(eloUri);
        setScyWindowTitle();
   }
   public override function canAcceptDrop(object:Object):Boolean{
      return true;
   }

   public override function acceptDrop(object:Object):Void{
      println("object {object}");

/**
      if( object instanceof ContactFrame ) {
         var cf = object as ContactFrame;
         studentPlanningTool.acceptDrop(cf.contact.awarenessUser);
      } else if( object instanceof BasicMetadata) {
         var elo = object as BasicMetadata;
         studentPlanningTool.acceptDrop(elo);
      }
**/

    

   }

   public override function create(): Node {
     //initTBI();
    // wrappedSPTPanel = studentPlanningTool.createStudentPlanningPanel();
        studentPlanningController = new StudentPlanningController(null);
        studentPlanningTool = new StudentPlanningTool(studentPlanningController);
        wrappedSPTPanel = SwingComponent.wrap(studentPlanningTool.createStudentPlanningPanel());
     return Group {
         blocksMouse:true;
         cache:false;
         content:

            wrappedSPTPanel;



      };
   }
}
