package eu.scy.tools.math.controller;

import org.apache.commons.lang.StringUtils;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class SCYMathToolController extends MathToolController {

	private static final String MATH_TOOL_END = "</MathTool>";
	private static final String MATH_TOOL_START = "<MathTool>";
	public SCYMathToolController() {
		super();
	}

	public String save() {
		ShapeCanvas sc = shapeCanvases.get(UIUtils._2D);
	      
		String xml = xstream.toXML(sc.getMathShapes());
		
		xml = MATH_TOOL_START + xml + MATH_TOOL_END;
		return xml;
	}
	
	@Override
	public void open(String xml) {
		String removeStart = StringUtils.remove(xml, MATH_TOOL_START);
		String removeEnd = StringUtils.remove(xml, MATH_TOOL_END);
		
		super.open(removeEnd);
	}
	public void logAction(String actionLog) { 
		
	}
}
