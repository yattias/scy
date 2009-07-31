package eu.scy.colemo.client.ui.api.styling;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 17:12:05
 * To change this template use File | Settings | File Templates.
 */
public interface ILinkStyleObservable {
    public void addObserver(ILinkStyleObserver o);
    public void removeObserver(ILinkStyleObserver o);
    public boolean hasObserver(ILinkStyleObserver o);
    void notifyStyleChanged(ILinkStyle s);
}
