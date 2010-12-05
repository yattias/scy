/*
 * InterviewGuides.fx
 *
 * Created on 15.02.2010, 15:06:55
 */

package eu.scy.client.tools.interviewtool;

/**
 * @author kaido
 */
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javafx.ext.swing.SwingComponent;
import javax.swing.JComponent;
import javax.swing.text.html.HTMLEditorKit;
import java.io.ByteArrayOutputStream;
import java.awt.Font;
import org.apache.log4j.Logger;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.text.Document;

/**
* Guide area of the Interview Tool
*/
public class InterviewGuides extends SwingComponent {
    def logger = Logger.getLogger("eu.scy.client.tools.interviewtool.InterviewGuides");
    var htmlEditor: HTMLEditorKit;
    var textPane: JTextPane;
    function getText() {
        var bos:ByteArrayOutputStream = new ByteArrayOutputStream();
        htmlEditor.write(bos, textPane.getDocument(), 0, textPane.getDocument().getLength());
        return bos.toString("UTF-8");
    }
    function replaceVars(beforeArr:String[], afterArr:String[]) {
        var txt = textPane.getText();
        var i = 0;
        while (i<sizeof(beforeArr)) {
            txt = txt.replaceAll(beforeArr[i],afterArr[i]);
            i++;
        }
        setText(txt);
    }
    public function setText(text:String) {
        textPane.setText(text);
        textPane.setCaretPosition(0);
    }
    public function setTextFromFile(fileName:String) {
        var url:URL = getClass().getResource("resources/{fileName}");
        textPane.getDocument().putProperty(Document.StreamDescriptionProperty, null);
        textPane.setPage(url);
    }
    public function setTextFromFile(fileName:String, beforeArr:String[], afterArr:String[]) {
        var guideText:String = "";
        var inputStream:InputStream = getClass().getResourceAsStream("resources/{fileName}");
        var bufferedReader:BufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        var line:String;
        while( (line = bufferedReader.readLine() ) !=null)
            guideText = "{guideText}\n{line}";
        setText(guideText);
        replaceVars(beforeArr, afterArr);
    }
    public override function createJComponent():JComponent{
        textPane = new JTextPane();
        textPane.setEditable(false);
        var myFont = new Font("Arial", Font.PLAIN, 14);
        textPane.setFont(myFont);
        htmlEditor = new HTMLEditorKit();
        textPane.setEditorKit(htmlEditor);
        textPane.setContentType("text/html;charset=utf-8");
        var scrollPane = new JScrollPane(textPane);
        return scrollPane;
    }
}
