/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IMetadata;

/**
 *
 * @author SikkenJ
 */
public abstract class BackgroundSearch implements Runnable
{

   private final static Logger logger = Logger.getLogger(BackgroundSearch.class);
   protected final ToolBrokerAPI tbi;
   private final EloInfoControl eloInfoControl;
   private final NewEloCreationRegistry newEloCreationRegistry;
   private boolean abort = false;
   private long totalNanos = 0;
   private long cacheMetadataNanos = 0;
   private long convertNanos = 0;
   private int nrOfSearchResults = 0;
   private int nrOfSkippedSearchResults = 0;

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

   public void start()
   {
      new Thread(this).start();
   }

   @Override
   public void run()
   {
      long startNanos = System.nanoTime();
      doSearch();
      totalNanos = System.nanoTime() - startNanos;
      logger.info("found " + nrOfSearchResults + " and skipped " + nrOfSkippedSearchResults + " elos in "
         + nanoToMillies(totalNanos) + " ms, caching " + nanoToMillies(cacheMetadataNanos) + " ms, converting " + nanoToMillies(convertNanos)
         + " ms, other " + nanoToMillies(totalNanos - cacheMetadataNanos - convertNanos) + " ms");
   }

   private String nanoToMillies(long nanos)
   {
      return "" + (nanos / 100000 / 10.0);
   }

   protected abstract void doSearch();

   protected abstract void setSearchResults(List<ScySearchResult> scySearchResults);

   protected List<ScySearchResult> convertToScySearchResults(List<ISearchResult> searchResults)
   {
      long startNanos = System.nanoTime();
      List<ScySearchResult> scySearchResults = new ArrayList<ScySearchResult>();
      if (searchResults != null)
      {
         startNanos = System.nanoTime();
         List<URI> searchResultUris = new ArrayList<URI>();
         for (ISearchResult searchResult : searchResults)
         {
            final String eloType = eloInfoControl.getEloType(searchResult.getUri());
            if (newEloCreationRegistry.containsEloType(eloType))
            {
               searchResultUris.add(searchResult.getUri());
            }
         }
         List<IMetadata> metadatas = tbi.getRepository().retrieveMetadatas(searchResultUris);
         cacheMetadataNanos = System.nanoTime() - startNanos;
         startNanos = System.nanoTime();
         int i = 0;
         for (ISearchResult searchResult : searchResults)
         {
            final String eloType = eloInfoControl.getEloType(searchResult.getUri());
            if (newEloCreationRegistry.containsEloType(eloType))
            {
               ScyElo scyElo = new ScyElo(metadatas.get(i), tbi);
               ScySearchResult scySearchResult = new ScySearchResult(scyElo, searchResult.getRelevance());
               scySearchResults.add(scySearchResult);
               ++nrOfSearchResults;
               ++i;
            }
            else
            {
               ++nrOfSkippedSearchResults;
            }
         }
         convertNanos = System.nanoTime() - startNanos;
      }
      return scySearchResults;
   }

   protected void sendScySearchResuls(final List<ScySearchResult> scySearchResults)
   {
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
