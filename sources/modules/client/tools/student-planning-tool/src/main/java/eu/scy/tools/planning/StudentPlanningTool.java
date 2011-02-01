package eu.scy.tools.planning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.core.model.User;
import eu.scy.core.model.UserDetails;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import eu.scy.core.model.impl.SCYTeacherUserDetails;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.tools.planning.controller.StudentPlanningController;
import eu.scy.tools.planning.ui.Colors;
import eu.scy.tools.planning.ui.JXBuddyPanel;
import eu.scy.tools.planning.ui.JXEntryPanel;
import eu.scy.tools.planning.ui.images.Images;

public class StudentPlanningTool {

	private static Logger log = Logger.getLogger("StudentPlanningTool.class"); //$NON-NLS-1$

	private static final String NO_ELOS_LABEL = "NO_ELOS_LABEL"; //$NON-NLS-1$

	private static final String TASKPANE = "TASKPANE"; //$NON-NLS-1$

	private static final String ACTIVITY_NAME = "ACTIVITY_NAME"; //$NON-NLS-1$

	private static final String END_DATE_MAP = "END_DATE_MAP"; //$NON-NLS-1$

	private static final String START_DATE_MAP = "START_DATE_MAP"; //$NON-NLS-1$

	private static final String STUDENT_PLANNED_ACTIVITY = "STUDENT_PLANNED_ACTIVITY"; //$NON-NLS-1$

	Font activityFont = new Font("Segoe UI", Font.BOLD, 11); //$NON-NLS-1$

	private PedagogicalPlanService pedagogicalPlanService;

	private JXTaskPaneContainer taskpanecontainer;

	private StudentPlanningController studentPlanningController;

	private String END_DATE_PICKER = "END_DATE_PICKER"; //$NON-NLS-1$

	private JXLabel messageLabel;

	private JScrollPane scrollPane;
	
	private String paneOpenStr = Images.class.getResource("PaneOpen.png").toExternalForm(); //$NON-NLS-1$
	private String paneClosedStr = Images.class.getResource("PaneClosed.png").toExternalForm(); //$NON-NLS-1$

	/**
	 * creates a JFrame and calls {@link #doInit} to create a JXPanel and adds
	 * the panel to this frame.
	 * 
	 * @param studentPlanningController
	 */
	public StudentPlanningTool(
			StudentPlanningController studentPlanningController) {

		new Messages();
		// Set the default locale to pre-defined locale
		//Locale.setDefault(new Locale("no", "NO"));
		// if (studentPlanningController == null) {
		// JOptionPane.showMessageDialog(null,
		// "The Server is probably not stable if u can read this!");
		// return;
		// }
		this.setStudentPlanningController(studentPlanningController);
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) { //$NON-NLS-1$
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		// Get current delay
		int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();

		// Show tool tips immediately
		ToolTipManager.sharedInstance().setInitialDelay(0);

		// Show tool tips after a second
		initialDelay = 1000;
		ToolTipManager.sharedInstance().setInitialDelay(initialDelay);
		ToolTipManager.sharedInstance().setDismissDelay(initialDelay * 4);

	}

