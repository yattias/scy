/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.edges;

import javafx.async.JavaTaskBase;
import javafx.async.RunnableFuture;
import com.sun.javafx.functions.Function;

/**
 * @author weinbrenner
 */
public class BackgroundTask extends JavaTaskBase {

    public-init var backgroundFunction : Function;

    protected override function create(): RunnableFuture {
        new JavaBackgroundTask(backgroundFunction);
    }

}
