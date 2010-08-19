/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import java.util.List;

/**
 *
 * @author SikkenJ
 */
public interface EloToolConfig {

   public String getEloType();
   public EloSystemRole getEloSystemRole();

   public String getContentCreatorId();

   public String getTopDrawerCreatorId();
   public String getRightDrawerCreatorId();
   public String getBottomDrawerCreatorId();
   public String getLeftDrawerCreatorId();

   public boolean isContentCollaboration();
   public boolean isTopDrawerCollaboration();
   public boolean isRightDrawerCollaboration();
   public boolean isBottomDrawerCollaboration();
   public boolean isLeftDrawerCollaboration();

   public List<EloLogicalRole> getEloLogicalRoles();
   public List<EloFunctionalRole> getEloFunctionalRoles();
}

