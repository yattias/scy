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
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javax.swing.JOptionPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.scene.effect.DropShadow;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import javafx.animation.KeyFrame;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;

/**
 * @author sven
 */
public static def DEFAULT_WIDTH: Double = 400;
public static def HGAP: Double = 15;
public static def VGAP: Double = 15;
public static def openDialogs: HashMap = new HashMap();
public static var windowStyler: WindowStyler on replace {loadDefaults()};
public static var scyDesktop:ScyDesktop;
public static var dialogScene:Scene;
def infoWindowEloIconName = "alert_message";
def questionWindowEloIconName = "alert_question";
def infoEloIconName = "information2";
def questionEloIconName = "assignment";
def eloIconFactory = EloIconFactory{};
var infoWindowEloIcon: EloIcon = eloIconFactory.createEloIcon(infoWindowEloIconName);
var questionWindowEloIcon: EloIcon = eloIconFactory.createEloIcon(questionWindowEloIconName);
var infoEloIcon: EloIcon = eloIconFactory.createEloIcon(infoEloIconName);
var questionEloIcon: EloIcon = eloIconFactory.createEloIcon(questionEloIconName);
var modalWindowColorScheme: WindowColorScheme = WindowColorScheme{};

function loadDefaults():Void{
   if (windowStyler!=null){
      infoWindowEloIcon = windowStyler.getScyEloIcon(infoWindowEloIconName);
      questionWindowEloIcon = windowStyler.getScyEloIcon(questionWindowEloIconName);
      modalWindowColorScheme = windowStyler.getWindowColorScheme("general/neww");
      infoWindowEloIcon.selected = true;
      questionWindowEloIcon.selected = true;
      infoEloIcon = windowStyler.getScyEloIcon(infoEloIconName);
      questionEloIcon = windowStyler.getScyEloIcon(questionEloIconName);
   }
}

