/**
 * 
 */
package eu.scy.lab.client.mission;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;


/**
 * @author Sven
 *
 */
public class MissionInfoWindow extends Window {
    
    private Mission mission;

	
	public static final String DEFAULT_IMAGE_URL = "res/icons/Spreadsheet.png";
	
	public MissionInfoWindow(Mission mission){
		super("Info on this mission");
		
		//FIXME replace it: set a real mission repository
		if (mission.getTitle().equals("CO2 neutral house")){
		    mission.setDescription("This Mission shows some Info on the Co2 neutral house. Collect all infos to answer the given questions at the end of this mission!");
		    mission.setGoal("Pass the test!");
		} else {
		    mission.setDescription("This Mission is a tutorial which will help you to use the SCY-Lab. Pass all slides to complete this mission!");
		    mission.setGoal("Pass the slides");
		}
		this.mission = mission;
		setLayout(new FitLayout());
		add(buildWindow());
		
		setClosable(true);  
		setWidth(450);  
		setHeight(300);  
		setPlain(true);  
		setCloseAction(Window.CLOSE);  
	
		setBorder(true);


	}
	
	public Panel buildWindow(){
	    Panel main = new Panel();
	    Panel header = createHeader();
	    Panel foot = createFoot();
	    main.add(header);
	    main.add(foot);
	    return main;
	}

    private Panel createFoot() {
        Panel panel = new Panel();
        panel.setBorder(false);
        Label description = new Label();
        description.setHtml("<p><em><b>Description:</b> " + mission.getDescription()+"</em></p>");
        Label hints = new Label();
        hints.setHtml("<br> <p> <b>Hints: </b></p> <p> <li>read all ressources carefully</li> <li>take notes</li> <li>don't try to walk through this mission as fast as possible</li> </p>");
        
        panel.add(description);
        panel.add(hints);
        return panel;
    }

    private Panel createHeader() {
        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setLayout(new HorizontalLayout(15));
        
        Label img = new Label();
        img.setHtml("<p><center><img src=\"res/icons/Spreadsheet.png\"></p>");

        
        Panel subPanel = new Panel();
        subPanel.setBorder(false);
        subPanel.setLayout(new VerticalLayout());
        Label nameLabel = new Label();
       nameLabel.setHtml("<p><b>Mission-Title:</b> " + mission.getTitle()+"</p>");
        Label goal = new Label ();
        goal.setHtml("<p><b>Goal:</b> " + mission.getGoal()+"</p>");
        subPanel.add(nameLabel);
        subPanel.add(goal);
        
        panel.add(img);
        panel.add(subPanel);
 
        return panel;
    }
	
}
