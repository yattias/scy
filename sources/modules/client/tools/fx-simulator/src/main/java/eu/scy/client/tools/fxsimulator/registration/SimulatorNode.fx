package eu.scy.client.tools.fxsimulator.registration;

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

import eu.scy.client.tools.scysimulator.EloSimQuestWrapper;


/**
 * @author sikkenj
 */

 // place your code here
public class SimulatorNode extends CustomNode, Resizable {

   public-init var simquestPanel:JPanel;
   public-init var eloSimQuestWrapper:EloSimQuestWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedWhiteboardPanel:SwingComponent;

   public function loadElo(uri:URI){
      eloSimQuestWrapper.loadElo(uri);
      setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Simulation: {eloSimQuestWrapper.getDocName()}";
		var eloUri = eloSimQuestWrapper.getEloUri();
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
                               eloSimQuestWrapper.newAction();
                               setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Load"
                           action: function() {
                              eloSimQuestWrapper.loadSimConfigAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save DataSet"
                           action: function() {
                              eloSimQuestWrapper.saveDataSetAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "SaveAs DataSet"
                           action: function() {
                              eloSimQuestWrapper.saveAsDataSetAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save Config"
                           action: function() {
                              eloSimQuestWrapper.saveSimConfigAction();
                              setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "SaveAs Config"
                           action: function() {
                              eloSimQuestWrapper.saveAsSimConfigAction();
                              setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  SwingComponent.wrap(simquestPanel)
               ]
            }
         ]
      };
   }

  

   function resizeContent(){
      var size = new Dimension(width,height);
      // setPreferredSize is needed
      simquestPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      simquestPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return simquestPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return simquestPanel.getPreferredSize().getWidth();
   }

}
