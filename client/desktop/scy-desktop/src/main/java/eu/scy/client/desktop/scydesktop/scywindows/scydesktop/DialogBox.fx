/*
 * DialogBox.fx
 *
 * Created on 18-jan-2010, 11:14:35
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.input.KeyEvent;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javax.swing.JOptionPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageEloIcon;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.scene.effect.DropShadow;

/**
 * @author sven
 */
public static def DEFAULT_WIDTH: Double = 400;
public static def HGAP: Double = 15;
public static def VGAP: Double = 15;
def imageLoader = ImageLoader.getImageLoader();

static  function getDialogBoxContent(dialogWidth: Integer, dialogBox: DialogBox, dialogType: DialogType, text: String, action1: function(),  action2: function (),action3:function ()): Group{

        def indicatorImage    : ImageView = getIndicatorImage(dialogType);
        def group: Group = Group {
                    content: [
                        VBox {
                            hpos: HPos.CENTER
                            vpos: VPos.CENTER
                            nodeHPos: HPos.CENTER
                            spacing: VGAP
                            content: [
                                HBox {
                                    hpos: HPos.CENTER
                                    vpos: VPos.CENTER
                                    nodeVPos: VPos.CENTER
                                    spacing: HGAP
                                    content: [
                                        indicatorImage,
                                        Text {
                                            content: text;
                                            wrappingWidth: (dialogWidth - 3 * HGAP - indicatorImage.layoutBounds.width)
                                        }
                                    ];
                                },
                                getButtonBar(dialogBox, dialogType, action1, action2, action3)
                            ];
                        }
                    ];
                }
        return group
    }

   static  function getIndicatorImage(dialogType)  : ImageView   {
        if (dialogType == DialogType.OK_DIALOG) {
                return  ImageView{
                     image: imageLoader.getImage("info_red_x32.png");
                };
        }else if (dialogType==DialogType.YES_NO_DIALOG or dialogType == DialogType.OK_CANCEL_DIALOG){
            return ImageView {
                    image: imageLoader.getImage("question_blue_x32.png");
                };
        } else {
            return ImageView {
                    image: imageLoader.getImage("info_red_x32.png");
                };
        }
    }

    static function getButtonBar(dialogBox:DialogBox, dialogType:DialogType, action1:function(), action2:function(), action3:function()):HBox{
        if (dialogType==DialogType.OK_DIALOG){
            return getOkButtonBar(dialogBox, action1) 
        } else if (dialogType==DialogType.YES_NO_DIALOG){
            return getYesNoButtonBar(dialogBox,action1, action2)
        } else {
            return getOkButtonBar(dialogBox, function(){}) 
        }
    }

    static function getOkButtonBar(dialogBox:DialogBox, okAction:function()):HBox{
        def buttonBar:HBox = HBox{
            hpos:HPos.CENTER
            spacing:HGAP
            content: [
                    Button{
                        text:##"OK!"
                        action:function():Void{
                            okAction();
                            dialogBox.close();
                        }
                    }
                    ]
        }
        return buttonBar
    }

        static function getYesNoButtonBar(dialogBox:DialogBox,yesAction:function(), noAction:function()):HBox{
        def buttonBar:HBox = HBox{
            hpos:HPos.CENTER
            spacing:HGAP
            content: [
                    Button{
                        text:##"Yes!"
                        action:function():Void{
                            yesAction();
                            dialogBox.close();
                        }
                    }
                     Button{
                        text:##"No!"
                        action:function():Void{
                            noAction();
                            dialogBox.close();
                        }
                    }
                    ]
        }
        return buttonBar
    }

