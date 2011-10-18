/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.scyelo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;

/**
 *
 * @author SikkenJ
 */
public class MultiScyEloLoader {
   private final RooloServices rooloServices;
   private final List<URI> uris;
   private final boolean onlyMetadata;
   private final Map<URI, ScyElo> scyEloMap = new HashMap<URI, ScyElo>();

   public MultiScyEloLoader(List<URI> uris, boolean onlyMetadata, RooloServices rooloServices)
   {
      this.uris = uris;
      this.onlyMetadata = onlyMetadata;
      this.rooloServices = rooloServices;
      loadScyElos();
   }

   public MultiScyEloLoader(URI[] uris, boolean onlyMetadata, RooloServices rooloServices)
   {
      this.rooloServices = rooloServices;
      this.onlyMetadata = onlyMetadata;
      this.uris = new ArrayList<URI>();
      this.uris.addAll(Arrays.asList(uris));
      loadScyElos();
   }

   private void loadScyElos()
   {
      if (onlyMetadata)
      {
         loadMetadataScyElos();
      }
      else
      {
         loadCompleteScyElos();
      }
   }

   private void loadCompleteScyElos()
   {
      List<IELO> elos = rooloServices.getRepository().retrieveELOs(uris);
      for (IELO elo : elos)
      {
         if (elo != null)
         {
            final ScyElo scyElo = new ScyElo(elo, rooloServices);
            scyEloMap.put(scyElo.getUri(), scyElo);
         }
      }
   }

   private void loadMetadataScyElos()
   {
      List<IMetadata> elos = rooloServices.getRepository().retrieveMetadatas(uris);
      for (IMetadata elo : elos)
      {
         if (elo != null)
         {
            final ScyElo scyElo = new ScyElo(elo, rooloServices);
            scyEloMap.put(scyElo.getUri(), scyElo);
         }
      }
   }

   public ScyElo getScyElo(URI uri)
   {
      return scyEloMap.get(uri);
   }

}
