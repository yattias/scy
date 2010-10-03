package eu.scy.client.tools.fxeportfolio.registration;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import javafx.util.StringLocalizer;

public class EportfolioContentCreatorFX extends ScyToolWindowContentCreatorFX {
    
    public override function createScyToolWindowContent():Node{
        StringLocalizer.associate("eu.scy.client.tools.fxeportfolio.registration.resources.EportfolioRegistration", "eu.scy.client.tools.fxeportfolio.registration");
        
        return EportfolioNode{};
    }
}
