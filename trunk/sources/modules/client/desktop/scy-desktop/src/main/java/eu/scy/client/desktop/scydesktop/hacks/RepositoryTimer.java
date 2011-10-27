/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.hacks;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.localtoolbroker.DirectoryUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.search.IQuery;
import roolo.search.ISearchResult;

/**
 *
 * @author SikkenJ
 */
public class RepositoryTimer implements IRepository
{

   private final static Logger logger = Logger.getLogger(RepositoryTimer.class);
   private IRepository repository;
   private final String dateFormatString = "HH:mm:ss.SSS";
   private final DateFormat dateFomat = new SimpleDateFormat(dateFormatString);
   private final String logFileName = "rooloAccess";
   private final String logFileNameExtension = ".txt";
   private long usedNanos = 0;
   private int nrOfCalls = 0;
   private PrintStream logStream = System.out;
   private QueryUtils queryUtils = new QueryUtils();
   private long minimumSleepTime = 0;
   private long maximumSleepTime = 0;
   private Random random = new Random(0);
   private final Object logLock = new Object();

   public RepositoryTimer() throws FileNotFoundException
   {
      setupLogStream();
   }

   private void setupLogStream() throws FileNotFoundException
   {
      String logDirectory = System.getProperty("loggingDirectory");
      if (!StringUtils.isEmpty(logDirectory))
      {
         File file = DirectoryUtils.getLogFile(logDirectory, logFileName, logFileNameExtension);
         logStream = new PrintStream(file);
      }
   }

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   public long getMaximumSleepTime()
   {
      return maximumSleepTime;
   }

   public void setMaximumSleepTime(long maximumSleepTime)
   {
      this.maximumSleepTime = maximumSleepTime;
   }

   public long getMinimumSleepTime()
   {
      return minimumSleepTime;
   }

   public void setMinimumSleepTime(long minimumSleepTime)
   {
      this.minimumSleepTime = minimumSleepTime;
   }

   private void sleep()
   {
      if (maximumSleepTime > 0)
      {
         int maxRandomSleepTime = (int) (maximumSleepTime - minimumSleepTime);
         long sleepTime = minimumSleepTime + random.nextInt(maxRandomSleepTime);
         try
         {
            Thread.sleep(sleepTime);
         }
         catch (InterruptedException ex)
         {
            logger.info("RepositoryTimer.sleep() intererupted ");
         }
      }
   }

   private void logCall(long startNanos, String callName, String... params)
   {
      long usedNanosInCall = System.nanoTime() - startNanos;
      usedNanos += usedNanosInCall;
      nrOfCalls++;
      if (logStream == null)
      {
         return;
      }
      String logLine = String.format("%s\t%4.1f\t%s\t%s", dateFomat.format(new Date()), usedNanosInCall / 1e6, callName, Thread.currentThread().getName());
      synchronized (logLock)
      {
         if (params.length == 0)
         {
            logStream.println(logLine);
         }
         else
         {
            logStream.printf("%s\t%s\n", logLine, params[0]);
            for (int i = 1; i < params.length; i++)
            {
               logStream.printf("\t\t\t\t%s\n", params[i]);
            }
         }
         logStream.flush();
      }
      sleep();
   }

   private void logCall(long startNanos, String callName, URI... uris)
   {
      String[] params = new String[uris.length];
      for (int i = 0; i < uris.length; i++)
      {
         params[i] = uris[i] == null ? "null" : uris[i].toString();
      }
      logCall(startNanos, callName, params);
   }

