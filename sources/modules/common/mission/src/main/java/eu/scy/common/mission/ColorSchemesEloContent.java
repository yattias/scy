package eu.scy.common.mission;

import java.util.List;

public interface ColorSchemesEloContent
{
	public ColorScheme getColorScheme(ColorSchemeId id);
	
	public List<ColorScheme> getColorSchemes();
	
	public void setColorSchemes(List<ColorScheme> colorSchemes);
}
