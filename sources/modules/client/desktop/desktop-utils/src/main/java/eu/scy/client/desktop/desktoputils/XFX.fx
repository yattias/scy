/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils;

import eu.scy.client.desktop.desktoputils.JavaFXUIThreadRunner;
import eu.scy.client.desktop.desktoputils.JavaFXBackgroundRunner;

/**
 * @author giemza
 */

public function deferActionAndWait(action: function(): Void): Void {
    JavaFXUIThreadRunner {
        runner: action;
    }.start();
}

public function runActionInBackgroundAndCallBack(backgroundAction: function(): Object, callbackAction: function(result: Object): Void): Void {
    JavaFXBackgroundRunner {
        runner: backgroundAction;
        finished: callbackAction;
    }.start();
}
