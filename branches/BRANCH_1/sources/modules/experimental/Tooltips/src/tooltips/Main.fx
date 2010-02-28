/*
 * Main.fx
 *
 * Created on 26-nov-2009, 17:21:34
 */

package tooltips;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipCreator;

/**
 * @author sikken
 */

var tooltipManager = SimpleTooltipManager{};
var tooltipCreator = SimpleTooltipCreator{};
var rect:Rectangle;

println("starting app");

rect = Rectangle {
  x: 100, y: 50
  width: 50, height: 50
  fill: Color.GRAY
  onMouseEntered: function( e: MouseEvent ):Void {
     println("onMouseEntered");
  }
  onMouseExited: function( e: MouseEvent ):Void {
     println("onMouseExited");
  }
}

tooltipManager.registerNode(rect,tooltipCreator);

Stage {
    title: "Application title"
    width: 250
    height: 200
    scene: Scene {
        content: [
           rect
//           TooltipNode{
//              sourceNode:rect
//              tooltip:"testing"
//           }

        ]
    }
}