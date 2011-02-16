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
public class Evaluation_reportIcon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.rgb(0xd3,0xe6,0xf6)
					stroke: null
					content: "M225.28,37.13 C222.10,33.31 218.53,29.74 213.33,29.13 C207.74,28.47 202.51,30.18 197.38,32.27 C195.25,33.14 194.93,35.19 195.67,36.82 C195.62,36.91 195.56,36.98 195.51,37.08 C190.85,46.72 191.46,55.53 199.88,62.95 C207.18,69.38 216.91,69.78 224.80,64.21 C228.79,61.39 231.49,56.68 232.09,51.90 C232.79,46.36 228.56,41.05 225.28,37.13 Z M220.22,58.74 C215.60,62.58 209.33,61.29 204.94,57.96 C198.09,52.76 198.91,46.33 202.22,39.49 C202.49,38.93 202.59,38.40 202.57,37.91 C208.51,35.73 214.21,34.92 218.85,40.58 C220.99,43.18 223.08,45.58 224.49,48.68 C226.30,52.67 223.05,56.39 220.22,58.74 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.98
					strokeLineCap: StrokeLineCap.BUTT
					content: "M223.38,51.16 C223.38,58.41 217.51,64.28 210.27,64.28 C203.02,64.28 197.15,58.41 197.15,51.16 C197.15,43.92 203.02,38.05 210.27,38.05 C217.51,38.05 223.38,43.92 223.38,51.16 Z "
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 5.27
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [198.68,39.58,208.51,59.59,229.57,33.96]
},
]
}
}
}
