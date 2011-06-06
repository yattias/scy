package eu.scy.client.tools.fxrichtexteditor.registration;

import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import eu.scy.client.common.richtexteditor.RichTextEditor;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;

public class RichTextEditorNode extends CustomNode, Resizable {
   protected def spacing = 5.0;
   protected var richTextEditor:RichTextEditor;
   protected var wrappedRichTextEditor:Node;

   protected function resizeContent(): Void{
      Container.resizeNode(wrappedRichTextEditor,width,height-wrappedRichTextEditor.boundsInParent.minY-spacing);
   }

   public override function create(): Node {
      richTextEditor = new RichTextEditor(false, true);
      wrappedRichTextEditor = ScySwingWrapper.wrap(richTextEditor,true);
      resizeContent();
      FX.deferAction(resizeContent);
      return Group {
         blocksMouse:true;
         content: [wrappedRichTextEditor]
      };
   }

   public override function getPrefHeight(width: Number) : Number{
      return Container.getNodePrefHeight(wrappedRichTextEditor, height)+wrappedRichTextEditor.boundsInParent.minY+spacing;
   }

   public override function getPrefWidth(height: Number) : Number{
      return Container.getNodePrefWidth(wrappedRichTextEditor, width);
   }

   public override function getMinHeight() : Number {
       return 200;
   }

   public override function getMinWidth() : Number {
       return 400;
   }

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

}
