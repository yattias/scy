/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art.javafx;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import javafx.scene.layout.Stack;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author SikkenJ
 */
public class MissionMapWindowIcon extends EloIcon {

   public override function create(): Node {
      Stack {
         content: [
            Group {
//               clip: Polygon {
//                  points: [0.02, 40.01, 40.02, 40.01, 40.02, 0.01, 0.02, 0.01]
//                  fill: null
//                  stroke: null
//               }
               content: [
                  Group {
                     clip: Polygon {
                        points: [0.02, 0.01, 40.02, 0.01, 40.02, 40.01, 0.02, 40.01]
                        fill: null
                        stroke: null
                     }
                     content: [
                        Group {
                           opacity: 0.5
                           content: [
                              Group {
                                 clip: Polygon {
                                    points: [0.00, 40.04, 40.00, 40.04, 40.00, -0.00, 0.00, -0.00]
                                    fill: null
                                    stroke: null
                                 }
                                 content: [
                                    Polygon {
                                       points: [40.00, 40.04, 0.00, 40.04, 0.00, -0.00, 40.00, -0.00]
                                       fill: Color.WHITE
                                       stroke: null
                                    },]
                              },]
                        },]
                  },
                  Polyline {
                     fill: null
                     stroke: Color.rgb(0x1d, 0x1d, 0x1b)
                     strokeWidth: 1.51
                     strokeLineCap: StrokeLineCap.BUTT
                     points: [33.10, 7.13, 9.89, 20.22, 33.10, 32.91]
                  },
                  Polyline {
                     fill: Color.rgb(0x25, 0x31, 0x65)
                     stroke: null
                     points: [0.08, 29.85, 19.70, 29.85, 19.70, 10.58, 0.08, 10.58]
                  },
                  Polygon {
                     points: [27.28, 1.10, 38.93, 1.10, 38.93, 13.16, 27.28, 13.16]
                     fill: Color.WHITE
                     stroke: null
                  },
                  SVGPath {
                     fill: Color.rgb(0x4e, 0x76, 0xba)
                     stroke: null
                     content: "M37.93,12.16 L28.28,12.16 L28.28,2.10 L37.93,2.10 Z M39.93,0.10 L26.28,0.10 L26.28,14.16 L39.93,14.16 Z "
                  },
                  Polygon {
                     points: [27.28, 26.88, 38.93, 26.88, 38.93, 38.94, 27.28, 38.94]
                     fill: Color.WHITE
                     stroke: null
                  },
                  SVGPath {
                     fill: Color.rgb(0x4e, 0x76, 0xba)
                     stroke: null
                     content: "M37.93,37.94 L28.28,37.94 L28.28,27.88 L37.93,27.88 Z M39.93,25.88 L26.28,25.88 L26.28,39.94 L39.93,39.94 Z "
                  },
                  SVGPath {
                     fill: Color.rgb(0x25, 0x31, 0x65)
                     stroke: null
                     content: "M2.04,2.03 L37.99,2.03 L37.99,38.04 L2.04,38.04 Z M0.02,40.01 L40.02,40.01 L40.02,0.01 L0.02,0.01 Z "
                  },]
            },]
      }
   }

}


function run(){
   Stage {
	title: "MyApp"
	onClose: function () {  }
	scene: Scene {
		width: 200
		height: 200
		content: [
         MissionMapWindowIcon{
            layoutX: 0
            layoutY: 0
         }

      ]
	}
}

}
