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
import java.io.File;
import javafx.stage.Stage;
import javafx.scene.Scene;

import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import org.jdom.Element;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import javafx.scene.control.Label;
import javafx.scene.layout.Resizable;
import javafx.scene.control.TextBox;
import javafx.scene.control.CheckBox;


/**
 * @author sikken
 */

public class ExternalDoc extends CustomNode,Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());

   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var technicalType:String;
   public var extensions:String[];

   var elo:IELO;


    var eloImporterModel:EloImporterModel = new EloImporterModel();
    def fileChooser = new JFileChooser();
    var file:File;
    var fileName:String = "?";
    var filePath:String = "?";
    var fileLastModified:String = "?";
    var syncState:String;
    var reloadPossible = false;
    var fileContent = new FileContent();

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
                           text: bind fileLastModified
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
                                 selected: false
                              }
                              Button {
                                 disable:bind file==null or not reloadPossible
                                 text: "Reload"
                                 action: function() {

                                 }
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

   public override function initialize(windowContent: Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
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
                           disable:bind file==null
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: "Save as"
                           disable:bind file==null
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
//         IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
//         // TODO fixe the locale problem!!!
//         Object titleObject = metadataValueContainer.getValue();
//         Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
//         Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);
//
//         setDocName(titleObject3.toString());
//         var text = eloContentXmlToText(newElo.getContent().getXmlString());
//         textEditor.setText(text);
         logger.info("elo text loaded");
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
      elo.getContent().setBytes(fileContent.getBytes());
      return elo;
   }

   function importFile():Void{
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
         //getting the file from the fileChooser
         file = fileChooser.getSelectedFile();
         fileName = file.getName();
         filePath = file.getParentFile().getAbsolutePath();
         fileLastModified = "{%tc file.lastModified()}";
         fileContent.setBytes(ImportUtils.getBytesFromFile(file));
      }
   }

   function exportFile():Void{
      
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
