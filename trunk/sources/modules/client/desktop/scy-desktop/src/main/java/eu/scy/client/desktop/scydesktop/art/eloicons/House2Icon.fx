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
public class House2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Polyline {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.17
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 1.0
					strokeDashArray: [
					0.48,
					]
					points: [3.66,6.86]
				},
				Polygon {
					points: [13.35,14.25,11.26,14.25,11.26,9.20,8.37,9.20,8.37,14.25,2.71,14.25,2.71,6.50,8.03,1.82,13.35,6.50]
					fill: Color.rgb(0xff,0x54,0x0)
					stroke: null
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 3.95
					y: 8.69
					width: 2.91
					height: 2.56
				},
				Polygon {
					points: [1.61,7.47,8.03,1.82,14.45,7.47]
					fill: Color.rgb(0xff,0x54,0x0)
					stroke: Color.WHITE
					strokeWidth: 0.24
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Rectangle {
					fill: Color.rgb(0xff,0x54,0x0)
					stroke: null
					x: 10.11
					y: 2.08
					width: 1.68
					height: 3.49
				},
				Polyline {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.17
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 1.0
					strokeDashArray: [
					0.48,
					]
					points: [3.66,6.86,8.15,2.91,12.64,6.86]
},
]
}
}
}
