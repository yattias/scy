/*
 * GrowlFX.fx
 *
 * Created on 22.01.2009, 14:09:34
 */

package eu.scy.elobrowser.notification;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.main.user.User;
import eu.scy.elobrowser.tool.colemo.ColemoNode;
import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationCallback;
import eu.scy.notification.NotificationService;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindowControl;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.lang.Duration;
import javafx.lang.FX;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javax.swing.JOptionPane;

/**
 * @author Giemza
 */

public class GrowlFX extends CustomNode, INotificationCallback {


    var text :String;

    public var width = 500;

    public var height = 50;

    public var roolo :Roolo;

    public var scyDesktop :ScyDesktop;

    public var scyWindowControl :ScyWindowControl;

    var content : Group;

    def DEFAULT_SHOW_TIME = 6s;

    var showTime : Duration;

    public override function create(): Node {
        content = Group {
            content: [Rectangle {
                    x: 10,
                    y: 10
                    width: bind width,
                    height: bind height
                    fill: Color.BLACK
                    arcWidth: 50
                    arcHeight: 50
                }, Text {
                    font: Font {
                        size: 20
                    }
                    x: 28,
                    y: 40
                    fill: Color.WHITE
                    content: bind text
                }]
        };
        content.effect = DropShadow {
            radius: 15
        }
        return content;
    }

    public function showMessage(message : String) : Void {
        showMessage(message, DEFAULT_SHOW_TIME);
    }

    public function showMessage(message : String, showTime : Duration) : Void {
        this.showTime = showTime;
        FX.deferAction(function() :Void {
            text = message;
            if(fadein.running) {
                fadein.stop();
            }
            fadein.playFromStart();
        });
    }

    override function onNotification(notification :INotification) {
        this.showTime = DEFAULT_SHOW_TIME;
        println("XXX -> Notification received...");
        var props = notification.getProperties();
        if(props.containsKey("errors")) {
            var errnum = notification.getProperty("errors");
            println("Received errors: {errnum}");
            FX.deferAction(function() :Void {
                text = "You have {errnum} errors in your last saved text!";
                if(fadein.running) {
                    fadein.stop();
                }
                fadein.playFromStart();
            });
        } else if (props.containsKey("initCollaboration")) {
            var userForCollaboration = notification.getProperty("username");
            FX.deferAction(function() :Void {
                var result = JOptionPane.showConfirmDialog(null, "Do you want to start collaboration with {userForCollaboration}?", "Collaborartion Request", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.OK_OPTION) {
                    var colemoWIndow = ColemoNode.createColemoWindow(roolo);
                    colemoWIndow.allowResize = true;
                    scyDesktop.addScyWindow(colemoWIndow);
                    colemoWIndow.openWindow(600,300);
                    scyWindowControl.addOtherScyWindow(colemoWIndow, true);
                }
            });
        } else if (props.containsKey("target")) {
            var target = props.get("target");
            if(props.containsKey("users")) {
                var usersString :String = props.get("users") as String;
                var users :String[] = usersString.split(";");
                println("Possible collaboration partners: {users}");
                FX.deferAction(function() :Void {
                    text = "Possible collaboration partners: {users}";
                    if(fadein.running) {
                        fadein.stop();
                    }
                    fadein.playFromStart();
                });
            }
        }
    }

    public function register() :Void {
       var notificationService = new NotificationService("scy.collide.info", 2525, "notifications");
       notificationService.registerCallback(User.instance.getUsername(), this);
       println("Registered callback for {User.instance.getUsername()}");
    }


    var fadein = Timeline {
        keyFrames: [
            KeyFrame {
                time: 1s
                canSkip: false
                values: [
                    opacity => 0.7 tween Interpolator.LINEAR
                ]
            },
            KeyFrame {
                time: bind showTime;
                action: function() {
                    fadeout.playFromStart();
                }
            }
        ]
    }

    var fadeout = Timeline {
        keyFrames: [
            KeyFrame {
                time: 1s
                canSkip: true
                values: [
                    opacity => 0.0 tween Interpolator.LINEAR
                ]
            }
        ]
    }

}
