/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.edges;

import com.sun.javafx.functions.Function;
import javafx.async.RunnableFuture;

/**
 *
 * @author weinbrenner
 */
public class JavaBackgroundTask implements RunnableFuture {

    private final Function backgroundFunction;

    public JavaBackgroundTask(Function backgroundFunction) {
        this.backgroundFunction = backgroundFunction;
    }

    @Override
    public void run() throws Exception {
        backgroundFunction.invoke();
    }
}
