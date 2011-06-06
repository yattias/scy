package eu.scy.scymapper.impl.ui.notification;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.logging.ConceptMapActionLoggerCollide;
import eu.scy.scymapper.impl.ui.KeywordLabel;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.ProposalEntry;
import eu.scy.scymapper.impl.ui.ProposalList;
import eu.scy.scymapper.impl.ui.diagram.RichNodeView;

public class KeywordSuggestionPanelCollide extends KeywordSuggestionPanel {

    private String selectedKeyword = null;
    
    public KeywordSuggestionPanelCollide(ConceptMapActionLogger actionLogger) {
    	super(actionLogger);
    }
    
    public KeywordSuggestionPanelCollide(ConceptMapActionLogger actionLogger, boolean init) {
    	super(actionLogger, init);
    }

    public String getSelectedKeyword() {
    	return this.selectedKeyword;
    }
	
    @Override
    protected ProposalEntry createProposalEntry(ImageIcon icon, String keyword, final String text, final String secondText, boolean isLink) {
    	if(text == null) {
    		return super.createProposalEntry(icon, keyword, text, secondText, isLink);
    	}
    	if(!isLink) {
    		JLabel secondLabel = new JLabel(text);
    		return new ProposalEntry(icon, new JLabel(keyword), secondLabel);
    	}

    	final KeywordLabel secondLabel = new KeywordLabel(text, keyword);
		secondLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		final ProposalEntry entry = new ProposalEntry(icon, new JLabel(keyword), secondLabel);

		secondLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				KeywordLabel label = (KeywordLabel) e.getSource();
				selectedKeyword = label.getKeyword();
				secondLabel.setLink(secondText);
                                SwingUtilities.getRoot(label).setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				label.requestFocusInWindow();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				KeywordLabel label = (KeywordLabel) e.getSource();
				SwingUtilities.getRoot(label).setCursor(new Cursor(Cursor.HAND_CURSOR));
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				KeywordLabel label = (KeywordLabel) e.getSource();
				if(!label.hasFocus()) {
					SwingUtilities.getRoot(label).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				} else {
                                    SwingUtilities.getRoot(label).setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                                    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
			}
		});

		secondLabel.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				Component newFocusOwner = e.getOppositeComponent();
				SwingUtilities.getRoot(newFocusOwner).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				secondLabel.setLink(text);
				INodeModel model = null;
				if (newFocusOwner == null) {
				    JOptionPane.showMessageDialog(null, Localization.getString("Mainframe.KeywordSuggestion.SynonymNodeNotFound"), "Problem", JOptionPane.ERROR_MESSAGE);
				    return;
				}
				if (newFocusOwner instanceof RichNodeView) {
					model = ((RichNodeView) newFocusOwner).getModel();
				} else if (newFocusOwner instanceof JTextArea) {
					// this will get the RichNodeView of a JTextArea
					Component parentContainer = newFocusOwner.getParent().getParent().getParent();
					if (parentContainer instanceof RichNodeView) {
						model = ((RichNodeView) parentContainer).getModel();
					}
				}
				if (model != null) {
					((ConceptMapActionLoggerCollide)actionLogger).logSynonymAdded(model, selectedKeyword);
					ProposalList container = (ProposalList) entry.getParent();
					container.removeEntry(entry);
				} else {
				    JOptionPane.showMessageDialog(null, Localization.getString("Mainframe.KeywordSuggestion.SynonymNodeNotFound"), "Problem", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return entry;
    }

}
