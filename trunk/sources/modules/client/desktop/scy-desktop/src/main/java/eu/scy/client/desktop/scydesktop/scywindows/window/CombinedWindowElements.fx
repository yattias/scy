/*
 * CombinedWindowElements.fx
 *
 * Created on 9-apr-2010, 15:31:53
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Cursor;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import java.lang.UnsupportedOperationException;
import java.lang.String;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.uicontrols.test.ruler.RulerRectangle;
import eu.scy.client.desktop.scydesktop.uicontrols.test.ruler.ResizableRulerRectangle;
import eu.scy.client.desktop.scydesktop.art.ArtSource;
import eu.scy.client.desktop.scydesktop.art.FxdImageLoader;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.FxdEloIcon;
import org.apache.log4j.Logger;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.TestAttribute;
import eu.scy.client.desktop.scydesktop.art.ScyColors;

/**
 * @author sikken
 */
public class CombinedWindowElements extends ScyWindow {

   def logger = Logger.getLogger(this.getClass());
   def scyWindowAttributeDevider = 3.0;
   public override var scyWindowAttributes on replace {
         placeAttributes()
      };
   public override var eloIcon on replace {
         eloIconChanged()
      };
   def drawerGroup: Group = Group {
         visible: bind not isClosed and not isMinimized
      };
   var topDrawer: TopDrawer;
   var rightDrawer: RightDrawer;
   var bottomDrawer: BottomDrawer;
   var leftDrawer: LeftDrawer;
   public override var topDrawerTool on replace { setTopDrawer() };
   public override var rightDrawerTool on replace { setRightDrawer() };
   public override var bottomDrawerTool on replace { setBottomDrawer() };
   public override var leftDrawerTools on replace { setLeftDrawer() };
   var emptyWindow: EmptyWindow;
   var contentElement: WindowContent;
   var windowTitleBar: WindowTitleBarDouble;
   var resizeElement: WindowResize;
   var rotateElement: WindowRotate;
   var closeElement: WindowClose;
   var windowStateControls: WindowStateControls;
   var closedWindow: ClosedWindow;
   var mainContentGroup: Group;
   def borderWidth = 2.0;
   def controlSize = 10.0;
   def cornerRadius = 10.0;
   def separatorLength = 7.0;
   def titleBarTopOffset = borderWidth / 2 + 3;
   def titleBarLeftOffset = borderWidth / 2 + 5;
   def titleBarRightOffset = titleBarLeftOffset;
   def closeBoxSize = 10.0;
   def iconSize = 40.0;
   def closedHeight = titleBarTopOffset + iconSize + borderWidth + 1;
   def controlStrokeWidth = 0.0;
   def contentSideBorder = 5.0;
   def contentTopOffset = titleBarLeftOffset + iconSize + contentSideBorder;
   def deltaWidthContentWidth = borderWidth + 2 * contentSideBorder + 1;
   def deltaHeightContentHeight = contentTopOffset + borderWidth / 2 + controlSize;
   def contentWidth = bind width - deltaWidthContentWidth;
   def contentHeight = bind height - deltaHeightContentHeight;
   def drawerCornerOffset = controlSize + separatorLength;

   postinit {
      if (isClosed) {
         height = closedHeight;
      }
      closedBoundsWidth = minimumWidth + deltaWidthContentWidth;
      closedBoundsHeight = closedHeight + deltaHeightContentHeight;
      setTopDrawer();
      setRightDrawer();
      setBottomDrawer();
      setLeftDrawer();
      this.cache = true;
   }

    override public function removeChangesListener (wcl : WindowChangesListener) : Void {
        throw new UnsupportedOperationException('Not implemented yet');
    }

    override public function addChangesListener (wcl : WindowChangesListener) : Void {
        throw new UnsupportedOperationException('Not implemented yet');
    }
    override public function acceptDrop (object : Object) : Void {
        throw new UnsupportedOperationException('Not implemented yet');
    }

    override public function canAcceptDrop (object : Object) : Boolean {
        throw new UnsupportedOperationException('Not implemented yet');
    }


    override public function openDrawer (which : String) : Void {
        throw new UnsupportedOperationException('Not implemented yet');
    }

   override public function close(): Void {
      throw new UnsupportedOperationException('Not implemented yet');
   }

   override public function openBoundWindow(width: Number, height: Number): Void {
       throw new UnsupportedOperationException('Not implemented yet');
   }

