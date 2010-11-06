package eu.scy.tools.math.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

public class MathTriangle  extends Polygon implements IMathTriangle {

	private Color fillColor  = new Color(0xff9999);
	private Rectangle cornerPointRectangle;
	private Point[] points;
	private boolean showCornerPoints;
	private String id;
	private Point pointP;
	private Point pointQ;
	private Point pointR;


	public MathTriangle(int x, int y, int length) {
		setPointP(new Point(x,y)); //top
		setPointQ(new Point(x-length,y+length)); //left
		setPointR(new Point(x,y+length)); //right
		
	}
	@Override
	public void addX(int x) {
//		this.x += x;
		this.createCornerPoints();
	}
	
	@Override
	public void createCornerPoints() {
		
	}

	@Override
	public void addY(int y) {
//		this.y += y;
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
	public void setCornerPointRectangle(Rectangle cornerPointRectangle) {
		this.cornerPointRectangle = cornerPointRectangle;
	}

	@Override
	public Rectangle getCornerPointRectangle() {
		return cornerPointRectangle;
	}


	@Override
	public int isHitOnEndPoints(Point eventPoint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void paintComponent(Graphics g) {
		
		System.out.println("repainting triangle");

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
	 
		
		Polygon p = new Polygon();
		p.addPoint(getPointP().x,getPointP().y);
		p.addPoint(getPointQ().x,getPointQ().y);
		p.addPoint(getPointR().x,getPointR().y);
		
		 g2.setPaint(fillColor);
	      g2.fill(p);
	      
		 int dx = getPointQ().x-getPointR().x;
		 int dy = getPointQ().y;
		 
	    
	      
	      Line2D.Double centerLine = new Line2D.Double(getPointP().x,getPointP().y, getPointP().x, dy);
			Stroke oldStroke = g2.getStroke();
			 Stroke thindashed = new BasicStroke(1.0f, // line width
				      /* cap style */BasicStroke.CAP_BUTT,
				      /* join style, miter limit */BasicStroke.JOIN_BEVEL, 1.0f,
				      /* the dash pattern */new float[] { 1.0f, 2.0f, 1.0f, 2.0f },
				      /* the dash phase */0.0f); /* on 8, off 3, on 2, off 3 */
		    g2.setStroke(thindashed);
			
			g2.setPaint(Color.black);
			g2.draw(centerLine);
			g2.setStroke(oldStroke);
//		GeneralPath triangle = new GeneralPath();
//		triangle.co
//		triangle.moveTo(x, y);
//	      triangle.lineTo(40, -40);
//	      triangle.lineTo(-40, -40);
//	      triangle.closePath();
		
//	      g2.setPaint(fillColor);
//	      g2.fill(triangle);
		
		
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
	public String getType() {
		return "triangle";
	}

	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		return this.id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public void setPointP(Point pointP) {
		this.pointP = pointP;
	}
	@Override
	public Point getPointP() {
		return pointP;
	}
	@Override
	public void setPointQ(Point pointQ) {
		this.pointQ = pointQ;
	}
	@Override
	public Point getPointQ() {
		return pointQ;
	}
	@Override
	public void setPointR(Point pointR) {
		this.pointR = pointR;
	}
	@Override
	public Point getPointR() {
		return pointR;
	}
}
