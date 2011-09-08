/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.search;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.List;
import roolo.search.IQuery;
import roolo.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class BackgroundQuerySearch extends BackgroundSearch {

   private final IQuery query;
   private final QuerySearchFinished querySearchFinished;

   public BackgroundQuerySearch(ToolBrokerAPI tbi, NewEloCreationRegistry newEloCreationRegistry, IQuery query, QuerySearchFinished querySearchFinished)
   {
      super(tbi, newEloCreationRegistry);
      this.query = query;
      this.querySearchFinished = querySearchFinished;
   }

   @Override
   public void doSearch()
   {
      if (isAbort())
      {
         return;
      }
      final List<ISearchResult> searchResults = tbi.getRepository().search(query);
      if (isAbort())
      {
         return;
      }
      final List<ScySearchResult> scySearchResults = convertToScySearchResults(searchResults);
      sendScySearchResuls(scySearchResults);
   }

   @Override
   protected void setSearchResults(List<ScySearchResult> scySearchResults)
   {
      querySearchFinished.querySearchFinished(scySearchResults);
   }
}
