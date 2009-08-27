package eu.scy.scymapper.api.nodes;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:30:02
 */
public interface INodeObservable {
    public void addObserver(INodeObserver observer);
    public void removeObserver(INodeObserver observer);
    public void notifyMoved();
    public void notifyResized();
    public void notifyLabelChanged();
    public void notifyStyleChanged();
    public void notifyShapeChanged();
    public void notifySelected();
}
