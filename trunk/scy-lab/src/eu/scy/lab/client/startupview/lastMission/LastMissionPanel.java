package eu.scy.lab.client.startupview.lastMission;

import java.util.Vector;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class LastMissionPanel extends Panel {

	private Vector<Mission> missions;
	
	public LastMissionPanel(){
		super("Last Missions");
		Panel wrapperPanel = new Panel();
		wrapperPanel.setLayout(new VerticalLayout(10));
		
		this.setMissions(getLastMissions());
		
		for (Mission t : missions){
			wrapperPanel.add(new Label(t.getTitle()+" was last visited at "+t.getLastVisitedDate()));
		}
		add(wrapperPanel);
		
	}
	
	/**
	 * @return the last missions from a remote service
	 */
	//TODO connect to remote service and correct return statement
	public Vector<Mission> getLastMissions(){
		
		Vector<Mission> missions = new Vector<Mission>();
		missions.add(new Mission("CO2 neutral house","09/22/2008"));
		missions.add(new Mission("Tutorial Mission","09/21/2008"));
		return missions;
	}

	/**
	 * @param missions the missions to set
	 */
	public void setMissions(Vector<Mission> missions) {
		this.missions = missions;
	}

	/**
	 * @return the missions
	 */
	public Vector<Mission> getMissions() {
		return missions;
	}
	
	
}
