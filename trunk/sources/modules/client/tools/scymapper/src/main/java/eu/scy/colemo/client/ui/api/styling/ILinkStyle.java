package eu.scy.colemo.client.ui.api.styling;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 17:10:41
 */
public interface ILinkStyle  extends ILinkStyleObservable {
    public Color getColor();

    public void setColor(Color c);

    public void setStroke(Stroke s);

    public Stroke getStroke();

}
