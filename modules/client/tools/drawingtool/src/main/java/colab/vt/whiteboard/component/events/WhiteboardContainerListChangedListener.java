package colab.vt.whiteboard.component.events;

public interface WhiteboardContainerListChangedListener
{
	void whiteboardPanelLoaded(WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent);

	void whiteboardContainersLoaded(WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent);

	void whiteboardContainersCleared(WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent);

	void whiteboardContainerAdded(WhiteboardContainerChangedEvent whiteboardContainerChangedEvent);

	void whiteboardContainerDeleted(WhiteboardContainerChangedEvent whiteboardContainerChangedEvent);

}
