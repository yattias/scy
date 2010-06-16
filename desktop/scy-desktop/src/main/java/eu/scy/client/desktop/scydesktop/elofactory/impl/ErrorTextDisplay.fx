/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.ext.swing.SwingComponent;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditor;
import javafx.scene.layout.Container;
import java.awt.Dimension;
import javafx.scene.layout.Resizable;
import javafx.scene.Group;

/**
 * @author SikkenJ
 */

public class ErrorTextDisplay  extends CustomNode,Resizable {
   var textEditor = new TextEditor();
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   public var text = "" on replace{
      textEditor.setText(text);
   }

	public override function create(): Node {
      textEditor.setEditable(false);
      textEditor.resetScrollbars();
       resizeContent();
      FX.deferAction(resizeContent);
      Group{
         autoSizeChildren:false
         content: SwingComponent.wrap(textEditor);
      }
	}

   function resizeContent():Void{
      var size = new Dimension(width,height);
      Container.resizeNode(this,size.width,size.height);

      // setPreferredSize is needed
      textEditor.setPreferredSize(size);
//      println("pref sized set to {size}");
      // setSize is not visual needed
      // but set it, so the component can react to it
      textEditor.setSize(size);
//      println("resized ScyTextEditorNode to ({width},{height}), text: ({size.width},{size.height})");
   }

   public override function getPrefHeight(width: Number) : Number{
//      println("textEditor.getPreferredSize(): {textEditor.getPreferredSize()}, textEditor.getSize(): {textEditor.getSize()}");
      return textEditor.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return textEditor.getPreferredSize().getWidth();
   }
}
