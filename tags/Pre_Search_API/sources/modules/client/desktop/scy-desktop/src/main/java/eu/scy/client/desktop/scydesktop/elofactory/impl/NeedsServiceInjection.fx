/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

/**
 * @author SikkenJ
 */
public mixin class NeedsServiceInjection {

   public abstract function injectServices(servicesInjector: ServicesInjector): Void;

}
