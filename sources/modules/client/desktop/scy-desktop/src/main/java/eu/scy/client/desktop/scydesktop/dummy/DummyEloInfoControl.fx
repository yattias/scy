/*
 * DummyEloInfoControl.fx
 *
 * Created on 30-jun-2009, 14:16:28
 */

package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;

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
      var extension = UriUtils.getExtention(eloUri);
      return extension;
   }


   public override function getEloTitle(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      return UriUtils.getTitle(eloUri);
   }
}
