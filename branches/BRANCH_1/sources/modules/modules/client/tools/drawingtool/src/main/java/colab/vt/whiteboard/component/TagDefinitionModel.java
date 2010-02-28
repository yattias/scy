package colab.vt.whiteboard.component;

import java.util.List;

public class TagDefinitionModel
{
	private List<TagLevel> tagLevels;
	
	public TagDefinitionModel(List<TagLevel> tagLevels)
	{
		super();
		this.tagLevels = tagLevels;
	}

	public List<TagLevel> getTagLevels()
	{
		return tagLevels;
	}
}
