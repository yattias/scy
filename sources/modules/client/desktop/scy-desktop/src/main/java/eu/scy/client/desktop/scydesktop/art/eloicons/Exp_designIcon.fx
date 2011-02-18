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
public class Exp_designIcon extends AbstractEloIcon {

public override function clone(): Exp_designIcon {
Exp_designIcon {
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
					content: "M209.76,36.33 C215.29,36.48 220.83,36.46 226.36,36.41 C228.95,36.38 228.50,32.70 226.01,32.72 C218.59,32.79 211.17,32.71 203.74,32.52 C201.84,32.46 199.26,32.09 197.50,33.16 C195.65,34.28 196.59,37.80 196.27,39.39 C195.01,45.76 202.78,46.14 210.53,45.90 C210.11,45.96 209.68,46.02 209.27,46.09 C205.82,46.67 200.69,46.60 198.17,49.31 C197.75,49.77 197.50,50.54 197.80,51.13 C199.60,54.67 203.38,55.12 207.04,55.44 C210.77,55.78 215.18,55.58 219.12,56.38 C218.69,56.63 218.33,56.83 218.18,56.90 C216.39,57.70 214.60,58.49 212.81,59.30 C208.58,61.23 204.62,63.52 200.49,65.63 C198.21,66.80 200.66,69.82 202.76,68.74 C207.80,66.16 212.70,63.45 217.90,61.14 C219.91,60.24 225.44,59.01 225.88,56.41 C226.02,55.55 225.56,54.76 224.78,54.38 C218.60,51.37 210.77,52.46 204.17,51.38 C199.74,50.66 208.25,50.04 207.72,50.12 C210.06,49.74 212.39,49.31 214.74,48.93 C218.55,48.30 222.37,48.00 226.12,47.01 C227.48,46.64 227.91,44.75 226.74,43.89 C223.85,41.78 220.22,42.05 216.76,42.01 C211.56,41.94 206.32,42.38 201.16,41.51 C201.44,41.56 200.08,36.24 200.30,36.22 C203.39,35.92 206.66,36.25 209.76,36.33 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 208.27
					y: 41.91
					width: 8.98
					height: 22.3
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 198.99
					y: 35.5
					width: 27.54
					height: 7.33
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M225.47,36.56 L225.47,41.77 L200.05,41.77 L200.05,36.56 Z M227.59,34.44 L197.93,34.44 L197.93,43.89 L227.59,43.89 Z "
},
]
}
}
}
