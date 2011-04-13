package eu.scy.scymapper.impl.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.Direction;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.SplineInterpolator;
import org.apache.log4j.Logger;

public class FadeNotificator implements ComponentListener, TimingTarget, Notificator {

    private final static Logger logger = Logger.getLogger(FadeNotificator.class);

    private int duration = 400;

    public enum Position {
        TOP,
        BOTTOM,
        EAST,
        WEST,
        LOWER_RIGHT_CORNER,
        LOWER_LEFT_CORNER,
        UPPER_RIGHT_CORNER,
        UPPER_LEFT_CORNER;
    }

    private JComponent parent;

    private JComponent content;

    private Position position;

    private Animator animator;

    private JPanel glassPane;

    private int contentWidth;

    private int contentHeight;

    private int xOffset;

    private int yOffset;

    private boolean visible;

    private boolean setToInvisible;

    private boolean borderPainted;

    public FadeNotificator(JComponent parent, JComponent content, Position position, int xOffset, int yOffset) {
    	this.xOffset = xOffset;
    	this.yOffset = yOffset;
        this.parent = parent;
        this.content = content;
        this.position = position;
        this.contentWidth = content.getWidth();
        this.contentHeight = content.getHeight();
        this.visible = false;
        this.borderPainted = true;
        content.setVisible(visible);
        animator = new Animator(duration, this);
        animator.setInterpolator(new SplineInterpolator(0.4f, 0.0f, 1f, 0.6f));
        Point contentPoint = calculatePosition();
        content.setBounds(contentPoint.x, contentPoint.y, content.getWidth(), content.getHeight());
        parent.addComponentListener(this);
        JRootPane parentsRootPane = SwingUtilities.getRootPane(parent);
        glassPane = new JPanel() {

            protected void paintChildren(java.awt.Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                Component[] components = getComponents();
                for (Component component : components) {
                    if (component.isVisible() && borderPainted) {
                        Rectangle bounds = component.getBounds();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                        int sw = 10 * 2;
                        for (int i = sw; i >= 2; i -= 2) {
                            float pct = (float) (sw - i) / (sw - 1);
                            g2d.setColor(getMixedColor(Color.DARK_GRAY, pct, Color.WHITE, 1.0f - pct));
                            g2d.setStroke(new BasicStroke(i));
                            g2d.drawRoundRect(bounds.x + (sw / 4), bounds.y + (sw / 4), bounds.width - (sw / 4), bounds.height - (sw / 4), 2, 2);
                        }
                    }
                }
                g2d.dispose();
                super.paintChildren(g);
            };

            public void paint(java.awt.Graphics g) {
                super.paint(g);
            };

            private Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
                float[] clr1 = c1.getComponents(null);
                float[] clr2 = c2.getComponents(null);
                for (int i = 0; i < clr1.length; i++) {
                    clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
                }
                return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
            }

        };
        glassPane.setOpaque(false);
        glassPane.setLayout(null);
        glassPane.add(content);
        parentsRootPane.setGlassPane(glassPane);
        parentsRootPane.getGlassPane().setVisible(true);
    }

    public FadeNotificator(JComponent parent, JComponent content, Position position) {
    	this(parent, content, position, 0, 0);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setDuration(int millis) {
        this.duration = millis;
        animator.setDuration(millis);
    }

    public void setBorderPainted(boolean enabled) {
        this.borderPainted = enabled;
    }

    /**
     * Sets the x-axis offset for the element, to fade in.
     * Values greater then zero move the element to the right,
     * values lesser then zero move the element to the left.
     * A value of zero means, the element is horizontally centered.
     * @param x The x offset in pixel.
     */
    public void setXOffset(int x) {
    	xOffset = x;
    }

    /**
     * Sets the y-axis offset for the element, to fade in.
     * Values greater then zero move the element above,
     * values lesser then zero move the element below.
     * A value of zero means, the element is vertically centered.
     * @param y The offset in pixel.
     */
    public void setYOffset(int y) {
    	yOffset = y;
    }

    /**
     * Current x-axis offset, the element fades in.
     * Values greater then zero move the element above,
     * values lesser then zero move the element below.
     * A value of zero means, the element is vertically centered.
     * @return The current x-offset in pixel.
     */
    public int getXOffset() {
    	return xOffset;
    }

    /**
     * Current y-axis offset, the element fades in.
     * Values greater then zero move the element to the right
     * values lesser then zero move the element left.
     * A value of zero means, the element is vertically centered.
     * @return The current y-offset in pixel.
     */
    public int getYOffset() {
    	return yOffset;
    }

    private Point calculatePosition() {
        Point point = null;
        int x = 0;
        int y = 0;
        switch (position) {
            case TOP:
                x = (parent.getWidth() / 2) - (content.getWidth() / 2);
                y = 0;
                break;
            case BOTTOM:
                x = (parent.getWidth() / 2) - (content.getWidth() / 2);
                y = parent.getHeight() - content.getHeight();
                break;
            case EAST:
                x = (parent.getWidth() - content.getWidth());
                y = (parent.getHeight() / 2) - (content.getHeight() / 2);
                break;
            case WEST:
                x = 0;
                y = (parent.getHeight() / 2) - (content.getHeight() / 2);
                break;
            case LOWER_RIGHT_CORNER:
                x = (parent.getWidth() - content.getWidth());
                y = (parent.getHeight()) - (content.getHeight());
                break;
            case LOWER_LEFT_CORNER:
                x = 0;
                y = parent.getHeight() - content.getHeight();
                break;
            case UPPER_RIGHT_CORNER:
                x = (parent.getWidth() - content.getWidth());
                y = 0;
                break;
            case UPPER_LEFT_CORNER:
                x = 0;
                y = 0;
                break;
            default:
                break;
        }
        x += xOffset;
        y += yOffset;
        point = new Point(x, y);
        return point;
    }

    public void show() {
        if (!visible) {
            try {
            animator.setStartFraction(1f);
            animator.setStartDirection(Direction.BACKWARD);
            visible = true;
            setToInvisible = false;
            animator.start();
            } catch (IllegalStateException ise) {
                logger.debug("You cannot perform the show() operation during an animation");
            }
        }
    }

    public void hide() {
        if (visible) {
            try {
                animator.setStartFraction(0f);
                animator.setStartDirection(Direction.FORWARD);
                setToInvisible = true;
                animator.start();
            } catch (IllegalStateException ise) {
                logger.debug("You cannot perform the hide() operation during an animation");
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Parent Frame");
        JPanel panel = new JPanel();
        JButton label = new JButton("Button as component");
        panel.setLayout(new GridLayout(1, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label.setSize(100, 80);
        frame.setSize(1000, 800);
        frame.setVisible(true);
        final FadeNotificator notification = new FadeNotificator(frame.getRootPane(), label, FadeNotificator.Position.EAST);
        notification.setDuration(300);
        label.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (notification.isVisible()) {
                    notification.hide();

                } else {
                    notification.show();

                }
            }
        });
        JButton showHideButton = new JButton(Localization.getString("Mainframe.FadeNotificiator.Button"));
        showHideButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (notification.isVisible()) {
                    notification.hide();
                } else {
                    notification.show();
                }
            }
        });
        panel.add(showHideButton);
        frame.add(panel, BorderLayout.NORTH);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Point contentPoint = calculatePosition();
        content.setBounds(contentPoint.x, contentPoint.y, content.getWidth(), content.getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void begin() {
        if (visible) {
            content.setVisible(true);
        } else {
            content.setVisible(false);
        }
    }

    @Override
    public void end() {
        if (setToInvisible) {
            visible = false;
            content.setVisible(false);
        } else {
            content.setVisible(true);
        }
    }

    @Override
    public void repeat() {}

    @Override
    public void timingEvent(float fraction) {
        int x = 0;
        int y = 0;
        switch (position) {
            case EAST:
                x = (int) (calculatePosition().x + (fraction * contentWidth));
                content.setBounds(x, content.getBounds().y, content.getWidth(), content.getHeight());
                break;
            case WEST:
                x = (int) (calculatePosition().x - (fraction * contentWidth));
                content.setBounds(x, content.getBounds().y, content.getWidth(), content.getHeight());
                break;
            case TOP:
                y = (int) (calculatePosition().y - (fraction * contentHeight));
                content.setBounds(content.getBounds().x, y, content.getWidth(), content.getHeight());
                break;
            case BOTTOM:
                y = (int) (calculatePosition().y + (fraction * contentHeight));
                content.setBounds(content.getBounds().x, y, content.getWidth(), content.getHeight());
                break;
            case LOWER_RIGHT_CORNER:
                y = (int) (calculatePosition().y + (fraction * contentHeight));
                x = (int) (calculatePosition().x + (fraction * contentWidth));
                content.setBounds(x, y, content.getWidth(), content.getHeight());
                break;
            case LOWER_LEFT_CORNER:
                x = (int) (calculatePosition().x - (fraction * contentWidth));
                y = (int) (calculatePosition().y + (fraction * contentHeight));
                content.setBounds(x, y, content.getWidth(), content.getHeight());
                break;
            case UPPER_RIGHT_CORNER:
                x = (int) (calculatePosition().x + (fraction * contentWidth));
                y = (int) (calculatePosition().y - (fraction * contentHeight));
                content.setBounds(x, y, content.getWidth(), content.getHeight());
                break;
            case UPPER_LEFT_CORNER:
                x = (int) (calculatePosition().x - (fraction * contentWidth));
                y = (int) (calculatePosition().y - (fraction * contentHeight));
                content.setBounds(x, y, content.getWidth(), content.getHeight());
                break;
            default:
                break;
        }
        glassPane.repaint();
    }
    public void hideNow(){
        //To be implemented
    }

}
