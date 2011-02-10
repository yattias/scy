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
public class InfoIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.rgb(0xe,0xa7,0xbf)
					stroke: Color.WHITE
					strokeWidth: 1.03
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 6.03
					y: 6.37
					width: 4.0
					height: 7.94
				},
				SVGPath {
					fill: Color.rgb(0xe,0xa7,0xbf)
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M10.53,4.63 C10.53,6.01 9.41,7.13 8.03,7.13 C6.65,7.13 5.53,6.01 5.53,4.63 C5.53,3.25 6.65,2.13 8.03,2.13 C9.41,2.13 10.53,3.25 10.53,4.63 Z "
},
]
}
}
}
