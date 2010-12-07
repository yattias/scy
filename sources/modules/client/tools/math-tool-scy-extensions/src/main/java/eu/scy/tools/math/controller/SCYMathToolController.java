package eu.scy.tools.math.controller;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class SCYMathToolController extends MathToolController {

	public SCYMathToolController() {
		super();
	}

	public String save() {
		ShapeCanvas sc = shapeCanvases.get(UIUtils._2D);
	      
		String xml = xstream.toXML(sc.getMathShapes());
		
		xml = "<MathTool>" + xml + "</MathTool>";
		return xml;
	}
	
	public void logAction(String actionLog) { 
		
	}
}
