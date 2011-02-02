/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

import colab.vt.whiteboard.component.WhiteboardPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * whiteboard panel for copex
 * @author Marjolaine
 */
public class MyWhiteBoardPanel extends JPanel{

    /* whiteboard panel */
    private WhiteboardPanel whiteBoardPanel;

    public MyWhiteBoardPanel() {
        super();
        setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        whiteBoardPanel = new WhiteboardPanel();
        add(whiteBoardPanel, BorderLayout.CENTER);
    }

    public WhiteboardPanel getWhiteBoardPanel() {
        return whiteBoardPanel;
    }

}
