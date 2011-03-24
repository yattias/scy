/*
 * ExternalDoc.fx
 *
 * Created on 20-jan-2010, 12:05:25
 */
package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import javax.swing.JFileChooser;
//import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataKey;
import javafx.scene.control.Label;
import javafx.scene.layout.Resizable;
import javafx.scene.control.TextBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import java.awt.Component;
import java.net.URI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.ExternalDocAnnotation;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;

/**
 * @author sikken
 */
public class ExternalDoc extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   public var toolBrokerAPI: ToolBrokerAPI on replace {
         eloFactory = toolBrokerAPI.getELOFactory();
         metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
         repository = toolBrokerAPI.getRepository();
      };
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var technicalType: String;
   public-init var extensions: String[];
   public-init var fileFilterDescription: String;
   var elo: IELO;
   var eloImporterModel: EloImporterModel = new EloImporterModel();
   def fileChooser = new JFileChooser();
   var lastUsedDirectory: File;
   var file: File;
   var fileName: String = "";
   var filePath: String = "";
   var fileLastModified: Long;
   var fileSize: Long = -1;
   var syncState: String;
   var reloadPossible = false;
   var autoReload = true;
   var fileContent = new FileContent();
   var assignment: String;
   def nrOfColumns = 40;
   def valueOffset = 130.0;
   def labelOffset = 4.0;
   def spacing = 5.0;
   var fileNameTextBox: TextBox;
   def font = Font {
         size: 12
      };
   var mainContent: Group = Group {
         content: [
            VBox {
               layoutX: spacing
               spacing: spacing
               content: [
                  //                  Text {
                  //                     font : Font {
                  //                        size: 12
                  //                     }
                  //                     x: 10, y: 10
                  //                     content: "Here you can import and export a document, which is the content of this ELO."
                  //                  }
                  Group {
                     content: [
                        Label {
                           layoutY: labelOffset
                           text: ##"Assignment"
                        }
                        Group {
                           layoutX: valueOffset
                           content: [
                              TextBox {
                                 text: bind assignment with inverse
                                 columns: nrOfColumns
                                 multiline: true
                                 selectOnFocus: true
                                 editable: bind elo == null
                              }
                           ]
                        }
                     ]
                  }
                  Group {
                     content: [
                        Label {
                           layoutY: labelOffset
                           text: ##"File name"
                        }
                        fileNameTextBox = TextBox {
                              layoutX: valueOffset
                              text: bind fileName
                              columns: nrOfColumns
                              selectOnFocus: true
                              editable: false
                           }
                     ]
                  }
                  Group {
                     content: [
                        Label {
                           layoutY: labelOffset
                           text: ##"File path"
                        }
                        TextBox {
                           layoutX: valueOffset
                           text: bind filePath
                           columns: nrOfColumns
                           selectOnFocus: true
                           editable: false
                        }
                     ]
                  }
                  Group {
                     content: [
                        Label {
                           layoutY: labelOffset
                           text: ##"Last modified"
                        }
                        TextBox {
                           layoutX: valueOffset
                           text: bind if (file != null) "{





             %tD fileLastModified}, {%tT fileLastModified}" else ""
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
                           text: ##"Sync state"
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
//                  Group{
//                     content:[
//                        Label {
//                           layoutY:labelOffset
//                           text: "Sync actions"
//                        }
//                        HBox{
//                           layoutX:valueOffset
//                           spacing:2*spacing
//                           content:[
//                              CheckBox {
//                                 translateY:labelOffset-1
//                                 text: "Auto reload"
//                                 allowTriState: false
//                                 selected: bind autoReload with inverse
//                              }
//                              Button {
//                                 disable:bind file==null or not reloadPossible
//                                 text: "Reload"
//                                 action: function():Void{
//                                    reloadFile();
//                                 }
//
//                              }
//                              Button {
//                                 text: "Chack state"
//                                 action: updateSyncState
//                              }
//                           ]
//                        }
//                     ]
//                  }
                ]
             }
          ];
      };

   var technicalFormatKey: IMetadataKey;
   var titleKey: IMetadataKey;
   var externalDocAnnotationKey:IMetadataKey;

   var minimumSizeBottomRightNode:Node;
   var contentGroup:Group;
   
   var autoSyncStateUpdateer = Timeline {
         repeatCount: Timeline.INDEFINITE
         keyFrames : [
            KeyFrame {
               time : 1s
               canSkip: true
               action: updateSyncState
            }
         ]
      }
   def saveTitleBarButton = TitleBarButton {
              actionId: "save"
              iconType: "save"
              action: doSaveElo
              tooltip: "save ELO"
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: "saveAs"
              iconType: "save_as"
              action: doSaveAsElo
              tooltip: "save copy of ELO"
           }
   def importFileTitleBarButton = TitleBarButton {
              actionId: "import"
              iconType: "import"
              action: importFile
              tooltip: ##"Upload file"
           }
   def exportFileTitleBarButton = TitleBarButton {
              actionId: "export"
              iconType: "export"
              action: exportFile
              tooltip:  ##"Download file"
              enabled: bind file != null
           }

   init {
      if (sizeof extensions > 0) {
         // TODO, find out out why netbeans does not use java 1.6
         fileChooser.setFileFilter(new ExampleFileFilter(extensions, fileFilterDescription));
      }

      autoSyncStateUpdateer.play();
   }

   function updateSyncState(): Void {
      reloadPossible = false;
      if (file == null) {
         syncState = ##"no file uploaded"
      } else if (not file.exists()) {
         syncState = ##"file not found"
      } else if (file.lastModified() < fileLastModified) {
         syncState = ##"file older";
         reloadPossible = true;
      } else if (file.lastModified() > fileLastModified) {
         syncState = ##"file newer";
         reloadPossible = true;
      } else if (file.length() != fileSize) {
         syncState = ##"file size changed";
         reloadPossible = true;
      } else {
         syncState = ##"equal";
      }

   }

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
      externalDocAnnotationKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.EXTERNAL_DOC.getId());
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton,
                    importFileTitleBarButton,
                    exportFileTitleBarButton
                 ]
      }
   }

   public override function loadElo(uri: URI) {
      doLoadElo(uri);
   }

   public override function newElo() {
   }

   public override function onQuit(): Void {
      if (elo != null) {
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   public override function create(): Node {
      contentGroup = Group {
            blocksMouse: true;
            content: [
               VBox {
                  translateX: spacing;
                  translateY: spacing;
                  spacing: spacing;
                  content: [
                     HBox {
                        //translateX:spacing;
                        spacing: spacing;
                        content: [
                           Button {
                              text: ##"Save"
                              //disable:bind file==null
                              action: function() {
                                 doSaveElo();
                              }
                           }
                           Button {
                              text: ##"Save as"
                              //disable:bind file==null
                              action: function() {
                                 doSaveAsElo();
                              }
                           }
                           Button {
                              text: ##"Upload file"
                              action: function() {
                                 importFile();
                              }
                           }
                           minimumSizeBottomRightNode = Button {
                                 text: ##"Download file"
                                 disable: bind file == null
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
      contentGroup.layout();
      contentGroup;
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         var metadata = newElo.getMetadata();
         var externalDocAnnotation: ExternalDocAnnotation;
         if (externalDocAnnotationKey != null) {
            externalDocAnnotation = newElo.getMetadata().getMetadataValueContainer(externalDocAnnotationKey).getValue() as ExternalDocAnnotation;
         }
         if (externalDocAnnotation != null) {
            assignment = externalDocAnnotation.getAssignment();
            fileName = externalDocAnnotation.getFileName();
            filePath = externalDocAnnotation.getFilePath();
            fileLastModified = externalDocAnnotation.getFileLastModified();
            fileSize = externalDocAnnotation.getFileSize();
            if (fileSize >= 0) {
               fileContent.setBytes(newElo.getContent().getBytes());
               file = new File(filePath, fileName);
            } else {
               file = null;
            }
            logger.info("externalDocAnnotation retrieved: {externalDocAnnotation}, with {if (fileContent.getBytes() != null) fileContent.getBytes().length else "null"} bytes");
         } else {
            logger.warn("there is no externalDocAnnotation found");
         }

         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo() {
      //      elo.getContent().setXmlString(textToEloContentXml(textEditor.getText()));
      eloSaver.eloUpdate(getElo(), this);
   }

   function doSaveAsElo() {
      eloSaver.eloSaveAs(getElo(), this);
   }

   function getElo(): IELO {
      if (elo == null) {
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
      if (externalDocAnnotationKey != null) {
         elo.getMetadata().getMetadataValueContainer(externalDocAnnotationKey).setValue(externalDocAnnotation);
      }
      if (file != null) {
         var title = elo.getMetadata().getMetadataValueContainer(titleKey).getValue();
         if (title == null) {
            var proposedTitle = fileName;
            var pointPos = proposedTitle.lastIndexOf('.');
            if (pointPos >= 0) {
               proposedTitle = proposedTitle.substring(0, pointPos);
            }
            elo.getMetadata().getMetadataValueContainer(titleKey).setValue(proposedTitle);
         }
         if (reloadPossible and autoReload) {
            reloadFile();
         }
         elo.getContent().setBytes(fileContent.getBytes());
      }

      return elo;
   }

   function importFile(): Void {
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getParentComponent())) {
         //getting the file from the fileChooser
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         loadFile(fileChooser.getSelectedFile(), true);
      }
   }

   function reloadFile(): Void {
      loadFile(file, true);
   }

   function loadFile(newFile: File, readBytes: Boolean) {
      logger.info("new file {newFile.getAbsolutePath()}");
      file = newFile;
      fileName = file.getName();
      filePath = file.getParentFile().getAbsolutePath();
      fileLastModified = file.lastModified();
      fileSize = file.length();
      if (readBytes) {
         fileContent.setBytes(ImportUtils.getBytesFromFile(file));
      }
      updateSyncState();
   }

   function exportFile(): Void {
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      var proposedFile = new File(filePath, fileName);
      fileChooser.setSelectedFile(proposedFile);
      if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(getParentComponent())) {
         //getting the file from the fileChooser
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         var newFile = fileChooser.getSelectedFile();
         ImportUtils.saveBytesToFile(fileContent.getBytes(), newFile);
         loadFile(newFile, false);
      }
   }

   function getParentComponent(): Component {
      return null;
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
      this.elo = elo;
   }

   override public function getMinHeight(): Number {
      minimumSizeBottomRightNode.boundsInParent.maxY;
   }

   override public function getMinWidth(): Number {
      minimumSizeBottomRightNode.boundsInParent.maxX;
   }

   override public function getPrefHeight(arg0: Number): Number {
      contentGroup.boundsInLocal.maxY;
   }

   override public function getPrefWidth(arg0: Number): Number {
      contentGroup.boundsInLocal.maxX;
   }

}

function run() {
   var externalDoc = ExternalDoc {
      }
   //externalDoc.layout();
   var prefSizeDsiplay = Rectangle {
         x: 0, y: 0
         width: 0, height: 0
         fill: Color.YELLOW
      }
   Timeline {
      repeatCount: 1
      keyFrames: [
         KeyFrame {
            time: 1s
            canSkip: true
            action: function(): Void {
               prefSizeDsiplay.width = externalDoc.getPrefWidth(200);
               prefSizeDsiplay.height = externalDoc.getPrefHeight(200)
            }
         }
      ]
   }.play();

   Stage {
      title: "External doc tester"
      scene: Scene {
         width: externalDoc.getPrefWidth(200)
         height: externalDoc.getPrefHeight(200)
         content: [
            prefSizeDsiplay,
            externalDoc
         ]
      }
   }

}
