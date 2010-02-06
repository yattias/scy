package eu.scy.client.common.richtexteditor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.StyledEditorKit.StyledTextAction;
import javax.swing.text.rtf.RTFEditorKit;
import java.util.ResourceBundle;

public class RtfFormatToolbar extends JToolBar implements ActionListener {

	private final String imagesLocation = "/eu/scy/client/common/richtexteditor/images/";

	private ImageIcon boldIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_bold.png"));
	private ImageIcon italicIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_italic.png"));
	private ImageIcon underlineIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_underline.png"));
	private ImageIcon superIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sup_letter.png"));
	private ImageIcon subIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sub_letter.png"));
	private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RtfFormatToolbar");

	private RichTextEditor editorPanel;

	public RtfFormatToolbar(RichTextEditor richTextEditor) {
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

		JButton button = new JButton(boldIcon);
		button.setActionCommand("bold");
		button.addActionListener(this);
		button.addActionListener(new RTFEditorKit.BoldAction());
		button.setToolTipText(messages.getString("bold"));
		this.add(button);

		button = new JButton(italicIcon);
		button.setActionCommand("italics");
		button.addActionListener(this);
		button.addActionListener(new StyledEditorKit.ItalicAction());
		button.setToolTipText(messages.getString("italics"));
		this.add(button);

		button = new JButton(underlineIcon);
		button.setActionCommand("underline");
		button.addActionListener(this);
		button.addActionListener(new StyledEditorKit.UnderlineAction());
		button.setToolTipText(messages.getString("underline"));
		this.add(button);

		button = new JButton(superIcon);
		button.setActionCommand("superscript");
		button.addActionListener(this);
		button.addActionListener(new SuperscriptAction());
		button.setToolTipText(messages.getString("superscript"));
		this.add(button);

		button = new JButton(subIcon);
		button.setActionCommand("subscript");
		button.addActionListener(this);
		button.addActionListener(new SubscriptAction());
		button.setToolTipText(messages.getString("subscript"));
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

	public static class SuperscriptAction extends StyledEditorKit.StyledTextAction {
		public SuperscriptAction() {
			super(StyleConstants.Superscript.toString());
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			JEditorPane editor = getEditor(ae);
			if (editor != null) {
				StyledEditorKit kit = getStyledEditorKit(editor);
				boolean superscript = (StyleConstants.isSuperscript(kit
						.getInputAttributes())) ? false : true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setSuperscript(sas, superscript);
				setCharacterAttributes(editor, sas, false);
			}
		}

	}

	public static class SubscriptAction extends StyledEditorKit.StyledTextAction {
		public SubscriptAction() {
			super(StyleConstants.Subscript.toString());
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			JEditorPane editor = getEditor(ae);
			if (editor != null) {
				StyledEditorKit kit = getStyledEditorKit(editor);
				boolean subscript = (StyleConstants.isSubscript(kit
						.getInputAttributes())) ? false : true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setSubscript(sas, subscript);
				setCharacterAttributes(editor, sas, false);
			}
		}

	}

	public static class BackgroundAction extends StyledTextAction {

		private Color fg;

		public BackgroundAction(String nm, Color fg) {
			super(nm);
			this.fg = fg;
		}

		public void actionPerformed(ActionEvent e) {
			JEditorPane editor = getEditor(e);
			if (editor != null) {
				Color fg = this.fg;
				if ((e != null) && (e.getSource() == editor)) {
					String s = e.getActionCommand();
					try {
						fg = Color.decode(s);
					} catch (NumberFormatException nfe) {
					}
				}
				if (fg != null) {
					MutableAttributeSet attr = new SimpleAttributeSet();
					StyleConstants.setBackground(attr, fg);
					setCharacterAttributes(editor, attr, false);
				} else {
					UIManager.getLookAndFeel().provideErrorFeedback(editor);
				}
			}
		}

	}

	
}
