/*
 * StandardScyWindow.fx
 *
 * Created on 2-sep-2009, 14:57:11
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.scywindows.TestAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.scene.shape.Rectangle;
import javafx.util.Math;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.layout.Container;
import javafx.scene.CacheHint;
import eu.scy.client.desktop.scydesktop.owner.OwnershipManager;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.Context;
import eu.scy.client.desktop.desktoputils.XFX;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.OnlineState;
import eu.scy.client.desktop.scydesktop.dialogs.DragBuddyDialog;
import eu.scy.actionlogging.api.IAction;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.actionlogging.api.ContextConstants;

/**
 * @author sikkenj
 */
public class StandardScyWindow extends ScyWindow {

    def logger = Logger.getLogger(this.getClass());
    def scyWindowAttributeDevider = 3.0;
    public override var title = "???";
    public override var eloType = "?123";
    public override var eloUri on replace oldEloUri {
                missionModelFX.eloUriChanged(oldEloUri, eloUri);
                titleBarBuddies.buddiesChanged();
            };
    public override var width = 150 on replace oldWidth {
                //                    println("before width from {oldWidth} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
                //              JAKOB: Why did you do this? It freaked the layouting....
                //              if (not isAnimating) {
                //                 width = limitSize(width, height).x;
                //              }
                realWidth = width;
            //                 println("after width from {oldWidth} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
            };
    public override var height = 100 on replace oldHeight {
                //                    println("before height from {oldHeight} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
                //              JAKOB: Why did you do this? It freaked the layouting....
                //              if (not isAnimating) {
                //                 if (isClosed or isMinimized) {
                //                    height = closedHeight;
                //                 } else {
                //                    height = limitSize(width, height).y;
                //                 }
                //              }
                realHeight = height;
            //                 println("after height from {oldHeight} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
            };
    public override var widthHeightProportion = -1.0;
    public override var scyContent on replace {
                scyContentChanged();
            };
    public override var eloIcon on replace {
                eloIconChanged()
            };
    public override var scyToolsList = ScyToolsList {};
    public override var activated on replace { activeStateChanged() };
    public override var scyWindowAttributes on replace {
                placeAttributes()
            };
    public override var allowRotate = true;
    public override var allowResize = true;
    public override var allowDragging = true;
    public override var allowClose = true;
    public override var allowMinimize = true;
    public override var windowEffect;
    // status variables
    var isAnimating = false;
    // layout constants
    def borderWidth = 2.0;
    def controlSize = 10.0;
    def cornerRadius = 10.0;
    def titleBarTopOffset = borderWidth / 2 + 3;
    def titleBarLeftOffset = borderWidth / 2 + 5;
    def iconSize = 40.0;
    def separatorLength = 7.0;
    def closedHeight = titleBarTopOffset + iconSize + borderWidth + 1;
    def drawerCornerOffset = controlSize + 1.5 * separatorLength;
    def leftRightDrawerOffset = 24.0;
    def contentSideBorder = 5.0;
    def contentTopOffset = titleBarTopOffset + iconSize + contentSideBorder;
    def deltaContentWidth = borderWidth + 2 * contentSideBorder + 1;
    def deltaContentHeight = contentTopOffset + borderWidth / 2 + controlSize;
    def contentWidth = bind realWidth - deltaContentWidth;
    def contentHeight = bind realHeight - deltaContentHeight;
    var realWidth: Number;
    var realHeight: Number;
    var originalX: Number;
    var originalY: Number;
    var originalW: Number;
    var originalH: Number;
    var maxDifX: Number;
    var maxDifY: Number;
    var sceneTopLeft: Point2D;
    var beingDragged = false;
    var hasBeenDragged = false;
    def animationDuration = 300ms;
    public override var minimumHeight = 50 on replace {
                minimumHeight = Math.max(minimumHeight, contentTopOffset + 2 * controlSize);
            }
    public override var minimumWidth = 120 on replace {
                minimumWidth = Math.max(minimumWidth, 2 * borderWidth + 3 * controlSize);
            }
    var emptyWindow: EmptyWindow;
    var windowTitleBar: WindowTitleBarDouble;
    var resizeElement: WindowResize;
    var rotateElement: WindowRotate;
    var windowStateControls: WindowStateControls;
    var titleBarWindowAttributes: TitleBarWindowAttributes;
    var titleBarBuddies: TitleBarBuddies;
    var closedWindowBuddies: TitleBarBuddies;
    var titleBarButtons: TitleBarButtons;
    var contentElement: WindowContent;
    var closedWindow: ClosedWindow;
    var hideDrawers: Boolean;
    def drawerGroup: Group = Group {
                visible: bind not isClosed and not isMinimized and not hideDrawers and allowDrawers;
            };
    var topDrawer: TopDrawer;
    var rightDrawer: RightDrawer;
    var bottomDrawer: BottomDrawer;
    var leftDrawers: LeftDrawer[];
    public override var topDrawerTool on replace { setTopDrawer() };
    public override var rightDrawerTool on replace { setRightDrawer() };
    public override var bottomDrawerTool on replace { setBottomDrawer() };
    public override var leftDrawerTools on replace oldValues { setLeftDrawer(oldValues) };
    public var missionModelFX: MissionModelFX;
    public var windowStyler: WindowStyler;
    var mainContentGroup: Group;
    var changesListeners: WindowChangesListener[]; //WindowChangesListener are stored here. youse them to gain more control over ScyWindow events.
    var eloFinishedButton: TitleBarButton;

    postinit {
        if (isClosed) {
            height = closedHeight;
        }
        closedBoundsWidth = minimumWidth + deltaContentWidth;
        closedBoundsHeight = closedHeight + deltaContentHeight;
        setTopDrawer();
        setRightDrawer();
        setBottomDrawer();
        setLeftDrawer([]);
        this.cache = true;
    }

