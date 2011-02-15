package eu.scy.common.mission.impl.jdom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import eu.scy.common.mission.ColorScheme;
import eu.scy.common.mission.ColorSchemeId;
import eu.scy.common.mission.ColorSchemesEloContent;
import eu.scy.common.mission.impl.BasicColorSchemesEloContent;

public class ColorSchemesEloContentXmlUtils
{
	static final String colorSchemesName = "colorSchemes";
	static final String colorSchemeName = "colorScheme";
	static final String colorSchemeIdName = "colorSchemeId";
	static final String mainColorName = "mainColor";
	static final String mainColorLightName = "mainColorLight";
	static final String secondColorName = "secondColor";
	static final String secondColorLightName = "secondColorLight";
	static final String thirdColorName = "thirdColor";
	static final String thirdColorLightName = "thirdColorLight";
	static final String backgroundColorName = "backgroundColor";
	static final String emptyBackgroundColorName = "emptyBackgroundColor";
	
	static final String redName = "red";
	static final String greenName = "green";
	static final String blueName = "blue";
	static final String alphaName = "alpha";
	static final String templateElosEloUriName = "templateElosEloUri";

	private ColorSchemesEloContentXmlUtils()
	{
	}

	public static String colorSchemesEloContentToXml(ColorSchemesEloContent colorSchemesEloContent)
	{
		Element root = new Element(colorSchemesName);
		List<ColorScheme> colorSchemes = colorSchemesEloContent.getColorSchemes();
		if (colorSchemes != null)
		{
			for (ColorScheme colorScheme : colorSchemes)
			{
				root.addContent(createColorSchemeElement(colorScheme));
			}
		}
		return new JDomStringConversion().xmlToString(root);
	}

	private static Element createColorSchemeElement(ColorScheme colorScheme)
	{
		Element colorSchemeRoot = new Element(colorSchemeName);
		colorSchemeRoot.addContent(JDomConversionUtils.createElement(colorSchemeIdName, colorScheme.getColorSchemeId()));
		colorSchemeRoot.addContent(createColorElement(mainColorName,colorScheme.getMainColor()));
		colorSchemeRoot.addContent(createColorElement(mainColorLightName,colorScheme.getMainColorLight()));
		colorSchemeRoot.addContent(createColorElement(secondColorName,colorScheme.getSecondColor()));
		colorSchemeRoot.addContent(createColorElement(secondColorLightName,colorScheme.getSecondColorLight()));
		colorSchemeRoot.addContent(createColorElement(thirdColorName,colorScheme.getThirdColor()));
		colorSchemeRoot.addContent(createColorElement(thirdColorLightName,colorScheme.getThirdColorLight()));
		colorSchemeRoot.addContent(createColorElement(backgroundColorName,colorScheme.getBackgroundColor()));
		colorSchemeRoot.addContent(createColorElement(emptyBackgroundColorName,colorScheme.getEmptyBackgroundColor()));
		return colorSchemeRoot;
	}

	private static Element createColorElement(String tagName,Color color)
	{
		Element colorRoot = new Element(tagName);
		colorRoot.addContent(JDomConversionUtils.createElement(redName, color.getRed()));
		colorRoot.addContent(JDomConversionUtils.createElement(greenName, color.getGreen()));
		colorRoot.addContent(JDomConversionUtils.createElement(blueName, color.getBlue()));
		colorRoot.addContent(JDomConversionUtils.createElement(alphaName, color.getAlpha()));
		return colorRoot;
	}

	public static ColorSchemesEloContent colorSchemesEloContentFromXml(String xml)
	{
		Element root = new JDomStringConversion().stringToXml(xml);
		if (root == null || !colorSchemesName.equals(root.getName()))
		{
			return null;
		}
		BasicColorSchemesEloContent colorSchemesEloContent = new BasicColorSchemesEloContent();
		List<ColorScheme> colorSchemes = new ArrayList<ColorScheme>();
      @SuppressWarnings("unchecked")
      List<Element> colorSchemeChildren = root.getChildren(colorSchemeName);
      if (colorSchemeChildren != null)
      {
         for (Element colorSchemeChild : colorSchemeChildren)
         {
         	colorSchemes.add(getColorScheme(colorSchemeChild));
         }
      }
		colorSchemesEloContent.setColorSchemes(colorSchemes);
		return colorSchemesEloContent;
	}

	private static ColorScheme getColorScheme(Element colorSchemeChild)
	{
		ColorScheme colorScheme = new ColorScheme();
		colorScheme.setColorSchemeId(JDomConversionUtils.getEnumValue(ColorSchemeId.class, colorSchemeChild, colorSchemeIdName));
		colorScheme.setMainColor(getColorValue(colorSchemeChild,mainColorName));
		colorScheme.setMainColorLight(getColorValue(colorSchemeChild,mainColorLightName));
		colorScheme.setSecondColor(getColorValue(colorSchemeChild,secondColorName));
		colorScheme.setSecondColorLight(getColorValue(colorSchemeChild,secondColorLightName));
		colorScheme.setThirdColor(getColorValue(colorSchemeChild,thirdColorName));
		colorScheme.setThirdColorLight(getColorValue(colorSchemeChild,thirdColorLightName));
		colorScheme.setBackgroundColor(getColorValue(colorSchemeChild,backgroundColorName));
		colorScheme.setEmptyBackgroundColor(getColorValue(colorSchemeChild,emptyBackgroundColorName));
		return colorScheme;
	}

	private static Color getColorValue(Element colorSchemeChild, String colorName)
	{
      Element colorChild = colorSchemeChild.getChild(colorName);
		int redValue = JDomConversionUtils.getIntValue(colorChild, redName);
		int greenValue = JDomConversionUtils.getIntValue(colorChild, greenName);
		int blueValue = JDomConversionUtils.getIntValue(colorChild, blueName);
		int alphaValue = JDomConversionUtils.getIntValue(colorChild, alphaName);
		return new Color(redValue,greenValue,blueValue,alphaValue);
	}

}
