package eu.scy.client.common.richtexteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import java.util.ResourceBundle;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
* Format toolbar for rich text editor component.
*/
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
        String text = editorPanel.getJTextPane().getSelectedText();
        if (text==null)
            text = "";
        if (e.getActionCommand().equals("bold")) {
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.BOLD,text,
                    editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Bold).toString());
		} else if (e.getActionCommand().equals("italics")) {
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.ITALIC,text,
                    editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Italic).toString());
		} else if (e.getActionCommand().equals("underline")) {
            if (editorPanel.getRichTextEditorLogger() != null) {
                // In some reasons undeline behaves differently from bold and italic
                // If at the end of document then getAttribute(StyleConstants.Underline)
                // returns null
                String underlineLogValue = "null";
                if (editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Underline)!=null)
                    underlineLogValue = editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Underline).toString();
//                underlineLogValue = String.valueOf(StyleConstants.isUnderline(editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes()));
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.UNDERLINE,text,underlineLogValue);
            }
		} else if (e.getActionCommand().equals("superscript")) {
    		RTFEditorKit rtfek=(RTFEditorKit)editorPanel.getJTextPane().getEditorKit();
			String in = (StyleConstants.isSuperscript(rtfek.getInputAttributes())) ? "true" : "false";
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.SUPERSCRIPT,text,in);
        } else if (e.getActionCommand().equals("subscript")) {
    			RTFEditorKit rtfek=(RTFEditorKit)editorPanel.getJTextPane().getEditorKit();
				String in = (StyleConstants.isSubscript(rtfek.getInputAttributes())) ? "true" : "false";
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.SUBSCRIPT,text,in);
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
                StyledDocument doc=(StyledDocument)editor.getDocument();
    			RTFEditorKit rtfek=(RTFEditorKit)editor.getEditorKit();
        		MutableAttributeSet attr=rtfek.getInputAttributes();
				boolean subscript = (StyleConstants.isSuperscript(rtfek
						.getInputAttributes())) ? false : true;
               	StyleConstants.setSuperscript(attr, subscript);
    			if(editor.getSelectedText()!=null && !editor.getSelectedText().equals(""))
        		{
            		int start=editor.getSelectionStart();
                	int end=editor.getSelectionEnd();
                    doc.setCharacterAttributes(start,(end-start),attr,false);
    			}
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
                StyledDocument doc=(StyledDocument)editor.getDocument();
    			RTFEditorKit rtfek=(RTFEditorKit)editor.getEditorKit();
        		MutableAttributeSet attr=rtfek.getInputAttributes();
				boolean subscript = (StyleConstants.isSubscript(rtfek
						.getInputAttributes())) ? false : true;
               	StyleConstants.setSubscript(attr, subscript);
    			if(editor.getSelectedText()!=null && !editor.getSelectedText().equals(""))
        		{
            		int start=editor.getSelectionStart();
                	int end=editor.getSelectionEnd();
                    doc.setCharacterAttributes(start,(end-start),attr,false);
    			}
            }
        }
	}
}
