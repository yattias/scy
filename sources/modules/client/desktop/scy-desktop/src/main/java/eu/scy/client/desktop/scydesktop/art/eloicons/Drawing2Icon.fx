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
public class Drawing2Icon extends AbstractEloIcon {

public function clone(): Drawing2Icon {
Drawing2Icon {
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
					content: "M201.60,34.92 C208.51,29.62 218.84,30.91 224.80,37.66 C230.56,44.17 235.90,53.74 227.43,60.04 C224.21,62.44 221.16,64.81 217.06,64.88 C217.73,66.30 217.48,67.97 215.61,68.68 C205.88,72.38 193.67,64.08 192.81,53.61 C192.22,46.34 196.01,39.21 201.60,34.92 Z M214.84,46.45 C215.80,47.17 216.33,48.35 215.81,49.81 C215.31,51.21 213.79,54.38 214.83,55.85 C216.36,58.04 220.32,56.18 221.62,54.88 C224.47,52.07 220.08,48.69 217.43,47.40 C216.82,47.10 215.86,46.71 214.84,46.45 Z M202.23,44.04 C200.11,47.35 198.10,52.30 200.27,56.26 C202.14,59.69 206.70,63.59 210.89,63.09 C209.19,62.03 207.70,60.59 206.56,59.00 C201.92,52.52 201.95,40.79 211.50,39.69 C213.81,39.43 216.25,39.83 218.57,40.70 C217.49,39.79 216.28,39.07 214.92,38.72 C209.61,37.39 205.03,39.65 202.23,44.04 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 22.0
					strokeLineJoin: StrokeLineJoin.BEVEL
					strokeMiterLimit: 4.0
					content: "M17.00,19.39 C17.00,19.73 16.83,20.00 16.62,20.00 L11.38,20.00 C11.17,20.00 11.00,19.73 11.00,19.39 L11.00,11.61 C11.00,11.27 11.17,11.00 11.38,11.00 L16.62,11.00 C16.83,11.00 17.00,11.27 17.00,11.61 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.01
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M196.92,61.67 Q198.67,54.29 199.05,53.98 Q199.89,53.11 200.78,53.45 C203.87,54.65 200.73,55.84 202.46,57.84 C203.12,58.60 203.84,59.19 206.48,60.45 Q207.72,61.04 206.61,61.40 L199.42,64.19 "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.67
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M226.73,41.69 L218.67,33.70 Q219.68,31.88 220.51,31.85 C221.33,31.82 228.21,39.13 228.56,39.84 Q228.92,40.55 226.73,41.69 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.35
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M199.86,61.31 Q200.73,62.07 200.68,63.56 L197.33,64.93 L197.24,64.95 L197.13,64.93 L197.03,64.95 L196.92,64.94 L196.81,64.92 L196.74,64.89 L196.66,64.81 L196.61,64.75 L196.48,64.60 L196.43,64.38 L196.43,64.14 L196.44,63.92 L197.49,60.12 Q198.99,60.07 199.86,61.31 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.99
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M208.29,60.33 C207.62,60.28 208.80,59.72 206.62,58.60 C204.44,57.48 202.95,57.69 203.60,55.13 C204.40,51.97 200.73,52.37 200.40,51.99 Q200.08,51.61 217.81,34.85 L225.62,42.61 Q208.96,60.37 208.29,60.33 Z "
				},
				Polygon {
					points: [217.70,34.15,218.24,33.72,226.54,42.01,226.16,42.50]
					fill: Color.WHITE
					stroke: null
},
]
}
}
}
