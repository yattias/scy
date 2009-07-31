package eu.scy.colemo.client.ui.api.links;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 20:45:12
 * To change this template use File | Settings | File Templates.
 */
public interface ILinkObservable {
    public void addObserver(ILinkObserver observer);
    public void removeObserver(ILinkObserver observer);
    public void notifyUpdated();
}
