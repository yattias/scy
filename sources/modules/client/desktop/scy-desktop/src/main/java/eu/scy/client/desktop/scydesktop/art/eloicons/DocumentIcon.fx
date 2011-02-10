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
public class DocumentIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Polyline {
					fill: Color.WHITE
					stroke: null
					points: [5.67,16.03]
				},
				Polyline {
					fill: Color.WHITE
					stroke: null
					points: [5.68,16.03]
				},
				Polygon {
					points: [5.53,15.88,15.88,15.88,15.88,5.48]
					fill: Color.WHITE
					stroke: null
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 3.82
					y: 3.99
					width: 12.21
					height: 12.03
				},
				Polygon {
					points: [6.39,14.31,6.61,6.31,14.53,6.18]
					fill: Color.rgb(0xfb,0x6,0xa2)
					stroke: null
				},
				Polygon {
					points: [6.39,14.31,6.61,6.31,14.53,6.18]
					fill: null
					stroke: Color.rgb(0xfb,0x6,0xa2)
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
},
]
}
}
}
