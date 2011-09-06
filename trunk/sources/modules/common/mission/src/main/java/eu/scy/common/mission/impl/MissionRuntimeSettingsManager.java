package eu.scy.common.mission.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import roolo.elo.api.exceptions.ELONotLastVersionException;
import eu.scy.common.mission.RuntimeSetting;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsManager;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;

/**
 * This implementation of the RuntimeSettingsManager handles the search for settings in the correct
 * sequence (elo user dependent, elo user independent, las user dependent, las user independent,
 * mission user dependent, mission user independent).
 * 
 * Any setting changes are stored in the runtimeRuntimeSettingsElo. If this elo has been updated
 * else where, it will reload the elo. As the elo is being updated for each addSettings call, it is
 * advised to add all new runtime settings in one call.
 * 
 * This implementation is thread save.
 * 
 * @author SikkenJ
 * 
 */
public class MissionRuntimeSettingsManager implements RuntimeSettingsManager
{

   private static final Logger logger = Logger.getLogger(MissionRuntimeSettingsManager.class);
   // private final RuntimeSettingsElo specificationRuntimeSettingsElo;
   private RuntimeSettingsElo runtimeRuntimeSettingsElo;
   private final Set<URI> specificationContentEloUris;
   private final RooloServices rooloServices;
   private final RuntimeSettingsManager specificationRuntimeSettings;
   private RuntimeSettingsManager runtimeRuntimeSettings;
   private final Object addSettingsLock = new Object();
   // a ConcurrentHashMap cannot store a null value, so use a special null value
   private final URI nullUri = new URI("");
   private final Map<URI, URI> specificationEloUriMap = new ConcurrentHashMap<URI, URI>();

   public MissionRuntimeSettingsManager(RuntimeSettingsElo specificationRuntimeSettingsElo,
      RuntimeSettingsElo runtimeRuntimeSettingsElo, Set<URI> specificationContentEloUris,
      RooloServices rooloServices) throws URISyntaxException
   {
      super();
      // this.specificationRuntimeSettingsElo = specificationRuntimeSettingsElo;
      this.runtimeRuntimeSettingsElo = runtimeRuntimeSettingsElo;
      // the set with the specification content elo uris is only used for looking up,
      // it is not modified, thus a HashSet is thread save
      this.specificationContentEloUris = new HashSet<URI>(specificationContentEloUris);
      this.rooloServices = rooloServices;
      if (specificationRuntimeSettingsElo != null)
      {
         this.specificationRuntimeSettings = specificationRuntimeSettingsElo.getTypedContent();
      }
      else
      {
         this.specificationRuntimeSettings = null;
      }
      if (runtimeRuntimeSettingsElo != null)
      {
         this.runtimeRuntimeSettings = runtimeRuntimeSettingsElo.getTypedContent();
      }
      else
      {
         logger.warn("runtimeRuntimeSettingsElo should not be null");

      }
   }

   @Override
   public String getSetting(RuntimeSettingKey key)
   {
      if (key == null)
      {
         return null;
      }
      String value = null;
      if (runtimeRuntimeSettings != null)
      {
         value = runtimeRuntimeSettings.getSetting(key);
      }
      if (value != null)
      {
         return value;
      }
      if (specificationRuntimeSettings != null)
      {
         URI specificationEloUri = null;
         if (!key.isEloUriEmpty())
         {
            specificationEloUri = getSpecificationEloUri(key.getEloUri());
         }
         value = specificationRuntimeSettings.getSetting(new RuntimeSettingKey(key.getName(), key.getLasId(), specificationEloUri));
         if (value != null)
         {
            return value;
         }
      }
      if (!key.isEloUriEmpty())
      {
         return getSetting(new RuntimeSettingKey(key.getName(), key.getLasId(), null));
      }
      if (!key.isLasIdEmpty())
      {
         return getSetting(new RuntimeSettingKey(key.getName(), null, null));
      }

      return null;
   }

   private URI getSpecificationEloUri(URI eloUri)
   {
      if (specificationEloUriMap.containsKey(eloUri))
      {
         URI uri = specificationEloUriMap.get(eloUri);
         if (nullUri.equals(uri))
         {
            return null;
         }
         return uri;
      }
      URI specificationEloUri = findSpecificationEloUri(eloUri);
      specificationEloUriMap.put(eloUri, specificationEloUri == null ? nullUri
         : specificationEloUri);
      return specificationEloUri;
   }

   private URI findSpecificationEloUri(URI eloUri)
   {
      ScyElo scyElo = ScyElo.loadMetadata(eloUri, rooloServices);
      if (scyElo == null)
      {
         return null;
      }
      if (specificationContentEloUris.contains(scyElo.getUri()))
      {
         return scyElo.getUri();
      }

      if (!eloUri.equals(scyElo.getUriFirstVersion()))
      {
         scyElo = ScyElo.loadMetadata(scyElo.getUriFirstVersion(), rooloServices);
      }
      return findSpecificationEloUri(scyElo.getIsForkedOfEloUri());
   }

   @Override
   public List<String> getSettings(List<RuntimeSettingKey> keys)
   {
      List<String> values = new ArrayList<String>(keys.size());
      for (RuntimeSettingKey key : keys)
      {
         values.add(getSetting(key));
      }
      return values;
   }

   @Override
   public void addSetting(RuntimeSettingKey key, String value)
   {
      addSetting(new RuntimeSetting(key, value));
   }

   @Override
   public void addSetting(RuntimeSetting runtimeSetting)
   {
      List<RuntimeSetting> runtimeSettings = new ArrayList<RuntimeSetting>(1);
      runtimeSettings.add(runtimeSetting);
      addSettings(runtimeSettings);
   }

   @Override
   public void addSettings(List<RuntimeSetting> runtimeSettings)
   {
      if (runtimeRuntimeSettings == null)
      {
         return;
      }
      synchronized (addSettingsLock)
      {
         runtimeRuntimeSettings.addSettings(runtimeSettings);
         try
         {
            runtimeRuntimeSettingsElo.updateElo();
         }
         catch (ELONotLastVersionException e)
         {
            // some one else has updated the runtime settings elo
            // get the latest version and try again
            runtimeRuntimeSettingsElo = RuntimeSettingsElo.loadLastVersionElo(
               runtimeRuntimeSettingsElo.getUri(), rooloServices);
            runtimeRuntimeSettings = runtimeRuntimeSettingsElo.getTypedContent();
            addSettings(runtimeSettings);
         }
      }
   }
}
