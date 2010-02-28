package eu.scy.scymapper.impl.ui.diagram.modes;

import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.diagram.RichNodeView;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 03.nov.2009
 * Time: 15:10:18
 */
public class DragMode implements IDiagramMode {
	private ConceptDiagramView view;

	private final MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getComponent() instanceof NodeViewComponent) {
				e.getComponent().setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Component com = e.getComponent();
			com.getParent().setComponentZOrder(com, 0);
			if (!e.isControlDown()) view.getSelectionModel().clearSelection();
			view.getSelectionModel().select(((RichNodeView) com).getModel());
		}
	};
	private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
		public Point relativePos;

		@Override
		public void mouseDragged(MouseEvent e) {

			if (relativePos == null) {
				relativePos = e.getPoint();
				e.getComponent().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						relativePos = null;
						e.getComponent().removeMouseListener(this);
					}
				});
			}

			// The relative mouse position from the component x,y
			Point relPoint = e.getPoint();

			RichNodeView view = (RichNodeView) e.getSource();

			// Create the new location
			Point newLocation = view.getLocation();
			// Translate the newLocation with the relative point
			newLocation.translate(relPoint.x, relPoint.y);
			newLocation.translate(-relativePos.x, -relativePos.y);

			//TODO: Use controller instead
			if (view.getModel().getConstraints().getCanMove()) view.getController().setLocation(newLocation);
		}
	};

	private FocusListener focusHandler = new FocusAdapter() {
	};

	public DragMode(ConceptDiagramView view) {
		this.view = view;

	}

	@Override
	public MouseListener getMouseListener() {
		return mouseListener;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public MouseMotionListener getMouseMotionListener() {
		return mouseMotionListener;
	}

	@Override
	public FocusListener getFocusListener() {
		return focusHandler;
	}


}
