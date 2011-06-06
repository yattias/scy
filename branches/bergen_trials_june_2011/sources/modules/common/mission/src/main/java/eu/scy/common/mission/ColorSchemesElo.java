package eu.scy.common.mission;

import java.net.URI;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicColorSchemesEloContent;
import eu.scy.common.mission.impl.jdom.ColorSchemesEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;

public class ColorSchemesElo extends ContentTypedScyElo<ColorSchemesEloContent>
{

	private static class ColorSchemesEloContentCreator implements
				ScyEloContentCreator<ColorSchemesEloContent>
	{

		@Override
		public ColorSchemesEloContent createScyEloContent(ScyElo scyElo)
		{
			String xml = scyElo.getElo().getContent().getXmlString();
			if (xml == null || xml.length() == 0)
			{
				return new BasicColorSchemesEloContent();
			}
			return ColorSchemesEloContentXmlUtils.colorSchemesEloContentFromXml(xml);
		}

		@Override
		public void updateEloContent(ContentTypedScyElo<ColorSchemesEloContent> scyElo)
		{
			scyElo.getElo()
						.getContent()
						.setXmlString(
									ColorSchemesEloContentXmlUtils.colorSchemesEloContentToXml(scyElo
												.getTypedContent()));
		}
	}

	private static final ColorSchemesEloContentCreator colorSchemesEloContentCreator = new ColorSchemesEloContentCreator();

	public ColorSchemesElo(IELO elo, RooloServices rooloServices)
	{
		super(elo, rooloServices, colorSchemesEloContentCreator, MissionEloType.COLOR_SCHEMES
					.getType());
	}

	public static ColorSchemesElo loadElo(URI uri, RooloServices rooloServices)
	{
		IELO elo = rooloServices.getRepository().retrieveELO(uri);
		if (elo == null)
		{
			return null;
		}
		return new ColorSchemesElo(elo, rooloServices);
	}

	public static ColorSchemesElo loadLastVersionElo(URI uri, RooloServices rooloServices)
	{
		IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
		if (elo == null)
		{
			return null;
		}
		return new ColorSchemesElo(elo, rooloServices);
	}

	public static ColorSchemesElo createElo(RooloServices rooloServices)
	{
		IELO elo = rooloServices.getELOFactory().createELO();
		elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(rooloServices))
					.setValue(MissionEloType.COLOR_SCHEMES.getType());
		ColorSchemesElo scyElo = new ColorSchemesElo(elo, rooloServices);
		return scyElo;
	}
}
