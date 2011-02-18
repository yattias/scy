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
public class SearchIcon extends AbstractEloIcon {

public override function clone(): SearchIcon {
SearchIcon {
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
					content: "M227.05,33.83 C224.50,29.69 219.54,30.64 215.44,31.79 C215.30,31.71 215.11,31.65 214.83,31.69 C210.64,32.27 206.65,33.65 204.41,37.44 C203.21,39.48 203.03,43.53 202.93,45.83 C202.88,46.84 203.28,47.69 203.63,48.59 C204.99,52.05 199.66,54.60 197.65,55.85 C193.67,58.33 191.98,61.35 193.75,65.88 C195.15,69.44 200.08,65.98 201.90,64.72 C204.43,62.98 206.57,60.65 208.90,58.66 C210.87,56.98 215.49,59.47 218.05,59.37 C222.80,59.18 228.06,54.06 229.40,49.77 C230.99,44.69 229.80,38.27 227.05,33.83 Z M221.90,56.20 C215.81,59.06 215.29,54.86 210.37,56.17 C207.44,56.96 205.16,59.87 203.00,61.83 C201.65,63.05 200.21,64.11 198.63,65.03 C196.29,65.08 195.11,64.19 195.09,62.37 C194.92,60.26 195.75,58.71 197.56,57.73 C200.04,55.74 202.87,54.52 205.14,52.23 C206.72,50.64 205.76,48.58 205.09,46.87 C204.27,44.78 205.58,41.84 205.86,39.75 C206.34,36.12 209.26,34.41 212.44,33.61 C212.60,33.87 212.94,34.03 213.44,33.89 C215.05,33.42 216.75,32.48 218.38,32.88 C221.44,33.63 226.30,35.75 227.44,39.88 C229.31,46.67 228.13,53.27 221.90,56.20 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M226.13,44.78 C226.13,50.01 221.89,54.25 216.66,54.25 C211.43,54.25 207.19,50.01 207.19,44.78 C207.19,39.55 211.43,35.31 216.66,35.31 C221.89,35.31 226.13,39.55 226.13,44.78 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M227.77,49.12 C229.95,42.84 226.61,35.97 220.33,33.79 C219.03,33.34 217.70,33.13 216.39,33.13 C211.40,33.13 206.73,36.26 205.00,41.24 C202.83,47.53 206.16,54.39 212.45,56.57 C213.75,57.02 215.08,57.23 216.39,57.23 C221.38,57.23 226.05,54.10 227.77,49.12 Z M216.39,53.97 C215.42,53.97 214.45,53.80 213.52,53.48 C211.30,52.71 209.51,51.13 208.49,49.02 C207.47,46.91 207.32,44.53 208.09,42.31 C209.31,38.77 212.65,36.40 216.39,36.40 C217.37,36.40 218.33,36.56 219.26,36.88 L219.26,36.88 C221.48,37.65 223.27,39.24 224.29,41.34 C225.31,43.46 225.46,45.84 224.69,48.05 C223.46,51.59 220.13,53.97 216.39,53.97 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.14
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 210.08
					startY: 51.23
					endX: 194.0
					endY: 65.33
},
]
}
}
}
