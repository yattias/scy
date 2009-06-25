package eu.scy.colemo.client.ui.api.nodes;

import eu.scy.colemo.client.ui.impl.model.ConceptNode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 22.jun.2009
 * Time: 18:29:28
 * To change this template use File | Settings | File Templates.
 */
public interface INodeObserver {
    public void moved(IConceptNode node);
    public void resized(IConceptNode node);

    public void labelChanged(IConceptNode node);

    public void styleChanged(IConceptNode conceptNode);

    public void shapeChanged(IConceptNode conceptNode);

    public void nodeSelected(ConceptNode conceptNode);
}
