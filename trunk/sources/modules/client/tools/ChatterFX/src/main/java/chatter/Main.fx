package chatter;

import javafx.stage.Stage;
import javafx.scene.Scene;

var chatter:Chatter = new Chatter();

Stage {
    title: "Jer's ChatFX";
    width: 650;
    height: 600;
    onClose: function() {
        java.lang.System.exit(0);
    }

    scene: Scene {
        content: Chatter {
            //customTable.disable = true;
            //inputText.disable = true;
            //chatHistory.disable = true;
        };
    }
}
    

