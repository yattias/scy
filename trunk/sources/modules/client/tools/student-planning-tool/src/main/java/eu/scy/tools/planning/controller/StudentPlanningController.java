package eu.scy.tools.planning.controller;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import roolo.elo.metadata.BasicMetadata;
import eu.scy.common.configuration.Configuration;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.tools.planning.ui.JXBuddyPanel;
import eu.scy.tools.planning.ui.JXEntryPanel;
import eu.scy.tools.planning.ui.images.Images;

public class StudentPlanningController {

	private static Logger log = Logger
			.getLogger("StudentPlanningController.class");

	private StudentPedagogicalPlanService studentPedagogicalPlanService;
	// private UserDAO userDAOHibernate;
	private PedagogicalPlanService pedagogicalPlanService;
	private List<StudentPlannedActivity> studentPlannedActivities;
	private StudentPlanELO studentPlanELO;
	private List<JXTaskPane> taskPanes = new ArrayList<JXTaskPane>();
	public Map<JXTaskPane, JXBuddyPanel> taskPanesToBuddyPanels = new HashMap<JXTaskPane, JXBuddyPanel>();
	private Configuration configuration;
	private JXTaskPaneContainer entryContainer;
	
	private Configuration config = Configuration.getInstance();
    private String filestreamerServer = config.getFilestreamerServer();
    private String filestreamerContext = config.getFilestreamerContext();
    private String filestreamerPort = config.getFilestreamerPort();
    private String IMAGE_BASE_DIR = "http://" + filestreamerServer+":"+filestreamerPort+filestreamerContext;

	private ToolBrokerAPI toolbrokerApi;
	

	public StudentPlanningController(String eloId, String s) {

	}
	
	public StudentPlanningController() {

	}
	
	public StudentPlanningController(StudentPlanELO studentPlanELO, ToolBrokerAPI toolBrokerAPI) {
		this.toolbrokerApi = toolBrokerAPI;
		this.studentPlanELO = studentPlanELO;
		
		this.dumpStudentPlan(studentPlanELO);
	}
	
