package eu.scy.lab.client.desktop.tools;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.tree.TreePanel;

import eu.scy.lab.client.desktop.Desktop;

public class ToolsTreeNavigation   {  

	private Panel panel;

	public ToolsTreeNavigation(Desktop desktop) {
		panel = new Panel();  
		panel.setBorder(false);  
		panel.setPaddings(5);

		panel.setAutoHeight(true);
		panel.setAutoWidth(true);

		final TreePanel treePanel = new Tools(desktop);  
		treePanel.setHeader(false);
		treePanel.setBorder(false);

		panel.add(treePanel);  
	}
	
	public Panel getPanel(){
		return  this.panel;
	}

}  