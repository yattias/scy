package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleListener;

import javax.swing.*;
import javax.swing.border.Border;
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

	private Color backgroundColor = new Color(255, 255, 255);

	private Stroke stroke = new BasicStroke(2f);

	private boolean opaque = true;

	private int minWidth = 60;

	private int minHeight = 46;

	private boolean shadow = true;

	private Border border = BorderFactory.createEmptyBorder();

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
	public void setOpaque(boolean b) {
		opaque = b;
		notifyStyleChanged(this);
	}

	@Override
	public boolean isOpaque() {
		return opaque;
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
	public void notifyStyleChanged(INodeStyle s) {
		for (INodeStyleListener listener : listeners) {
			listener.styleChanged(s);
		}
	}

	@Override
	public int getMinHeight() {
		return minHeight;
	}

	@Override
	public void setMinHeight(int h) {
		minHeight = h;
	}

	@Override
	public int getMinWidth() {
		return minWidth;
	}

	@Override
	public void setMinWidth(int w) {
		minWidth = w;
	}

	@Override
	public void setBorder(Border border) {
		this.border = border;
		notifyStyleChanged(this);
	}

	public Border getBorder() {
		return border;
	}

	@Override
	public void setPaintShadow(boolean b) {
		shadow = b;
		notifyStyleChanged(this);
	}

	@Override
	public boolean getPaintShadow() {
		return shadow;
	}
}
