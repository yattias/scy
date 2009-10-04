package eu.scy.scymapper.impl.ui.tabpane;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.IConceptMapListener;
import eu.scy.scymapper.impl.model.DefaultConceptMapManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Bjoerge Naess
 * Date: 30.sep.2009
 * Time: 13:36:42
 */
public class ConceptMapTabButton extends JPanel implements IConceptMapListener {
    private final JTabbedPane pane;
	private IConceptMap cmap;
	private JLabel labelField;

	public ConceptMapTabButton(IConceptMap cmap, final JTabbedPane pane) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));

		if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }

		this.cmap = cmap;
		this.cmap.addConceptMapListener(this);
        this.pane = pane;
        setOpaque(false);

        //make JLabel read titles from JTabbedPane
        labelField = new JLabel(cmap.getName());
        labelField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(labelField);
        JButton button = new TabCloseButton();
        add(button);
    }

	@Override
	public void conceptMapUpdated(IConceptMap cmap) {
		labelField.setText(cmap.getName());
	}

	private class TabCloseButton extends JButton implements ActionListener {
        public TabCloseButton() {
            setPreferredSize(new Dimension(20, 20));
            setToolTipText("Close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

		public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ConceptMapTabButton.this);
            if (i != -1 && pane.getTabCount() != 1) {
                pane.remove(i);
				DefaultConceptMapManager.getInstance().remove(cmap);
			}
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

			if (pane.getTabCount() == 1) return;

			int margin = 8;
            Graphics2D g2 = (Graphics2D) g.create();

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
									   RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isRollover()) {
				g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval((margin/2), (margin/2), 1+getWidth()-margin, 1+getHeight()-margin);
            }

            g2.setStroke(new BasicStroke(1f));
            if (getModel().isRollover()) {
				g2.setColor(Color.WHITE);
			}
			margin -= 1;
            g2.drawLine(margin, margin, getWidth()-margin, getHeight()-margin);
            g2.drawLine(margin, getHeight()-margin, getWidth()-margin, margin);

            g2.dispose();
        }
    }
}