static function getDialogBoxContent(dialogWidth: Integer, dialogBox: DialogBox, dialogType: DialogType, text: String, action1: function(): Void, action2: function(): Void, action3: function(): Void): Group {

   def indicatorImage: Node = getIndicatorImage(dialogType);
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

static function getIndicatorImage(dialogType): Node {
   if (dialogType == DialogType.OK_DIALOG) {
      return infoEloIcon.clone()
   } else if (dialogType == DialogType.YES_NO_DIALOG or dialogType == DialogType.OK_CANCEL_DIALOG) {
      return questionEloIcon.clone()
   } else {
      return infoEloIcon.clone()
   }
}

static function getButtonBar(dialogBox: DialogBox, dialogType: DialogType, action1: function(): Void, action2: function(): Void, action3: function(): Void): HBox {
   if (dialogType == DialogType.OK_DIALOG) {
      return getOkButtonBar(dialogBox, action1)
   } else if (dialogType == DialogType.YES_NO_DIALOG) {
      return getYesNoButtonBar(dialogBox, action1, action2)
   } else {
      return getOkButtonBar(dialogBox, function() {
              })
   }
}

static function getOkButtonBar(dialogBox: DialogBox, okAction: function(): Void): HBox {
   def buttonBar: HBox = HBox {
              hpos: HPos.CENTER
              spacing: HGAP
              content: [
                 Button {
                    text: ##"OK!"
                    action: function(): Void {
                       okAction();
                       dialogBox.close();
                    }
                 }
              ]
           }
   return buttonBar
}

static function getYesNoButtonBar(dialogBox: DialogBox, yesAction: function(): Void, noAction: function(): Void): HBox {
   def buttonBar: HBox = HBox {
              hpos: HPos.CENTER
              spacing: HGAP
              content: [
                 Button {
                    text: ##"Yes!"
                    action: function(): Void {
                       yesAction();
                       dialogBox.close();
                    }
                 }
                 Button {
                    text: ##"No!"
                    action: function(): Void {
                       noAction();
                       dialogBox.close();
                    }
                 }
              ]
           }
   return buttonBar
}

public static function showMessageDialog(text: String, dialogTitle: String, dialogWidth: Integer, scyDesktop: ScyDesktop, modal: Boolean, indicateFocus: Boolean, okAction: function(): Void, id: String): Void {

   def dialogBox: DialogBox = DialogBox {
              //                    content: getDialogBoxContent(dialogWidth,dialogBox, DialogType.OK_DIALOG,text, okAction, function(){}, function(){})
              targetScene: scyDesktop.scene
              eloIcon: infoWindowEloIcon.clone()
              title: dialogTitle
              dialogid: id
              modal: modal
              indicateFocus: indicateFocus
//              scyDesktop: scyDesktop
              closeAction: function() {

              }
              windowColorScheme: modalWindowColorScheme
           };
   dialogBox.content = getDialogBoxContent(dialogWidth, dialogBox, DialogType.OK_DIALOG, text, okAction, function() {}, function() {});
   dialogBox.place();
}

public static function showMessageDialog(text: String, dialogTitle: String, scyDesktop: ScyDesktop, okAction: function(): Void, id: String): Void {
   showMessageDialog(text, dialogTitle, DEFAULT_WIDTH, scyDesktop, true, true, okAction, id);
}

public static function showMessageDialog(params: DialogBoxParams, id: String): Void {
   showMessageDialog(params.text, params.title, params.dialogWidth, params.scyDesktop, params.modal, params.indicateFocus, params.okAction, id);
}

public static function showOptionDialog(dialogType: DialogType, text: String, dialogTitle: String, dialogWidth: Integer, scyDesktop: ScyDesktop, modal: Boolean, indicateFocus: Boolean, okAction: function(): Void, cancelAction: function(): Void, id: String): Void {
   def supportedOptionTypes = [DialogType.OK_CANCEL_DIALOG, DialogType.YES_NO_DIALOG];
   def dialogBox: DialogBox = DialogBox {
              //                    content: if (sizeof supportedOptionTypes[n|n==dialogType] > 0) {
              //                                getDialogBoxContent(dialogWidth, dialogBox, dialogType,text, okAction, cancelAction, function(){})
              //                            } else { //Default OptionPane Type
              //                                getDialogBoxContent(dialogWidth, dialogBox, DialogType.OK_CANCEL_DIALOG,text, okAction, cancelAction, function(){})
              //                                }
              targetScene: scyDesktop.scene
              eloIcon: questionWindowEloIcon.clone()
              title: dialogTitle
              dialogid: id
              modal: modal
              indicateFocus: indicateFocus
//              scyDesktop: scyDesktop
              closeAction: function() {

              }
              windowColorScheme: modalWindowColorScheme
           };
   dialogBox.content = if (sizeof supportedOptionTypes[n | n == dialogType] > 0) {
              getDialogBoxContent(dialogWidth, dialogBox, dialogType, text, okAction, cancelAction, function() {})
           } else { //Default OptionPane Type
              getDialogBoxContent(dialogWidth, dialogBox, DialogType.OK_CANCEL_DIALOG, text, okAction, cancelAction, function() {})
           };
   dialogBox.place();
//        FX.deferAction(dialogBox.place);
}

public static function showOptionDialog(text: String, dialogTitle: String, scyDesktop: ScyDesktop, yesAction: function(): Void, noAction: function(): Void, id: String): Void {
   showOptionDialog(DialogType.YES_NO_DIALOG, text, dialogTitle, DEFAULT_WIDTH, scyDesktop, true, true, yesAction, noAction, id);
}

public static function showOptionDialog(params: DialogBoxParams, id: String): Void {
   showOptionDialog(params.dialogType, params.text, params.title, params.dialogWidth, params.scyDesktop, params.modal, params.indicateFocus, params.okAction, params.noAction, id);
}

public static function closeDialog(id: String): Boolean {
   def dialog: DialogBox = openDialogs.get(id) as DialogBox;
   if (dialog != null) {
      dialog.close();
      return true;
   } else {
      return false;
   }
}

public class DialogBox extends CustomNode {

   public var content: Node;
   public var targetScene: Scene;
   var addedAsScyWindow: Boolean = false;
   public var title: String;
   public var eloIcon: EloIcon;
   public var windowColorScheme: WindowColorScheme;
   public var closeAction: function(): Void;
   public-init var modal: Boolean = true;
   public-init var indicateFocus: Boolean = true;
   public-init var dialogType: Integer = JOptionPane.OK_OPTION;
//   public-init var scyDesktop: ScyDesktop;
   public-init var dialogid: String;
   var dialogWindow: ScyWindow;

   postinit {
      if (content != null) {
         FX.deferAction(place);
      }
   }

   public override function create(): Node {
      dialogWindow = StandardScyWindow {
                 eloUri: null;
                 scyElo: null;
                 eloType: null;
                 scyContent: bind content;
                 title: title
                 eloIcon: eloIcon;
                 windowColorScheme: windowColorScheme
                 closedAction: function(window: ScyWindow) {
                    close();
                 }
                 allowMinimize: false
                 allowMaximize:false
                 allowCenter:false
                 allowClose: false
                 activated: true
              }
      dialogWindow.visible = false;
      //        dialogWindow.open();

      //scyDesktop.effect = glow;
      if (indicateFocus) {
         /*def glow:Glow = Glow{
         level:0.5
         }*/
         def outerGlow: DropShadow = DropShadow {
                    //color:Color.LIGHTYELLOW;
                    //color:Color.LIGHTYELLOW;
                    color: Color.rgb(243, 243, 150);
                    spread: 0.5
                    radius: 40
                 }
         dialogWindow.effect = outerGlow;
         Timeline {
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
                          fill: ArtSource.dialogBlockLayerColor
                          onKeyPressed: function(e: KeyEvent): Void {
                          }
                          onKeyReleased: function(e: KeyEvent): Void {
                          }
                          onKeyTyped: function(e: KeyEvent): Void {
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
      openDialogs.remove(dialogid);
   }

   public function place(): Void {
      def targetScene = dialogScene;
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
      openDialogs.put(dialogid, this);
      center();
      FX.deferAction(dialogWindow.open);
      Timeline {
         repeatCount: 1
         keyFrames: [
            KeyFrame {
               time: 50ms
               action: function(): Void {
                  dialogWindow.visible = true;
               }

            }
         ];
      }.play();
   }

   function center() {
      def windowWidth = content.layoutBounds.width;
      def windowHeight = dialogWindow.layoutBounds.height + content.layoutBounds.height;
      if (modal) {
         dialogWindow.layoutX = scene.width / 2 - windowWidth / 2;
         dialogWindow.layoutY = scene.height / 2 - windowHeight / 2;
      } else if (not (scyDesktop == null)) {
         dialogWindow.layoutX = scyDesktop.scene.width / 2 - windowWidth / 2;
         dialogWindow.layoutY = scyDesktop.scene.height / 2 - windowHeight / 2;
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
