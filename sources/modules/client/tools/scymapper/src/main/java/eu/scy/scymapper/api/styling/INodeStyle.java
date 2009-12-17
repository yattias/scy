package eu.scy.scymapper.api.styling;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * @author Bjorge Naess
 * Date: 24.jun.2009
 * Time: 15:04:05
 * Used to guide the painting of nodes
 */
public interface INodeStyle {

    /**
     * Returns the foreground color
     * @return the foreground color
     */
    Color getForeground();

    /**
     * Sets the foreground color
     * @param c the foreground color
     */
    void setForeground(Color c);

    /**
     * Returns the background color
     * @return the background color
     */
    Color getBackground();

    /**
     * Sets the background color
     * @param c the background color
     */
    void setBackground(Color c);

    /**
     * Sets the stroke
     * @param s the stroke
     * @see java.awt.Stroke
     */
    void setStroke(Stroke s);

    /**
     * Returns the stroke
     * @return the stroke
     */
    Stroke getStroke();

    /**
     * Sets whether or not to paint the concept node as opaque
     * @param b whether or not to paint it as opaque
     */
    void setOpaque(boolean b);

    /**
     * Returns whether or not the the concept node should be considered opaque
     * @return whether or not the the concept node should be considered opaque
     */
    boolean isOpaque();

    /**
     * Adds a style listener
     * @param l the style listener
     */
    void addStyleListener(INodeStyleListener l);

    /**
     * Removes a style listener
     * @param l the style listener
     */
    void removeStyleListener(INodeStyleListener l);

    /**
     * Defined here to ensure its implementation
     * Notifies listeners about style changes
     * @param s the node style
     */
    void notifyStyleChanged(INodeStyle s);

    /**
     * Returns the selection color
     * @return the selection color
     */
    Color getSelectionColor();

    /**
     * Sets the selection color
     * @param c the selection color
     */
    void setSelectionColor(Color c);

	int getMinHeight();

	void setMinHeight(int h);

	int getMinWidth();

	void setMinWidth(int w);
}
