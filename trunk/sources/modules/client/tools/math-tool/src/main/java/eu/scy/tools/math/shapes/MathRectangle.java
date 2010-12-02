package eu.scy.tools.math.shapes;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

import eu.scy.tools.math.ui.UIUtils;

public class MathRectangle extends Rectangle implements IMathRectangle {

	private Rectangle bounds;
	private Point[] points = new Point[2];
	private boolean showCornerPoints = true;
	private String id; 
	private Color fillColor = new Color(0x9999ff);
	private Rectangle[] cornerPointRectangles = new Rectangle[1];

	public MathRectangle(double x,double y, double w, double h) {
		this.setFrame(x, y, w, h);
		this.createCornerPoints();
//		this.setOpaque(false);
		this.repaint();
		
	}
	
	public void createCornerPoints() {
		
	
		int cp = (UIUtils.SHAPE_END_POINT_SIZE/2); 

		
		
		//bottom right
		
		points[0] = new Point(getRectangle().x + getRectangle().width,  getRectangle().y + getRectangle().height);
		
		//top left
		points[1] = new Point(getRectangle().x, getRectangle().y);
	}

	@Override
	public void paintComponent(Graphics g) {

		System.out.println("repainting rectangle");

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);


		

		g2.setPaint(fillColor);
		this.setFrameFromDiagonal(points[0], points[1]);
		g2.fill(this);
		
		if( isShowCornerPoints() ) {
			g2.setPaint(Color.black);
			g2.draw(this);
		}
		
		//draw corner rects
		if( isShowCornerPoints() ) {
				cornerPointRectangles[0] = new Rectangle(points[0].x - UIUtils.SHAPE_END_POINT_SIZE,points[0].y - UIUtils.SHAPE_END_POINT_SIZE,UIUtils.SHAPE_END_POINT_SIZE, UIUtils.SHAPE_END_POINT_SIZE); 
				g2.fill(cornerPointRectangles[0]);
		}	
				//text height
				String s = "h = " + this.getHeight();

			    AttributedString heightText = new AttributedString(s);
			    heightText.addAttribute(TextAttribute.FONT, UIUtils.plainFont);
			    
			    int x = getRectangle().x + getRectangle().width + 3;
			    int y = getRectangle().y + (getRectangle().height  / 2);
			    g2.setPaint(Color.black);
				g2.drawString(heightText.getIterator(), x,y);
				
				//text height
				s = "w = " + this.getWidth();

			    AttributedString widthText = new AttributedString(s);
			    widthText.addAttribute(TextAttribute.FONT, UIUtils.plainFont);
			    
			    
				
				
				FontMetrics metrics = g.getFontMetrics();
				 
				Rectangle2D rect = metrics.getStringBounds(s, g);
				int sw = (int) rect.getWidth();
				
				x = (getRectangle().x + (getRectangle().width / 2)) - (sw/3);
			    y = getRectangle().y+ getRectangle().height + 15;
			    
			    g2.setPaint(Color.black);
			    g2.drawString(widthText.getIterator(), x,y);
		
		

		
		
		
	}

	@Override
	public boolean contains(Point point) {
	            if (getBounds2D().contains(point.x, point.y)) {
	                return true;
	            } else {
	                return false;
	            }
	}
	@Override
	public int isHitOnEndPoints(Point eventPoint) {

		for (int i = 0; i < cornerPointRectangles.length; i++) {
			if (cornerPointRectangles[i].getBounds2D().contains(eventPoint)) {
				System.out.println("mouse pressed found at position " + 1);
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public void addX(int x) {
		getRectangle().x += x;
		this.createCornerPoints();
	}
	@Override
	public void addY(int y) {
		getRectangle().y += y;
		this.createCornerPoints();
	}
	
	@Override
	public void setShowCornerPoints(boolean showCornerPoints) {
		this.showCornerPoints = showCornerPoints;
	}

	@Override
	public boolean isShowCornerPoints() {
		return showCornerPoints;
	}

	@Override
	public void setPoints(Point[] points) {
		this.points = points;
	}

	@Override
	public Point[] getPoints() {
		return points;
	}

	@Override
	public double getHeight() {
		return getRectangle().height;
	}
	
	@Override
	public double getWidth() {
		return getRectangle().width;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void repaint() {
		
	}

	@Override
	public Rectangle getRectangle() {
		return this;
	}

	@Override
	public void setRectangle(Rectangle rectangle) {
		
	}

	@Override
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public Color getFillColor() {
		return this.fillColor;
	}

	@Override
	public String toString() {
		return "x : " + this.x + " y; " + this.y + " w: " + this.width + " h: " + this.height + " type: " + this.getType();
	}
	
	@Override
	public String getType() {
		return "rectangle";
	}

	@Override
	public void setCornerPointRectangles(Rectangle[] cornerPointRectangles) {
		this.cornerPointRectangles = cornerPointRectangles;
		
	}

	@Override
	public Rectangle[] getCornerPointRectangles() {
		return cornerPointRectangles;
	}


}
