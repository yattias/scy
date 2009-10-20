/*
 * SCYMapperNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.client.tools.fxscymapper.registration;

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

import eu.scy.scymapper.impl.SCYMapperPanel;


/**
 * @author sikkenj
 */

 // place your code here
public class SCYMapperNode extends CustomNode, Resizable {

   public-init var scyMapperPanel:SCYMapperPanel;
   public-init var eloSCYMapperActionWrapper:EloScyMapperActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()
	};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedSCYMapperPanel:SwingComponent;

   def spacing = 5.0;

	public function loadElo(uri:URI){
      eloSCYMapperActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = eloSCYMapperActionWrapper.getDocName();
		var eloUri = eloSCYMapperActionWrapper.getEloUri();
      	scyWindow.eloUri = eloUri;
	};

   public override function create(): Node {
      wrappedSCYMapperPanel = SwingComponent.wrap(scyMapperPanel);
      return Group {
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
//								eloSCYMapperActionWrapper.newConceptMapAction();
								setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Load"
                           action: function() {
                              	//eloSCYMapperActionWrapper.loadConceptMapAction();
								setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save"
                           action: function() {
//					  			eloSCYMapperActionWrapper.saveConceptMapAction();
								setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save as"
                           action: function() {
  //                            	eloSCYMapperActionWrapper.saveAsConceptMapAction();
								setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  wrappedSCYMapperPanel
               ]
            }
         ]
      };
   }

   function resizeContent(){
      var size = new Dimension(width,height-wrappedSCYMapperPanel.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      scyMapperPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      scyMapperPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return scyMapperPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return scyMapperPanel.getPreferredSize().getWidth();
   }

}
