/*
 * ScyToolsList.fx
 *
 * Created on 11-jan-2010, 11:26:30
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import eu.scy.client.desktop.scydesktop.tools.ScyToolGetterPresent;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Void;
import java.net.URI;

/**
 * @author sikken
 */

public class ScyToolsList extends ScyTool {
   def logger = Logger.getLogger(this.getClass());

   public var windowContentTool:Node on replace {buildToolList()};
   public var topDrawerTool:Node on replace {buildToolList()};
   public var rightDrawerTool:Node on replace {buildToolList()};
   public var bottomDrawerTool:Node on replace {buildToolList()};
   public var leftDrawerTool:Node on replace {buildToolList()};
   public var actionLoggerTool:Node on replace {buildToolList()};

   var scyToolList:ScyTool[];

   function buildToolList():Void{
      delete scyToolList;
      insertIfScyTool(windowContentTool);
      insertIfScyTool(topDrawerTool);
      insertIfScyTool(rightDrawerTool);
      insertIfScyTool(bottomDrawerTool);
      insertIfScyTool(leftDrawerTool);
      insertIfScyTool(actionLoggerTool);
//      logger.info("nr of scyTools {sizeof scyToolList}");
   }

   function insertIfScyTool(node:Node):Void{
//      logger.info("insertIfScyTool({node.getClass()})");
      if (node instanceof ScyTool){
         insert (node as ScyTool) into scyToolList;
//         logger.debug("add node as ScyTool {sizeof scyToolList}");
      }
      else if (node instanceof ScyToolGetterPresent){
         insert (node as ScyToolGetterPresent).getScyTool() into scyToolList;
//         logger.debug("add node.getScyTool as ScyTool {sizeof scyToolList}");
      }
   }

    public function initialize () : Void {
        for (scyTool in scyToolList){
           scyTool.initialize(scyTool==windowContentTool);
        }
    }

    override public function initialize (windowContent: Boolean) : Void {
        logger.error("Should not be called");
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
              return;
           }
        }
        throw new IllegalStateException("could not find a tool, which accepted the drop of {object.getClass()}");
    }

}
