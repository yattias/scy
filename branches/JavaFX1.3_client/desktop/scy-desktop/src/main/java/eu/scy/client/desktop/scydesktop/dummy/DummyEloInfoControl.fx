/*
 * DummyEloInfoControl.fx
 *
 * Created on 30-jun-2009, 14:16:28
 */

package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;

import java.net.URI;
import eu.scy.client.desktop.scydesktop.utils.UriUtils;

/**
 * @author sikkenj
 */

public class DummyEloInfoControl extends EloInfoControl {

   public override function getEloType(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      var type = UriUtils.getExtension(eloUri);
      return type;
   }


   public override function getEloTitle(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      var eloTitle = UriUtils.getTitle(eloUri);
      return eloTitle;
   }
}
