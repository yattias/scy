package eu.scy.tools.math.ui.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXPanel;

public class ScratchPanel extends JXPanel {
	
	private String type;
	private JXEditorPane editor;

	public ScratchPanel(String type) {
		super(new MigLayout("fill, inset 0 0 0 0"));
		this.setType(type);
		init();
	}

	private void init() {
		setOpaque(false);
		setEditor(new JXEditorPane());
		getEditor().setText("...");
		getEditor().setPreferredSize(new Dimension(200, 50));
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

}