	public void launchInFrame(final StudentPlanningTool studentPlanningToolMain) {

		JFrame frame = new JFrame("Launcher"); //$NON-NLS-1$

		// add the panel to this frame

		JXButton openFrame = new JXButton("Launch It"); //$NON-NLS-1$
		openFrame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Planning Tool"); //$NON-NLS-1$
				frame.setLayout(new MigLayout("insets 0 0 0 0")); //$NON-NLS-1$
				// JScrollPane scrollPane = new JScrollPane(doInit());
				//				
				// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				frame.add(studentPlanningToolMain.createStudentPlanningPanel());
				// frame.setPreferredSize(new Dimension(500, 600));
				// when you close the frame, the app exits
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// center the frame and show it
				frame.setLocationRelativeTo(null);
				frame.pack();
				frame.setVisible(true);

				// frame = new JFrame("Test drag panel");
				// frame.setLayout(new MigLayout("insets 0 0 0 0"));
				//
				// frame.add(createDragPanel());
				// // frame.setPreferredSize(new Dimension(500, 600));
				// // when you close the frame, the app exits
				// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				//
				// // center the frame and show it
				// frame.setLocationRelativeTo(null);
				// frame.pack();
				// frame.setVisible(true);

			}
		});

		frame.add(openFrame);

		// when you close the frame, the app exits
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// center the frame and show it
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

	}

	public JXButton testButton() {
		JXButton openFrame = new JXButton("Test components press here"); //$NON-NLS-1$
		openFrame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFrame frame = new JFrame("Test drag panel"); //$NON-NLS-1$
				frame.setLayout(new MigLayout("insets 0 0 0 0")); //$NON-NLS-1$

				// frame.add(createDragPanel());
				// frame.setPreferredSize(new Dimension(500, 600));
				// when you close the frame, the app exits
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// center the frame and show it
				frame.setLocationRelativeTo(null);
				frame.pack();
				frame.setVisible(true);

			}
		});
		return openFrame;
	}

	/** creates a JXLabel and attaches a painter to it. */
	public JComponent createStudentPlanningPanel() {

		// tweak with the UI defaults for the taskpane and taskpanecontainer
		changeUIdefaults();

		messageLabel = new JXLabel(
				Messages.getString("StudentPlanningTool.0")); //$NON-NLS-1$
		// create a taskpanecontainer
		taskpanecontainer = new JXTaskPaneContainer();
		taskpanecontainer.setLayout(new MigLayout("inset 0 0 0 0, wrap")); //$NON-NLS-1$
		this.studentPlanningController.setEntryContainer(taskpanecontainer);
		taskpanecontainer.setOpaque(true);
		taskpanecontainer.setBackgroundPainter(getTaskPaneTitlePainter());
		taskpanecontainer.setBorder(BorderFactory.createEmptyBorder());
		taskpanecontainer.getInsets(new Insets(0, 0, 0, 0));
		//taskpanecontainer.setLayout(new GridLayout(5, 1));
		//taskpanecontainer.setPreferredSize(new Dimension(800, 700));
		//taskpanecontainer.setLayout(new VerticalLayout(5));
		// add the task pane to the taskpanecontainer
		// iterate over the las structure

		if( getStudentPlanningController().getStudentPlanELO() != null ) {
			List<StudentPlannedActivity> studentPlannedActivities = getStudentPlanningController()
					.getStudentPlannedActivities();
	
			
			for (StudentPlannedActivity studentPlannedActivity : studentPlannedActivities) {
                // System.out.println("ABOUT TO CREATE ANCHOR ELO PANEL FOR ACTIVITY: " + studentPlannedActivity.getId());
				this.addTaskPane(createAnchorELOPanel(studentPlannedActivity));
			}
		} else {
			
		}


		scrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// panel.add(taskpanecontainer, BorderLayout.CENTER);
		//panel.setPreferredSize(new Dimension(500, 600));
		//scrollPane.setPreferredSize(new Dimension(400, 800));
		scrollPane.setOpaque(false);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		//JXPanel outerPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		// JXPanel panel = new JXPanel();
		//outerPanel.setBackgroundPainter(getTaskPaneTitlePainter());
		// outerPanel.setLayout(new BorderLayout());
//		outerPanel.setBorder(new TitledBorder(null, null, 0, 0, activityFont,
//				Color.black));

		scrollPane.getViewport().add(taskpanecontainer);
		// //
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// //
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scrollPane.setPreferredSize(new Dimension(500, 600));
		//scrollPane.setPreferredSize(new Dimension(750, 800));
		//outerPanel.add(scrollPane,"grow, span");
		
		// outerPanel.add(createDragPanel());

		Action infoAction = new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				
				
				
				String msg = Messages.getString("StudentPlanningTool.21") + //$NON-NLS-1$
								Messages.getString("StudentPlanningTool.22")+ //$NON-NLS-1$
								Messages.getString("StudentPlanningTool.1") + paneClosedStr +"'>" + //$NON-NLS-1$ //$NON-NLS-2$
										Messages.getString("StudentPlanningTool.25") + paneOpenStr +"'>" + //$NON-NLS-1$ //$NON-NLS-2$
										Messages.getString("StudentPlanningTool.27")+ //$NON-NLS-1$
												Messages.getString("StudentPlanningTool.28"); //$NON-NLS-1$

				JOptionPane optionPane = new JOptionPane();
				optionPane.setMessage(msg);
				optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(null,
						Messages.getString("StudentPlanningTool.29")); //$NON-NLS-1$
				dialog.setVisible(true);
				
			}
		};
		
		
		
		
		JTextField lasTestTextField = new JTextField(10);
		lasTestTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField source = (JTextField) e.getSource();
				// System.out.println("TEST IS " +source.getText()); //$NON-NLS-1$
				
				String eloId = source.getText();
				//StudentPlannedActivity studentPlannedIdFromEloId = studentPlanningController.getStudentPlannedIdFromEloId(eloId);
				
				//log.severe("ADDING NEW STP PANEL" + studentPlannedIdFromEloId);
				acceptDrop(eloId);
                //acceptDrop(source);
			}
		});
		
		lasTestTextField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyChar() == 'b') {
					JTextField source = (JTextField) e.getSource();
					// System.out.println("make buddy " +source.getText()); //$NON-NLS-1$
					
					IAwarenessUser a = new AwarenessUser();
					a.setNickName(source.getText());
					acceptDrop(a);
					
				}
			}
		});

		infoAction.putValue(infoAction.SMALL_ICON, Images.Information.getIcon());
		JXHyperlink infoLink = new JXHyperlink(infoAction);
		infoLink.setToolTipText(Messages.getString("StudentPlanningTool.32")); //$NON-NLS-1$
		
		
		JXPanel messagePanel = new JXPanel(new BorderLayout(0,0));
		
		messagePanel.add(messageLabel,BorderLayout.WEST);
		//messagePanel.add(new JXLabel(" "),"growx");
		//messagePanel.add(lasTestTextField,BorderLayout.EAST);
		//messagePanel.add(infoLink,BorderLayout.EAST);
