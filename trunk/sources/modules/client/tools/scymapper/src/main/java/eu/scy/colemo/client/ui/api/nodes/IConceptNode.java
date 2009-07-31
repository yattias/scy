package eu.scy.colemo.client.ui.api.nodes;

import eu.scy.colemo.client.shapes.ConceptShape;
import eu.scy.colemo.client.ui.api.styling.INodeStyle;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 19:00:33
 */
public interface IConceptNode extends INodeObservable {
    public String getLabel();

    public void setLabel(String label);

    public Point getLocation();

    public void setLocation(Point location);

    public Dimension getSize();

    public void setSize(Dimension size);

    public Point getCenterLocation();

    public int getDistanceToPerimeter(Point p);

    public Point getConnectionPoint(Point p);

    public ConceptShape getShape();

    public void setShape(ConceptShape shape);

    public void setStyle(INodeStyle style);

    public INodeStyle getStyle();

    public void setSelected(boolean b);
}