	public Icon getBuddyImageIcon(String nickName) {
		
		if( nickName == null)
			return Images.Profile.getIcon();
			
			
		Image image = null;
		URL url;
		try {
			url = new URL (IMAGE_BASE_DIR + "?username="+nickName);
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return Images.Profile.getIcon();
		} catch (IOException e) {
			e.printStackTrace();
			return Images.Profile.getIcon();
		}
		
		
		
		Icon icon = new ImageIcon(image.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
	
	
		return icon;
		
	}

	public StudentPlanningController(ToolBrokerAPI toolbrokerApi) {
		this.toolbrokerApi = toolbrokerApi;
		log.info("Starting Student Planning Controller...");
		 //studentPedagogicalPlanService = this.toolbrokerApi.getStudentPedagogicalPlanService();

		studentPedagogicalPlanService = this.getStudentPlanService();
		if (studentPedagogicalPlanService == null) 
			throw new RuntimeException("STUDENT SERVICE IS NULL!!!! LAME");

		// get the user from the tool broker

		log.info("fetching student plans");
		List<StudentPlanELO> studentPlans = studentPedagogicalPlanService
				.getStudentPlans("tony");

		if( studentPlans != null && studentPlans.size() > 0 ) {
			 setStudentPlanELO(studentPlans.get(0));
	         System.out.println("PLAN:" + getStudentPlanELO());
	         
			for (StudentPlanELO studentPlanELO : studentPlans) {
				this.dumpStudentPlan(studentPlanELO);
			}
		} else {
			
			//create a new plan
			setStudentPlanELO(null);
			return;
		}

		// pedagogicalPlanService = toolbroker.getPedagogicalPlanService();
		// pedagogicalPlanService.getPedagogicalPlan(mission, scenario);
		// how do i get the current ped plan if dont know what mission i am
		// currently logined in as. QUESTION, how do i figure out which mission
		// am in from the tool broker? or in general. because if
		// getPedagogicalPlan is accessable from toolbroker and it needs a
		// mission how do i get it?
		// do i get the ped plan that student is currently on and find the
		// studentPlanELO that is assocaiated with it?
		// PedagogicalPlan pedagogicalPlan =
		// pedagogicalPlanService.getPedagogicalPlan(null, null);

		log.info("fetch done......");

	}

	public void addTaskPane(JXTaskPane taskPane) {
		taskPanes.add(taskPane);
	}

	public void removeTaskPane(JXTaskPane taskPane) {
		taskPanes.remove(taskPane);
	}
	
	public void removeEntry(JXEntryPanel entryPanel) {
		
		
		StudentPlannedActivity studentPlannedActivity = entryPanel.getStudentPlannedActivity();
		
		log.severe("removing studentPlannedActivity :" + studentPlannedActivity);
		if( studentPlannedActivity == null ) {
			
				String msg = "<html>There is no student planned activity to remove</html>";

				JOptionPane optionPane = new JOptionPane();
				optionPane.setMessage(msg);
				optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(null,
						"For Your Information");
				dialog.setVisible(true);
		} else {
			taskPanes.remove(entryPanel);
			entryContainer.remove(entryPanel);
			entryContainer.revalidate();
			
			getStudentPlanService().removeStudentPlannedActivityFromStudentPlan(studentPlannedActivity, this.studentPlanELO);
			//getStudentPlanService().save((ScyBaseObject) this.studentPlanELO);
			
		}
		
		
		
	}

	public void collapseAllExcept(String name) {
		for (JXTaskPane tpane : taskPanes) {
			JXEntryPanel entryPanel = (JXEntryPanel) tpane.getParent();
			if (!tpane.getName().equals(name)) {
				tpane.setCollapsed(true);
				entryPanel.setBackgroundOff();
			} else {
				entryPanel.setBackgroundOn();
			}

		}
	}
	
	
	
	public  JXTaskPane getOpenTaskPane() {
		for (JXTaskPane tpane : taskPanes) {
			if( tpane.isCollapsed() == false ) {
				return tpane;
			}
		}
		
		return null;
	}
	public StudentPedagogicalPlanService getStudentPlanService() {

		// service =
		// getWithUrl("http://localhost:8080/server-external-components/remoting/studentPlan-httpinvoker");
//		if (studentPedagogicalPlanService == null)
		configuration = Configuration.getInstance();
		studentPedagogicalPlanService = getWithUrl(configuration.getStudentPlanningToolUrl());
			//studentPedagogicalPlanService = getWithUrl("http://scy.collide.info:8080/extcomp/remoting/studentPlan-httpinvoker");

		return studentPedagogicalPlanService;

	}

	
	public void acceptDrop(Object drop) {
//		log.info("we just dropped a load of..." + drop.toString());
//		if( drop instanceof BasicMetadata ) {
//			
//			
//			
//			
//		} else if(drop instanceof IAwarenessUser ){
//			IAwarenessUser awarenessUser = ((IAwarenessUser)drop);
//			JXTaskPane openTaskPane = getOpenTaskPane();
//			
//			JXBuddyPanel jxBuddyPanel = this.taskPanesToBuddyPanels.get(openTaskPane);
//			jxBuddyPanel.addBuddy(awarenessUser);
//			//awarenessUser.get
//		}
//		
	}
	
	
	
	private StudentPedagogicalPlanService getWithUrl(String url) {
		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
		fb.setServiceInterface(StudentPedagogicalPlanService.class);
		fb.setServiceUrl(url);
		fb.afterPropertiesSet();
		return (StudentPedagogicalPlanService) fb.getObject();
	}

	public List<StudentPlannedActivity> getStudentPlannedActivities() {
		return studentPlanELO.getStudentPlannedActivities();
	}

	public void dumpStudentPlan(StudentPlanELO studentPlan) {
		System.out.println("Dumping student plan");
		System.out.println("id:" + studentPlan.getId());
		System.out.println("description:" + studentPlan.getDescription());
		System.out.println("name " + studentPlan.getName());
		System.out.println("number of activities: "
				+ studentPlan.getStudentPlannedActivities().size());
		for (StudentPlannedActivity activity : studentPlan
				.getStudentPlannedActivities()) {
			dumpStudentActivity(activity);
		}
	}

	public void dumpStudentActivity(StudentPlannedActivity studentActivity) {
		System.out.println("Dumping activity");
		System.out.println("id: " + studentActivity.getId());
		// System.out.println("start date: " +
		// studentActivity.getStartDate().toString());
		// System.out.println("end date: " +
		// studentActivity.getEndDate().toString());
		// System.out.println("name: " + studentActivity.getName());
		// System.out.println("note: " + studentActivity.getNote());
		// System.out.println("members: " +
		// studentActivity.getMembers().toString());
		System.out.println("anchor elo: "
				+ studentActivity.getAssoicatedELO().toString());

	}

	public void setStudentPlanELO(StudentPlanELO studentPlanELO) {
		this.studentPlanELO = studentPlanELO;
	}

	public StudentPlanELO getStudentPlanELO() {
		return studentPlanELO;
	}

	public void saveStudentActivity(StudentPlannedActivity studenPlannedActivity) {
		this.studentPedagogicalPlanService.save((ScyBaseObject) studenPlannedActivity);
	}

	public void addMembersPanel(JXTaskPane taskpane, JXBuddyPanel membersPanel) {
		this.taskPanesToBuddyPanels.put(taskpane, membersPanel);
		
	}

	public void setEntryContainer(JXTaskPaneContainer entryContainer) {
		this.entryContainer = entryContainer;
	}

	public JXTaskPaneContainer getEntryContainer() {
		return entryContainer;
	}

	public void addMemberToStudentPlannedActivity(
			StudentPlannedActivity studentPlannedActivity, String nickName) {
		
		this.getStudentPlanService().addMember(studentPlannedActivity, nickName);
		
	}

	public void removeMemberStudentPlannedActivity(
			StudentPlannedActivity studentPlannedActivity, String nickName) {
		// TODO Auto-generated method stub
		this.getStudentPlanService().removeMember(studentPlannedActivity, nickName);
		//studentPlannedActivity.removeMember(nickName);
		//this.getStudentPlanService().save((ScyBaseObject) studentPlannedActivity);
		
	}

	public StudentPlannedActivity getStudentPlannedIdFromEloId(String eloId) {
		
		try {
			String loginUserName = toolbrokerApi.getLoginUserName();
			log.severe("login user name " + loginUserName);		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

        //I had to comment out this one since I had to change the signature of the service...
		StudentPlannedActivity spa = this.getStudentPlanService().getStudentPlannedActivity("wiwoo", "firstIdeas", eloId);
		
		return spa;
	}
}
