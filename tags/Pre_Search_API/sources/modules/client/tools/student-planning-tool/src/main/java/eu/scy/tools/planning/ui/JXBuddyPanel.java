package eu.scy.tools.planning.ui;

import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.tools.planning.Messages;
import eu.scy.tools.planning.controller.StudentPlanningController;
import eu.scy.tools.planning.ui.images.Images;

public class JXBuddyPanel extends JXPanel {
	
	private static final String KILL_LABEL = "KILL_LABEL"; //$NON-NLS-1$
	private static final String USER = "USER"; //$NON-NLS-1$
	private List<IAwarenessUser> buddies = new ArrayList<IAwarenessUser>();
	private JXLabel messageLabel;
	private StudentPlanningController studentPlanningController;
	private StudentPlannedActivity studentPlannedActivity;
	
	/**
     * Creates a new instance of JXPanel
     */
    public JXBuddyPanel(StudentPlanningController studentPlanningController) {
    	super();
    	this.studentPlanningController = studentPlanningController;
    }
    
    /**
     * @param isDoubleBuffered
     */
    public JXBuddyPanel(boolean isDoubleBuffered, StudentPlanningController studentPlanningController) {
        super(isDoubleBuffered);
        this.studentPlanningController = studentPlanningController;
    }
    
    /**
     * @param layout
     */
    public JXBuddyPanel(LayoutManager layout,StudentPlanningController studentPlanningController) {
        super(layout);
        this.studentPlanningController = studentPlanningController;
    }
	
	public void addBuddy(IAwarenessUser user) {
        // System.out.println("ADDING BUDDY : " + user.getNickName());
		getBuddies().add(user);
		this.createBuddyIcon(user);
	}
	
	public void removeBuddy(JXLabel buddyLabel) {
		IAwarenessUser user = (IAwarenessUser) buddyLabel.getClientProperty(USER);
		
		studentPlanningController.removeMemberStudentPlannedActivity(this.studentPlannedActivity, user.getNickName());
		getBuddies().remove(user);
		this.remove(buddyLabel.getParent());
		getMessageLabel().setText(Messages.getString("JXBuddyPanel.2")); //$NON-NLS-1$
		
		this.validate();
		this.revalidate();
		this.repaint();
	}

	public void setBuddies(List<IAwarenessUser> buddies) {
		this.buddies = buddies;
	}

	public List<IAwarenessUser> getBuddies() {
		return buddies;
	}
	