    function scyContentChanged() {
        if (scyContent instanceof Parent) {
            (scyContent as Parent).layout();
        }
        var newContentWidth = Container.getNodePrefWidth(scyContent);
        var newContentHeight = Container.getNodePrefHeight(scyContent);
        if (scyContent instanceof Resizable) {
            if (desiredWidth > 0 and desiredHeight > 0) {
                newContentWidth = desiredWidth - deltaContentWidth;
                newContentHeight = desiredHeight - deltaContentHeight;
            } else if (desiredContentWidth > 0 and desiredContentHeight > 0) {
                newContentWidth = desiredContentWidth;
                newContentHeight = desiredContentHeight;
            }
            allowResize = allowResize and true;
        } else {
            allowResize = false;
        }
        if (not isClosed) {
            FX.deferAction(function(): Void {
                var limittedSize = limitSize(newContentWidth + deltaContentWidth, newContentHeight + deltaContentHeight);
                openBoundWindow(limittedSize.x, limittedSize.y);
            });
        }
        scyToolsList.windowContentTool = scyContent;
    }

    function limitSize(w: Number, h: Number): Point2D {
        var limittedWidth = Math.max(w, Math.max(minimumWidth, windowTitleBar.minimumWidth));
        var limittedHeight = Math.max(h, minimumHeight);
        if (scyContent != null) {
            //         println("limitSize(): Width:  {Container.getNodeMinWidth(scyContent)} - {Container.getNodePrefWidth(scyContent)} - {Container.getNodeMaxWidth(scyContent)}");
            //         println("limitSize(): Height: {Container.getNodeMinHeight(scyContent)} - {Container.getNodePrefHeight(scyContent)} - {Container.getNodeMaxHeight(scyContent)}");
            //         println("limitSize(): layout: {scyContent.layoutBounds}");
            //         println("limitSize(): local:  {scyContent.boundsInLocal}");
            // this is check on content limits, subtract "border" sizes
            limittedWidth -= deltaContentWidth;
            limittedHeight -= deltaContentHeight;
            limittedWidth = Math.max(limittedWidth, Container.getNodeMinWidth(scyContent));
            limittedHeight = Math.max(limittedHeight, Container.getNodeMinHeight(scyContent));
            limittedWidth = Math.min(limittedWidth, Container.getNodeMaxWidth(scyContent));
            limittedHeight = Math.min(limittedHeight, Container.getNodeMaxHeight(scyContent));
            // this is check on content limits, add "border" sizes
            limittedWidth += deltaContentWidth;
            limittedHeight += deltaContentHeight;
        } else {
            // no content
            limittedHeight = closedHeight;
        }
        // now limit it to the scy desktop window size
        if (scene.width > 0) {
            limittedWidth = Math.min(limittedWidth, scene.width - deltaContentWidth);
            limittedHeight = Math.min(limittedHeight, scene.height - deltaContentHeight + iconSize + controlSize);
        }

//      println("limitSize({w},{h}):{limittedWidth},{limittedHeight} of {eloUri}, with: {scyContent}");
        return Point2D {
                    x: limittedWidth;
                    y: limittedHeight
                }
    }

    function activeStateChanged() {

        if (activated) {
            scyToolsList.onGotFocus();
            windowManager.scyDesktop.edgesManager.findLinks(this);
        } else {
            scyToolsList.onLostFocus();
        }
    }

    public override function canAcceptDrop(object: Object): Boolean {
        if (object instanceof ContactFrame) {
            def c:ContactFrame = object as ContactFrame;
            if (not ownershipManager.isOwner(c.contact.name)) {
                return true;
            }
        }
        return scyToolsList.canAcceptDrop(object);
    }

    public override function acceptDrop(object: Object): Void {
        //buddy & offline -> addBuddyAsOwner
        //buddy & online ->
        //!collaborativ -> Dialog (add buddy as owner / send elo to buddy)
        //collaborativ -> Dialog (add buddy as owner / send elo to buddy / start collaboration)
        if (object instanceof ContactFrame) {
            def c: ContactFrame = object as ContactFrame;
            if (c.contact.onlineState == OnlineState.OFFLINE) {
                addBuddyAsOwner(c);
            } else if (c.contact.onlineState == OnlineState.AWAY) {
                //XXX what should happen here? For the moment we just add the buddy as owner
                addBuddyAsOwner(c);
            } else {
                //online
                DragBuddyDialog {
                    scyDesktop: windowControl.windowManager.scyDesktop
                    scyElo: scyElo
                    eloIconName: "collaboration_invitation"
                    title: ##"Dragged buddy"
                    message: "{##"You dragged a buddy on this ELO. What do you want to do?"}"
                              collaborative: scyToolsList.canAcceptDrop(object)
                    sendEloFunction: function(): Void {
                        sendEloToUser(c, scyElo);
                    }
                    addBuddyFunction: function(): Void {
                        addBuddyAsOwner(c);
                    }
                    collaborateFunction: function(): Void {
                        scyToolsList.acceptDrop(object);
                    }
                }
            }
        }
    }

    function addBuddyAsOwner(contactFrame: ContactFrame): Void {
        if (not ownershipManager.isOwner(contactFrame.contact.name)) {
            ownershipManager.addOwner(contactFrame.contact.name, true);
        }
    }

    function sendEloToUser(contactFrame: ContactFrame, scyElo: ScyElo): Void {
        if ((not (scyElo == null)) and (not (scyElo.getUri() == null))) {
            def sendEloAction: IAction = new Action();
            def proposingUser: String = windowControl.windowManager.scyDesktop.config.getToolBrokerAPI().getContextService().getUsername();
            sendEloAction.setUser(proposingUser);
            sendEloAction.setType("proposed_elo");
            sendEloAction.addContext(ContextConstants.tool, "scylab");
            sendEloAction.addAttribute("proposing_user", proposingUser);
            sendEloAction.addAttribute("proposed_user", contactFrame.contact.awarenessUser.getJid());
            sendEloAction.addAttribute("proposed_elo", scyElo.getUri().toString());
            windowControl.windowManager.scyDesktop.config.getToolBrokerAPI().getActionLogger().log(sendEloAction);
        }
    }

