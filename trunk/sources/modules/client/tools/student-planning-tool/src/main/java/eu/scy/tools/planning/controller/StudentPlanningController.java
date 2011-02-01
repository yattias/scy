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
import javax.swing.SwingUtilities;

import eu.scy.tools.service.LocalFreakinStudentPedagogicalPlanService;
import eu.scy.tools.service.StudentPlanELOParser;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.metadata.BasicMetadata;
import eu.scy.awareness.IAwarenessUser;
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

    private PedagogicalPlanService pedagogicalPlanService;
    private StudentPedagogicalPlanService studentPedagogicalPlanService;
    private StudentPlanELO studentPlanELO;
    private List<JXTaskPane> taskPanes = new ArrayList<JXTaskPane>();
    public Map<JXTaskPane, JXBuddyPanel> taskPanesToBuddyPanels = new HashMap<JXTaskPane, JXBuddyPanel>();
    private Configuration configuration;
    private JXTaskPaneContainer entryContainer;

    private Configuration config = Configuration.getInstance();
    private String filestreamerServer = config.getFilestreamerServer();
    private String filestreamerContext = config.getFilestreamerContext();
    private String filestreamerPort = config.getFilestreamerPort();
    private String IMAGE_BASE_DIR = "http://" + filestreamerServer + ":" + filestreamerPort + filestreamerContext;

    private ToolBrokerAPI toolbrokerApi;

    private String userName;

    private IELO elo;

    private IRepository repository;

    public IELO getElo() {
        return elo;
    }

    public void setElo(IELO elo) {
        this.elo = elo;
    }

    public StudentPlanningController(String eloId, String userName) {
        log.severe("ELO PASSED TO STP CONTROLLER CONSTRUCTOR " + eloId);
        log.severe("ELO PASSED TO STP USERNAME" + userName);
        try {
            setStudentPlanELO(this.getStudentPlanService().getStudentPlanELO(eloId));
        } catch (Exception e) {
            log.severe(e.getMessage());
        }

        log.severe("StudentPlanningController STUDENTPLANNED ELO ID" + getStudentPlanELO().getId());
        this.setToolbrokerApi(null);
        this.setUserName(userName);
    }

    public StudentPlanningController(String eloId, String userName, IELO elo, IRepository repository) {
        this.elo = elo;
        this.repository = repository;
        log.severe("ELO PASSED TO STP CONTROLLER CONSTRUCTOR " + eloId);
        log.severe("ELO PASSED TO STP USERNAME" + userName);


        try {
            setStudentPlanELO(this.getStudentPlanService().getStudentPlanELO(eloId));
        } catch (Exception e) {
            log.severe(e.getMessage());
        }

        log.severe("STUDENTPLANNEDELO " + studentPlanELO);
        this.setUserName(userName);
    }

    public StudentPlanningController() {

    }

    public StudentPlanningController(StudentPlanELO studentPlanELO, ToolBrokerAPI toolBrokerAPI, StudentPedagogicalPlanService studentPedagogicalPlanService) {
        this.setToolbrokerApi(toolBrokerAPI);
        this.setStudentPlanELO(studentPlanELO);
        this.setUserName(getToolbrokerApi().getLoginUserName());
        if (studentPedagogicalPlanService == null)
            // System.out.println("STUDENT PLANNING SERVICE IS NULL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        this.studentPedagogicalPlanService = studentPedagogicalPlanService;

        log.severe("SPT GOT USERNAME GO BUDDY GO: " + getUserName());

        //this.dumpStudentPlan(studentPlanELO);
    }


    public void updateCurrenteELOWithContent() {

        String xmlContent = StudentPlanELOParser.parseToXML(getStudentPlanService().getCurrentStudentPlanELO());

        List elos = getToolbrokerApi().getRepository().retrieveELOAllVersions(elo.getUri());

        //REALLY BIG HACK:

        for (int i = 0; i < elos.size(); i++) {
            IELO ielo = (IELO) elos.get(i);
            this.elo = ielo;

            // System.out.println("WOOOOO : REMOVE ME WHEN RETRIEVE LAST ELO VERSION IS WORKING!!!!");
            // System.out.println("WOOOOO : REMOVE ME WHEN RETRIEVE LAST ELO VERSION IS WORKING!!!!");
            // System.out.println("WOOOOO : REMOVE ME WHEN RETRIEVE LAST ELO VERSION IS WORKING!!!!");

        }

        //this.elo = getToolbrokerApi().getRepository().retrieveELOLastVersion(elo.getUri());
        // System.out.println("ELO URI: " + elo.getUri());
        this.elo.getContent().setXmlString(xmlContent);
        getToolbrokerApi().getRepository().updateELO(this.elo);
    }

    public Icon getBuddyImageIcon(String nickName) {

        if (nickName == null)
            return Images.Profile.getIcon();


        Image image = null;
        URL url;
        try {
            url = new URL(IMAGE_BASE_DIR + "?username=" + nickName);
            image = ImageIO.read(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return Images.Profile.getIcon();
        } catch (IOException e) {
            e.printStackTrace();
            return Images.Profile.getIcon();
        }


        if (image == null)
            return Images.Profile.getIcon();

        Icon icon = new ImageIcon(image.getScaledInstance(32, 32, Image.SCALE_SMOOTH));


        return icon;

    }

    public StudentPlanningController(ToolBrokerAPI toolbrokerApi) {
        this.setToolbrokerApi(toolbrokerApi);
        log.info("Starting Student Planning Controller...");
        //studentPedagogicalPlanService = this.toolbrokerApi.getStudentPedagogicalPlanService();

        if (this.getStudentPlanService() == null)
            throw new RuntimeException("STUDENT SERVICE IS NULL!!!! LAME");

        // get the user from the tool broker

        log.info("fetching student plans");
        List<StudentPlanELO> studentPlans = this.getStudentPlanService()
                .getStudentPlans("tony");

        if (studentPlans != null && studentPlans.size() > 0) {
            setStudentPlanELO(studentPlans.get(0));
            // System.out.println("PLAN:" + getStudentPlanELO());

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


    public boolean doesTaskPaneExist(String eloId) {
        for (JXTaskPane taskPane : taskPanes) {
            if (eloId.equals(taskPane.getName()))
                return true;
        }

        return false;
    }


    public void removeEntry(JXEntryPanel entryPanel) {


        StudentPlannedActivity studentPlannedActivity = entryPanel.getStudentPlannedActivity();

        log.severe("removing studentPlannedActivity :" + studentPlannedActivity);
        if (studentPlannedActivity == null) {

            String msg = "<html>There is no student planned activity to remove</html>";

            JOptionPane optionPane = new JOptionPane();
            optionPane.setMessage(msg);
            optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog(null,
                    "For Your Information");
            dialog.setVisible(true);
        } else {


            this.removeTaskPane(entryPanel.getTaskpane());
            entryContainer.remove(entryPanel);
            entryContainer.revalidate();

            try {
                getStudentPlanService().removeStudentPlannedActivityFromStudentPlan(studentPlannedActivity, this.studentPlanELO);
                updateCurrenteELOWithContent();
            } catch (Exception e) {
                log.severe(e.getMessage());
            }
        }


    }

    public void collapseAllExcept(String name) {
        // System.out.println("COLLAPSING ALL EXCEPT: " + name);
        if (name != null) {
            for (JXTaskPane tpane : taskPanes) {
                JXEntryPanel entryPanel = (JXEntryPanel) tpane.getParent();
                if (!name.equals(tpane.getName())) {
                    tpane.setCollapsed(true);
                    entryPanel.setBackgroundOff();
                } else {
                    entryPanel.setBackgroundOn();
                }

            }
        } else {
            // System.out.println("NAME WAS NULL, DID NOTHING");
        }

    }


    public JXTaskPane getOpenTaskPane() {
        for (JXTaskPane tpane : taskPanes) {
            if (tpane.isCollapsed() == false) {
                return tpane;
            }
        }

        return null;
    }

    public StudentPedagogicalPlanService getStudentPlanService() throws NullPointerException {
        return studentPedagogicalPlanService;
    }


    public List<StudentPlannedActivity> getStudentPlannedActivities() {
        return studentPlanELO.getStudentPlannedActivities();
    }

    public void dumpStudentPlan(StudentPlanELO studentPlan) {

        // System.out.println("Dumping student plan");
        // System.out.println("id:" + studentPlan.getId());
        // System.out.println("description:" + studentPlan.getDescription());
        // System.out.println("name " + studentPlan.getName());
        // System.out.println("number of activities: "
                + studentPlan.getStudentPlannedActivities().size());
        for (StudentPlannedActivity activity : studentPlan
                .getStudentPlannedActivities()) {
            dumpStudentActivity(activity);
        }
    }

    public void dumpStudentActivity(StudentPlannedActivity studentActivity) {
        // System.out.println("Dumping activity");
        // System.out.println("id: " + studentActivity.getId());
        // // System.out.println("start date: " +
        // studentActivity.getStartDate().toString());
        // // System.out.println("end date: " +
        // studentActivity.getEndDate().toString());
        // // System.out.println("name: " + studentActivity.getName());
        // // System.out.println("note: " + studentActivity.getNote());
        // // System.out.println("members: " +
        // studentActivity.getMembers().toString());
        // System.out.println("anchor elo: "
                + studentActivity.getAssoicatedELO().toString());

    }

    public void setStudentPlanELO(StudentPlanELO studentPlanELO) {
        this.studentPlanELO = studentPlanELO;
    }

    public StudentPlanELO getStudentPlanELO() {
        return studentPlanELO;
    }

    public void saveStudentActivity(StudentPlannedActivity studenPlannedActivity) {

        try {
            this.getStudentPlanService().save((ScyBaseObject) studenPlannedActivity);
            updateCurrenteELOWithContent();
        } catch (Exception e) {
            log.severe(e.getMessage());
        }

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
            final StudentPlannedActivity studentPlannedActivity, final String nickName) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                log.severe("Adding MEMBER TO STUDENT ACTIVITY " + studentPlannedActivity.getId() + " " + nickName + " " + studentPlannedActivity);
                try {
                    getStudentPlanService().addMember(studentPlannedActivity, nickName);
                    updateCurrenteELOWithContent();
                } catch (Exception e) {
                    log.severe(e.getMessage());
                }
            }
        });

    }

    public void removeMemberStudentPlannedActivity(
            StudentPlannedActivity studentPlannedActivity, String nickName) {
        // TODO Auto-generated method stub

        log.severe("YOU DONT LIKE, REMOVING STUDENT PLANNED ACTIVITY: " + studentPlannedActivity.getId() + " " + studentPlannedActivity.getName() + " from user: " + nickName);
        try {
            this.getStudentPlanService().removeMember(studentPlannedActivity, nickName);
            updateCurrenteELOWithContent();
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
        //studentPlannedActivity.removeMember(nickName);
        //this.getStudentPlanService().save((ScyBaseObject) studentPlannedActivity);

    }

    public StudentPlannedActivity getStudentPlannedIdFromEloId(String eloId) {


        log.severe("ELO ID TO GET: " + eloId);
        log.severe("USERNAME to use" + getUserName());

        if (getStudentPlanELO() == null) {
            log.severe("Student plan ELO is null");
            throw new NullPointerException("STUDENT PLANNED ELO IS NULL - FUNKY ");

        } else {
            String id = getStudentPlanELO().getId();
            log.severe("Student plan id " + id);


            StudentPlannedActivity spa = null;
            try {
                if (getStudentPlanService() == null) // System.out.println("STUDENTPLAN SERVICE IS NULL!!");
                spa = this.getStudentPlanService().getStudentPlannedActivity(getUserName(), eloId, id);
                /*this.elo = getToolbrokerApi().getRepository().retrieveELOLastVersion(elo.getUri());
                this.elo.getContent().setXmlString(StudentPlanELOParser.parseToXML(getStudentPlanService().getCurrentStudentPlanELO()));
                getToolbrokerApi().getRepository().updateELO(this.elo);*/
                updateCurrenteELOWithContent();


            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane optionPane = new JOptionPane();
                String msg = "<html>Error! This ELO id <b>" + eloId + "</b> is not connected to the Pedagogical Plan.</html>";
                optionPane.setMessage(msg);
                optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog(null,
                        "Whoa!");
                dialog.setVisible(true);
            }

            return spa;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setToolbrokerApi(ToolBrokerAPI toolbrokerApi) {
        this.toolbrokerApi = toolbrokerApi;
    }

    public ToolBrokerAPI getToolbrokerApi() {
        return toolbrokerApi;
    }
}
