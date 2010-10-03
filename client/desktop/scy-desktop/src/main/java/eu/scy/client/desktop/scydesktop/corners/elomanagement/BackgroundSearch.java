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
public abstract class BackgroundSearch implements Runnable {
   protected final ToolBrokerAPI tbi;
   private final EloInfoControl eloInfoControl;
   private final NewEloCreationRegistry newEloCreationRegistry;
   private boolean abort = false;

   public BackgroundSearch(ToolBrokerAPI tbi, EloInfoControl eloInfoControl, NewEloCreationRegistry newEloCreationRegistry)
   {
      this.tbi = tbi;
      this.eloInfoControl = eloInfoControl;
      this.newEloCreationRegistry = newEloCreationRegistry;
    }

   public void abort()
   {
      this.abort = true;
   }

   public boolean isAbort()
   {
      return abort;
   }

   public void start(){
      new Thread(this).start();
   }

   protected abstract void setSearchResults(List<ScySearchResult> scySearchResults);

   protected List<ScySearchResult> convertToScySearchResults(List<ISearchResult> searchResults)
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

   protected void sendScySearchResuls(final List<ScySearchResult> scySearchResults){
      SwingUtilities.invokeLater(new Runnable()
      {

         @Override
         public void run()
         {
            if (!isAbort())
            {
               setSearchResults(scySearchResults);
            }
         }
      });
   }

}
