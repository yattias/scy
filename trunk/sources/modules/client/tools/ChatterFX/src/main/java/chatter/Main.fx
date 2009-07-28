package chatter;
/*
 * Main.fx
 *
 * Created on Jun 19, 2009, 7:55:11 AM
 */



import javafx.stage.Stage;
import javafx.scene.Scene;
import java.lang.System;




/**
 * @author jeremyt
 */



var fxChatter = FXChatter {};


var stage: Stage = Stage {
    resizable: false;
    title: "The mighty Chatter";
	width: 500;
	height: 400;
    scene: Scene {
        content: [ 
            fxChatter
        ];
    }
}


public function run () {
    fxChatter.doIt();
    //insert "jeremyt" into fxChatter.buddyNames;
    //insert "Blue" into fxChatter.buddyNames;
}