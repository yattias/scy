package eu.scy.client.tools.fxeportfolio.registration;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELO;
import java.net.URI;
import javafx.scene.Group;
import eu.scy.tools.eportfolio.Eportfolio;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.layout.Container;


public class EportfolioNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};
    def spacing = 5.0;
    
    public override function initialize(windowContent:Boolean):Void{
    }

    public override function loadElo(uri:URI){
    }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
    }

    override function postInitialize():Void {
    }

    public override function getPrefHeight(height: Number) : Number{
        return Container.getNodePrefHeight(wrappedEportfolio, height)+wrappedEportfolio.boundsInParent.minY+spacing;
    }

    public override function getPrefWidth(width: Number) : Number{
        return Container.getNodePrefWidth(wrappedEportfolio, width);
    }

    public override function getMinHeight() : Number {
        return 400;
    }

    public override function getMinWidth() : Number {
        return 600;
    }

    function resizeContent(): Void{
        Container.resizeNode(wrappedEportfolio,width,height-wrappedEportfolio.boundsInParent.minY-spacing);
    }

    def logger = Logger.getLogger("eu.scy.client.tools.fxeportfolio.EportfolioNode");
    var epf:Eportfolio;
    var wrappedEportfolio:Node;

    public override function create(): Node {
        epf = new Eportfolio();
        wrappedEportfolio = ScySwingWrapper.wrap(epf.getGUI(), true);
        resizeContent();
        FX.deferAction(resizeContent);

        return Group {
            blocksMouse:true;
            content: [
                wrappedEportfolio
            ]
        };
    }
}
