/*
 * FxdImageLoader.fx
 *
 * Created on 16-mrt-2010, 11:16:24
 */

package eu.scy.client.desktop.scydesktop.art;
import javafx.fxd.FXDContent;
import javafx.scene.Group;
import javafx.fxd.Duplicator;
import javafx.scene.Node;

/**
 * @author sikken
 */

public class FxdImageLoader extends FXDContent {
   public-init var sourceLocation = ArtSource.artSource;
   public-init var sourceName:String;
   public-init var backgroundLoading=false;
   public-read var loaded = false;
   public-read var sourceUrl = "{sourceLocation}{sourceName}";
   public-init var loadedAction: function():Void;
   public var returnDuplicates = true;

   def fxdNode = MyFxdLoader{
      url:sourceUrl
      loadedAction:stuffLoaded
      backgroundLoading:backgroundLoading
   }

   function stuffLoaded():Void{
      loaded = true;
      loadedAction();
   }

   override public function getGroup(id: String) : Group{
      var group = fxdNode.getGroup(id);
      if (group!=null and returnDuplicates){
         group = Duplicator.duplicate(group) as Group;
      }
      group;
   }

   override public function getNode(id: String) : Node{
      var node = fxdNode.getNode(id);
      if (node!=null and returnDuplicates){
         node = Duplicator.duplicate(node);
      }
      node;
   }

}
