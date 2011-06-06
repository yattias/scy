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
 */
public class DefaultLinkStyle implements ILinkStyle {
	private Color foregroundColor = new Color(0x333333);
	private Stroke stroke = new BasicStroke(1f);
	private transient Collection<ILinkStyleListener> listeners;
	private Color backgroundColor = Color.black;

	private Object readResolve() {
		listeners = new ArrayList<ILinkStyleListener>();
		return this;
	}

	public DefaultLinkStyle() {
		listeners = new ArrayList<ILinkStyleListener>();
	}

	@Override
	public Color getForeground() {
		return foregroundColor;
	}

	@Override
	public void setForeground(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		notifyStyleChanged();
	}

	@Override
	public void setBackground(Color c) {
		backgroundColor = c;
		notifyStyleChanged();
	}

	@Override
	public Color getBackground() {
		return backgroundColor;
	}

	@Override
	public void setStroke(Stroke s) {
		stroke = s;
		notifyStyleChanged();
	}

	@Override
	public Stroke getStroke() {
		return stroke;
	}

	@Override
	public void addStyleListener(ILinkStyleListener l) {
		listeners.add(l);
	}

	@Override
	public void removeStyleListener(ILinkStyleListener l) {
		listeners.remove(l);
	}

	@Override
	public void notifyStyleChanged() {
		for (ILinkStyleListener listener : listeners) {
			listener.styleChanged(this);
		}
	}
}
