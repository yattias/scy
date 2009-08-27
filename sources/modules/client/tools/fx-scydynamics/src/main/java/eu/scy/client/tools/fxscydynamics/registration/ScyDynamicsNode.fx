package eu.scy.client.tools.fxscydynamics.registration;

import java.net.URI;
import javafx.ext.swing.SwingComponent;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import java.awt.Dimension;
import javax.swing.JPanel;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;

public class ScyDynamicsNode extends CustomNode, Resizable {

   public-init var modelEditor:ModelEditor;
   public-init var editorPanel:JPanel;
   public-init var eloModelWrapper:EloModelWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   
   public function loadElo(uri:URI){
      eloModelWrapper.loadElo(uri);
      setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Model: {eloModelWrapper.getDocName()}";
		var eloUri = eloModelWrapper.getEloUri();
		if (eloUri != null)
			scyWindow.id = eloUri.toString()
		else
			scyWindow.id = "";
	};


public override function create(): Node {
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:5;
               spacing:5;
               content:[
                  HBox{
                     translateX:5;
                     spacing:5;
                     content:[
                        Button {
                           text: "New"
                           action: function() {
                               eloModelWrapper.newAction();
                               setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Load"
                           action: function() {
                              eloModelWrapper.loadModelAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save DataSet"
                           action: function() {
                              eloModelWrapper.saveDatasetAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "SaveAs DataSet"
                           action: function() {
                              eloModelWrapper.saveAsDatasetAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save Model"
                           action: function() {
                              eloModelWrapper.saveModelAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "SaveAs Model"
                           action: function() {
                              eloModelWrapper.saveAsModelAction();
                              setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  SwingComponent.wrap(modelEditor)
               ]
            }
         ]
      };;;
   }

  

   function resizeContent(){
      var size = new Dimension(width,height);
      // setPreferredSize is needed
      editorPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      editorPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return editorPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return editorPanel.getPreferredSize().getWidth();
   }

}
