package eu.scy.scymapper.api.diagram.controller;

import eu.scy.scymapper.api.styling.INodeStyle;

import java.awt.*;

/**
 * @author bjoerge
 * @created 22.jun.2009
 * Time: 18:34:13
 */
public interface INodeController {
	void setSize(Dimension size);

	void setLocation(Point location);

	void setLabel(String text);

	void setSelected(boolean b);

	void setDeleted(boolean b);

	void setStyle(INodeStyle style);
}
