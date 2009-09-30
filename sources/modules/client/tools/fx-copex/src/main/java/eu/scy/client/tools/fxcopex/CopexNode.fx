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
    public-init var copexPanel:ScyCopexPanel;
    public-init var eloCopexActionWrapper:EloCopexActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedCopexPanel:SwingComponent;
   def spacing = 5.0;

   public function loadElo(uri:URI){
        eloCopexActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = eloCopexActionWrapper.getDocName();
		var eloUri = eloCopexActionWrapper.getEloUri();
      scyWindow.eloUri = eloUri;
	};

   public override function create(): Node {
      wrappedCopexPanel = SwingComponent.wrap(copexPanel);
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
      var size = new Dimension(width,height-wrappedCopexPanel.boundsInParent.minY-spacing);
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
