/*
 * InterviewTextArea.fx
 *
 * Created on 21.08.2009, 11:33:30
 */

package eu.scy.client.tools.interviewtool;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javafx.ext.swing.SwingComponent;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author kaido
 */

/**
 * Wrapping class for JTextArea
 */
public class InterviewTextArea extends SwingComponent {
    var textArea: JTextArea;
    public var length: Integer;
    public var text: String on replace{
        textArea.setText(text);
    }
    public override var font on replace {
        var fontStyle = Font.PLAIN;
        if (font.style.toUpperCase() == "BOLD") {
            fontStyle = Font.BOLD;
        } else if (font.style.toUpperCase() == "ITALIC") {
            fontStyle = Font.ITALIC;
        }
        var myFont = new Font(font.name, fontStyle, font.size);
        textArea.setFont(myFont);
    }
    public var textBefore:String;
    public var textAfter:String;
    public override function createJComponent():JComponent{
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.addKeyListener(KeyListener{
            public override function keyPressed(keyEvent:KeyEvent) {
                if (keyEvent.VK_PASTE == keyEvent.getKeyCode()) {
                    textArea.paste();
                }
            }
            public override function keyReleased( keyEvent:KeyEvent) {
                var pos = textArea.getCaretPosition();
                text = textArea.getText();
                textArea.setCaretPosition(pos);
            }
            public override function keyTyped(keyEvent:KeyEvent) {
                length = textArea.getDocument().getLength();
            }
        });
        textArea.addFocusListener(FocusListener{
            public override function focusGained(focusEvent:FocusEvent) {
                textBefore = text;
            }
            public override function focusLost(focusEvent:FocusEvent) {
                textAfter = text;
            }
        });
        var scrollPane = new JScrollPane(textArea);
        return scrollPane;
    }
}
