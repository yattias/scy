package eu.scy.client.common.richtexteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import java.util.ResourceBundle;

public class RtfFileToolbar extends JToolBar implements ActionListener {

	private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RtfFileToolbar");
	private RichTextEditor editorPanel;

	public RtfFileToolbar(RichTextEditor richTextEditor) {
		super();
		this.editorPanel = richTextEditor;
		initUI();
	}

	public RichTextEditor getRichTextEditor() {
		return editorPanel;
	}

	private void initUI() {
		this.setOrientation(SwingConstants.HORIZONTAL);
		this.setFloatable(false);

		JButton button = new JButton(messages.getString("saveFile"));
		button.setActionCommand("savefile");
		button.addActionListener(this);
		this.add(button);

		button = new JButton(messages.getString("print"));
		button.setActionCommand("print");
		button.addActionListener(this);
		this.add(button);

		button = new JButton(messages.getString("pdf"));
		button.setActionCommand("pdf");
		button.addActionListener(this);
		this.add(button);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
/*
		if (e.getActionCommand().equals("addtextbox")) {
			Label newLabel = new Label(getSketchPanel().nextLabelId++, new Point(0,0), new Point(rand.nextInt(300), rand.nextInt(300)));
			newLabel.setText(editorPanel.getJTextPane().getSelectedText());
			this.editorPanel.getSacaPanel().getSketchPanel().addLabel(newLabel);
			this.editorPanel.getSacaPanel().getActionLogger().logAddLabelAction(newLabel);
			updatePreview();
			JOptionPane.showMessageDialog(editorPanel, Config.getInstance().get("new_box"));
		} else if (e.getActionCommand().equals("bold")) {
			IAction action = ((TextLogger)editorPanel.getActionLogger()).createBasicAction("text_bold");
			action.addAttribute("text", editorPanel.getJTextPane().getSelectedText());
			((TextLogger)editorPanel.getActionLogger()).getLogger().log(action);
		} else if (e.getActionCommand().equals("italics")) {
			IAction action = ((TextLogger)editorPanel.getActionLogger()).createBasicAction("text_italics");
			action.addAttribute("text", editorPanel.getJTextPane().getSelectedText());
			((TextLogger)editorPanel.getActionLogger()).getLogger().log(action);
		} else if (e.getActionCommand().equals("underline")) {
			IAction action = ((TextLogger)editorPanel.getActionLogger()).createBasicAction("text_underline");
			action.addAttribute("text", editorPanel.getJTextPane().getSelectedText());
			((TextLogger)editorPanel.getActionLogger()).getLogger().log(action);
		} else if (e.getActionCommand().equals("superscript")) {
			IAction action = ((TextLogger)editorPanel.getActionLogger()).createBasicAction("text_superscript");
			action.addAttribute("text", editorPanel.getJTextPane().getSelectedText());
			((TextLogger)editorPanel.getActionLogger()).getLogger().log(action);
		} else if (e.getActionCommand().equals("subscript")) {
			IAction action = ((TextLogger)editorPanel.getActionLogger()).createBasicAction("text_subscript");
			action.addAttribute("text", editorPanel.getJTextPane().getSelectedText());
			((TextLogger)editorPanel.getActionLogger()).getLogger().log(action);
		}
*/
	}
}
