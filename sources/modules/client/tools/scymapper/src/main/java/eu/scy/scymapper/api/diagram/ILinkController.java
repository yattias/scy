package eu.scy.scymapper.api.diagram;

import java.awt.*;

/**
 * @author bjoerge
 * @created 22.jun.2009
 * Time: 20:59:29
 * To change this template use File | Settings | File Templates.
 */
public interface ILinkController {
    public void setLabel(String text);
    public void setTo(Point p);
    public void setFrom(Point p);
}
