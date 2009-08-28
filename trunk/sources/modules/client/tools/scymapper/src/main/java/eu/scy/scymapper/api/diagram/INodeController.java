package eu.scy.scymapper.api.diagram;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:34:13
 */
public interface INodeController {
    public void setSize(Dimension size);

    public void setLocation(Point location);

    public void setLabel(String text);

    public void setSelected(boolean b);
}
