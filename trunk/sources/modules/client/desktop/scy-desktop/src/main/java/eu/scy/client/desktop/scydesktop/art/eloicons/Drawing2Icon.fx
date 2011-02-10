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
public class Drawing2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Polygon {
					points: [2.55,12.79,2.99,13.30,2.01,13.60,1.98,13.60,1.95,13.60,1.92,13.60,1.89,13.59,1.86,13.59,1.84,13.58,1.82,13.55,1.80,13.54,1.77,13.49,1.76,13.43,1.76,13.37,1.76,13.31,2.12,12.30,2.55,12.79,2.55,12.79]
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.38
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.38
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					points: [2.11,12.30,3.08,9.47,5.74,12.41,2.99,13.30]
				},
				Rectangle {
					transforms: [Transform.rotate( -41.76, 8.38, 7.41)]
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.22
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					x: 3.64
					y: 5.29
					width: 9.48
					height: 4.25
				},
				Polygon {
					points: [13.70,5.51,10.87,2.34,11.60,1.69,14.43,4.86]
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.25
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
},
]
}
}
}
