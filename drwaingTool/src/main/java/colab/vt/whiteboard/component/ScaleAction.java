package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public abstract class ScaleAction extends SimpleSelectionAction
{
	private static final long serialVersionUID = 8552498781649511783L;

//	private final double maximumDelta = 0.1;
//	private final int maximumIterations = 10;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ScaleAction.class.getName());

	private Cursor moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

	// move values
//	private int xBegin;
//	private int yBegin;
//	private double xOffset;
//	private double yOffset;
	// scale values
	private double initialXScale;
	private double initialYScale;
//	private Rectangle2D objectRectangle;
	private double centerX = 0;
	private double centerY = 0;
//	private Point2D transformedCenter;
	private double initialXLength = 1;
	private double initialYLength = 1;

	public ScaleAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label);
	}

	@Override
	public Cursor getActionCursor()
	{
		return moveCursor;
	}

	@Override
	protected void prepareAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		// move values
//		xBegin = e.getX();
//		yBegin = e.getY();
//		xOffset = whiteboardObjectContainer.getXOffset();
//		yOffset = whiteboardObjectContainer.getYOffset();
		// scale values
		initialXScale = whiteboardContainer.getXScale();
		initialYScale = whiteboardContainer.getYScale();
//		objectRectangle = whiteboardContainer.getContentBounds();
//		centerX = objectRectangle.getCenterX() + whiteboardObjectContainer.getXOffset();
//		centerY = objectRectangle.getCenterY() + whiteboardObjectContainer.getYOffset();
//		transformedCenter = getScaledCenterPoint(initialXScale, initialYScale);
		initialXLength = e.getX() - centerX;
		initialYLength = e.getY() - centerY;
		if (Math.abs(initialXLength) < 0.1)
			initialXLength = 1;
		if (Math.abs(initialYLength) < 0.1)
			initialYLength = 1;
//		System.out.println("prepareAction");
//		System.out.println(" xOffset=" + xOffset + ", yOffset=" + yOffset + ", initialXScale="
//					+ initialXScale + ", initialYScale=" + initialYScale);
//		System.out.println(" centerX=" + centerX + ", centerY=" + centerY + ", initialXLength="
//					+ initialXLength + ", initialYLength=" + initialYLength);
//		System.out.println(" objectRectangle=" + objectRectangle);
//		System.out.println(" transformedCenter=" + transformedCenter);
	}

	@Override
	protected boolean applyAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		double xLength = e.getX() - centerX;
		double xScale = xLength / initialXLength * initialXScale;
		double yLength = e.getY() - centerY;
		double yScale = yLength / initialYLength * initialYScale;

		whiteboardContainer.setXScale(xScale);
		whiteboardContainer.setYScale(yScale);
		
//		whiteboardObjectContainer.setXScaleOffsetCompensation((1-xScale)*objectRectangle.getCenterX());
//		whiteboardObjectContainer.setYScaleOffsetCompensation((1-yScale)*objectRectangle.getCenterY());

		return true;
	}

//	private Point2D getScaledCenterPoint(double scaleX, double scaleY)
//	{
//		AffineTransform affineTransform = new AffineTransform();
//		affineTransform.scale(scaleX, scaleY);
//		Point2D point = new Point2D.Double(centerX, centerY);
//		Point2D transformedPoint = new Point2D.Double();
//		affineTransform.transform(point, transformedPoint);
//		return transformedPoint;
//	}
//
//	private Point2D transformPoint(AffineTransform affineTransform, Point2D point2D, String label)
//	{
//		Point2D transformedPoint = new Point2D.Double();
//		affineTransform.transform(point2D, transformedPoint);
//		System.out.println(label + "=" + transformedPoint);
//		return transformedPoint;
//	}

}
