package eu.scy.tools.math.shapes.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.text.DecimalFormat;

import eu.scy.tools.math.doa.json.ICircleToolbarShape;
import eu.scy.tools.math.shapes.IMathEllipse;
import eu.scy.tools.math.ui.UIUtils;

public class MathEllipse extends Ellipse2D.Double implements IMathEllipse {

	private DecimalFormat twoDForm = new DecimalFormat(UIUtils.PATTERN);

	private String id;
	private Point[] points  = new Point[1];
	private Color fillColor = new Color(153,204,153, UIUtils.ALPHA);
	private double radius;
	private Rectangle[] cornerPointRectangles = new Rectangle[1];
	private String formula;

	private String oldResult;

	private boolean hasDecorations;

	private boolean showCornerPoints = true;

	private ICircleToolbarShape shape;
	
	public MathEllipse(double x, double y, double width, double height) {
        setFrame(x, y, width, height);
        this.createCornerPoints();
    }
	
	public MathEllipse(ICircleToolbarShape shape, int x, int y, int width, int height) {
		this(x, y,java.lang.Double.valueOf(shape.getRadius())*2, java.lang.Double.valueOf(shape.getRadius())*2);
		this.shape = shape;
	}

	@Override
	public void paintComponent(Graphics g) {
		
		// // System.out.println("repainting ellipse");
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		this.setRadius(this.width / 2);

		double xCenter = this.x+getRadius();
		double yCenter = this.y+getRadius();
		Line2D.Double centerLine = new Line2D.Double(xCenter,yCenter, xCenter+getRadius(), yCenter);

		g2.setPaint(fillColor);
		g2.fill(this);
		
		if( isShowCornerPoints() ) {
			g2.setPaint(Color.black);
			g2.draw(this);
		}
		
		
		
		//draw corner rects
		if( isShowCornerPoints() ) {
			    cornerPointRectangles[0] = new Rectangle(points[0].x,points[0].y,UIUtils.SHAPE_END_POINT_SIZE, UIUtils.SHAPE_END_POINT_SIZE);
				g2.fill(cornerPointRectangles[0]);
		}
		
		
		if( hasDecorations ) {
			
			//text height
				String s = "r = " + twoDForm.format( getRadius() / UIUtils._PIXEL ) + " " + UIUtils.METERS;;

			    AttributedString widthText = new AttributedString(s);
			    widthText.addAttribute(TextAttribute.FONT, UIUtils.plainFont);
			    
			    
				
				
				FontMetrics metrics = g.getFontMetrics();
				 
				Rectangle2D rect = metrics.getStringBounds(s, g);
				int sw = (int) rect.getWidth();
				
				int x = (centerLine.getBounds().x + (centerLine.getBounds().width / 2)) - (sw/3);
			    int y = centerLine.getBounds().y+ centerLine.getBounds().height + 15;
			    
			    g2.setPaint(Color.black);
			    g2.drawString(widthText.getIterator(), x,y);
			    
			    Stroke oldStroke = g2.getStroke();
				 Stroke thindashed = new BasicStroke(1.0f, // line width
					      /* cap style */BasicStroke.CAP_BUTT,
					      /* join style, miter limit */BasicStroke.JOIN_BEVEL, 1.0f,
					      /* the dash pattern */new float[] { 1.0f, 2.0f, 1.0f, 2.0f },
					      /* the dash phase */0.0f); /* on 8, off 3, on 2, off 3 */
			    g2.setStroke(thindashed);
				
				g2.setPaint(UIUtils.dottedShapeLine);
				g2.draw(centerLine);
				g2.setStroke(oldStroke);
		}

	}
	
	@Override
	public void repaint() {
		
	}

	@Override
	public void addX(int x) {
		this.x += x;
		this.createCornerPoints();
	}

	@Override
	public void addY(int y) {
		this.y += y;
		this.createCornerPoints();
	}
	
	@Override
	public void addWidth(int w) {
		this.width += w;
		this.createCornerPoints();
	}

	@Override
	public void addHeight(int h) {
		this.height += h;
		this.createCornerPoints();
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isShowCornerPoints() {
		return showCornerPoints;
	}

	@Override
	public void setShowCornerPoints(boolean showCornerPoints) {
		this.showCornerPoints = showCornerPoints;
		
	}

	@Override
	public Point[] getPoints() {
		return this.points;
	}

	@Override
	public void setPoints(Point[] points) {
		this.points = points;
	}

	@Override
	public int isHitOnEndPoints(Point eventPoint) {
		
		for (int i = 0; i < cornerPointRectangles.length; i++) {
			if (cornerPointRectangles[i].getBounds2D().contains(eventPoint)) {
				// // System.out.println("mouse pressed found at position " + 0);
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public boolean contains(Point point) {
		  if (this.contains(point.x, point.y)) {
              return true;
          } else {
              return false;
          }
	}

	@Override
	public Double getEllipse() {
		return this;
	}

	@Override
	public void setEllipse(Double ellipse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public Color getFillColor() {
		return fillColor;
	}

	@Override
	public String toString() {
		return "x : " + this.x + " y; " + this.y + " w: " + this.width + " h: " + this.height + " type: " + this.getType();
	}

	@Override
	public String getType() {
		return "Circle";
	}

	@Override
	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public double getRadius() {
		return radius;
	}

	@Override
	public double getScaledRadius() {
		return java.lang.Double.valueOf(twoDForm.format( getRadius() / UIUtils._PIXEL ));
	}


	@Override
	public void createCornerPoints() {
		double radius = this.width / 2;
		int xp = (int) (this.x+(radius*2));
		int yp = (int) (this.y+radius);
		points[0] = new Point(xp,yp);
		
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
	public void setFormula(String formula) {
		this.formula = formula;
		
	}

	@Override
	public String getFormula() {
		return this.formula;
	}

	@Override
	public String getResult() {
		return this.oldResult;
	}

	@Override
	public void setResult(String result) {
		this.oldResult = result;
	}

	@Override
	public void setHasDecorations(boolean hasDecorations) {
		this.hasDecorations = hasDecorations;
	}

}
