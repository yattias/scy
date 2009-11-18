/*
 * ELOImporterNode.fx
 *
 * Created on 16.11.2009, 12:32:39
 */

package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.JFileChooser;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;




import javafx.scene.control.TextBox;

import javafx.scene.text.Text;

import javafx.scene.layout.Tile;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.EloImporterModel;



/**
 * @author Sven
 */

public class ELOImporterNode extends CustomNode{

    init{
        //Setting a file filter for the JFileChooser
//        FileFilter filter = new FileFilter();

//        fileChooser.setFileFilter(filter);
    }


    public-init var eloImporterActionWrapper:EloImporterActionWrapper;

//    def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.tools.content.eloImporter");

    public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()
    };


    var fileChooser = new JFileChooser();
    var file:File;
    public var filename;
    public var fileEncoded:String;

    public var eloImporterModel:EloImporterModel = new EloImporterModel();

    var uploadButton:Button = Button{
        text: "Upload file..."
        action: function(){
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
                    //getting the file from the fileChooser
                    file = fileChooser.getSelectedFile();
                    filename = file.getAbsolutePath();
                    }
            }
        }
    

    public var titleBox = TextBox{
        
    }


    public var descriptionBox = TextBox{

    }

    public var filenameTextBox = TextBox{
        editable:false;
        text:bind filename;
    }


    var form: Group = Group{
        content: [];
    }

    
    var mainContent:Group = Group{
            content: [
                   Tile{
                    hgap:5
                    vgap:5
                    columns:2
                    content: [
                        Text{
                           content:"Title"
                        },
                        titleBox,
                        Text{
                            content:"Filename"
                            },
                        HBox{
                            content: [filenameTextBox,uploadButton]
                        }

                        Text{
                            content:"Description"
                            },
                        descriptionBox
                        ]
                    }
                    ];
            };



    function setScyWindowTitle(){
	if (scyWindow == null){
//        logger.info("could not set window title, because scyWindow == null");
        return;
      }
      /*logger.info("set title to {eloTextEditorActionWrapper.getDocName()} and eloUri to {eloTextEditorActionWrapper.getEloUri()}");
		scyWindow.title = eloTextEditorActionWrapper.getDocName();
		var eloUri = eloTextEditorActionWrapper.getEloUri();
		scyWindow.eloUri = eloUri*/
	};

   def spacing = 5.0;

   public override function create(): Node {
       return Group{
            blocksMouse:true;
         content: [
            VBox{
               translateY:spacing;
               spacing:spacing;
               content:[
                  HBox{
                     translateX:spacing;
                     spacing:spacing;
                     content:[
                        Button {
                           text: "New"
                           action: function() {
                              eloImporterModel.updateModel(file, filename, descriptionBox.text, titleBox.text); 
                              println("{eloImporterModel.getFilename()}");
                              eloImporterActionWrapper.newELOAction();
										setScyWindowTitle();
                           }
                        }
                        /*Button {
                           text: "Load"
                           action: function() {
                              eloTextEditorActionWrapper.
										setScyWindowTitle();
                           }
                        }*/
                        Button {
                           text: "Save"
                           action: function() {
                              eloImporterModel.updateModel(file, filename, descriptionBox.text, titleBox.text);
                              eloImporterActionWrapper.saveImportedELOAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save as"
                           action: function() {
                              eloImporterModel.updateModel(file, filename, descriptionBox.text, titleBox.text);
                              eloImporterActionWrapper.saveImportedELOAsAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  mainContent
               ]
            }
         ]
       }

   }

   


    
}
function run(__ARGS__ : String[]){

    def eloImporter = ELOImporterNode{
       
    };

    Stage {
        title: "ELO Importer"
        width: 620
        height: 420
        visible: true;
        scene: Scene {
            content: [eloImporter];
        }
    }
}