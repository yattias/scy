/*
 * MyFxdLoader.fx
 *
 * Created on 12-mrt-2010, 13:50:08
 */

package eu.scy.client.desktop.desktoputils.art;
import javafx.fxd.FXDNode;

/**
 * @author sikken
 */

public class MyFxdLoader extends FXDNode {
//   public-init var sourceLocation = ArtSource.artSource;
//   public-init var sourceName:String;
//   override public var url = "{sourceLocation}{sourceName}";
   public-read var loaded = false;
   public var loadedAction: function():Void;
   override protected function contentLoaded() {
//      println("loaded from {url}");
      loaded = true;
      FX.deferAction(loadedAction);
   }

}
