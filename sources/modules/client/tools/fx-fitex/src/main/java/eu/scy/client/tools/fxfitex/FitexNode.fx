/*
 * FitexNode.fx
 *
 * Created on 17 août 2009, 14:21:29
 */

package eu.scy.client.tools.fxfitex;

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

public class FitexNode extends CustomNode, Resizable {
    public-init var fitexPanel:FitexPanel;
    public-init var eloFitexActionWrapper:EloFitexActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedFitexPanel:SwingComponent;
   def spacing = 5.0;

   public function loadElo(uri:URI){
      eloFitexActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = eloFitexActionWrapper.getDocName();
		var eloUri = eloFitexActionWrapper.getEloUri();
      scyWindow.eloUri = eloUri;
	};

   public override function create(): Node {
      wrappedFitexPanel = SwingComponent.wrap(fitexPanel);
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
                              eloFitexActionWrapper.newFitexAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Open"
                           action: function() {
                              var dsElo = eloFitexActionWrapper.loadFitexAction();
										setScyWindowTitle();
                                        if(dsElo != null){
                                            eloFitexActionWrapper.loadElo(dsElo);
                                        }
                           }
                        }
                        Button {
                           text: "Import csv file"
                           action: function() {
                              var dsElo = eloFitexActionWrapper.importFitexAction();
										setScyWindowTitle();
                                        if(dsElo != null){
                                            eloFitexActionWrapper.loadElo(dsElo);
                                        }

                           }
                        }
                        Button {
                           text: "Merge dataset"
                           action: function() {
                              eloFitexActionWrapper.mergeFitexAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save"
                           action: function() {
                              eloFitexActionWrapper.saveFitexAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save copy"
                           action: function() {
                              eloFitexActionWrapper.saveAsFitexAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  wrappedFitexPanel
               ]
            }
         ]
      };
   }

   function resizeContent(){
//      var size = new Dimension(width,height-25);
      var size = new Dimension(width,height-wrappedFitexPanel.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      fitexPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      fitexPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return fitexPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return fitexPanel.getPreferredSize().getWidth();
   }
}