   override public function openWindow(width: Number, height: Number): Void {
      openWindow(layoutX, layoutY, width, height);
   }

   override public function openWindow(posX: Number, posY: Number, width: Number, height: Number): Void {
      openWindow(posX, posY, width, height, rotate);
   }

   override public function openWindow(posX: Number, posY: Number, width: Number, height: Number, rotation: Number): Void {
        openWindow(posX, posY, width, height, rotate, false);
   }

   override function openWindow(posX: Number, posY: Number, openWidth: Number, openHeight: Number, rotation: Number, hideDrawersAfterOpenning: Boolean): Void {
      isClosed = false;
      this.layoutX = posX;
      this.layoutY = posY;
      this.width = width;
      this.height = height;
      this.rotate = rotation;
   }

   function placeAttributes() {
      var sortedScyWindowAttributes =
         Sequences.sort(scyWindowAttributes, null) as ScyWindowAttribute[];
      var x = 0.0;
      for (scyWindowAttribute in reverse sortedScyWindowAttributes) {
         scyWindowAttribute.translateX = x;
         x += scyWindowAttribute.boundsInLocal.width;
         x += scyWindowAttributeDevider;
      }

   }

   function eloIconChanged():Void{
      closedWindow.eloIcon = eloIcon;
      windowTitleBar.eloIcon = eloIcon.clone();
   }


