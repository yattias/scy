package eu.scy.tools.math.adapters;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JLabel;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.dnd.DnDUtils;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class ShapeJXLabelDropTargetListener implements DropTargetListener {

	private ShapeCanvas shapeCanvas;
	private MathToolController mathToolController;
	private String type;

	public ShapeJXLabelDropTargetListener(ShapeCanvas shapeCanvas) {
		this.shapeCanvas = shapeCanvas;
	}

	public ShapeJXLabelDropTargetListener(
			MathToolController mathToolController, String type) {
		this.mathToolController = mathToolController;
		this.type = type;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
//		DnDUtils.debugPrintln("dragEnter, drop action = "
//				+ DnDUtils.showActions(dtde.getDropAction()));

	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
//		DnDUtils.debugPrintln("DropTarget dragExit");

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
//		DnDUtils.debugPrintln("DropTarget dragOver, drop action = "
//				+ DnDUtils.showActions(dtde.getDropAction()));

	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
//		DnDUtils.debugPrintln("DropTarget dropActionChanged, drop action = "
//				+ DnDUtils.showActions(dtde.getDropAction()));
		
		Transferable transferable = dtde.getTransferable();
		try {
			if( dropComponent(transferable, dtde.getLocation()) ) {
				dtde.acceptDrop(dtde.getDropAction());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
//		DnDUtils.debugPrintln("DropTarget DropTargetDragEvent, drop action = "
//				+ DnDUtils.showActions(dtde.getDropAction()));

	}

	protected boolean dropComponent(Transferable transferable, Point dropPoint)
			throws IOException, UnsupportedFlavorException {
		Object o = transferable.getTransferData(transferable.getTransferDataFlavors()[0]);
		if (o instanceof Component) {
//			DnDUtils.debugPrintln("Dragged component class is "
//					+ o.getClass().getName());
			
			if( o instanceof JLabel) {
				mathToolController.addShape((JLabel)o, dropPoint,type);
			}
			
			
			return true;
		}
		return false;
	}
}
