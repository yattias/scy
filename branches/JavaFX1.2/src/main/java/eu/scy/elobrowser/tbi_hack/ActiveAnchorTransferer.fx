/*
 * ActiveAnchorTransferer.fx
 *
 * Created on 1-apr-2009, 16:37:59
 */

package eu.scy.elobrowser.tbi_hack;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.missionmap.Anchor;
import java.net.URI;

/**
 * @author sikkenj
 */

public class ActiveAnchorTransferer extends EloSavedListener {
    public var roolo:Roolo;
    public var activeAnchor:Anchor on replace{
        var activeAnchorUri:URI = null;
        if (activeAnchor!=null) activeAnchorUri = activeAnchor.eloUri;
        roolo.metadataAddingRepository.setAnchorEloUri(activeAnchorUri);
        //println("new activeAnchor {activeAnchorUri}");
    }
    public var eloSavedAction:function(eloUri : URI):Void on replace {
        roolo.metadataAddingRepository.addEloSavedListener(this);
        //println("eloSavedAction set {eloSavedAction}");
        };

    public override function eloSaved(eloUri : URI):Void{
        println("eloSaved({eloUri})");
       if (eloSavedAction!=null){
           eloSavedAction(eloUri);
       }

    }

}