   function setTopDrawer() {
      if (drawerGroup == null) {
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (topDrawer != null) {
         delete topDrawer from drawerGroup.content;
         topDrawer = null;
      }
      if (topDrawerTool != null) {
         topDrawer = TopDrawer {
               windowColorScheme: windowColorScheme
               //            color:bind drawerColor;
               //            highliteColor:controlColor;
               //            closedSize:bind width-2*drawerCornerOffset;
               //            closedStrokeWidth:controlStrokeWidth;
               content: topDrawerTool;
               activated: bind activated;
               //            activate: activate;
               layoutX: drawerCornerOffset;
               layoutY: 0;
               width: bind width - 2 * drawerCornerOffset
            }
         insert topDrawer into drawerGroup.content;
      }
   //      scyToolsList.topDrawerTool = topDrawerTool;
   }

   function setRightDrawer() {
      if (drawerGroup == null) {
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (rightDrawer != null) {
         delete rightDrawer from drawerGroup.content;
         rightDrawer = null;
      }
      if (rightDrawerTool != null) {
         rightDrawer = RightDrawer {
               windowColorScheme: windowColorScheme
               //            color:bind drawerColor;
               //            highliteColor:controlColor;
               //            closedStrokeWidth:controlStrokeWidth;
               //            closedSize:bind height-2*drawerCornerOffset;
               content: rightDrawerTool;
               activated: bind activated;
               //            activate: activate;
               layoutX: bind width;
               layoutY: drawerCornerOffset;
               height: bind height - 2 * drawerCornerOffset
            }
         insert rightDrawer into drawerGroup.content;
      }
   //      scyToolsList.rightDrawerTool = rightDrawerTool;
   }

   function setBottomDrawer() {
      if (drawerGroup == null) {
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (bottomDrawer != null) {
         delete bottomDrawer from drawerGroup.content;
         bottomDrawer = null;
      }
      if (bottomDrawerTool != null) {
         //println("new BottomDrawer with color {drawerColor}");
         bottomDrawer = BottomDrawer {
               windowColorScheme: windowColorScheme
               //            color:bind drawerColor;
               //            highliteColor:controlColor;
               //            closedSize:bind width-2*drawerCornerOffset;
               //            closedStrokeWidth:controlStrokeWidth;
               content: bottomDrawerTool;
               activated: bind activated;
               //            activate: activate;
               layoutX: drawerCornerOffset;
               layoutY: bind height;
               width: bind width - 2 * drawerCornerOffset
            }
         insert bottomDrawer into drawerGroup.content;
      }
   //      scyToolsList.bottomDrawerTool = bottomDrawerTool;
   }

   function setLeftDrawer() {
      if (drawerGroup == null) {
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (leftDrawer != null) {
         delete leftDrawer from drawerGroup.content;
         leftDrawer = null;
      }
      if (sizeof leftDrawerTools > 0) {
         leftDrawer = LeftDrawer {
               windowColorScheme: windowColorScheme
               //            color:bind drawerColor;
               //            highliteColor:controlColor;
               //            closedStrokeWidth:controlStrokeWidth;
               //            closedSize:bind height-2*drawerCornerOffset;
               content: leftDrawerTools[0];
               activated: bind activated;
               //            activate: activate;
               layoutX: 0;
               layoutY: drawerCornerOffset;
               height: bind height - 2 * drawerCornerOffset
            }
         insert leftDrawer into drawerGroup.content;
      }
   //      scyToolsList.leftDrawerTool = leftDrawerTool;
   }

   public override function create(): Node {
      blocksMouse = true;

      emptyWindow = EmptyWindow {
            width: bind width;
            height: bind height;
            controlSize: cornerRadius;
            borderWidth: borderWidth;
            windowColorScheme: windowColorScheme
         }

      contentElement = WindowContent {
            width: bind contentWidth;
            height: bind contentHeight;
            windowColorScheme: windowColorScheme
            content: bind scyContent;
            activated: bind activated;
            //         activate: activate;
            layoutX: borderWidth / 2 + 1 + contentSideBorder;
            layoutY: contentTopOffset;
         }

      windowStateControls = WindowStateControls{
         windowColorScheme:bind windowColorScheme
         enableRotateNormal:bind rotate!=0.0
         enableMinimize: bind allowMinimize and not isClosed
      }

      windowTitleBar = WindowTitleBarDouble {
            width: bind width + borderWidth
            //         iconSize:iconSize;
            //         iconGap:iconGap;
//            closeBoxWidth: bind if (closeElement.visible) closeBoxSize + 2 * borderWidth else 0.0;
            iconSize: iconSize
            windowStateControls: windowStateControls
            title: bind title;
            eloIcon: eloIcon;
            activated: bind activated
            windowColorScheme: windowColorScheme
            layoutX: -borderWidth / 2;
            layoutY: 0;
         }

      resizeElement = WindowResize {
            visible: bind allowResize or isClosed;
            size: controlSize;
            borderWidth: borderWidth;
            separatorLength: separatorLength
            windowColorScheme: windowColorScheme
            //         activate: activate;
            //         startResize:startResize;
            //         doResize:doResize;
            //         stopResize:stopResize;
            layoutX: bind width//+controlBorderOffset+controlStrokeWidth;
            layoutY: bind height//+controlBorderOffset+controlStrokeWidth;
         }

      rotateElement = WindowRotate {
            visible: bind allowRotate;
            size: controlSize;
            borderWidth: borderWidth;
            separatorLength: separatorLength
            windowColorScheme: windowColorScheme
            //         activate: activate;
            rotateWindow: this;
            layoutX: 0;
            layoutY: bind height;
         }

      closeElement = WindowClose {
            visible: bind allowClose and not isClosed;
            size: closeBoxSize;
            //         strokeWidth:controlStrokeWidth;
            windowColorScheme: windowColorScheme
            //         activate: activate;
            activated: false
            //         closeAction:doClose;
            layoutX: bind width + 0.5 * borderWidth - closeBoxSize// - 1;
            layoutY: 1.5*borderWidth// + closeBoxSize / 2;
         }

      def closedGroup = Group{
         visible: bind isClosed
         content:[
            closedWindow = ClosedWindow{
               window: this
               windowColorScheme: bind windowColorScheme
//               scyElo: bind scyElo
               eloIcon: eloIcon
               activated: bind activated
               title: bind title
            }
            ClosedWindowResize {
               //                  visible: bind allowResize or isClosed;
               size: controlSize;
               borderWidth: borderWidth;
               separatorLength: separatorLength
               windowColorScheme: windowColorScheme
               //         activate: activate;
               //         startResize:startResize;
               //         doResize:doResize;
               //         stopResize:stopResize;
               layoutX: ArtSource.thumbnailWidth + ThumbnailView.eloIconOffset
               layoutY: ArtSource.thumbnailWidth
            }
         ]
      }

      def openGroup = Group{
         visible: bind not isClosed
         content:[
                  emptyWindow,
                  contentElement,
                  drawerGroup,
                  windowTitleBar,
                  resizeElement,
                  rotateElement,

                  Group { // the scy window attributes
                     layoutX: iconSize + 5
                     layoutY: 19
                     content: bind scyWindowAttributes,
                  },
         ]
      }

      eloIconChanged();

      return mainContentGroup = Group {
               cursor: Cursor.MOVE;
               cache: true;
               content: [
                  closedGroup,
                  openGroup
//                  emptyWindow,
//                  contentElement,
//                  drawerGroup,
//                  windowTitleBar,
//                  minimizeElement,
//                  resizeElement,
//                  rotateElement,
//                  Group { // the scy window attributes
//                     layoutX: iconSize + 5
//                     layoutY: 19
//                     content: bind scyWindowAttributes,
//                  },
               //            draggingLayer,
               //            Group {
               //               content: [circleLayer]
               //               effect: DropShadow {
               //                  offsetX: 3
               //                  offsetY: 3
               //                  color: Color.BLACK
               //                  radius: 10
               //               }
               //               onMouseReleased: function( e: MouseEvent ):Void {
               //                  println("G entered");
               //               }
               //            }

               ]
            };
   }

}
var imageLoader = ImageLoader.getImageLoader();

function loadEloIcon(type: String): EloIcon {
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def imageLoader = FxdImageLoader {
         sourceName: ArtSource.plainIconsPackage
      };
   var name = EloImageInformation.getIconName(type);
//   println("name: {name}");
   FxdEloIcon {
      visible: true
      fxdNode: imageLoader.getNode(name)
      windowColorScheme: windowColorScheme
   }
}

function run() {
   //   windowColorScheme= highcontrastColorScheme;

   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   var openWindow = CombinedWindowElements {
         windowColorScheme: windowColorScheme
         eloIcon: loadEloIcon("scy/mapping")
         title: "opened and not selected"
         isClosed: false
         allowClose: false
         scyContent: Rectangle {
            x: 0, y: 0
            width: 200, height: 200
            fill: Color.RED
         }
         scyWindowAttributes: [
               TestAttribute{
                  radius: 8
               }
            ]
         layoutX: 10
         layoutY: 20
      }

   var openWindowSelected = CombinedWindowElements {
         windowColorScheme: windowColorScheme
         eloIcon: loadEloIcon("scy/mapping")
         title: "opened and selected"
         scyContent: Rectangle {
            x: 0, y: 0
            width: 200, height: 200
            fill: Color.WHITE
         }
         isClosed: false
         allowClose: false
         allowResize: true
         activated: true
         layoutX: 190
         layoutY: 20
      }

   var openWindowWithClose = CombinedWindowElements {
         windowColorScheme: windowColorScheme
         eloIcon: loadEloIcon("scy/mapping")
         title: "open and not selected"
         scyContent: RulerRectangle {
            xSize: 200
            ySize: 200
         }
         isClosed: false
         allowClose: true
         allowResize: true
         layoutX: 10
         layoutY: 150
      }

   var openWindowSelectedWithClose = CombinedWindowElements {
         windowColorScheme: windowColorScheme
         eloIcon: loadEloIcon("scy/mapping")
         title: "opened and selllllll"
         scyContent: ResizableRulerRectangle {
            width: 200, height: 200
         }
         isClosed: false
         allowClose: true
         activated: true
         layoutX: 190
         layoutY: 150
      }

   def minimumWidth = 100;

   var closedWindow = CombinedWindowElements {
         windowColorScheme: windowColorScheme
         eloIcon: loadEloIcon("scy/mapping")
         title: "closed and not selected"
         isClosed: true
         allowClose: true
         height: openWindow.closedHeight
         width: minimumWidth
         layoutX: 40
         layoutY: 280
      }

   var closedWindow2 = CombinedWindowElements {
         windowColorScheme: windowColorScheme
         eloIcon: loadEloIcon("scy/mapping")
         title: "closed and selected"
         isClosed: true
//         isMinimized: false
         allowClose: true
//         allowMinimize: false
         activated: true
         height: openWindow.closedHeight
         width: minimumWidth
         layoutX: closedWindow.layoutX + minimumWidth + 20
         layoutY: 280
      }

   var closedWindow3 = CombinedWindowElements {
         windowColorScheme: windowColorScheme
         eloIcon: loadEloIcon("scy/mapping")
         title: "closed and not selected"
         isClosed: false
         isMinimized: true
         allowClose: true
         allowMinimize: true
         height: openWindow.closedHeight
         width: 90
         layoutX: closedWindow2.layoutX + minimumWidth + 20
         layoutY: 280
      }

//   openWindowWithClose.windowColorScheme = highcontrastColorScheme;
   Stage {
      title: "Test of Combined window elements"
      scene: Scene {
         width: 400
         height: 350
         fill: Color.web("#eaeaea");
         content: [
            openWindow,
            openWindowSelected,
            openWindowWithClose,
            openWindowSelectedWithClose,
            closedWindow,
            closedWindow2,
            closedWindow3
         ]
      }
   }

}
