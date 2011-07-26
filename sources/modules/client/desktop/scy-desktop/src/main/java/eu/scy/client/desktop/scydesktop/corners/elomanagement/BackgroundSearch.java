/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import roolo.search.ISearchResult;
import roolo.elo.api.IMetadata;

/**
 *
 * @author SikkenJ
 */
public abstract class BackgroundSearch implements Runnable
{

   private final static Logger logger = Logger.getLogger(BackgroundSearch.class);
   protected final ToolBrokerAPI tbi;
   private final NewEloCreationRegistry newEloCreationRegistry;
   private boolean abort = false;
   private long totalNanos = 0;
   private long cacheMetadataNanos = 0;
   private long convertNanos = 0;
   private int nrOfSearchResults = 0;
   private int nrOfSkippedSearchResults = 0;

   public BackgroundSearch(ToolBrokerAPI tbi, NewEloCreationRegistry newEloCreationRegistry)
   {
      this.tbi = tbi;
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
      try
      {
         doSearch();
         totalNanos = System.nanoTime() - startNanos;
         logger.info("found " + nrOfSearchResults + " and skipped " + nrOfSkippedSearchResults + " elos in "
            + nanoToMillies(totalNanos) + " ms, caching " + nanoToMillies(cacheMetadataNanos) + " ms, converting " + nanoToMillies(convertNanos)
            + " ms, other " + nanoToMillies(totalNanos - cacheMetadataNanos - convertNanos) + " ms");
      }
      catch (Exception e)
      {
         sendScySearchResuls(new ArrayList<ScySearchResult>());
         throw new RuntimeException(e);
      }
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
            //FIXME No! Just make an ELO of a result if needed. Needed means, when it is retrieved and *just* a searchresult
             ScySearchResult scySearchResult = new ScySearchResult(null, startNanos);
            searchResultUris.add(searchResult.getUri());
         }
         List<IMetadata> metadatas = tbi.getRepository().retrieveMetadatas(searchResultUris);
         cacheMetadataNanos = System.nanoTime() - startNanos;
         startNanos = System.nanoTime();
         int i = 0;
         for (ISearchResult searchResult : searchResults)
         {
            ScyElo scyElo = new ScyElo(metadatas.get(i), tbi);
            ++i;
            //XXX this skipping should now never happen!
            //TODO check if filtering is not happening here. It is implemented and working on the server side
            //now IQuery.setAllowedTypes(Set<String> allowedTypes) is used for serverside filtering
            if (newEloCreationRegistry.containsEloType(scyElo.getTechnicalFormat()))
            {
               ScySearchResult scySearchResult = new ScySearchResult(scyElo, searchResult.getRelevance());
               scySearchResults.add(scySearchResult);
               ++nrOfSearchResults;
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
