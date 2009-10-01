package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleListener;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 15:13:45
 */
public class DefaultNodeStyle implements INodeStyle {

	private Color foregroundColor = new Color(0x000000);

	private Color backgroundColor = new Color(0xaaaaaa);

    private Stroke stroke = new BasicStroke(2f);

	private int fillStyle = FILLSTYLE_STROKED;

    private transient java.util.List<INodeStyleListener> listeners = new ArrayList<INodeStyleListener>();

	private Object readResolve() {
		listeners = new ArrayList<INodeStyleListener>();
		return this;
	}
	public DefaultNodeStyle() {

    }

    @Override
    public Color getForeground() {
        return foregroundColor;
    }

    @Override
    public void setForeground(Color c) {
        foregroundColor = c;
		notifyStyleChanged(this);
    }

    @Override
    public Color getBackground() {
        return backgroundColor;
    }

    @Override
    public void setBackground(Color c) {
        backgroundColor = c;
		notifyStyleChanged(this);
    }

    @Override
    public void setStroke(Stroke s) {
        stroke = s;
		notifyStyleChanged(this);
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setFillStyle(int s) {
        fillStyle = s;
		notifyStyleChanged(this);
    }

    @Override
    public int getFillStyle() {
        return fillStyle;
    }

    @Override
    public void addStyleListener(INodeStyleListener o) {
        listeners.add(o);
    }

    @Override
    public void removeStyleListener(INodeStyleListener o) {
        listeners.remove(o);
    }
    @Override
    public boolean hasObserver(INodeStyleListener o) {
        return listeners.contains(o);
    }

    @Override
    public void notifyStyleChanged(INodeStyle s) {
        for (INodeStyleListener listener : listeners) {
            listener.styleChanged(s);
        }
    }
}
