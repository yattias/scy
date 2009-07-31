package eu.scy.colemo.client.ui.impl.model;

import eu.scy.colemo.client.ui.api.styling.ILinkStyle;
import eu.scy.colemo.client.ui.api.styling.ILinkStyleObserver;

import java.awt.*;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 17:16:31
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLinkStyle implements ILinkStyle {
    private Color color = new Color(0x000000);
    private Stroke stroke = new BasicStroke(2f);
    private Collection<ILinkStyleObserver> observers;

    public DefaultLinkStyle() {
        observers = new ArrayList<ILinkStyleObserver>();
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color c) {
        color = c;
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
    public void addObserver(ILinkStyleObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ILinkStyleObserver o) {
        observers.remove(o);
    }
    @Override
    public boolean hasObserver(ILinkStyleObserver o) {
        return observers.contains(o);
    }

    @Override
    public void notifyStyleChanged(ILinkStyle s) {
        for (ILinkStyleObserver observer : observers) {
            observer.styleChanged(s);
        }
    }
}
