/*
 * ScyWindowContentCreator.fx
 *
 * Created on 23-mrt-2009, 10:27:27
 */

package eu.scy.elobrowser.tool.elofactory;

import java.net.URI;
import javafx.scene.Node;

/**
 * @author sikkenj
 */

public class ScyWindowContentCreator {
    public var scyWindowContentFactories: ScyWindowContentFactory[];

    public function getScyWindowContent(eloUri:URI):Node{
        var scyWindowContent: Node = null;
        var maxSuitability = 0;
        var useScyWindowContentFactory: ScyWindowContentFactory = null;
        for (scyWindowContentFactory in scyWindowContentFactories){
            var suitability = scyWindowContentFactory.getSuitability(eloUri);
            if (suitability > maxSuitability){
                useScyWindowContentFactory = scyWindowContentFactory;
                maxSuitability = suitability;
            }
        }
        if (useScyWindowContentFactory != null){
            scyWindowContent = useScyWindowContentFactory.getScyWindowContent(eloUri);
        }
        return scyWindowContent;
    }

}
