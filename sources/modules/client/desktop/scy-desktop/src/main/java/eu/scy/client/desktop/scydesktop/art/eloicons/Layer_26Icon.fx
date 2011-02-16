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
public class Layer_26Icon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 201.0
					y: 41.0
					width: 21.0
					height: 19.0
				},
				SVGPath {
					fill: Color.rgb(0xd3,0xe6,0xf6)
					stroke: null
					content: "M231.55,38.41 C231.44,36.01 229.83,33.77 227.23,33.74 C217.27,33.61 207.91,33.42 198.12,31.11 C193.87,30.11 191.29,34.79 192.75,37.71 C191.81,39.23 191.62,41.24 192.97,43.04 C195.52,46.43 195.49,50.86 195.04,54.85 C194.71,57.81 193.35,60.63 193.17,63.54 C193.00,66.21 195.12,67.82 197.49,68.21 C200.91,68.78 205.24,67.20 208.73,66.97 C214.27,66.60 219.85,67.72 225.24,68.88 C227.87,69.46 231.17,67.88 231.06,64.76 C230.76,56.00 231.95,47.13 231.55,38.41 Z M204.71,58.33 C204.34,58.36 203.95,58.43 203.54,58.50 C204.00,55.61 204.35,52.77 204.34,49.83 C204.32,46.88 203.43,43.79 202.07,41.01 C208.91,42.11 215.67,42.50 222.62,42.66 C222.58,48.13 222.16,53.63 222.05,59.11 C218.83,58.53 215.58,58.09 212.32,57.92 C209.79,57.78 207.23,58.10 204.71,58.33 Z "
				},
				Line {
					fill: Color.rgb(0xff,0x0,0x97)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.75
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 211.73
					startY: 33.61
					endX: 211.73
					endY: 63.01
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.75
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
					startX: 195.99
					startY: 33.06
					endX: 227.48
					endY: 45.59
				},
				Line {
					fill: Color.rgb(0xff,0x0,0x97)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.33
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 200.19
					startY: 39.95
					endX: 200.19
					endY: 34.53
				},
				Line {
					fill: Color.rgb(0xff,0x0,0x97)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.35
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 223.5
					startY: 51.17
					endX: 223.5
					endY: 44.83
				},
				Polygon {
					points: [205.64,44.83,194.91,44.83,200.32,40.08]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.14
					strokeLineCap: StrokeLineCap.BUTT
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
				},
				Polygon {
					points: [228.90,55.14,218.17,55.14,223.58,50.39]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.14
					strokeLineCap: StrokeLineCap.BUTT
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
				},
				Polygon {
					points: [218.50,64.98,204.73,64.98,211.67,60.23]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.43
					strokeLineCap: StrokeLineCap.BUTT
					strokeLineJoin: StrokeLineJoin.ROUND
					strokeMiterLimit: 4.0
},
]
}
}
}
