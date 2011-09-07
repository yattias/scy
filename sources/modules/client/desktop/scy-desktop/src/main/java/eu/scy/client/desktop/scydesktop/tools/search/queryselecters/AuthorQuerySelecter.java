/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.IQuery;

/**
 *
 * @author SikkenJ
 */
public class AuthorQuerySelecter extends AbstractSimpleQuerySelecter
{

   private static final Logger logger = Logger.getLogger(AuthorQuerySelecter.class);
   private final IMetadataKey authorKey;
   private final String myLoginName;

   private enum AuthorOptions
   {

      ME,
      MY_BUDDIES,
      NOT_ME,
      SAME,
      //      SAME_BUDDIES,
      NOT_SAME;
   }

   AuthorQuerySelecter(ToolBrokerAPI tbi, String id, QuerySelecterUsage querySelectorUsage)
   {
      super(tbi, id, querySelectorUsage);
      this.authorKey = getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
      this.myLoginName = tbi.getLoginUserName();
   }

   @Override
   protected List<String> createDisplayOptions()
   {
      List<String> displayOptions = new ArrayList<String>();
      switch (getQuerySelectorUsage())
      {
         case TEXT:
            displayOptions.add(AuthorOptions.ME.toString());
            if (!getBuddies().isEmpty())
            {
               displayOptions.add(AuthorOptions.MY_BUDDIES.toString());
            }
            displayOptions.add(AuthorOptions.NOT_ME.toString());
            break;
         case ELO_BASED:
            if (!getBasedOnElo().getAuthors().isEmpty())
            {
               displayOptions.add(AuthorOptions.SAME.toString());
//            displayOptions.add(AuthorOptions.SAME_BUDDIES.toString());
               displayOptions.add(AuthorOptions.NOT_SAME.toString());
            }
            break;
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "Buddy_me";
   }

   private String getBasedOnAuthor()
   {
      List<String> authors = getBasedOnElo().getAuthors();
      if (authors.size() > 0)
      {
         return authors.get(0);
      }
      return "";
   }

   private List<IAwarenessUser> getBuddies()
   {
      try
      {
         getTbi().getAwarenessService().getBuddies();
      }
      catch (AwarenessServiceException ex)
      {
         logger.warn("problems with getting the buddies from the awaerenessService", ex);
      }
      return new ArrayList<IAwarenessUser>();
   }

   private List<String> getBuddyNames()
   {
      List<String> buddyNames = new ArrayList<String>();
      List<IAwarenessUser> buddies = getBuddies();
      for (IAwarenessUser awarenessUser : buddies)
      {
         buddyNames.add(awarenessUser.getNickName());
      }
      return buddyNames;
   }

   @Override
   public void setFilterOptions(IQuery query)
   {
      AuthorOptions authorOption = getSelectedEnum(AuthorOptions.class);
      if (authorOption != null)
      {
         Set<String> allowedUsers = new HashSet<String>();
         Set<String> notAllowedUsers = new HashSet<String>();
         switch (authorOption)
         {
            case ME:
               allowedUsers.add(myLoginName);
               break;
            case MY_BUDDIES:
               allowedUsers.addAll(getBuddyNames());
               break;
            case NOT_ME:
               notAllowedUsers.add(myLoginName);
               break;
            case SAME:
               allowedUsers.addAll(getBasedOnElo().getAuthors());
               break;
            case NOT_SAME:
               notAllowedUsers.addAll(getBasedOnElo().getAuthors());
               break;
         }
         if (!allowedUsers.isEmpty())
         {
            query.setIncludedUsers(allowedUsers);
         }
         if (!notAllowedUsers.isEmpty())
         {
            query.setExcludedUsers(notAllowedUsers);
         }
      }
   }
}
