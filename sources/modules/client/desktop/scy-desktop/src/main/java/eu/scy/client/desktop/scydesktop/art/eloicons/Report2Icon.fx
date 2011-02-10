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
public class Report2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.WHITE
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 3.25
					y: 2.03
					width: 9.67
					height: 12.3
				},
				Line {
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 4.3
					startY: 3.58
					endX: 8.08
					endY: 3.58
				},
				Line {
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 4.3
					startY: 5.52
					endX: 11.93
					endY: 5.52
				},
				Line {
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 4.3
					startY: 7.36
					endX: 11.93
					endY: 7.36
				},
				Line {
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 4.3
					startY: 9.19
					endX: 11.93
					endY: 9.19
				},
				Line {
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 4.3
					startY: 11.02
					endX: 11.93
					endY: 11.02
				},
				Line {
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 4.3
					startY: 12.85
					endX: 11.93
					endY: 12.85
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 11.26
					y: 1.65
					width: 2.14
					height: 2.06
				},
				Polygon {
					points: [12.78,3.89,11.07,3.89,11.07,2.18]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.27
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Line {
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.26
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 11.17
					startY: 1.92
					endX: 13.02
					endY: 3.79
},
]
}
}
}
