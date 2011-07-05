package eu.scy.client.common.richtexteditor;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
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
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
* Format toolbar for rich text editor component.
*/
public class RtfFormatToolbar extends JToolBar implements ActionListener {
    public static final String BOLD_ACTION = "bold";
    public static final String ITALIC_ACTION = "italics";
    public static final String UNDERLINE_ACTION = "underline";
    public static final String SUBSCRIPT_ACTION = "subscript";
    public static final String SUPERSCRIPT_ACTION = "superscript";
    private final String imagesLocation = "/eu/scy/client/common/richtexteditor/images/";
    private ImageIcon boldIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_bold.png"));
    private ImageIcon italicIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_italic.png"));
    private ImageIcon underlineIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_underline.png"));
    private ImageIcon superIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sup_letter.png"));
    private ImageIcon subIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sub_letter.png"));
    private ImageIcon boldIconIn = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_bold_in.png"));
    private ImageIcon italicIconIn = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_italic_in.png"));
    private ImageIcon underlineIconIn = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_underline_in.png"));
    private ImageIcon superIconIn = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sup_letter_in.png"));
    private ImageIcon subIconIn = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_sub_letter_in.png"));
    private ResourceBundleWrapper messages = new ResourceBundleWrapper(this);
    private JButton boldButton = new JButton(boldIcon);
    private JButton italicButton = new JButton(italicIcon);
    private JButton underlineButton = new JButton(underlineIcon);
    private JButton superButton = new JButton(superIcon);
    private JButton subButton = new JButton(subIcon);

    private RichTextEditor editorPanel;

    /**
    * Creates format toolbar for rich text editor component
    * @param richTextEditor reference to main component
    */
    public RtfFormatToolbar(RichTextEditor richTextEditor) {
        super();
        this.editorPanel = richTextEditor;
        initUI();
    }

    /**
     * Gets rich text editor main component
     * @return rich text editor main component
     */
    public RichTextEditor getRichTextEditor() {
        return editorPanel;
    }

    /*
     * Initializes user interface.
     */
    private void initUI() {
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.setFloatable(false);

        boldButton.setActionCommand(BOLD_ACTION);
        boldButton.addActionListener(this);
        boldButton.addActionListener(new RTFEditorKit.BoldAction());
        boldButton.setToolTipText(messages.getString("bold"));
        this.add(boldButton);

        italicButton = new JButton(italicIcon);
        italicButton.setActionCommand(ITALIC_ACTION);
        italicButton.addActionListener(this);
        italicButton.addActionListener(new StyledEditorKit.ItalicAction());
        italicButton.setToolTipText(messages.getString("italics"));
        this.add(italicButton);

        underlineButton = new JButton(underlineIcon);
        underlineButton.setActionCommand(UNDERLINE_ACTION);
        underlineButton.addActionListener(this);
        underlineButton.addActionListener(new StyledEditorKit.UnderlineAction());
        underlineButton.setToolTipText(messages.getString("underline"));
        this.add(underlineButton);

        superButton = new JButton(superIcon);
        superButton.setActionCommand(SUPERSCRIPT_ACTION);
        superButton.addActionListener(this);
        superButton.addActionListener(new SuperscriptAction());
        superButton.setToolTipText(messages.getString("superscript"));
        this.add(superButton);

        subButton = new JButton(subIcon);
        subButton.setActionCommand(SUBSCRIPT_ACTION);
        subButton.addActionListener(this);
        subButton.addActionListener(new SubscriptAction());
        subButton.setToolTipText(messages.getString("subscript"));
        this.add(subButton);

    }

    /*
     * Implementation of ActionListener interface.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int pos = editorPanel.getJTextPane().getSelectionStart();
        String text = editorPanel.getJTextPane().getSelectedText();
        RTFEditorKit rtfek=(RTFEditorKit)editorPanel.getJTextPane().getEditorKit();
        if (text==null)
            text = "";
        if (e.getActionCommand().equals(BOLD_ACTION)) {
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.BOLD,text,
                    editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Bold).toString());
        } else if (e.getActionCommand().equals(ITALIC_ACTION)) {
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.ITALIC,text,
                    editorPanel.getJTextPane().getStyledDocument().getCharacterElement(pos).getAttributes().getAttribute(StyleConstants.Italic).toString());
        } else if (e.getActionCommand().equals(UNDERLINE_ACTION)) {
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
        } else if (e.getActionCommand().equals(SUPERSCRIPT_ACTION)) {
            String in = (StyleConstants.isSuperscript(rtfek.getInputAttributes())) ? "true" : "false";
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.SUPERSCRIPT,text,in);
        } else if (e.getActionCommand().equals(SUBSCRIPT_ACTION)) {
                String in = (StyleConstants.isSubscript(rtfek.getInputAttributes())) ? "true" : "false";
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFormatAction(
                    RichTextEditorLogger.SUBSCRIPT,text,in);
        }
        editorPanel.formatSync(e.getActionCommand(), text, pos);
        setFormatIcons(
            StyleConstants.isBold(rtfek.getInputAttributes()),
            StyleConstants.isItalic(rtfek.getInputAttributes()),
            StyleConstants.isUnderline(rtfek.getInputAttributes()),
            StyleConstants.isSuperscript(rtfek.getInputAttributes()),
            StyleConstants.isSubscript(rtfek.getInputAttributes())
        );
        editorPanel.getJTextPane().requestFocusInWindow();
    }

    /*
     * Class for managing superscript in rich text editor.
     */
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
                boolean superscript = (StyleConstants.isSuperscript(rtfek
                        .getInputAttributes())) ? false : true;
                boolean subscript = (StyleConstants.isSubscript(rtfek
                        .getInputAttributes())) ? true : false;
                if (superscript)
                    subscript = false;
                StyleConstants.setSuperscript(attr, superscript);
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

    /*
     * Class for managing subscript in rich text editor.
     */
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
                boolean superscript = (StyleConstants.isSuperscript(rtfek
                        .getInputAttributes())) ? true : false;
                if (subscript)
                    superscript = false;
                StyleConstants.setSubscript(attr, subscript);
                StyleConstants.setSuperscript(attr, superscript);
                if(editor.getSelectedText()!=null && !editor.getSelectedText().equals(""))
                {
                    int start=editor.getSelectionStart();
                    int end=editor.getSelectionEnd();
                    doc.setCharacterAttributes(start,(end-start),attr,false);
                }
            }
        }
    }

    /*
     * Sets icons for buttons
     * @param bold is bold switched on in caret location
     * @param italic is italic switched on in caret location
     * @param underline is underline switched on in caret location
     * @param sup is superscript switched on in caret location
     * @param sub is subscript switched on in caret location
     */
    public void setFormatIcons(boolean bold, boolean italic, boolean underline,
            boolean sup, boolean sub) {
        if (bold)
            boldButton.setIcon(boldIconIn);
        else
            boldButton.setIcon(boldIcon);
        if (italic)
            italicButton.setIcon(italicIconIn);
        else
            italicButton.setIcon(italicIcon);
        if (underline)
            underlineButton.setIcon(underlineIconIn);
        else
            underlineButton.setIcon(underlineIcon);
        if (sup)
            superButton.setIcon(superIconIn);
        else
            superButton.setIcon(superIcon);
        if (sub)
            subButton.setIcon(subIconIn);
        else
            subButton.setIcon(subIcon);
    }
}
