package eu.scy.scymapper.api.diagram;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:30:02
 */
public interface INodeModelObservable {
    public void addObserver(INodeModelObserver observer);
    public void removeObserver(INodeModelObserver observer);
    public void notifyMoved();
    public void notifyResized();
    public void notifyLabelChanged();
    public void notifyStyleChanged();
    public void notifyShapeChanged();
    public void notifySelected();
}