//		topPanel.add(messageLabel);
//		topPanel.add(new JXLabel(" "));
		
		JXPanel topPanel = new JXPanel(new BorderLayout(0, 0));
		//topPanel.setBackgroundPainter(Colors.getMessageBGPainter());
		topPanel.setBackground(Color.red);
		
		
		topPanel.add(messagePanel, BorderLayout.NORTH);
		topPanel.add(scrollPane,BorderLayout.CENTER);
		//topPanel.setPreferredSize(new Dimension(725, 850));
		// infoPanel.add(dateLink);
		
		//scrollPane.setPreferredSize(topPanel.getPreferredSize());
		scrollPane.setPreferredSize(new Dimension(400, 250));
		
		return topPanel;
	}
	
	public void resizeSPT(int newWidth, int newHeight) {
		this.scrollPane.setPreferredSize(new Dimension(newWidth, newHeight));
	}

	public void acceptDrop(Object drop) {
		
	
		log.severe("======> we just dropped a load of..." + drop.toString() +  " " + drop.getClass().getName());
		log.severe("======> we just dropped a load of..." + drop.toString() +  " " + drop.getClass().getName());
		log.severe("======> we just dropped a load of..." + drop.toString() +  " " + drop.getClass().getName());
		log.severe("======> we just dropped a load of..." + drop.toString() +  " " + drop.getClass().getName()); 

		if( drop == null) {
			
			log.severe("DROOPPED IS NULL: " + drop); //$NON-NLS-1$
			
			String msg = Messages.getString("StudentPlanningTool.35"); //$NON-NLS-1$

			JOptionPane optionPane = new JOptionPane();
			optionPane.setMessage(msg);
			optionPane.setMessageType(JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(null,
					Messages.getString("StudentPlanningTool.36")); //$NON-NLS-1$
			dialog.setVisible(true);
			return;
		}
		
		if( drop instanceof String ) {
			
			//find the activity
			//create studenActivityPlane			

            log.severe("DROPPED ID: " + drop);
			String eloId = (String) drop;
			
			if( studentPlanningController.doesTaskPaneExist(eloId) ) {
				
				String msg = Messages.getString("StudentPlanningTool.37") + eloId +Messages.getString("StudentPlanningTool.38"); //$NON-NLS-1$ //$NON-NLS-2$

				JOptionPane optionPane = new JOptionPane();
				optionPane.setMessage(msg);
				optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(null,
						Messages.getString("StudentPlanningTool.39")); //$NON-NLS-1$
				dialog.setVisible(true);
				
				return;
			}
			
			
			
			log.severe("ELO ID DROPPED IN STUDENT PLANNING TOOL" + eloId); //$NON-NLS-1$
			
			StudentPlannedActivity studentPlannedIdFromEloId = studentPlanningController.getStudentPlannedIdFromEloId(eloId);
			
			log.severe("ADDING NEW STP PANEL" + studentPlannedIdFromEloId + " ID: " + studentPlannedIdFromEloId.getId()); //$NON-NLS-1$
			this.addTaskPane(createAnchorELOPanel(studentPlannedIdFromEloId));
			
		} else if(drop instanceof IAwarenessUser ){
			IAwarenessUser awarenessUser = ((IAwarenessUser)drop);
			JXTaskPane openTaskPane = studentPlanningController.getOpenTaskPane();
			
			if( openTaskPane != null ) {
				JXBuddyPanel jxBuddyPanel = studentPlanningController.taskPanesToBuddyPanels.get(openTaskPane);
			
				jxBuddyPanel.addBuddy(awarenessUser);
			
			
				log.severe("Awareness user: jid" + awarenessUser.getJid() + " nick name" + awarenessUser.getNickName()); //$NON-NLS-1$ //$NON-NLS-2$
			
				studentPlanningController.addMemberToStudentPlannedActivity((StudentPlannedActivity) openTaskPane.getClientProperty(STUDENT_PLANNED_ACTIVITY), awarenessUser.getNickName());
                getStudentPlanningController().updateCurrenteELOWithContent();
				messageLabel.setText(Messages.getString("StudentPlanningTool.44")); //$NON-NLS-1$
			} else {
				String msg = Messages.getString("StudentPlanningTool.45"); //$NON-NLS-1$

				JOptionPane optionPane = new JOptionPane();
				optionPane.setMessage(msg);
				optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(null,
						Messages.getString("StudentPlanningTool.2")); //$NON-NLS-1$
				dialog.setVisible(true);
				
				return;
			}
		}
		
		
		
		
		//studentPlanningController.acceptDrop(drop);
	}
	
	public void addTaskPane(JComponent taskpane) {
		
		
		
		
		JXEntryPanel entryPanel = new JXEntryPanel(new MigLayout("insets 5 5 5 5"), this.studentPlanningController); //$NON-NLS-1$
		entryPanel.setBackground(Colors.White.color());
		//entryPanel.addEntry(taskpane, "w 600!");
		entryPanel.addEntry(taskpane,"w 80%!, growx, right"); //$NON-NLS-1$
		entryPanel.setStudentPlannedActivity((StudentPlannedActivity) taskpane.getClientProperty(STUDENT_PLANNED_ACTIVITY));
		//entryPanel.addEntry(taskpane,null);
		
		taskpanecontainer.add(entryPanel,"w 100%!"); //$NON-NLS-1$
		
		Map<String, Date> endDateMap = (Map<String, Date>) taskpane
		.getClientProperty(END_DATE_MAP);
		Map<String, Date> startDateMap = (Map<String, Date>) taskpane
		.getClientProperty(START_DATE_MAP);

		//add dates the entry field
		modTaskPaneTitleDateRange((JXTaskPane) taskpane, startDateMap, endDateMap);
		
		
		taskpanecontainer.revalidate();
		if( taskpane instanceof JXTaskPane) {
			studentPlanningController.addTaskPane((JXTaskPane) taskpane);
		}
	}

	

	public JXTaskPane createAnchorELOPanel(
			StudentPlannedActivity studentPlannedActivity) {

        // System.out.println("CREATING ANCHOR ELO PANEL: " + studentPlannedActivity.getId());
		
		AnchorELO assoicatedELO = studentPlannedActivity.getAssoicatedELO();
		//Activity activity = assoicatedELO.getProducedBy();
		Activity activity = null;
		// create a taskpane, and set it's title and icon
		JXTaskPane taskpane = new JXTaskPane();
		if(assoicatedELO != null) taskpane.setName(assoicatedELO.getName());
		taskpane.setToolTipText(Messages.getString("StudentPlanningTool.4")); //$NON-NLS-1$
		taskpane.setCollapsed(true);
		if(assoicatedELO != null) taskpane.setTitle(assoicatedELO.getName());
		taskpane.setForeground(Color.white);
		taskpane.setBackground(Color.black);
		taskpane.setOpaque(true);
		taskpane.setAnimated(true);
		taskpane.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
//				JXTaskPane tp = (JXTaskPane) e.getSource();
//				JXEntryPanel ep = (JXEntryPanel) tp.getParent();
//				ep.setBackgroundOff();

			}

			@Override
			public void mouseEntered(MouseEvent e) {
//				JXTaskPane tp = (JXTaskPane) e.getSource();
//				JXEntryPanel ep = (JXEntryPanel) tp.getParent();
//				ep.setBackgroundOn();

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// System.out.println("clicked !!"); //$NON-NLS-1$

				JXTaskPane source = (JXTaskPane) e.getSource();
				studentPlanningController.collapseAllExcept(source.getName());
			}
		});
		Map<String, Date> endDateMap = new HashMap<String, Date>();
		Map<String, Date> startDateMap = new HashMap<String, Date>();
		taskpane.putClientProperty(STUDENT_PLANNED_ACTIVITY, studentPlannedActivity);
		taskpane.putClientProperty(START_DATE_MAP, startDateMap);
		taskpane.putClientProperty(END_DATE_MAP, endDateMap);
		taskpane.putClientProperty(ACTIVITY_NAME, studentPlannedActivity.getName());
		// taskpane.setIcon(Images.One.getIcon(24, 24));

		// cycle through all the activities

		taskpane.add(createActivityPanel(studentPlannedActivity, activity,
				taskpane, 1));

		// taskpane.setBackground(Colors.Black.color(0.5f));
		// // // create a label
		// final JXLabel label = new JXLabel();
		// label.setFont(new Font("Segoe UI", Font.BOLD, 14));
		// label.setText("task pane item 1 : a label");
		// // label.setIcon(Images.Folder.getIcon(32, 32));
		// label.setHorizontalAlignment(JXLabel.LEFT);
		// label.setBackgroundPainter(getActivitTitlePainter());
		//
		// taskpane.add(label);
		// taskpane.add(new AbstractAction() {
		// {
		// putValue(Action.NAME, "task pane item 2 : an action");
		// putValue(Action.SHORT_DESCRIPTION, "perform an action");
		// putValue(Action.SMALL_ICON, Images.NetworkConnected.getIcon(32,
		// 32));
		// }
		//
		// public void actionPerformed(ActionEvent e) {
		// label.setText("an action performed");
		// }
		// });

		return taskpane;
	}

	private List<Date> sortDates(Map<String, Date> dateMap) {
		// sort the list
		Collection<Date> values = dateMap.values();

		List<Date> sortDates = new ArrayList<Date>();
		for (Date date : values) {
			sortDates.add(date);
		}

		Collections.sort((List<Date>) sortDates);
		// System.out.println("dates: " + sortDates.toString()); //$NON-NLS-1$
		// TODO Auto-generated method stub
		return sortDates;
	}

	private JXPanel createActivityPanel(
			StudentPlannedActivity studentPlannedActivity, Activity activity,
			final JXTaskPane taskpane, int activityNumber) {

		

		String activityName;

		if (activity == null)
			activityName = Messages.getString("StudentPlanningTool.53"); //$NON-NLS-1$
		else
			activityName = activity.getName();

		JXTitledPanel activityPanel = new JXTitledPanel(activityNumber + ". " //$NON-NLS-1$
				+ activityName);
		activityPanel.setTitleFont(activityFont);
		activityPanel.setTitleForeground(Colors.White.color());

		// infoPanel.setAlpha(0.0f);

		// activityPanel.setRightDecoration(infoLink);
		// activityPanel.setTitlePainter(getActivitTitlePainter());
		activityPanel.setBackground(Colors.Black.color());
		activityPanel.setLayout(new VerticalLayout(0));
		activityPanel.setBorder(new LineBorder(Colors.Black.color()));
	

		JXPanel innerMainPanel = new JXPanel();

		innerMainPanel.setOpaque(true);
		innerMainPanel.setLayout(new MigLayout("insets 0 3 4 3")); //$NON-NLS-1$

		JXBuddyPanel membersPanel = createMembersPanel(activityPanel,
				studentPlannedActivity);
		
		studentPlanningController.addMembersPanel(taskpane, membersPanel);

		JXPanel notePanel = createNotePanel(studentPlannedActivity);
		JXPanel datePanel = createDatePanel(studentPlannedActivity, taskpane, activityName);
		
		
		innerMainPanel.add(datePanel, "span, wrap, growx"); //$NON-NLS-1$
		innerMainPanel.add(membersPanel, "span, wrap, growx"); //$NON-NLS-1$
		innerMainPanel.add(notePanel, "span, wrap, growx"); //$NON-NLS-1$

		activityPanel.add(innerMainPanel);
		
	

		return activityPanel;
	}

	/**
	 * @param studentPlannedActivity
	 * @return
	 */
	protected JXPanel createNotePanel(
			StudentPlannedActivity studentPlannedActivity) {
		JXPanel noteTextPanel = new JXPanel();
		noteTextPanel.setLayout(new BorderLayout(0, 0));
		noteTextPanel.setOpaque(false);
		noteTextPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0,
				0, 0, 0)), new TitledBorder(Messages.getString("StudentPlanningTool.59")))); //$NON-NLS-1$
		JTextArea noteTextArea = new JTextArea();

		if (studentPlannedActivity.getNote() == null
				|| StringUtils.stripToNull(studentPlannedActivity.getNote()) == null) {
			noteTextArea.setText(Messages.getString("StudentPlanningTool.60")); //$NON-NLS-1$
		} else {
			noteTextArea.setText(studentPlannedActivity.getNote());
		}

		noteTextArea.setLineWrap(true);
		noteTextArea.setRows(8);
		noteTextArea.setWrapStyleWord(true);
		noteTextArea
				.setToolTipText(Messages.getString("StudentPlanningTool.61")); //$NON-NLS-1$
		noteTextArea.putClientProperty(STUDENT_PLANNED_ACTIVITY,
				studentPlannedActivity);

        // System.out.println("Set client property::::::: " + studentPlannedActivity.getId());

		noteTextArea.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				JTextArea textArea = (JTextArea) e.getSource();

				final StudentPlannedActivity stp = (StudentPlannedActivity) textArea
						.getClientProperty(STUDENT_PLANNED_ACTIVITY);

				stp.setNote(textArea.getText());

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						// System.out.println("updating " + stp.getName() + " " + stp.getId());
                        studentPlanningController.saveStudentActivity(stp);
						messageLabel.setText(Messages.getString("StudentPlanningTool.3")); //$NON-NLS-1$
                        getStudentPlanningController().updateCurrenteELOWithContent();

					}
				});

			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});
		JScrollPane eloScroll = new JScrollPane(noteTextArea);

		noteTextPanel.add(eloScroll, BorderLayout.CENTER);
		// notesPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0,
		// 0, 0, 0)), new TitledBorder("Resources")));

		return noteTextPanel;
	}

	
	protected JXPanel createDatePanel(StudentPlannedActivity studentPlannedActivity, JXTaskPane taskpane, String activityName) {

		
		final JXDatePicker endDatePicker = new JXDatePicker();
		final JXDatePicker startDatePicker = new JXDatePicker();

	
		 endDatePicker.getEditor().setEditable(false);
		
			 
		final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy"); //$NON-NLS-1$
		
		Map<String, Date> endDateMap = (Map<String, Date>) taskpane
		.getClientProperty(END_DATE_MAP);
		Map<String, Date> startDateMap = (Map<String, Date>) taskpane
		.getClientProperty(START_DATE_MAP);

		startDatePicker.setFormats(formatter);
		
		startDatePicker.setToolTipText(Messages.getString("StudentPlanningTool.64")); //$NON-NLS-1$
		endDatePicker.setFormats(formatter);
		
		endDatePicker.putClientProperty(ACTIVITY_NAME, activityName);
		endDatePicker.putClientProperty(STUDENT_PLANNED_ACTIVITY,
				studentPlannedActivity);
		endDatePicker.setToolTipText(Messages.getString("StudentPlanningTool.65")); //$NON-NLS-1$
		endDatePicker.putClientProperty(TASKPANE, taskpane);
		endDatePicker.setToolTipText(Messages.getString("StudentPlanningTool.66")); //$NON-NLS-1$
		if (studentPlannedActivity.getEndDate() == null) {
			endDatePicker.setEnabled(false);
		} else {
			java.util.Date date = new java.util.Date(studentPlannedActivity
					.getEndDate().getTime());
			endDatePicker.setDate(date);
			endDatePicker.setEnabled(true);
			endDateMap.put(activityName, endDatePicker.getDate());
			taskpane.putClientProperty(END_DATE_MAP, endDateMap);

		}

		startDatePicker.setToolTipText(Messages.getString("StudentPlanningTool.67")); //$NON-NLS-1$
		startDatePicker.putClientProperty(ACTIVITY_NAME, activityName);
		startDatePicker.putClientProperty(TASKPANE, taskpane);
		startDatePicker.putClientProperty(STUDENT_PLANNED_ACTIVITY,
				studentPlannedActivity);
		startDatePicker.putClientProperty(END_DATE_PICKER, endDatePicker);
		if (studentPlannedActivity.getStartDate() != null) {
			log.severe(Messages.getString("StudentPlanningTool.68") + studentPlannedActivity.getStartDate()); //$NON-NLS-1$
			java.util.Date date = new java.util.Date(studentPlannedActivity
					.getStartDate().getTime());
			startDatePicker.setDate(date);
			startDateMap.put(activityName, startDatePicker.getDate());
			endDatePicker.setEnabled(true);
			taskpane.putClientProperty(START_DATE_MAP, startDateMap);

		}

		// info

		
		endDatePicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JXDatePicker endDatePicker = (JXDatePicker) e.getSource();
				
				Date endDate = endDatePicker.getDate();
				
				String activityName = (String) endDatePicker
						.getClientProperty(ACTIVITY_NAME);
				JXTaskPane lasTaskPane = (JXTaskPane) endDatePicker
						.getClientProperty(TASKPANE);

				Map<String, Date> endDateMap = (Map<String, Date>) lasTaskPane
						.getClientProperty(END_DATE_MAP);
				Map<String, Date> startDateMap = (Map<String, Date>) lasTaskPane
						.getClientProperty(START_DATE_MAP);

				
				Date startDate = startDateMap.get(activityName);
				
				if( startDate != null ) {
					
					//when firstDate is greater than secondDate will return 1
					if( startDate.compareTo(endDate) == 1 ) {
						
						final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy"); //$NON-NLS-1$
						formatter.format(startDate);
						
						String msg = Messages.getString("StudentPlanningTool.70") + formatter.format(startDate) +Messages.getString("StudentPlanningTool.71"); //$NON-NLS-1$ //$NON-NLS-2$
	

						JOptionPane optionPane = new JOptionPane();
						optionPane.setMessage(msg);
						optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
						JDialog dialog = optionPane.createDialog(null,
								Messages.getString("StudentPlanningTool.72")); //$NON-NLS-1$
						dialog.setVisible(true);
						
						endDate = startDate;
						endDatePicker.setDate(endDate);
					}
				}
				
				
				endDateMap.put(activityName, endDate);
				lasTaskPane.putClientProperty(END_DATE_MAP, endDateMap);

				// // System.out.println("client "+ activityName);
				String title = lasTaskPane.getTitle();

				final StudentPlannedActivity stp = (StudentPlannedActivity) endDatePicker
						.getClientProperty(STUDENT_PLANNED_ACTIVITY);

				//we have to this because the conversion rips silently sets to midnight
				//Date addDays = DateUtils.addDays(endDate, 1);
				stp.setEndDate(new java.sql.Date(endDate
						.getTime()));

				modTaskPaneTitleDateRange(lasTaskPane, startDateMap, endDateMap);

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						studentPlanningController.saveStudentActivity(stp);
						messageLabel.setText(Messages.getString("StudentPlanningTool.73")); //$NON-NLS-1$
					}
				});

			}

		});

		startDatePicker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JXDatePicker startDatePicker = (JXDatePicker) e.getSource();
				JXDatePicker endDatePicker = (JXDatePicker) startDatePicker
						.getClientProperty(END_DATE_PICKER);

				
				Date startDate = startDatePicker.getDate();
				Date testDate = new Date();
				endDatePicker.setEnabled(true);

				String activityName = (String) startDatePicker
						.getClientProperty(ACTIVITY_NAME);
				JXTaskPane lasTaskPane = (JXTaskPane) startDatePicker
						.getClientProperty(TASKPANE);

				Map<String, Date> endDateMap = (Map<String, Date>) lasTaskPane
						.getClientProperty(END_DATE_MAP);
				Map<String, Date> startDateMap = (Map<String, Date>) lasTaskPane
						.getClientProperty(START_DATE_MAP);

				Date endDate = endDateMap.get(activityName);
				
				if( endDate != null ) {
					
					//when firstDate is greater than secondDate will return 1
					if( startDate.compareTo(endDate) == 1 ) {
						
						final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy"); //$NON-NLS-1$
						formatter.format(endDate);
						
						String msg = Messages.getString("StudentPlanningTool.75") + formatter.format(endDate) +Messages.getString("StudentPlanningTool.76"); //$NON-NLS-1$ //$NON-NLS-2$

						JOptionPane optionPane = new JOptionPane();
						optionPane.setMessage(msg);
						optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
						JDialog dialog = optionPane.createDialog(null,
								Messages.getString("StudentPlanningTool.77")); //$NON-NLS-1$
						dialog.setVisible(true);
						startDate = endDate;
						startDatePicker.setDate(startDate);
					}
				}
				
				startDateMap.put(activityName, startDate);
				lasTaskPane.putClientProperty(START_DATE_MAP, startDateMap);

				final StudentPlannedActivity stp = (StudentPlannedActivity) startDatePicker
						.getClientProperty(STUDENT_PLANNED_ACTIVITY);
			
				java.sql.Date sql = null;
				
				//we have to this because the conversion rips silently sets to midnight
				//Date addDays = DateUtils.addDays(startDate, 1);
					sql = new java.sql.Date(startDate.getTime());
				
				
				
				stp.setStartDate(sql);

				modTaskPaneTitleDateRange(lasTaskPane, startDateMap, endDateMap);

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						studentPlanningController.saveStudentActivity(stp);
						messageLabel.setText(Messages.getString("StudentPlanningTool.78")); //$NON-NLS-1$
					}
				});

			}

		});

		
		
		
	

		JXLabel startLabel = new JXLabel(Messages.getString("StudentPlanningTool.79")); //$NON-NLS-1$
		JXLabel endLabel = new JXLabel(Messages.getString("StudentPlanningTool.80")); //$NON-NLS-1$

		JXPanel infoPanel = new JXPanel(new MigLayout("insets 0 0 0 0")); //$NON-NLS-1$

		infoPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0,
				0, 0, 0)), new TitledBorder(
				Messages.getString("StudentPlanningTool.82")))); //$NON-NLS-1$
		infoPanel.add(startLabel);
		infoPanel.add(startDatePicker);
		infoPanel.add(endLabel);
		infoPanel.add(endDatePicker);
		infoPanel.setOpaque(false);
		infoPanel.setToolTipText(Messages.getString("StudentPlanningTool.83")); //$NON-NLS-1$
		return infoPanel;
	}
	
	/**
	 * @param activityPanel
	 * @param studentPlannedActivity
	 * @return
	 */
	protected JXBuddyPanel createMembersPanel(JXTitledPanel activityPanel,
			final StudentPlannedActivity studentPlannedActivity) {
		final JXBuddyPanel membersPanel = new JXBuddyPanel(new HorizontalLayout(1), studentPlanningController);
		membersPanel.setMessageLabel(messageLabel);
		membersPanel.setStudentPlannedActivity(studentPlannedActivity);
		membersPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0,
				0, 0, 0)), new TitledBorder(
				Messages.getString("StudentPlanningTool.84")))); //$NON-NLS-1$
		membersPanel.setOpaque(false);
		membersPanel.setBackground(Color.CYAN);
		membersPanel.setPreferredSize(new Dimension(activityPanel
				.getMaximumSize().width / 2, 115));
