package eu.scy.scymapper.api.styling;

/**
 * @author Bjorge Naess
 * Date: 24.jun.2009
 * Time: 17:12:34
 * This allows clients to listen for changes in ILinkStyle objects
 */
public interface ILinkStyleListener {
    /**
     * Called when the style changes
     * @param s The new style
     */
    public void styleChanged(ILinkStyle s);
}
