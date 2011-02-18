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
public class AssignmentIcon extends AbstractEloIcon {

public override function clone(): AssignmentIcon {
AssignmentIcon {
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
					content: "M225.02,47.10 C226.69,43.47 226.59,38.97 225.18,35.31 C221.67,26.17 210.06,29.89 203.31,29.88 C201.76,29.88 200.87,30.79 200.53,31.98 C195.66,34.04 192.46,40.51 194.77,45.88 C195.70,48.03 198.20,49.12 200.29,48.87 C201.92,48.68 203.43,48.16 204.97,47.56 C205.41,47.39 206.52,46.66 207.60,46.03 C207.23,47.40 206.71,48.76 206.38,49.99 C205.70,52.53 205.49,54.86 205.51,57.48 C205.53,60.12 205.99,66.04 208.09,67.86 C210.39,69.85 214.64,69.01 217.07,67.68 C219.95,66.09 222.01,62.25 221.38,58.56 C221.09,56.83 219.91,54.77 220.98,53.14 C222.37,51.03 223.93,49.47 225.02,47.10 Z M215.49,48.40 C213.73,50.45 212.86,53.78 213.60,56.50 C214.05,58.13 213.46,51.38 214.52,46.71 C215.10,44.15 215.77,40.49 213.53,38.55 C211.32,36.63 208.75,37.56 206.41,38.51 C205.02,39.08 204.86,38.07 205.20,37.43 C208.85,37.30 212.49,36.52 216.12,36.86 C222.66,37.48 217.60,45.94 215.49,48.40 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M214.44,54.70 L207.43,54.70 C207.40,53.72 207.38,53.17 207.38,53.04 C207.38,50.50 208.40,48.16 210.45,46.02 C211.77,44.61 212.63,43.57 213.00,42.88 C213.37,42.19 213.56,41.57 213.56,41.03 C213.56,39.15 212.29,38.20 209.77,38.20 C207.76,38.20 205.97,38.70 204.38,39.69 L202.57,34.41 C205.14,33.07 208.13,32.40 211.53,32.40 C214.69,32.40 217.17,33.09 218.95,34.47 C220.75,35.85 221.64,37.66 221.64,39.88 C221.64,41.00 221.32,42.17 220.69,43.42 C220.06,44.65 218.87,46.15 217.13,47.89 C215.33,49.63 214.44,51.63 214.44,53.89 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.17
					strokeLineCap: StrokeLineCap.BUTT
					content: "M214.51,54.77 L207.51,54.77 C207.48,53.79 207.46,53.24 207.46,53.11 C207.46,50.57 208.48,48.23 210.52,46.09 C211.85,44.68 212.70,43.63 213.07,42.95 C213.44,42.26 213.63,41.64 213.63,41.10 C213.63,39.21 212.37,38.27 209.84,38.27 C207.84,38.27 206.04,38.76 204.45,39.75 L202.64,34.48 C205.21,33.14 208.20,32.47 211.61,32.47 C214.77,32.47 217.25,33.16 219.03,34.54 C220.82,35.92 221.72,37.73 221.72,39.94 C221.72,41.06 221.40,42.24 220.76,43.48 C220.14,44.72 218.95,46.21 217.21,47.96 C215.40,49.70 214.51,51.70 214.51,53.95 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M216.03,61.78 C216.03,63.07 215.60,64.13 214.73,64.95 C213.85,65.78 212.72,66.19 211.32,66.19 C209.94,66.19 208.80,65.77 207.92,64.94 C207.04,64.11 206.60,63.05 206.60,61.78 C206.60,60.48 207.04,59.42 207.92,58.60 C208.80,57.78 209.94,57.37 211.32,57.37 C212.70,57.37 213.83,57.78 214.71,58.60 C215.59,59.42 216.03,60.48 216.03,61.78 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.93
					strokeLineCap: StrokeLineCap.BUTT
					content: "M216.05,61.67 C216.05,62.96 215.62,64.02 214.75,64.85 C213.87,65.67 212.74,66.08 211.34,66.08 C209.95,66.08 208.82,65.66 207.94,64.83 C207.06,64.00 206.62,62.95 206.62,61.67 C206.62,60.37 207.06,59.32 207.94,58.49 C208.82,57.67 209.95,57.26 211.34,57.26 C212.72,57.26 213.85,57.67 214.73,58.49 C215.61,59.32 216.05,60.37 216.05,61.67 Z "
},
]
}
}
}
