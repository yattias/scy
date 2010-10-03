/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.List;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class BackgroundQuerySearch extends BackgroundSearch {

   private final IQuery query;
   private final QuerySearchFinished querySearchFinished;

   public BackgroundQuerySearch(ToolBrokerAPI tbi, EloInfoControl eloInfoControl, NewEloCreationRegistry newEloCreationRegistry, IQuery query, QuerySearchFinished querySearchFinished)
   {
      super(tbi, eloInfoControl, newEloCreationRegistry);
      this.query = query;
      this.querySearchFinished = querySearchFinished;
   }

   @Override
   public void run()
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
