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

	private final MouseAdapter mouseAdapter = new MouseAdapter() {

		public Point relativePos;
		
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

		@Override
		public void mouseDragged(MouseEvent e) {
			if(relativePos == null) {
				relativePos = e.getPoint();
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
//			if (view.getModel().getConstraints().getCanMove()) view.getController().setLocation(newLocation);
			if (view.getModel().getConstraints().getCanMove()) view.getModel().setLocation(newLocation);
		}
		
		public void mouseReleased(MouseEvent e) {
         if (relativePos==null){
            // mouse released without dragging, so no need to do anything
            return;
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
			relativePos = null;
		};
		
	};

	private FocusListener focusHandler = new FocusAdapter() {
	};

	public DragMode(ConceptDiagramView view) {
		this.view = view;

	}

	@Override
	public MouseListener getMouseListener() {
		return mouseAdapter;
	}

	@Override
	public MouseMotionListener getMouseMotionListener() {
		return mouseAdapter;
	}

	@Override
	public FocusListener getFocusListener() {
		return focusHandler;
	}


}
