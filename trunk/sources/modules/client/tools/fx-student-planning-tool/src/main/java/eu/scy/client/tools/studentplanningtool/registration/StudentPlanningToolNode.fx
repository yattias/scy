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
import javax.swing.JLabel;
import java.lang.Exception;





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
    
    public var studentPlanningTool:StudentPlanningTool;
    public var metadataTypeManager: IMetadataTypeManager;
    public var repository:IRepository on replace {
        
    }

    public var technicalFormatKey: IMetadataKey;
    public var eloFactory:IELOFactory;
    public var toolBrokerAPI:ToolBrokerAPI on replace {
               println("tbi updated");
    };
    
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

   public override function canAcceptDrop(object:Object):Boolean{
      return true;
   }

   public override function acceptDrop(object:Object):Void{

       println("Display {object}");

      if( object instanceof ContactFrame ) {
         var cf = object as ContactFrame;
         studentPlanningTool.acceptDrop(cf.contact.awarenessUser);
      } else if( object instanceof BasicMetadata) {
         var elo = object as BasicMetadata;


          println("METADATATYPEMANAGER {metadataTypeManager}");

         var anchorIdKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID.getId());

          println("ANCHOR ID {anchorIdKey}");

          var value = elo.getMetadataValueContainer(anchorIdKey).getValue();

         if( value != null) {
            println("DROPPED Value {value}");

            studentPlanningTool.acceptDrop(value);
         } else {
            println("i cant accept you, you are null");
         }


         
      }

   }

    public override function create(): Node {
     //initTBI();
    // wrappedSPTPanel = studentPlanningTool.createStudentPlanningPanel();

        var errorLabel = new JLabel("There has been a problem with the server side services.");
        println( "STARTING SPT create node");
        println("toolbroker on stp is {toolBrokerAPI}");

                

        var uri = scyWindow.eloUri;
        var studentPlan;
        if( uri != null) {
            println( "ELO URI {uri}");

            elo = repository.retrieveELO(uri);

            println( "Retrieved ELO {elo}");

            var xmlString = elo.getContent().getXmlString();

            println( "xml String from ELO {xmlString}");

            if( xmlString == null) {
                //need to create a new id
                var loginName = toolBrokerAPI.getLoginUserName();

                println("tbi login name: {loginName}");

                try {
                    studentPlan = toolBrokerAPI.getStudentPedagogicalPlanService().createStudentPlan(loginName);
                } catch(e: Exception) {
                    logger.debug(e.getMessage());
                    wrappedSPTPanel = SwingComponent.wrap(errorLabel);

                    return VBox {
                        blocksMouse:true;
                        cache:false;
                        content:wrappedSPTPanel;
                    };
                }

                println("NEW SPT ID {studentPlan.getId()}");

                elo.getContent().setXmlString("<studentPlanId>{studentPlan.getId()}</studentPlanId>");

                var newMetadata = repository.updateELO(elo)
            } else {
                var rootElement = jdomStringConversion.stringToXml(xmlString);
                println("ROOT ELEMENT {rootElement}");
                var parsed = StringUtils.stripToNull(rootElement.getText());

                println("FOUND ID {parsed}");

                try {
                    studentPlan = toolBrokerAPI.getStudentPedagogicalPlanService().getStudentPlanELO(parsed);
                } catch(e: Exception) {
                    logger.debug(e.getMessage());
                    wrappedSPTPanel = SwingComponent.wrap(errorLabel);

                    return VBox {
                        blocksMouse:true;
                        cache:false;
                        content:wrappedSPTPanel;
                    };
                }
            }
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