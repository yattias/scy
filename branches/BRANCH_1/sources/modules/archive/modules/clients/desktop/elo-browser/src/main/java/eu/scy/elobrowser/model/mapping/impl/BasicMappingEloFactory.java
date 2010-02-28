/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.MappingElo;
import eu.scy.elobrowser.model.mapping.MappingEloFactory;
import eu.scy.elobrowser.model.mapping.XmlMetadataDisplayMappingConverter;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.RooloMetadataKeys;

/**
 *
 * @author sikkenj
 */
public class BasicMappingEloFactory implements MappingEloFactory
{

	public final static String metadataDisplayMappingFormat = "scy/eloBrowser/metadataDisplayMapping";
	public final static String name = "scy/eloBrowser/metadataDisplayMapping";
	private XmlMetadataDisplayMappingConverter xmlMetadataDisplayMappingConverter;
	private IMetadataTypeManager metadataTypeManager;
	private IELOFactory<IMetadataKey> eloFactory;
	private IMetadataKey nameKey;
	private IMetadataKey descriptionKey;
	private IMetadataKey formatKey;

	public void setDescriptionKey(IMetadataKey descriptionKey)
	{
		this.descriptionKey = descriptionKey;
	}

	public void setEloFactory(IELOFactory<IMetadataKey> eloFactory)
	{
		this.eloFactory = eloFactory;
	}

	public void setFormatKey(IMetadataKey formatKey)
	{
		this.formatKey = formatKey;
	}

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
	{
		this.metadataTypeManager = metadataTypeManager;
	}

	public void setNameKey(IMetadataKey nameKey)
	{
		this.nameKey = nameKey;
	}

	public void setXmlMetadataDisplayMappingConverter(XmlMetadataDisplayMappingConverter xmlMetadataDisplayMappingConverter)
	{
		this.xmlMetadataDisplayMappingConverter = xmlMetadataDisplayMappingConverter;
	}

	public void initialized()
	{
		if (formatKey==null)
			formatKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());
		if (nameKey==null)
			nameKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TITLE.getId());
		if (descriptionKey==null)
			descriptionKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TITLE.getId());
	}
	
	@Override
	public MappingElo createMappingElo()
	{
		IELO<IMetadataKey> elo = eloFactory.createELO();
		elo.getMetadata().getMetadataValueContainer(formatKey).setValue(metadataDisplayMappingFormat);
		MappingElo mappingElo = new BasicMappingElo(elo, nameKey, descriptionKey, xmlMetadataDisplayMappingConverter);
		return mappingElo;
	}

	@Override
	public MappingElo createMappingElo(IELO<IMetadataKey> elo)
	{
		@SuppressWarnings("unchecked")
		String eloFormat = (String) elo.getMetadata().getMetadataValueContainer(formatKey).getValue();
		if (!metadataDisplayMappingFormat.equals(eloFormat))
		{
			throw new IllegalArgumentException("elo is not a mapping elo, its format is " + eloFormat);
		}
		return realCreateMappingElo(elo);
	}

	private MappingElo realCreateMappingElo(IELO<IMetadataKey> elo)
	{
		MappingElo mappingElo = new BasicMappingElo(elo, nameKey, descriptionKey, xmlMetadataDisplayMappingConverter);
		return mappingElo;
	}
}
