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
public class Layer_1Icon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.rgb(0xd3,0xe6,0xf6)
					stroke: Color.rgb(0xd3,0xe6,0xf6)
					strokeLineCap: StrokeLineCap.BUTT
					content: "M230.04,44.50 C228.83,36.14 220.77,33.75 214.98,33.55 C214.52,32.93 213.88,32.53 213.07,32.62 C204.41,33.54 196.47,37.74 194.63,50.20 C192.53,64.43 206.88,66.54 214.06,65.39 C221.83,64.15 231.80,56.72 230.04,44.50 Z M223.84,52.81 C221.88,56.20 218.49,57.99 215.46,58.83 C210.43,60.21 198.58,61.19 199.27,50.74 C199.82,42.57 206.02,40.00 211.33,39.06 C211.66,39.48 212.13,39.74 212.77,39.71 C216.64,39.54 221.83,39.93 224.70,43.87 C226.60,46.50 225.22,50.42 223.84,52.81 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M228.58,49.22 C228.58,55.85 221.89,61.24 213.62,61.24 C205.36,61.24 198.66,55.85 198.66,49.22 C198.66,42.58 205.36,37.20 213.62,37.20 C221.89,37.20 228.58,42.58 228.58,49.22 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.33
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M226.98,49.81 C226.98,55.02 220.78,59.24 213.13,59.24 C205.48,59.24 199.28,55.02 199.28,49.81 C199.28,44.60 205.48,40.38 213.13,40.38 C220.78,40.38 226.98,44.60 226.98,49.81 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M222.57,50.15 C222.57,53.54 219.96,56.29 216.75,56.29 C213.54,56.29 210.93,53.54 210.93,50.15 C210.93,46.76 213.54,44.01 216.75,44.01 C219.96,44.01 222.57,46.76 222.57,50.15 Z "
},
]
}
}
}
