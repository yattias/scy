package eu.scy.lab.client.desktop.tools;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;



public class CopyOfToolsTreeNavigation extends TreePanel  {
	
	TreeNode drawing;
	TreeNode jDT;
	TreeNode simulation;
	TreeNode co2Simulation;
	
	public CopyOfToolsTreeNavigation(){
		super();
		
		final TreePanel treePanel = buildTreePanel();
		
	   TreeNode root = new TreeNode("Tools");  
	   TreeNode drawing = new TreeNode("Drawing");  
	   drawing.setExpanded(true);  
	   TreeNode jDT = new TreeNode("JDT");  
	   jDT.setExpanded(true);  
	   drawing.appendChild(jDT);  
	   
	   TreeNode simulation = new TreeNode("Simulation");  
	   drawing.setExpanded(true);  
	   TreeNode co2Simulation = new TreeNode("CO2 Simulation");  
	   co2Simulation.setExpanded(true);  
	   simulation.appendChild(co2Simulation);  
	  
	   setRootVisible(false);  
	   setWidth(200);  
	   setHeight(400);  
	   setRootNode(root);  
	   root.setExpanded(true);  
	   
	   
	
	}
	
	private TreePanel buildTreePanel() {

		TreePanel treePanel = new TreePanel();
		TreeNode root = new TreeNode("Tools");  
		   TreeNode drawing = new TreeNode("Drawing");  
		   drawing.setExpanded(true);  
		   TreeNode jDT = new TreeNode("JDT");  
		   jDT.setExpanded(true);  
		   drawing.appendChild(jDT);  
		   
		   TreeNode simulation = new TreeNode("Simulation");  
		   drawing.setExpanded(true);  
		   TreeNode co2Simulation = new TreeNode("CO2 Simulation");  
		   co2Simulation.setExpanded(true);  
		   simulation.appendChild(co2Simulation);  
		  
		   treePanel.setRootVisible(false);  
		   treePanel.setWidth(200);  
		   treePanel.setHeight(400);  
		   treePanel.setRootNode(root);  
		   root.setExpanded(true);  
		return treePanel;
	}

	public VerticalPanel createTreePanel(){
		VerticalPanel verticalPanel = new VerticalPanel();
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.add(this);
		
		return verticalPanel;
	}

}
