package eu.scy.elobrowser.main;

import eu.scy.scywindows.ScyWindow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.lang.Math;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.SimpleInterpolator;
import javafx.animation.Timeline;
import javafx.ext.swing.SwingButton;
import javafx.scene.control.TextBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jfxtras.scene.layout.Cell;
import org.jfxtras.scene.layout.Grid;
import org.jfxtras.scene.layout.HorizontalAlignment;
import org.jfxtras.scene.layout.Row;

class SlackyInterpolator extends SimpleInterpolator {

    override function curve(x: Number) : Number {
        if (x >= 0 and x <= 0.2) {
            return 11.61 * Math.pow(x,3) + 0.04 * x;
        } else
        if (x > 0.2 and x <= 0.3) {
            return  -12.5 * Math.pow(x,3) + 14.46 * Math.pow(x,2) - 2.86 * x + 0.19;
        } else
        if (x > 0.3 and x <= 0.5) {
            return  -2.23 * Math.pow(x,3) + 5.22 * Math.pow(x,2) - 0.08 * x - 0.08;
        } else
        if (x > 0.5 and x <= 0.7) {
            return  -58.48 * Math.pow(x,3) + 89.6 * Math.pow(x,2) - 42.27 * x + 6.95;
        } else
        if (x > 0.7 and x <= 0.8) {
            return  212.5 * Math.pow(x,3) - 479.46 * Math.pow(x,2) + 356.07 * x - 86;
        } else {
            return  -50.89 * Math.pow(x,3) + 152.68 * Math.pow(x,2) - 149.64 * x + 48.86;
        }
    }
}

public class SCYLogin extends Group {
    var text : Text;
    var loginNode : ScyWindow;
    def maxOpacity = 0.9;
    var preferredWidth = 250;
    var preferredHeight = 140;

    public-init var mainContent : Node[] on replace {
        for (n in mainContent) {
            n.opacity = 0.0;
        }
    }

    var loginGroup : Node;

    init  {
        content = [
            text = Text {
                visible: false
                opacity: 0.0
                content: "SCY"
                fill: Color.DARKGRAY
                translateX: bind (scene.width - text.layoutBounds.width) / 2 - text.layoutBounds.minX
                translateY: bind (scene.height - text.layoutBounds.height) / 2 - text.layoutBounds.minY
                font: Font.font("Verdana", FontWeight.MEDIUM,300)
                stroke: Color.CADETBLUE
                effect: Reflection {
                    fraction: 0.8
                    topOpacity: 0.4
                    topOffset: 10
                }
                effect: DropShadow {
                    radius: 20
                }

            },
            loginNode= ScyWindow{
                translateX: bind (scene.width - loginNode.layoutBounds.width) / 2 - loginNode.layoutBounds.minX
                translateY: bind (scene.height - loginNode.layoutBounds.height) / 2 - loginNode.layoutBounds.minY
                opacity:0.0;
                width: 90;
                height: 90;
                title:"Login"
                color:Color.BLUEVIOLET

                var usernamefield = TextBox {}

                var passwordfield = TextBox {}

                scyContent:
                loginGroup = Grid {
                    width: 50
                    height: 50
                    growRows: [0.9]
                    growColumns: [0.9]
                    border: 10
                    rows: [
                        Row {
                            cells: [
                                Text {
                                    content: "Username:"
                                },
                                Cell {
                                    content: usernamefield
                                }
                            ]
                        },
                        Row {
                            cells: [
                                Text {
                                    content: "Password:"
                                },
                                Cell {
                                    content: passwordfield
                                }
                            ]
                        },
                        Row {
                            cells: Cell {
                                columnSpan: 2
                                horizontalAlignment: HorizontalAlignment.CENTER
                                content: SwingButton {
                                    text: "Ok"
                                    action: function() {
                                        var sm = new ScyLoginManager();
                                        var username = usernamefield.text;
                                        var password = passwordfield.text;
                                        var loginResult = sm.login(username, password);
                                        if(loginResult.equals(ScyLoginManager.LOGIN_OK)) {
                                            loginNodeDisappear();
                                        } else {
                                            startLoginAnimation();
                                        }


                                    }
                                }
                            }
                        }
                    ]
                }
                allowClose:false;
                allowResize:true;
                allowRotate:false;
            }

        ];
		  loginNode.openWindow(90, 90);
        startLoginAnimation();
    }

    public function loginNodeDisappear() {
        var t : Timeline[];
            insert Timeline { // 0, login node fade out
            keyFrames: KeyFrame {
                time: 1s
                values: [
                    loginNode.opacity => 0.0 tween Interpolator.EASEBOTH
                ]
                action: function() {
                    t[1].play();
                }
            }
        } into t;
            insert Timeline {// 1, other nodes fade in
            keyFrames: KeyFrame {
                time: 1s
                values: [
                    // FIXME there must be a better way ...
                    mainContent[0].opacity => 1.0
                    mainContent[1].opacity => 1.0
                ]
            }
        } into t;
        t[0].play();
    }
    
    public function loginNodeAppear() {
        loginGroup.opacity = 0.0;
        var interpol = SlackyInterpolator {}
        var t : Timeline[];
            insert Timeline {// 0, appear
            keyFrames: KeyFrame {
                time: 0.5s
                values: loginNode.opacity => maxOpacity
                action: function() {
                    t[1].play();
                }
            }
        } into t;
            insert Timeline {// 1, enlarge
            keyFrames: KeyFrame {
                time: 0.5s
                values: [
                    loginNode.width => 250 tween interpol
                    loginNode.height => 140 tween interpol
                ]
                action: function() {
                    t[2].play();
                }
            }
        } into t;
            insert Timeline {// 2
            keyFrames: KeyFrame {
                time: 1s
                values: [
                    loginGroup.opacity => 1.0,
                ]
            }
        } into t;
        t[0].play();
    }


    public function startLoginAnimation() {
        text.visible = true;
        var t : Timeline[];
            insert Timeline { // 0, delay
            keyFrames: KeyFrame {
                time: 1s
                action: function() {
                    t[1].play();
                }
            }
        } into t;
            insert Timeline {// 1, fade in
            keyFrames: KeyFrame {
                time: 1s
                values: text.opacity => 0.5
                action: function() {
                    t[2].play();
                }
            }
        } into t;
            insert Timeline {// 2, delay
            keyFrames: KeyFrame {
                time: 1s
                action: function() {
                    t[3].play();
                }
            }
        } into t;
            insert Timeline {// 3, fade out
            keyFrames: KeyFrame {
                time: 1s
                values: text.opacity => 0.0
                action: function() {
                    text.visible = false;
                    loginNodeAppear();
                }
            }
        } into t;
        t[0].play();
    }
    
}