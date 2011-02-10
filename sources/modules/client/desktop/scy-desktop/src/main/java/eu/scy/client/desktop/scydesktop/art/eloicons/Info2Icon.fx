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
public class Info2Icon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.rgb(0xe,0xa7,0xbf)
					stroke: Color.WHITE
					strokeWidth: 0.5
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 6.11
					y: 6.52
					width: 4.0
					height: 7.94
				},
				SVGPath {
					fill: Color.rgb(0xe,0xa7,0xbf)
					stroke: null
					content: "M10.03,3.96 C10.03,5.07 9.14,5.96 8.03,5.96 C6.93,5.96 6.03,5.07 6.03,3.96 C6.03,2.86 6.93,1.96 8.03,1.96 C9.14,1.96 10.03,2.86 10.03,3.96 Z "
},
]
}
}
}
