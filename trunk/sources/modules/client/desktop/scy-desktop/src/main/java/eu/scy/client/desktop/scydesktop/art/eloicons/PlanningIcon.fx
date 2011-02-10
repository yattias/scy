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
public class PlanningIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.WHITE
					stroke: Color.rgb(0x8d,0xb8,0x0)
					strokeWidth: 1.91
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M13.79,8.03 C13.79,11.15 11.24,13.69 8.09,13.69 C4.94,13.69 2.38,11.15 2.38,8.03 C2.38,4.91 4.94,2.37 8.09,2.37 C11.24,2.37 13.79,4.91 13.79,8.03 Z "
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x8d,0xb8,0x0)
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [8.04,4.19,8.04,8.03,4.65,8.03]
				},
				Line {
					fill: null
					stroke: Color.rgb(0x8d,0xb8,0x0)
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.ROUND
					strokeMiterLimit: 4.0
					startX: 13.49
					startY: 8.04
					endX: 11.86
					endY: 8.04
				},
				Line {
					fill: null
					stroke: Color.rgb(0x8d,0xb8,0x0)
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.ROUND
					strokeMiterLimit: 4.0
					startX: 8.02
					startY: 13.49
					endX: 8.02
					endY: 11.78
				},
				Polygon {
					points: [5.53,6.78,3.34,8.02,5.51,9.28]
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
				},
				Polygon {
					points: [9.24,5.36,7.99,3.20,6.73,5.36]
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
},
]
}
}
}
