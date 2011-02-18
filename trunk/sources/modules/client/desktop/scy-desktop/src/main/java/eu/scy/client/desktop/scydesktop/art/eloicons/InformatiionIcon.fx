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

public override function clone(): InformatiionIcon {
InformatiionIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

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
					stroke: null
					content: "M210.44,54.19 C210.41,53.47 210.40,53.05 210.40,52.94 C210.40,50.40 211.44,48.03 213.49,45.88 C214.93,44.35 215.84,43.23 216.25,42.46 C216.68,41.66 216.90,40.93 216.90,40.28 C216.90,38.70 216.13,36.81 212.42,36.81 C210.44,36.81 208.63,37.25 207.01,38.14 L205.43,33.55 C207.98,32.31 210.95,31.68 214.28,31.68 C217.48,31.68 220.01,32.38 221.80,33.76 C223.58,35.13 224.44,36.86 224.44,39.06 C224.44,40.16 224.12,41.34 223.49,42.57 C222.86,43.83 221.63,45.37 219.83,47.16 C217.85,49.08 216.85,51.32 216.85,53.83 L216.85,54.19 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M214.28,32.17 C217.37,32.17 219.80,32.83 221.50,34.15 C223.15,35.42 223.95,37.03 223.95,39.06 C223.95,40.08 223.65,41.19 223.05,42.35 C222.44,43.56 221.24,45.06 219.49,46.80 C217.44,48.79 216.39,51.11 216.36,53.70 L210.91,53.70 C210.90,53.28 210.89,53.02 210.89,52.94 C210.89,50.50 211.86,48.30 213.85,46.21 C215.32,44.66 216.25,43.50 216.69,42.69 C217.16,41.81 217.39,41.02 217.39,40.28 C217.39,38.80 216.75,36.31 212.42,36.31 C210.57,36.31 208.85,36.69 207.29,37.44 L206.04,33.81 C208.43,32.72 211.20,32.17 214.28,32.17 M214.28,31.19 C210.69,31.19 207.54,31.89 204.83,33.31 L206.74,38.86 C208.41,37.82 210.31,37.30 212.42,37.30 C215.08,37.30 216.41,38.29 216.41,40.28 C216.41,40.85 216.21,41.50 215.82,42.23 C215.43,42.95 214.53,44.06 213.13,45.54 C210.98,47.80 209.91,50.26 209.91,52.94 C209.91,53.08 209.93,53.66 209.96,54.68 L217.34,54.68 L217.34,53.83 C217.34,51.45 218.28,49.34 220.18,47.51 C222.02,45.67 223.27,44.10 223.93,42.79 C224.60,41.49 224.93,40.24 224.93,39.06 C224.93,36.73 223.99,34.83 222.10,33.37 C220.22,31.91 217.61,31.19 214.28,31.19 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M210.52,54.26 C210.49,53.54 210.48,53.12 210.48,53.01 C210.48,50.48 211.52,48.10 213.57,45.95 C215.02,44.42 215.92,43.30 216.33,42.53 C216.76,41.73 216.98,41.00 216.98,40.35 C216.98,38.77 216.20,36.88 212.50,36.88 C210.52,36.88 208.71,37.33 207.09,38.21 L205.51,33.62 C208.06,32.38 211.03,31.75 214.36,31.75 C217.56,31.75 220.09,32.45 221.88,33.83 C223.66,35.20 224.52,36.94 224.52,39.14 C224.52,40.23 224.20,41.42 223.57,42.64 C222.94,43.90 221.70,45.44 219.91,47.23 C217.93,49.14 216.93,51.39 216.93,53.90 L216.93,54.26 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M214.36,32.24 C217.45,32.24 219.88,32.91 221.58,34.22 C223.23,35.49 224.03,37.10 224.03,39.14 C224.03,40.16 223.73,41.26 223.13,42.42 C222.52,43.63 221.32,45.13 219.57,46.87 C217.52,48.85 216.47,51.18 216.44,53.77 L210.99,53.77 C210.98,53.35 210.97,53.09 210.97,53.01 C210.97,50.57 211.94,48.37 213.93,46.28 C215.40,44.72 216.33,43.57 216.77,42.77 C217.24,41.88 217.47,41.09 217.47,40.35 C217.47,38.87 216.83,36.39 212.50,36.39 C210.65,36.39 208.93,36.76 207.37,37.51 L206.12,33.88 C208.51,32.79 211.27,32.24 214.36,32.24 M214.36,31.26 C210.77,31.26 207.62,31.96 204.91,33.38 L206.82,38.94 C208.49,37.89 210.39,37.37 212.50,37.37 C215.16,37.37 216.49,38.36 216.49,40.35 C216.49,40.93 216.29,41.57 215.90,42.30 C215.51,43.03 214.61,44.13 213.21,45.61 C211.06,47.87 209.99,50.33 209.99,53.01 C209.99,53.15 210.01,53.73 210.04,54.76 L217.42,54.76 L217.42,53.90 C217.42,51.52 218.36,49.41 220.26,47.58 C222.10,45.75 223.35,44.17 224.01,42.87 C224.68,41.56 225.01,40.32 225.01,39.14 C225.01,36.80 224.07,34.90 222.18,33.44 C220.30,31.99 217.69,31.26 214.36,31.26 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M214.05,66.30 C212.72,66.30 211.66,65.92 210.81,65.12 C209.98,64.33 209.57,63.36 209.57,62.14 C209.57,60.91 209.98,59.93 210.81,59.15 C211.65,58.37 212.71,57.99 214.05,57.99 C215.40,57.99 216.46,58.37 217.30,59.15 C218.13,59.93 218.53,60.91 218.53,62.14 C218.53,63.38 218.13,64.35 217.31,65.13 C216.48,65.92 215.42,66.30 214.05,66.30 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M214.05,58.48 C215.27,58.48 216.22,58.82 216.96,59.51 C217.69,60.19 218.04,61.05 218.04,62.14 C218.04,63.23 217.69,64.09 216.97,64.78 C216.24,65.47 215.28,65.81 214.05,65.81 C212.85,65.81 211.90,65.47 211.15,64.76 C210.42,64.07 210.07,63.22 210.07,62.14 C210.07,61.05 210.42,60.19 211.15,59.51 C211.89,58.82 212.84,58.48 214.05,58.48 M214.05,57.50 C212.60,57.50 211.40,57.93 210.48,58.79 C209.55,59.66 209.08,60.78 209.08,62.14 C209.08,63.49 209.55,64.60 210.48,65.48 C211.40,66.35 212.60,66.79 214.05,66.79 C215.53,66.79 216.73,66.36 217.65,65.49 C218.56,64.62 219.03,63.51 219.03,62.14 C219.03,60.78 218.56,59.66 217.63,58.79 C216.70,57.93 215.51,57.50 214.05,57.50 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M214.07,66.19 C212.74,66.19 211.68,65.80 210.83,65.01 C210.00,64.22 209.59,63.24 209.59,62.03 C209.59,60.80 210.00,59.82 210.83,59.04 C211.67,58.26 212.73,57.88 214.07,57.88 C215.42,57.88 216.48,58.26 217.31,59.04 C218.15,59.82 218.55,60.80 218.55,62.03 C218.55,63.26 218.15,64.24 217.33,65.02 C216.49,65.81 215.43,66.19 214.07,66.19 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M214.07,58.37 C215.29,58.37 216.24,58.71 216.98,59.40 C217.71,60.08 218.06,60.94 218.06,62.03 C218.06,63.12 217.71,63.98 216.99,64.66 C216.25,65.36 215.30,65.70 214.07,65.70 C212.87,65.70 211.92,65.36 211.17,64.65 C210.44,63.96 210.08,63.10 210.08,62.03 C210.08,60.94 210.44,60.08 211.17,59.40 C211.91,58.71 212.86,58.37 214.07,58.37 M214.07,57.38 C212.61,57.38 211.42,57.81 210.49,58.68 C209.56,59.55 209.10,60.67 209.10,62.03 C209.10,63.38 209.56,64.49 210.49,65.37 C211.42,66.24 212.61,66.68 214.07,66.68 C215.55,66.68 216.74,66.25 217.66,65.38 C218.58,64.51 219.04,63.39 219.04,62.03 C219.04,60.67 218.58,59.55 217.65,58.68 C216.72,57.81 215.53,57.38 214.07,57.38 Z "
},
]
}
}
}
