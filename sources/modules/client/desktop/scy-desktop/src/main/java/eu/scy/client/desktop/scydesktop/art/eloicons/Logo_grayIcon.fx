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
public class Logo_grayIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M16.03,8.04 C16.03,12.45 12.45,16.03 8.04,16.03 C3.62,16.03 0.04,12.45 0.04,8.04 C0.04,3.63 3.62,0.05 8.04,0.05 C12.45,0.05 16.03,3.63 16.03,8.04 Z "
				},
				SVGPath {
					fill: Color.rgb(0x47,0x47,0x47)
					stroke: null
					content: "M15.90,6.70 C15.26,2.92 11.98,0.04 8.02,0.04 C5.85,0.04 3.88,0.91 2.44,2.32 L11.42,6.69 Z M0.94,4.33 C0.36,5.44 0.03,6.69 0.03,8.03 C0.03,9.34 0.35,10.57 0.91,11.66 L8.29,7.96 Z M11.39,9.21 L2.42,13.72 C3.86,15.14 5.84,16.02 8.02,16.02 C12.04,16.02 15.35,13.06 15.92,9.21 Z "
},
]
}
}
}
