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
public class Idea2Icon extends AbstractEloIcon {

public function clone(): Idea2Icon {
Idea2Icon {
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
					content: "M229.99,37.68 C227.68,33.51 223.81,31.14 219.11,30.37 C218.75,30.17 218.34,30.00 217.88,29.86 C211.20,27.91 206.30,28.89 200.40,32.05 C196.05,34.38 191.22,39.16 192.22,43.93 C192.69,46.19 193.44,48.20 194.85,50.17 C195.63,51.26 200.86,56.32 200.16,57.53 C197.60,61.95 202.49,65.60 206.40,67.59 C210.78,69.83 216.66,69.64 219.68,65.86 C220.92,64.31 221.41,62.51 221.93,60.71 C222.84,57.54 226.53,55.37 228.75,52.90 C232.81,48.39 232.74,42.64 229.99,37.68 Z M215.29,52.06 C213.52,53.79 212.63,54.62 211.94,56.81 C211.84,57.11 211.97,56.63 211.88,56.94 C212.85,54.68 211.04,56.30 210.94,56.31 C212.17,52.41 208.92,51.37 206.35,48.35 C204.69,46.40 201.78,43.81 203.91,41.45 C205.51,39.69 208.23,38.18 210.93,37.99 C211.91,38.86 213.31,39.40 215.11,39.28 C219.00,39.04 221.45,41.73 221.30,45.05 C221.18,47.74 217.19,50.21 215.29,52.06 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.17
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M212.26,32.40 C205.65,32.40 200.38,37.35 200.38,43.45 Q200.33,47.96 206.47,52.92 Q207.86,54.24 207.79,56.65 L216.64,56.65 Q216.57,54.24 217.96,52.92 Q224.10,47.96 224.05,43.45 C224.05,37.35 218.78,32.40 212.17,32.40 "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 208.33
					startY: 58.4
					endX: 216.31
					endY: 58.4
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 208.33
					startY: 60.11
					endX: 216.31
					endY: 60.11
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 208.33
					startY: 61.94
					endX: 216.31
					endY: 61.94
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.59
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					content: "M216.38,63.79 Q216.38,64.05 216.21,64.53 Q214.10,66.43 212.20,66.43 Q210.20,66.43 208.29,64.52 Q208.02,63.98 208.02,63.79 Z "
				},
				Polyline {
					fill: null
					stroke: Color.rgb(0xff,0x5f,0xd7)
					strokeLineCap: StrokeLineCap.BUTT
					points: [218.21,35.71]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M209.46,54.95 Q209.62,53.54 209.06,53.07 C205.10,49.73 203.44,46.54 203.44,43.78 C203.44,39.45 205.71,34.14 213.41,34.14 C215.69,34.14 217.79,34.93 219.43,36.27 C219.54,36.36 219.65,36.45 219.75,36.54 Q211.88,32.76 207.33,38.75 C201.85,45.95 210.31,53.20 210.33,53.23 Q210.79,53.78 210.63,54.91 Z "
},
]
}
}
}
