/*
 * IEdgesManager.fx
 *
 * Created on 08.02.2010, 16:37:24
 */

package eu.scy.client.desktop.scydesktop.edges;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;

/**
 * @author pg, lars
 */

public abstract class IEdgesManager extends CustomNode {

    public abstract function findLinks(targetWindow:ScyWindow):Void;

    public abstract function addDatasyncLink(scyWindowStart: ScyWindow, scyWindowEnd: ScyWindow): DatasyncEdge;

    public abstract function removeDatasyncLink(edge:DatasyncEdge): Void;
}
