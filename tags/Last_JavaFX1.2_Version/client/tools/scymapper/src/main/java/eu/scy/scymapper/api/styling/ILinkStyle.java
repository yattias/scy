package eu.scy.scymapper.api.styling;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 17:10:41
 * This class dictates the style of a link
 * It currently supports color, selection color and stroke
 */
public interface ILinkStyle {
	/**
	 * Returns the color of this link
	 *
	 * @return the color
	 */
	Color getBackground();

	/**
	 * Set / change color of this link
	 *
	 * @param c the color
	 */
	void setBackground(Color c);

	Color getForeground();

	void setForeground(Color foregroundColor);

	/**
	 * Set stroke
	 *
	 * @param s stroke object
	 * @see java.awt.Stroke
	 */
	void setStroke(Stroke s);

	/**
	 * Returns the current stroke object
	 *
	 * @return the stroke
	 */
	Stroke getStroke();

	/**
	 * Allow clients to add a listener for changes in style
	 *
	 * @param l the style listener
	 */
	void addStyleListener(ILinkStyleListener l);

	/**
	 * @param l the style listener
	 */
	void removeStyleListener(ILinkStyleListener l);

	/**
	 * Notify listeners about change in style
	 */
	void notifyStyleChanged();
}
