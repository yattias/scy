package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.ScyLoginManager;
import eu.scy.elobrowser.notification.GrowlFX;
import eu.scy.elobrowser.ui.SwingPasswordField;
import eu.scy.scywindows.ScyWindow;
import eu.scy.notification.Notification;
import eu.scy.scywindows.ScyWindowControl;
import java.lang.Math;
import javax.swing.JOptionPane;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.SimpleInterpolator;
import javafx.animation.Timeline;
import javafx.ext.swing.SwingButton;
import javafx.scene.control.TextBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
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
import eu.scy.elobrowser.awareness.contact.ChatConnector;
import eu.scy.elobrowser.main.user.User;

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
    var preferredWidth = 280;
    var preferredHeight = 180;
    var firstAssignment = true;

    public-init var growl: GrowlFX;

    public-init var mainContent : Node[] on replace {
        if (firstAssignment) {
            firstAssignment = false;
                for (n in mainContent) {
                n.visible = false;
                n.opacity = 0.0;
            }
        }
    }
    public var register : function():Void;
    public var scyWindowControl:ScyWindowControl;

    var loginGroup : Node;
    var loginButton: SwingButton;

    public function insertComponent(newComp : Node) {
        insert newComp into mainContent;
    }

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
                width: 70;
                height: 70;
                title:"Login"
                color:Color.BLUEVIOLET

                var usernamefield:TextBox = TextBox {
                    onKeyTyped: function( e: KeyEvent ):Void {
                        var entry = usernamefield.text.trim();
                        loginButton.enabled = entry.length()>0;
                     }

                }
                
                //var passwordfield = TextBox {}
                var passwordfield = SwingPasswordField {}

                scyContent:
                loginGroup = Grid {
                    width: 50
                    height: 50
                    growRows: [0.6]
                    growColumns: [0.6]
                    border: 20
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
                                content: loginButton = SwingButton {
                                   enabled:false;
                                    text: "Ok"
                                    action: function() {
                                        var sm = new ScyLoginManager();
                                        var username = usernamefield.text.trim();
                                        var password = passwordfield.text;
                                        // dirty hack, to be deleted after review
                                        var allowedUsers = ["Adam", "Anders", "Anne", "Barbara", "Wouter"];
                                        var allowed = false;
                                        for (user in allowedUsers) {
                                            if (user.equals(username)) {
                                                allowed = true;
                                            }
                                        }
                                        if (allowed) {
                                            var loginResult = sm.login(username, password);
                                            if (register!=null){
                                                println("################# Calling to register ################");
                                                register();
                                            }
                                            if(loginResult.equals(ScyLoginManager.LOGIN_OK)) {
                                                loginNodeDisappear();
                                            } else {
                                                startLoginAnimation();
                                            }
                                        } else {
                                            growl.showMessage("User '{username}' could not be found!", 3s);
                                        }


                                    }
                                }
                            }
                        }
                    ]
                }
                allowClose:false;
                allowResize:false;
                allowRotate:false;
                allowMinimize:false;
                allowDragging:false;
            }

        ];
		  loginNode.openWindow(100, 100);
        startLoginAnimation();
    }

    public function loginNodeDisappear() {
        var t : Timeline[];
            insert Timeline { // 0, login node fade out
            keyFrames: [KeyFrame {
                time: 1s
                values: [
                    loginNode.opacity => 0.0 tween Interpolator.EASEBOTH
                ]
                action: function() {
                    t[1].play();
                    loginNode.visible = false;
                   scyWindowControl.positionWindows();
                }
            } ]
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
                    },
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
                       for (n in mainContent) {
                            n.visible = false;
                            n.opacity = 0.0;
                         }
                }
            }
        } into t;
            insert Timeline {// 1, enlarge
            keyFrames: KeyFrame {
                time: 0.5s
                values: [
                    loginNode.width => 290 tween interpol
                    loginNode.height => 160 tween interpol
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
                    for (n in mainContent) {
                            n.visible = false;
                            n.opacity = 0.0;
                         }
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
