/*
 * SuperTestStage.fx
 *
 * Created on 16.06.2009, 13:57:59
 */

package eu.scy.elobrowser.tool.webressource;

import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author pg
 */

 var webRess:WebRessourceNode = WebRessourceNode{};
 var scyWind = webRess.createWebRessourceWindow(webRess);

 webRess.addQuotation("tschen kaufen in der Krise weniger Fernseher und Handys. Die Segmente Unterhaltungselektronik und Telekommunikation verzeichneten im zweiten Quartal deutliche Umsatzrückgänge, wie die Ge");
 webRess.addQuotation("uro deutlich schwächer als das erste. Verantwortlich dafür war besonders der starke Rückgang bei der Unterhaltungselektronik um 11,4 Prozent im zweiten Quartal. Dies liegt laut GfK auch daran, dass im vergangenen Jahr vor der Fußball-Europameisterschaft massenweise neue Fernsehgeräte gekauft worden waren. Der Markt für Telekommunikation büßte zwischen April u");
 webRess.addQuotation("teilte die GfK mit. Zwar gehe der Trend zu höherwertigen Smartphones und Touchscreen-Mobiltelefonen, doch die Mengen- und Wertverluste würden dadurch nicht aufgefangen. Gut laufen dagegen weiterhin Elektrogeräte, angefangen von Espressoautomaten über Ganzkörper-Rasierer (Bodygroom");
 
Stage {
    title: "Application title"
    width: 600
    height: 600
    scene: Scene {
        content: [
            scyWind
        ]
    }
}