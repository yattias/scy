package eu.scy.scymapper.api.styling;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 16:43:23
 * To change this template use File | Settings | File Templates.
 */
public interface INodeStyleObservable {
    public void addObserver(INodeStyleObserver o);
    public void removeObserver(INodeStyleObserver o);
    public boolean hasObserver(INodeStyleObserver o);
    void notifyStyleChanged(INodeStyle s);
}
