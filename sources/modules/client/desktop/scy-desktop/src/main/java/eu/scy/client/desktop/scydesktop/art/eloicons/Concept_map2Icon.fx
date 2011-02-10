package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
/**
 * @author lars
 */
public class Concept_map2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.44
					x: 2.42
					y: 1.95
					width: 4.75
					height: 2.8
				},
				Rectangle {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 1.84
					y: 7.23
					width: 4.49
					height: 2.29
				},
				Line {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.31
					startY: 2.75
					endX: 7.17
					endY: 2.75
				},
				Line {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 3.87
					startY: 4.75
					endX: 3.87
					endY: 7.15
				},
				Polyline {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [3.87,9.51,3.87,12.50,7.77,12.50]
				},
				Line {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 10.89
					startY: 11.38
					endX: 15.6
					endY: 8.37
				},
				Line {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 12.41
					startY: 12.5
					endX: 16.08
					endY: 12.5
				},
				Line {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 15.6
					startY: 7.15
					endX: 12.41
					endY: 3.35
				},
				Rectangle {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 7.77
					y: 11.38
					width: 5.59
					height: 3.01
				},
				Rectangle {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.48
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 10.31
					y: 1.55
					width: 3.24
					height: 2.4
},
]
}
}
}
