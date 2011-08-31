/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.SearchOperation;

/**
 *
 * @author SikkenJ
 */
public class AuthorQuerySelecter extends AbstractSimpleQuerySelecter
{

   private final IMetadataKey authorKey;
   private final String myLoginName;

   private enum AuthorOptions
   {

      ME,
      MY_BUDDIES,
      NOT_ME,
      SAME,
      SAME_BUDDIES,
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
            displayOptions.add(AuthorOptions.MY_BUDDIES.toString());
            displayOptions.add(AuthorOptions.NOT_ME.toString());
            break;
         case ELO_BASED:
            displayOptions.add(AuthorOptions.SAME.toString());
            displayOptions.add(AuthorOptions.SAME_BUDDIES.toString());
            displayOptions.add(AuthorOptions.NOT_SAME.toString());
            break;
      }
      return displayOptions;
   }

   @Override
   public String getEloIconName()
   {
      return "Buddy_me";
   }

   @Override
   public String getEloIconTooltip()
   {
      return "author";
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

   @Override
   public IQueryComponent getQueryComponent()
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         return null;
      }
      AuthorOptions authorOption = AuthorOptions.valueOf(getSelectedOption());
      switch (authorOption)
      {
         case ME:
            return new MetadataQueryComponent(authorKey, SearchOperation.EQUALS, myLoginName);
         case NOT_ME:
            return new MetadataQueryComponent(authorKey, SearchOperation.NOT_EQUALS, myLoginName);
         case SAME:
            return new MetadataQueryComponent(authorKey, SearchOperation.EQUALS, getBasedOnAuthor());
         case NOT_SAME:
            return new MetadataQueryComponent(authorKey, SearchOperation.NOT_EQUALS, getBasedOnAuthor());
      }
      return null;
   }

   @Override
   public void setFilterOptions(IQuery query)
   {
      if (StringUtils.isEmpty(getSelectedOption()))
      {
         query.enableUserRestriction(false);
      }
      else
      {
         query.enableUserRestriction(false);
         Set<String> allowedUsers = new HashSet<String>();
         Set<String> notAllowedUsers = new HashSet<String>();
         AuthorOptions authorOption = AuthorOptions.valueOf(getSelectedOption());
         switch (authorOption)
         {
            case ME:
               allowedUsers.add(myLoginName);
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
         query.setAllowedUsers(allowedUsers);
//         query.setNotAllowedUsers(notAllowedUsers);
      }
   }
}
