package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.jdom.Element;

public interface WhiteboardAction
{
	JToggleButton getToggleButton();

	JComponent getToolbarButton();

	Cursor getDefaultCursor();
	
	String getToolTipText(MouseEvent event);

	Element getStatus();

	void setStatus(Element status);
}
