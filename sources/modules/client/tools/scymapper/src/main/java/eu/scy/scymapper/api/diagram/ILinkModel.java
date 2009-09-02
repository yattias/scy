package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.styling.ILinkStyle;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:29:15
 * To change this template use File | Settings | File Templates.
 */
public interface ILinkModel extends ILinkModelObservable {
    public String getLabel();
    public void setLabel(String label);

    public Point getFrom();
    public void setFrom(Point p);

    public Point getTo();
    public void setTo(Point p);

    public ILinkShape getShape();

    public void setShape(ILinkShape shape);

    public void setStyle(ILinkStyle style);

    public ILinkStyle getStyle();
}
