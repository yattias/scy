package colab.vt.whiteboard.component;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.jdom.Element;

import colab.vt.whiteboard.component.ScaleRectangle.Direction;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedListener;
import colab.vt.whiteboard.component.state.WhiteboardContainerInfoDisplay;
import colab.vt.whiteboard.utils.TransformUtils;
import colab.vt.whiteboard.utils.XmlUtils;

public abstract class AbstractWhiteboardContainer implements WhiteboardContainer
{
	private static final long serialVersionUID = -7851432279930538916L;
	// @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AbstractWhiteboardContainer.class
				.getName());

	private static final int selectionRectangleSize = 10;
	private static final double selectionRectangleSize2 = selectionRectangleSize / 2.0;
	// private static final double maxSelectionRectangleSizeIncrease = 0.5 * Math.sqrt(2)
	// * selectionRectangleSize;
	private static final double maxSelectionRectangleSizeIncrease = selectionRectangleSize2;
	private static final int rotateSelectionRectangleDistance = 30;
	private static final Color selectionRectangleColor = new Color(128, 128, 255, 128);
	private static final Color lockedSelectionRectangleColor = new Color(128, 128, 255, 32);
	private static final Color selectionLinesColor = new Color(128, 128, 255, 224);
	// private static final Color boundsColor = new Color(128, 255, 128, 64);
	private static final Color boundsColor = new Color(96, 255, 96, 64);
	private static final double minimumTransformScale = 1e-3;

	private boolean drawObjectBounds = false;
	private boolean smartObjectPaint = true;
	private boolean showContainerToStringTootip = false;

	private WhiteboardPanel whiteboardPanel;
	private CopyOnWriteArrayList<WhiteboardContainerChangedListener> whiteboardContainerChangedListeners = new CopyOnWriteArrayList<WhiteboardContainerChangedListener>();
	private WhiteboardContainerInfoDisplay whiteboardContainerInfoDisplay = null;

	private String id = null;

	private double xOffset = 0;
	private double yOffset = 0;
	private double lineWidth = 1;
	private double xScaleOffsetCompensation = 0;
	private double yScaleOffsetCompensation = 0;
	private double rotation = 0;
	private double xScale = 1;
	private double yScale = 1;
	private double xShear = 0;
	private double yShear = 0;
	private Color fillColor = new Color(255, 255, 255, 0);
	private Color lineColor = Color.black;
	private AlphaComposite alphaComposite = AlphaComposite.SrcOver;
	private double penSize = 1;
	private boolean scaleIndependentPenSize = false;
	private boolean selected = false;
	private boolean locked = false;

	private long creationTime = 0;

	private AffineTransform affineTransform = null;
	private Rectangle2D drawedBounds = new Rectangle2D.Double();
	private ArrayList<ScaleRectangle> scaleRectangles = new ArrayList<ScaleRectangle>();
	private ArrayList<Point> selectionLines = new ArrayList<Point>();
	private Shape rotateRectangle = null;

	public AbstractWhiteboardContainer(WhiteboardPanel whiteboardPanel)
	{
		super();
		this.whiteboardPanel = whiteboardPanel;
		id = new VMID().toString();
		this.setCreationTime(System.currentTimeMillis());
	}

	public AbstractWhiteboardContainer(WhiteboardPanel whiteboardPanel, Element status)
	{
		super();
		this.whiteboardPanel = whiteboardPanel;
		this.id = status.getAttributeValue(XmlNames.id);
		// this.setCreationTime(System.currentTimeMillis());
	}

	@Override
	public void addWhiteboardContainerChangedListener(
				WhiteboardContainerChangedListener whiteboardObjectContainerChangedListener)
	{
		if (!whiteboardContainerChangedListeners.contains(whiteboardObjectContainerChangedListener))
		{
			whiteboardContainerChangedListeners.add(whiteboardObjectContainerChangedListener);
		}
	}

	public void removeWhiteboardContainerChangedListener(
				WhiteboardContainerChangedListener whiteboardObjectContainerChangedListener)
	{
		if (whiteboardContainerChangedListeners.contains(whiteboardObjectContainerChangedListener))
		{
			whiteboardContainerChangedListeners.remove(whiteboardObjectContainerChangedListener);
		}
	}

	public void sendWhiteboardContainerChangedEvent()
	{
		createSelectionRectangles();
		WhiteboardContainerChangedEvent whiteboardContainerChangedEvent = new WhiteboardContainerChangedEvent(
					this, getWhiteboardPanel(), this);
		for (WhiteboardContainerChangedListener whiteboardContainerChangedListener : whiteboardContainerChangedListeners)
		{
			whiteboardContainerChangedListener
						.whiteboardContainerChanged(whiteboardContainerChangedEvent);
		}
	}

	public void dispose()
	{
		if (whiteboardContainerInfoDisplay != null)
		{
			whiteboardContainerInfoDisplay.dispose();
		}
	}

	public WhiteboardContainerInfoDisplay getWhiteboardContainerInfoDisplay()
	{
		if (whiteboardContainerInfoDisplay == null)
		{
			whiteboardContainerInfoDisplay = new WhiteboardContainerInfoDisplay();
			whiteboardContainerInfoDisplay.setWhiteboardContainer(this);
		}
		return whiteboardContainerInfoDisplay;
	}

	@Override
	public String getToolTipText(MouseEvent event)
	{
		if (showContainerToStringTootip)
			return toString();
		return null;
	}

	public WhiteboardPanel getWhiteboardPanel()
	{
		return whiteboardPanel;
	}

	public void setCreationTime(long time)
	{
		this.creationTime = time;
	}

	public long getCreationTime()
	{
		return this.creationTime;
	}

	public double getXOffset()
	{
		return xOffset;
	}

	public void setXOffset(double offset)
	{
		xOffset = offset;
		sendWhiteboardContainerChangedEvent();
	}

	public double getYOffset()
	{
		return yOffset;
	}

	public void setYOffset(double offset)
	{
		yOffset = offset;
		sendWhiteboardContainerChangedEvent();
	}

	public double getLineWidth()
	{
		return lineWidth;
	}

	public void setLineWidth(double lineWidth)
	{
		this.lineWidth = lineWidth;
		sendWhiteboardContainerChangedEvent();
	}

	public double getXScaleOffsetCompensation()
	{
		return xScaleOffsetCompensation;
	}

	public void setXScaleOffsetCompensation(double scaleOffsetCompensation)
	{
		xScaleOffsetCompensation = scaleOffsetCompensation;
		sendWhiteboardContainerChangedEvent();
	}

	public double getYScaleOffsetCompensation()
	{
		return yScaleOffsetCompensation;
	}

	public void setYScaleOffsetCompensation(double scaleOffsetCompensation)
	{
		yScaleOffsetCompensation = scaleOffsetCompensation;
		sendWhiteboardContainerChangedEvent();
	}

	public double getRotation()
	{
		return rotation;
	}

	public void setRotation(double rotation)
	{
		this.rotation = rotation;
		calculateScaleOffsetCompensation();
		sendWhiteboardContainerChangedEvent();
	}

	public double getRotationDegrees()
	{
		return Math.toDegrees(rotation);
	}

	public void setRotationDegrees(double degrees)
	{
		setRotation(Math.toRadians(degrees));
	}

	public double getXScale()
	{
		return noZeroScale(xScale);
	}

	public void setXScale(double scale)
	{
		xScale = limitScale(scale);
		calculateScaleOffsetCompensation();
		sendWhiteboardContainerChangedEvent();
	}

	public double getYScale()
	{
		return noZeroScale(yScale);
	}

	public void setYScale(double scale)
	{
		yScale = limitScale(scale);
		calculateScaleOffsetCompensation();
		sendWhiteboardContainerChangedEvent();
	}

	public double getMinimumScale()
	{
		return WhiteboardPanel.minimumScale;
	}

	public double getMaximumScale()
	{
		return WhiteboardPanel.maximumScale;
	}

	public double limitScale(double scale)
	{
		if (scale < WhiteboardPanel.minimumScale)
			return WhiteboardPanel.minimumScale;
		else if (scale > WhiteboardPanel.maximumScale)
			return WhiteboardPanel.maximumScale;
		return scale;
	}

	private double noZeroScale(double scale)
	{
		if (Math.abs(scale) > minimumTransformScale)
			return scale;
		if (scale < 0)
			return -minimumTransformScale;
		return minimumTransformScale;
	}

	public void calculateScaleOffsetCompensation()
	{
		// TODO shear compensation does not work good
		xScaleOffsetCompensation = (1 - xScale - calculateShearFactor(xScale, xShear))
					* getContentBounds().getCenterX();
		yScaleOffsetCompensation = (1 - yScale - calculateShearFactor(yScale, yShear))
					* getContentBounds().getCenterY();
	}

	private double calculateShearFactor(double scale, double shear)
	{
		// double angle = Math.asin(1/shear);
		// double factor = 1/Math.sin(Math.PI/2-angle);
		// return factor;
		double factor = 0.686;
		return scale * shear * factor;
	}

	public double getXShear()
	{
		return xShear;
	}

	public void setXShear(double shear)
	{
		xShear = shear;
		calculateScaleOffsetCompensation();
		sendWhiteboardContainerChangedEvent();
	}

	public double getYShear()
	{
		return yShear;
	}

	public void setYShear(double shear)
	{
		yShear = shear;
		calculateScaleOffsetCompensation();
		sendWhiteboardContainerChangedEvent();
	}

	public Color getFillColor()
	{
		return fillColor;
	}

	public void setFillColor(Color fillColor)
	{
		this.fillColor = fillColor;
		sendWhiteboardContainerChangedEvent();
	}

	public Color getLineColor()
	{
		return lineColor;
	}

	public void setLineColor(Color lineColor)
	{
		this.lineColor = lineColor;
		sendWhiteboardContainerChangedEvent();
	}

	public AlphaComposite getAlphaComposite()
	{
		return alphaComposite;
	}

	public void setAlphaComposite(AlphaComposite alphaComposite)
	{
		this.alphaComposite = alphaComposite;
		sendWhiteboardContainerChangedEvent();
	}

	public double getPenSize()
	{
		return penSize;
	}

	public void setPenSize(double penSize)
	{
		this.penSize = penSize;
		sendWhiteboardContainerChangedEvent();
	}

	public boolean isScaleIndependentPenSize()
	{
		return scaleIndependentPenSize;
	}

	public void setScaleIndependentPenSize(boolean scaleIndependentPenSize)
	{
		this.scaleIndependentPenSize = scaleIndependentPenSize;
		sendWhiteboardContainerChangedEvent();
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		// System.out.println("setSelected(" + selected + ")");
		this.selected = selected;
		createSelectionRectangles();
		// sendWhiteboardContainerChangedEvent();
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}

	public double[] getTransformMatrix()
	{
		double[] flatmatrix = new double[6];
		setupAffineTransformation();
		affineTransform.getMatrix(flatmatrix);
		return flatmatrix;
	}

	public abstract Rectangle2D getContentBounds();

	abstract void paintContent(Graphics2D g2d);

	abstract boolean contentUnderMouse(int x, int y, double withPenSize);

	private void setupAffineTransformation()
	{
		Rectangle2D contentBounds = new Rectangle2D.Double();
		contentBounds.setRect(getContentBounds());
		affineTransform = new AffineTransform();
		affineTransform.translate(xOffset + xScaleOffsetCompensation, yOffset
					+ yScaleOffsetCompensation);
		// affineTransform.rotate(rotation, objectBounds.getCenterX(), objectBounds.getCenterY());
		affineTransform.rotate(rotation, contentBounds.getCenterX() - xScaleOffsetCompensation,
					contentBounds.getCenterY() - yScaleOffsetCompensation);
		affineTransform.scale(noZeroScale(xScale), noZeroScale(yScale));
		affineTransform.shear(xShear, yShear);
	}

	private double getRotateRectangleSizeIncrease()
	{
		return (rotateSelectionRectangleDistance + selectionRectangleSize2) / noZeroScale(yScale);
	}

	public Rectangle2D getScreenUpdateRectangleOld()
	{
		setupAffineTransformation();
		Rectangle2D contentBounds = new Rectangle2D.Double();
		contentBounds.setRect(getContentBounds());
		double y = contentBounds.getY();
		double height = contentBounds.getHeight();
		if (selected)
		{
			int rotateSizeIncrease = (int) Math.ceil(getRotateRectangleSizeIncrease());
			y -= rotateSizeIncrease;
			height += rotateSizeIncrease;
		}
		Rectangle2D objectBounds = new Rectangle2D.Double(contentBounds.getX(), y, contentBounds
					.getWidth(), height);
		Rectangle2D transformedBounds = TransformUtils.getTranformedRectangle(affineTransform,
					objectBounds);
		double sizeIncrease = lineWidth + 0;
		if (selected)
			sizeIncrease += maxSelectionRectangleSizeIncrease + 0;
		Rectangle2D bounds = new Rectangle2D.Double(transformedBounds.getX() - sizeIncrease,
					transformedBounds.getY() - sizeIncrease, transformedBounds.getWidth() + 2
								* sizeIncrease + 1, transformedBounds.getHeight() + 2 * sizeIncrease + 1);
		// bounds = new Rectangle2D.Double(objectBounds.getX() - sizeIncrease, objectBounds.getY()
		// - sizeIncrease, objectBounds.getWidth() + 2 * sizeIncrease, objectBounds.getHeight()
		// + 2 * sizeIncrease);
		// System.out.println("getScreenUpdateRectangle() " + bounds);
		return bounds;
	}

	public Rectangle2D getScreenUpdateRectangle()
	{
		// if (yScale == 0)
		// {
		// System.out.println("yScale=" + yScale);
		// }
		setupAffineTransformation();
		Rectangle2D contentBounds = new Rectangle2D.Double();
		contentBounds.setRect(getContentBounds());
		// calculate the sides from the draw edges
		double penSizeIncrease = Math.ceil(penSize / 2);
		Point2D topLeft = TransformUtils.getTransformedPoint(affineTransform, contentBounds.getMinX()
					- penSizeIncrease, contentBounds.getMinY() - penSizeIncrease);
		Point2D topRight = TransformUtils.getTransformedPoint(affineTransform, contentBounds
					.getMaxX()
					+ penSizeIncrease, contentBounds.getMinY() - penSizeIncrease);
		Point2D bottomRight = TransformUtils.getTransformedPoint(affineTransform, contentBounds
					.getMaxX()
					+ penSizeIncrease, contentBounds.getMaxY() + penSizeIncrease);
		Point2D bottomLeft = TransformUtils.getTransformedPoint(affineTransform, contentBounds
					.getMinX()
					- penSizeIncrease, contentBounds.getMaxY() + penSizeIncrease);

		double left = TransformUtils.getMinimum(topLeft.getX(), topRight.getX(), bottomRight.getX(),
					bottomLeft.getX());
		double top = TransformUtils.getMinimum(topLeft.getY(), topRight.getY(), bottomRight.getY(),
					bottomLeft.getY());
		double right = TransformUtils.getMaximum(topLeft.getX(), topRight.getX(), bottomRight.getX(),
					bottomLeft.getX());
		double bottom = TransformUtils.getMaximum(topLeft.getY(), topRight.getY(),
					bottomRight.getY(), bottomLeft.getY());

		if (selected)
		{
			// calculate the sides from the bounds edges for the selection rectangles
			topLeft = TransformUtils.getTransformedPoint(affineTransform, contentBounds.getMinX(),
						contentBounds.getMinY());
			topRight = TransformUtils.getTransformedPoint(affineTransform, contentBounds.getMaxX(),
						contentBounds.getMinY());
			bottomRight = TransformUtils.getTransformedPoint(affineTransform, contentBounds.getMaxX(),
						contentBounds.getMaxY());
			bottomLeft = TransformUtils.getTransformedPoint(affineTransform, contentBounds.getMinX(),
						contentBounds.getMaxY());
			Point2D rotatePoint;
			if (yScale >= 0)
				rotatePoint = TransformUtils.getTransformedPoint(affineTransform, contentBounds
							.getCenterX(), contentBounds.getMinY() - getRotateRectangleSizeIncrease());
			else
				rotatePoint = TransformUtils.getTransformedPoint(affineTransform, contentBounds
							.getCenterX(), contentBounds.getMinY() + getRotateRectangleSizeIncrease());
			double xSelectionIncrease = maxSelectionRectangleSizeIncrease;
			double ySelectionIncrease = maxSelectionRectangleSizeIncrease;
			double selectionLeft = TransformUtils.getMinimum(topLeft.getX(), topRight.getX(),
						bottomRight.getX(), bottomLeft.getX(), rotatePoint.getX())
						- xSelectionIncrease;
			double selectionTop = TransformUtils.getMinimum(topLeft.getY(), topRight.getY(),
						bottomRight.getY(), bottomLeft.getY(), rotatePoint.getY())
						- ySelectionIncrease;
			double selectionRight = TransformUtils.getMaximum(topLeft.getX(), topRight.getX(),
						bottomRight.getX(), bottomLeft.getX(), rotatePoint.getX())
						+ xSelectionIncrease;
			double selectionBottom = TransformUtils.getMaximum(topLeft.getY(), topRight.getY(),
						bottomRight.getY(), bottomLeft.getY(), rotatePoint.getY())
						+ ySelectionIncrease;

			left = Math.min(left, selectionLeft);
			top = Math.min(top, selectionTop);
			right = Math.max(right, selectionRight);
			bottom = Math.max(bottom, selectionBottom);
		}

		Rectangle2D bounds = new Rectangle2D.Double(left, top, right - left, bottom - top);
		// System.out.println("getScreenUpdateRectangle() " + bounds);
		return bounds;
	}

	private Rectangle2D getScreenUpdateRectangle(double x, double y, double width, double height)
	{
		setupAffineTransformation();
		Rectangle2D contentRectangle = new Rectangle2D.Double(x - penSize, y - penSize, width + 2
					* penSize, height + 2 * penSize);
		Rectangle2D transformedContentRectangle = TransformUtils.getTranformedRectangle(
					affineTransform, contentRectangle);
		return transformedContentRectangle;
	}

	public void paint(Graphics2D g)
	{
		if (!smartObjectPaint || g.getClip().getBounds().intersects(getScreenUpdateRectangle()))
		{
			// System.out.println("0:transform " + g2d.getTransform());
			Rectangle2D objectBounds = getContentBounds();

			Graphics2D g2d = (Graphics2D) g.create();
			g2d.translate(xOffset, yOffset);
			g2d.rotate(rotation, objectBounds.getCenterX(), objectBounds.getCenterY());
			g2d.translate(xScaleOffsetCompensation, yScaleOffsetCompensation);
			g2d.scale(noZeroScale(xScale), noZeroScale(yScale));
			g2d.shear(xShear, yShear);
			g2d.setColor(lineColor);
			g2d.setBackground(fillColor);
			g2d.setStroke(getStroke());
			g2d.setComposite(alphaComposite);
			paintContent(g2d);
			g2d.dispose();

			g2d = (Graphics2D) g.create();
			if (selected)
			{
				// paintSelection(g2d);
				drawSelectionRectangles(g2d);
			}
			if (drawObjectBounds)
			{
				g2d.setPaint(boundsColor);
				g2d.fill(getScreenUpdateRectangle());
			}
			g2d.dispose();
		}
		drawedBounds.setRect(getContentBounds());
	}

	private Stroke getStroke()
	{
		Stroke stroke = new BasicStroke((float) getPenSize());
		return stroke;
	}

	private void drawSelectionRectangles(Graphics2D g2d)
	{
		// createSelectionRectangles();
		if (isLocked())
			g2d.setPaint(lockedSelectionRectangleColor);
		else
			g2d.setPaint(selectionRectangleColor);
		for (ScaleRectangle scaleRectangle : scaleRectangles)
		{
			g2d.fill(scaleRectangle.getRectangle());
		}
		g2d.fill(rotateRectangle);
		g2d.setPaint(selectionLinesColor);
		for (int i = 1; i < selectionLines.size(); i++)
			g2d.drawLine(selectionLines.get(i - 1).x, selectionLines.get(i - 1).y, selectionLines
						.get(i).x, selectionLines.get(i).y);
	}

	protected void createSelectionRectangles()
	{
		setupAffineTransformation();
		// create the scale rectangles
		scaleRectangles.clear();
		Rectangle2D objectBounds = getContentBounds();
		// the corners
		scaleRectangles.add(new ScaleRectangle(Direction.northWest, createSelectionRactangle(
					objectBounds.getX(), objectBounds.getY())));
		scaleRectangles.add(new ScaleRectangle(Direction.northEast, createSelectionRactangle(
					objectBounds.getX(), objectBounds.getMaxY())));
		scaleRectangles.add(new ScaleRectangle(Direction.southEast, createSelectionRactangle(
					objectBounds.getMaxX(), objectBounds.getMaxY())));
		scaleRectangles.add(new ScaleRectangle(Direction.southWest, createSelectionRactangle(
					objectBounds.getMaxX(), objectBounds.getY())));
		// the middle point of bounding rectangle
		scaleRectangles.add(new ScaleRectangle(Direction.north, createSelectionRactangle(objectBounds
					.getCenterX(), objectBounds.getY())));
		scaleRectangles.add(new ScaleRectangle(Direction.east, createSelectionRactangle(objectBounds
					.getMaxX(), objectBounds.getCenterY())));
		scaleRectangles.add(new ScaleRectangle(Direction.south, createSelectionRactangle(objectBounds
					.getCenterX(), objectBounds.getMaxY())));
		scaleRectangles.add(new ScaleRectangle(Direction.west, createSelectionRactangle(objectBounds
					.getX(), objectBounds.getCenterY())));

		// create the rotate rectangle
		Rectangle2D rotateRect;
		if (yScale >= 0)
			rotateRect = createSelectionRactangle(objectBounds.getCenterX(), objectBounds.getY()
						- getRotateRectangleSizeIncrease());
		else
			rotateRect = createSelectionRactangle(objectBounds.getCenterX(), objectBounds.getY()
						+ getRotateRectangleSizeIncrease());
		rotateRectangle = new RoundRectangle2D.Double(rotateRect.getX(), rotateRect.getY(),
					rotateRect.getWidth(), rotateRect.getHeight(), rotateRect.getWidth(), rotateRect
								.getHeight());

		// create selection lines points
		selectionLines.clear();
		if (yScale >= 0)
			selectionLines.add(createSelectionPoint(objectBounds.getCenterX(), objectBounds.getY()
						- getRotateRectangleSizeIncrease()));
		else
			selectionLines.add(createSelectionPoint(objectBounds.getCenterX(), objectBounds.getY()
						+ getRotateRectangleSizeIncrease()));
		selectionLines.add(createSelectionPoint(objectBounds.getCenterX(), objectBounds.getY()));
		selectionLines.add(createSelectionPoint(objectBounds.getX(), objectBounds.getY()));
		selectionLines.add(createSelectionPoint(objectBounds.getX(), objectBounds.getMaxY()));
		selectionLines.add(createSelectionPoint(objectBounds.getMaxX(), objectBounds.getMaxY()));
		selectionLines.add(createSelectionPoint(objectBounds.getMaxX(), objectBounds.getY()));
		selectionLines.add(createSelectionPoint(objectBounds.getCenterX(), objectBounds.getY()));
	}

	private Rectangle2D createSelectionRactangle(double x, double y)
	{
		Point2D transformedPoint = TransformUtils.getTransformedPoint(affineTransform, x, y);
		int top = (int) Math.round(transformedPoint.getX() - selectionRectangleSize2);
		int left = (int) Math.round(transformedPoint.getY() - selectionRectangleSize2);
		Rectangle2D scaleRectangle = new Rectangle2D.Double(top, left, selectionRectangleSize,
					selectionRectangleSize);
		return scaleRectangle;
	}

	private Point createSelectionPoint(double x, double y)
	{
		Point2D transformedPoint = TransformUtils.getTransformedPoint(affineTransform, x, y);
		return new Point((int) Math.round(transformedPoint.getX()), (int) Math.round(transformedPoint
					.getY()));
	}

	// private void paintSelection(Graphics2D g2d)
	// {
	// Rectangle2D objectBounds = getContentBounds();
	// g2d.setPaint(selectionRectangleColor);
	// paintSelectionRectangle(g2d, objectBounds.getX(), objectBounds.getY());
	// paintSelectionRectangle(g2d, objectBounds.getX(), objectBounds.getMaxY());
	// paintSelectionRectangle(g2d, objectBounds.getMaxX(), objectBounds.getMaxY());
	// paintSelectionRectangle(g2d, objectBounds.getMaxX(), objectBounds.getY());
	// }
	//
	// private void paintSelectionRectangle(Graphics2D g2d, double x, double y)
	// {
	// Point2D transformedPoint = TransformUtils.getTransformedPoint(affineTransform, x, y);
	// int top = (int) Math.round(transformedPoint.getX() - selectionRectangleSize2);
	// int left = (int) Math.round(transformedPoint.getY() - selectionRectangleSize2);
	// AffineTransform oldTransform = g2d.getTransform();
	// g2d.rotate(rotation, transformedPoint.getX(), transformedPoint.getY());
	// g2d.fillRect(top, left, selectionRectangleSize, selectionRectangleSize);
	// g2d.setTransform(oldTransform);
	// }

	// private String transformationMatrixToString(AffineTransform transform)
	// {
	// StringBuilder builder = new StringBuilder();
	// double[] matrix = new double[9];
	// transform.getMatrix(matrix);
	// for (int i = 0; i < matrix.length; i += 3)
	// builder.append(String.format("%.3f %.3f %.3f", matrix[i], matrix[i + 1], matrix[i + 2]));
	// return builder.toString();
	// }

	public Rectangle2D getUpdateRectangle()
	{
		if (drawedBounds == null)
			drawedBounds = getScreenUpdateRectangle();
		else
			drawedBounds.add(getScreenUpdateRectangle());
		return drawedBounds;
	}

	public boolean isContentUnderMouse(int x, int y)
	{
		Point virtualMouseLocation = getObjectMousePoint(x, y);
		boolean underMouse = contentUnderMouse(virtualMouseLocation.x, virtualMouseLocation.y,
					penSize);
		if (!underMouse && selected)
		{
			// object is not under mouse, but the object is selected
			// so selection rectangles may still be under mouse
			underMouse = getScaleRectangleUnderMouse(x, y) != null;
			if (!underMouse)
				underMouse = isRotateRectangleUnderMouse(x, y);
		}
		return underMouse;
	}

	public Point getObjectMousePoint(int xMouseScreen, int yMouseScreen)
	{
		setupAffineTransformation();
		return TransformUtils.getInverseTransformedPoint(affineTransform, xMouseScreen, yMouseScreen);
	}

	@Override
	public boolean isContentInRectangle(Rectangle rectangle)
	{
		Rectangle2D contentBounds = getContentBounds();
		if (!isPointInScreenRectangle(rectangle, contentBounds.getMinX(), contentBounds.getMinY()))
			return false;
		if (!isPointInScreenRectangle(rectangle, contentBounds.getMaxX(), contentBounds.getMinY()))
			return false;
		if (!isPointInScreenRectangle(rectangle, contentBounds.getMaxX(), contentBounds.getMaxY()))
			return false;
		if (!isPointInScreenRectangle(rectangle, contentBounds.getMinX(), contentBounds.getMaxY()))
			return false;
		return true;
	}

	private boolean isPointInScreenRectangle(Rectangle rectangle, double x, double y)
	{
		Point2D screenPoint = TransformUtils.getTransformedPoint(affineTransform, x, y);
		// System.out.println("(" + x + "," + y + ") -> (" + screenPoint.getX() + ","
		// + screenPoint.getY() + ") in " + rectangle.contains(screenPoint));
		return rectangle.contains(screenPoint);
	}

	public ScaleRectangle getScaleRectangleUnderMouse(int x, int y)
	{
		if (selected)
		{
			for (ScaleRectangle scaleRectangle : scaleRectangles)
				if (scaleRectangle.getRectangle().contains(x, y))
					return scaleRectangle;
		}
		return null;
	}

	public boolean isRotateRectangleUnderMouse(int x, int y)
	{
		if (rotateRectangle == null)
			return false;
		return rotateRectangle.contains(x, y);
	}

	public Point2D correctMouseLocation(double x, double y)
	{
		setupAffineTransformation();
		return TransformUtils.getTransformedPoint(affineTransform, x, y);
	}

	@Override
	public void repaint()
	{
		Rectangle2D updateRectangle2D = getUpdateRectangle();
		Rectangle updateRectangle = getSurrondingRectangle(updateRectangle2D);
		// System.out.println("updateRectangle2D: " + updateRectangle2D + "\nupdateRectangle: "
		// + updateRectangle);
		getWhiteboardPanel().repaint(updateRectangle);
	}

	private Rectangle getSurrondingRectangle(Rectangle2D rectangle2D)
	{
		int left = (int) Math.floor(rectangle2D.getMinX());
		int top = (int) Math.floor(rectangle2D.getMinY());
		int right = (int) Math.ceil(rectangle2D.getMaxX());
		int bottom = (int) Math.ceil(rectangle2D.getMaxY());
		int x = Math.min(left, right);
		int y = Math.min(top, bottom);
		int width = Math.abs(right - left);
		int height = Math.abs(top - bottom);
		return new Rectangle(x, y, width, height);
	}

	@Override
	public void repaint(int x, int y, int width, int height)
	{
		Rectangle2D screenRectangle = getScreenUpdateRectangle(x, y, width, height);
		Rectangle updateRectangle = getSurrondingRectangle(screenRectangle);
		// System.out.println("updateRectangle: " + updateRectangle);
		getWhiteboardPanel().repaint(updateRectangle);
	}

	@Override
	public void repaint(boolean panel)
	{
		if (panel)
			getWhiteboardPanel().repaint();
		else
			repaint();
	}

	public Element getStatus()
	{
		Element status = new Element(XmlNames.whiteboardContainer);
		status.setAttribute(XmlNames.type, getType());
		status.setAttribute(XmlNames.id, getId());
		status.addContent(getContainerStatus());
		Element content = new Element(XmlNames.content);
		status.addContent(content);
		content.addContent(getContentStatus());
		return status;
	}

	abstract String getType();

	public Element getContainerStatus()
	{
		Element status = new Element(XmlNames.container);
		status.setAttribute(XmlNames.type, getType());
		status.setAttribute(XmlNames.id, getId());
		XmlUtils.addXmlTag(status, XmlNames.xOffset, getXOffset());
		XmlUtils.addXmlTag(status, XmlNames.yOffset, getYOffset());
		XmlUtils.addXmlTag(status, XmlNames.rotation, getRotation());
		XmlUtils.addXmlTag(status, XmlNames.xScale, getXScale());
		XmlUtils.addXmlTag(status, XmlNames.yScale, getYScale());
		XmlUtils.addXmlTag(status, XmlNames.xShear, getXShear());
		XmlUtils.addXmlTag(status, XmlNames.yShear, getYShear());
		XmlUtils.addXmlTag(status, XmlNames.penSize, getPenSize());
		XmlUtils.addXmlTag(status, XmlNames.lineColor, getLineColor());
		XmlUtils.addXmlTag(status, XmlNames.fillColor, getFillColor());
		XmlUtils.addXmlTag(status, XmlNames.scaleIndependentPenSize, isScaleIndependentPenSize());
		// XmlUtils.addXmlTag(status, XmlNames.selected, isSelected());
		XmlUtils.addXmlTag(status, XmlNames.locked, isLocked());
		XmlUtils.addXmlTag(status, XmlNames.creationTime, getCreationTime());
		return status;
	}

	public abstract Element getContentStatus();

	public void setStatus(Element status)
	{
		setContainerStatus(status.getChild(XmlNames.container));
		setContentStatus(XmlUtils.getFirstChild(status.getChild(XmlNames.content)));
		calculateScaleOffsetCompensation();
		sendWhiteboardContainerChangedEvent();
		repaint();
	}

	private void setContainerStatus(Element containerStatus)
	{
		String xmlType = containerStatus.getAttributeValue(XmlNames.type);
		String xmlId = containerStatus.getAttributeValue(XmlNames.id);
		if (getId() == null)
		{
			id = xmlId;
		}
		else
		{
			if (!getType().equals(xmlType))
			{
				logger.warning("different types, expected " + getType() + ", but in xml " + xmlType);
			}
			if (!getId().equals(xmlId))
			{
				logger.warning("different ids, expected " + getId() + ", but in xml " + xmlId);
			}
			// repaint();
		}
		xOffset = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.xOffset);
		yOffset = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.yOffset);
		rotation = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.rotation);
		xScale = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.xScale);
		yScale = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.yScale);
		xShear = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.xShear);
		yShear = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.yShear);
		penSize = XmlUtils.getDoubleValueFromXmlTag(containerStatus, XmlNames.penSize);
		lineColor = XmlUtils.getColorValueFromXmlTag(containerStatus, XmlNames.lineColor);
		fillColor = XmlUtils.getColorValueFromXmlTag(containerStatus, XmlNames.fillColor);
		scaleIndependentPenSize = XmlUtils.getBooleanValueFromXmlTag(containerStatus,
					XmlNames.scaleIndependentPenSize);
		// selected = XmlUtils.getBooleanValueFromXmlTag(containerStatus, XmlNames.selected);
		locked = XmlUtils.getBooleanValueFromXmlTag(containerStatus, XmlNames.locked);
		setCreationTime(XmlUtils.getLongValueFromXmlTag(containerStatus, XmlNames.creationTime));
	}

	public abstract void setContentStatus(Element contentStatus);

	public String getId()
	{
		return id;
	}

}
