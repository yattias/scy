/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import java.util.List;

/**
 *
 * @author sikken
 */
public interface DisplayNames {

   public String getDisplayName(String type);

   public List<String> getDisplayNames(List<String> types);

   public boolean typeExists(String type);
}
