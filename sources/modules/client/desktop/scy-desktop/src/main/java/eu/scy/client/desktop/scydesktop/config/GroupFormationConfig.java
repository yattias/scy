/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.common.mission.StrategyType;
import java.net.URI;

/**
 *
 * @author SikkenJ
 */
public class GroupFormationConfig
{

   private String strategy;
   private URI referenceEloUri;
   private int minimumNrOfUsers;
   private int maximumNrOfUsers;

   @Override
   public String toString()
   {
      return "GroupFormationConfig{" + "strategy=" + strategy + "referenceEloUri=" + referenceEloUri + "minimumNrOfUsers=" + minimumNrOfUsers + "maximumNrOfUsers=" + maximumNrOfUsers + '}';
   }

   public int getMaximumNrOfUsers()
   {
      return maximumNrOfUsers;
   }

   public void setMaximumNrOfUsers(int maximumNrOfUsers)
   {
      this.maximumNrOfUsers = maximumNrOfUsers;
   }

   public int getMinimumNrOfUsers()
   {
      return minimumNrOfUsers;
   }

   public void setMinimumNrOfUsers(int minimumNrOfUsers)
   {
      this.minimumNrOfUsers = minimumNrOfUsers;
   }

   public URI getReferenceEloUri()
   {
      return referenceEloUri;
   }

   public void setReferenceEloUri(URI referenceEloUri)
   {
      this.referenceEloUri = referenceEloUri;
   }

   public String getStrategy()
   {
      return strategy;
   }

   public void setStrategy(String strategy)
   {
      this.strategy = strategy;
   }
}
