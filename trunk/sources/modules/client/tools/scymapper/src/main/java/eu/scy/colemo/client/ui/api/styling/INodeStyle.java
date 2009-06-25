package eu.scy.colemo.client.ui.api.styling;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 24.jun.2009
 * Time: 15:04:05
 * To change this template use File | Settings | File Templates.
 */
public interface INodeStyle extends INodeStyleObservable {
    public static final int FILLSTYLE_STROKED = 0;
    public static final int FILLSTYLE_FILLED = 1;

    public Color getForeground();

    public void setForeground(Color c);

    public Color getBackground();

    public void setBackground(Color c);

    public void setStroke(Stroke s);

    public Stroke getStroke();

    public void setFillStyle(int s);

    public int getFillStyle();
}
