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
public class Concept_mapIcon extends AbstractEloIcon {

public override function clone(): Concept_mapIcon {
Concept_mapIcon {
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
					content: "M227.22,39.12 C227.22,39.07 227.23,39.03 227.23,38.97 C227.17,37.01 227.21,38.61 227.22,39.12 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M227.22,39.12 C227.22,39.14 227.22,39.15 227.22,39.17 C227.22,39.32 227.22,39.27 227.22,39.12 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M219.23,47.76 C215.72,47.34 212.19,47.02 208.67,46.67 C208.24,46.63 207.78,46.59 207.31,46.56 C210.39,46.12 213.46,45.67 216.53,45.16 C219.90,44.60 227.18,44.25 227.22,39.17 C227.21,39.04 227.21,38.79 227.19,38.31 C227.16,37.46 226.82,36.45 226.18,35.89 C224.17,34.15 222.02,33.15 219.82,31.76 C214.94,28.68 207.19,29.57 201.70,29.29 C197.46,29.08 197.69,36.08 201.91,36.29 C206.90,36.54 212.19,36.33 216.85,38.12 C207.22,39.52 189.64,39.30 192.25,48.66 C193.57,53.37 202.98,53.03 206.38,53.41 C208.56,53.65 210.73,53.86 212.91,54.08 C209.79,54.66 206.66,55.17 203.55,55.80 C200.45,56.44 195.12,58.09 194.80,61.74 C192.14,67.47 200.49,68.66 204.34,69.00 C208.03,69.32 208.32,64.05 205.55,62.44 C207.08,62.14 208.67,61.97 210.18,61.70 C216.64,60.57 224.79,59.99 230.69,56.82 C231.77,56.24 232.25,54.91 232.28,53.71 C232.43,47.98 222.69,48.18 219.23,47.76 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 4.45
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 204.96
					startY: 56.16
					endX: 195.52
					endY: 65.57
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 213.52
					y: 33.04
					width: 14.43
					height: 14.36
				},
				Polygon {
					points: [208.97,63.35,213.11,47.92,197.67,52.06]
					fill: bind windowColorScheme.secondColor
					stroke: null
},
]
}
}
}
