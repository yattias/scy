/**
 * 
 */
package eu.scy.lab.client.startupview.lastMission;

import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

import eu.scy.lab.client.startupview.missionBrowser.MissionBrowser;

/**
 * @author Sven
 *
 */
public class InfoWindow extends Window {

	
	public static final String DEFAULT_IMAGE_URL = "res/images/defaultIcon.png";
	
	public InfoWindow(MissionBrowser missionBrowser){
		super("Info on this mission");
		Panel panel = new Panel();
		panel.setWidth(600);
		panel.setHeight(400);
		panel.setBorder(false);
		panel.setLayout(new HorizontalLayout(15));
		
		setClosable(true);  
//		setLayout (new FitLayout());
//		setAutoWidth(true);
//		setAutoHeight(true);
		setWidth(600);  
		setHeight(400);  
		setPlain(true);  
		setCloseAction(Window.CLOSE);  
	
//		setFrame(true);
		setLayout(new HorizontalLayout(15));
		setBorder(true);

		//scaling the image
		Image img = new Image(DEFAULT_IMAGE_URL);
		double scale_factor = img.getHeight() / 50.0;
		img.setHeight("50px");
		int width = (int) (img.getWidth() / scale_factor);
		img.setWidth(Integer.toString(width));

		Panel subpanel = new Panel();
		subpanel.setBorder(false);
		subpanel.setLayout(new VerticalLayout());

		Label nameLabel = new Label("Mission-Title: " + missionBrowser.getActualMission().getTitle());
		subpanel.add(nameLabel);
		//TODO add other Mission-specific data-labels here

		panel.add(img);
		panel.add(subpanel);
		add(panel);

	}
	
}
