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
public class HypotheseIcon extends AbstractEloIcon {

public function clone(): HypotheseIcon {
HypotheseIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M231.50,47.12 C231.02,44.81 229.93,42.74 228.43,41.02 C228.19,39.49 227.73,37.99 227.00,36.59 C225.26,33.26 222.15,30.65 218.32,29.57 C214.18,28.42 210.67,29.32 206.92,30.97 C206.13,31.32 205.40,31.76 204.69,32.23 C203.56,32.58 202.44,32.96 201.28,33.53 C197.32,35.48 193.56,40.24 192.71,44.34 C192.02,47.64 191.95,49.41 192.38,52.74 C192.57,54.16 193.82,57.28 194.14,57.92 C196.30,62.25 199.90,64.56 204.43,66.35 C205.73,66.86 207.09,67.43 208.45,67.76 C212.27,68.70 214.62,68.56 218.40,68.03 C222.80,67.41 226.87,64.07 229.13,60.67 C231.86,56.59 232.46,51.77 231.50,47.12 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M228.54,49.12 C228.54,58.28 221.11,65.70 211.96,65.70 C202.80,65.70 195.38,58.28 195.38,49.12 C195.38,39.97 202.80,32.54 211.96,32.54 C221.11,32.54 228.54,39.97 228.54,49.12 Z "
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 4.99
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 228.56
					startY: 49.09
					endX: 195.31
					endY: 49.09
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 4.55
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 212.03
					startY: 32.44
					endX: 212.03
					endY: 65.74
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M215.52,49.06 C215.52,50.99 213.95,52.56 212.02,52.56 C210.08,52.56 208.52,50.99 208.52,49.06 C208.52,47.13 210.08,45.56 212.02,45.56 C213.95,45.56 215.52,47.13 215.52,49.06 Z "
},
]
}
}
}
