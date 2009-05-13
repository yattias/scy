package colab.vt.whiteboard.component;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import org.jdom.Element;

public interface WhiteboardObject extends Serializable
{
	Rectangle getBounds();

	boolean objectUnderMouse(int x, int y, double withPenSize);
	
	void paint(Graphics g);

	String getType();

	String getDescription();
	
	String getToolTipText(MouseEvent event, Point mouseLocation);

	Element getStatus();

	void setStatus(Element status);
}
