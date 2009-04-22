package colab.vt.whiteboard.component.selection;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.WhiteboardObjectContainer;
import colab.vt.whiteboard.component.WhiteboardRectangle;

public class DragSelectionAction extends AbstractDragEditAction
{
	private static final long serialVersionUID = 2666308962165001416L;
	private static final Color selectionRectangleLineColor = new Color(0, 0, 255, 224);
	private static final Color selectionRectangleFillColor = new Color(0, 0, 255, 32);

	private WhiteboardContainer dragSelectionWhiteboardObjectContainer = null;
	private WhiteboardRectangle dragSelectionRectangle = null;
	private ArrayList<WhiteboardContainer> handledContainers = new ArrayList<WhiteboardContainer>();

	@Override
	public void prepareAction()
	{
		dragSelectionRectangle = new WhiteboardRectangle();
		dragSelectionWhiteboardObjectContainer = new WhiteboardObjectContainer(getWhiteboardPanel(),
					dragSelectionRectangle);
		getWhiteboardPanel().setTemporaryWhiteboardContainer(dragSelectionWhiteboardObjectContainer);
		dragSelectionWhiteboardObjectContainer.setPenSize(1.0);
		dragSelectionWhiteboardObjectContainer.setLineColor(selectionRectangleLineColor);
		dragSelectionWhiteboardObjectContainer.setFillColor(selectionRectangleFillColor);
		dragSelectionRectangle.setBegin(getStartMouseEvent().getX(), getStartMouseEvent().getY());
	}

	@Override
	public void doAction(MouseEvent mouseEvent)
	{
		dragSelectionWhiteboardObjectContainer.repaint();
		dragSelectionRectangle.setIntermediate(mouseEvent.getX(), mouseEvent.getY());
		selectWhiteboardContainersInsideDragSelectionRectangle();
		dragSelectionWhiteboardObjectContainer.repaint();
	}

	@Override
	public void finishAction(MouseEvent mouseEvent)
	{
		dragSelectionRectangle.setEnd(mouseEvent.getX(), mouseEvent.getY());
		selectWhiteboardContainersInsideDragSelectionRectangle();
		getWhiteboardPanel().makeTemporaryWhiteboardContainerFinal(
					dragSelectionWhiteboardObjectContainer, false);
		dragSelectionWhiteboardObjectContainer.repaint();
		dragSelectionRectangle = null;
		dragSelectionWhiteboardObjectContainer = null;
	}

	private void selectWhiteboardContainersInsideDragSelectionRectangle()
	{
		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel().getWhiteboardContainers())
		{
			boolean insideDragSelectionRangle = whiteboardContainer.isContentInRectangle(dragSelectionRectangle.getBounds());
//			System.out.println("container inside " + insideDragSelectionRangle);
			if (insideDragSelectionRangle != whiteboardContainer.isSelected())
			{
				if (!whiteboardContainer.isSelected())
				{
					whiteboardContainer.repaint();
					whiteboardContainer.setSelected(true);
					whiteboardContainer.repaint();
					if (!handledContainers.contains(whiteboardContainer))
						handledContainers.add(whiteboardContainer);
				}
				else
				{
					// only deselect if we have selected it
					if (handledContainers.contains(whiteboardContainer))
					{
						whiteboardContainer.repaint();
						whiteboardContainer.setSelected(false);
						whiteboardContainer.repaint();
					}
				}
//				System.out.println("container selected " + whiteboardContainer.isSelected());
			}
		}

	}
}
