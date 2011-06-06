package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;

import eu.scy.scyplanner.application.Strings;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 09:02:21
 */
public class SCYPlannerSplashWindow extends JWindow { 
    //private JLabel image = new JLabel(ImageLoader.getImageLoader().getImageIcon(ImageLoader.SPLASH));
    private final PaintUtil paintUtil = new PaintUtil();

    /**
     * Creates a new SplashWindow which runs in a thread during
     * loading of the application, and that displays the current
     * staus in a StatusBar.
     */
    public SCYPlannerSplashWindow() {
        setSize(new Dimension(300, 320));
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());

        TitledLabel titledLabel = new TitledLabel(Strings.getString("SCYPlanner"), 70, true, true, Color.LIGHT_GRAY);
        add(BorderLayout.NORTH, titledLabel);


        //getContentPane().add(image, BorderLayout.NORTH);

        //statusBar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        //getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    private class TitledLabel extends JPanel {
        private EtchedBorder lineBorder = new EtchedBorder();
        private String text = null;
        private Color color = null;
        private boolean showFullBorder = false;

        public TitledLabel(String text, int heightAndWidth, boolean opaque, boolean showFullBorder, Color color) {
            this.text = text;
            this.color = color;
            this.showFullBorder = showFullBorder;
            Dimension d = new Dimension(heightAndWidth, heightAndWidth);
            setMinimumSize(d);
            setPreferredSize(d);
            setOpaque(opaque);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (isOpaque()) {
                paintUtil.paintGradiantBackground(this, g);
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            TextLayout tl = new TextLayout(text,
                    new Font("verdana", Font.BOLD, getSize().height - 30),
                    g2.getFontRenderContext());
            float sw = (float) tl.getBounds().getWidth();
            float sh = (float) tl.getBounds().getHeight();
            AffineTransform transform = new AffineTransform();
            transform.setToTranslation(getSize().width / 2 - sw / 2, (((float) getHeight()) / 2) + sh / 2);
            Shape shape = tl.getOutline(transform);
            g2.draw(shape);

            g2.setColor(color);
            g2.fill(shape);
            g2.setColor(UIManager.getColor("control").darker().darker());
            g2.draw(shape);
        }

        protected void paintBorder(Graphics graphics) {
            if (showFullBorder) {
                lineBorder.paintBorder(this, graphics, 0, 0, getWidth(), getHeight());
            } else {
                lineBorder.paintBorder(this, graphics, -2, 0, getWidth() + 4, getHeight());
            }
        }
    }

    private class PaintUtil {
        public void paintGradiantBackground(Component component, Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics;
            GradientPaint gp1 = new GradientPaint(0f, 0f, UIManager.getColor("control"), 20f, 40f, UIManager.getColor("control").brighter(), true);
            g2.setPaint(gp1);
            g2.fillRect(0, 0, component.getWidth(), component.getHeight());
        }

        public void paintGradiantBackground1(Component component, Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics;
            GradientPaint gp1 = new GradientPaint(0f, component.getHeight() / 2, UIManager.getColor("control").brighter(), component.getWidth(), component.getHeight(), UIManager.getColor("control").darker().darker(), true);
            g2.setPaint(gp1);
            g2.fillRect(0, 0, component.getWidth(), component.getHeight());
        }

        public void paintGradiantBackground2(Component component, Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics;
            GradientPaint gp1 = new GradientPaint(0f, component.getHeight() / 2, new Color(255, 255, 255), component.getWidth(), component.getHeight(), new Color(181, 178, 171), true);
            g2.setPaint(gp1);
            g2.fillRect(0, 0, component.getWidth(), component.getHeight());
        }
    }
}
