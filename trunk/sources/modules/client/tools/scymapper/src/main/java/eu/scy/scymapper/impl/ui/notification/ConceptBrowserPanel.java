package eu.scy.scymapper.impl.ui.notification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;
import eu.scy.scymapper.impl.ui.Localization;

/**
 * @author krueger
 * @created 10.11.2010
 */
public class ConceptBrowserPanel extends JPanel {
	
	private static final int VISIBLE_ENTRIES = 5;
	
    private JTextPane descriptionLabel;

    private JPanel lexiconPanel;
    
    private JPanel navigationPanel;

    private JButton previousBtn;
    
    private JButton nextBtn;

    private JPanel compound;
    
    private List<String> lexicon;
    
    private int currentEntry;
    
    public ConceptBrowserPanel() {
        this(true);
    }

    public ConceptBrowserPanel(boolean init) {
        lexicon = readLexicon();
        if (init) {
            initComponents();
        }
    }

    void initComponents() {
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        descriptionLabel = new JTextPane();
        
        StyledDocument doc = descriptionLabel.getStyledDocument();
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style s = doc.addStyle("bold", def);
        StyleConstants.setBold(s, true);

        descriptionLabel.setEditable(false);
        descriptionLabel.setBackground(Color.WHITE);

        lexiconPanel = new JPanel();
        lexiconPanel.setLayout(new GridLayout(VISIBLE_ENTRIES, 1));
        lexiconPanel.setBackground(Color.WHITE);
        
        previousBtn = new JButton(Localization.getString("Mainframe.Lexicon.PreviousEntries"));
        previousBtn.setEnabled(false);
        previousBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	showPreviousEntries();
            }
        });

        nextBtn = new JButton(Localization.getString("Mainframe.Lexicon.NextEntries"));
        nextBtn.addActionListener(new ActionListener() {
        	
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		showNextEntries();
        	}
        });
        
        navigationPanel = new JPanel(new GridLayout(1, 2));
        navigationPanel.setBackground(Color.WHITE);
        navigationPanel.add(previousBtn);
        navigationPanel.add(nextBtn);
        
        compound = new JPanel(new BorderLayout());
        compound.setBackground(Color.WHITE);
        compound.setBorder(new EmptyBorder(5, 5, 5, 5));
        compound.add(descriptionLabel, BorderLayout.NORTH);
        compound.add(lexiconPanel, BorderLayout.CENTER);

        add(compound, BorderLayout.CENTER);
    }
    
    public void showFirstEntries() {
        String text = null;

    	if(lexicon == null || lexicon.size() == 0 || (lexicon.size() == 1 && lexicon.get(0).equals(""))) {
            text = Localization.getString("Mainframe.Lexicon.NoEntry") + "\n";
    	} else {
    		if(lexicon.size() > VISIBLE_ENTRIES) {
    			compound.add(navigationPanel, BorderLayout.SOUTH);
    		}
            text = Localization.getString("Mainframe.Lexicon.EntryTitle") + "\n";
            currentEntry = 0;

            if (lexicon.size() <= VISIBLE_ENTRIES) {
    			for (String entry : lexicon) {
    				lexiconPanel.add(new JLabel(entry));
    			}
    		} else {
    			for(int i = 0; i < VISIBLE_ENTRIES; i++) {
    				lexiconPanel.add(new JLabel(lexicon.get(i)));
    			}
    		} 
    	}
    	
    	StyledDocument doc = descriptionLabel.getStyledDocument();
    	try {
    		if (!text.equals(doc.getText(0, doc.getLength()))) {
    			doc.remove(0, doc.getLength());
    			doc.insertString(doc.getLength(), text, doc.getStyle("regular"));
    		}
    		// following line is for broken Mac Java to have a correct layout ...
    		if (System.getProperty("java.vm.vendor").startsWith("Apple")) {
    			descriptionLabel.getPreferredSize();
    		}
    	} catch (BadLocationException e) {
    		e.printStackTrace();
    	}
    }
    
    private List<String> readLexicon() {
    	List<String> entries = SCYMapperStandaloneConfig.getInstance().getLexicon();
    	if(entries == null) {
    		return null;
    	}
    	
    	if(entries.size() > VISIBLE_ENTRIES) {
    		Collections.shuffle(entries);
    	}
    	return entries;
    }
    
    private void showNextEntries() {
    	if(lexicon == null || lexicon.size() <= VISIBLE_ENTRIES || currentEntry + VISIBLE_ENTRIES >= lexicon.size()) {
    		return;
    	}
    	int x;
    	if(currentEntry + 2 * VISIBLE_ENTRIES > lexicon.size()) {
    		// last page may contain less then VISIBLE_ENTRIES entries
    		x = lexicon.size();
    	} else {
    		x = currentEntry + 2 * VISIBLE_ENTRIES;
    	}
		lexiconPanel.removeAll();
    	for(int i = currentEntry + VISIBLE_ENTRIES; i < x; i++) {
    		lexiconPanel.add(new JLabel(lexicon.get(i)));
    	}
    	
//		lexiconPanel.validate();
		lexiconPanel.updateUI();
		currentEntry = currentEntry + VISIBLE_ENTRIES;

		if(lexicon.size() - currentEntry <= VISIBLE_ENTRIES) {
    		nextBtn.setEnabled(false);    		
    	}
		previousBtn.setEnabled(true);
    }
    
    private void showPreviousEntries() {
    	if(lexicon == null || lexicon.size() <= VISIBLE_ENTRIES || currentEntry < VISIBLE_ENTRIES) {
    		return;
    	}
		lexiconPanel.removeAll();
		int x = currentEntry;
    	for(int i = currentEntry - VISIBLE_ENTRIES; i < x; i++) {
    		lexiconPanel.add(new JLabel(lexicon.get(i)));
    	}
    	
		lexiconPanel.validate();
		currentEntry = currentEntry - VISIBLE_ENTRIES;

    	if(currentEntry == 0) {
    		previousBtn.setEnabled(false);
    	}
		nextBtn.setEnabled(true);
    }
}
