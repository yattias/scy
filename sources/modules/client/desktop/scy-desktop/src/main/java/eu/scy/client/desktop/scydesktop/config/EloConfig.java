/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import java.util.List;

/**
 *
 * @author sikkenj
 */
public interface EloConfig {


   public String getContentCreatorId();

   public String getTopDrawerCreatorId();
   public String getRightDrawerCreatorId();
   public String getBottomDrawerCreatorId();
   public String getLeftDrawerCreatorId();

   public List<String> getLogicalTypeNames();
   public List<String> getFunctionalTypeNames();
}
