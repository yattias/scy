package eu.scy.client.tools.fxmathtool.registration;

import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.controller.MathToolController;
import javax.swing.JComponent;
import eu.scy.tools.math.controller.SCYMathToolController;

public class MathToolNode extends CustomNode, Resizable {
   protected def spacing = 5.0;
   protected var mathTool:MathTool;
   protected var wrappedMathTool:Node;

   protected function resizeContent(): Void{
      Container.resizeNode(wrappedMathTool,width,height-wrappedMathTool.boundsInParent.minY-spacing);
   }

   public override function create(): Node {
      mathTool = new MathTool( new SCYMathToolController());
      wrappedMathTool = ScySwingWrapper.wrap(mathTool.createMathTool(0, 0),true);
      resizeContent();
      FX.deferAction(resizeContent);
      return Group {
         blocksMouse:true;
         content: [wrappedMathTool]
      };
   }

   public override function getPrefHeight(width: Number) : Number{
      return Container.getNodePrefHeight(wrappedMathTool, height)+wrappedMathTool.boundsInParent.minY+spacing;
   }

   public override function getPrefWidth(height: Number) : Number{
      return Container.getNodePrefWidth(wrappedMathTool, width);
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
