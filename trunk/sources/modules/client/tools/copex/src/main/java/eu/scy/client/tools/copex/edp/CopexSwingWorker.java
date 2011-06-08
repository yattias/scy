/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

//import java.util.Random;
import eu.scy.client.tools.copex.main.CopexPanel;
import javax.swing.SwingWorker;

/**
 *
 * @author Marjolaine
 */
public class CopexSwingWorker extends SwingWorker{
    CopexPanel copex = null;
    public CopexSwingWorker(CopexPanel copex) {
        this.copex = copex;
    }

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
//            Random random = new Random();
//            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            copex.loadData();
            setProgress(100);
//            while (progress < 100) {
//                //Sleep for up to one second.
//                try {
//                    Thread.sleep(random.nextInt(1000));
//                } catch (InterruptedException ignore) {}
//                //Make random progress.
//                progress += random.nextInt(10);
//                setProgress(Math.min(progress, 100));
//            }
            return null;
        }
}
