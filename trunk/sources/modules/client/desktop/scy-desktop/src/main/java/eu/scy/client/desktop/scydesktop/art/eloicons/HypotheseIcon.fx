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
public class HypotheseIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.rgb(0xff,0x54,0x0)
					stroke: null
					content: "M13.57,7.78 C13.57,10.84 11.09,13.32 8.03,13.32 C4.97,13.32 2.49,10.84 2.49,7.78 C2.49,4.72 4.97,2.24 8.03,2.24 C11.09,2.24 13.57,4.72 13.57,7.78 Z "
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 16.06
					startY: 8.08
					endX: 0.0
					endY: 8.08
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 8.03
					startY: 0.0
					endX: 8.03
					endY: 16.06
				},
				SVGPath {
					fill: Color.rgb(0xff,0x54,0x0)
					stroke: null
					content: "M9.60,8.04 C9.60,8.91 8.90,9.61 8.03,9.61 C7.16,9.61 6.46,8.91 6.46,8.04 C6.46,7.18 7.16,6.48 8.03,6.48 C8.90,6.48 9.60,7.18 9.60,8.04 Z "
},
]
}
}
}
