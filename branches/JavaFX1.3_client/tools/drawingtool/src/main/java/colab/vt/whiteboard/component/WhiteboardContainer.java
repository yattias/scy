package colab.vt.whiteboard.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jdom.Element;

import colab.vt.whiteboard.component.events.WhiteboardContainerChangedListener;
import colab.vt.whiteboard.component.state.WhiteboardContainerInfoDisplay;

public interface WhiteboardContainer extends Serializable
{
	void addWhiteboardContainerChangedListener(
				WhiteboardContainerChangedListener whiteboardContainerChangedListener);

	void removeWhiteboardContainerChangedListener(
				WhiteboardContainerChangedListener whiteboardContainerChangedListener);

	void sendWhiteboardContainerChangedEvent();

	WhiteboardPanel getWhiteboardPanel();

	WhiteboardContainerInfoDisplay getWhiteboardContainerInfoDisplay();
	
	String getToolTipText(MouseEvent event);

	void paint(Graphics2D g);

	void repaint();

	void repaint(int x, int y, int width, int height);

	void repaint(boolean panel);

	Rectangle2D getScreenUpdateRectangle();
	
	Point getObjectMousePoint(int xMouseScreen, int yMouseScreen);

	boolean isContentUnderMouse(int x, int y);
	
	boolean isContentInRectangle(Rectangle rectangle);

	ScaleRectangle getScaleRectangleUnderMouse(int x, int y);

	boolean isRotateRectangleUnderMouse(int x, int y);

	Rectangle2D getContentBounds();

	void dispose();
	
	long getCreationTime();
	
	void setCreationTime(long time);

	double getXOffset();

	void setXOffset(double xOffset);

	double getYOffset();

	void setYOffset(double yOffset);

	double getRotation();

	void setRotation(double rotation);

	double getRotationDegrees();

	void setRotationDegrees(double degrees);

	double getXScale();

	void setXScale(double xScale);

	double getYScale();

	void setYScale(double yScale);

	double getXShear();

	void setXShear(double xShear);

	double getYShear();

	void setYShear(double yShear);

	Color getLineColor();

	void setLineColor(Color lineColor);

	Color getFillColor();

	void setFillColor(Color fillColor);
	
	AlphaComposite getAlphaComposite();
	
	void setAlphaComposite(AlphaComposite alphaComposite);

	double getPenSize();

	void setPenSize(double penSize);

	boolean isScaleIndependentPenSize();

	void setScaleIndependentPenSize(boolean scaleIndependentPenSize);

	boolean isSelected();

	void setSelected(boolean selected);

	double getMinimumScale();

	double getMaximumScale();

	// debug options

	double getXScaleOffsetCompensation();

	void setXScaleOffsetCompensation(double scaleOffsetCompensation);

	double getYScaleOffsetCompensation();

	void setYScaleOffsetCompensation(double scaleOffsetCompensation);

	double[] getTransformMatrix();
	
	void setLocked(boolean locked);
	
	boolean isLocked();

	String getId();

	Element getStatus();

	void setStatus(Element status);

}
