/*
 * textEditorNode.fx
 *
 * Created on 29-sep-2009, 19:39:36
 */

package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.ext.swing.SwingComponent;
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

/**
 * @author sikken
 */

// place your code here
public class TextEditorNode extends CustomNode, Resizable {

   public-init var textEditor:TextEditor;
   public-init var eloTextEditorActionWrapper:EloTextEditorActionWrapper;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedTextEditor:SwingComponent;

   public function loadElo(uri:URI){
      eloTextEditorActionWrapper.loadElo(uri);
		setScyWindowTitle();
   }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Text: {eloTextEditorActionWrapper.getDocName()}";
		var eloUri = eloTextEditorActionWrapper.getEloUri();
		if (eloUri != null)
			scyWindow.id = eloUri.toString()
		else
			scyWindow.id = "";
	};

   public override function create(): Node {
      wrappedTextEditor = SwingComponent.wrap(textEditor);
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
      println("wrappedTextEditor.boundsInParent: {wrappedTextEditor.boundsInParent}");
      var size = new Dimension(width,height-wrappedTextEditor.boundsInParent.minY);
      // setPreferredSize is needed
      textEditor.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      textEditor.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return textEditor.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return textEditor.getPreferredSize().getWidth();
   }

}
