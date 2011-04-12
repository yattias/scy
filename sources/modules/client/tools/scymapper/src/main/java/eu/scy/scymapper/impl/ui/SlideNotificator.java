package eu.scy.scymapper.impl.ui;

/**
 * @author bjoerge
 * @created 07.jan.2010 13:42:26
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class SlideNotificator implements Notificator {

	protected static final float ANIMATION_TIME = 500;
	protected static final int ANIMATION_DELAY = 10;

	private Roller roller;
	private int startY;
	private Timer animationTimer;
	private long animationStart;
	private Container ownerComponent;

	private JWindow window;

	public SlideNotificator(JComponent ownerComponent, JComponent contents) {
		//Frame f = JOptionPane.getFrameForComponent(ownerComponent);
//		if (!(f instanceof JFrame))
//			throw new IllegalArgumentException("The ownerComponent component must be contained in a JFrame");
//		ownerFrame = f;
		   Container c = ownerComponent.getParent();
           while (c.getParent() != null) {
               c = c.getParent();
           }
		this.ownerComponent = c;
		System.out.println("Owner Component: "+ownerComponent.getClass());
		setContents(contents);
	}

	private Point getLocationForWindow() {

		Point loc = new Point(ownerComponent.getLocationOnScreen());
//		loc.x = loc.x + ownerComponent.getWidth();
//		loc.y = loc.y + ownerComponent.getHeight();

		loc.x += ownerComponent.getWidth() - window.getWidth() - 10;
		loc.y += ownerComponent.getHeight() - window.getHeight() - 10;
		return loc;
	}

	public void setContents(JComponent contents) {
		Dimension componentSize = new Dimension(contents.getWidth(), contents.getWidth());

		window = new JWindow();
		window.setSize(componentSize);
		window.setLocation(getLocationForWindow());
		window.setContentPane(contents);
	}

	public void show() {
		roller = new Roller(makeOffscreenImage(window));
		startY = window.getY() + window.getHeight();

		ActionListener animation = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long elapsed = System.currentTimeMillis() - animationStart;
				if (elapsed > ANIMATION_TIME) {
					animationTimer.stop();
					animationTimer = null;
					window.setLocation(getLocationForWindow());
					window.setVisible(true);
					roller.setVisible(false);
					roller.dispose();
//					ownerFrame.addComponentListener(new ComponentAdapter() {
//						@Override
//						public void componentResized(ComponentEvent e) {
//							window.setLocation(getLocationForWindow());
//						}
//
//						@Override
//						public void componentMoved(ComponentEvent e) {
//							window.setLocation(getLocationForWindow());
//						}
//					});
//					ownerFrame.addWindowStateListener(new WindowAdapter() {
//						@Override
//						public void windowStateChanged(WindowEvent e) {
//							window.setLocation(getLocationForWindow());
//						}
//					});
				} else {
					float progress = (float) elapsed / ANIMATION_TIME;
					int h = (int) (progress * window.getHeight());
					h = Math.max(h, 1);
					int y = startY - h;
					roller.setBounds(window.getX(), y, window.getWidth(), h);
					roller.repaint();
				}
			}
		};
		animationTimer = new javax.swing.Timer(ANIMATION_DELAY, animation);
		animationStart = System.currentTimeMillis();
		roller.setBounds(window.getX(), window.getY() + window.getHeight(), window.getWidth(), 1);
		roller.setVisible(true);
		animationTimer.start();

	}

	public void hide() {
		if (!window.isVisible()) return;

		ActionListener animation = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long elapsed = System.currentTimeMillis() - animationStart;
				if (elapsed > ANIMATION_TIME) {
					animationTimer.stop();
					animationTimer = null;
					window.setVisible(false);
					roller.setVisible(false);
					roller.dispose();
				} else {
					float progress = (float) elapsed / ANIMATION_TIME;
					int diff = (int) (progress * window.getHeight());

					roller.setBounds(window.getX(), window.getY() + diff, window.getWidth(), window.getHeight() - diff);
					roller.repaint();
				}
			}
		};
		animationTimer = new Timer(ANIMATION_DELAY, animation);
		animationStart = System.currentTimeMillis();
		roller.setBounds(window.getX(), window.getY(), window.getWidth(), window.getHeight());
		roller.setVisible(true);
		window.setVisible(false);
		animationTimer.start();
	}

	private BufferedImage makeOffscreenImage(JWindow w) {
		Point tmpLoc = new Point(w.getLocation());
		w.setLocation(-w.getWidth(), -w.getHeight());
		w.setVisible(true);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gfxConfig = ge.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage offscreenImage = gfxConfig.createCompatibleImage(w.getWidth(), w.getHeight());
		Graphics2D offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
		// windows workaround
		offscreenGraphics.setColor(w.getBackground());
		offscreenGraphics.fillRect(0, 0, w.getWidth(), w.getHeight());
		// paint from source to offscreen buffer
		w.paint(offscreenGraphics);
		w.setVisible(false);
		w.setLocation(tmpLoc);
		return offscreenImage;
	}

	public boolean isVisible() {
		return window.isVisible();
	}

	class Roller extends JWindow {
		private BufferedImage offscreenImage;

		Roller(BufferedImage offscreenImage) {
			this.offscreenImage = offscreenImage;
			setSize(offscreenImage.getWidth(), offscreenImage.getHeight());
		}

		public void update(Graphics g) {
			paint(g);
		}

		public void paint(Graphics g) {
			BufferedImage fragment = offscreenImage.getSubimage(0, 0, getWidth(), getHeight());
			g.drawImage(fragment, 0, 0, this);
		}
	}
}
