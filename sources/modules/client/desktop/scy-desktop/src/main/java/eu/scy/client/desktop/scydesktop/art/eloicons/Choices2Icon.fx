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
public class Choices2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Polyline {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.3
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [10.70,6.78]
				},
				Polyline {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.53
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [6.03,8.46]
				},
				Polyline {
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.22
					points: [14.01,13.98,14.01,8.75,4.00,8.75,2.27,11.44,4.00,14.13,8.45,14.13,12.72,14.09,14.01,14.09]
				},
				Polygon {
					points: [8.64,13.78,7.54,13.78,7.54,13.07,8.64,13.07]
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.22
				},
				SVGPath {
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeWidth: 0.36
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M6.32,10.65 C6.35,10.23 6.37,9.84 6.99,9.49 C7.32,9.31 7.76,9.21 8.19,9.21 C9.13,9.21 9.96,9.63 9.96,10.42 C9.96,10.82 9.75,11.03 9.18,11.44 C8.69,11.78 8.44,12.02 8.45,12.56 L7.72,12.56 C7.72,11.86 7.92,11.68 8.51,11.24 C9.06,10.82 9.18,10.74 9.18,10.38 C9.18,10.12 8.98,9.68 8.18,9.68 C7.11,9.68 7.06,10.39 7.06,10.65 Z "
				},
				Polygon {
					points: [12.13,1.86,13.85,4.55,12.13,7.24,2.11,7.24,2.11,1.86]
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.22
				},
				Polygon {
					points: [7.48,6.95,8.58,6.95,8.58,6.25,7.48,6.25]
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.22
				},
				SVGPath {
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeWidth: 0.36
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M9.80,3.82 C9.77,3.41 9.75,3.01 9.13,2.67 C8.80,2.48 8.36,2.39 7.93,2.39 C6.99,2.39 6.16,2.80 6.16,3.59 C6.16,4.00 6.37,4.21 6.94,4.61 C7.43,4.96 7.69,5.19 7.67,5.73 L8.40,5.73 C8.40,5.03 8.20,4.86 7.61,4.42 C7.06,4.00 6.94,3.91 6.94,3.55 C6.94,3.30 7.14,2.86 7.95,2.86 C9.02,2.86 9.07,3.57 9.07,3.82 Z "
},
]
}
}
}
