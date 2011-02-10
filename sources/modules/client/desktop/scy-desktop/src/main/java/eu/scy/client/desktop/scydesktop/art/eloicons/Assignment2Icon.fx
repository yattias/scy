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
public class Assignment2Icon extends AbstractEloIcon {

//public function createNodeContent(): Node {
//
//}

public function createNode(): Node {

return Group {

			content: [
				Polyline {
					fill: Color.WHITE
					stroke: Color.rgb(0x0,0x42,0xf1)
					strokeWidth: 0.27
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [8.03,6.89]
				},
				Polygon {
					points: [9.20,13.82,6.60,13.82,6.60,12.04,9.20,12.04]
					fill: Color.rgb(0xe,0xa7,0xbf)
					stroke: Color.rgb(0xe,0xa7,0xbf)
					strokeWidth: 0.2
				},
				SVGPath {
					fill: Color.rgb(0xe,0xa7,0xbf)
					stroke: Color.rgb(0xe,0xa7,0xbf)
					strokeWidth: 0.87
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M3.72,5.89 C3.80,4.83 3.84,3.83 5.31,2.96 C6.09,2.49 7.12,2.24 8.15,2.24 C10.36,2.24 12.34,3.30 12.34,5.30 C12.34,6.33 11.82,6.86 10.48,7.88 C9.34,8.76 8.73,9.35 8.76,10.72 L7.04,10.72 C7.04,8.95 7.51,8.51 8.90,7.39 C10.20,6.33 10.48,6.11 10.48,5.20 C10.48,4.55 10.01,3.43 8.11,3.43 C5.58,3.43 5.46,5.24 5.46,5.89 Z "
},
]
}
}
}
