package eu.scy.tools.math.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.paint.Colors;

public class MathToolRectangle extends Rectangle implements IMathRectangle {

	private Rectangle bounds;
	private Rectangle cornerPointRectangle;
	private Point[] points = new Point[2];
	private boolean showCornerPoints = true;
	int CORNER_POINT_ADJUST = (UIUtils.SHAPE_END_POINT_SIZE/2);
	private String id; 
	
	public MathToolRectangle(double x,double y, double w, double h) {
		this.setFrame(x, y, w, h);
		this.createCornerPoints();
//		this.setOpaque(false);
		this.repaint();
		
	}
	
	public void createCornerPoints() {
		
		//top left
		int cp = (UIUtils.SHAPE_END_POINT_SIZE/2); 

		points[0] = new Point(getRectangle().x, getRectangle().y);
		
		//bottom right
		
		points[1] = new Point(getRectangle().x + getRectangle().width,  getRectangle().y + getRectangle().height);
	}

	@Override
	public void paintComponent(Graphics g) {

		System.out.println("repainting rect");
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(1f);
		Color color2 = Colors.Gray.color(0.7f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 1.0f,
				width, height, new float[] { 0.6f, 1f }, new Color[] { color1,
						color2 });

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// GradientPaint gradientPaint= new GradientPaint(x, y, Color.WHITE,
		// 10, y,Color.BLUE, true);

		

		g2.setPaint(Color.black);
//		g2.draw(getRectangle());
		


		Color color = new Color(1, 0, 0, .7f);

		g2.setPaint(color);
		this.setFrameFromDiagonal(points[0], points[1]);
		g2.fill(this);
		g2.setPaint(Color.black);
		g2.draw(this);
		
		//draw corner rects
		if( isShowCornerPoints() ) {
				setCornerPointRectangle(new Rectangle(points[1].x - UIUtils.SHAPE_END_POINT_SIZE,points[1].y - UIUtils.SHAPE_END_POINT_SIZE,UIUtils.SHAPE_END_POINT_SIZE, UIUtils.SHAPE_END_POINT_SIZE)); 
				g2.fill(getCornerPointRectangle());
		}
		
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

		if (getCornerPointRectangle().getBounds2D().contains(eventPoint)) {
			System.out.println("mouse pressed found at position " + 1);
			return 1;
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
	public String toString() {
		return " x " + getRectangle().x + " y" + getRectangle().y + " bounds " + getRectangle().getBounds();
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
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}


	@Override
	public void addMouseListeners(MouseAdapter mouseAdapter) {
		this.addMouseListeners(mouseAdapter);
	}

	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getRectangle() {
		return this;
	}

	@Override
	public void setRectangle(Rectangle rectangle) {
		
	}

	
}
