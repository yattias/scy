/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission;

import java.util.List;

import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.scyelo.EloLogicalRole;

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

   public Boolean isContentCollaboration();
   public Boolean isTopDrawerCollaboration();
   public Boolean isRightDrawerCollaboration();
   public Boolean isBottomDrawerCollaboration();
   public Boolean isLeftDrawerCollaboration();

   public Boolean isContentStatic();

   public List<EloLogicalRole> getEloLogicalRoles();
   public List<EloFunctionalRole> getEloFunctionalRoles();
}

