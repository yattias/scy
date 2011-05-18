package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;

public class EloFinishedIcon extends AbstractEloIcon {

    public override function clone(): EloFinishedIcon {
        EloFinishedIcon {
            selected: selected
            size: size
            windowColorScheme: windowColorScheme
        }
    }

    public override function createNode(): Node {
        return Group { content: [
                        Rectangle {
                            fill: bind windowColorScheme.mainColor
                            stroke: null
                            x: 0.42
                            y: 0.14
                            width: 40.0
                            height: 40.0
                        },
                        Rectangle {
                            fill: bind windowColorScheme.mainColorLight
                            stroke: null
                            x: 3.42
                            y: 3.14
                            width: 34.0
                            height: 37.0
                        },
                        Line {
                            startX: 10, startY: 25
                            endX: 20, endY: 35
                            strokeWidth: 3
                            stroke: bind windowColorScheme.secondColor
                        },
                        Line {
                            startX: 20, startY: 35
                            endX: 32, endY: 10
                            strokeWidth: 3
                            stroke: bind windowColorScheme.secondColor
                        }
                    ]
                }
    }

}

function run() {
    def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
    Stage {
        title: 'MyApp'
        onClose: function() {
        }
        scene: Scene {
            width: 200
            height: 200
            fill: Color.YELLOW
            content: [
                EloFinishedIcon {
                    windowColorScheme: windowColorScheme
                    layoutX: 25
                    layoutY: 25
                }
                EloFinishedIcon {
                    windowColorScheme: windowColorScheme
                    layoutX: 75
                    layoutY: 25
                    selected: true
                }
            ]
        }
    }
}
