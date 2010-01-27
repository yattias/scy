/*
 * ExternalDoc.fx
 *
 * Created on 20-jan-2010, 12:05:25
 */

package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import java.net.URI;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import javax.swing.JFileChooser;
//import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.awt.Component;
import javafx.stage.Stage;
import javafx.scene.Scene;

import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import org.jdom.Element;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.ExternalDocAnnotation;

import javafx.scene.control.Label;
import javafx.scene.layout.Resizable;
import javafx.scene.control.TextBox;
import javafx.scene.control.CheckBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import eu.scy.client.desktop.scydesktop.ScyRooloMetadataKeyIds;


/**
 * @author sikken
 */

public class ExternalDoc extends CustomNode,Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());

   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var technicalType:String;
   public-init var extensions:String[];

   var elo:IELO;


    var eloImporterModel:EloImporterModel = new EloImporterModel();
    def fileChooser = new JFileChooser();
    var lastUsedDirectory:File;
    var file:File;
    var fileName:String = "";
    var filePath:String = "";
    var fileLastModified:Long;
    var fileSize:Long = -1;
    var syncState:String;
    var reloadPossible = false;
    var autoReload = true;
    var fileContent = new FileContent();

    var assignment:String;

    def nrOfColumns = 40;
    def valueOffset = 90.0;
    def labelOffset = 4.0;
   def spacing = 5.0;

    var mainContent:Group = Group{
         content: [
            VBox{
               layoutX:spacing
               spacing:spacing
               content: [
                  Text {
                     font : Font {
                        size: 12
                     }
                     x: 10, y: 10
                     content: "Here you can import and export a document, which is the content of this ELO."
                  }
                  Group{
                     content:[
                        Label {
                           layoutY:labelOffset
                           text: "Assignment"
                        }
                        TextBox {
                           layoutX:valueOffset
                           text: bind assignment with inverse
                           columns: nrOfColumns
                           selectOnFocus: true
                           editable:true
                        }
                     ]
                  }
                  Group{
                     content:[
                        Label {
                           layoutY:labelOffset
                           text: "File name"
                        }
                        TextBox {
                           layoutX:valueOffset
                           text: bind fileName
                           columns: nrOfColumns
                           selectOnFocus: true
                           editable:false
                        }
                     ]
                  }
                  Group{
                     content:[
                        Label {
                           layoutY:labelOffset
                           text: "File path"
                        }
                        TextBox {
                           layoutX:valueOffset
                           text: bind filePath
                           columns: nrOfColumns
                           selectOnFocus: true
                           editable:false
                        }
                     ]
                  }
                  Group{
                     content:[
                        Label {
                           layoutY:labelOffset
                           text: "Last modified"
                        }
                        TextBox {
                           layoutX:valueOffset
                           text: bind if (file!=null) "{%tD fileLastModified}, {%tT fileLastModified}" else ""
                           columns: nrOfColumns
                           selectOnFocus: true
                           editable:false
                        }
                     ]
                  }
                  Group{
                     content:[
                        Label {
                           layoutY:labelOffset
                           text: "Sync state"
                        }
                        TextBox {
                           layoutX:valueOffset
                           text: bind syncState
                           columns: nrOfColumns
                           selectOnFocus: true
                           editable:false
                        }
                     ]
                  }
                  Group{
                     content:[
                        Label {
                           layoutY:labelOffset
                           text: "Sync actions"
                        }
                        HBox{
                           layoutX:valueOffset
                           spacing:2*spacing
                           content:[
                              CheckBox {
                                 translateY:labelOffset-1
                                 text: "Auto reload"
                                 allowTriState: false
                                 selected: bind autoReload with inverse
                              }
                              Button {
                                 disable:bind file==null or not reloadPossible
                                 text: "Reload"
                                 action: function():Void{
                                    reloadFile();
                                 }

                              }
                              Button {
                                 text: "Chack state"
                                 action: updateSyncState
                              }
                           ]
                        }
                     ]
                  }
                ]
             }
          ];
      };

   var technicalFormatKey: IMetadataKey;
   var titleKey: IMetadataKey;
   var externalDocAnnotationKey:IMetadataKey;
   
   var autoSyncStateUpdateer = Timeline {
         repeatCount: Timeline.INDEFINITE
         keyFrames : [
            KeyFrame {
               time : 1s
               canSkip : true
               action:updateSyncState
            }
         ]
      }

   init{
      if (sizeof extensions > 0){
         // TODO, find out out why netbeans does not use java 1.6
         //fileChooser.setFileFilter(new FileNameExtensionFilter("description",extensions));
      }

      autoSyncStateUpdateer.play();
   }


   function updateSyncState():Void{
      reloadPossible = false;
      if (file==null){
         syncState = "no file uploaded"
      }
      else if (not file.exists()){
         syncState = "file not found"
      }
      else if (file.lastModified()<fileLastModified){
         syncState = "file older";
         reloadPossible = true;
      }
      else if (file.lastModified()>fileLastModified){
         syncState = "file newer";
         reloadPossible = true;
      }
      else if (file.length()!=fileSize){
         syncState = "file size changed";
         reloadPossible = true;
      }
      else{
         syncState = "equal";
      }

   }

   public override function initialize(windowContent: Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
      externalDocAnnotationKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.EXTERNAL_DOC.getId());
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }


   public override function create(): Node {
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateX:spacing;
               translateY:spacing;
               spacing:spacing;
               content:[
                  HBox{
                     //translateX:spacing;
                     spacing:spacing;
                     content:[
                        Button {
                           text: "Save"
                           //disable:bind file==null
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: "Save as"
                           //disable:bind file==null
                           action: function() {
										doSaveAsElo();
                           }
                        }
                        Button {
                           text: "Import file"
                           action: function() {
										importFile();
                           }
                        }
                        Button {
                           text: "Export file"
                           disable:bind file==null
                           action: function() {
										exportFile();
                           }
                        }
                     ]
                  }
                  mainContent
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
         var metadata = newElo.getMetadata();
         var externalDocAnnotation:ExternalDocAnnotation;
         if (externalDocAnnotationKey!=null){
            externalDocAnnotation = newElo.getMetadata().getMetadataValueContainer(externalDocAnnotationKey).getValue() as ExternalDocAnnotation;
         }
         if (externalDocAnnotation!=null){
            assignment = externalDocAnnotation.getAssignment();
            fileName = externalDocAnnotation.getFileName();
            filePath = externalDocAnnotation.getFilePath();
            fileLastModified = externalDocAnnotation.getFileLastModified();
            fileSize = externalDocAnnotation.getFileSize();
            if (fileSize>=0){
               fileContent.setBytes(elo.getContent().getBytes());
               file =new File(filePath,fileName);
            }
            else{
               file=null;
            }
            logger.info("externalDocAnnotation retrieved: {externalDocAnnotation}");

         }
         else{
            logger.warn("there is no externalDocAnnotation found");
         }

         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
//      elo.getContent().setXmlString(textToEloContentXml(textEditor.getText()));
      eloSaver.eloUpdate(getElo(),this);
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(technicalType);
      }
      var externalDocAnnotation = new ExternalDocAnnotation();
      externalDocAnnotation.setAssignment(assignment);
      externalDocAnnotation.setFileName(fileName);
      externalDocAnnotation.setFilePath(filePath);
      externalDocAnnotation.setFileLastModified(fileLastModified);
      externalDocAnnotation.setFileSize(fileSize);
      logger.info("externalDocAnnotation to save: {externalDocAnnotation}");
      if (externalDocAnnotationKey!=null){
         elo.getMetadata().getMetadataValueContainer(externalDocAnnotationKey).setValue(externalDocAnnotation);
      }
      if (file!=null){
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(fileName);
         if (reloadPossible and autoReload){
            reloadFile();
         }
         elo.getContent().setBytes(fileContent.getBytes());
      }

      return elo;
   }

   function importFile():Void{
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getParentComponent())) {
         //getting the file from the fileChooser
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         loadFile(fileChooser.getSelectedFile(),true);
      }
   }

   function reloadFile():Void{
      loadFile(file,true);
   }


   function loadFile(newFile: File, readBytes: Boolean){
      logger.info("new file {newFile.getAbsolutePath()}");
      file = newFile;
      fileName = file.getName();
      filePath = file.getParentFile().getAbsolutePath();
      fileLastModified = file.lastModified();
      fileSize = file.length();
      if (readBytes){
         fileContent.setBytes(ImportUtils.getBytesFromFile(file));
      }
      updateSyncState();
   }


   function exportFile():Void{
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(getParentComponent())) {
         //getting the file from the fileChooser
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         var newFile = fileChooser.getSelectedFile();
         ImportUtils.saveBytesToFile(fileContent.getBytes(), newFile);
         loadFile(newFile,false);
      }
   }

   function getParentComponent():Component{
      return null;
   }


    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
    }

    override public function getPrefHeight (arg0 : Number) : Number {
        return 100;
    }

    override public function getPrefWidth (arg0 : Number) : Number {
        return 200;
    }
}


function run(){
   Stage {
	title : "External doc tester"
	scene: Scene {
		width: 200
		height: 200
		content: [
         ExternalDoc{
            
         }

      ]
	}
}

}
