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

    // the amplitude of the wave
    // controls how far out the object can go from it's final stopping point.
    public-init var amplitude:Number = 1.0;
    // determines the weight of the object
    // makes the wave motion go longer and farther
    public-init var mass:Number = 0.058;
    // the stiffness of the wave motion / spring
    // makes the motion shorter and more snappy
    public-init var stiffness:Number = 12.0;
    // makes the wave motion be out of phase, so that the object
    // doesn't end up on the final resting spot.
    // this variable is usually never changed
    public-init var phase:Number = 0.0;

    // if this should do a normal spring or a bounce motion
    public-init var bounce:Boolean = false;


    // internal variables used for calcuations
    var pulsation:Number;

    init {
        this.pulsation = Math.sqrt(stiffness / mass);
    }


    // the actual spring equation
    override public function curve(t: Number) : Number {
        var t2 = -Math.cos(pulsation*t+phase+Math.PI) * (1-t) * amplitude ;
        // use the absolute value of the distance if doing a bounces
        if(bounce) {
            return 1-Math.abs(t2);
        } else {
            return 1-t2;
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
            n.visible = false;
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
                    // Nodes are draggable when opaque, so set visible = false too
             
                    for (n in mainContent){
                    n.visible = true;
                    n.opacity => 1.0 tween SimpleInterpolator.LINEAR;
                    }
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