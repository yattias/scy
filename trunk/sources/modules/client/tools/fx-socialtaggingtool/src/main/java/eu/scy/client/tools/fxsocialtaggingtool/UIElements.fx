/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;

import javafx.scene.layout.Stack;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * @author sindre
 */
def iconRadius = 12;
def iconFontSize = 22;
public def labelWidth = 80;

public class Plus extends Stack {

    public var tag;
    override var content = [
                Circle {
                    radius: iconRadius
                    fill: Color.GREEN
                },
                Text {
                    font: Font {
                        size: iconFontSize
                    }
                    fill: Color.WHITE
                    content: "+"
                }
            ]
}

public class Minus extends Stack {

    public var tag;
    override var content = [
                Circle {
                    radius: iconRadius
                    fill: Color.RED
                },
                Text {
                    font: Font {
                        size: iconFontSize
                    }
                    fill: Color.WHITE
                    content: "-"
                }
            ]
}

public class SmallPlus extends Stack {

    public var tag;
    override var content = [
                Circle {
                    radius: iconRadius / 2
                    fill: Color.GREEN
                },
                Text {
                    font: Font {
                        size: iconFontSize / 2
                    }
                    fill: Color.WHITE
                    content: "+"
                }
            ]
}

public class SmallMinus extends Stack {

    override var content = [
                Circle {
                    radius: iconRadius / 2
                    fill: Color.RED
                },
                Text {
                    font: Font {
                        size: iconFontSize / 2
                    }
                    fill: Color.WHITE
                    content: "-"
                }
            ]
}

public class ThumbsUp extends Stack {

    public var tag;
    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/thumbsup.png";
                    }
                }
            ]
}

public class ThumbsDown extends Stack {

    public var tag;
    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/thumbsdown.png";
                    }
                }
            ]
}
