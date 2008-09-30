package eu.scy.agents.roolo;

import java.util.List;

import org.jdom.Element;

import roolo.api.IELO;
import roolo.api.IMetadata;
import roolo.api.IMetadataKey;
import roolo.cms.elo.JDomStringConversion;

public class DrawAnalyserAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractRooloAgent<T,K>
{
	private static final String scyDrawType = "scy/drawing";
	private static final String contentTagName = "content";
	private K typeKey;
	private JDomStringConversion jdomStringConversion = new JDomStringConversion();
	
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
		return scyDrawType.equals(elo.getMetadata().getMetadataValueContainer(typeKey).getValue());
	}

	private void analyseElo(T elo)
	{
		String contentXml = elo.getContent().getXml();
		Element drawRoot = jdomStringConversion.stringToXml(contentXml);
		scanElements(drawRoot);
	}
	
	private void scanElements(Element element)
	{
		if (contentTagName.equals(element.getName()))
		{
			
		}
		else
		{
			@SuppressWarnings("unchecked")
			List<Element> children = element.getChildren();
			for (Element child : children)
			{
				scanElements(child);
			}
		}
	}

	public void setTypeKey(K typeKey)
	{
		this.typeKey = typeKey;
	}
}
