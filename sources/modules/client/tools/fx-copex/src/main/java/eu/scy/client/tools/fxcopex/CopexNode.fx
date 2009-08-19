/*
 * CopexNode.fx
 *
 * Created on 18 août 2009, 15:18:39
 */

package eu.scy.client.tools.fxcopex;

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
 * @author Marjolaine
 */

public class CopexNode extends CustomNode, Resizable {
    public-init var copexPanel:CopexPanel;
    public-init var eloCopexActionWrapper:EloCopexActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedCopexPanel:SwingComponent;

   public function loadElo(uri:URI){
        eloCopexActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Experimental procedure: {eloCopexActionWrapper.getDocName()}";
		var eloUri = eloCopexActionWrapper.getEloUri();
		if (eloUri != null)
			scyWindow.id = eloUri.toString()
		else
			scyWindow.id = "";
	};

   public override function create(): Node {
      wrappedCopexPanel = SwingComponent.wrap(copexPanel);
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
                              eloCopexActionWrapper.newCopexAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Open"
                           action: function() {
                              eloCopexActionWrapper.loadCopexAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save"
                           action: function() {
                              eloCopexActionWrapper.saveCopexAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save copy"
                           action: function() {
                              eloCopexActionWrapper.saveAsCopexAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  wrappedCopexPanel
               ]
            }
         ]
      };
   }

   function resizeContent(){
      var size = new Dimension(width,height);
      // setPreferredSize is needed
      copexPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      copexPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return copexPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return copexPanel.getPreferredSize().getWidth();
   }
}
