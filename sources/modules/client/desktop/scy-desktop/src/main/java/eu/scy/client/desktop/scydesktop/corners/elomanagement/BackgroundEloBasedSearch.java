/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import roolo.api.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class BackgroundEloBasedSearch implements Runnable
{

   private final ToolBrokerAPI tbi;
   private final EloInfoControl eloInfoControl;
   private final NewEloCreationRegistry newEloCreationRegistry;
   private final EloBasedSearcher eloBasedSearcher;
   private final ScyElo scyElo;
   private final EloBasedSearchFinished eloBasedSearchFinished;
   private boolean abort = false;

   public BackgroundEloBasedSearch(ToolBrokerAPI tbi, EloInfoControl eloInfoControl, NewEloCreationRegistry newEloCreationRegistry, EloBasedSearcher eloBasedSearcher, ScyElo scyElo, EloBasedSearchFinished eloBasedSearchFinished)
   {
      this.tbi = tbi;
      this.eloInfoControl = eloInfoControl;
      this.newEloCreationRegistry = newEloCreationRegistry;
      this.eloBasedSearcher = eloBasedSearcher;
      this.scyElo = scyElo;
      this.eloBasedSearchFinished = eloBasedSearchFinished;
   }

   public void abort()
   {
      this.abort = true;
   }

   public void start(){
      new Thread(this).start();
   }

   @Override
   public void run()
   {
      List<ISearchResult> searchResults = eloBasedSearcher.findElos(scyElo);
      if (abort)
      {
         return;
      }
      final List<ScySearchResult> scySearchResults = convertToSyySearchResults(searchResults);
      if (abort)
      {
         return;
      }
      SwingUtilities.invokeLater(new Runnable()
      {

         @Override
         public void run()
         {
            if (!abort)
            {
               eloBasedSearchFinished.eloBasedSearchFinished(scySearchResults);
            }
         }
      });
   }

   private List<ScySearchResult> convertToSyySearchResults(List<ISearchResult> searchResults)
   {
      List<ScySearchResult> scySearchResults = new ArrayList<ScySearchResult>();
      if (searchResults != null)
      {
         for (ISearchResult searchResult : searchResults)
         {
            final String eloType = eloInfoControl.getEloType(searchResult.getUri());
            if (newEloCreationRegistry.containsEloType(eloType))
            {
               ScySearchResult scySearchResult = new ScySearchResult(ScyElo.loadMetadata(searchResult.getUri(), tbi), searchResult.getRelevance());
               scySearchResults.add(scySearchResult);
            }
         }
      }
      return scySearchResults;
   }
}
