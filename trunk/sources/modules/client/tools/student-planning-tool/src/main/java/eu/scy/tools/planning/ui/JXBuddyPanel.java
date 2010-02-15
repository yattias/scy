package eu.scy.tools.planning.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
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
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.tools.planning.ui.images.Images;

public class JXBuddyPanel extends JXPanel {
	
	private static final String KILL_LABEL = "KILL_LABEL";
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
				
				JXLabel killLabel = (JXLabel) bp.getClientProperty("KILL_LABEL");
				
				
				
				bp.setBackgroundPainter(getBuddyPanelPainterOFF());
				
				killLabel.setVisible(false);
//				bp.setBackground(Colors.Gray.color(0.5f));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				JXPanel bp = (JXPanel) e.getSource();
				
				bp.setBackgroundPainter(getBuddyPanelPainterON());
				JXLabel killLabel = (JXLabel) bp.getClientProperty("KILL_LABEL");
				
				killLabel.setVisible(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		//bPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JXLabel killLabel = new JXLabel(Images.TinyDelete.getIcon());
		killLabel.setVerticalTextPosition(JXLabel.BOTTOM);
		killLabel.setHorizontalTextPosition(JXLabel.RIGHT);
		killLabel.setOpaque(false);
		//killLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		JXLabel buddyLabel = new JXLabel(Images.Profile.getIcon());
		//buddyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		bPanel.add(killLabel, "align right");
		killLabel.setVisible(false);
		bPanel.add(buddyLabel);
		killLabel.putClientProperty(USER, user);
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
				parent.setBackgroundPainter(getBuddyPanelPainterOFF());
				killLabel.setVisible(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				JXLabel killLabel = (JXLabel) e.getSource();
				killLabel.setVisible(true);
				JXPanel parent = (JXPanel) killLabel.getParent();
				parent.setBackgroundPainter(getBuddyPanelPainterON());
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out
						.println("JXBuddyPanel.createBuddyIcon(...).new MouseListener() {...}.mouseClicked()");
				JXLabel killLabel = (JXLabel) e.getSource();
				JXBuddyPanel.this.removeBuddy(killLabel);
			
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
	
	public Painter getBuddyPanelPainterON() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.8f);
		Color color2 = Colors.Yellow.color(0.5f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
						color2, color1 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}
	
	public Painter getBuddyPanelPainterOFF() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.2f);
		Color color2 = Colors.Black.color(0.8f);
		Color color3 = Colors.Gray.color(0.0f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1.0f }, new Color[] {
				color3, color3 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}
	

}