public static function showMessageDialog(text:String,dialogTitle:String, dialogWidth:Integer, scyDesktop:ScyDesktop, modal:Boolean, indicateFocus:Boolean, okAction:function()):Void{
   
        def dialogBox: DialogBox = DialogBox {
//                    content: getDialogBoxContent(dialogWidth,dialogBox, DialogType.OK_DIALOG,text, okAction, function(){}, function(){})
                    targetScene: scyDesktop.scene
                    eloIcon: ImageEloIcon{
                            activeImage:imageLoader.getImage("info_red_active_x16.png");
                            inactiveImage:imageLoader.getImage("info_red_inactive_x16.png");
                            }
                    title: dialogTitle
                    modal: modal
                    indicateFocus: indicateFocus
                    scyDesktop:scyDesktop
                    closeAction: function () {

                    }
                    windowColorScheme: WindowColorScheme.getWindowColorScheme(EloImageInformation.getScyColors("general/new"));
                };
        dialogBox.content= getDialogBoxContent(dialogWidth,dialogBox, DialogType.OK_DIALOG,text, okAction, function(){}, function(){});
        dialogBox.place();
}

public static function showMessageDialog(text:String,dialogTitle:String, scyDesktop:ScyDesktop, okAction:function()):Void{
    showMessageDialog(text,dialogTitle, DEFAULT_WIDTH, scyDesktop, true,true,  okAction);
}

public static function showMessageDialog(params:DialogBoxParams):Void{
    showMessageDialog(params.text,params.title, params.dialogWidth, params.scyDesktop, params.modal,params.indicateFocus, params.okAction);
}


public static function showOptionDialog(dialogType:DialogType,text:String,dialogTitle:String, dialogWidth:Integer, scyDesktop:ScyDesktop, modal:Boolean,indicateFocus:Boolean, okAction:function(), cancelAction:function()):Void{
        def supportedOptionTypes = [DialogType.OK_CANCEL_DIALOG, DialogType.YES_NO_DIALOG];
        def dialogBox: DialogBox = DialogBox {
//                    content: if (sizeof supportedOptionTypes[n|n==dialogType] > 0) {
//                                getDialogBoxContent(dialogWidth, dialogBox, dialogType,text, okAction, cancelAction, function(){})
//                            } else { //Default OptionPane Type
//                                getDialogBoxContent(dialogWidth, dialogBox, DialogType.OK_CANCEL_DIALOG,text, okAction, cancelAction, function(){})
//                                }
                    targetScene: scyDesktop.scene
                    eloIcon: ImageEloIcon{
                            activeImage:imageLoader.getImage("question_blue_active_x16.png");
                            inactiveImage:imageLoader.getImage("question_blue_inactive_x16.png");
                            }
                    title: dialogTitle
                    modal: modal
                    indicateFocus: indicateFocus
                    scyDesktop:scyDesktop
                    closeAction: function () {

                    }
                    windowColorScheme: WindowColorScheme.getWindowColorScheme(EloImageInformation.getScyColors("general/search"));
                };
        dialogBox.content= if (sizeof supportedOptionTypes[n|n==dialogType] > 0) {
                                getDialogBoxContent(dialogWidth, dialogBox, dialogType,text, okAction, cancelAction, function(){})
                            } else { //Default OptionPane Type
                                getDialogBoxContent(dialogWidth, dialogBox, DialogType.OK_CANCEL_DIALOG,text, okAction, cancelAction, function(){})
                                };
        dialogBox.place();
}

public static function showOptionDialog(text:String,dialogTitle:String, scyDesktop:ScyDesktop, yesAction:function(), noAction:function()):Void{
    showOptionDialog(DialogType.YES_NO_DIALOG,text,dialogTitle, DEFAULT_WIDTH, scyDesktop, true, true, yesAction, noAction);
} 

public static function showOptionDialog(params:DialogBoxParams):Void{
    showOptionDialog(params.dialogType,params.text,params.title, params.dialogWidth, params.scyDesktop, params.modal, params.indicateFocus, params.okAction, params.noAction);
}



public class DialogBox extends CustomNode {

    public var content: Node;

    public  var targetScene: Scene;
    var addedAsScyWindow:Boolean = false;
    public var title: String;
    public var eloIcon: EloIcon;
    public var windowColorScheme: WindowColorScheme;
    public var closeAction: function(): Void;
    public-init var modal: Boolean = true;
    public-init var indicateFocus: Boolean = true;
    public-init var dialogType: Integer = JOptionPane.OK_OPTION;
    public-init var scyDesktop:ScyDesktop;
    var dialogWindow: ScyWindow;

