package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.ILinkStyleListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 17:16:31
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLinkStyle implements ILinkStyle {
    private Color color = new Color(0x333333);
    private Stroke stroke = new BasicStroke(1f);
    private transient Collection<ILinkStyleListener> listeners;
    private Color selectionColor = new Color(0x2244ff);

    public DefaultLinkStyle() {
        listeners = new ArrayList<ILinkStyleListener>();
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
    public void addStyleListener(ILinkStyleListener o) {
        listeners.add(o);
    }

    @Override
    public void removeStyleListener(ILinkStyleListener o) {
        listeners.remove(o);
    }
    @Override
    public boolean hasStyleListener(ILinkStyleListener o) {
        return listeners.contains(o);
    }

    @Override
    public void notifyStyleChanged(ILinkStyle s) {
        for (ILinkStyleListener listener : listeners) {
            listener.styleChanged(s);
        }
    }

    @Override
    public Color getSelectionColor() {
        return selectionColor;
    }

    @Override
    public void setSelectionColor(Color c) {
        selectionColor = c;
    }
}
