/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.Mapping;
import eu.scy.elobrowser.model.mapping.MappingTypes;
import eu.scy.elobrowser.model.mapping.MetadataDisplayMapping;
import java.util.List;

/**
 *
 * @author sikkenj
 */
public class BasicMetadataDisplayMapping implements MetadataDisplayMapping
{

	private MappingTypes mappingType;
	private List<Mapping> mappings;

	public BasicMetadataDisplayMapping(MappingTypes mappingType, List<Mapping> mappings)
	{
		this.mappingType = mappingType;
		this.mappings = mappings;
	}

	@Override
	public String toString()
	{
		return "mappingType:" + mappingType + ", mappings:" + mappings;
	}

	@Override
	public MappingTypes getMappingType()
	{
		return mappingType;
	}

	@Override
	public List<Mapping> getMappings()
	{
		return mappings;
	}

	public void setMappingType(MappingTypes mappingType)
	{
		this.mappingType = mappingType;
	}

	public void setMappings(List<Mapping> mappings)
	{
		this.mappings = mappings;
	}
}
