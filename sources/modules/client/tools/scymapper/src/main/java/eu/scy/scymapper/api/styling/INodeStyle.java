package eu.scy.scymapper.api.styling;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 15:04:05
 * To change this template use File | Settings | File Templates.
 */
public interface INodeStyle {

    Color getForeground();

    void setForeground(Color c);

    Color getBackground();

    void setBackground(Color c);

    void setStroke(Stroke s);

    Stroke getStroke();

    void setOpaque(boolean b);

    boolean isOpaque();

    void addStyleListener(INodeStyleListener l);
    void removeStyleListener(INodeStyleListener l);
    boolean hasObserver(INodeStyleListener l);
    void notifyStyleChanged(INodeStyle s);

    Color getSelectionColor();
    void setSelectionColor(Color c);
}
