package eu.scy.tools.planning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
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
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import eu.scy.core.model.impl.pedagogicalplan.ActivityImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.tools.dnd.ImageSelection;
import eu.scy.tools.dnd.JXDropTargetListener;
import eu.scy.tools.dnd.JXDropTaskPaneTargetListener;
import eu.scy.tools.planning.controller.StudentPlanningController;
import eu.scy.tools.planning.ui.Colors;
import eu.scy.tools.planning.ui.images.Images;

public class StudentPlanningTool {
	
	private static final String TASKPANE = "TASKPANE";

	private static final String ACTIVITY_NAME = "ACTIVITY_NAME";

	private static final String END_DATE_MAP = "END_DATE_MAP";

	private static final String START_DATE_MAP = "START_DATE_MAP";

	private static final String STUDENT_PLANNED_ACTIVITY = "STUDENT_PLANNED_ACTIVITY";

	Font activityFont = new Font("Segoe UI", Font.BOLD, 11);

	private PedagogicalPlanService pedagogicalPlanService;
	

	private JXTaskPaneContainer taskpanecontainer;

	private StudentPlanningController studentPlanningController;
	
	private StudentPlanningController controller = new StudentPlanningController();

	private String END_DATE_PICKER = "END_DATE_PICKER";

	/**
	 * creates a JFrame and calls {@link #doInit} to create a JXPanel and adds
	 * the panel to this frame.
	 * @param studentPlanningController 
	 */
	public StudentPlanningTool(StudentPlanningController studentPlanningController) {
		
		
		if( studentPlanningController == null ) {
			JOptionPane.showMessageDialog(null, "Student Planning Tool wasnt configured correctly!");
			return;
		}
		this.setStudentPlanningController(studentPlanningController);
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		// Get current delay
	    int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();
	    
	    // Show tool tips immediately
	    ToolTipManager.sharedInstance().setInitialDelay(0);
	    
	    // Show tool tips after a second
	    initialDelay = 1000;
	    ToolTipManager.sharedInstance().setInitialDelay(initialDelay);
	    ToolTipManager.sharedInstance().setDismissDelay(initialDelay*4);
		

	}
	
