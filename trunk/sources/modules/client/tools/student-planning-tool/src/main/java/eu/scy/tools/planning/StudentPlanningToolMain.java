package eu.scy.tools.planning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;

import net.miginfocom.swing.MigLayout;

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

import eu.scy.core.model.impl.pedagogicalplan.ActivityImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.tools.dnd.ImageSelection;
import eu.scy.tools.dnd.JXDropTargetListener;

public class StudentPlanningToolMain {
	
	private static final String TASKPANE = "TASKPANE";

	private static final String ACTIVITY_NAME = "ACTIVITY_NAME";

	private static final Object DATE_MAP = "DATE_MAP";

	Font activityFont = new Font("Segoe UI", Font.BOLD, 11);

	
	

	/**
	 * creates a JFrame and calls {@link #doInit} to create a JXPanel and adds
	 * the panel to this frame.
	 */
	public StudentPlanningToolMain() {
		
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
		

	}
	
	public void launchInFrame() {
		
		JFrame frame = new JFrame("Launcher");

		// add the panel to this frame
		
		
		JXButton openFrame = new JXButton("launchy");
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

	/** creates a JXLabel and attaches a painter to it. */
	public JComponent createStudentPlanningPanel(Scenario s) {
		
		Scenario scenario = setupPedagogicalPlan();
		
		
		// tweak with the UI defaults for the taskpane and taskpanecontainer
		changeUIdefaults();

		// create a taskpanecontainer
		JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();
		taskpanecontainer.setOpaque(true);
		taskpanecontainer.setBackgroundPainter(getTaskPaneTitlePainter());
		taskpanecontainer.setLayout(new VerticalLayout(5));

		// add the task pane to the taskpanecontainer
		// iterate over the las structure

		// for now hard coded

		LearningActivitySpace firstSpace = scenario.getLearningActivitySpace();

        List activities = firstSpace.getActivities();		
		
        
		taskpanecontainer.add(createLASPanel(firstSpace));
	
		
		// set the transparency of the JXPanel to 50% transparent
		// panel.setAlpha(0.7f);

		JScrollPane scrollPane = new JScrollPane(taskpanecontainer,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		panel.add(taskpanecontainer, BorderLayout.CENTER);
//		panel.setPreferredSize(new Dimension(500, 600));
		scrollPane.setPreferredSize(new Dimension(500, 600));
		scrollPane.setOpaque(false);
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		JXPanel outerPanel = new JXPanel(new HorizontalLayout(1));
		//JXPanel panel = new JXPanel();
		outerPanel.setBackgroundPainter(getTaskPaneTitlePainter());
		//outerPanel.setLayout(new BorderLayout());
		outerPanel.setBorder(new TitledBorder(null, "Student Planner", 0, 0, activityFont, Color.black));
		
		
//		scrollPane.getViewport().add(panel);
////		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
////		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		scrollPane.setPreferredSize(new Dimension(500, 600));
		outerPanel.add(scrollPane);
		
		//outerPanel.add(createDragPanel());
		
		
		
		
		return outerPanel;
	}

	private JComponent createDragPanel() {
		JXPanel dragPanel = new JXPanel();
		dragPanel.setBorder(new TitledBorder("Drag Panel buddies and Elos"));
		dragPanel.setPreferredSize(new Dimension(250, 300));
		//dragPanel.setOpaque(false);
		dragPanel.setBackgroundPainter(getDragPainter());
		
		JXLabel elo1Label = new JXLabel(Images.Excel1.getIcon());
		elo1Label.setText("elo 1");
		
		JXLabel elo2Label = new JXLabel(Images.Excel2.getIcon());
		elo2Label.setText("elo 2");
		
		JXLabel member1Label = new JXLabel(Images.Profile.getIcon());
		member1Label.setText("bob");
		
		JXLabel member2Label = new JXLabel(Images.Profile.getIcon());
		member2Label.setText("jack");
		
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


		
		
		
		//dragPanel.addMouseListener(listener);
		dragPanel.add(elo1Label);
		dragPanel.add(elo2Label);
		dragPanel.add(member1Label);
		dragPanel.add(member2Label);
		
//		DragSource dragSource = DragSource.getDefaultDragSource();
//		    dragSource.createDefaultDragGestureRecognizer(bobLabel,DnDConstants.ACTION_MOVE, new JXDragListener());
//		//dragPanel.setDropTarget(new DropTarget(dragPanel, new JXDropTargetListener()));
		return dragPanel;
	}

	private JXTaskPane createLASPanel(LearningActivitySpace las) {

		// create a taskpane, and set it's title and icon
		JXTaskPane taskpane = new JXTaskPane();
		taskpane.setCollapsed(true);
		taskpane.setTitle(las.getName());
		taskpane.setForeground(Color.white);
		taskpane.setBackground(Color.black);
		taskpane.setOpaque(true);
		Map<String, Date> dateMap = new HashMap<String, Date>();
		taskpane.putClientProperty(DATE_MAP, dateMap);
		//taskpane.setIcon(Images.One.getIcon(24, 24));

		//cycle through all the activities
		
		
		List activities = las.getActivities();

        boolean foundAnAnchorElo = false;

        for (int i = 0; i < activities.size(); i++) {
            Activity activity = (Activity) activities.get(i);
            taskpane.add(createActivityPanel(activity, taskpane, i+1));
            AnchorELO anchorELO = activity.getAnchorELO();

            if(anchorELO != null) {
                assert(anchorELO.getInputTo() != null);
                foundAnAnchorElo = true;
            }


        }
		
		
		

		
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

	private JXPanel createActivityPanel(Activity activity, final JXTaskPane taskpane, int activityNumber) {
		
		final JXDatePicker datePicker = new JXDatePicker();
		
		final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
	
	    
		datePicker.setFormats(formatter);
		
	
		
		JXTitledPanel activityPanel = new JXTitledPanel(activityNumber + ". " + activity.getName());
		activityPanel.setTitleFont(activityFont);
		activityPanel.setTitleForeground(Colors.White.color());
		datePicker.putClientProperty(ACTIVITY_NAME, activity.getName());
		datePicker.putClientProperty(TASKPANE, taskpane);
		//info
		
		
		JXPanel infoPanel = new JXPanel(new HorizontalLayout(1));
		
		
		Action dateAction = new AbstractAction("Date") {
		    
		    
		    public void actionPerformed(ActionEvent e) {
			      System.out.println("pop up date");
			      datePicker.setEnabled(true);
			      
		    }
		};
		
		datePicker.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JXDatePicker source = (JXDatePicker) e.getSource();
				String activityName = (String) source.getClientProperty(ACTIVITY_NAME);
				JXTaskPane lasTaskPane = (JXTaskPane)source.getClientProperty(TASKPANE);
				
				Map<String, Date> dateMap = (Map<String, Date>) lasTaskPane.getClientProperty(DATE_MAP);
				
				//System.out.println("client "+ activityName);
				String title = lasTaskPane.getTitle();
				
				dateMap.put(activityName, datePicker.getDate());
				
				
				lasTaskPane.putClientProperty(DATE_MAP, dateMap);
				
				List<Date> sortedDates = sortDates( dateMap );
				
				Date firstDate = sortedDates.get(0);
				Date lastDate = null;
				if( sortedDates.size()-1 != 0)
					lastDate = sortedDates.get(sortedDates.size()-1);
				
				String firstDateString = formatter.format(firstDate);
				String lastDateString = null;
				if( lastDate != null) {
					lastDateString = formatter.format(lastDate);
				}
				
				modTaskPaneTitle(taskpane, firstDateString, lastDateString);
				
				
				
				
				
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
		
		
//		JXHyperlink infoLink = new JXHyperlink(infoAction);
//		infoLink.setText("Info");
//		infoLink.setForeground(Colors.White.color());
		
		
		JXHyperlink dateLink = new JXHyperlink(dateAction);
		dateLink.setText("Dater");
		dateLink.setForeground(Colors.White.color());
	
		
	
		
		
		
		
		//infoPanel.add(new JXButton(infoLink));
		
		//infoPanel.add(dateLink);
		//infoPanel.add(progressBar);
		infoPanel.add(datePicker);
		//infoPanel.add(infoLink);
		infoPanel.setOpaque(false);
		//infoPanel.setAlpha(0.0f);
		
		activityPanel.setRightDecoration(infoPanel);
		//activityPanel.setTitlePainter(getActivitTitlePainter());
		activityPanel.setBackground(Colors.Black.color());
		activityPanel.setLayout(new VerticalLayout(0));
		activityPanel.setBorder(new  LineBorder(Colors.Black.color()));
		
		
		JXPanel innerMainPanel = new JXPanel();
		
		innerMainPanel.setOpaque(true);
		innerMainPanel.setLayout(new MigLayout("insets 0 3 4 3"));
		
		//add ELO panel
//		JXPanel eloPanel = new JXPanel(new HorizontalLayout(1));
//		//eloPanel.add(new JXLabel("bob"));
//		eloPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Planned ELOs")));
//		eloPanel.setOpaque(false);
//		eloPanel.setBackground(Color.CYAN);
//		eloPanel.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width /2, 100));
//		eloPanel.setDropTarget(new DropTarget(eloPanel, new JXDropTargetListener(eloPanel)));
//		
		JXPanel membersPanel = new JXPanel(new HorizontalLayout(1));
		//membersPanel.add(new JXLabel("bob"));
		membersPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Collaboration Partners (Drag Here)")));
		membersPanel.setOpaque(false);
		membersPanel.setBackground(Color.CYAN);
		membersPanel.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width/2, 100));
		membersPanel.setDropTarget(new DropTarget(membersPanel, new JXDropTargetListener(membersPanel)));
		
		JXPanel resourcesPanel = new JXPanel();
		resourcesPanel.setLayout(new BorderLayout(0,0));
		//notesPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Notes")));
		resourcesPanel.setOpaque(false);
		//notesPanel.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width, 100));
		
		JTextArea textArea = new JTextArea();
		textArea.setText("Planned Resources...");
        //textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        
        JScrollPane jScrollPane = new JScrollPane(textArea);
        
        
        resourcesPanel.add(jScrollPane,BorderLayout.CENTER);
        //notesPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Resources")));
        
        Action saveResourceAction = new AbstractAction("Save Resource Plan") {
		    
		    
		    public void actionPerformed(ActionEvent e) {
			      System.out.println("saved resource plan");
			      
		    }
		};
        
        JXHyperlink saveNoteLink = new JXHyperlink(saveResourceAction);
		saveNoteLink.setText("Save Resource Plan");
		
		
		JXPanel resourceTempPanel = new JXPanel(new FlowLayout(FlowLayout.RIGHT));
		resourceTempPanel.setOpaque(false);
		resourceTempPanel.add(saveNoteLink);
        resourcesPanel.add(resourceTempPanel, BorderLayout.SOUTH);
		
		JXPanel eloPanel = new JXPanel();
		eloPanel.setLayout(new BorderLayout(0,0));
		eloPanel.setOpaque(false);
		
		JTextArea eloTextArea = new JTextArea();
		eloTextArea.setText("Planned ELOs...");
		eloTextArea.setLineWrap(true);
		eloTextArea.setRows(5);
		eloTextArea.setWrapStyleWord(true);
        
        JScrollPane eloScroll = new JScrollPane(eloTextArea);
        
        
        eloPanel.add(eloScroll,BorderLayout.CENTER);
        //notesPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Resources")));
        
        Action eloAction = new AbstractAction("Save ELO Plan") {
		    
		    
		    public void actionPerformed(ActionEvent e) {
			      System.out.println("saved elo plan");
		    }
		};
        
        JXHyperlink eloLink = new JXHyperlink(eloAction);
        eloLink.setText("Save ELO PLan");
		
		
		JXPanel eloTempPanel = new JXPanel(new FlowLayout(FlowLayout.RIGHT));
		eloTempPanel.setOpaque(false);
		eloTempPanel.add(eloLink);
		eloPanel.add(eloTempPanel, BorderLayout.SOUTH);
        
        
    	JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(50);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width, progressBar.getPreferredSize().height));
        