    override function addChangesListener(wcl: WindowChangesListener) {
        insert wcl into changesListeners;
    }

    override function removeChangesListener(wcl: WindowChangesListener) {
        delete wcl from changesListeners;
    }

    /**
     *    method added to 'catch' the startResize event
     */
    function startResize(e: MouseEvent): Void {
        // TODO: check if listener notification takes too long -> thread
        for (wcl in changesListeners) {
            wcl.resizeStarted();
        }
        startDragging(e);
    //        oldCacheValue = cache;
    //        cache = false;
    //        scyContent.cache = false;
    //        contentElement.cache = false;
    //        println("caching set from {oldCacheValue} to false");
    }

    /**
     *    method added to 'catch' the stopResize event
     */
    function stopResize(e: MouseEvent): Void {
        // TODO: check if listener notification takes too long -> thread
        for (wcl in changesListeners) {
            wcl.resizeFinished();
        }
        stopDragging(e);
    //        cache = oldCacheValue;
    //        scyContent.cache = oldCacheValue;
    //        contentElement.cache = oldCacheValue;
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

    function eloIconChanged(): Void {
        closedWindow.eloIcon = eloIcon.clone();
        windowTitleBar.eloIcon = eloIcon;
    }

    public override function open(): Void {
        checkScyContent();
        var openWidth = Container.getNodePrefWidth(scyContent);
        var openHeight = Container.getNodePrefHeight(scyContent);
        openWindow(openWidth + deltaContentWidth, openHeight + deltaContentHeight);
    }

    public override function openBoundWindow(openWidth: Number, openHeight: Number): Void {
        if (isClosed) {
            closedPosition = Point2D {
                        x: layoutX;
                        y: layoutY;
                    }
        }
        checkScyContent();
        var useSize = limitSize(openWidth, openHeight);
        cache = true;
        cacheHint = CacheHint.SCALE_AND_ROTATE;
        isAnimating = true;
        var openTimeline = Timeline {
                    keyFrames: [
                        KeyFrame {
                            canSkip: false;
                            time: animationDuration;
                            values: [
                                width => useSize.x tween Interpolator.EASEOUT,
                                height => useSize.y tween Interpolator.EASEOUT,
                            ]
                            action: function() {
                                isClosed = false;
                                //scyToolsList.onOpened();
                                updateRelativeBounds();
                                cache = false;
                                cacheHint = CacheHint.DEFAULT;
                                isAnimating = false;
                                eloFinishedButton.enabled = true;
                                finishedOpeningWindow();
                            }
                        }
                    ]
                };
        openTimeline.play();
    }

    public var finishedOpeningWindow: function(): Void;

    public override function openWindow(openWidth: Number, openHeight: Number): Void {
        openWindow(layoutX, layoutY, openWidth, openHeight);
    }

    public override function openWindow(posX: Number, posY: Number, openWidth: Number, openHeight: Number): Void {
        openWindow(posX, posY, openWidth, openHeight, rotate);
    }

    public override function openWindow(posX: Number, posY: Number, openWidth: Number, openHeight: Number, rotation: Number): Void {
        openWindow(posX, posY, openWidth, openHeight, rotation, false)
    }

    public override function openWindow(posX: Number, posY: Number, openWidth: Number, openHeight: Number, rotation: Number, hideDrawersAfterOpenning: Boolean): Void {
        if (isClosed) {
            closedPosition = Point2D {
                        x: layoutX;
                        y: layoutY;
                    }
            logger.info("Stored closed position of window {title} to {closedPosition.x} x {closedPosition.y}");
        }
        ProgressOverlay.startShowWorking();
        XFX.runActionInBackgroundAndCallBack(checkScyContent, function(result) {
            ProgressOverlay.stopShowWorking();
            desiredWidth = openWidth;
            desiredHeight = openHeight;
            var useSize: Point2D = limitSize(openWidth, openHeight);
            logger.info("Setting size of window {title} to {useSize.x} x {useSize.y}");
            var useLocation: Point2D = limitLocation(posX, posY);
            cache = true;
            cacheHint = CacheHint.SCALE_AND_ROTATE;
            isAnimating = true;
            hideDrawers = isClosed;
            isClosed = false;
            var openTimeline = Timeline {
                        keyFrames: [
                            KeyFrame {
                                canSkip: false;
                                time: animationDuration;
                                values: [
                                    layoutX => useLocation.x tween Interpolator.EASEOUT,
                                    layoutY => useLocation.y tween Interpolator.EASEOUT,
                                    width => useSize.x tween Interpolator.EASEOUT,
                                    height => useSize.y tween Interpolator.EASEOUT,
                                    rotate => rotation tween Interpolator.EASEOUT
                                ]
                                action: function() {
                                    hideDrawers = hideDrawersAfterOpenning;
                                    //scyToolsList.onOpened();
                                    updateRelativeBounds();
                                    cache = false;
                                    cacheHint = CacheHint.DEFAULT;
                                    isAnimating = false;
                                    eloFinishedButton.enabled = true;
                                    finishedOpeningWindow();
                                }
                            }
                        ]
                    };
            openTimeline.play();
        });
    }

    /**
     *  this function limits the position not to be negative or bigger
     *  than the window size, i.e., prevents a location outside the visible area
     */
    function limitLocation(posX: Number, posY: Number): Point2D {
        var newX = Math.max(0, posX);
        newX = Math.min(newX, scene.width - 10);

        var newY = Math.max(0, posY);
        newY = Math.min(newY, scene.height - 10);

        return Point2D {
                    x: newX
                    y: newY
                }
    }

    function checkScyContent() {
        //println("checkScyContent: scyContent: {scyContent==null}, setScyContent: {setScyContent!=null}");
        if (not isScyContentSet and scyContent == null and setScyContent != null) {
            setScyContent(this)
        }
    }

    function getCloseTimeline(): Timeline {
        return Timeline {
                    keyFrames: [
                        KeyFrame {
                            time: animationDuration;
                            values: [
                                width => minimumWidth tween Interpolator.EASEBOTH,
                                height => closedHeight tween Interpolator.EASEBOTH,
                                layoutX => closedPosition.x tween Interpolator.EASEOUT,
                                layoutY => closedPosition.y tween Interpolator.EASEOUT,
                                rotate => 0
                            ]
                            action: function() {
                                isClosed = true;
                                hideDrawers = false;
                                isCentered = false;
                                if (closedAction != null) {
                                    closedAction(this);
                                }
                                isAnimating = false;
                                scyToolsList.onClosed();
                                updateRelativeBounds();
                                reorganizeOtherMainWindows();
                            }
                        }
                    ]
                }
    }

    public override function close(animateClose: Boolean) {
        if (animateClose) {
            doClose();
        } else {
            width = minimumWidth;
            height = closedHeight;
            layoutX = closedPosition.x;
            layoutY = closedPosition.y;
            rotate = 0;
            isClosed = true;
            hideDrawers = false;
            isCentered = false;
            if (closedAction != null) {
                closedAction(this);
            }
            isAnimating = false;
            scyToolsList.onClosed();
            //          updateRelativeBounds();
            reorganizeOtherMainWindows();
        }
    }

    //update relative values
    function updateRelativeBounds(): Void {
        relativeLayoutCenterX = (layoutX + (width / 2)) / scene.width as Number;
        relativeLayoutCenterY = (layoutY + (height / 2)) / scene.height as Number;
        relativeWidth = width / scene.width as Number;
        relativeHeight = height / scene.height as Number;
    }

    function startDragging(e: MouseEvent): Void {
        for (wcl in changesListeners) {
            wcl.draggingStarted();
        }

        activate();
        originalX = layoutX;
        originalY = layoutY;
        originalW = width;
        originalH = height;
        maxDifX = 0;
        maxDifY = 0;
        sceneTopLeft = localToScene(0, 0);
        beingDragged = true;
        MouseBlocker.startMouseBlocking();
    }

    // set from FunctionalRoleWindowPositioner.makeMainWindow to re-rotate windows in center
    public var reorganizeOtherMainWindows: function();

    function stopDragging(e: MouseEvent): Void {
        for (wcl in changesListeners) {
            wcl.draggingFinished();
        }

        if (hasBeenDragged) {
            // if window is being dragged after being centered, it loses the centered status
            isCentered = false;
            // now after dragging we update the relative bounds for relative repositioning
            updateRelativeBounds();
            // re-rotate centered windows after this window is draged out/away
            reorganizeOtherMainWindows();
            hasBeenDragged = false;
        }

        MouseBlocker.stopMouseBlocking();
        beingDragged = false;
    }

    function printMousePos(label: String, e: MouseEvent) {
    //      System.out.println("{label} - x:{e.x}, sceneX:{e.sceneX}, screenX:{e.screenX}, y:{e.y}, sceneY:{e.sceneY}, screenY:{e.screenY}");

    }

    function doDrag(e: MouseEvent): Void {
        printMousePos("drag", e);
        var mouseEventInScene = MouseEventInScene { mouseEvent: e };
        var difX = mouseEventInScene.dragX;
        var difY = mouseEventInScene.dragY;
        maxDifX = Math.max(maxDifX, difX);
        maxDifY = Math.max(maxDifY, difY);
        //System.out.println("difX: {e.x}-{e.dragAnchorX} {difX}, difY: {e.y}-{e.dragAnchorY} {difY}");
        layoutX = originalX + difX;
        layoutY = originalY + difY;
        hasBeenDragged = true;
        isManuallyRepositioned = true;
    }

    function doResize(e: MouseEvent): Void {
        printMousePos("resize", e);
        if (isClosed) {
            isClosed = false;
        }
        if (isMinimized) {
            isMinimized = false;
        }
        checkScyContent();
        var mouseEventInScene = MouseEventInScene { mouseEvent: e };
        var angle = Math.toRadians(rotate);
        var difW = Math.cos(angle) * mouseEventInScene.dragX + Math.sin(angle) * mouseEventInScene.dragY;
        var difH = Math.cos(angle) * mouseEventInScene.dragY - Math.sin(angle) * mouseEventInScene.dragX;
        var difX: Number;
        var difY: Number;
        difX = (1 - Math.cos(angle)) * difW / 2;
        difY = (1 - Math.cos(angle)) * difH / 2;

        var newSize = limitSize(originalW + difW, originalH + difH);
        width = newSize.x;
        height = newSize.y;
        desiredWidth = newSize.x;
        desiredHeight = newSize.y;

        updateRelativeBounds();

        layoutX = originalX - difX;
        layoutY = originalY - difY;
    }

    function doClose(): Void {
        userDidSomething();
        if (isMaximized) {
            resetMaximizedState();
        }
        if (not scyToolsList.aboutToClose()) {
            // abort close action
            return;
        }
        if (aboutToCloseAction != null) {
            if (not aboutToCloseAction(this)) {
                // close blocked
                return;
            }
        }
        var closeTimeline = getCloseTimeline();

        if (closeTimeline != null) {
            isAnimating = true;
            hideDrawers = true;
            closeTimeline.play();
        }

        toBack();
        logger.debug("closed {title}");
    }

    function doMaximize(): Void {
        userDidSomething();
        // TODO: needs to call window positioning code
        activate();
        toFront();
        isMaximized = true;
        openWindow(5, 5, scene.width - 10, scene.height - 10, 0, true);
        windowManager.fullscreenWindow = this;
        allowDragging = false;
        allowResize = false;
        allowRotate = false;
        isCentered = false;
        if (reorganizeOtherMainWindows != null) {
            FX.deferAction(reorganizeOtherMainWindows);
        } else {
            logger.info("could not call reorganizeOtherMainWindows, because it is null");
        }
    }

    public override function openFixedFullScreen(): Void {
        windowStateControls.visible = false;
        doMaximize();
    }

    function resetMaximizedState(): Void {
        windowManager.fullscreenWindow = null;
        hideDrawers = true;
        allowDragging = true;
        allowResize = true;
        allowRotate = true;
        isMaximized = false;
    }

    function handleDoubleClick(e: MouseEvent): Void {
        activate();
        if (isMaximized) {
            resetMaximizedState();
        }
        windowControl.makeMainScyWindow(this);
    }

    function centerAction(): Void {
        userDidSomething();
        if (isMaximized) {
            resetMaximizedState();
        }
        windowControl.makeMainScyWindow(eloUri);
    }

    function doRotateNormal(): Void {
        userDidSomething();
        cache = true;
        cacheHint = CacheHint.ROTATE;
        Timeline {
            keyFrames: [
                KeyFrame {
                    time: 300ms
                    values: [rotate => 0.0 tween Interpolator.EASEOUT]
                    action: function() {
                        cache = false;
                        cacheHint = CacheHint.DEFAULT;
                        if (isCentered) {
                            reorganizeOtherMainWindows();
                        }
                    }
                }
            ]
        }.playFromStart();
    }

    function getScyContent(scyCont: Node): Node {
        if (scyCont != null)
            return scyCont;
        return Rectangle {
                    x: 0,
                    y: 0
                    width: 10,
                    height: 10
                    fill: Color.TRANSPARENT
                };
    }

    function activate() {
        if (windowManager != null) {
            windowManager.activateScyWindow(this);
        }
    }

    public override function copyWindowColorSchemeColors(newColors: WindowColorScheme): Void {
        topDrawer.windowColorScheme.assign(windowColorScheme);
        for (drawer in leftDrawers) {
            drawer.windowColorScheme.assign(windowColorScheme);
        }
        rightDrawer.windowColorScheme.assign(windowColorScheme);
        bottomDrawer.windowColorScheme.assign(windowColorScheme);
        eloIcon.windowColorScheme.assign(windowColorScheme);
        emptyWindow.windowColorScheme.assign(windowColorScheme);
        contentElement.windowColorScheme.assign(windowColorScheme);
        windowStateControls.windowColorScheme.assign(windowColorScheme);
        windowTitleBar.windowColorScheme.assign(windowColorScheme);
        titleBarBuddies.windowColorScheme.assign(windowColorScheme);
        resizeElement.windowColorScheme.assign(windowColorScheme);
        rotateElement.windowColorScheme.assign(windowColorScheme);
        closedWindow.windowColorScheme.assign(windowColorScheme);
    }

    function updateWindowColorScheme(node: Node): Void {
        if (node instanceof ScyToolFX) {
            def scyToolFX = node as ScyToolFX;
            scyToolFX.setWindowColorScheme(windowColorScheme);
        }
    }

    public override function buddiesChanged(): Void {
        titleBarBuddies.buddiesChanged();
        closedWindowBuddies.buddiesChanged();
        closedWindow.buddiesDisplayChanged();
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
                        window: this
                        tooltipManager: tooltipManager
                        bubbleManager: bubbleManager
                        content: topDrawerTool;
                        activated: bind activated;
                        activate: activate;
                        layoutX: drawerCornerOffset;
                        layoutY: 0;
                        width: bind realWidth - 2 * drawerCornerOffset - 4
                    }
            insert topDrawer into drawerGroup.content;
        }
        scyToolsList.topDrawerTool = topDrawerTool;
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
                        window: this
                        tooltipManager: tooltipManager
                        bubbleManager: bubbleManager
                        content: rightDrawerTool;
                        activated: bind activated;
                        activate: activate;
                        layoutX: bind realWidth;
                        layoutY: drawerCornerOffset + leftRightDrawerOffset
                        height: bind realHeight - 3 * drawerCornerOffset
                    }
            insert rightDrawer into drawerGroup.content;
        }
        scyToolsList.rightDrawerTool = rightDrawerTool;
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
                        window: this
                        tooltipManager: tooltipManager
                        bubbleManager: bubbleManager
                        content: bottomDrawerTool;
                        activated: bind activated;
                        activate: activate;
                        layoutX: drawerCornerOffset;
                        layoutY: bind realHeight;
                        width: bind realWidth - 2 * drawerCornerOffset
                    }
            insert bottomDrawer into drawerGroup.content;
        }
        scyToolsList.bottomDrawerTool = bottomDrawerTool;
    }

    function setLeftDrawer(oldDrawerTools: Node[]) {
        if (drawerGroup == null) {
            // initialisation not yet ready, a call from postinit will be done again
            return;
        }
        var newLeftDrawers: LeftDrawer[];
        for (drawerTool in leftDrawerTools) {
            var drawer: LeftDrawer;
            def previousIndex = Sequences.indexOf(oldDrawerTools, drawerTool);
            if (previousIndex >= 0) {
                // tool was already there
                drawer = leftDrawers[previousIndex];
            } else {
                // new drawer tool
                drawer = LeftDrawer {
                            windowColorScheme: windowColorScheme
                            window: this
                            tooltipManager: tooltipManager
                            bubbleManager: bubbleManager
                            content: drawerTool;
                            handleNumber: indexof drawerTool
                            activated: bind activated;
                            activate: activate;
                            layoutX: 0;
                            layoutY: drawerCornerOffset + leftRightDrawerOffset
                            height: bind realHeight - 3 * drawerCornerOffset - 4
                        }
                insert drawer into drawerGroup.content;
            }
            insert drawer into newLeftDrawers;
            drawer.handleNumber = indexof drawerTool;
        }

        for (oldDrawerTool in oldDrawerTools) {
            if (Sequences.indexOf(leftDrawerTools, oldDrawerTool) < 0) {
                def drawer = leftDrawers[indexof oldDrawerTool];
                delete drawer from drawerGroup.content;
            }
        }
        leftDrawers = newLeftDrawers;
        for (drawer in leftDrawers) {
            drawer.otherDrawers = leftDrawers;
        }
        scyToolsList.leftDrawerTools = leftDrawerTools;
    }

    public override function openDrawer(which: String): Void {
        if ("top".equalsIgnoreCase(which)) {
            topDrawer.opened = true;
        } else if ("right".equalsIgnoreCase(which)) {
            rightDrawer.opened = true;
        } else if ("bottom".equalsIgnoreCase(which)) {
            bottomDrawer.opened = true;
        } else if ("left".equalsIgnoreCase(which)) {
        //         leftDrawer.opened = true;
        }
    }

    function startDragIcon(e: MouseEvent): Void {
        var dragNode: Node;
        var dragObject: Object;
        if (scyElo != null) {
            dragNode = windowControl.windowStyler.getScyEloIcon(scyElo);
            dragObject = scyElo.getMetadata();
        } else {
            dragNode = windowControl.windowStyler.getScyEloIcon(eloType);
            dragObject = this;
        }
        dragAndDropManager.startDrag(dragNode, dragObject, this, e);
    }

    function userDidSomething(): Void {
        bubbleManager.userDidSomething()
    }

    public override function create(): Node {
        if (isClosed) {
            width = minimumWidth;
            height = closedHeight;
        }

        blocksMouse = true;

        emptyWindow = EmptyWindow {
                    width: bind realWidth;
                    height: bind realHeight;
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
                    activate: activate;
                    layoutX: borderWidth / 2 + 1 + contentSideBorder;
                    layoutY: contentTopOffset;
                }

        windowStateControls = WindowStateControls {
                    tooltipManager: tooltipManager
                    windowStyler: windowStyler
                    windowColorScheme: windowColorScheme
                    enableRotateNormal: bind rotate != 0.0
                    enableMinimize: bind allowClose and not isClosed
                    enableCenter: bind allowCenter and not isCentered
                    enableMaximize: bind allowMaximize and not isMaximized
                    rotateNormalAction: doRotateNormal
                    minimizeAction: doClose
                    centerAction: centerAction
                    maximizeAction: doMaximize
                }

        titleBarWindowAttributes = TitleBarWindowAttributes {
                    windowColorScheme: windowColorScheme
                    scyWindowAttributes: bind scyWindowAttributes
                }

        ownershipManager = OwnershipManager {
                    elo: bind scyElo
                    tbi: tbi
                    scyWindow: this
                }

        titleBarBuddies = TitleBarBuddies {
                    tooltipManager: tooltipManager
                    windowColorScheme: windowColorScheme
                    window: this
                    ownershipManager: ownershipManager
                    showOneIcon: false
                }

        closedWindowBuddies = TitleBarBuddies {
                    tooltipManager: tooltipManager
                    windowColorScheme: windowColorScheme
                    window: this
                    ownershipManager: ownershipManager
                    showOneIcon: true
                    myName: tbi.getLoginUserName()
                }

        ownershipManager.update();

        eloFinishedButton = TitleBarButton {
                    actionId: "elo_finished"
                    enabled: true
                    iconType: "Elo_finished"
                    tooltip: ##"I am finished"
                    action: function() {
                        missionModelFX.showWebNews(eloUri);
                        //add finished metadata to the elo
                        scyElo.setFinished(true);
                        tbi.getRepository().addMetadata(eloUri, scyElo.getMetadata());
                        var action = new Action();
                        var context = new Context();
                        context.setEloURI(eloUri.toString());
                        context.setTool(eloToolConfig.getContentCreatorId());
                        context.setMission(tbi.getMissionRuntimeURI().toString());
                        context.setSession("n/a");
                        action.setType("elo_finished");
                        action.setContext(context);
                        tbi.getActionLogger().log(action);
                        eloFinishedButton.enabled = false;
                    }
                };

        var globalTitleBarButtons: TitleBarButton[];
        insert eloFinishedButton into globalTitleBarButtons;

        titleBarButtons = TitleBarButtons {
                    tooltipManager: tooltipManager
                    windowColorScheme: windowColorScheme
                    windowStyler: windowStyler
                    globalTitleBarButtons: globalTitleBarButtons
                    isReadOnly: function(): Boolean {
                        scyToolsList.readOnly
                    }
                }
        titleBarButtonManager = titleBarButtons;
        //      scyToolsList.setTitleBarButtonManager(titleBarButtons);

        windowTitleBar = WindowTitleBarDouble {
                    width: bind realWidth + borderWidth
                    windowStateControls: windowStateControls
                    titleBarWindowAttributes: titleBarWindowAttributes
                    titleBarBuddies: titleBarBuddies
                    titleBarButtons: titleBarButtons
                    iconSize: iconSize
                    title: bind title;
                    activated: bind activated
                    beingDragged: bind beingDragged
                    startDragIcon: startDragIcon
                    allowDragIcon: bind allowDragging and not isMaximized
                    allowMouseOverDisplay: bind not isMaximized
                    windowColorScheme: windowColorScheme
                    layoutX: -borderWidth / 2;
                    layoutY: 0;
                }

        resizeElement = WindowResize {
                    visible: bind (allowResize or isClosed) and not isMaximized
                    size: controlSize;
                    borderWidth: borderWidth;
                    separatorLength: separatorLength
                    windowColorScheme: windowColorScheme
                    activate: activate;
                    startResize: startResize;
                    doResize: doResize;
                    stopResize: stopResize;
                    layoutX: bind realWidth
                    layoutY: bind realHeight
                    bubbleManager: bubbleManager
                }

        rotateElement = WindowRotate {
                    visible: bind allowRotate and not isMaximized
                    size: controlSize;
                    borderWidth: borderWidth;
                    separatorLength: separatorLength
                    windowColorScheme: windowColorScheme
                    activate: activate;
                    rotateWindow: this;
                    layoutX: 0;
                    layoutY: bind realHeight;
                    bubbleManager: bubbleManager
                }

        // show a filled rect as content for test purposes
//      scyContent = Rectangle {
//         x: -100, y: -100
//         width: 1000, height: 1000
//         fill: Color.color(1,.25,.25,.75)
//      }
        def closedGroup = Group {
                    visible: bind isClosed
                    content: [
                        closedWindow = ClosedWindow {
                                    window: this
                                    windowColorScheme: windowColorScheme
                                    scyElo: bind scyElo
                                    buddiesDisplay: closedWindowBuddies
                                    startDragIcon: startDragIcon
                                    doubleClickAction: handleDoubleClick
                                    activated: bind activated
                                    activate: activate;
                                    title: bind title
                                }
                    ]
                }

        def openGroup = Group {
                    visible: bind not isClosed
                    content: [
                        emptyWindow,
                        contentElement,
                        drawerGroup,
                        windowTitleBar,
                        resizeElement,
                        rotateElement,
                    //               Group { // the scy window attributes
                    //                  layoutX: iconSize + 5
                    //                  layoutY: 19
                    //                  content: bind scyWindowAttributes,
                    //               },
                    ]
                }

        eloIconChanged();

        def ownersDefined = function(): Boolean {
                    sizeof ownershipManager.getOwners() > 0
                }

        var bubble = bubbleManager.createBubble(titleBarBuddies, "elo-buddies", BubbleLayer.DESKTOP, BubbleKey.CLOSED_ELO_BUDDIES, this);
        bubble.canBeUsed = ownersDefined;
        bubble = bubbleManager.createBubble(closedWindowBuddies, "elo-buddies", BubbleLayer.DESKTOP, BubbleKey.OPEN_ELO_BUDDIES, this);
        bubble.canBeUsed = ownersDefined;

        bubble = bubbleManager.createBubble(closedWindow.eloIcon, BubbleLayer.DESKTOP, BubbleKey.ELO_ICON_CLOSED, this);
        bubble.getTargetNode = function(): Node {
                    closedWindow.eloIcon
                }
        bubble.canBeUsed = function(): Boolean {
                    isClosed
                }

        bubble = bubbleManager.createBubble(windowTitleBar.eloIcon, BubbleLayer.DESKTOP, BubbleKey.ELO_ICON_OPEN, this);
        bubble.getTargetNode = function(): Node {
                    windowTitleBar.eloIcon
                }
        bubble.canBeUsed = function(): Boolean {
                    not isClosed
                }

        return mainContentGroup = Group {
                            cursor: bind if (allowDragging and not isMaximized) Cursor.MOVE else null
                            cache: true;
                            content: [
                                closedGroup,
                                openGroup
                            ]
                            onMouseClicked: function(e: MouseEvent): Void {
                                userDidSomething();
                                if (e.clickCount == 2) {
                                    handleDoubleClick(e);
                                }
                            }
                            onMousePressed: function(e: MouseEvent): Void {
                                bubbleManager.pauze();
                                if (allowDragging) {
                                    startDragging(e);
                                }
                            }
                            onMouseDragged: function(e: MouseEvent): Void {
                                if (allowDragging) {
                                    doDrag(e);
                                }
                            }
                            onMouseReleased: function(e: MouseEvent): Void {
                                bubbleManager.resume();
                                if (allowDragging) {
                                    stopDragging(e);
                                }
                            }
                        };
    }

}