   @Override
   public IMetadata addNewELO(IELO elo)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.addNewELO(elo);
      logCall(startNanos, "addNewELO(IELO)", elo.getUri());
      return metadata;
   }

   @Override
   public IMetadata addForkedELO(IELO elo)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.addForkedELO(elo);
      logCall(startNanos, "addForkedELO(IELO)", elo.getUri());
      return metadata;
   }

   @Override
   public IMetadata addForkedELO(IELO elo, URI parentUri)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.addForkedELO(elo, parentUri);
      logCall(startNanos, "addForkedELO(IELO,URI)", elo.getUri());
      return metadata;
   }

   @Override
   public IMetadata updateELO(IELO elo)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.updateELO(elo);
      logCall(startNanos, "updateELO(IELO)", elo.getUri());
      return metadata;
   }

   @Override
   public IMetadata updateELO(IELO elo, URI parentUri)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.updateELO(elo, parentUri);
      logCall(startNanos, "updateELO(IELO,URI)", elo.getUri());
      return metadata;
   }

   @Override
   public void addMetadata(URI uri, IMetadata metadata)
   {
      long startNanos = System.nanoTime();
      repository.addMetadata(uri, metadata);
      logCall(startNanos, "addMetadata(URI,IMetadata)", uri);
   }

   @Override
   public void deleteELO(URI uri)
   {
      long startNanos = System.nanoTime();
      repository.deleteELO(uri);
      logCall(startNanos, "deleteELO(URI)", uri);
   }

   @Override
   public IELO retrieveELO(URI uri)
   {
      long startNanos = System.nanoTime();
      IELO elo = repository.retrieveELO(uri);
      logCall(startNanos, "retrieveELO(URI)", uri);
      return elo;
   }

   @Override
   public IELO retrieveELOLastVersion(URI uri)
   {
      long startNanos = System.nanoTime();
      IELO elo = repository.retrieveELOLastVersion(uri);
      logCall(startNanos, "retrieveELOLastVersion(URI)", uri);
      return elo;
   }

   @Override
   public IELO retrieveELOFirstVersion(URI uri)
   {
      long startNanos = System.nanoTime();
      IELO elo = repository.retrieveELOFirstVersion(uri);
      logCall(startNanos, "retrieveELOFirstVersion(URI)", uri);
      return elo;
   }

   @Override
   public List<IELO> retrieveELOAllVersions(URI uri)
   {
      long startNanos = System.nanoTime();
      List<IELO> elos = repository.retrieveELOAllVersions(uri);
      logCall(startNanos, "retrieveELOAllVersions(URI)", uri);
      return elos;
   }

   @Override
   public List<IELO> retrieveELOs(List<URI> uris)
   {
      long startNanos = System.nanoTime();
      List<IELO> elos = repository.retrieveELOs(uris);
      logCall(startNanos, "retrieveELOs(List<URI>)", uris.toArray(new URI[0]));
      return elos;
   }

   @Override
   public IMetadata retrieveMetadata(URI uri)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.retrieveMetadata(uri);
      logCall(startNanos, "retrieveMetadata(URI)", uri);
      return metadata;
   }

   @Override
   public IMetadata retrieveMetadataLastVersion(URI uri)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.retrieveMetadataLastVersion(uri);
      logCall(startNanos, "retrieveMetadataLastVersion(URI)", uri);
      return metadata;
   }

   @Override
   public IMetadata retrieveMetadataFirstVersion(URI uri)
   {
      long startNanos = System.nanoTime();
      IMetadata metadata = repository.retrieveMetadataFirstVersion(uri);
      logCall(startNanos, "retrieveMetadataFirstVersion(URI)", uri);
      return metadata;
   }

   @Override
   public List<IMetadata> retrieveMetadataAllVersions(URI uri)
   {
      long startNanos = System.nanoTime();
      List<IMetadata> metadatas = repository.retrieveMetadataAllVersions(uri);
      logCall(startNanos, "retrieveMetadataAllVersions(URI)", uri);
      return metadatas;
   }

   @Override
   public List<IMetadata> retrieveMetadatas(List<URI> uris)
   {
      long startNanos = System.nanoTime();
      List<IMetadata> metadatas = repository.retrieveMetadatas(uris);
      logCall(startNanos, "retrieveMetadatas(List<URI>)", uris.toArray(new URI[0]));
      return metadatas;
   }

   @Override
   public List<ISearchResult> search(IQuery query)
   {
      long startNanos = System.nanoTime();
      List<ISearchResult> searchResults = repository.search(query);
      logCall(startNanos, "search(IQuery)", queryUtils.queryToString(query));
      return searchResults;
   }

   @Override
   public int getHits(IQuery query)
   {
      long startNanos = System.nanoTime();
      int hits = repository.getHits(query);
      logCall(startNanos, "getHits(IQuery)", queryUtils.queryToString(query));
      return hits;
   }

   @Override
   public List<ISearchResult> listAllElos()
   {
      long startNanos = System.nanoTime();
      List<ISearchResult> searchResults = repository.listAllElos();
      logCall(startNanos, "listAllElos()", new String[0]);
      return searchResults;
   }

   @Override
   public void archiveELO(URI uri)
   {
      long startNanos = System.nanoTime();
      repository.archiveELO(uri);
      logCall(startNanos, "archiveELO(URI)", uri);
   }

   @Override
   public void unarchiveELO(URI uri)
   {
      long startNanos = System.nanoTime();
      repository.unarchiveELO(uri);
      logCall(startNanos, "unarchiveELO(URI)", uri);
   }

   @Override
   public List<IELO> retrieveAllELOs()
   {
      long startNanos = System.nanoTime();
      List<IELO> elos = repository.retrieveAllELOs();
      logCall(startNanos, "retrieveAllELOs()", new String[0]);
      return elos;
   }

   @Override
   public IMetadata updateWithMinorChange(IELO elo)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public List<Map<Integer, URI>> getAllVersionLists()
   {
      long startNanos = System.nanoTime();
      List<Map<Integer, URI>> versionLists = repository.getAllVersionLists();
      logCall(startNanos, "getAllVersionLists()", new String[0]);
      return versionLists;
   }
}
