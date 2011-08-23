package edu.scy.tools.math.test.ui.shapes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


	public class Boxes extends JPanel
	{
	    Shape[] shapes;
	    Dimension size;
	    Rectangle bounds;
	    int selectedIndex;
	    boolean adjustingScale;
	    final int H   = 20;
	    final int PAD = 15;
	 
	    public Boxes()
	    {
	        size = new Dimension();
	        adjustingScale = false;
	    }
	 
	    protected void paintComponent(Graphics g)
	    {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D)g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                            RenderingHints.VALUE_ANTIALIAS_ON);
	        if(shapes == null)
	            initPaths();
	        for(int j = 0; j < shapes.length; j++)
	            g2.draw(shapes[j]);
	 
	        //g2.setPaint(Color.red);
	        //g2.draw(bounds);
	    }
	 
	    public void setPath(int index, int x, int y)
	    {
	        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
	        shapes[index] = at.createTransformedShape(shapes[index]);
	        repaint();
	        resetSize();
	    }
	 
	    public void setScaleSelection(int index)
	    {
	        if(index == -1)
	            adjustingScale = false;
	        else
	        {
	            selectedIndex = index;
	            adjustingScale = true;
	        }
	    }
	 
	    public Dimension getPreferredSize() { return size; }
	 
	    private void resetSize()
	    {
	        int minX = getWidth(), minY = getHeight();
	        int maxWidth = 0, maxHeight = 0;
	        for(int j = 0; j < shapes.length; j++)
	        {
	            Rectangle r = shapes[j].getBounds();
	            if(r.x < minX) minX = r.x;
	            if(r.y < minY) minY = r.y;
	            if(r.x + r.width > maxWidth) maxWidth = r.x + r.width;
	            if(r.y + r.height > maxHeight) maxHeight = r.y + r.height;
	        }
	        bounds = new Rectangle(minX, minY, maxWidth-minX, maxHeight-minY);
	        size.width = maxWidth + 2*PAD;
	        size.height = maxHeight + PAD;
	        revalidate();
	    }
	 
	    private void initPaths()
	    {
	        Random seed = new Random();
	        shapes = new Shape[2];
	        double yInc = (getHeight() - 2*H)/3;
	        for(int j = 0; j < shapes.length; j++)
	        {
	            double y = yInc + j*(yInc + H);
	            shapes[j] = getPath(y, seed);
	        }
	        resetSize();
	    }
	 
	    private GeneralPath getPath(double y0, Random seed)
	    {
	        int width = 0;
	        int n = 4 + seed.nextInt(5);        // 4 - 8 boxes
	        int[] widths = new int[n];
	        for(int j = 0; j < n; j++)
	        {
	            int w = 20 + seed.nextInt(41);
	            width += w;
	            widths[j] = w;                  // 20 - 60 px width
	        }
	        double x0 = (getWidth() - width)/2;
	        Rectangle2D.Double r = new Rectangle2D.Double(x0, y0, width, H);
	        GeneralPath path = new GeneralPath(r);
	        double x = r.x;
	        for(int j = 0; j < widths.length; j++)
	        {
	            path.append(new Line2D.Double(x, y0, x, y0+H), false);
	            x += widths[j];
	        }
	        return path;
	    }
	 
	    private JPanel getControls()
	    {
	        final JButton zoomIn  = new JButton("zoom in");
	        final JButton zoomOut = new JButton("zoom out");
	        ActionListener l = new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                if(adjustingScale)
	                {
	                    JButton button = (JButton)e.getSource();
	                    double scale = 1.0;
	                    if(button == zoomIn)
	                        scale = 1.05;
	                    if(button == zoomOut)
	                        scale = 0.95;
	                    Rectangle2D r = shapes[selectedIndex].getBounds2D();
	                    double x = r.getCenterX()*(1.0 - scale);
	                    AffineTransform at = AffineTransform.getTranslateInstance(x,0);
	                    at.scale(scale, 1.0);
	                    shapes[selectedIndex] =
	                        at.createTransformedShape(shapes[selectedIndex]);
	                    repaint();
	                    resetSize();
	                }
	            }
	        };
	        zoomIn.addActionListener(l);
	        zoomOut.addActionListener(l);
	        JPanel panel = new JPanel();
	        panel.add(zoomIn);
	        panel.add(zoomOut);
	        return panel;
	    }
	 
	    public static void main(String[] args)
	    {
	        Boxes boxes = new Boxes();
	        ShapeMover mover = new ShapeMover(boxes);
	        boxes.addMouseListener(mover);
	        boxes.addMouseMotionListener(mover);
	        JFrame f = new JFrame();
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.getContentPane().add(new JScrollPane(boxes));
	        f.getContentPane().add(boxes.getControls(), "South");
	        f.setSize(400,400);
	        f.setLocation(200,200);
	        f.setVisible(true);
	    }
	}
	 
	class ShapeMover extends MouseInputAdapter
	{
	    Boxes boxes;
	    int selectedIndex;
	    Point start;
	    boolean dragging;
	 
	    public ShapeMover(Boxes boxes)
	    {
	        this.boxes = boxes;
	        dragging = false;
	    }
	 
	    public void mousePressed(MouseEvent e)
	    {
	        Point p = e.getPoint();
	        if(boxes.adjustingScale)
	        {
	            boxes.setScaleSelection(-1);
	            return;
	        }
	        for(int j = 0; j < boxes.shapes.length; j++)
	        {
	            Rectangle r = boxes.shapes[j].getBounds();
	            if(r.contains(p))
	            {
	                if(SwingUtilities.isRightMouseButton(e))
	                    boxes.setScaleSelection(j);
	                else
	                {
	                    selectedIndex = j;
	                    start = p;
	                    dragging = true;
	                }
	                break;
	            }
	        }
	    }
	 
	    public void mouseReleased(MouseEvent e) { dragging = false; }
	 
	    public void mouseDragged(MouseEvent e)
	    {
	        if(dragging)
	        {
	            Point end = e.getPoint();
	            int x = end.x - start.x;
	            int y = end.y - start.y;
	            boxes.setPath(selectedIndex, x, y);
	            start = end;
	        }
	    }
	}
