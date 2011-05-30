/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.tools.RuntimeSettingsRetriever;
import java.lang.String;
import eu.scy.client.desktop.scydesktop.tools.RuntimeSettingsCallback;
import java.net.URI;
import eu.scy.common.mission.RuntimeSettingsManager;
import eu.scy.common.mission.RuntimeSettingKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SikkenJ
 */
public class EloRuntimeSettingsRetriever extends RuntimeSettingsRetriever {

   public var eloUri: URI;
   public-init var runtimeSettingsManager: RuntimeSettingsManager;
   public var lasId:String;

   public override function toString():String{
      "EloRuntimeSettingsRetriever\{eloUri={eloUri},lasId={lasId}\}"
   }

   override public function getSetting(name: String): String {
      return runtimeSettingsManager.getSetting(new RuntimeSettingKey(name, lasId, eloUri));
   }

   override public function registerInterest(arg0: String): Void {
   // TODO, implement it
   }

   override public function getSettings(names: List): List {
      var keys = new ArrayList();
      for (name in names) {
         keys.add(new RuntimeSettingKey(name as String, lasId, eloUri));
      }
      return runtimeSettingsManager.getSettings(keys);
   }

   override public function registerInterest(arg0: List): Void {
   // TODO, implement it
   }

   override public function setRuntimeSettingsCallback(arg0: RuntimeSettingsCallback): Void {
   // TODO, implement it
   }

}
