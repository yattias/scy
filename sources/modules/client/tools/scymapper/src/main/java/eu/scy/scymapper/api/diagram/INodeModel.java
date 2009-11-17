package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 19:00:33
 */
public interface INodeModel {
	String getLabel();

	void setLabel(String label);

	Point getLocation();

	void setLocation(Point location);

    void setLocation(int x, int y);

    void setX(int x);

    void setY(int y);

	Dimension getSize();

    void setSize(int height, int width);

	void setSize(Dimension size);

	Point getCenterLocation();

	int getDistanceToPerimeter(Point p);

	Point getConnectionPoint(Point p);

	INodeShape getShape();

	void setShape(INodeShape shape);

	void setStyle(INodeStyle style);

	void setSelected(boolean b);

	boolean isSelected();

    void setDeleted(boolean b);

    boolean isDeleted();

	INodeStyle getStyle();

	boolean isLabelHidden();

	void setLabelHidden(boolean labelHidden);

	void addListener(INodeModelListener listener);

	void removeListener(INodeModelListener listener);

	void notifyMoved();

	void notifyResized();

	void notifyLabelChanged();

	void notifyShapeChanged();

	void notifySelected();

	void notifyDeleted();

    void setConstraints(INodeModelConstraints constraints);
    INodeModelConstraints getConstraints();

    void setHeight(int height);

    int getHeight();

    void setWidth(int width);

    int getWidth();
}
