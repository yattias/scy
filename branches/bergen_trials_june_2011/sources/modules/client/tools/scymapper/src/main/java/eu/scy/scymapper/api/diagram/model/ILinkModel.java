package eu.scy.scymapper.api.diagram.model;

import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.styling.ILinkStyle;

import java.awt.*;
import java.io.Serializable;

/**
 * @author bjoerge
 * @created 22.jun.2009
 * Time: 18:29:15
 */
public interface ILinkModel extends IDiagramElement, Serializable {
	String getLabel();

	void setLabel(String label);

	boolean isLabelHidden();

	void setLabelHidden(boolean labelHidden);

	Point getFrom();

	void setFrom(Point p);

	Point getTo();

	void setTo(Point p);

	ILinkShape getShape();

	void setShape(ILinkShape shape);

	void setStyle(ILinkStyle style);

	ILinkStyle getStyle();

	void addListener(ILinkModelListener listener);

	void removeListener(ILinkModelListener listener);

	void notifyUpdated();

	void notifyLabelChanged();

	boolean isSelected();

	void setSelected(boolean b);

    void notifyEdgeFlipped();

}
