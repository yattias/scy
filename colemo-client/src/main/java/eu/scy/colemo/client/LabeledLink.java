package eu.scy.colemo.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

public class LabeledLink extends JComponent {
    private String default_label = "Link";
    private Point from = new Point(0, 0);
    private Point to = new Point(100, 100);
    private boolean bidirectional = false;
    private JTextField textField = new JTextField(default_label);

    public LabeledLink() {
        setOpaque(false);
        setLayout(null);
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(default_label)) textField.setText("");
                textField.setOpaque(true);
                textField.setBorder(BorderFactory.createEtchedBorder());

                Dimension prefsize = textField.getPreferredSize();
                textField.setSize(prefsize.width+20, prefsize.height);
                fixBounds();
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().equals("")) textField.setText(default_label);
                textField.setOpaque(false);
                textField.setBorder(null);
            }
        });
        textField.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {
                Dimension prefsize = textField.getPreferredSize();
                textField.setSize(prefsize.width+20, prefsize.height);
                fixBounds();
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) requestFocus();
            }

            public void keyReleased(KeyEvent e) {

            }
        });
        textField.setSize(textField.getPreferredSize());
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(null);
        textField.setOpaque(false);
        add(textField);
    }

    private void drawArrow(Graphics2D g2, Dimension d) {
        GeneralPath path = new GeneralPath();

        float diag = ((float)Math.sqrt(Math.pow(getInnerHeight(),2)+Math.pow(getInnerWidth(),2)));

        float[] start = new float[] {diag / 2f, 0};
        float[] end = new float[] {diag / -2f, 0};

        path.moveTo(start[0], start[1]);
        path.lineTo(end[0], end[1]);

        // Draw the arrow heads
        int x,y;

        // degrees between head and shaft
        float deg = 45;

        int size = 10;

        // Create the first arrow head line.
        x = (int)end[0] + size;
        y = (int) (size * Math.sin(deg));
        path.lineTo(x,y);

        // Move to line-end
        path.moveTo(end[0], end[1]);

        // and create the second
        y = (int) (size * Math.sin(-deg));
        path.lineTo(x,y);

        if (bidirectional) {
            // Move to start
            path.moveTo(start[0], start[1]);
            x = (int)start[0] - size;
            y = (int) (size * Math.sin(deg));
            path.lineTo(x,y);
            path.moveTo(start[0], start[1]);
            y = (int) (size * Math.sin(-deg));
            path.lineTo(x,y);
        }
        g2.setPaint(Color.blue);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        Shape shape = at.createTransformedShape(path);
        g2.draw(shape);
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public void setFrom(Point p) {
        from = p;
        fixBounds();
    }
    public void setTo(Point p) {
        to = p;
        fixBounds();
    }
    public boolean isBidirectional() {
        return bidirectional;
    }
    public void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    private int getInnerWidth() {
        return Math.abs(from.x - to.x);
    }
    private int getInnerHeight() {
        return Math.abs(from.y - to.y);
    }

    private void fixBounds() {
        Dimension labelSize = textField.getSize();
        int w, x, h, y;
        if (to.x < from.x) {
            x = to.x;
            w = from.x - to.x;
        }
        else {
            x = from.x;
            w = to.x - from.x;
        }
        if (to.y < from.y) {
            y = to.y;
            h = from.y - to.y;
        }
        else {
            y = from.y;
            h = to.y - from.y;
        }
        setBounds(x-(labelSize.width/2), y-10, w+labelSize.width, h+20);
    }
    public double getAngle() {
        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        double angle;

        if (dx == 0.0) {
            if(dy == 0.0) angle = 0.0;
            else if(dy > 0.0) angle = Math.PI / 2.0;
            else angle = (Math.PI * 3.0) / 2.0;
        }
        else if(dy == 0.0) {
            if(dx > 0.0) angle = 0.0;
            else angle = Math.PI;
        }
        else {
            if(dx < 0.0) angle = Math.atan(dy/dx) + Math.PI;
            else if(dy < 0.0) angle = Math.atan(dy/dx) + (2*Math.PI);
            else angle = Math.atan(dy/dx);
        }
        return (angle * 180) / Math.PI;
    }

    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        double angle = getAngle();
        Dimension d = getSize();

        g2.rotate(Math.toRadians(angle), d.width/2, d.height/2);
        g2.translate(d.width/2, d.height / 2);

        drawArrow(g2, d);

        g2.dispose();

        Dimension labelSize = textField.getSize();
        textField.setBounds(getWidth()/2-(labelSize.width/2), getHeight()/2-(labelSize.height/2), labelSize.width, labelSize.height);
    }
}