        innerMainPanel.add(progressBar, "span, wrap, growx");
        innerMainPanel.add(membersPanel,"span, wrap, growx");
		innerMainPanel.add(eloPanel, "span, wrap, growx");
		innerMainPanel.add(resourcesPanel,"span, growx");
		
		
		activityPanel.add(innerMainPanel);

		
//		//activity label
//		final JXLabel activityLabel = new JXLabel("Identify Means");
//		activityLabel.setFont(activityFont);
//		activityLabel.setText();
//		// label.setIcon(Images.Folder.getIcon(32, 32));
//		activityLabel.setHorizontalAlignment(JXLabel.LEFT);
//		
//		innerPanel.add(activityLabel, BorderLayout.WEST);
//		
//		JXButton infoButton = new JXButton("Info");
//		infoButton.setFont(activityFont);
//		innerPanel.add(infoButton, BorderLayout.CENTER);
//
//		JXButton dateButton = new JXButton("Date");
//		dateButton.setFont(activityFont);
//		innerPanel.add(dateButton, BorderLayout.EAST);
//
//		activityPanel.add(innerPanel);
		
		
		//activityPanel.setBackgroundPainter(getPainter());
		
		return activityPanel;
	}
	

	private void modTaskPaneTitle(JXTaskPane taskpane, String firstDateString, String lastDateString ) {
		int indexOf = taskpane.getTitle().indexOf("-");
		if( indexOf == -1 ) {
			if( lastDateString == null)
				taskpane.setTitle(taskpane.getTitle() + " - " + firstDateString);
			else
				taskpane.setTitle(taskpane.getTitle() + " - " + firstDateString + " to " + lastDateString);
		} else {
			StringBuilder sb = new StringBuilder(taskpane.getTitle());
			if( lastDateString == null)
				sb.replace(indexOf, sb.length(), "- " + firstDateString);
			else
				sb.replace(indexOf, sb.length(), "- " + firstDateString + " to " + lastDateString);
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
    
	
	
	
	
}// end class TaskPaneExample1
	
	