	public void launchInFrame() {
		
		JFrame frame = new JFrame("Launcher");

		// add the panel to this frame
		
		JXButton openFrame = new JXButton("Launch It");
		openFrame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Planning Tool");
				frame.setLayout(new MigLayout("insets 0 0 0 0"));
//				JScrollPane scrollPane = new JScrollPane(doInit());
//				
//				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				frame.add(createStudentPlanningPanel(null));
				//frame.setPreferredSize(new Dimension(500, 600));
				// when you close the frame, the app exits
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// center the frame and show it
				frame.setLocationRelativeTo(null);
				frame.pack();
				frame.setVisible(true);
				
				frame = new JFrame("Test drag panel");
				frame.setLayout(new MigLayout("insets 0 0 0 0"));
				
				frame.add(createDragPanel());
				//frame.setPreferredSize(new Dimension(500, 600));
				// when you close the frame, the app exits
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// center the frame and show it
				frame.setLocationRelativeTo(null);
				frame.pack();
				frame.setVisible(true);

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
		JXButton openFrame = new JXButton("Test components press here");
		openFrame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {


				JFrame frame = new JFrame("Test drag panel");
				frame.setLayout(new MigLayout("insets 0 0 0 0"));
				
				frame.add(createDragPanel());
				//frame.setPreferredSize(new Dimension(500, 600));
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
	public JComponent createStudentPlanningPanel(Scenario s) {
		
		Scenario scenario = setupPedagogicalPlan();
		
		
		// tweak with the UI defaults for the taskpane and taskpanecontainer
		changeUIdefaults();

		// create a taskpanecontainer
		taskpanecontainer = new JXTaskPaneContainer();
		taskpanecontainer.setOpaque(true);
		taskpanecontainer.setBackgroundPainter(getTaskPaneTitlePainter());
		taskpanecontainer.setLayout(new VerticalLayout(5));

		// add the task pane to the taskpanecontainer
		// iterate over the las structure

		
        taskpanecontainer.add(testButton());
        
        List<StudentPlannedActivity> studentPlannedActivities = getStudentPlanningController().getStudentPlannedActivities();
        
        for (StudentPlannedActivity studentPlannedActivity : studentPlannedActivities) {
        	this.addTaskContainer(createAnchorELOPanel(studentPlannedActivity));
		}
        
        
		

		taskpanecontainer.setDropTarget(new DropTarget(taskpanecontainer, new JXDropTaskPaneTargetListener(this)));
		
		// set the transparency of the JXPanel to 50% transparent
		// panel.setAlpha(0.7f);

		JScrollPane scrollPane = new JScrollPane(taskpanecontainer,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		panel.add(taskpanecontainer, BorderLayout.CENTER);
//		panel.setPreferredSize(new Dimension(500, 600));
		scrollPane.setPreferredSize(new Dimension(400, 600));
		scrollPane.setOpaque(false);
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		JXPanel outerPanel = new JXPanel(new HorizontalLayout(1));
		//JXPanel panel = new JXPanel();
		outerPanel.setBackgroundPainter(getTaskPaneTitlePainter());
		//outerPanel.setLayout(new BorderLayout());
		outerPanel.setBorder(new TitledBorder(null, null, 0, 0, activityFont, Color.black));
		
		
//		scrollPane.getViewport().add(panel);
////		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
////		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		scrollPane.setPreferredSize(new Dimension(500, 600));
		outerPanel.add(scrollPane);
		
		//outerPanel.add(createDragPanel());
		
		
		
		
		return outerPanel;
	}
	
	public void addTaskContainer(JXTaskPane taskpane) {
		taskpanecontainer.add(taskpane);
		taskpanecontainer.revalidate();
		controller.addTaskPane(taskpane);
	}

	private JComponent createDragPanel() {
		JXPanel dragPanel = new JXPanel();
		dragPanel.setBorder(new TitledBorder("Drag Panel buddies and Elos"));
		dragPanel.setPreferredSize(new Dimension(250, 300));
		//dragPanel.setOpaque(false);
		dragPanel.setBackgroundPainter(getDragPainter());
		
		JXLabel elo1Label = new JXLabel(Images.Excel1.getIcon());
		elo1Label.setText("elo 1");
		elo1Label.setName("anchor elo 1");
		
		JXLabel elo2Label = new JXLabel(Images.Excel2.getIcon());
		elo2Label.setText("elo 2");
		elo2Label.setName("anchor elo 2");
		
		JXLabel member1Label = new JXLabel(Images.Profile.getIcon());
		member1Label.setText("bob");
		
		JXLabel member2Label = new JXLabel(Images.Profile.getIcon());
		member2Label.setText("jack");
		
		JXLabel anchorELOLabel = new JXLabel("ANCHOR ELO");
		anchorELOLabel.setText("jack");
		anchorELOLabel.setBackground(Color.black);
		anchorELOLabel.setForeground(Color.white);
		
		
		
		MouseListener listener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
	            JComponent c = (JComponent) e.getSource();
	            TransferHandler handler = c.getTransferHandler();
	            handler.exportAsDrag(c, e, TransferHandler.COPY);
	        }
		};
		
		elo1Label.setTransferHandler(new ImageSelection());
		elo1Label.addMouseListener(listener);
		elo2Label.setTransferHandler(new ImageSelection());
		elo2Label.addMouseListener(listener);
		
		
		member1Label.setTransferHandler(new ImageSelection());
		member1Label.addMouseListener(listener);
		member2Label.setTransferHandler(new ImageSelection());
		member2Label.addMouseListener(listener);


		anchorELOLabel.setTransferHandler(new ImageSelection());
		anchorELOLabel.addMouseListener(listener);
		anchorELOLabel.setTransferHandler(new ImageSelection());
		anchorELOLabel.addMouseListener(listener);
		
		
		
		//dragPanel.addMouseListener(listener);
		dragPanel.add(elo1Label);
		dragPanel.add(elo2Label);
		dragPanel.add(member1Label);
		dragPanel.add(member2Label);
		//dragPanel.add(anchorELOLabel);
		
//		DragSource dragSource = DragSource.getDefaultDragSource();
//		    dragSource.createDefaultDragGestureRecognizer(bobLabel,DnDConstants.ACTION_MOVE, new JXDragListener());
//		//dragPanel.setDropTarget(new DropTarget(dragPanel, new JXDropTargetListener()));
		return dragPanel;
	}

	public  JXTaskPane createAnchorELOPanel(StudentPlannedActivity studentPlannedActivity) {

		AnchorELO assoicatedELO = studentPlannedActivity.getAssoicatedELO();
		Activity activity = assoicatedELO.getProducedBy();
		// create a taskpane, and set it's title and icon
		JXTaskPane taskpane = new JXTaskPane();
		taskpane.setName(assoicatedELO.getName());
		taskpane.setToolTipText("Click to expand.");
		taskpane.setCollapsed(true);
		taskpane.setTitle(assoicatedELO.getName());
		taskpane.setForeground(Color.white);
		taskpane.setBackground(Color.black);
		taskpane.setOpaque(true);
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("clicked !!");
				
				JXTaskPane source = (JXTaskPane) e.getSource();
				controller.collapseAllExcept(source.getName());
			}
		});
		Map<String, Date> endDateMap = new HashMap<String, Date>();
		Map<String, Date> startDateMap = new HashMap<String, Date>();
		taskpane.putClientProperty(START_DATE_MAP, startDateMap);
		taskpane.putClientProperty(END_DATE_MAP, endDateMap);
		//taskpane.setIcon(Images.One.getIcon(24, 24));

		//cycle through all the activities
		
		taskpane.add(createActivityPanel(studentPlannedActivity, activity, taskpane, 1));
		
		

		
