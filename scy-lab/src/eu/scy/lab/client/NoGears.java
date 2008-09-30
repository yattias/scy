/**
 * 
 */
package eu.scy.lab.client;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Sven
 *
 */


/**
 * This entry point will be used when no Gears plugin is detected
 */
public class NoGears {
  public void onModuleLoad() {
    RootPanel rootPanel = RootPanel.get();
    rootPanel.add(new HTML(
        "<font color=\"red\">ERROR: This browser does not support Gears. "
        + " Please <a href=\"http://gears.google.com/\">install Gears</a> " 
        + "and reload the application.  Note that GWT Gears applications can "
        + "only be debugged in hosted mode on Windows.</font>"));
  }
}