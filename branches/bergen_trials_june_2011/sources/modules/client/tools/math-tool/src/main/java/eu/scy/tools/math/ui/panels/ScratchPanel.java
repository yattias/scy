package eu.scy.tools.math.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXPanel;

import eu.scy.tools.math.controller.MathToolController;

public class ScratchPanel extends JXPanel {
	
	private String type;
	private JXEditorPane editor;
	private MathToolController mathToolController;
	private String calculation;
	private String result;

	public ScratchPanel(String type, MathToolController mathToolController) {
		super(new MigLayout("fill, inset 0 0 0 0"));
		this.setType(type);
		this.mathToolController = mathToolController;
		init();
	}

	private void init() {
		setOpaque(false);
		setEditor(new JXEditorPane());
		getEditor().setText("....");
		getEditor().setPreferredSize(new Dimension(200, 50));
		getEditor().addFocusListener(new FocusListener() {
			
			Calculator calculator;
			
			@Override
			public void focusLost(FocusEvent e) {
//				// TODO Auto-generated method stub
//				//clear calc
//				if(StringUtils.stripToNull(calculator.getForumla()) != null ) {
//					setExpression(calculator.getForumla());
//				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// System.out
//						.println("ScratchPanel.init().new FocusListener() {...}.focusGained()");
				//deselect all shapes
				//clear calc and add old calc
				calculator = mathToolController.getCalculators().get(type);
				calculator.clearForumla();
				calculator.setForumla(getExpression());
				mathToolController.setSelectedShape(type);
				calculator.setResultValue(getResult());
//				mathToolController.selectAllShapes(false, type);
//				
//				mathToolController.setSelectedMathShape(null);
				
				if(StringUtils.stripToNull(getExpression()) != null ) {
					calculator.setForumla(getExpression());
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(getEditor());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane,"grow");
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setEditor(JXEditorPane editor) {
		this.editor = editor;
	}

	public JXEditorPane getEditor() {
		return editor;
	}

	public void setExpression(String calculation) {
		this.calculation = calculation;
	}

	public String getExpression() {
		return calculation;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return this.result;
	}

}
