/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;

import java.util.List;

/**
 * This interface is for tools to access runtime settings manager. Each tools is supplied with an own instance of this interface.
 *
 * The runtime settings manager also needs the eloUri and lasId. These values are set by the scy-desktop, the tool does not have any thing to do for this.
 * @author SikkenJ
 */
public interface RuntimeSettingsRetriever
{
   /**
    * returns the value specified for the given name.
    * 
    * If no value is pecified, a null is returned.
    * 
    * @param name
    * @return runtime setting value
    */
   public String getSetting(String name);

   /**
    * returns a list of values specified for the given list of names.
    *
    * If no value is pecified, a null is returned.
    *
    * @param names
    * @return list of runtime setting values
    */
   public List<String> getSettings(List<String> names);

   /**
    * register a setting by its name, when ever the value of the setting changes, the tool will be informed through the runtimeSettingsCallback interface.
    *
    * @param name
    */
   public void registerInterest(String name);

   /**
    * register a list of settinga by its names, when ever the value of the setting changes, the tool will be informed through the runtimeSettingsCallback interface.
    *
    * @param names
    */
   public void registerInterest(List<String> names);

   /**
    * supplies scy-desktopl with runtimeSettingsCallback object, which will be used to tell the tool new settings values (for the registered settings).
    *
    * @param runtimeSettingsCallback
    */
   public void setRuntimeSettingsCallback(RuntimeSettingsCallback runtimeSettingsCallback);
}
