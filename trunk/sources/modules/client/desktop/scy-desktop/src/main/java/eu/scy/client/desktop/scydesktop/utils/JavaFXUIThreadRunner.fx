package eu.scy.client.desktop.scydesktop.utils;

import java.lang.Runnable;
import javax.swing.SwingUtilities;

/**
 * The purpose of this class is to put JavaFX code onto the UI thread
 * and wait until the execution is done. This is the main difference to
 * FX.deferActtion().
 *
 * @author Adam
 */
public class JavaFXUIThreadRunner extends Runnable {

    public-init var runner: function();

    override public function run () : Void {
        runner();
    }

    public function start() {
        SwingUtilities.invokeAndWait(this);
    }
}