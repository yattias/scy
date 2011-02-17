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
public class Designed_artifactIcon extends AbstractEloIcon {

public function clone(): Designed_artifactIcon {
Designed_artifactIcon {
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
					opacity: 0.99
					content: "M222.19,29.86 C218.32,33.79 213.90,35.19 209.46,36.33 C204.77,37.53 199.56,36.10 195.06,38.38 C188.47,41.74 193.81,72.13 200.44,68.76 C204.93,66.48 210.13,67.85 214.84,66.71 C220.21,65.41 225.40,63.09 230.07,58.36 C236.09,52.26 228.16,23.82 222.19,29.86 Z "
				},
				Polygon {
					points: [192.63,31.69,192.48,41.77,231.29,63.55,231.51,54.08]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [193.47,58.96,193.56,64.62,197.20,67.55,216.63,55.56,216.63,45.71]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [207.91,52.32,208.03,60.28,216.83,54.98,225.50,60.28,225.33,52.16,216.81,47.16]
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.92
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [203.13,47.70,203.40,39.77,210.22,43.69]
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polyline {
					fill: bind windowColorScheme.thirdColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.19
					strokeLineCap: StrokeLineCap.BUTT
					points: [231.51,54.08]
				},
				Polygon {
					points: [231.51,54.08,231.53,47.59,199.17,29.69,192.60,29.61,192.63,32.00]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M224.46,52.16 C224.42,52.12 224.38,52.10 224.33,52.08 C224.16,51.69 223.80,51.51 223.37,51.58 C223.05,51.63 222.82,51.92 222.83,52.25 C222.84,52.44 222.90,52.65 222.99,52.85 C223.06,53.21 223.24,53.50 223.67,53.53 C223.74,53.56 223.82,53.57 223.90,53.57 C224.29,53.56 224.51,53.23 224.59,52.86 C224.64,52.60 224.67,52.35 224.46,52.16 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M224.45,56.90 C224.41,56.86 224.36,56.84 224.31,56.81 C224.12,56.39 223.74,56.20 223.27,56.27 C222.92,56.32 222.68,56.64 222.69,57.00 C222.70,57.19 222.76,57.42 222.87,57.64 C222.94,58.03 223.14,58.34 223.59,58.37 C223.67,58.40 223.76,58.42 223.85,58.42 C224.27,58.41 224.50,58.05 224.58,57.65 C224.64,57.38 224.67,57.10 224.45,56.90 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M210.75,52.47 C210.71,52.43 210.67,52.41 210.62,52.39 C210.44,52.00 210.09,51.82 209.65,51.88 C209.33,51.94 209.11,52.23 209.12,52.56 C209.12,52.74 209.18,52.96 209.28,53.16 C209.34,53.52 209.53,53.81 209.95,53.84 C210.03,53.87 210.11,53.88 210.19,53.88 C210.58,53.87 210.79,53.54 210.87,53.17 C210.93,52.91 210.96,52.66 210.75,52.47 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M210.89,56.90 C210.85,56.86 210.80,56.84 210.75,56.81 C210.56,56.39 210.18,56.20 209.71,56.27 C209.37,56.32 209.13,56.64 209.13,57.00 C209.14,57.19 209.21,57.42 209.31,57.64 C209.38,58.03 209.58,58.34 210.03,58.37 C210.12,58.40 210.20,58.42 210.29,58.42 C210.71,58.41 210.94,58.05 211.03,57.65 C211.09,57.38 211.12,57.10 210.89,56.90 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M215.69,54.29 C215.68,54.12 215.54,53.96 215.35,54.03 C215.17,54.01 215.01,54.18 215.03,54.34 C215.01,54.37 215.00,54.40 214.99,54.43 C214.99,54.43 214.99,54.44 214.99,54.44 C214.98,54.47 214.97,54.50 214.97,54.54 C214.97,54.54 214.97,54.55 214.96,54.55 C214.95,54.71 214.98,54.88 215.06,55.04 C215.12,55.17 215.29,55.25 215.42,55.18 C215.57,55.09 215.67,55.00 215.73,54.85 C215.82,54.67 215.79,54.46 215.69,54.29 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M215.81,48.88 C215.81,48.70 215.67,48.54 215.48,48.62 C215.30,48.60 215.14,48.77 215.16,48.93 C215.14,48.96 215.13,48.99 215.12,49.02 C215.12,49.02 215.12,49.03 215.12,49.03 C215.11,49.06 215.10,49.09 215.10,49.13 C215.10,49.13 215.09,49.14 215.09,49.14 C215.08,49.30 215.11,49.47 215.19,49.63 C215.24,49.76 215.42,49.84 215.55,49.77 C215.70,49.68 215.80,49.58 215.86,49.44 C215.95,49.26 215.92,49.05 215.81,48.88 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M221.65,53.38 C221.64,53.13 221.40,52.90 221.09,53.01 C220.78,52.97 220.51,53.22 220.54,53.45 C220.51,53.50 220.49,53.54 220.48,53.58 C220.47,53.59 220.47,53.60 220.47,53.60 C220.46,53.65 220.45,53.69 220.44,53.74 C220.44,53.75 220.43,53.75 220.43,53.76 C220.40,53.99 220.46,54.23 220.59,54.48 C220.69,54.65 220.98,54.78 221.20,54.67 C221.45,54.55 221.62,54.40 221.72,54.20 C221.88,53.93 221.83,53.62 221.65,53.38 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M213.34,53.18 C213.34,52.94 213.10,52.72 212.80,52.83 C212.50,52.79 212.23,53.03 212.26,53.26 C212.24,53.30 212.22,53.34 212.20,53.39 C212.20,53.39 212.20,53.40 212.20,53.40 C212.18,53.45 212.17,53.49 212.16,53.54 C212.16,53.54 212.16,53.55 212.16,53.56 C212.13,53.78 212.18,54.01 212.31,54.25 C212.41,54.42 212.69,54.54 212.90,54.44 C213.14,54.32 213.31,54.18 213.41,53.98 C213.57,53.72 213.52,53.42 213.34,53.18 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M205.48,45.42 C205.48,45.24 205.21,45.07 204.87,45.15 C204.53,45.12 204.23,45.31 204.26,45.47 C204.24,45.51 204.22,45.54 204.20,45.57 C204.20,45.58 204.19,45.58 204.19,45.58 C204.17,45.62 204.16,45.65 204.15,45.69 C204.15,45.69 204.15,45.70 204.15,45.70 C204.12,45.87 204.17,46.05 204.32,46.23 C204.43,46.36 204.74,46.45 204.98,46.37 C205.25,46.28 205.45,46.17 205.56,46.02 C205.73,45.83 205.68,45.60 205.48,45.42 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M205.35,41.72 C205.34,41.53 205.16,41.35 204.91,41.44 C204.67,41.41 204.46,41.60 204.48,41.78 C204.46,41.82 204.45,41.85 204.44,41.89 C204.44,41.89 204.43,41.89 204.43,41.90 C204.42,41.93 204.41,41.97 204.40,42.01 C204.40,42.01 204.40,42.02 204.40,42.03 C204.38,42.20 204.42,42.39 204.52,42.58 C204.60,42.72 204.83,42.81 205.00,42.73 C205.19,42.63 205.33,42.52 205.40,42.36 C205.53,42.16 205.49,41.92 205.35,41.72 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M218.39,49.06 C218.38,48.89 218.24,48.73 218.05,48.81 C217.87,48.78 217.71,48.96 217.73,49.11 C217.71,49.14 217.70,49.17 217.69,49.21 C217.69,49.21 217.69,49.21 217.69,49.22 C217.68,49.25 217.67,49.28 217.67,49.31 C217.67,49.32 217.67,49.32 217.67,49.33 C217.65,49.48 217.68,49.65 217.76,49.82 C217.82,49.95 217.99,50.03 218.12,49.95 C218.27,49.87 218.37,49.77 218.43,49.63 C218.53,49.44 218.50,49.23 218.39,49.06 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M218.27,54.16 C218.27,53.99 218.13,53.83 217.94,53.90 C217.76,53.88 217.60,54.05 217.62,54.21 C217.60,54.24 217.59,54.27 217.58,54.30 C217.58,54.31 217.58,54.31 217.58,54.31 C217.57,54.34 217.56,54.38 217.55,54.41 C217.55,54.41 217.55,54.42 217.55,54.42 C217.53,54.58 217.56,54.75 217.64,54.92 C217.70,55.04 217.88,55.12 218.00,55.05 C218.15,54.97 218.26,54.87 218.31,54.72 C218.41,54.54 218.38,54.33 218.27,54.16 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.19
					strokeLineCap: StrokeLineCap.BUTT
					startX: 216.76
					startY: 46.67
					endX: 216.82
					endY: 55.47
				},
				Polyline {
					fill: Color.rgb(0x9a,0x99,0x99)
					stroke: Color.rgb(0x47,0x46,0x46)
					strokeWidth: 0.39
					strokeLineCap: StrokeLineCap.BUTT
					points: [216.79,51.98]
				},
				Polygon {
					points: [218.13,53.40,216.74,52.92,215.35,53.37,215.30,52.10,216.74,51.57,218.10,52.04]
					fill: Color.rgb(0xf1,0xf1,0xef)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.39
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [216.76,49.53,215.22,51.35,218.14,51.37]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [193.48,53.24,211.85,43.00,216.61,45.57,193.47,58.96]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
},
]
}
}
}
