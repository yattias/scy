package eu.scy.tools.math.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

import diva.canvas.CanvasUtilities;
import diva.util.java2d.Polygon2D;
import diva.util.java2d.ShapeUtilities;

import eu.scy.tools.math.ui.UIUtils;

public class MathTriangle extends Rectangle implements IMathTriangle {

	private Color fillColor  = new Color(0xff9999);
	private Rectangle[] cornerPointRectangles = new Rectangle[2];
	private Point[] points = new Point[2];
	private boolean showCornerPoints = true;
	private String id;
	private Point pointP;
	private Point pointQ;
	private Point pointR;
	private Polygon polygon;


	public MathTriangle(int x, int y, int length) {
		setPointP(new Point(x,y)); //top
		setPointQ(new Point(x-length,y+length)); //left
		setPointR(new Point(x,y+length)); //right
		
		if( polygon == null )
			polygon = new Polygon();
		
		polygon.addPoint(getPointP().x,getPointP().y);
		polygon.addPoint(getPointQ().x,getPointQ().y);
		polygon.addPoint(getPointR().x,getPointR().y);
		
		this.createCornerPoints();
		
		this.x = x;
		this.y = y;
		this.width = length;
		this.height = length;
		
	}

	
	@Override
	public void moveXY(int x, int y) {
		polygon.translate(x, y);
		setPointP(new Point(getPointP().x+x,getPointP().y+y));
		setPointQ(new Point(getPointQ().x+x,getPointQ().y+y));
		setPointR(new Point(getPointR().x+x,getPointR().y+y));
		this.createCornerPoints();
	}
	
	@Override
	public void createCornerPoints() {
		points[0] = getPointP();
		points[1] = getPointR();
	}

	@Override
	public void addX(int x) {
		this.x += x;
	}
	@Override
	public void addY(int y) {
		this.y += y;
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
	public void setCornerPointRectangles(Rectangle[] cornerPointRectangles) {
		this.cornerPointRectangles = cornerPointRectangles;
	}

	@Override
	public Rectangle[] getCornerPointRectangles() {
		return cornerPointRectangles;
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
	public void paintComponent(Graphics g) {
	
		System.out.println("repainting triangle");
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(fillColor);
		g2.fill(polygon);
		
//		g2.setPaint(Color.blue);
//		g2.draw(this);

		if (isShowCornerPoints()) {
			g2.setPaint(Color.black);
			g2.draw(polygon);

			cornerPointRectangles[0] = new Rectangle(getPointP().x,
					getPointP().y - UIUtils.SHAPE_END_POINT_SIZE,
					UIUtils.SHAPE_END_POINT_SIZE, UIUtils.SHAPE_END_POINT_SIZE);
			g2.fill(cornerPointRectangles[0]);
			cornerPointRectangles[1] = new Rectangle(getPointR().x,
					getPointR().y, UIUtils.SHAPE_END_POINT_SIZE,
					UIUtils.SHAPE_END_POINT_SIZE);
			g2.fill(cornerPointRectangles[1]);
		}
			int dy = getPointR().y;
			
				Line2D.Double centerLine = new Line2D.Double(getPointP().x,
						getPointP().y, getPointP().x, dy);
				
				
			   Line2D.Double baseLine = new Line2D.Double(pointQ, pointR);
			   
			   System.out.println("baseline " + baseLine);
			   System.out.println("CenterLine " + centerLine);
			   
			   if( centerLine.intersectsLine(baseLine)) {
				
				
				Stroke oldStroke = g2.getStroke();
				Stroke thindashed = new BasicStroke(1.0f, // line width
						/* cap style */BasicStroke.CAP_BUTT,
						/* join style, miter limit */BasicStroke.JOIN_BEVEL, 1.0f,
						/* the dash pattern */new float[] { 2.0f, 2.0f, 1.0f, 2.0f },
						/* the dash phase */0.0f); /* on 8, off 3, on 2, off 3 */
				g2.setStroke(thindashed);
	
				g2.setPaint(UIUtils.dottedShapeLine);
				g2.draw(centerLine);
				
				g2.setStroke(oldStroke);
				
				
				 double height = Math.abs((centerLine.getY1() - centerLine.getY2()));
				//text height
				String s = "h = " + height;

			    AttributedString widthText = new AttributedString(s);
			    widthText.addAttribute(TextAttribute.FONT, UIUtils.plainFont);
			    
			    
				
				
				FontMetrics metrics = g.getFontMetrics();
				 
				Rectangle2D rect = metrics.getStringBounds(s, g);
				int sw = (int) rect.getWidth();
				
				
			    Point2D centerPoint = CanvasUtilities.getCenterPoint(centerLine.getBounds2D());
			    
//			    System.out.println("------ new Point " + centerPoint);
			    g2.setPaint(Color.black);
			    g2.drawString(widthText.getIterator(), (int)centerPoint.getX() + 5,(int)centerPoint.getY());
		
		}

		
			
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


	@Override
	public boolean contains(Point point) {
		return this.pointInPolygon(polygon.xpoints, polygon.ypoints, point.x, point.y);
	}

	
	public boolean pointInPolygon(int X[], int Y[], int x, int y) {
		int i, j;
		boolean c = false;
		for (i = 0, j = X.length-2; i < X.length-1; j = i++)
		{

		if (( ((Y[i]<=y) && (y<Y[j])) || ((Y[j]<=y) && (y<Y[i])) ) &&
		(x < (X[j] - X[i]) * (y - Y[i]) / (Y[j] - Y[i]) + X[i]))
		c = !c;
		}
		return c;
	}

	@Override
	public void moveCornerPoint(int position, Point newPoint) {
		switch (position) {
		case 0:
			// can move all ways
			this.translateCornerPoint(position, newPoint);
			repaint();
			break;
		case 1:
			// can move just x
			this.translateCornerPoint(position, newPoint);
			repaint();
			break;
		default:
			break;
		}
	}

	public void translateCornerPoint(int polyPosition, Point newPoint) {
		Point oldPosition = points[polyPosition];
		for (int i = 0; i < polygon.xpoints.length; i++) {
			int x = polygon.xpoints[i];
			int y = polygon.ypoints[i];
			
			if( oldPosition.x == x && oldPosition.y == y ) {
				polygon.xpoints[i] = newPoint.x;
				
				if(polyPosition == 0) {
					polygon.ypoints[polyPosition] = newPoint.y;
					points[polyPosition] = newPoint;
					setPointP(newPoint);
				}else if(polyPosition == 1){
					points[polyPosition] = new Point(newPoint.x,oldPosition.y);
					setPointR(points[polyPosition]);
				}
			}
		}
		
	}

}
