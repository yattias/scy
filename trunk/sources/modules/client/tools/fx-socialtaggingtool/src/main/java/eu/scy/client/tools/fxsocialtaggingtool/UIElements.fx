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

/**
 * @author sindre
 */

def iconRadius = 16;
def iconFontSize = 32;
public def labelWidth = 100;

public class Plus extends Stack {
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

    override var content = [
                Circle {
                    radius: iconRadius
                    fill: bind if (hover) Color.GREEN else Color.GREY
                },
                Text {
                    font: Font {
                        size: iconFontSize
                        name: "wingdings"
                    }
                    fill: Color.WHITE
                    content: "C"
                }
            ]
}

public class ThumbsDown extends Stack {


    override var content = [
                Circle {
                    radius: iconRadius
                    fill: bind if (hover) Color.RED else Color.GREY
                },
                Text {
                    font: Font {
                        size: iconFontSize
                        name: "wingdings"
                    }
                    fill: Color.WHITE
                    content: "D"
                }
            ]
}