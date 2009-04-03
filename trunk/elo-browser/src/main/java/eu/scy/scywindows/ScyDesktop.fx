/*
 * ScyDesktop.fx
 *
 * Created on 16-dec-2008, 14:57:37
 */

package eu.scy.scywindows;

import eu.scy.elobrowser.awareness.contact.Contact;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import java.lang.System;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Sequences;

/**
 * @author sikkenj
 */

 // place your code here

public class ScyDesktop{
    public var desktop: Group = Group{
    } ;
    public var minimizedWindows: ScyWindow[];
//    var isLinking = false; //true if an edge is dragged to another edge
//    var newEdge: ScyEdge; //new edge not saved in windows
    var idCount = 0;
    var windowOffsetStep = 10;
    var activeWindow: ScyWindow;
    var activeWindowEffect: Effect = DropShadow {
        offsetX: 6,
        offsetY: 6,
        color: Color.color(0.25,.25,.25)
    }
    //   var inactiveWindowEffect:Effect = DropShadow {
    //      offsetX: 1,
    //      offsetY: 1
    //      color: Color.BLACK
    //			}
    var inactiveWindowEffect: Effect = null;

    // for drag and drop collaboration
    public var contactDragging: Boolean = false;
    public var draggedContact: Contact;
    public var targetWindow: ScyWindow;

	init{
		println("ScyDesktop created");
	}

    public function addScyWindow(scyWindow:ScyWindow){
		println("addScyWindow({scyWindow.id})");
      //scyWindow.id = "id_{idCount++}";
        scyWindow.scyDesktop = this;
      //      scyWindow.translateX = windowOffsetStep * idCount;
        //      scyWindow.translateY = windowOffsetStep * idCount;
        deactivateScyWindow(scyWindow);
		addToDesktop(scyWindow);
    }

    public function removeScyWindow(scyWindow:ScyWindow){
		println("removeScyWindow({scyWindow.id})");
        deactivateScyWindow(scyWindow);
        scyWindow.scyDesktop = null;
		removeFromDesktop(scyWindow);
    }

	function addToDesktop(scyWindow:ScyWindow){
		if (not desktopContainsWindow(scyWindow)){
			insert scyWindow into desktop.content;
			println("Added scyWindow {scyWindow.id} to the desktop ({desktop.content.size()})");
		}
		else {
			println("Trying to add scyWindow {scyWindow.id} to the desktop, but it is allready there");
		}
	}

	function removeFromDesktop(scyWindow:ScyWindow){
        if (desktopContainsWindow(scyWindow)){
            delete scyWindow from desktop.content;
            System.out.println("Removed scyWindow {scyWindow.id} from the desktop ({desktop.content.size()})");
        }
		else {
            println("Trying to remove scyWindow {scyWindow.id} from the desktop, but it is not there");
		}
	}

    public function activateScyWindow(scyWindow:ScyWindow){
		println("activateScyWindow({scyWindow.id})");
		if (scyWindow == activeWindow){
			// nothing to do, the window is allready activated
			return;
		}
        if (activeWindow != null){
            deactivateScyWindow(activeWindow);
        }
        showScyWindow(scyWindow);
        scyWindow.toFront();
        activeWindow = scyWindow;
        activeWindow.windowEffect = activeWindowEffect;
		if (not desktopContainsWindow(scyWindow)){
			println("activated scyWindow {scyWindow.id}, but it is not on the desktop");
		}
    }

    function deactivateScyWindow(scyWindow:ScyWindow){
        scyWindow.windowEffect = inactiveWindowEffect;
        if (scyWindow == activeWindow){
            activeWindow = null;
        }
//		if (not desktopContainsWindow(scyWindow)){
//			println("deactivated scyWindow {scyWindow.title}, but it is not on the desktop");
//		}

	}

    public function hideScyWindow(scyWindow:ScyWindow){
		println("hideScyWindow({scyWindow.id})");
		if (Sequences.indexOf(minimizedWindows,scyWindow) != - 1){
			// window allready minimized
			return;
		}
        if (scyWindow == activeWindow){
            deactivateScyWindow(scyWindow);
        }
        scyWindow.visible = false;
		if (not desktopContainsWindow(scyWindow)){
			println("hided scyWindow {scyWindow.id}, but it is not on the desktop");
		}
		removeFromDesktop(scyWindow);
		insert scyWindow into minimizedWindows;
    }

    public function showScyWindow(scyWindow:ScyWindow){
		println("showScyWindow({scyWindow.id})");
		var index = Sequences.indexOf(minimizedWindows,scyWindow);
		if (index == - 1){
			println("cannot show scyWindow {scyWindow.id}, because it is not hided");
			return;
		}
      
        scyWindow.visible = true;
		delete minimizedWindows[index];
		addToDesktop(scyWindow);
    }

	public function findScyWindow(id:String):ScyWindow{
        for (window in desktop.content){
            if (window.id == id)
            return window as ScyWindow;
        }
        for (window in minimizedWindows){
            if (window.id == id)
            return window as ScyWindow;
        }
		return null as ScyWindow;
 	}

    public function getScyWindows():ScyWindow[]{
        var scyWindows: ScyWindow[];
        for (window in desktop.content){
            if (window instanceof ScyWindow){
                insert window as ScyWindow into scyWindows;
            }
        }
        return scyWindows;
    }


    function desktopContainsWindow(scyWindow:ScyWindow) : Boolean{
        for (window in desktop.content){
            if (window == scyWindow)
            return true;
        }
		return false;
    }

    public function checkVisibilityScyWindows(showIt: function(ScyWindow):Boolean){
        for (window in desktop.content){
            checkVisibilityScyWindow(window,showIt);
        }
        for (window in minimizedWindows){
            checkVisibilityScyWindow(window,showIt);
        }
    }

    function checkVisibilityScyWindow(node:Node,showIt: function(ScyWindow):Boolean){
        if (node instanceof ScyWindow){
            var scyWindow = node as ScyWindow;
            var newVisible = showIt(scyWindow);
            if (newVisible != scyWindow.visible){
                if (newVisible){
                    showScyWindow(scyWindow)
                }
                    else {
                    hideScyWindow(scyWindow);
                }
            }
        }
    }

}

var scyDesktop = ScyDesktop{
};

public function getScyDesktop(){
	return scyDesktop;
}