//function hideScyWindow(scyWindow:ScyWindow):Void{
//   scyWindow.hideTo(scyWindow.translateX, scyWindow.translateY);
//}
//
//function showScyWindow(scyWindow:ScyWindow):Void{
//   scyWindow.showFrom(scyWindow.translateX, scyWindow.translateY);
//}
//
//function closeScyWindow(scyWindow:ScyWindow):Void{
//   scyWindow.closeIt();
//}
function run() {

//   var scyDesktop: WindowManager = WindowManagerImpl{
//   };
//   var newGroup = VBox {
//      translateX: 5
//      translateY: 5;
//      spacing: 3;
//      content: [
//         SwingButton{
//            text: "Tree"
//            action: function() {
//               var tree = new JTree();
//               var treeSize = new Dimension(2000,2000);
//					//tree.setMinimumSize(treeSize);
//               //tree.setMaximumSize(treeSize);
//               tree.setPreferredSize(treeSize);
//					//tree.setSize(treeSize);
//               var treeNode = SwingComponent.wrap(tree);
//               var drawingWindow = StandardScyWindow{
//                  color: Color.BLUE
//                  title: "Drawing"
//                  width: 150
//                  height: 150
//                  scyContent: treeNode
//						//swingContent:tree
//                  visible: true
//               }
//               scyDesktop.addScyWindow(drawingWindow)
//            }
//         }
//         SwingButton{
//            text: "Text"
//            action: function() {
//               var textArea = new JTextArea();
//               textArea.setPreferredSize(new Dimension(2000,2000));
//               textArea.setEditable(true);
//               textArea.setText("gfggfggfdgdgdfgfgfgfdgafgfgd");
//               var textNode = SwingComponent.wrap(textArea);
//               var drawingWindow = StandardScyWindow{
//                  color: Color.BLUE
//                  title: "Drawing"
//                  width: 150
//                  height: 150
//                  scyContent: SwingScrollPane{
//                     view: textNode
//                     scrollable: true;
//                  }
//                  visible: true
//               }
//               scyDesktop.addScyWindow(drawingWindow)
//            }
//         }
//         SwingButton{
//            text: "Text 2"
//            action: function() {
//               var textArea = new JTextArea();
//					//textArea.setPreferredSize(new Dimension(2000,2000));
//               textArea.setEditable(true);
//               textArea.setWrapStyleWord(true);
//               textArea.setLineWrap(true);
//					//textArea.setText("gfggfggfdgdgdfgfgfgfdgafgfgd");
//               var scrollPane = new JScrollPane(textArea);
//					//scrollPane.setPreferredSize(new Dimension(200,200));
//               var textNode = SwingComponent.wrap(scrollPane);
//               var drawingWindow = StandardScyWindow{
//                  color: Color.BLUE
//                  title: "Drawing"
//                  scyContent: textNode
//                  visible: true
//						//swingContent:scrollPane
//                  width: 150
//                  height: 150
//               }
//               scyDesktop.addScyWindow(drawingWindow);
//               drawingWindow.width = 151;
//            }
//         }
//         SwingButton{
//            text: "Red"
//            action: function() {
//               var drawingWindow = StandardScyWindow{
//						//						 translateX:100;
//                  //						 translateY:100;
//                  color: Color.BLUE
//                  title: "Red"
//                  scyContent: Rectangle {
//                     x: 10,
//                     y: 10
//                     width: 140,
//                     height: 90
//                     fill: Color.PERU
//                  }
//                  visible: true
//						//opacity:0;
//						//closeAction:closeScyWindow;
//
//               }
//               scyDesktop.addScyWindow(drawingWindow);
//               var opacityTimeline = Timeline{
//                  keyFrames: [
//						 at (0s){
//							//drawingWindow.opacity => 0.0;
//                     drawingWindow.translateX => 0;
//                     drawingWindow.translateY => 0;
//                     drawingWindow.width => 0;
//                     drawingWindow.height => 0;
//						 }
//						 at (500ms){
//							//drawingWindow.opacity => 1.0;
//                     drawingWindow.translateX => 200;
//                     drawingWindow.translateY => 200;
//                     drawingWindow.width => 150;
//                     drawingWindow.height => 150;
//						 }
//                  ];
//               }
//               opacityTimeline.play();
//            }
//         }
//      ]
//   };
//   var newScyWindow: ScyWindow= StandardScyWindow{
//      title: "New"
//      color: Color.BLUEVIOLET
//      height: 150;
//      //scyContent:newGroup
//      allowClose: true;
//      allowResize: true;
//      allowMinimize: true;
//      translateX: 20;
//      translateY: 20;
//      setScyContent: function(scyWindow:ScyWindow){
//         println("setScyContent");
//         scyWindow.scyContent = newGroup;
////			scyWindow.color =
////				 Color.CORAL;
//
//
//      };
//      closedAction: function(scyWindow:ScyWindow){
//         println("closedAction");
//         scyWindow.scyContent = null
//      }
//   };
//   newScyWindow.open();
//   //newScyWindow.openWindow(0, 150);
//   scyDesktop.addScyWindow(newScyWindow);
    var fixedScyWindow = StandardScyWindow {
                title: "Fixed"
                windowColorScheme: WindowColorScheme.getWindowColorScheme(ScyColors.green)
                height: 150;
                //      scyContent:newGroup
                allowClose: false;
                allowResize: false;
                allowRotate: false;
                allowMinimize: false;
                translateX: 200;
                translateY: 40;
                topDrawerTool: Button {
                    text: "Top"
                    action: function() {
                    }
                }
                rightDrawerTool: Button {
                    text: "Right"
                    action: function() {
                    }
                }
                bottomDrawerTool: Button {
                    text: "Bottom"
                    action: function() {
                    }
                }
                leftDrawerTools: Button {
                    text: "Left"
                    action: function() {
                    }
                }
            };
    fixedScyWindow.openWindow(100, 100);
    //   scyDesktop.addScyWindow(fixedScyWindow);

    var closedScyWindow = StandardScyWindow {
                title: "Closed and very closed"
                eloType: "M"
                windowColorScheme: WindowColorScheme.getWindowColorScheme(ScyColors.darkGray)
                height: 27;
                isClosed: true
                allowClose: true;
                allowResize: true;
                allowRotate: true;
                allowMinimize: true;
                translateX: 20;
                translateY: 200;
                topDrawerTool: Button {
                    text: "Top"
                    action: function() {
                    }
                }
                rightDrawerTool: Button {
                    text: "Right"
                    action: function() {
                    }
                }
                bottomDrawerTool: Button {
                    text: "Bottom"
                    action: function() {
                    }
                }
                leftDrawerTools: Button {
                    text: "Left"
                    action: function() {
                    }
                }
            };
    closedScyWindow.openWindow(100, 150);
    //   scyDesktop.addScyWindow(closedScyWindow);

    var eloWindow = StandardScyWindow {
                title: bind "elo window";
                windowColorScheme: WindowColorScheme.getWindowColorScheme(ScyColors.pink)
                allowClose: true;
                allowMinimize: true;
                allowResize: false;
                allowRotate: true;
                //setScyContent:setEloContent;
                translateX: 200
                translateY: 200
                scyWindowAttributes: [
                    TestAttribute {
                    }
                ]
            //      topDrawerTool:Button {
            //            text: "Top"
            //            action: function() {
            //            }
            //         }
            //      rightDrawerTool:Button {
            //            text: "Right"
            //            action: function() {
            //            }
            //         }
            //      bottomDrawerTool:Button {
            //            text: "Bottom"
            //            action: function() {
            //           }
            //         }
            //      leftDrawerTool:Button {
            //            text: "Left"
            //            action: function() {
            //            }
            //         }
            }
    //   scyDesktop.addScyWindow(eloWindow);

    var stage: Stage;
    FX.deferAction(function() {
        MouseBlocker.initMouseBlocker(stage);
        ProgressOverlay.initOverlay(stage);
    });

    stage = Stage {
                title: "Scy window test"
                width: 400
                height: 600
                scene: Scene {
                    content: [
                        fixedScyWindow,
                        eloWindow,
                        closedScyWindow
                    //            scyDesktop.scyWindows
                    //				whiteboardNode,
                    //				drawingWindow2
                    //				drawingWindow3
                    ]
                }
            }
}
