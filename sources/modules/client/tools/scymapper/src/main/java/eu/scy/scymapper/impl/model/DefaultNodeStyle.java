package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleObserver;

import java.awt.*;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 15:13:45
 */
public class DefaultNodeStyle implements INodeStyle {
    private Color foregroundColor = new Color(0xffffff);
    private Color backgroundColor = new Color(0xaaaaaa);

    private Stroke stroke = new BasicStroke(2f);
    private int fillStyle = FILLSTYLE_STROKED;

    private Collection<INodeStyleObserver> observers;

    public DefaultNodeStyle() {
        observers = new ArrayList<INodeStyleObserver>();
    }

    @Override
    public Color getForeground() {
        return foregroundColor;
    }

    @Override
    public void setForeground(Color c) {
        foregroundColor = c;
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }

    @Override
    public void setBackground(Color c) {
        backgroundColor = c;
    }

    @Override
    public void setStroke(Stroke s) {
        stroke = s;
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setFillStyle(int s) {
        fillStyle = s;
    }

    @Override
    public int getFillStyle() {
        return fillStyle;
    }

    @Override
    public void addObserver(INodeStyleObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(INodeStyleObserver o) {
        observers.remove(o);
    }
    @Override
    public boolean hasObserver(INodeStyleObserver o) {
        return observers.contains(o);
    }

    @Override
    public void notifyStyleChanged(INodeStyle s) {
        for (INodeStyleObserver observer : observers) {
            observer.styleChanged(s);
        }
    }
}
