package eu.scy.common.mission.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.scy.common.mission.ColorScheme;
import eu.scy.common.mission.ColorSchemeId;
import eu.scy.common.mission.ColorSchemesEloContent;

public class BasicColorSchemesEloContent implements ColorSchemesEloContent
{
	private Map<ColorSchemeId,ColorScheme> colorSchemesMap = new HashMap<ColorSchemeId,ColorScheme>();
	
	@Override
	public ColorScheme getColorScheme(ColorSchemeId id)
	{
		return colorSchemesMap.get(id);
	}

	@Override
	public List<ColorScheme> getColorSchemes()
	{
		List<ColorScheme> colorSchemes = new ArrayList<ColorScheme>(colorSchemesMap.values());
		Collections.sort(colorSchemes);
		return colorSchemes;
	}
	
	public void setColorSchemes(List<ColorScheme> colorSchemes){
		assert colorSchemes!=null;
		colorSchemesMap.clear();
		for (ColorScheme colorScheme: colorSchemes){
			assert colorScheme.getColorSchemeId()!=null;
			assert colorSchemesMap.containsKey(colorScheme.getColorSchemeId())==false;
			colorSchemesMap.put(colorScheme.getColorSchemeId(), colorScheme);
		}
	}

}
