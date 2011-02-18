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
public class PizzaIcon extends AbstractEloIcon {

public override function clone(): PizzaIcon {
PizzaIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColorLight
					strokeWidth: 5.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M37.06,15.37 C35.83,11.25 33.00,8.06 29.23,5.52 C24.37,2.24 18.72,3.17 13.29,4.51 C12.73,4.65 12.27,4.88 11.90,5.16 C11.14,5.27 10.38,5.58 9.76,6.23 C3.67,12.55 0.14,21.54 5.80,29.12 C10.78,35.79 19.91,38.20 28.33,35.71 C37.55,32.98 39.19,22.54 37.06,15.37 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L8.82,8.68 C11.95,5.55 15.73,3.99 20.14,3.99 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L4.14,20.00 C4.14,15.58 5.70,11.80 8.82,8.68 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L8.82,31.32 C5.70,28.19 4.14,24.42 4.14,20.00 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L20.14,36.01 C15.73,36.01 11.95,34.44 8.82,31.32 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L31.47,31.32 C28.34,34.44 24.57,36.01 20.14,36.01 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L36.15,20.00 C36.15,24.42 34.59,28.19 31.47,31.32 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L31.47,8.68 C34.59,11.80 36.15,15.58 36.15,20.00 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.14,20.00 L20.14,3.99 C24.57,3.99 28.34,5.55 31.47,8.68 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.22
					strokeLineCap: StrokeLineCap.BUTT
					content: "M8.63,16.06 C9.46,16.74 9.94,15.91 10.79,16.68 C11.52,17.64 11.72,18.64 12.70,19.59 C13.62,18.42 14.72,18.00 15.71,17.12 C14.60,16.33 14.12,15.50 12.83,14.73 C15.11,10.00 5.51,12.69 8.63,16.06 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.2
					strokeLineCap: StrokeLineCap.BUTT
					content: "M22.34,8.90 C22.75,9.73 23.54,9.18 23.92,10.09 C24.12,11.13 23.84,12.03 24.25,13.14 C25.58,12.44 26.70,12.42 27.95,11.99 C27.35,10.99 27.31,10.14 26.55,9.09 C30.63,5.82 21.17,5.10 22.34,8.90 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.2
					strokeLineCap: StrokeLineCap.BUTT
					content: "M31.04,21.83 C30.68,22.68 31.61,22.92 31.18,23.80 C30.52,24.63 29.66,25.02 29.10,26.05 C30.49,26.59 31.25,27.43 32.39,28.09 C32.75,26.97 33.37,26.39 33.66,25.13 C38.80,26.06 33.14,18.45 31.04,21.83 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M26.38,32.13 C30.97,32.84 27.77,27.15 24.33,27.28 C20.01,27.45 24.71,31.88 26.38,32.13 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M33.15,18.54 C37.84,16.23 31.19,12.67 28.11,15.06 C24.24,18.07 31.45,19.38 33.15,18.54 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M14.54,22.45 C13.54,21.12 10.12,21.61 9.66,23.77 C8.89,27.39 13.94,25.64 14.53,24.39 C14.93,23.54 14.87,22.89 14.54,22.45 "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.21
					strokeLineCap: StrokeLineCap.BUTT
					content: "M15.38,28.03 C15.32,28.71 15.97,29.08 16.16,29.65 C15.78,30.47 15.40,31.30 15.01,32.12 C15.63,32.79 17.10,32.27 18.19,32.85 C18.69,31.64 18.32,30.82 18.53,29.75 C19.18,29.99 19.60,29.50 20.01,28.93 C20.24,26.98 16.12,25.36 15.38,28.03 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.02,11.32 C18.40,14.00 13.47,11.92 13.19,9.37 C14.69,6.41 19.85,8.84 20.02,11.32 Z "
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
         PizzaIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         PizzaIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
