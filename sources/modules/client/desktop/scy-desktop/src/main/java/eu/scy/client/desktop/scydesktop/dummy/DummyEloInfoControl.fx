/*
 * DummyEloInfoControl.fx
 *
 * Created on 30-jun-2009, 14:16:28
 */

package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;

import java.net.URI;

/**
 * @author sikkenj
 */

public class DummyEloInfoControl extends EloInfoControl {

   public override function getEloType(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      var extension = getUriExtension(eloUri);
      return extension;
   }


   public override function getEloTitle(eloUri:URI):String{
      if (eloUri==null){
         return null;
      }
      return getUriTitle(eloUri);
   }

   function getUriExtension(uri:URI):String{
      var lastPointPos = uri.getPath().lastIndexOf('.');
      if (lastPointPos>=0){
         return uri.getPath().substring(lastPointPos+1);
      }
      return "";
   }

   function getUriTitle(uri:URI):String{
      var path = uri.getPath();
      var titleStartPos = 0;
      var titleEndPos = path.length();
      var lastSlashPos = uri.getPath().lastIndexOf('/');
      if (lastSlashPos>=0){
         titleStartPos = lastSlashPos+1;
      }
      var lastPointPos = uri.getPath().lastIndexOf('.',titleStartPos);
      if (lastPointPos>=0){
         titleEndPos = lastPointPos;
      }
      return path.substring(titleStartPos, titleEndPos);
   }

}
