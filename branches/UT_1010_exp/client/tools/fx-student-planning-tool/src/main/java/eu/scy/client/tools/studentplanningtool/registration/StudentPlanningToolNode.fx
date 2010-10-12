/**
 * Student Planning Tool
 */

package eu.scy.client.tools.studentplanningtool.registration;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;

import eu.scy.tools.service.LocalFreakinStudentPedagogicalPlanService;
import eu.scy.tools.service.StudentPlanELOParser;
import eu.scy.tools.planning.controller.*;
import eu.scy.tools.planning.*;
import roolo.elo.metadata.BasicMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.api.IELO;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import javax.swing.JComponent;
import javafx.scene.layout.VBox;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import javax.swing.JLabel;
import java.lang.Exception;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.scene.layout.Container;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;





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

    public var wrappedSPTPanel:Node;
    public var panel:JComponent;
    public var studentPlanningController:StudentPlanningController;
    
    public var studentPlanningTool:StudentPlanningTool;
    public var metadataTypeManager: IMetadataTypeManager;
    public var service: LocalFreakinStudentPedagogicalPlanService;
    public var repository:IRepository on replace {
        
    }

    public var technicalFormatKey: IMetadataKey;
    public var eloFactory:IELOFactory;
    public var toolBrokerAPI:ToolBrokerAPI on replace {
               println("tbi updated");
    };
    
    var elo:IELO;
    var errorLabel = new JLabel("There has been a problem with the server side services.");

    public var scyWindow:ScyWindow on replace {
    };


   public override function loadElo(eloUri:URI):Void{
       println("LOAD ELO SPT: {eloUri}");
       logger.info("Trying to load elo {eloUri}");
       elo = repository.retrieveELO(eloUri);
   }

   public override function canAcceptDrop(object:Object):Boolean{

         println("Can Accept drop called");

         if( object instanceof BasicMetadata) {
            var elo = object as BasicMetadata;

            var anchorIdKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID.getId());

            var value = elo.getMetadataValueContainer(anchorIdKey).getValue();
            println("can accept DROPPED anchhor id {value}");
            if( value == null) {
                println("i cant accept you, you are null");
                return false;
            }
        }


      return true;
   }

   public override function acceptDrop(object:Object):Void{

       println("Display {object}");

      if( object instanceof ContactFrame ) {
         var cf = object as ContactFrame;
         studentPlanningTool.acceptDrop(cf.contact.awarenessUser);
     }
      else if( object instanceof BasicMetadata) {
         var elo = object as BasicMetadata;


          println("METADATATYPEMANAGER {metadataTypeManager}");

         //Here somebody has changed the metadata keys that are used. This simply WORKED until somebody started to use other metadatakeys. Cannot explain it any other way!
         var anchorIdKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
          println("ANCHOR----ID {anchorIdKey}");
          var value = elo.getMetadataValueContainer(anchorIdKey).getValue();

         if( value != null) {
            println("DROPPED Value:: {value}");

            studentPlanningTool.acceptDrop(value);
            //studentPlanningTool.acceptDrop("HenrikIsCOOl");
         } else {
            println("accept drop, i cant accept you, you are null");
         }     
      }
      else {
        studentPlanningTool.acceptDrop(elo);
        }


   }

    public override function create(): Node {
        service = new LocalFreakinStudentPedagogicalPlanService(); 
        println("STARTING SPT create node.......");
        println("toolbroker on stp is {toolBrokerAPI}");

               
        var uri = scyWindow.eloUri;
        var studentPlan;
        var loginName = toolBrokerAPI.getLoginUserName();
        
        if( uri != null) {
            println( "ELO URI {uri}");

            elo = repository.retrieveELO(uri);

            println( "Retrieved ELO {elo}");

            var xmlString = elo.getContent().getXmlString();

            println( "xml String from ELO {xmlString}");

            if( xmlString == null ) {
                //need to create a new StudentPlanELO
                

                println("tbi login name: {loginName}");




                try {
                    studentPlan = service.createStudentPlan(loginName);
                } catch(e: Exception) {
                    logger.debug(e.getMessage());
                    wrappedSPTPanel = ScySwingWrapper.wrap(errorLabel);

                    return VBox {
                        blocksMouse:true;
                        cache:false;
                        content:wrappedSPTPanel;
                    };
                }

                println("we got a new SPT ID: {studentPlan.getId()}");

                elo.getContent().setXmlString(StudentPlanELOParser.parseToXML(studentPlan));

                var newMetadata = repository.updateELO(elo)
            } else {

               try {
               studentPlan = StudentPlanELOParser.parseFromXML(xmlString);
               service.setCurrentStudentPlanELO(studentPlan);
                println("STUDENT PLAN IS: {studentPlan} ");
               } catch(e: Exception) {
                logger.debug(e.getMessage());
                wrappedSPTPanel = ScySwingWrapper.wrap(errorLabel);

                return VBox {
                    blocksMouse:true;
                    cache:false;
                    content:wrappedSPTPanel;
                    };
                }

            }
        }

        studentPlanningController = new StudentPlanningController(studentPlan, toolBrokerAPI, service);
        studentPlanningController.setElo(elo);
        studentPlanningTool = new StudentPlanningTool(studentPlanningController);
        panel = studentPlanningTool.createStudentPlanningPanel();
        wrappedSPTPanel = ScySwingWrapper.wrap(panel);

        println("ENDING SPT create node.......");

        return VBox {
            blocksMouse:true;
            cache:false;
            content:wrappedSPTPanel;
        };
   }

   def spacing = 5.0;


   function resizeContent(){
       Container.resizeNode(wrappedSPTPanel,width,height);
   }

   public override function getPrefHeight(height: Number) : Number{
      Container.getNodePrefHeight(wrappedSPTPanel, height);
    }

   public override function getPrefWidth(width: Number) : Number{
      Container.getNodePrefWidth(wrappedSPTPanel, width);
   }


}