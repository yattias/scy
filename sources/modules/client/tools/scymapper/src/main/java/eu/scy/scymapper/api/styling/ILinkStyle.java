package eu.scy.scymapper.api.styling;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 17:10:41
 */
public interface ILinkStyle {
    Color getColor();

    void setColor(Color c);

    void setStroke(Stroke s);

    Stroke getStroke();

    void addStyleListener(ILinkStyleListener o);
    void removeStyleListener(ILinkStyleListener o);
    boolean hasStyleListener(ILinkStyleListener o);
    void notifyStyleChanged(ILinkStyle s);

}
