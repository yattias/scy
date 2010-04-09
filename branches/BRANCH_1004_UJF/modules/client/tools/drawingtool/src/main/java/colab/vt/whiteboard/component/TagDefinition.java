/**
 * 
 */
package colab.vt.whiteboard.component;

import java.awt.Color;

public class TagDefinition
{
	private final String tag;
	private final Color color;
	private final String tooltip;
	private final Color nonTransparentColor;

	public TagDefinition(String tag, Color color, String tooltip)
	{
		super();
		this.color = color;
		this.tag = tag;
		this.tooltip = tooltip;
		nonTransparentColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public String toString()
	{
		return "tag=" + tag + ",color=" + color + ",tooltip=" + tooltip;
	}

	public String getTag()
	{
		return tag;
	}

	public Color getColor()
	{
		return color;
	}

	public String getTooltip()
	{
		return tooltip;
	}

	public Color getNonTransparentColor()
	{
		return nonTransparentColor;
	}


}