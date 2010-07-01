/*
 * textEditorNode.fx
 *
 * Created on 29-sep-2009, 19:39:36
 */

package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import java.awt.Dimension;
import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.scene.layout.Container;

/**
 * @author sikken
 */

// place your code here
public class TextEditorNode extends CustomNode, Resizable {
   def logger = Logger.getLogger(this.getClass());

   public-init var textEditor:TextEditor;
   public-init var eloTextEditorActionWrapper:EloTextEditorActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedTextEditor:Node;

   public function loadElo(uri:URI){
      eloTextEditorActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null){
         logger.info("could not set window title, because scyWindow == null");
         return;
      }
      logger.info("set title to {eloTextEditorActionWrapper.getDocName()} and eloUri to {eloTextEditorActionWrapper.getEloUri()}");
		scyWindow.title = eloTextEditorActionWrapper.getDocName();
		var eloUri = eloTextEditorActionWrapper.getEloUri();
		scyWindow.eloUri = eloUri
	};

   def spacing = 5.0;

   public override function create(): Node {
      wrappedTextEditor = ScySwingWrapper.wrap(textEditor);
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
                              eloTextEditorActionWrapper.newTextAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Load"
                           action: function() {
                              eloTextEditorActionWrapper.loadTextAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save"
                           action: function() {
                              eloTextEditorActionWrapper.saveTextAction();
										setScyWindowTitle();
                           }
                        }
                        Button {
                           text: "Save as"
                           action: function() {
                              eloTextEditorActionWrapper.saveAsTextAction();
										setScyWindowTitle();
                           }
                        }
                     ]
                  }
                  wrappedTextEditor
               ]
            }
         ]
      };
   }

   function resizeContent(){
      Container.resizeNode(wrappedTextEditor,width,height-wrappedTextEditor.boundsInParent.minY-spacing);
   }

   public override function getPrefHeight(width: Number) : Number{
      return Container.getNodePrefHeight(wrappedTextEditor, width)+wrappedTextEditor.boundsInParent.minY+spacing;
   }

   public override function getPrefWidth(width: Number) : Number{
      return Container.getNodePrefWidth(wrappedTextEditor, width);
  }

}
