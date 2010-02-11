package eu.scy.tools.planning.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.tools.planning.ui.images.Images;

public class JXBuddyPanel extends JXPanel {
	
	private static final String USER = "USER";
	private List<IAwarenessUser> buddies = new ArrayList<IAwarenessUser>();
	
	/**
     * Creates a new instance of JXPanel
     */
    public JXBuddyPanel() {
    	super();
    }
    
    /**
     * @param isDoubleBuffered
     */
    public JXBuddyPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }
    
    /**
     * @param layout
     */
    public JXBuddyPanel(LayoutManager layout) {
        super(layout);
    }
	
	public void addBuddy(IAwarenessUser user) {
		getBuddies().add(user);
		this.createBuddyIcon(user);
	}
	
	public void removeBuddy(JXLabel buddyLabel) {
		IAwarenessUser user = (IAwarenessUser) buddyLabel.getClientProperty(USER);
		getBuddies().remove(user);
		this.remove(buddyLabel.getParent());
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
	
	private void createBuddyIcon(IAwarenessUser user) {
		
		JXPanel bPanel = new JXPanel(new MigLayout("insets 0 0 0 0, wrap"));
		//bPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JXLabel killLabel = new JXLabel(Images.TinyDelete.getIcon());
		killLabel.setVerticalTextPosition(JXLabel.BOTTOM);
		killLabel.setHorizontalTextPosition(JXLabel.RIGHT);
		//killLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		JXLabel buddyLabel = new JXLabel(Images.Profile.getIcon());
		//buddyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		bPanel.add(killLabel, "align right");
		bPanel.add(buddyLabel);
		killLabel.putClientProperty(USER, user);
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out
						.println("JXBuddyPanel.createBuddyIcon(...).new MouseListener() {...}.mouseClicked()");
				JXLabel buddyLabel = (JXLabel) e.getSource();
				JXBuddyPanel.this.removeBuddy(buddyLabel);
			
				// TODO Auto-generated method stub
				
			}
		});
		buddyLabel.setName(user.getNickName());
		buddyLabel.setText(user.getNickName());
		buddyLabel.setVerticalTextPosition(JXLabel.BOTTOM);
		buddyLabel.setHorizontalTextPosition(JXLabel.CENTER);
		this.add(bPanel);
		this.revalidate();
	}
	

}
