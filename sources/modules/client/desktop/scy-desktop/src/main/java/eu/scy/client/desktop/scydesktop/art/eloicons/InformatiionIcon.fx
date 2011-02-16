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
public class InformatiionIcon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: bind windowColorScheme.secondColorLight
					strokeWidth: 1.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M215.46,30.75 C206.56,27.98 195.76,32.44 193.17,41.92 C191.97,46.30 192.41,51.48 194.25,55.58 C196.25,60.04 201.29,61.77 205.49,63.12 C208.14,63.97 209.61,59.83 206.95,58.97 C203.64,57.91 200.15,56.95 198.17,53.75 C196.51,51.05 196.81,47.30 197.10,44.30 C197.97,35.34 208.45,33.17 215.41,35.33 C222.76,37.62 227.01,42.79 227.20,50.61 C227.28,54.06 226.80,57.52 226.27,60.92 C226.08,61.68 225.89,62.44 225.70,63.21 C225.40,64.41 225.30,64.40 225.40,63.18 C224.57,62.35 223.74,61.64 222.83,60.91 C220.65,59.14 218.21,62.78 220.38,64.54 C222.61,66.33 225.58,70.50 228.42,67.38 C231.45,64.03 231.27,56.80 231.47,52.64 C232.01,41.23 225.97,34.01 215.46,30.75 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.06
					strokeLineCap: StrokeLineCap.BUTT
					content: "M219.91,59.19 L226.30,63.32 L225.62,53.78 C227.45,52.00 228.64,49.78 228.93,47.30 C229.75,40.33 223.10,33.90 214.08,32.95 C205.07,31.99 197.09,36.87 196.28,43.85 C195.50,50.42 201.38,56.52 209.62,57.98 "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M217.27,54.97 L209.77,54.97 C209.74,53.92 209.72,53.33 209.72,53.19 C209.72,50.47 210.81,47.96 213.00,45.67 C214.42,44.16 215.33,43.04 215.73,42.30 C216.13,41.57 216.33,40.91 216.33,40.32 C216.33,38.30 214.97,37.29 212.27,37.29 C210.12,37.29 208.20,37.82 206.50,38.88 L204.55,33.23 C207.31,31.79 210.51,31.08 214.16,31.08 C217.55,31.08 220.20,31.82 222.11,33.30 C224.03,34.78 224.99,36.71 224.99,39.09 C224.99,40.29 224.65,41.55 223.97,42.88 C223.30,44.21 222.03,45.81 220.16,47.67 C218.23,49.54 217.27,51.68 217.27,54.09 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.25
					strokeLineCap: StrokeLineCap.BUTT
					content: "M217.35,55.04 L209.85,55.04 C209.82,53.99 209.80,53.40 209.80,53.27 C209.80,50.54 210.89,48.04 213.08,45.74 C214.50,44.23 215.41,43.11 215.81,42.38 C216.21,41.64 216.41,40.98 216.41,40.40 C216.41,38.37 215.05,37.36 212.35,37.36 C210.20,37.36 208.28,37.90 206.58,38.96 L204.63,33.31 C207.39,31.87 210.59,31.15 214.24,31.15 C217.63,31.15 220.28,31.89 222.19,33.37 C224.11,34.85 225.07,36.79 225.07,39.16 C225.07,40.36 224.73,41.62 224.05,42.95 C223.38,44.28 222.11,45.88 220.24,47.74 C218.31,49.61 217.35,51.75 217.35,54.17 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M218.99,62.55 C218.99,63.93 218.52,65.07 217.58,65.95 C216.65,66.84 215.43,67.28 213.93,67.28 C212.45,67.28 211.24,66.83 210.29,65.94 C209.35,65.05 208.88,63.92 208.88,62.55 C208.88,61.16 209.35,60.03 210.29,59.14 C211.24,58.26 212.45,57.83 213.93,57.83 C215.41,57.83 216.62,58.26 217.57,59.14 C218.51,60.03 218.99,61.16 218.99,62.55 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M219.00,62.43 C219.00,63.82 218.53,64.96 217.60,65.84 C216.67,66.72 215.45,67.16 213.95,67.16 C212.47,67.16 211.26,66.71 210.31,65.83 C209.37,64.93 208.90,63.80 208.90,62.43 C208.90,61.05 209.37,59.91 210.31,59.03 C211.26,58.15 212.47,57.71 213.95,57.71 C215.43,57.71 216.64,58.15 217.59,59.03 C218.53,59.91 219.00,61.05 219.00,62.43 Z "
},
]
}
}
}
