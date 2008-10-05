package eu.scy.agents.roolo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jdom.Element;

import roolo.api.IELO;
import roolo.api.IMetadata;
import roolo.api.IMetadataKey;
import roolo.api.IMetadataValueContainer;
import roolo.cms.elo.JDomStringConversion;

public class DrawAnalyserAgent<T extends IELO<K>, K extends IMetadataKey> extends
			AbstractRooloAgent<T, K>
{
	private static final String scyDrawType = "scy/drawing";
	private static final String contentTagName = "content";
	private JDomStringConversion jdomStringConversion = new JDomStringConversion();
	private K formatKey;
	private K lineCountKey;
	private K rectangleCountKey;
	private K ovalCountKey;
	private K freehandCountKey;
	private K textCountKey;
	private K imageCountKey;
	private K tagPointerCountKey;

	@Override
	public IMetadata<K> addELO(T elo)
	{
		if (isCompatibleType(elo))
			analyseElo(elo);
		return super.addELO(elo);
	}

	@Override
	public IMetadata<K> updateELO(T elo)
	{
		if (isCompatibleType(elo))
			analyseElo(elo);
		return super.updateELO(elo);
	}

	private boolean isCompatibleType(T elo)
	{
		return scyDrawType.equals(elo.getMetadata().getMetadataValueContainer(formatKey).getValue());
	}

	private synchronized void analyseElo(T elo)
	{
		String contentXml = elo.getContent().getXml();
		Element drawRoot = jdomStringConversion.stringToXml(contentXml);
		NameCounters nameCounters = new NameCounters();
		scanElements(drawRoot, nameCounters);
		placeNameCountersInMetadata(elo.getMetadata(), nameCounters);
	}

	private void scanElements(Element element, NameCounters nameCounters)
	{
		@SuppressWarnings("unchecked")
		List<Element> children = element.getChildren();
		if (contentTagName.equals(element.getName()))
		{
			if (children.size() > 0)
			{
				String childTagName = children.get(0).getName();
				nameCounters.increment(childTagName);
			}
		}
		else
		{
			for (Element child : children)
			{
				scanElements(child, nameCounters);
			}
		}
	}

	private void placeNameCountersInMetadata(IMetadata<K> metatdata, NameCounters nameCounters)
	{
		Map<String, AtomicInteger> counters = nameCounters.getCounters();
		setCountMetadataValue(metatdata.getMetadataValueContainer(lineCountKey), counters
					.get(WhiteboardXmlNames.line));
		setCountMetadataValue(metatdata.getMetadataValueContainer(rectangleCountKey), counters
					.get(WhiteboardXmlNames.rectangle));
		setCountMetadataValue(metatdata.getMetadataValueContainer(ovalCountKey), counters
					.get(WhiteboardXmlNames.oval));
		setCountMetadataValue(metatdata.getMetadataValueContainer(freehandCountKey), counters
					.get(WhiteboardXmlNames.freehand));
		setCountMetadataValue(metatdata.getMetadataValueContainer(textCountKey), counters
					.get(WhiteboardXmlNames.text));
		setCountMetadataValue(metatdata.getMetadataValueContainer(imageCountKey), counters
					.get(WhiteboardXmlNames.image));
		setCountMetadataValue(metatdata.getMetadataValueContainer(tagPointerCountKey), counters
					.get(WhiteboardXmlNames.tagPointer));
	}

	private void setCountMetadataValue(IMetadataValueContainer valueContainer, AtomicInteger value)
	{
		int count = 0;
		if (value != null)
			count = value.get();
		valueContainer.setValue(Long.valueOf(count));
	}

//	public void setTypeKey(K typeKey)
//	{
//		this.formatKey = typeKey;
//	}
//
	public void setJdomStringConversion(JDomStringConversion jdomStringConversion)
	{
		this.jdomStringConversion = jdomStringConversion;
	}

	public void setFormatKey(K formatKey)
	{
		this.formatKey = formatKey;
	}

	public void setLineCountKey(K lineCountKey)
	{
		this.lineCountKey = lineCountKey;
	}

	public void setRectangleCountKey(K rectangleCountKey)
	{
		this.rectangleCountKey = rectangleCountKey;
	}

	public void setOvalCountKey(K ovalCountKey)
	{
		this.ovalCountKey = ovalCountKey;
	}

	public void setFreehandCountKey(K freehandCountKey)
	{
		this.freehandCountKey = freehandCountKey;
	}

	public void setTextCountKey(K textCountKey)
	{
		this.textCountKey = textCountKey;
	}

	public void setImageCountKey(K imageCountKey)
	{
		this.imageCountKey = imageCountKey;
	}

	public void setTagPointerCountKey(K tagPointerCountKey)
	{
		this.tagPointerCountKey = tagPointerCountKey;
	}
}
