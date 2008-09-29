package eu.scy.lab.client.desktop.tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;

import eu.scy.lab.client.desktop.Desktop;
import eu.scy.lab.client.tools.map.MapTool;

class Tools extends TreePanel {

	public Tools(final Desktop desktop) {

		TreeNode root = new TreeNode("SCY Toolbox");

		TreeNode drawing = new TreeNode("Drawing");
		drawing.setExpanded(true);

		TreeNode jDT = new TreeNode("JDT");
		jDT.setExpanded(true);
		TreeNode paint = new TreeNode("alternativ Paint");
		paint.setExpanded(true);

		drawing.appendChild(jDT);
		drawing.appendChild(paint);

		TreeNode simulation = new TreeNode("Simulation");
		simulation.setExpanded(true);

		TreeNode co2sim = new TreeNode("CO2-Simulation");
		co2sim.setExpanded(true);

		simulation.appendChild(co2sim);

		TreeNode mapTool = new TreeNode("Map");
		mapTool.addListener(new TreeNodeListenerAdapter() {
		    public void onDblClick(Node node, EventObject e) {
		    	if (!desktop.getWorkspace().containsComponentID(MapTool.ID)) {
		    		GWT.log("Activating tool: MapTool", null);
			    	final MapTool map = new MapTool();
			    	desktop.getWorkspace().add(map);
			    	// FIXME: This is a rather ugly hack to work around issues
			    	// adding a MapWidget into a gwt-ext Panel
			    	// see http://code.google.com/p/gwt-google-apis/issues/detail?id=127
			    	Timer t = new Timer() {
						@Override
						public void run() {
							map.init();
						}
			    	};
			    	t.schedule(500);
		    	} else {
		    		GWT.log("Tool already loaded: MapTool", null);
		    	}
		    	desktop.getWorkspace().activate(MapTool.ID);
		    }
		});

		root.appendChild(drawing);
		root.appendChild(simulation);
		root.appendChild(mapTool);

		setRootVisible(false);

		setTitle("Tools");
		// setWidth(200);
		// setHeight(400);
		setRootNode(root);
		root.setExpanded(true);
	}
}