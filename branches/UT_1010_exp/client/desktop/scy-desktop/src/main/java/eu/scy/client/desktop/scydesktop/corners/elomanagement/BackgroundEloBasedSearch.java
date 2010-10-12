/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.List;
import javax.swing.SwingUtilities;
import roolo.api.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class BackgroundEloBasedSearch extends BackgroundSearch
{

   private final EloBasedSearcher eloBasedSearcher;
   private final ScyElo scyElo;
   private final EloBasedSearchFinished eloBasedSearchFinished;

   public BackgroundEloBasedSearch(ToolBrokerAPI tbi, EloInfoControl eloInfoControl, NewEloCreationRegistry newEloCreationRegistry, EloBasedSearcher eloBasedSearcher, ScyElo scyElo, EloBasedSearchFinished eloBasedSearchFinished)
   {
      super(tbi, eloInfoControl, newEloCreationRegistry);
      this.eloBasedSearcher = eloBasedSearcher;
      this.scyElo = scyElo;
      this.eloBasedSearchFinished = eloBasedSearchFinished;
   }

   @Override
   public void run()
   {
      if (isAbort())
      {
         return;
      }
      final List<ISearchResult> searchResults = eloBasedSearcher.findElos(scyElo);
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
      eloBasedSearchFinished.eloBasedSearchFinished(scySearchResults);
   }
}