    init {
       if (content!=null){
         FX.deferAction(place);
       }
    }

    public override function create(): Node {
        dialogWindow = StandardScyWindow {
            eloUri:null;
            eloType:null;
            scyContent: bind content;
            title: title
            eloIcon: eloIcon;
            windowColorScheme: windowColorScheme
            closedAction: function (window: ScyWindow) {
                close();
            }
            allowMinimize: false
            activated: true
        }
        dialogWindow.open();
       
        //scyDesktop.effect = glow;
        if(indicateFocus){
             /*def glow:Glow = Glow{
            level:0.5
        }*/
        def outerGlow:DropShadow = DropShadow{
            //color:Color.LIGHTYELLOW;
            //color:Color.LIGHTYELLOW;
            color:Color.rgb(243, 243, 150);
            spread:0.5
            radius:40
        }
         dialogWindow.effect  = outerGlow;
        Timeline{
             keyFrames: [
                     at (0.25s) {outerGlow.radius => 40.0 tween Interpolator.EASEIN},
                    at (0.5s) {outerGlow.radius => 0.0 tween Interpolator.EASEIN},
                    at (0.75s) {outerGlow.radius => 40.0 tween Interpolator.EASEIN},
                    at (1s) {outerGlow.radius => 0.0 tween Interpolator.EASEIN}]
            }.play();

        }
        return Group {
                    content: [
                        if (modal) {
                            Rectangle {
                                blocksMouse: true
                                x: 0, y: 0
                                width: bind scene.width, height: bind scene.height
                                fill: Color.color(1.0, 1.0, 1.0, 0.5)
                                onKeyPressed: function (e: KeyEvent): Void {
                                }
                                onKeyReleased: function (e: KeyEvent): Void {
                                }
                                onKeyTyped: function (e: KeyEvent): Void {
                                } }
                        } else []//,
                    //dialogWindow
                    ]
                };
    }

    public function close(): Void {
        if (not addedAsScyWindow) {
            var sceneContentList = scene.content;
            delete this from sceneContentList;
            delete dialogWindow from sceneContentList;
            scene.content = sceneContentList;
            closeAction();
        } else {
            scyDesktop.windows.removeScyWindow(dialogWindow);
            closeAction();
        }
    }

    public function place(): Void {
        var sceneContentList = targetScene.content;
        if (modal) {
            insert this into sceneContentList;
            insert dialogWindow into sceneContentList;
            targetScene.content = sceneContentList;
            this.requestFocus();
        } else {
            if (not (scyDesktop == null)) {
                targetScene.content = sceneContentList;
                scyDesktop.windows.addScyWindow(dialogWindow);
                scyDesktop.windows.activateScyWindow(dialogWindow);
                addedAsScyWindow = true;
            }
        }
        center();
    }

    function center() {
        if (modal) {
            dialogWindow.layoutX = scene.width / 2 - dialogWindow.layoutBounds.width / 2;
            dialogWindow.layoutY = scene.height / 2 - dialogWindow.layoutBounds.height / 2;
        } else if (not (scyDesktop == null)) {
            dialogWindow.layoutX = scyDesktop.scene.width / 2 - dialogWindow.layoutBounds.width / 2;
            dialogWindow.layoutY = scyDesktop.scene.height / 2 - dialogWindow.layoutBounds.height / 2;
        }
    }

}

public function placeInDialogBox(content: Node, scene: Scene): DialogBox {
    var DialogBox = DialogBox {
                content: content;
            }
    var sceneContentList = scene.content;
    insert DialogBox into sceneContentList;
    scene.content = sceneContentList;
    return DialogBox;
}

public function placeInDialogBox(content: Node[], scene: Scene): DialogBox {
    return placeInDialogBox(Group { content: content }, scene);
}
