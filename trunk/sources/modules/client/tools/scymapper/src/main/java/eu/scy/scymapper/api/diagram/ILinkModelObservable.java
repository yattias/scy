package eu.scy.scymapper.api.diagram;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 20:45:12
 * To change this template use File | Settings | File Templates.
 */
public interface ILinkModelObservable {
    public void addObserver(ILinkModelObserver observer);
    public void removeObserver(ILinkModelObserver observer);
    public void notifyUpdated();
}
