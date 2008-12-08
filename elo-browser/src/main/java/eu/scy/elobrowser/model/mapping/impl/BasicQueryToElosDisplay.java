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
import roolo.api.IELO;
import roolo.api.IQuery;
import roolo.api.IRepository;
import roolo.api.ISearchResult;

/**
 *
 * @author sikken
 */
public class BasicQueryToElosDisplay implements QueryToElosDisplay
{

	private IRepository repository;

	public void setRepository(IRepository repository)
	{
		this.repository = repository;
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
			displayEloMappings.add(createDisplayEloMapping(mappingElo.getMetadataDisplayMapping().getMappings(), searchResult, basicDisplayMappingListMap));
		}
		for (Mapping mapping : mappingElo.getMetadataDisplayMapping().getMappings())
		{
			if (mapping.isAutoRanging())
			{
				basicDisplayMappingListMap.get(mapping.getDisplayPropperty()).autoRange();
			}
			else
			{
				basicDisplayMappingListMap.get(mapping.getDisplayPropperty()).range(mapping.getMinimum(), mapping.getMaximum());
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
			Double displayValue = null;
			Object value = elo.getMetadata().getMetadataValueContainer(mapping.getMetadataKey()).getValue();
			if (value instanceof Number)
			{
				Number numberValue = (Number) value;
				displayValue = new Double(numberValue.doubleValue());
			}
			BasicDisplayMapping basicDisplayMapping = new BasicDisplayMapping(mapping.getDisplayPropperty(), displayValue);
			displayMappings.add(basicDisplayMapping);
			getBasicDisplayMappingList(basicDisplayMappingListMap, mapping.getDisplayPropperty()).addBasicDisplayMapping(basicDisplayMapping);
		}
		return new BasicDisplayEloMapping(elo, displayMappings);
	}
}
