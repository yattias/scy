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
public class Logo_scy_copyIcon extends CustomNode {

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
					fill: Color.rgb(0xc8,0xc8,0xc8)
					stroke: null
					content: "M15.79,6.70 C15.16,2.97 11.92,0.12 8.01,0.12 C5.87,0.12 3.92,0.98 2.50,2.37 L11.37,6.69 Z M11.34,9.18 L2.48,13.65 C3.90,15.05 5.85,15.92 8.01,15.92 C11.98,15.92 15.25,12.99 15.81,9.18 Z M1.02,4.36 C0.45,5.46 0.12,6.70 0.12,8.02 C0.12,9.31 0.43,10.53 0.98,11.61 L8.27,7.95 Z "
},
]
}
}
}
