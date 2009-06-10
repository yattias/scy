/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.impl;

import eu.scy.elobrowser.model.mapping.DisplayProperty;
import eu.scy.elobrowser.model.mapping.Mapping;
import eu.scy.elobrowser.model.mapping.MappingTypes;
import eu.scy.elobrowser.model.mapping.MetadataDisplayMapping;
import eu.scy.elobrowser.model.mapping.XmlMetadataDisplayMappingConverter;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author sikkenj
 */
public class JDomXmlMetadataDisplayMappingConverter implements XmlMetadataDisplayMappingConverter
{

	private final static Logger logger = Logger.getLogger(JDomXmlMetadataDisplayMappingConverter.class);
	private JDomStringConversion jdomStringConversion = new JDomStringConversion();
	private IMetadataTypeManager metadataTypeManager;

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
	{
		this.metadataTypeManager = metadataTypeManager;
	}

	@Override
	public String toXmlString(MetadataDisplayMapping metadataDisplayMapping)
	{
		Element root = new Element(XmlNames.metadataDisplayMapping);
		root.addContent(createElement(XmlNames.mappingType, metadataDisplayMapping.getMappingType().name().toLowerCase()));
		for (Mapping mapping : metadataDisplayMapping.getMappings())
		{
			Element mappingElement = new Element(XmlNames.mapping);
			mappingElement.addContent(createElement(XmlNames.display, mapping.getDisplayPropperty().name().toLowerCase()));
			mappingElement.addContent(createElement(XmlNames.metadata, mapping.getMetadataKey().getId()));
			if (!mapping.isAutoRanging())
			{
				mappingElement.addContent(createElement(XmlNames.minimum, Double.toString(mapping.getMinimum())));
				mappingElement.addContent(createElement(XmlNames.maximum, Double.toString(mapping.getMaximum())));
			}
			root.addContent(mappingElement);
		}
		String xmlString = jdomStringConversion.xmlToString(root);
		return xmlString;
	}

	private Element createElement(String name, String value)
	{
		Element element = new Element(name);
		element.setText(value);
		return element;
	}

	@Override
	public MetadataDisplayMapping fromXmlString(String xmlString)
	{
		//logger.debug("xml: " + xmlString);
		Element root = jdomStringConversion.stringToXml(xmlString);
		MappingTypes mappingType = MappingTypes.valueOf(root.getChildTextNormalize(XmlNames.mappingType).toUpperCase());
		List<Mapping> mappings = new ArrayList<Mapping>();
		@SuppressWarnings("unchecked")
		List<Element> mappingChildren = root.getChildren(XmlNames.mapping);
		for (Element mappingElement : mappingChildren)
		{
			Mapping mapping;
			DisplayProperty displayPropperty = DisplayProperty.valueOf(mappingElement.getChildTextNormalize(XmlNames.display).toUpperCase());
			String metadataKeyId = mappingElement.getChildTextNormalize(XmlNames.metadata);
			IMetadataKey metadataKey = metadataTypeManager.getMetadataKey(metadataKeyId);
			String minimumString = root.getChildTextNormalize(XmlNames.minimum);
			if (minimumString == null)
			{
				mapping = new BasicMapping(displayPropperty, metadataKey);
			} else
			{
				float minimum = Float.parseFloat(minimumString);
				String maximumString = root.getChildTextNormalize(XmlNames.maximum);
				float maximum = Float.parseFloat(maximumString);
				mapping = new BasicMapping(displayPropperty, metadataKey, minimum, maximum);
			}
			mappings.add(mapping);
		}
		return new BasicMetadataDisplayMapping(mappingType, mappings);
	}
}
