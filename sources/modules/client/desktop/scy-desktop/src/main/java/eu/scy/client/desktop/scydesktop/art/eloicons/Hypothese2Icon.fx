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
public class Hypothese2Icon extends AbstractEloIcon {

public override function clone(): Hypothese2Icon {
Hypothese2Icon {
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
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M21.83,0.53 C20.35,-0.14 19.06,0.11 18.12,0.85 C9.11,0.82 -2.42,5.02 1.05,16.27 C1.83,18.78 3.09,21.03 4.13,23.43 C5.55,26.67 4.44,30.03 4.99,33.33 C5.91,38.79 13.58,39.03 17.70,39.62 C21.95,40.24 27.98,41.15 30.85,36.93 C32.50,34.52 33.63,32.72 35.82,30.71 C37.71,28.97 38.93,26.87 39.79,24.47 C44.03,12.64 30.42,4.46 21.83,0.53 Z M29.51,25.06 C28.32,26.15 26.87,27.10 25.91,28.43 C24.93,29.79 24.63,31.69 23.16,31.69 C21.40,31.69 15.70,31.60 13.28,30.54 C13.22,26.39 12.90,22.53 11.15,18.52 C10.20,16.35 7.58,13.16 9.77,11.03 C12.41,8.47 17.01,9.08 20.29,9.46 C20.54,9.49 20.77,9.48 21.00,9.47 C27.03,12.51 36.48,18.66 29.51,25.06 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M29.19,37.16 L12.35,37.16 Q12.76,31.47 12.65,30.93 C12.54,30.38 11.78,26.20 10.91,25.65 C10.04,25.11 10.87,25.62 7.66,22.38 C4.44,19.14 4.55,14.12 5.84,11.68 C7.13,9.24 10.31,5.93 14.55,4.51 Q18.78,3.10 25.03,4.33 Q29.38,5.77 30.23,6.60 Q32.61,7.56 33.98,14.31 C34.08,14.81 33.00,14.81 32.78,16.04 Q33.16,17.55 33.76,17.91 Q34.35,18.27 35.48,19.86 Q35.85,20.51 35.63,20.87 C35.40,21.23 33.68,21.73 33.61,22.02 C33.53,22.31 33.34,22.86 33.46,23.10 C33.64,23.50 34.00,23.77 34.02,23.98 Q34.07,24.37 32.26,25.00 Q33.97,24.97 33.98,25.26 C34.02,26.07 33.36,26.57 33.49,27.14 C33.68,27.93 33.87,27.82 33.83,28.61 C33.82,28.94 33.68,29.37 33.61,29.59 C33.53,29.81 33.08,30.67 31.06,30.96 Q29.03,31.25 28.88,31.97 Q28.21,33.05 28.81,33.62 Q29.41,34.20 29.30,37.16 L29.27,37.16 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M29.85,17.86 C29.85,23.09 25.45,27.32 20.01,27.32 C14.58,27.32 10.18,23.09 10.18,17.86 C10.18,12.64 14.58,8.41 20.01,8.41 C25.45,8.41 29.85,12.64 29.85,17.86 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M17.53,10.85 Q17.38,9.73 19.10,9.59 C20.34,9.48 23.13,9.62 23.34,10.38 Q23.53,11.06 21.09,13.80 Q20.49,14.27 19.70,13.77 Q18.92,13.26 17.53,10.85 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M25.59,12.46 Q26.60,11.89 27.42,13.36 C28.00,14.41 28.96,16.93 28.31,17.41 Q27.74,17.85 24.16,16.77 Q23.48,16.42 23.65,15.53 Q23.82,14.63 25.59,12.46 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M27.73,19.29 Q28.75,19.83 27.87,21.25 C27.23,22.27 25.48,24.37 24.71,24.08 Q24.03,23.83 23.15,20.32 Q23.11,19.58 23.99,19.27 Q24.88,18.95 27.73,19.29 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M15.29,23.11 Q14.36,23.79 13.37,22.43 C12.66,21.46 11.38,19.07 11.96,18.51 Q12.48,18.01 16.16,18.66 Q16.88,18.92 16.82,19.84 Q16.76,20.74 15.29,23.11 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M23.22,24.94 Q23.39,26.05 21.67,26.23 C20.44,26.36 17.64,26.28 17.42,25.52 Q17.22,24.84 19.60,22.05 Q20.19,21.57 20.98,22.06 Q21.78,22.55 23.22,24.94 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M13.12,16.36 Q12.04,15.92 12.77,14.42 C13.29,13.34 14.81,11.08 15.60,11.29 Q16.31,11.47 17.56,14.87 Q17.68,15.60 16.83,16.01 Q15.99,16.41 13.12,16.36 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M21.68,17.69 C21.68,18.53 20.96,19.22 20.08,19.22 C19.20,19.22 18.48,18.53 18.48,17.69 C18.48,16.84 19.20,16.15 20.08,16.15 C20.96,16.15 21.68,16.84 21.68,17.69 Z "
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
         Hypothese2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Hypothese2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
