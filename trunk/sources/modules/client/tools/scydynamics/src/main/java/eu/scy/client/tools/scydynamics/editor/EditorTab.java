package eu.scy.client.tools.scydynamics.editor;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EditorTab extends JPanel implements ChangeListener {

    private ModelEditor editor;
    private EditorPanel editorPanel;
    private EditorToolbar toolbar;
    private FileToolbar filetoolbar;
    private final ResourceBundleWrapper bundle;

    public EditorTab(ModelEditor editor, ActionListener listener, ResourceBundleWrapper bundle) {
        super();
        this.editor = editor;
        this.bundle = bundle;
        this.setLayout(new BorderLayout());
        editorPanel = new EditorPanel();
        toolbar = new EditorToolbar(listener, bundle);
        filetoolbar = new FileToolbar(editor, bundle);
        this.add(toolbar, BorderLayout.WEST);
        if (editor.getProperties().get("show.filetoolbar").equals("true")) {
            this.add(filetoolbar, BorderLayout.NORTH);
        }
	JScrollPane scroller = new JScrollPane();
	scroller.setViewportView(editorPanel);
	scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroller, BorderLayout.CENTER);
    }

    public EditorPanel getEditorPanel() {
        return editorPanel;
    }

    public EditorToolbar getEditorToolbar() {
        return toolbar;
    }

    public FileToolbar getFileToolbar() {
        return this.filetoolbar;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        editor.getActionLogger().logActivateWindow("model", null, this);
    }

    public EditorToolbar getToolbar() {
        return toolbar;
    }
}
