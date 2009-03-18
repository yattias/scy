/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.DisplayEloMapping;
import eu.scy.elobrowser.model.mapping.DisplayMapping;
import eu.scy.elobrowser.model.mapping.DisplayProperty;
import eu.scy.elobrowser.model.mapping.Mapping;
import eu.scy.elobrowser.model.mapping.MappingElo;
import eu.scy.elobrowser.model.mapping.QueryToElosDisplay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.RooloMetadataKeys;

/**
 *
 * @author sikken
 */
public class BasicQueryToElosDisplay implements QueryToElosDisplay
{

   private static final Logger logger = Logger.getLogger(BasicQueryToElosDisplay.class);
   private IRepository repository;
   private IMetadataTypeManager metadataTypeManager;
   private IMetadataKey typeKey;

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      this.metadataTypeManager = metadataTypeManager;
      typeKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());
   }

   private DisplayMappingList getBasicDisplayMappingList(
      Map<DisplayProperty, DisplayMappingList> basicDisplayMappingListMap,
      DisplayProperty displayProperty)
   {
      DisplayMappingList displayMappingList = basicDisplayMappingListMap.get(displayProperty);
      if (displayMappingList == null)
      {
         displayMappingList = new DisplayMappingList();
         basicDisplayMappingListMap.put(displayProperty, displayMappingList);
      }
      return displayMappingList;
   }

   @Override
   public List<DisplayEloMapping> getDisplayEloMapping(MappingElo mappingElo, IQuery query)
   {
      Map<DisplayProperty, DisplayMappingList> basicDisplayMappingListMap = new HashMap<DisplayProperty, DisplayMappingList>();
      List<DisplayEloMapping> displayEloMappings = new ArrayList<DisplayEloMapping>();
      List<ISearchResult> searchResults = repository.search(query);
      for (ISearchResult searchResult : searchResults)
      {
//		  logger.debug("searchResult:" + searchResult);
         displayEloMappings.add(createDisplayEloMapping(mappingElo.getMetadataDisplayMapping().getMappings(), searchResult, basicDisplayMappingListMap));
      }
      for (Mapping mapping : mappingElo.getMetadataDisplayMapping().getMappings())
      {
         DisplayMappingList displayMappingList = basicDisplayMappingListMap.get(mapping.getDisplayPropperty());
         if (displayMappingList != null)
         {
            if (mapping.isAutoRanging())
            {
               displayMappingList.autoRange();
            }
            else
            {
               displayMappingList.range(mapping.getMinimum(), mapping.getMaximum());
            }
         }
      }
      return displayEloMappings;
   }

   private DisplayEloMapping createDisplayEloMapping(List<Mapping> mappings,
      ISearchResult searchResult,
      Map<DisplayProperty, DisplayMappingList> basicDisplayMappingListMap)
   {
      IELO elo = repository.retrieveELO(searchResult.getUri());
      List<DisplayMapping> displayMappings = new ArrayList<DisplayMapping>();
      for (Mapping mapping : mappings)
      {
         Float displayValue = null;
         Object value = elo.getMetadata().getMetadataValueContainer(mapping.getMetadataKey()).getValue();
         if (value instanceof Number)
         {
            Number numberValue = (Number) value;
            displayValue = new Float(numberValue.floatValue());
         }
         BasicDisplayMapping basicDisplayMapping = new BasicDisplayMapping(mapping.getDisplayPropperty(), displayValue);
         displayMappings.add(basicDisplayMapping);
         getBasicDisplayMappingList(basicDisplayMappingListMap, mapping.getDisplayPropperty()).addBasicDisplayMapping(basicDisplayMapping);
      }
      String eloType = (String) elo.getMetadata().getMetadataValueContainer(typeKey).getValue();
      return new BasicDisplayEloMapping(elo, displayMappings, eloType, true);
   }
}