//		membersPanel.setDropTarget(new DropTarget(membersPanel,
//				new JXDropTargetListener(this)));
		membersPanel
				.setToolTipText(Messages.getString("StudentPlanningTool.85")); //$NON-NLS-1$
		membersPanel.putClientProperty(STUDENT_PLANNED_ACTIVITY,
				studentPlannedActivity);
		
		
		//fill it
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
				List<User> members = studentPlannedActivity.getMembers();
				for (User user : members) {
					UserDetails userDetails = user.getUserDetails();
                    // System.out.println("create MEMBERS panel, adding buddies: " + userDetails.getUsername());
					String nickName = nickName = userDetails.getUsername();

					IAwarenessUser aw = new AwarenessUser();
					aw.setNickName(nickName);
					membersPanel.addBuddy(aw);
				}
		    }
		  });
		

		
		
		
		
		return membersPanel;
	}

	private void modTaskPaneTitleDateRange(JXTaskPane taskpane,
			Map<String, Date> startDateMap, Map<String, Date> endDateMap) {

		JXEntryPanel entryPanel = (JXEntryPanel) taskpane.getParent();
		final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd"); //$NON-NLS-1$

		List<Date> endDateSort = sortDates(endDateMap);
		List<Date> startDateSort = sortDates(startDateMap);

		String startDateString = " "; //$NON-NLS-1$
		String endDateString = " "; //$NON-NLS-1$

		if (!endDateSort.isEmpty()) {
			Date endDate = endDateSort.get(endDateSort.size() - 1);
			if( endDate != null)
				endDateString = formatter.format(endDate);
		}
		if (!startDateSort.isEmpty()) {
			Date startDate = startDateSort.get(0);
			if( startDateSort != null)
				startDateString = formatter.format(startDate);
		}

	
		JXLabel dateLabel = entryPanel.getDateLabel();
		String title = dateLabel.getText();

		if( StringUtils.stripToNull(endDateString) == null)
			title = startDateString;
		else
			title = startDateString + Messages.getString("StudentPlanningTool.89") + endDateString; //$NON-NLS-1$
		 entryPanel.getDateLabel().setText(Messages.getString("StudentPlanningTool.90")+title+Messages.getString("StudentPlanningTool.91")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void changeUIdefaults() {
		// JXTaskPaneContainer settings (developer defaults)
		/*
		 * These are all the properties that can be set (may change with new
		 * version of SwingX) "TaskPaneContainer.useGradient",
		 * "TaskPaneContainer.background",
		 * "TaskPaneContainer.backgroundGradientStart",
		 * "TaskPaneContainer.backgroundGradientEnd", etc.
		 */

		// setting taskpanecontainer defaults
		UIManager.put("TaskPaneContainer.useGradient", Boolean.TRUE); //$NON-NLS-1$
		UIManager.put("TaskPaneContainer.background", Colors.Gray.color(0.5f)); //$NON-NLS-1$

		// setting taskpane defaults
		UIManager.put("TaskPane.font", new FontUIResource(new Font("Segoe UI", //$NON-NLS-1$ //$NON-NLS-2$
				Font.BOLD, 13)));
		UIManager.put("TaskPane.titleBackgroundGradientStart", Colors.Black //$NON-NLS-1$
				.color(0.1f));
		UIManager.put("TaskPane.titleBackgroundGradientEnd", Colors.Blue //$NON-NLS-1$
				.color(0.1f));
	}

	/** this painter draws a gradient fill */
	public Painter getActivitTitlePainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.2f);
		Color color2 = Colors.Black.color(0.8f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
						color2, color2 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	/** this painter draws a gradient fill */

	public Painter getTaskPaneTitlePainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(1.0f);
		Color color2 = Colors.Black.color(0.5f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
						color1, color1 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	public Painter getTaskPanePainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.2f);
		Color color2 = Colors.Black.color(0.8f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
						color1, color1 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	public Painter getActivityPanePainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.2f);
		Color color2 = Colors.Black.color(0.8f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
						color2, color2 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	public Painter getDragPainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.2f);
		Color color2 = Colors.Black.color(0.8f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
						color1, color1 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	public void setStudentPlanningController(
			StudentPlanningController studentPlanningController) {
		this.studentPlanningController = studentPlanningController;
	}

	public StudentPlanningController getStudentPlanningController() {
		return studentPlanningController;
	}

}// end class TaskPaneExample1


//public JComponent createDragPanel() {
//	JXPanel dragPanel = new JXPanel();
//	dragPanel.setBorder(new TitledBorder("Drag Panel buddies and Elos"));
//	dragPanel.setPreferredSize(new Dimension(250, 300));
//	// dragPanel.setOpaque(false);
//	dragPanel.setBackgroundPainter(getDragPainter());
//
//	JXLabel elo1Label = new JXLabel(Images.Excel1.getIcon());
//	elo1Label.setText("elo 1");
//	elo1Label.setName("anchor elo 1");
//
//	JXLabel elo2Label = new JXLabel(Images.Excel2.getIcon());
//	elo2Label.setText("elo 2");
//	elo2Label.setName("anchor elo 2");
//
//	
//	IAwarenessUser user1 = new AwarenessUser();
//	user1.setNickName("bob");
//	
//	
//	JXLabel member1Label = new JXLabel(Images.Profile.getIcon());
//	member1Label.setText("bob");
//	member1Label.putClientProperty("USER", user1);
//
//	IAwarenessUser user2 = new AwarenessUser();
//	user2.setNickName("jack");
//	
//	JXLabel member2Label = new JXLabel(Images.Profile.getIcon());
//	member2Label.setText("jack");
//	member2Label.putClientProperty("USER", user2);
//	
//	JXLabel anchorELOLabel = new JXLabel("ANCHOR ELO");
//	anchorELOLabel.setText("jack");
//	anchorELOLabel.setBackground(Color.black);
//	anchorELOLabel.setForeground(Color.white);
//
//	MouseListener listener = new MouseAdapter() {
//		public void mousePressed(MouseEvent e) {
//			JComponent c = (JComponent) e.getSource();
//			TransferHandler handler = c.getTransferHandler();
//			handler.exportAsDrag(c, e, TransferHandler.COPY);
//		}
//	};
//
//	elo1Label.setTransferHandler(new ImageSelection());
//	elo1Label.addMouseListener(listener);
//	elo2Label.setTransferHandler(new ImageSelection());
//	elo2Label.addMouseListener(listener);
//
//	member1Label.setTransferHandler(new ImageSelection());
//	member1Label.addMouseListener(listener);
//	member2Label.setTransferHandler(new ImageSelection());
//	member2Label.addMouseListener(listener);
//
//	anchorELOLabel.setTransferHandler(new ImageSelection());
//	anchorELOLabel.addMouseListener(listener);
//	anchorELOLabel.setTransferHandler(new ImageSelection());
//	anchorELOLabel.addMouseListener(listener);
//
//	// dragPanel.addMouseListener(listener);
//	dragPanel.add(elo1Label);
//	dragPanel.add(elo2Label);
//	dragPanel.add(member1Label);
//	dragPanel.add(member2Label);
//	// dragPanel.add(anchorELOLabel);
//
//	// DragSource dragSource = DragSource.getDefaultDragSource();
//	// dragSource.createDefaultDragGestureRecognizer(bobLabel,DnDConstants.ACTION_MOVE,
//	// new JXDragListener());
//	// //dragPanel.setDropTarget(new DropTarget(dragPanel, new
//	// JXDropTargetListener()));
//	return dragPanel;
//}
