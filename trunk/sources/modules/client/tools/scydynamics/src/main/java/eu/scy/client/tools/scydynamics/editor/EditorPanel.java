package eu.scy.client.tools.scydynamics.editor;

import colab.um.draw.JdObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import eu.scy.client.tools.scydynamics.model.Model;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {

    private final static Logger LOGGER = Logger.getLogger(EditorPanel.class.getName());
    private Model dModel;
    private Rectangle rDrag = null;
    private int startX, startY; // The starting position of the mouse.
    private String tip = null;
    private BufferedImage backgroundImage = null;
    private int alpha;
    private int tip_x, tip_y;
    private Font tip_font = new Font("Arial", Font.PLAIN, 10); // label font

    public EditorPanel() {
	super();
	setPreferredSize(new Dimension(1600, 1200));
	setLayout(null);
	setModel(null);
    }

    public void setModel(Model m) {
	dModel = m;
	if (dModel == null) {
	    setBackground(Color.gray);
	    setForeground(Color.gray);
	} else {
	    setBackground(m.getBackground());
	    setForeground(m.getForeground());
	}
	repaint();
    }

    public void setBackgroundImage(BufferedImage image, int alpha) {
	backgroundImage = image;
	this.alpha = alpha;
    }

    public void setBackgroundImage(BufferedImage image) {
	setBackgroundImage(image, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
	clear(g);
	if (dModel == null) {
	    return;
	}
	Graphics2D g2d = (Graphics2D) g;

	if (backgroundImage != null) {
	    g2d.drawImage(backgroundImage, 0, 0, null);
	    g2d.setColor(new Color(255, 255, 255, alpha)); // R,G,B,Alpha)
	    g2d.fillRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
	}

	for (JdObject obj : dModel.getObjects().values()) {
	    obj.draw(g2d);
	}

	if (tip != null) {
	    g2d.setFont(tip_font);
	    FontMetrics fm = g2d.getFontMetrics();
	    int w = fm.stringWidth(tip) + 8;
	    int h = fm.getHeight() + 4;
	    g2d.setColor(Color.black);
	    g2d.setBackground(new Color(255, 255, 224)); // light yellow
	    g2d.clearRect(tip_x - w / 2, tip_y - h / 2, w, h);
	    g2d.drawRect(tip_x - w / 2, tip_y - h / 2, w, h);
	    g2d.drawString(tip, tip_x - w / 2 + 4, tip_y + h / 2 - 3);
	}
    }

    public void repaintObject(JdObject o) {
	Graphics2D g = (Graphics2D) this.getGraphics();
	o.draw(g);
    }

    protected void clear(Graphics g) {
	super.paintComponent(g);
    }

    public void startRect(int x, int y) {
	startX = x;
	startY = y;
	rDrag = new Rectangle(x, y, 0, 0);
    }

    public void extendRect(int x2, int y2) {
	if (rDrag != null) {
	    Graphics2D g = (Graphics2D) this.getGraphics();
	    g.setXORMode(this.getBackground());
	    g.drawRoundRect(rDrag.x, rDrag.y, rDrag.width, rDrag.height, 2, 2);
	    int x, y, w, h;
	    if (startX > x2) {
		x = x2;
		w = startX - x2;
	    } else {
		x = startX;
		w = x2 - startX;
	    }
	    if (startY > y2) {
		y = y2;
		h = startY - y2;
	    } else {
		y = startY;
		h = y2 - startY;
	    }
	    g.drawRoundRect(x, y, w, h, 2, 2);
	    rDrag.setBounds(x, y, w, h);
	}
    }

    public Rectangle stopRect() {
	Rectangle r = null;
	if (rDrag != null) {
	    Graphics2D g = (Graphics2D) this.getGraphics();
	    g.setXORMode(this.getBackground());
	    g.drawRoundRect(rDrag.x, rDrag.y, rDrag.width, rDrag.height, 2, 2);
	    g.setPaintMode();
	    r = new Rectangle(rDrag);
	    rDrag = null;
	}
	return r;
    }

    public void setTip(String s, int x, int y) {
	if (s == null || s.length() < 1) {
	    return;
	}
	if (!s.equals(tip) && rDrag == null) {
	    tip = s;
	    tip_x = x;
	    tip_y = y;
	    this.repaint();
	}
    }

    public void clearTip() {
	if (tip == null) {
	    return;
	}
	tip = null;
	tip_x = -1;
	tip_y = -1;
	if (rDrag == null) {
	    this.repaint();
	}
    }

}
