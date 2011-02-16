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
public class ModelIcon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: null
					content: "M231.50,56.99 C231.31,56.02 230.68,55.39 229.89,55.05 C230.81,49.02 232.10,43.57 228.58,37.73 C225.38,32.42 219.30,30.13 212.55,31.98 C206.91,33.53 201.41,37.15 200.27,42.34 C199.53,45.68 198.91,47.45 195.74,49.63 C193.22,51.36 192.35,53.91 192.04,56.53 C191.69,59.48 193.36,66.67 198.87,64.14 C201.51,62.92 203.66,61.03 205.87,59.31 C208.61,57.17 208.56,62.88 210.08,64.23 C211.99,65.94 214.13,66.82 216.88,67.22 C219.59,67.63 223.54,68.34 226.29,67.82 C232.78,66.58 232.41,61.55 231.50,56.99 Z M224.95,62.28 C223.00,62.34 220.80,62.02 218.86,61.83 C213.76,61.34 214.96,57.00 212.34,54.22 C210.34,52.09 206.81,52.53 204.28,53.47 C203.02,53.94 201.86,54.81 200.86,55.59 C200.11,56.07 199.41,56.59 198.73,57.14 C198.77,55.65 199.20,54.48 200.91,53.31 C202.51,52.21 203.87,51.16 205.07,49.73 C207.52,46.82 205.78,42.94 208.97,40.09 C210.30,38.91 212.04,38.19 213.76,37.52 C218.22,35.77 221.36,38.09 223.29,41.30 C226.11,45.96 223.59,51.79 222.99,56.67 C222.80,58.26 223.97,59.24 225.38,59.54 C225.66,60.78 225.84,62.25 224.95,62.28 Z "
				},
				Polyline {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: null
					points: [197.51,51.95]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M226.91,43.75 C226.91,48.97 222.68,53.19 217.46,53.19 C212.25,53.19 208.02,48.97 208.02,43.75 C208.02,38.54 212.25,34.31 217.46,34.31 C222.68,34.31 226.91,38.54 226.91,43.75 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M207.26,43.01 C207.26,37.43 211.78,32.91 217.36,32.91 C222.94,32.91 227.46,37.43 227.46,43.01 "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M227.29,41.55 C227.29,47.99 222.84,53.20 217.36,53.20 C211.88,53.20 207.43,47.99 207.43,41.55 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.13
					content: "M223.64,54.05 C225.04,58.00 226.30,61.75 226.30,62.52 C226.30,64.31 225.27,64.98 223.60,64.98 C221.88,64.98 213.16,64.98 212.04,64.93 C211.03,64.89 209.19,64.15 209.19,62.56 C209.19,60.78 210.69,56.21 211.44,54.04 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					content: "M223.37,53.11 C221.80,48.46 220.06,43.52 219.91,42.74 Q219.64,41.30 219.91,38.00 L214.99,38.00 Q215.37,41.74 215.13,42.88 Q214.86,44.13 212.07,51.93 Q211.92,52.39 211.68,53.10 Z "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.88
					strokeLineCap: StrokeLineCap.BUTT
					content: "M197.10,51.79 L207.76,45.20 C207.54,44.37 207.42,43.50 207.42,42.60 C207.42,37.12 211.86,32.67 217.35,32.67 C222.83,32.67 227.27,37.12 227.27,42.60 C227.27,48.08 222.83,52.53 217.35,52.53 C214.14,52.53 211.30,51.01 209.48,48.66 L199.02,54.77 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M209.04,45.33 Q197.44,51.45 197.60,52.08 C197.66,52.32 198.89,54.32 199.18,54.41 C199.18,54.41 199.19,54.41 199.20,54.41 Q199.98,54.41 210.21,47.74 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M198.63,64.56 C196.81,64.56 195.65,62.75 196.25,60.76 C196.45,60.12 197.07,59.56 197.44,58.97 Q198.13,57.89 198.63,56.96 Q199.24,57.99 199.89,58.97 C200.31,59.61 200.78,60.06 200.99,60.76 C201.59,62.76 200.84,64.56 198.63,64.56 Z "
},
]
}
}
}
