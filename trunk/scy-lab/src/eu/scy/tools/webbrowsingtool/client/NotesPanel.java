package eu.scy.tools.webbrowsingtool.client;

import java.util.Vector;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.VerticalLayout;


public class NotesPanel extends Panel {

    private Vector<Note> notes;
    
    private Vector<Label> labels;
    
    public NotesPanel(Vector<Note> notes){
        super();
        if (notes == null){
            notes = new Vector<Note>();
        }
        if (labels == null){
            labels = new Vector<Label>();
        }
        setLayout(new VerticalLayout(20));
        for (Note note : notes) {
            addNote(note);
        }
    }

    public void addNote(Note note) {
        if (notes==null) {
            notes = new Vector<Note>();
        }
        notes.add(note);
        Label label = new Label();
        label.setText(note.getText());
        ToolTip tip = new ToolTip();
        tip.setTitle(note.getTitle());
        tip.setHtml(note.getAnnotation());
        tip.setDismissDelay(15000);
        tip.setWidth(300);
        tip.setTrackMouse(true);
        tip.applyTo(label);
        this.add(label);
        labels.add(label);
    }
    
    public void addNote(String title, String text, String annotation){
        Note note = new Note(title,text,annotation);
        addNote(note);
    }

    
    /**
     * @return the labels
     */
    public Vector<Label> getLabels() {
        return labels;
    }

    
    /**
     * @param labels the labels to set
     */
    public void setLabels(Vector<Label> labels) {
        this.labels = labels;
    }
    
    
}
