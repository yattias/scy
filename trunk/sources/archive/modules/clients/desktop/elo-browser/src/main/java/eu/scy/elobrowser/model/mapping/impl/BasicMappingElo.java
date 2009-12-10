/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.Mapping;
import eu.scy.elobrowser.model.mapping.MappingElo;
import eu.scy.elobrowser.model.mapping.MappingTypes;
import eu.scy.elobrowser.model.mapping.MetadataDisplayMapping;
import eu.scy.elobrowser.model.mapping.XmlMetadataDisplayMappingConverter;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.MetadataSingleLanguageValueAccessor;
import roolo.elo.content.BasicContent;

/**
 *
 * @author sikkenj
 */
public class BasicMappingElo implements MappingElo,Cloneable
{
	private static final Logger logger = Logger.getLogger(BasicMappingElo.class);
	private final XmlMetadataDisplayMappingConverter xmlMetadataDisplayMappingConverter;
	private IELO elo;
	private MetadataSingleLanguageValueAccessor<String> nameValueAccessor;
	private MetadataSingleLanguageValueAccessor<String> descriptionValueAccessor;
	private MetadataDisplayMapping metadataDisplayMapping;
	private final IMetadataKey nameMetadataKey;
	private final IMetadataKey descriptionMetadataKey;

	BasicMappingElo(IELO<IMetadataKey> elo, IMetadataKey nameMetadataKey, IMetadataKey descriptionMetadataKey, XmlMetadataDisplayMappingConverter xmlMetadataDisplayMappingConverter)
	{
		this.elo = elo;
		this.nameMetadataKey = nameMetadataKey;
		this.descriptionMetadataKey = descriptionMetadataKey;
		nameValueAccessor = new MetadataSingleLanguageValueAccessor<String>(elo, nameMetadataKey);
		descriptionValueAccessor = new MetadataSingleLanguageValueAccessor<String>(elo, descriptionMetadataKey);
		this.xmlMetadataDisplayMappingConverter = xmlMetadataDisplayMappingConverter;
		IContent content = null;
		// TODO remove elo!=null check!
		if (elo!=null)
			content = elo.getContent();
		if (content != null && content.getXmlString()!=null)
		{
			metadataDisplayMapping = xmlMetadataDisplayMappingConverter.fromXmlString(content.getXmlString());
		} 
		else
		{
			metadataDisplayMapping = new BasicMetadataDisplayMapping(MappingTypes.DIRECT, new ArrayList<Mapping>());
		}
	}

	@Override
	public BasicMappingElo clone()
	{
		try
		{
			BasicMappingElo basicMappingElo = (BasicMappingElo) super.clone();
			basicMappingElo.elo = elo.clone();
			basicMappingElo.nameValueAccessor = new MetadataSingleLanguageValueAccessor<String>(elo, nameMetadataKey);
			basicMappingElo.descriptionValueAccessor = new MetadataSingleLanguageValueAccessor<String>(elo, descriptionMetadataKey);
			return basicMappingElo;
		}
		catch (CloneNotSupportedException ex)
		{
			logger.error("failed to clone", ex);
			throw new IllegalStateException("failed to clone", ex);
		}
	}



	@Override
	public String toString()
	{
		return "name:"+getName()+", description:" + getDescription() + ", metadataDisplayMapping:" + metadataDisplayMapping;
	}
	
	@Override
	public IELO getElo()
	{
		elo.setContent(new BasicContent(xmlMetadataDisplayMappingConverter.toXmlString(metadataDisplayMapping)));
		return elo;
	}

	@Override
	public void setName(String name)
	{
		nameValueAccessor.setValue(name);
	}

	@Override
	public String getName()
	{
		return nameValueAccessor.getValue();
	}

	@Override
	public void setDescription(String description)
	{
		descriptionValueAccessor.setValue(description);
	}

	@Override
	public String getDescription()
	{
		return descriptionValueAccessor.getValue();
	}

	@Override
	public void setMetadataDisplayMapping(MetadataDisplayMapping metadataDisplayMapping)
	{
		this.metadataDisplayMapping = metadataDisplayMapping;
	}

	@Override
	public MetadataDisplayMapping getMetadataDisplayMapping()
	{
		return metadataDisplayMapping;
	}
}
