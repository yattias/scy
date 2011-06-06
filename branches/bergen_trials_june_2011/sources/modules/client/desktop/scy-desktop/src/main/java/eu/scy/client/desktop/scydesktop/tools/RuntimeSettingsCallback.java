/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;

/**
 * Callback interface to be implemented by the tool, in order to recieve the new value of any changed registered setting.
 * 
 * @author SikkenJ
 */
public interface RuntimeSettingsCallback
{

   /**
    * the value of the runtime setting with the specified name is changed.
    *
    * @param name
    * @param value
    */
   public void newRuntimeSetting(String name, String value);
}
