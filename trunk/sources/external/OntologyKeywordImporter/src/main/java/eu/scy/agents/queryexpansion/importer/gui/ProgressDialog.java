package eu.scy.agents.queryexpansion.importer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class ProgressDialog extends JDialog {

    private JProgressBar progressBar;

    public ProgressDialog(Frame parent, String title, String message) {
        super(parent, title, true);
        JPanel root = new JPanel(new BorderLayout(5,5));
        setLayout(new FlowLayout());
        setResizable(false);
        progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 100);
        progressBar.setStringPainted(true);
        root.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.NORTH);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(progressBar);
        root.add(panel, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);
        pack();
        Point refLocation;
        Dimension refDimension;
        if (parent != null) {
            refLocation = parent.getLocation();
            refDimension = parent.getSize();
        } else {
            refLocation = new Point(0, 0);
            refDimension = Toolkit.getDefaultToolkit().getScreenSize();
        }
        setLocation(refLocation.x + refDimension.width / 2 - getSize().width / 2, refLocation.y + refDimension.height / 2 - getSize().height / 2);
    }

    public static void main(String[] args) {
        ProgressDialog d = new ProgressDialog(null, "Please wait", "Linking the keywords to the ontology ...");
        d.setVisible(true);
        
    }
    
    public JProgressBar getProgressBar() {
        return progressBar;
    }

}
