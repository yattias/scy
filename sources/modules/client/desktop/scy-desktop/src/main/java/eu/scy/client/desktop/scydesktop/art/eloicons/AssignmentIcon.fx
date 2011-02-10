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
public class AssignmentIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Polygon {
					points: [9.24,14.31,6.55,14.31,6.55,12.91,9.24,12.91]
					fill: Color.rgb(0x9f,0x8b,0x55)
					stroke: null
				},
				SVGPath {
					fill: Color.rgb(0x9f,0x8b,0x55)
					stroke: Color.rgb(0x9f,0x8b,0x55)
					strokeWidth: 0.91
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M3.57,6.37 C3.65,5.27 3.69,4.24 5.21,3.33 C6.03,2.85 7.09,2.59 8.15,2.59 C10.45,2.59 12.49,3.69 12.49,5.76 C12.49,6.82 11.96,7.37 10.57,8.44 C9.38,9.34 8.75,9.96 8.79,11.38 L7.01,11.38 C7.01,9.54 7.50,9.08 8.93,7.92 C10.28,6.82 10.57,6.60 10.57,5.66 C10.57,4.98 10.08,3.82 8.11,3.82 C5.49,3.82 5.37,5.69 5.37,6.37 Z "
},
]
}
}
}
