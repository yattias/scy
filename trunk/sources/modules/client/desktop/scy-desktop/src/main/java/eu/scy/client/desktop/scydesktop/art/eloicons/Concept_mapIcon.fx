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
public class Concept_mapIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Line {
					fill: null
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 5.19
					startY: 10.94
					endX: 0.95
					endY: 15.17
				},
				Rectangle {
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: null
					x: 9.04
					y: 0.55
					width: 6.49
					height: 6.46
				},
				Polygon {
					points: [7.00,14.18,8.86,7.24,1.91,9.10]
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: null
},
]
}
}
}
