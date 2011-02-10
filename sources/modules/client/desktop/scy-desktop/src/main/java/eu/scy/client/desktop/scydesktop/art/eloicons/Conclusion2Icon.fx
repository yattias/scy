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
public class Conclusion2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Line {
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 1.11
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 8.03
					startY: 2.05
					endX: 8.03
					endY: 13.92
				},
				Line {
					fill: Color.WHITE
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 1.11
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 1.68
					startY: 1.83
					endX: 14.39
					endY: 6.89
				},
				Line {
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.54
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 3.38
					startY: 4.61
					endX: 3.38
					endY: 2.42
				},
				Line {
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.55
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 12.79
					startY: 9.14
					endX: 12.79
					endY: 6.58
				},
				Polygon {
					points: [10.16,14.92,5.91,14.92,8.05,13.17]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.82
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Polygon {
					points: [14.36,10.31,11.22,10.31,12.80,8.92]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.63
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Polygon {
					points: [4.95,6.00,1.81,6.00,3.39,4.61]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeWidth: 0.63
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
},
]
}
}
}
