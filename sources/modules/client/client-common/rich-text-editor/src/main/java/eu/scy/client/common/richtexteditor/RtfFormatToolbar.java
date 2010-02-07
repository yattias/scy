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
import javax.swing.text.StyleConstants;

public class RtfFormatToolbar extends JToolBar implements ActionListener {
	private final String imagesLocation = "/eu/scy/client/common/richtexteditor/images/";
	private ImageIcon boldIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_bold.png"));
	private ImageIcon italicIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_italic.png"));
	private ImageIcon underlineIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_underline.png"));
	private ImageIcon superIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sup_letter.png"));
	private ImageIcon subIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sub_letter.png"));
    private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RichTextEditor");

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
        int pos = editorPanel.getJTextPane().getSelectionStart();
        if (e.getActionCommand().equals("bold")) {
            editorPanel.getRichTextEditorLogger().logFormatAction(
                RichTextEditorLogger.BOLD,
                editorPanel.getJTextPane().getSelectedText(),
                editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Bold).toString());
		} else if (e.getActionCommand().equals("italics")) {
            editorPanel.getRichTextEditorLogger().logFormatAction(
                RichTextEditorLogger.ITALIC,
                editorPanel.getJTextPane().getSelectedText(),
                editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Italic).toString());
		} else if (e.getActionCommand().equals("underline")) {
            editorPanel.getRichTextEditorLogger().logFormatAction(
                RichTextEditorLogger.UNDERLINE,
                editorPanel.getJTextPane().getSelectedText(),
                editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Underline).toString());
		} else if (e.getActionCommand().equals("superscript")) {
            editorPanel.getRichTextEditorLogger().logFormatAction(
                RichTextEditorLogger.SUPERSCRIPT,
                editorPanel.getJTextPane().getSelectedText(),
                editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Superscript).toString());
		} else if (e.getActionCommand().equals("subscript")) {
            editorPanel.getRichTextEditorLogger().logFormatAction(
                RichTextEditorLogger.SUBSCRIPT,
                editorPanel.getJTextPane().getSelectedText(),
                editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Subscript).toString());
		}
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
}
