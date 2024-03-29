package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
/**
 * @author lars
 */
public class Debate_argumentIcon extends AbstractEloIcon {

public override function clone(): Debate_argumentIcon {
Debate_argumentIcon {
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
					opacity: 0.62
					content: "M40.51,13.71 C40.00,10.79 38.23,7.30 35.72,5.57 C35.33,4.99 34.78,4.50 34.01,4.19 C26.24,0.99 15.35,0.80 8.19,5.91 C0.35,11.51 -2.33,24.00 4.79,31.33 C11.48,38.22 22.26,43.14 31.12,38.20 C37.88,34.43 41.87,21.44 40.51,13.71 Z M31.43,12.74 C32.12,12.57 32.56,13.29 32.83,14.28 C32.18,13.62 31.28,13.27 30.20,13.15 C30.60,12.99 31.01,12.85 31.43,12.74 Z M9.34,16.78 C11.41,12.53 15.63,10.69 20.14,10.30 C17.67,12.47 15.51,15.12 14.24,18.25 C12.10,19.35 10.41,20.31 9.64,20.73 C9.12,21.02 8.71,21.38 8.39,21.78 C8.15,20.35 8.39,18.72 9.34,16.78 Z M28.06,30.46 C24.67,33.94 17.32,30.94 14.14,28.68 C13.85,28.47 13.57,28.27 13.29,28.06 C13.66,27.86 14.03,27.67 14.39,27.48 C15.14,28.90 16.25,30.06 17.76,30.64 C21.71,32.18 25.35,28.51 28.18,26.07 C29.41,25.01 31.04,23.70 32.25,22.17 C31.33,25.25 30.03,28.45 28.06,30.46 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M29.41,30.91 C29.41,31.67 28.91,32.28 28.30,32.28 C27.68,32.28 27.18,31.67 27.18,30.91 C27.18,30.16 27.68,29.54 28.30,29.54 C28.91,29.54 29.41,30.16 29.41,30.91 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M19.66,32.93 C19.66,33.80 18.62,34.50 17.33,34.50 C16.04,34.50 15.00,33.80 15.00,32.93 C15.00,32.07 16.04,31.36 17.33,31.36 C18.62,31.36 19.66,32.07 19.66,32.93 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					x: 28.42
					y: 11.14
					width: 10.0
					height: 7.0
				},
				Rectangle {
					fill: bind windowColorScheme.secondColor
					stroke: null
					x: 3.42
					y: 11.14
					width: 15.0
					height: 8.0
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.06
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M17.78,9.73 C17.66,9.29 16.87,9.41 17.00,9.85 C17.47,11.58 20.43,19.06 16.51,18.76 C13.06,18.49 9.64,18.82 6.19,18.62 C4.58,18.53 4.76,15.46 4.79,14.66 C4.83,13.72 4.14,10.55 5.64,10.57 C9.17,10.62 12.68,10.47 16.21,10.45 C16.72,10.44 16.80,9.74 16.28,9.74 C13.45,9.76 10.62,9.95 7.78,9.95 C6.72,9.95 4.51,9.39 3.76,10.36 C3.54,10.64 3.72,11.09 3.80,11.37 C4.19,12.70 3.88,14.02 3.98,15.35 C4.04,16.23 3.36,18.55 4.83,19.03 C6.62,19.61 8.78,19.41 10.66,19.35 C13.20,19.26 15.97,19.81 18.48,19.46 C21.02,19.11 18.07,10.78 17.78,9.73 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M38.45,9.73 C38.35,9.29 37.74,9.41 37.83,9.85 C38.21,11.58 40.53,19.06 37.45,18.76 C34.74,18.49 32.06,18.82 29.35,18.62 C28.09,18.53 28.23,15.46 28.25,14.66 C28.28,13.72 27.75,10.55 28.92,10.57 C31.69,10.62 34.44,10.47 37.22,10.45 C37.62,10.44 37.68,9.74 37.27,9.74 C35.05,9.76 32.83,9.95 30.60,9.95 C29.77,9.95 28.04,9.39 27.44,10.36 C27.28,10.64 27.41,11.09 27.48,11.37 C27.79,12.70 27.54,14.02 27.62,15.35 C27.67,16.23 27.13,18.55 28.28,19.03 C29.69,19.61 31.39,19.41 32.86,19.35 C34.86,19.26 37.03,19.81 39.00,19.46 C40.99,19.11 38.67,10.78 38.45,9.73 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M18.61,30.01 C18.56,29.99 18.51,30.00 18.47,30.01 C18.01,28.56 17.16,27.11 16.61,25.81 C15.81,23.90 14.82,21.78 13.48,20.19 C13.02,19.64 12.25,20.13 12.30,20.69 L12.45,21.05 C13.57,22.39 14.38,24.16 15.10,25.75 C15.74,27.16 16.92,29.02 17.32,30.68 C16.81,30.72 16.34,30.87 15.97,31.28 C14.90,32.50 16.52,34.50 17.59,35.03 C18.92,35.68 20.49,34.78 20.99,33.46 C21.60,31.82 19.90,30.58 18.61,30.01 Z M20.53,33.30 C20.39,33.87 19.88,34.28 19.39,34.55 C18.66,34.96 18.01,34.77 17.36,34.31 C16.40,33.63 15.38,31.50 17.14,31.18 C17.22,31.16 17.32,31.17 17.41,31.16 C17.43,31.31 17.46,31.46 17.46,31.60 C17.49,32.46 18.82,32.58 18.80,31.72 C18.79,31.56 18.76,31.39 18.74,31.23 C18.97,31.26 19.20,31.29 19.42,31.29 C19.53,31.29 19.59,31.23 19.63,31.15 C20.30,31.67 20.78,32.38 20.53,33.30 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M7.91,32.87 C7.91,33.83 6.91,34.60 5.68,34.60 C4.45,34.60 3.45,33.83 3.45,32.87 C3.45,31.92 4.45,31.15 5.68,31.15 C6.91,31.15 7.91,31.92 7.91,32.87 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M10.64,20.37 C9.30,21.96 8.31,24.08 7.50,25.99 C6.97,27.25 6.15,28.66 5.68,30.08 C4.72,29.95 3.76,30.16 3.22,31.10 C2.55,32.27 3.05,33.79 4.02,34.59 C5.07,35.46 6.88,35.80 7.86,34.65 C8.42,33.99 8.85,33.49 8.80,32.59 C8.75,31.76 8.13,31.18 7.58,30.64 C7.45,30.51 7.29,30.48 7.15,30.50 C7.07,30.46 7.00,30.43 6.92,30.40 C7.40,28.86 8.44,27.21 9.02,25.93 C9.74,24.34 10.54,22.57 11.67,21.23 L11.81,20.87 C11.86,20.31 11.10,19.82 10.64,20.37 Z M7.20,33.91 C6.47,34.78 5.35,34.35 4.58,33.79 C3.91,33.30 3.71,32.21 4.12,31.50 C4.39,31.03 4.90,30.94 5.43,31.00 C5.37,31.30 5.33,31.60 5.32,31.90 C5.29,32.76 6.63,32.63 6.66,31.78 C6.66,31.64 6.69,31.50 6.71,31.36 C6.83,31.42 6.94,31.47 7.04,31.51 C7.05,31.52 7.07,31.52 7.09,31.53 C7.31,31.74 7.53,31.97 7.69,32.23 C8.09,32.87 7.60,33.45 7.20,33.91 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M32.80,20.58 C31.67,22.85 31.15,23.77 29.98,26.03 C29.62,26.73 29.31,27.44 29.02,28.17 C28.75,28.84 28.62,30.31 28.07,30.82 C27.67,31.20 28.22,31.86 28.62,31.48 C29.62,30.54 29.84,28.39 30.37,27.14 C31.51,24.50 32.65,23.32 33.92,20.77 C34.17,20.27 33.04,20.09 32.80,20.58 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M34.84,19.44 C35.89,21.75 37.25,23.94 38.35,26.24 C38.69,26.96 38.97,27.67 39.24,28.41 C39.48,29.09 39.57,30.56 40.09,31.09 C40.48,31.48 39.91,32.13 39.52,31.73 C38.56,30.76 38.41,28.61 37.92,27.34 C36.87,24.66 35.20,22.30 34.02,19.71 C33.79,19.20 34.61,18.94 34.84,19.44 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M5.98,22.77 C5.57,22.67 5.16,22.64 4.74,22.64 C4.72,22.17 4.70,21.71 4.69,21.25 C4.69,21.13 4.51,21.14 4.51,21.25 C4.52,21.72 4.55,22.18 4.57,22.64 C4.24,22.64 3.90,22.66 3.57,22.66 C3.17,22.66 3.21,23.29 3.61,23.29 C3.94,23.28 4.27,23.27 4.61,23.27 C4.61,23.35 4.62,23.44 4.62,23.53 C4.63,24.06 4.85,24.75 4.62,25.26 C4.58,25.37 4.73,25.45 4.78,25.35 C4.98,24.91 4.87,24.36 4.83,23.90 C4.81,23.69 4.80,23.47 4.78,23.27 C5.14,23.27 5.50,23.29 5.85,23.38 C6.25,23.48 6.37,22.87 5.98,22.77 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.52,22.23 L17.48,22.23 C16.99,22.23 16.95,23.00 17.45,23.00 L20.48,23.00 C20.98,23.00 21.02,22.23 20.52,22.23 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.25,6.08 C20.65,5.72 21.04,5.35 21.43,4.97 C22.17,4.23 20.88,3.22 20.15,3.96 C19.63,4.47 19.12,5.04 18.51,5.44 C18.00,5.78 17.58,6.20 17.65,6.85 C17.72,7.50 18.45,7.80 18.95,8.07 C19.65,8.47 20.38,8.78 21.18,8.92 C22.21,9.09 22.45,7.47 21.41,7.30 C20.91,7.22 20.47,7.02 20.02,6.80 C19.89,6.74 19.76,6.67 19.64,6.60 "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.24,6.49 C20.28,6.46 20.32,6.43 20.36,6.40 C20.37,6.40 20.38,6.41 20.39,6.41 C22.70,6.44 24.96,6.74 27.28,6.71 C27.57,6.71 27.56,6.26 27.26,6.26 C25.12,6.28 23.00,6.03 20.86,5.98 "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.87
					strokeLineCap: StrokeLineCap.BUTT
					content: "M29.15,6.12 C29.07,5.86 28.72,5.65 28.54,5.47 C28.16,5.09 27.77,4.73 27.35,4.40 C27.20,4.29 27.01,4.50 27.16,4.61 C27.52,4.89 27.85,5.19 28.18,5.51 C28.36,5.68 28.53,5.85 28.71,6.01 C29.12,6.36 28.60,6.73 28.38,6.89 C27.98,7.21 26.76,8.32 26.22,7.91 C26.08,7.80 25.88,8.00 26.03,8.11 C26.80,8.70 28.06,7.49 28.63,7.06 C28.92,6.83 29.27,6.52 29.15,6.12 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M28.92,28.89 C28.69,28.78 28.50,28.88 28.39,29.05 C27.49,29.02 26.53,29.04 26.22,30.07 C25.83,31.37 26.80,32.64 27.98,33.05 C29.06,33.43 30.40,32.78 30.87,31.77 C31.46,30.52 29.86,29.33 28.92,28.89 Z M28.40,32.20 C27.54,32.04 26.88,31.11 27.14,30.24 C27.29,29.75 28.43,29.99 28.80,29.99 C28.86,29.99 28.91,29.98 28.96,29.96 C29.43,30.24 29.90,30.64 30.02,31.15 C30.19,31.87 28.91,32.30 28.40,32.20 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M30.90,22.45 C30.35,22.37 29.79,22.31 29.23,22.27 L29.23,21.23 C29.23,21.15 29.10,21.13 29.10,21.22 L29.10,22.26 C28.44,22.22 27.81,22.08 27.15,22.05 C27.04,22.04 27.00,22.22 27.12,22.23 C27.61,22.26 28.08,22.43 28.56,22.43 C28.74,22.44 28.92,22.45 29.10,22.46 L29.10,24.36 C29.10,24.44 29.23,24.46 29.23,24.38 L29.23,22.47 C29.78,22.51 30.32,22.56 30.87,22.64 C30.99,22.65 31.02,22.47 30.90,22.45 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M39.77,31.80 C39.21,32.11 38.35,31.81 38.49,31.05 C38.58,30.58 39.57,30.62 39.92,30.59 C40.48,30.53 40.47,29.65 39.91,29.71 C38.87,29.81 37.58,29.95 37.60,31.29 C37.63,32.62 39.19,33.13 40.22,32.55 C40.72,32.27 40.26,31.52 39.77,31.80 Z "
},
]
}
}
}
function run(){
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   Stage {
	title: 'MyApp'
	onClose: function () {  }
	scene: Scene {
		width: 200
		height: 200
      fill: Color.YELLOW
		content: [
         Debate_argumentIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Debate_argumentIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
