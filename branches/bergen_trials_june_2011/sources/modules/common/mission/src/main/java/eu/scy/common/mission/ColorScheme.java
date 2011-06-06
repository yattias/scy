package eu.scy.common.mission;

import java.awt.Color;

import eu.scy.common.scyelo.ColorSchemeId;

public class ColorScheme implements Comparable<ColorScheme>
{
	private ColorSchemeId colorSchemeId;
	private Color mainColor;
	private Color mainColorLight;
	private Color secondColor;
	private Color secondColorLight;
	private Color thirdColor;
	private Color thirdColorLight;
	private Color backgroundColor;
	private Color emptyBackgroundColor;

	public ColorSchemeId getColorSchemeId()
	{
		return colorSchemeId;
	}

	public void setColorSchemeId(ColorSchemeId colorSchemeId)
	{
		this.colorSchemeId = colorSchemeId;
	}

	public Color getMainColor()
	{
		return mainColor;
	}

	public void setMainColor(Color mainColor)
	{
		this.mainColor = mainColor;
	}

	public Color getMainColorLight()
	{
		return mainColorLight;
	}

	public void setMainColorLight(Color mainColorLight)
	{
		this.mainColorLight = mainColorLight;
	}

	public Color getSecondColor()
	{
		return secondColor;
	}

	public void setSecondColor(Color secondColor)
	{
		this.secondColor = secondColor;
	}

	public Color getSecondColorLight()
	{
		return secondColorLight;
	}

	public void setSecondColorLight(Color secondColorLight)
	{
		this.secondColorLight = secondColorLight;
	}

	public Color getThirdColor()
	{
		return thirdColor;
	}

	public void setThirdColor(Color thirdColor)
	{
		this.thirdColor = thirdColor;
	}

	public Color getThirdColorLight()
	{
		return thirdColorLight;
	}

	public void setThirdColorLight(Color thirdColorLight)
	{
		this.thirdColorLight = thirdColorLight;
	}

	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	public Color getEmptyBackgroundColor()
	{
		return emptyBackgroundColor;
	}

	public void setEmptyBackgroundColor(Color emptyBackgroundColor)
	{
		this.emptyBackgroundColor = emptyBackgroundColor;
	}

	@Override
	public int compareTo(ColorScheme colorScheme)
	{
		if (colorSchemeId == null)
		{
			if (colorScheme.colorSchemeId == null)
			{
				return 0;
			}
			else
				return -1;
		}
		return colorSchemeId.compareTo(colorScheme.colorSchemeId);
	}

}
