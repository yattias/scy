package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
/**
 * @author lars
 */
public class Dataset_processedIcon extends AbstractEloIcon {

public override function clone(): Dataset_processedIcon {
Dataset_processedIcon {
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
					content: "M38.59,36.09 C37.60,30.16 37.45,23.86 37.64,17.87 C37.77,13.68 38.50,8.88 37.64,4.74 C37.41,3.64 36.38,3.18 35.36,3.52 C31.08,4.94 24.80,3.90 20.34,3.51 C15.08,3.05 10.05,1.90 4.74,1.96 C2.79,1.98 2.34,4.82 4.02,5.16 C4.43,11.87 3.39,19.06 3.01,25.75 C2.82,29.17 0.50,34.30 3.50,36.94 C4.60,37.92 7.18,37.48 8.46,37.49 C11.54,37.51 14.67,38.07 17.75,38.30 C24.16,38.79 30.83,38.37 37.24,37.95 C38.12,37.89 38.72,36.90 38.59,36.09 Z M5.55,34.19 C5.86,34.20 7.11,11.74 7.20,5.26 C12.20,5.52 17.09,6.49 22.09,6.93 C25.97,7.27 30.66,7.76 34.69,6.99 C35.25,11.71 34.37,16.97 34.31,21.61 C34.25,25.96 34.51,30.47 35.12,34.82 C25.17,35.32 15.49,34.55 5.55,34.19 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 7.57
					y: 6.35
					width: 27.57
					height: 27.57
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M34.23,7.27 L34.23,33.00 L8.49,33.00 L8.49,7.27 Z M36.07,5.43 L6.65,5.43 L6.65,34.84 L36.07,34.84 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 28.69
					y: 27.52
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 28.69
					y: 16.69
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 28.69
					y: 5.54
					width: 7.35
					height: 7.4
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 17.7
					y: 5.46
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 6.55
					y: 5.46
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 17.7
					y: 16.66
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 6.55
					y: 16.66
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 17.7
					y: 27.58
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 6.55
					y: 27.58
					width: 7.37
					height: 7.37
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M34.23,7.27 L34.23,33.00 L8.49,33.00 L8.49,7.27 Z M36.06,5.43 L6.65,5.43 L6.65,34.84 L36.06,34.84 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M11.22,9.14 L10.91,9.14 C10.09,9.14 9.36,9.66 9.30,10.53 C9.24,11.29 9.86,12.14 10.68,12.14 L10.99,12.14 C11.81,12.14 12.54,11.62 12.60,10.75 C12.66,9.99 12.04,9.14 11.22,9.14 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M21.15,8.90 C21.12,8.90 21.12,8.90 21.10,8.89 C21.16,8.91 21.22,8.93 21.30,8.96 C21.23,8.93 21.16,8.92 21.09,8.89 C20.90,8.89 20.89,8.85 21.05,8.88 C19.42,8.42 18.37,10.92 20.04,11.69 C20.50,11.90 20.95,11.91 21.44,11.88 C22.26,11.85 22.86,11.03 22.79,10.24 C22.71,9.40 21.96,8.86 21.15,8.90 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M21.10,8.89 C21.08,8.89 21.06,8.88 21.05,8.88 C21.06,8.88 21.08,8.89 21.09,8.89 C21.10,8.89 21.10,8.89 21.10,8.89 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M31.61,8.89 C29.67,8.89 29.67,11.89 31.61,11.89 C33.55,11.89 33.54,8.89 31.61,8.89 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M32.71,19.28 C32.05,18.83 31.10,18.98 30.64,19.64 C30.62,19.65 30.61,19.66 30.56,19.69 C29.88,20.14 29.93,21.25 30.41,21.81 C31.00,22.48 31.84,22.41 32.53,21.96 C32.76,21.81 32.94,21.60 33.09,21.36 C33.54,20.68 33.39,19.75 32.71,19.28 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M22.38,19.69 C21.73,19.35 21.00,19.54 20.55,20.04 C20.08,20.42 19.86,21.05 20.05,21.65 C20.30,22.43 21.16,22.89 21.93,22.63 C22.43,22.47 22.73,22.16 22.99,21.72 C23.41,21.02 23.07,20.06 22.38,19.69 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M11.21,20.02 C9.30,20.02 8.95,23.01 10.88,23.01 C12.79,23.01 13.14,20.02 11.21,20.02 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M11.64,29.33 C9.70,29.33 9.64,32.33 11.58,32.33 C13.51,32.33 13.57,29.33 11.64,29.33 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M22.43,29.81 C22.09,29.18 21.17,28.77 20.49,29.15 C19.87,29.32 19.39,29.82 19.36,30.52 C19.33,31.30 19.98,32.09 20.80,32.08 C21.15,32.07 21.51,32.04 21.81,31.84 C22.49,31.38 22.85,30.59 22.43,29.81 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M32.00,28.89 C30.07,28.89 29.91,31.89 31.84,31.89 C33.77,31.89 33.93,28.89 32.00,28.89 Z "
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
         Dataset_processedIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Dataset_processedIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