	private void createBuddyIcon(final IAwarenessUser user) {
		
		JXPanel bPanel = new JXPanel(new MigLayout("insets 0 0 0 0, wrap")); //$NON-NLS-1$
		bPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				JXPanel bp = (JXPanel) e.getSource();
				
				JXLabel killLabel = (JXLabel) bp.getClientProperty("KILL_LABEL"); //$NON-NLS-1$
				
				
				
				bp.setBackgroundPainter(Colors.getHighlightOffPainter());
				
				killLabel.setVisible(false);
//				bp.setBackground(Colors.Gray.color(0.5f));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				JXPanel bp = (JXPanel) e.getSource();
				
				bp.setBackgroundPainter(Colors.getHighlightOnPainterBuddy());
				JXLabel killLabel = (JXLabel) bp.getClientProperty("KILL_LABEL"); //$NON-NLS-1$
				
				killLabel.setVisible(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		//bPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JXLabel killLabel = new JXLabel(Images.FDelete.getIcon());
		killLabel.setVerticalTextPosition(JXLabel.BOTTOM);
		killLabel.setHorizontalTextPosition(JXLabel.RIGHT);
		killLabel.setOpaque(false);
		//killLabel.setBorder(BorderFactory.createLineBorder(Color.black));
			
		
		final JXLabel buddyLabel = new JXLabel();
		
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    Icon buddyImageIcon = studentPlanningController.getBuddyImageIcon(user.getNickName());
		      // Here, we can safely update the GUI
		      // because we'll be called from the
		      // event dispatch thread
		    
		    	buddyLabel.setIcon(buddyImageIcon);
		    }
		  });
		
		
		//buddyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		bPanel.add(killLabel, "align right"); //$NON-NLS-1$
		killLabel.setVisible(false);
		bPanel.add(buddyLabel);
		killLabel.putClientProperty(USER, user);
		killLabel.setToolTipText(Messages.getString("JXBuddyPanel.7")); //$NON-NLS-1$
		bPanel.putClientProperty(KILL_LABEL, killLabel);
		killLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				JXLabel killLabel = (JXLabel) e.getSource();
				JXPanel parent = (JXPanel) killLabel.getParent();
				parent.setBackgroundPainter(Colors.getHighlightOffPainter());
				killLabel.setVisible(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				JXLabel killLabel = (JXLabel) e.getSource();
				killLabel.setVisible(true);
				JXPanel parent = (JXPanel) killLabel.getParent();
				parent.setBackgroundPainter(Colors.getHighlightOnPainterBuddy());
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out
						.println("JXBuddyPanel.createBuddyIcon(...).new MouseListener() {...}.mouseClicked()"); //$NON-NLS-1$
				
			
				Object[] options = {Messages.getString("JXBuddyPanel.9"), //$NON-NLS-1$
                Messages.getString("JXBuddyPanel.10")}; //$NON-NLS-1$
				int n = JOptionPane
				.showOptionDialog(null,
						Messages.getString("JXBuddyPanel.11"), Messages.getString("JXBuddyPanel.12"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options,
						options[1]);
		
				if( n == 0) {
			
					JXLabel killLabel = (JXLabel) e.getSource();
					JXBuddyPanel.this.removeBuddy(killLabel);
			
				}
				// TODO Auto-generated method stub
				
			}
		});
		
		String nickName = user.getNickName();
		if( nickName == null )
			nickName = "no name"; //$NON-NLS-1$
		
		buddyLabel.setToolTipText(nickName);
		buddyLabel.setName(nickName);
		buddyLabel.setText(nickName);
		buddyLabel.setVerticalTextPosition(JXLabel.BOTTOM);
		buddyLabel.setHorizontalTextPosition(JXLabel.CENTER);
		buddyLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {				
				JXLabel l = (JXLabel) e.getSource();
				l.setBackgroundPainter(Colors.getHighlightOffPainter());
				
				JXPanel bp = (JXPanel) l.getParent();
				
				bp.setBackgroundPainter(Colors.getHighlightOffPainter());
				JXLabel killLabel = (JXLabel) bp.getClientProperty("KILL_LABEL"); //$NON-NLS-1$
				
				killLabel.setVisible(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				JXLabel l = (JXLabel) e.getSource();
				l.setBackgroundPainter(Colors.getHighlightOnPainterBuddy());
				
				JXPanel bp = (JXPanel) l.getParent();
				
				bp.setBackgroundPainter(Colors.getHighlightOnPainterBuddy());
				JXLabel killLabel = (JXLabel) bp.getClientProperty("KILL_LABEL"); //$NON-NLS-1$
				
				killLabel.setVisible(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		this.add(bPanel);
		this.revalidate();
	}

	public void setMessageLabel(JXLabel messageLabel) {
		this.messageLabel = messageLabel;
	}

	public JXLabel getMessageLabel() {
		return messageLabel;
	}

	public void setStudentPlanningController(StudentPlanningController studentPlanningController) {
		this.studentPlanningController = studentPlanningController;
	}

	public StudentPlanningController getStudentPlanningController() {
		return studentPlanningController;
	}

	public void setStudentPlannedActivity(StudentPlannedActivity studentPlannedActivity) {
		this.studentPlannedActivity = studentPlannedActivity;
	}

	public StudentPlannedActivity getStudentPlannedActivity() {
		return studentPlannedActivity;
	}
	

}
