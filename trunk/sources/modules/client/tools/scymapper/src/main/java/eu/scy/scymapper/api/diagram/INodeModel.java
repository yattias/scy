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
public interface INodeModel extends INodeModelObservable {
    public String getLabel();

    public void setLabel(String label);

    public Point getLocation();

    public void setLocation(Point location);

    public Dimension getSize();

    public void setSize(Dimension size);

    public Point getCenterLocation();

    public int getDistanceToPerimeter(Point p);

    public Point getConnectionPoint(Point p);

    public INodeShape getShape();

    public void setShape(INodeShape shape);

    public void setStyle(INodeStyle style);

    public INodeStyle getStyle();

    public void setSelected(boolean b);
}
