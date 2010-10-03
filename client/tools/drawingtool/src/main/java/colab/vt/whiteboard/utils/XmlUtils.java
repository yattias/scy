package colab.vt.whiteboard.utils;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;

import colab.vt.whiteboard.component.XmlNames;

public class XmlUtils
{
	private XmlUtils()
	{
	}

	public static void addXmlTag(Element element, String tagName, double value)
	{
		addXmlTag(element, tagName, "" + value);
	}

	public static void addXmlTag(Element element, String tagName, int value)
	{
		addXmlTag(element, tagName, "" + value);
	}
	
	public static void addXmlTag(Element element, String tagName, long value) {
		addXmlTag(element, tagName, "" + value);
	}

	public static void addXmlTag(Element element, String tagName, boolean value)
	{
		addXmlTag(element, tagName, "" + value);
	}

	public static void addXmlTag(Element element, String tagName, String value)
	{
		Element tag = new Element(tagName);
		tag.setText(value);
		element.addContent(tag);
	}

	public static void addXmlTag(Element element, String tagName, Color color)
	{
		Element tag = new Element(tagName);
		addXmlTag(tag, XmlNames.red, color.getRed());
		addXmlTag(tag, XmlNames.green, color.getGreen());
		addXmlTag(tag, XmlNames.blue, color.getBlue());
		addXmlTag(tag, XmlNames.alpha, color.getAlpha());
		element.addContent(tag);
	}

	public static void addXmlTag(Element element, String tagName, Point point)
	{
		Element tag = new Element(tagName);
		addXmlTag(tag, XmlNames.x, point.x);
		addXmlTag(tag, XmlNames.y, point.y);
		element.addContent(tag);
	}

	public static void addXmlTag(Element element, String tagName, byte[] bytes)
	{
		addXmlTag(element,tagName,new String(Base64.encodeBase64(bytes)));
	}

	public static double getDoubleValueFromXmlTag(Element element, String tagName)
	{
		String value = element.getChildText(tagName);
		return Double.parseDouble(value);
	}

	public static int getIntValueFromXmlTag(Element element, String tagName)
	{
		String value = element.getChildText(tagName);
		return Integer.parseInt(value);
	}
	
	public static long getLongValueFromXmlTag(Element element, String tagName)
	{
		String value = element.getChildText(tagName);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException ex) {
			// if something goes wrong, return 0
			return 0;
		}
	}

	public static boolean getBooleanValueFromXmlTag(Element element, String tagName)
	{
		String value = element.getChildText(tagName);
		return Boolean.parseBoolean(value);
	}

	public static Color getColorValueFromXmlTag(Element element, String tagName)
	{
		Element colorTag = element.getChild(tagName);
		return getColorValueFromXmlTag(colorTag);
	}

	public static Color getColorValueFromXmlTag(Element element)
	{
		int red = getIntValueFromXmlTag(element, XmlNames.red);
		int green = getIntValueFromXmlTag(element, XmlNames.green);
		int blue = getIntValueFromXmlTag(element, XmlNames.blue);
		int alpha = getIntValueFromXmlTag(element, XmlNames.alpha);
		return new Color(red, green, blue, alpha);
	}

	public static Point getPointValueFromXmlTag(Element element, String tagName)
	{
		Element pointTag = element.getChild(tagName);
		return getPointValueFromXmlTag(pointTag);
	}

	public static Point getPointValueFromXmlTag(Element element)
	{
		int x = getIntValueFromXmlTag(element, XmlNames.x);
		int y = getIntValueFromXmlTag(element, XmlNames.y);
		return new Point(x,y);
	}

	public static byte[] getBytesValueFromXmlTag(Element element, String tagName)
	{
		Element bytes = element.getChild(tagName);
		return getBytesValueFromXmlTag(bytes);
	}

	public static byte[] getBytesValueFromXmlTag(Element element)
	{
		byte[] bytes = Base64.decodeBase64(element.getText().getBytes());
		return bytes;
	}
	
	public static Element getFirstChild(Element element)
	{
		Element firstChild = null;
		@SuppressWarnings("unchecked")
		List<Element> children = element.getChildren();
		if (children != null && children.size() > 0)
			firstChild = children.get(0);
		return firstChild;
	}
}
