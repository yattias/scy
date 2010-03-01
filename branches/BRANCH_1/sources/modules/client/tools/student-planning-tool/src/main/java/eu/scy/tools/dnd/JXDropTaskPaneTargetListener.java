package eu.scy.tools.dnd;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import eu.scy.core.model.impl.pedagogicalplan.ActivityImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.tools.planning.StudentPlanningTool;

public class JXDropTaskPaneTargetListener implements DropTargetListener {
	
	private StudentPlanningTool spt;

	public JXDropTaskPaneTargetListener(
			StudentPlanningTool studentPlanningToolMain) {
		this.spt = studentPlanningToolMain;
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
			
			if( ((Component) o).getName().contains("elo") ) {
				
				LearningActivitySpace planning = new LearningActivitySpaceImpl();
		        planning.setName(((Component) o).getName());


		        Activity firstActivity = new ActivityImpl();
		        firstActivity.setName("Gather in the big hall and listen to your teacher");
		        planning.addActivity(firstActivity);

		        Activity conceptMappingSession = new  ActivityImpl();
		        conceptMappingSession.setName("Concept mapping");
		        planning.addActivity(conceptMappingSession);

		        AnchorELO conceptMap = new AnchorELOImpl();
		        conceptMap.setName("Expected concept map");
		        conceptMappingSession.setAnchorELO(conceptMap);

		        LearningActivitySpace lastSpace = new LearningActivitySpaceImpl();
		        lastSpace.setName("Evaluation");
		        conceptMap.setInputTo(lastSpace);

		      
		        //StudentPlannedActivity spa = new 
		        
				//spt.addTaskContainer(spt.createAnchorELOPanel(firstActivity));
				return true;
			}
			
		}
		return false;
	}
}
