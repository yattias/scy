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
public class OrientationIcon extends AbstractEloIcon {

public function clone(): OrientationIcon {
OrientationIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: null
					content: "M229.81,44.78 C226.31,33.64 216.46,30.76 206.04,31.45 C204.64,31.54 204.09,32.63 204.20,33.72 C204.15,33.75 204.09,33.77 204.04,33.81 C196.54,39.18 191.12,46.57 194.55,56.13 C197.78,65.12 209.80,67.57 217.97,65.79 C226.85,63.87 232.54,53.46 229.81,44.78 Z M214.77,61.63 C208.64,62.50 201.52,60.01 198.74,54.19 C195.36,47.09 201.91,41.03 207.00,37.38 C207.55,36.98 207.82,36.49 207.88,35.99 C214.93,35.68 220.99,37.20 224.66,44.10 C228.86,52.01 222.67,60.52 214.77,61.63 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M211.67,62.95 C207.22,62.95 203.07,60.75 200.56,57.06 C196.37,50.90 197.94,42.47 204.06,38.25 C206.31,36.71 208.94,35.89 211.65,35.89 C216.10,35.89 220.25,38.09 222.76,41.78 C226.95,47.94 225.38,56.37 219.25,60.58 C217.01,62.13 214.38,62.95 211.67,62.95 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M212.00,35.00 L212.00,36.81 C216.00,36.81 219.84,38.86 222.18,42.30 C226.08,48.04 224.53,55.90 218.82,59.83 C216.73,61.26 214.24,62.03 211.72,62.03 C207.57,62.03 203.68,59.98 201.34,56.54 C197.43,50.80 199.06,42.94 204.77,39.01 C206.87,37.57 209.00,36.81 212.00,36.81 Z M211.65,34.97 C208.85,34.97 206.02,35.79 203.54,37.50 C196.99,42.00 195.31,50.99 199.80,57.58 C202.58,61.67 207.09,63.87 211.67,63.87 C214.47,63.87 217.29,63.05 219.78,61.34 C226.33,56.84 228.01,47.85 223.52,41.26 C220.74,37.17 216.23,34.97 211.65,34.97 Z "
				},
				Polygon {
					points: [210.99,44.19,220.59,39.43,215.88,49.13,220.59,58.84,210.99,54.07,201.39,58.84,206.10,49.13,201.39,39.43]
					fill: bind windowColorScheme.secondColor
					stroke: null
				},
				Polygon {
					points: [211.95,49.00,229.14,49.00,214.72,51.69]
					fill: Color.WHITE
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M228.06,49.00 L214.76,51.48 L212.20,49.00 L228.00,49.00 M230.21,49.00 L211.71,49.00 L214.69,51.90 Z "
				},
				Polygon {
					points: [211.71,49.00,230.21,49.00,214.69,46.10]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [194.68,49.00,209.14,46.30,211.86,49.00]
					fill: Color.WHITE
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M209.06,46.51 L211.61,49.00 L195.75,49.00 Z M209.12,46.10 L193.60,49.00 L212.11,49.00 Z "
				},
				Polygon {
					points: [212.11,49.00,193.60,49.00,209.12,51.90]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [209.33,51.92,212.00,49.16,212.00,66.52]
					fill: Color.WHITE
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M212.00,49.00 L212.00,65.42 L209.54,52.00 L212.00,49.42 M212.00,48.92 L209.13,51.93 L212.00,67.62 Z "
				},
				Polygon {
					points: [212.00,48.92,212.00,67.62,214.87,51.93]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [212.00,31.72,214.67,46.32,212.00,49.07]
					fill: Color.WHITE
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M212.00,32.82 L214.46,46.24 L212.00,48.82 L212.00,33.00 M212.00,30.62 L212.00,49.32 L214.87,46.30 Z "
				},
				Polygon {
					points: [212.00,49.32,212.00,30.62,209.13,46.30]
					fill: bind windowColorScheme.mainColor
					stroke: null
},
]
}
}
}
