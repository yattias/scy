package eu.scy.tools.planning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.Date;

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

import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.tools.dnd.ImageSelection;
import eu.scy.tools.dnd.JXDropTargetListener;

public class StudentPlanningToolMain {
	
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
				frame.add(doInit());
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
	private Component doInit() {
		
		// tweak with the UI defaults for the taskpane and taskpanecontainer
		changeUIdefaults();

		// create a taskpanecontainer
		JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();
		taskpanecontainer.setOpaque(false);
		taskpanecontainer.setBackgroundPainter(getTaskPanePainter());
		taskpanecontainer.setLayout(new VerticalLayout(5));

		// add the task pane to the taskpanecontainer
		// iterate over the las structure

		// for now hard coded

		LearningActivitySpace las1 = new LearningActivitySpaceImpl();

		las1.setName("Orientation");

		LearningActivitySpace las2 = new LearningActivitySpaceImpl();

		las2.setName("Management");

		LearningActivitySpace las3 = new LearningActivitySpaceImpl();

		las3.setName("Information");
		
		LearningActivitySpace las4 = new LearningActivitySpaceImpl();

		las4.setName("Conceptualisation");
		
		LearningActivitySpace las5 = new LearningActivitySpaceImpl();

		las5.setName("Debate");
		
		
		taskpanecontainer.add(createLASPanel(las1));
		taskpanecontainer.add(createLASPanel(las2));
		taskpanecontainer.add(createLASPanel(las3));
		taskpanecontainer.add(createLASPanel(las4));
		taskpanecontainer.add(createLASPanel(las5));

		
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
		outerPanel.setBackgroundPainter(getTaskPanePainter());
		//outerPanel.setLayout(new BorderLayout());
		outerPanel.setBorder(new TitledBorder(null, "Student Planner", 0, 0, activityFont, Color.white));
		
		
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
		taskpane.setIcon(Images.One.getIcon(24, 24));

		//cycle through all the activities

		taskpane.add(createActivityPanel(null,"Identity Means", taskpane));
		taskpane.add(createActivityPanel(null,"identify resources", taskpane));
		//taskpane.setBackground(Colors.Black.color(0.5f));
//		// create a label
//		final JXLabel label = new JXLabel();
//		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
//		label.setText("task pane item 1 : a label");
//		// label.setIcon(Images.Folder.getIcon(32, 32));
//		label.setHorizontalAlignment(JXLabel.LEFT);
//		label.setBackgroundPainter(getPainter());
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

	private JXPanel createActivityPanel(Activity activity, String title, final JXTaskPane taskpane) {
		
		final JXDatePicker datePicker = new JXDatePicker(new Date());
		
		final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
	
	    
		datePicker.setFormats(formatter);
		JXTitledPanel activityPanel = new JXTitledPanel(title);
		
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
				
				String title = taskpane.getTitle();
				
				String dateTitle = formatter.format(datePicker.getDate());
				int indexOf = title.indexOf("-");
				if( indexOf == -1 ) {
					taskpane.setTitle(taskpane.getTitle() + " - " + dateTitle + " - " + "(30% completed)");
				} else {
					StringBuilder sb = new StringBuilder(title);
					sb.replace(indexOf, sb.length(), "- " + dateTitle + " - " + "(50% completed)");
					taskpane.setTitle(sb.toString());
				}
				
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
		
		
		JXHyperlink infoLink = new JXHyperlink(infoAction);
		infoLink.setText("Info");
		infoLink.setForeground(Colors.White.color());
		
		
		JXHyperlink dateLink = new JXHyperlink(dateAction);
		dateLink.setText("Dater");
		dateLink.setForeground(Colors.White.color());
	
		
	
		
		
		
		
		//infoPanel.add(new JXButton(infoLink));
		
		//infoPanel.add(dateLink);
		//infoPanel.add(progressBar);
		infoPanel.add(datePicker);
		infoPanel.add(infoLink);
		infoPanel.setOpaque(false);
		//infoPanel.setAlpha(0.0f);
		
		activityPanel.setRightDecoration(infoPanel);
		activityPanel.setTitlePainter(getActivitTitlePainter());
		activityPanel.setBackground(Colors.White.color(0.2f));
		activityPanel.setLayout(new VerticalLayout(0));
		activityPanel.setBorder(new  LineBorder(Colors.Black.color()));
		
		
		JXPanel innerMainPanel = new JXPanel();
		
		innerMainPanel.setOpaque(true);
		innerMainPanel.setLayout(new MigLayout("insets 0 3 4 3"));
		
		//add ELO panel
		JXPanel eloPanel = new JXPanel(new HorizontalLayout(1));
		//eloPanel.add(new JXLabel("bob"));
		eloPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("ELOs")));
		eloPanel.setOpaque(false);
		eloPanel.setBackground(Color.CYAN);
		eloPanel.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width /2, 100));
		eloPanel.setDropTarget(new DropTarget(eloPanel, new JXDropTargetListener(eloPanel)));
		
		JXPanel membersPanel = new JXPanel(new HorizontalLayout(1));
		//membersPanel.add(new JXLabel("bob"));
		membersPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Members")));
		membersPanel.setOpaque(false);
		membersPanel.setBackground(Color.CYAN);
		membersPanel.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width/2, 100));
		membersPanel.setDropTarget(new DropTarget(membersPanel, new JXDropTargetListener(membersPanel)));
		
		JXPanel notesPanel = new JXPanel();
		notesPanel.setLayout(new BorderLayout(0,0));
		//notesPanel.setBorder(new CompoundBorder(new EmptyBorder(new Insets(0, 0, 0, 0)), new TitledBorder("Notes")));
		notesPanel.setOpaque(false);
		//notesPanel.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width, 100));
		
		JTextArea textArea = new JTextArea();
		textArea.setText("Notes...");
        //textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        
        JScrollPane jScrollPane = new JScrollPane(textArea);
        
        
        notesPanel.add(jScrollPane,BorderLayout.CENTER);
        
        Action noteAction = new AbstractAction("Save Note") {
		    
		    
		    public void actionPerformed(ActionEvent e) {
			      System.out.println("saved note");
			      
		    }
		};
        
        JXHyperlink saveNoteLink = new JXHyperlink(noteAction);
		saveNoteLink.setText("Save Note");
		
		
		JXPanel tempPanel = new JXPanel(new FlowLayout(FlowLayout.RIGHT));
		tempPanel.setOpaque(false);
		tempPanel.add(saveNoteLink);
        notesPanel.add(tempPanel, BorderLayout.SOUTH);
		
    	JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(50);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(activityPanel.getMaximumSize().width, progressBar.getPreferredSize().height));
        
        innerMainPanel.add(progressBar, "span, wrap, growx");
		innerMainPanel.add(eloPanel, "span, wrap, growx");
		innerMainPanel.add(membersPanel,"span, wrap, growx");
		innerMainPanel.add(notesPanel,"span, growx");
		
		
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
		UIManager.put("TaskPaneContainer.background", Colors.Gray.color(1.0f));

		// setting taskpane defaults
		UIManager.put("TaskPane.font", new FontUIResource(new Font("Verdana",
				Font.BOLD, 16)));
		UIManager.put("TaskPane.titleBackgroundGradientStart", Colors.White
				.color());
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
				color1, color2  });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}

	/** this painter draws a gradient fill */
	public Painter getTaskPanePainter() {
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
}// end class TaskPaneExample1
	
	

