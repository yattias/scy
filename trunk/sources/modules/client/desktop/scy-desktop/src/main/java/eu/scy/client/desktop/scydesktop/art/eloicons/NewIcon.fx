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
public class NewIcon extends AbstractEloIcon {

public override function clone(): NewIcon {
NewIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M230.45,65.28 C228.53,62.26 226.62,59.29 225.22,55.99 C224.65,54.67 221.23,49.58 221.81,48.24 C223.97,43.26 228.36,38.78 229.04,33.16 C229.08,32.81 228.72,32.61 228.41,32.61 C226.46,32.61 224.55,33.92 222.78,34.62 C219.39,35.98 215.92,37.73 212.88,39.78 C207.18,37.89 202.21,34.14 197.38,30.73 C196.94,30.42 195.40,30.77 195.66,31.51 C197.47,36.64 198.76,42.45 202.22,46.76 C203.70,48.60 199.63,54.76 198.80,56.58 C197.50,59.42 195.66,61.94 194.40,64.80 C194.04,65.62 195.29,65.54 195.72,65.38 C198.71,64.23 201.79,63.40 204.74,62.17 C205.42,61.89 212.48,57.85 212.99,58.41 C214.33,59.90 217.68,60.87 219.45,61.77 C222.58,63.35 225.96,64.08 228.81,66.21 C229.33,66.60 230.94,66.05 230.45,65.28 Z M213.22,56.65 C211.26,56.31 208.25,59.15 206.72,59.91 C203.55,61.48 200.18,62.49 196.86,63.66 C198.04,61.31 199.47,59.10 200.63,56.73 C201.82,54.28 204.35,50.62 204.78,47.97 C205.05,46.27 202.47,43.93 201.84,42.68 C200.41,39.86 199.45,36.68 198.46,33.61 C202.97,36.79 207.71,39.95 213.05,41.41 C213.72,41.60 215.02,40.74 214.46,40.33 C218.56,37.64 223.20,35.92 227.66,33.94 C226.17,34.60 225.55,37.79 224.93,39.17 C223.85,41.54 222.63,43.84 221.31,46.07 C219.59,48.99 219.15,49.27 221.13,52.16 C222.64,54.35 223.26,56.88 224.48,59.20 C225.18,60.53 225.96,61.83 226.77,63.11 C224.40,62.01 221.88,61.19 219.51,59.99 C217.56,59.00 215.35,57.03 213.22,56.65 Z "
				},
				Polygon {
					points: [224.93,36.39,218.06,48.82,224.93,61.24,212.50,54.38,200.07,61.24,206.94,48.82,200.07,36.39,212.50,43.26]
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.58
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
},
]
}
}
}
