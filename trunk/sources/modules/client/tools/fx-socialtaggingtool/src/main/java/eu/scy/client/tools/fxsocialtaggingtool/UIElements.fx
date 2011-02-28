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
import javafx.scene.control.Button;

/**
 * @author sindre
 */
def iconRadius = 16;
def iconFontSize = 32;
public def labelWidth = 100;

public class Plus extends Stack {

    public var tag;
    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/plus.png";
                    }
                }
            ]
}

public class Minus extends Stack {

    public var tag;
    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/minus.png";
                    }
                }
            ]
}

public class SmallPlus extends Stack {

    public var tag;
    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/plus_small.png";
                    }
                }
            ]
}

public class SmallMinus extends Stack {

    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/minus_small.png";
                    }
                }
            ]
}

public class ThumbsUp extends Stack {

    public var tag;
    public var alwaysOn;
    override var layoutX = iconRadius * 2 + 4;
    override var layoutY = iconRadius * 2 + 4;
    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/thumbsupGrey.png";
                    }
                    opacity: bind if (alwaysOn or hover) 0.0 else 1.0;
                },
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/thumbsup_mouseover.png";
                    }
                    opacity: bind if (alwaysOn or hover) 1.0 else 0.0;
                }
            ]
}

public class ThumbsDown extends Stack {

    public var tag;
    public var alwaysOn;
    override var layoutX = iconRadius * 2 + 4;
    override var layoutY = iconRadius * 2 + 4;
    override var content = [
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/thumbsdownGrey.png";
                    }
                    opacity: bind if (alwaysOn or hover) 0.0 else 1.0;
                },
                ImageView {
                    image: Image {
                        url: "{__DIR__}images/thumbsdown_mouseover.png";
                    }
                    opacity: bind if (alwaysOn or hover) 1.0 else 0.0;
                }
            ]
}

