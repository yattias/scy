package colab.vt.whiteboard.component.selection;

import java.awt.event.MouseEvent;
import java.io.Serializable;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardContainer;

public interface DragEditAction extends Serializable
{
	void startEvent(WhiteboardPanel WhiteboardPanel, WhiteboardContainer mainWhiteboardContainer,
				MouseEvent mouseEvent);

	void prepareAction();

	void doAction(MouseEvent mouseEvent);

	void finishAction(MouseEvent mouseEvent);
}
