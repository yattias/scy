/**
 * Student Planning Tool
 */

package eu.scy.client.tools.studentplanningtool.registration;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import javafx.ext.swing.SwingComponent;
import org.apache.commons.lang.StringUtils;
import eu.scy.client.desktop.scydesktop.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;

import javafx.ext.swing.SwingComponent;
import eu.scy.tools.planning.controller.*;
import eu.scy.tools.planning.*;
import roolo.elo.metadata.BasicMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import javax.swing.JComponent;
import java.awt.Dimension;
import javafx.scene.layout.VBox;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import eu.scy.core.model.student.StudentPlanELO;





/**
 * @@author aperritano
 */

 // place your code here
public class StudentPlanningToolNode extends CustomNode,ScyToolFX, Resizable {

    def logger = Logger.getLogger(this.getClass());
    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};
     def scySPTType = "scy/studentplanningtool";
    def jdomStringConversion = new JDomStringConversion();

    public var wrappedSPTPanel:SwingComponent;
    public var panel:JComponent;
    public var studentPlanningController:StudentPlanningController;
    public var toolBrokerAPI:ToolBrokerAPI;
    public var studentPlanningTool:StudentPlanningTool;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository:IRepository;
    public var technicalFormatKey: IMetadataKey;
    public var eloFactory:IELOFactory;
    
    var elo:IELO;

    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
    };


    function setScyWindowTitle(){
        if (scyWindow == null) {
            return;
        }

        scyWindow.title = "Student Planning Tool";
      
    };

   public override function loadElo(eloUri:URI):Void{
       println("LOAD ELO SPT: {eloUri}");
       logger.info("Trying to load elo {eloUri}");
       elo = repository.retrieveELO(eloUri);
        setScyWindowTitle();
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scySPTType);
      }
      return elo;
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

         var anchorIdKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID.getId());

          println("ANCHOR ID {anchorIdKey}");

         println("ANCHOR Value {elo.getMetadataValueContainer(anchorIdKey).getValue()}");
         studentPlanningTool.acceptDrop(elo);
      }

     println("object {object}");

   }

    public override function create(): Node {
     //initTBI();
    // wrappedSPTPanel = studentPlanningTool.createStudentPlanningPanel();

        var uri = scyWindow.eloUri;
        elo = repository.retrieveELO(uri);

        var xmlString = elo.getContent().getXmlString();

        println( "xml String from ELO {xmlString}");

        var rootElement = jdomStringConversion.stringToXml(xmlString);

        var parsed = StringUtils.stripToNull(rootElement.getText());

        var studentPlan;
        if( parsed == null) {
            //need to create a new id
            studentPlan = toolBrokerAPI.getStudentPedagogicalPlanService().createStudentPlan(toolBrokerAPI.getLoginUserName());

            println("NEW SPT ID {studentPlan.getId()}");

            elo.getContent().setXmlString("<studentPlanId>{studentPlan.getId()}</studentPlanId>");

            var newMetadata = repository.updateELO(elo)
        } else {
            var s1 = StringUtils.remove(parsed, "<studentPlanId>");
            var finalId = StringUtils.remove(s1, "<studentPlanId>");

            studentPlan = toolBrokerAPI.getStudentPedagogicalPlanService().getStudentPlanELO(finalId);

        }

        println("toolbroker on stp is {toolBrokerAPI}");

        studentPlanningController = new StudentPlanningController(studentPlan, toolBrokerAPI);
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
