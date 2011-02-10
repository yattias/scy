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
public class Exp_designIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.rgb(0xff,0x54,0x0)
					stroke: null
					x: 0.03
					y: 0.03
					width: 16.0
					height: 16.0
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 1.52
					y: 1.48
					width: 13.02
					height: 3.47
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M14.03,1.98 L14.03,4.44 L2.02,4.44 L2.02,1.98 Z M15.03,0.97 L1.02,0.97 L1.02,5.44 L15.03,5.44 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 5.71
					y: 4.51
					width: 4.63
					height: 10.54
},
]
}
}
}
