/**
 * 
 */
package colab.vt.whiteboard.component;

import java.awt.Color;
import java.util.List;

public class TagLevel
{
	private final String name;
	private final Color color;
	private final String tooltip;
	private final List<TagLevel> tagLevels;
	private final TagDefinition tagDefinition;

	public TagLevel(String name,Color color,String tooltip, List<TagLevel> tagLevels)
	{
		this.name = name;
		this.color = color;
		this.tooltip = tooltip;
		this.tagLevels = tagLevels;
		tagDefinition = null;
	}

	public TagLevel(String name, TagDefinition tagDefinition)
	{
		this.name = name;
		this.color = tagDefinition.getNonTransparentColor();
		this.tooltip = tagDefinition.getTooltip();
		this.tagLevels = null;
		this.tagDefinition = tagDefinition;
	}

	public String getName()
	{
		return name;
	}

	public Color getColor()
	{
		return color;
	}

	public String getTooltip()
	{
		return tooltip;
	}

	public TagDefinition getTagDefinition()
	{
		return tagDefinition;
	}

	public List<TagLevel> getTagLevels()
	{
		return tagLevels;
	}

	@Override
	public String toString()
	{
		return "name=" + name + ",tagLevels=" + tagLevels + ",tagDefinition=" + tagDefinition;
	}
}