//		taskpane.setBackground(Colors.Black.color(0.5f));
////		// create a label
//		final JXLabel label = new JXLabel();
//		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
//		label.setText("task pane item 1 : a label");
//		// label.setIcon(Images.Folder.getIcon(32, 32));
//		label.setHorizontalAlignment(JXLabel.LEFT);
//		label.setBackgroundPainter(getActivitTitlePainter());
//
//		taskpane.add(label);
//		taskpane.add(new AbstractAction() {
//			{
//				putValue(Action.NAME, "task pane item 2 : an action");
//				putValue(Action.SHORT_DESCRIPTION, "perform an action");
//				putValue(Action.SMALL_ICON, Images.NetworkConnected.getIcon(32,
//						32));
//			}
//
//			public void actionPerformed(ActionEvent e) {
//				label.setText("an action performed");
//			}
//		});

		return taskpane;
	}

	private List<Date> sortDates(Map<String, Date> dateMap) {
		//sort the list
		Collection<Date> values = dateMap.values();
		
		List<Date> sortDates = new ArrayList<Date>();
		for (Date date : values) {
			sortDates.add(date);
		}
		
		Collections.sort((List<Date>) sortDates);
		System.out.println("dates: " + sortDates.toString());
		// TODO Auto-generated method stub
		return sortDates;
	}

	private JXPanel createActivityPanel(StudentPlannedActivity studentPlannedActivity, Activity activity, final JXTaskPane taskpane, int activityNumber) {
		
		final JXDatePicker endDatePicker = new JXDatePicker();
		final JXDatePicker startDatePicker = new JXDatePicker();
		
		final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
	
	    startDatePicker.setFormats(formatter);

	    startDatePicker.setToolTipText("The date of the start of this task.");
		endDatePicker.setFormats(formatter);
		
		
		
	
		String activityName;
		
		if( activity == null)
			activityName = "Best Activity in the world";
		else
			activityName = activity.getName();
		
		JXTitledPanel activityPanel = new JXTitledPanel(activityNumber + ". " + activityName);
		activityPanel.setTitleFont(activityFont);
		activityPanel.setTitleForeground(Colors.White.color());
		
		endDatePicker.putClientProperty(ACTIVITY_NAME, activityName);
		endDatePicker.putClientProperty(STUDENT_PLANNED_ACTIVITY, studentPlannedActivity);
		endDatePicker.setToolTipText("The date of the end of this task.");
		endDatePicker.putClientProperty(TASKPANE, taskpane);
	
		if( studentPlannedActivity.getEndDate() == null ) {
			endDatePicker.setEditable(false);
		} else {
			java.util.Date date = 
		        new java.util.Date(studentPlannedActivity.getEndDate().getTime());
			endDatePicker.setDate(date);
		}
		
		
		startDatePicker.putClientProperty(ACTIVITY_NAME, activityName);
		startDatePicker.putClientProperty(TASKPANE, taskpane);
		startDatePicker.putClientProperty(STUDENT_PLANNED_ACTIVITY, studentPlannedActivity);
		startDatePicker.putClientProperty(END_DATE_PICKER , endDatePicker);
		if( studentPlannedActivity.getStartDate() != null ) {
			java.util.Date date = 
		        new java.util.Date(studentPlannedActivity.getStartDate().getTime());
			startDatePicker.setDate(date);
		}
		
		//info
		
	
		
		JXPanel infoPanel = new JXPanel(new HorizontalLayout(1));
	
		
		endDatePicker.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				JXDatePicker endDatePicker = (JXDatePicker) e.getSource();
				String activityName = (String) endDatePicker.getClientProperty(ACTIVITY_NAME);
				JXTaskPane lasTaskPane = (JXTaskPane)endDatePicker.getClientProperty(TASKPANE);
				
				Map<String, Date> endDateMap = (Map<String, Date>) lasTaskPane.getClientProperty(END_DATE_MAP);
				Map<String, Date> startDateMap = (Map<String, Date>) lasTaskPane.getClientProperty(START_DATE_MAP);
				
				endDateMap.put(activityName, endDatePicker.getDate());
				lasTaskPane.putClientProperty(END_DATE_MAP, endDateMap);
				
				//System.out.println("client "+ activityName);
				String title = lasTaskPane.getTitle();
				
				
				final StudentPlannedActivity stp = (StudentPlannedActivity) endDatePicker.getClientProperty(STUDENT_PLANNED_ACTIVITY);
				
				stp.setEndDate(new java.sql.Date(endDatePicker.getDate().getTime()));
				
				modTaskPaneTitleDateRange(taskpane, startDateMap, endDateMap);
				

				SwingUtilities.invokeLater( new Runnable() {
					
					@Override
					public void run() {
						studentPlanningController.saveStudentActivity(stp);						
					}
				});
				
			}

	
		});
		
		startDatePicker.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				JXDatePicker startDatePicker = (JXDatePicker) e.getSource();
				JXDatePicker endDatePicker = (JXDatePicker) startDatePicker.getClientProperty(END_DATE_PICKER);
			
				endDatePicker.setEditable(true);
				
				String activityName = (String) startDatePicker.getClientProperty(ACTIVITY_NAME);
				JXTaskPane lasTaskPane = (JXTaskPane)startDatePicker.getClientProperty(TASKPANE);
				
				Map<String, Date> endDateMap = (Map<String, Date>) lasTaskPane.getClientProperty(END_DATE_MAP);
				Map<String, Date> startDateMap = (Map<String, Date>) lasTaskPane.getClientProperty(START_DATE_MAP);
				
				startDateMap.put(activityName, startDatePicker.getDate());
				lasTaskPane.putClientProperty(START_DATE_MAP, startDateMap);
				
				
			
				final StudentPlannedActivity stp = (StudentPlannedActivity) startDatePicker.getClientProperty(STUDENT_PLANNED_ACTIVITY);
				
				stp.setStartDate(new java.sql.Date(startDatePicker.getDate().getTime()));
				
				modTaskPaneTitleDateRange(taskpane, startDateMap, endDateMap);
				
				
				SwingUtilities.invokeLater( new Runnable() {
					
					@Override
					public void run() {
						studentPlanningController.saveStudentActivity(stp);						
					}
				});
				
				
			}

	
		});
		
		Action infoAction = new AbstractAction("info") {
			    
			
			    public void actionPerformed(ActionEvent e) {
			    	String msg = "<html>description of the activity</html>";

			        JOptionPane optionPane = new JOptionPane();
			        optionPane.setMessage(msg);
			        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
			        JDialog dialog = optionPane.createDialog(null, "Width 100");
			        dialog.setVisible(true);
			    }
			};
		
		//infoPanel.add(dateLink);
		
		JXLabel startLabel = new JXLabel("Start Date:");
		JXLabel endLabel = new JXLabel("End Date:");
		
		infoPanel.add(startLabel);
		infoPanel.add(startDatePicker);
		infoPanel.add(endLabel);
		infoPanel.add(endDatePicker);
		infoPanel.setOpaque(false);
		//infoPanel.setAlpha(0.0f);
		
		//activityPanel.setRightDecoration(infoLink);
		//activityPanel.setTitlePainter(getActivitTitlePainter());
		activityPanel.setBackground(Colors.Black.color());
		activityPanel.setLayout(new VerticalLayout(0));
		activityPanel.setBorder(new  LineBorder(Colors.Black.color()));
		
		
		JXPanel innerMainPanel = new JXPanel();
		
		innerMainPanel.setOpaque(true);
		innerMainPanel.setLayout(new MigLayout("insets 0 3 4 3"));
	
		JXPanel membersPanel = createMembersPanel(activityPanel, studentPlannedActivity);
		
		JXPanel notePanel = createNotePanel(studentPlannedActivity);
		
		innerMainPanel.add(infoPanel,"span, wrap, growx");
        innerMainPanel.add(membersPanel,"span, wrap, growx");
		innerMainPanel.add(notePanel, "span, wrap, growx");
		
		activityPanel.add(innerMainPanel);
		
		return activityPanel;
	}

	/**
	 * @param studentPlannedActivity 
	 * @return
	 */
	protected JXPanel createNotePanel(StudentPlannedActivity studentPlannedActivity) {
		JXPanel noteTextPanel = new JXPanel();
		noteTextPanel.setLayout(new BorderLayout(0,0));
		noteTextPanel.setOpaque(false);
		
		JTextArea noteTextArea = new JTextArea();
		
		if( studentPlannedActivity.getNote() == null || StringUtils.stripToNull(studentPlannedActivity.getNote()) == null ) {
			noteTextArea.setText("Notes...");
		} else {
			noteTextArea.setText(studentPlannedActivity.getNote());
		}
		
		noteTextArea.setLineWrap(true);
		noteTextArea.setRows(5);
		noteTextArea.setWrapStyleWord(true);
		noteTextArea.setToolTipText("What do you need to do the task?\nHow do you that this is completed?");
        noteTextArea.putClientProperty(STUDENT_PLANNED_ACTIVITY, studentPlannedActivity);
        noteTextArea.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				JTextArea textArea = (JTextArea) e.getSource();
				
				final StudentPlannedActivity stp = (StudentPlannedActivity) textArea.getClientProperty(STUDENT_PLANNED_ACTIVITY);
				
				stp.setNote(textArea.getText());
				

				SwingUtilities.invokeLater( new Runnable() {
					
					@Override
					public void run() {
						studentPlanningController.saveStudentActivity(stp);						
					}
				});
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        JScrollPane eloScroll = new JScrollPane(noteTextArea);
        
        
        noteTextPanel.add(eloScroll,BorderLayout.CENTER);
        //notesPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Resources")));
        
		return noteTextPanel;
	}

	/**
	 * @param activityPanel
	 * @param studentPlannedActivity 
	 * @return
	 */
	protected JXPanel createMembersPanel(JXTitledPanel activityPanel, StudentPlannedActivity studentPlannedActivity) {
		JXPanel membersPanel = new JXPanel(new HorizontalLayout(1));
		//membersPanel.add(new JXLabel("bob"));
		membersPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Collaboration Partners (Drag Here)")));
		membersPanel.setOpaque(false);
		membersPanel.setBackground(Color.CYAN);
		membersPanel.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width/2, 100));
		membersPanel.setDropTarget(new DropTarget(membersPanel, new JXDropTargetListener(membersPanel)));
		membersPanel.setToolTipText("Drag the persons that will work with the task from the desktop here.");
		membersPanel.putClientProperty(STUDENT_PLANNED_ACTIVITY, studentPlannedActivity);
		return membersPanel;
	}
	

	private void modTaskPaneTitleDateRange(JXTaskPane taskpane, Map<String, Date> startDateMap, Map<String, Date> endDateMap ) {
		
		final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
		
		List<Date> endDateSort = sortDates( endDateMap );
		List<Date> startDateSort = sortDates( startDateMap );
		
		String startDateString = " ";
		String endDateString = " ";
		
		if( !endDateSort.isEmpty() ) {
			Date endDate = endDateSort.get(endDateSort.size()-1);
			endDateString = formatter.format(endDate);
		}
		if( !startDateSort.isEmpty() ) {
			Date startDate = startDateSort.get(0);
			startDateString = formatter.format(startDate);
		}
		
		
//		Date lastDate = null;
//		if( endDateSort.size()-1 != 0)
//			lastDate = endDateSort.get(endDateSort.size()-1);
//		
//		
//		
		
		
		int indexOf = taskpane.getTitle().indexOf("-");
		if( indexOf == -1 ) {
			if( StringUtils.stripToNull(endDateString) == null)
				taskpane.setTitle(taskpane.getTitle() + " - " + startDateString);
			else
				taskpane.setTitle(taskpane.getTitle() + " - " + startDateString + " to " + endDateString);
		} else {
			StringBuilder sb = new StringBuilder(taskpane.getTitle());
			if( StringUtils.stripToNull(endDateString) == null)
				sb.replace(indexOf, sb.length(), "- " + startDateString);
			else if( StringUtils.stripToNull(startDateString) == null)
					sb.replace(indexOf, sb.length(), "- " + endDateString);
			else
				sb.replace(indexOf, sb.length(), "- " + startDateString + " to " + endDateString);
			taskpane.setTitle(sb.toString());
		}
		
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
		UIManager.put("TaskPaneContainer.useGradient", Boolean.FALSE);
		UIManager.put("TaskPaneContainer.background", Colors.Gray.color(0.5f));

		// setting taskpane defaults
		UIManager.put("TaskPane.font", new FontUIResource(new Font("Segoe UI",
				Font.BOLD, 13)));
		UIManager.put("TaskPane.titleBackgroundGradientStart", Colors.Black
				.color(0.1f));
		UIManager.put("TaskPane.titleBackgroundGradientEnd", Colors.Blue
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
				color2, color2  });
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
				color1, color1});
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
				color1, color1});
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
				color2, color2});
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
				color1, color1  });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}
	
	
	/**
	 * TEST FOR NOW
	 * @return 
	 */
	public Scenario setupPedagogicalPlan() {

//		 String url = JOptionPane.showInputDialog("Input host (for example localhost)");
//	     String username = JOptionPane.showInputDialog("Enter your freakin username");
//
//		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
//		fb.setServiceInterface(PedagogicalPlanService.class);
//		fb.setServiceUrl("http://" + url
//				+ ":8080/webapp/remoting/pedagogicalPlan-httpinvoker");
//		fb.afterPropertiesSet();
//	    PedagogicalPlanService service = (PedagogicalPlanService) fb.getObject();
		
		
        Scenario scenario = createScenario();
       
        
        
        
        return scenario;
        //assert(scenario.getLearningActivitySpace() != null);
        //assert(learningActivitySpace.getActivities().size() ==2);
        //assert(learningActivitySpace.getProduces().contains(anchorElo));
    }
	
    private Activity createActivity(String name, String description) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        activity.setDescription(description);
        return activity;
    }

    private Scenario createScenario() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("DA SCENARIO");

        LearningActivitySpace planning = new LearningActivitySpaceImpl();
        planning.setName("Planning");
        scenario.setLearningActivitySpace(planning);


        Activity firstActivity = new ActivityImpl();
        firstActivity.setName("Gather in the big hall and listen to your teacher");
        planning.addActivity(firstActivity);

        Activity conceptMappingSession = new  ActivityImpl();
        conceptMappingSession.setName("Concept mapping");
        planning.addActivity(conceptMappingSession);

        AnchorELO conceptMap = new AnchorELOImpl();
        conceptMap.setName("Expected concept map");
        conceptMappingSession.setAnchorELO(conceptMap);

        LearningActivitySpace lastSpace = new LearningActivitySpaceImpl();
        lastSpace.setName("Evaluation");
        conceptMap.setInputTo(lastSpace);

        return scenario;
    }

	public void setStudentPlanningController(StudentPlanningController studentPlanningController) {
		this.studentPlanningController = studentPlanningController;
	}

	public StudentPlanningController getStudentPlanningController() {
		return studentPlanningController;
	}
    
	
	
	
	
}// end class TaskPaneExample1
	
	

