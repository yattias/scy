package eu.scy.tools.dnd;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.tools.planning.StudentPlanningTool;

public class JXDropTargetListener implements DropTargetListener {

	private StudentPlanningTool stp;

	public JXDropTargetListener(StudentPlanningTool studentPlanningTool) {
		
		this.stp = studentPlanningTool;
		
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		DnDUtils.debugPrintln("dragEnter, drop action = "
				+ DnDUtils.showActions(dtde.getDropAction()));

	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		DnDUtils.debugPrintln("DropTarget dragExit");

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		DnDUtils.debugPrintln("DropTarget dragOver, drop action = "
				+ DnDUtils.showActions(dtde.getDropAction()));

	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		DnDUtils.debugPrintln("DropTarget dropActionChanged, drop action = "
				+ DnDUtils.showActions(dtde.getDropAction()));
		dtde.acceptDrop(dtde.getDropAction());
		Transferable transferable = dtde.getTransferable();
		try {
			dropComponent(transferable);
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
		DnDUtils.debugPrintln("DropTarget DropTargetDragEvent, drop action = "
				+ DnDUtils.showActions(dtde.getDropAction()));

	}

	protected boolean dropComponent(Transferable transferable)
			throws IOException, UnsupportedFlavorException {
		Object o = transferable.getTransferData(transferable.getTransferDataFlavors()[0]);
		if (o instanceof Component) {
			DnDUtils.debugPrintln("Dragged component class is "
					+ o.getClass().getName());
			
			if( o instanceof JXLabel) {
				IAwarenessUser  u = (IAwarenessUser) ((JXLabel)o).getClientProperty("USER");
				stp.acceptDrop(u);
			}
			
			
			return true;
		}
		return false;
	}
}
