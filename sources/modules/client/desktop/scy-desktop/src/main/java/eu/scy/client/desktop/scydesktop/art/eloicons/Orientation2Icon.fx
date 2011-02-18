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
public class Orientation2Icon extends AbstractEloIcon {

public override function clone(): Orientation2Icon {
Orientation2Icon {
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
					stroke: bind windowColorScheme.mainColorLight
					strokeLineCap: StrokeLineCap.BUTT
					content: "M230.04,44.50 C228.83,36.14 220.77,33.75 214.98,33.55 C214.52,32.93 213.88,32.53 213.07,32.62 C204.41,33.54 196.47,37.74 194.63,50.20 C192.53,64.43 206.88,66.54 214.06,65.39 C221.83,64.15 231.80,56.72 230.04,44.50 Z M223.84,52.81 C221.88,56.20 218.49,57.99 215.46,58.83 C210.43,60.21 198.58,61.19 199.27,50.74 C199.82,42.57 206.02,40.00 211.33,39.06 C211.66,39.48 212.13,39.74 212.77,39.71 C216.64,39.54 221.83,39.93 224.70,43.87 C226.60,46.50 225.22,50.42 223.84,52.81 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M228.58,49.47 C228.58,56.79 221.19,62.74 212.06,62.74 C202.94,62.74 195.55,56.79 195.55,49.47 C195.55,42.13 202.94,36.20 212.06,36.20 C221.19,36.20 228.58,42.13 228.58,49.47 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.47
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M226.81,50.12 C226.81,55.87 219.97,60.53 211.52,60.53 C203.08,60.53 196.23,55.87 196.23,50.12 C196.23,44.37 203.08,39.71 211.52,39.71 C219.97,39.71 226.81,44.37 226.81,50.12 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M221.94,50.50 C221.94,54.24 219.07,57.28 215.52,57.28 C211.97,57.28 209.10,54.24 209.10,50.50 C209.10,46.75 211.97,43.72 215.52,43.72 C219.07,43.72 221.94,46.75 221.94,50.50 Z "
},
]
}
}
}
