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
public class SearchIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 0.03
					y: 0.03
					width: 16.0
					height: 16.0
				},
				SVGPath {
					fill: Color.rgb(0x0,0x1,0x5f)
					stroke: null
					content: "M13.34,7.83 C14.05,5.79 12.97,3.55 10.92,2.84 C10.50,2.70 10.07,2.63 9.64,2.63 C8.01,2.63 6.49,3.65 5.93,5.27 C5.23,7.32 6.31,9.55 8.36,10.26 C8.78,10.40 9.21,10.47 9.64,10.47 C11.26,10.47 12.78,9.45 13.34,7.83 Z M9.64,9.41 C9.32,9.41 9.01,9.36 8.70,9.25 C7.98,9.00 7.40,8.49 7.07,7.80 C6.74,7.11 6.69,6.34 6.94,5.62 C7.34,4.47 8.42,3.69 9.64,3.69 C9.96,3.69 10.27,3.74 10.57,3.85 L10.57,3.85 C11.30,4.10 11.88,4.62 12.21,5.30 C12.54,5.99 12.59,6.76 12.34,7.49 C11.94,8.64 10.86,9.41 9.64,9.41 Z "
				},
				Line {
					fill: null
					stroke: Color.rgb(0x0,0x1,0x5f)
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 7.58
					startY: 8.52
					endX: 2.35
					endY: 13.11
},
]
}
}
}
