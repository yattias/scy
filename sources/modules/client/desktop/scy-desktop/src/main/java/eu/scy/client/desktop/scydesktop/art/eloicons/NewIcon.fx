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
public class NewIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Polygon {
					points: [12.85,3.21,10.19,8.03,12.85,12.85,8.03,10.19,3.21,12.85,5.87,8.03,3.21,3.21,8.03,5.87]
					fill: null
					stroke: Color.rgb(0x1f,0x16,0x62)
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
},
]
}
}
}
