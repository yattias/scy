/*
 * ScyToolsList.fx
 *
 * Created on 11-jan-2010, 11:26:30
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import java.net.URI;
import java.lang.IllegalStateException;

/**
 * @author sikken
 */

public class ScyToolsList extends ScyTool {

   public var windowContentTool:ScyTool on replace {buildToolList};
   public var topDrawerTool:ScyTool on replace {buildToolList};
   public var rightDrawerTool:ScyTool on replace {buildToolList};
   public var bottomDrawerTool:ScyTool on replace {buildToolList};
   public var leftDrawerTool:ScyTool on replace {buildToolList};
   public var actionLoggerTool:ScyTool on replace {buildToolList};

   var scyToolList:ScyTool[];

   function buildToolList():Void{
      delete scyToolList;
      insert windowContentTool into scyToolList;
      insert topDrawerTool into scyToolList;
      insert rightDrawerTool into scyToolList;
      insert bottomDrawerTool into scyToolList;
      insert leftDrawerTool into scyToolList;
      insert actionLoggerTool into scyToolList;
   }

    override public function initialize () : Void {
        for (scyTool in scyToolList){
           scyTool.initialize();
        }
    }

    override public function postInitialize () : Void {
        for (scyTool in scyToolList){
           scyTool.postInitialize();
        }
    }

    override public function newElo () : Void {
        for (scyTool in scyToolList){
           scyTool.newElo();
        }
    }

    override public function loadElo (eloUri : URI) : Void {
        for (scyTool in scyToolList){
           scyTool.loadElo(eloUri);
        }
    }

    override public function loadedEloChanged (eloUri : URI) : Void {
         for (scyTool in scyToolList){
           scyTool.loadedEloChanged(eloUri);
        }
   }

    override public function onGotFocus () : Void {
         for (scyTool in scyToolList){
           scyTool.onGotFocus();
        }
    }

    override public function onLostFocus () : Void {
        for (scyTool in scyToolList){
           scyTool.onLostFocus();
        }
    }

    override public function onMinimized () : Void {
        for (scyTool in scyToolList){
           scyTool.onMinimized();
        }
    }

    override public function onUnMinimized () : Void {
        for (scyTool in scyToolList){
           scyTool.onUnMinimized();
        }
    }

    override public function aboutToClose () : Boolean {
        for (scyTool in scyToolList){
           if (not scyTool.aboutToClose()){
              return false;
           }
        }
        return true;
    }

    override public function onClosed () : Void {
        for (scyTool in scyToolList){
           scyTool.onClosed();
        }
    }

    override public function setEloSaver (eloSaver : EloSaver) : Void {
        for (scyTool in scyToolList){
           scyTool.setEloSaver(eloSaver);
        }
    }

    override public function setMyEloChanged (myEloChanged : MyEloChanged) : Void {
        for (scyTool in scyToolList){
           scyTool.setMyEloChanged(myEloChanged);
        }
    }

    override public function canAcceptDrop (object : Object) : Boolean {
        for (scyTool in scyToolList){
           if (scyTool.canAcceptDrop(object)){
              return true;
           }
        }
        return false;
    }

    override public function acceptDrop (object : Object) : Void {
        for (scyTool in scyToolList){
           if (scyTool.canAcceptDrop(object)){
              scyTool.acceptDrop(object);
           }
        }
        throw new IllegalStateException("could not find a tool, which accepted the drop of {object.getClass()}");
    }

}
