/**
 * Student Planning Tool
 */

package eu.scy.client.tools.studentplanningtool.registration;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import javafx.ext.swing.SwingComponent;
import eu.scy.client.desktop.scydesktop.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import javafx.ext.swing.SwingComponent;
import eu.scy.tools.planning.controller.*;
import eu.scy.tools.planning.*;
import roolo.elo.metadata.BasicMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import javax.swing.JComponent;
import java.awt.Dimension;
import javafx.scene.layout.VBox;


/**
 * @@author aperritano
 */

 // place your code here
public class StudentPlanningToolNode extends CustomNode,ScyToolFX, Resizable {

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    public-init var eloSTPWrapper:EloStudenPlanningActionWrapper;


    public var wrappedSPTPanel:SwingComponent;
    public var panel:JComponent;
    public var studentPlanningController:StudentPlanningController;
    public var studentPlanningTool:StudentPlanningTool;
    public var metadataTypeManager: IMetadataTypeManager;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
    };


    function setScyWindowTitle(){
        if (scyWindow == null) {
            return;
        }

        scyWindow.title = "Student Planning Tool";
        var eloUri = eloSTPWrapper.getEloUri();
        if (eloUri != null) {
            scyWindow.id = eloUri.toString();
        }
        else {
            scyWindow.id = "";
        }
    };

   public override function loadElo(eloUri:URI):Void{
        eloSTPWrapper.loadElo(eloUri);
        setScyWindowTitle();
   }
   public override function canAcceptDrop(object:Object):Boolean{
      return true;
   }

   public override function acceptDrop(object:Object):Void{
     

      if( object instanceof ContactFrame ) {
         var cf = object as ContactFrame;
         studentPlanningTool.acceptDrop(cf.contact.awarenessUser);
      } else if( object instanceof BasicMetadata) {
         var elo = object as BasicMetadata;


          println("METADATATYPEMANAGER {metadataTypeManager}");

         var anchorIdKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.CONTAINS_ASSIGMENT_ELO.getId());

          println("ANCHOR ID {anchorIdKey}");

         println("ANCHOR Value {elo.getMetadataValueContainer(anchorIdKey).getValue()}");
         studentPlanningTool.acceptDrop(elo);
      }

     println("object {object}");

   }

    public override function create(): Node {
     //initTBI();
    // wrappedSPTPanel = studentPlanningTool.createStudentPlanningPanel();
        studentPlanningController = new StudentPlanningController(null);
        studentPlanningTool = new StudentPlanningTool(studentPlanningController);
        panel = studentPlanningTool.createStudentPlanningPanel();
        wrappedSPTPanel = SwingComponent.wrap(panel);
        
        return VBox {
            blocksMouse:true;
            cache:false;
            content:wrappedSPTPanel;
        };
   }

   def spacing = 5.0;

   
     function resizeContent(){
//      println("wrappedTextEditor.boundsInParent: {wrappedTextEditor.boundsInParent}");
//      println("wrappedTextEditor.layoutY: {wrappedTextEditor.layoutY}");
//      println("wrappedTextEditor.translateY: {wrappedTextEditor.translateY}");
      //var size = new Dimension(width,height-wrappedSPTPanel.boundsInParent.minY-5);
      // setPreferredSize is needed
      //panel.setPreferredSize(size);
      //panel.resizeChat(width, height-wrappedSPTPanel.boundsInParent.minY-spacing);
      // setSize is not visual needed
      // but set it, so the component react to it
      //panel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height


       var size = new Dimension(width,height-wrappedSPTPanel.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      panel.setPreferredSize(size);
      studentPlanningTool.resizeSPT(width, height-wrappedSPTPanel.boundsInParent.minY-spacing);
      // setSize is not visual needed
      // but set it, so the component react to it
      panel.setSize(size);
   }

   public override function getPrefHeight(width: Number) : Number{
      return panel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return panel.getPreferredSize().getWidth();
   }


}
