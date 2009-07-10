/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.client.tools.fxdrawingtool.registration;

import colab.vt.whiteboard.component.WhiteboardPanel;
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


/**
 * @author sikkenj
 */

 // place your code here
public class DrawingNode extends CustomNode, Resizable {

   public-init var whiteboardPanel:WhiteboardPanel;
   public-init var eloDrawingActionWrapper:EloDrawingActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedWhiteboardPanel:SwingComponent;

   public function loadElo(uri:URI){
      eloDrawingActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Drawing: {eloDrawingActionWrapper.getDocName()}";
		var eloUri = eloDrawingActionWrapper.getEloUri();
		if (eloUri != null)
			scyWindow.id = eloUri.toString()
		else
			scyWindow.id = "";
	};

   public override function create(): Node {
      wrappedWhiteboardPanel = SwingComponent.wrap(whiteboardPanel);
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
                              eloDrawingActionWrapper.newDrawingAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Load"
                           action: function() {
                              eloDrawingActionWrapper.loadDrawingAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save"
                           action: function() {
                              eloDrawingActionWrapper.saveDrawingAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save as"
                           action: function() {
                              eloDrawingActionWrapper.saveAsDrawingAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  wrappedWhiteboardPanel
               ]
            }
         ]
      };
   }

   function resizeContent(){
      var size = new Dimension(width,height);
      // setPreferredSize is needed
      whiteboardPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      whiteboardPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return whiteboardPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return whiteboardPanel.getPreferredSize().getWidth();
   }

}
