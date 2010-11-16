package eu.scy.scymapper.impl.ui.notification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.Random;

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
    
    public ConceptBrowserPanel() {
        this(true);
    }

    public ConceptBrowserPanel(boolean init) {
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
        
        JPanel compound = new JPanel(new BorderLayout());
        compound.setBackground(Color.WHITE);
        compound.setBorder(new EmptyBorder(5, 5, 5, 5));
        compound.add(descriptionLabel, BorderLayout.NORTH);
        compound.add(lexiconPanel, BorderLayout.CENTER);

        add(compound, BorderLayout.CENTER);
    }
    
    public void readLexicon() {
        String text = null;
    	List<String> lexicon = SCYMapperStandaloneConfig.getInstance().getLexicon();
    	if(lexicon == null || lexicon.size() == 0) {
            text = Localization.getString("Mainframe.Lexicon.NoEntry") + "\n";
    	} else {
            text = Localization.getString("Mainframe.Lexicon.EntryTitle") + "\n";
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

    	if (lexicon.size() <= VISIBLE_ENTRIES) {
			for (String entry : lexicon) {
				lexiconPanel.add(new JLabel(entry));
			}
		} else {
			Random rndGenerator = new Random();
			for(int i = 0; i < VISIBLE_ENTRIES; i++) {
				int rnd = rndGenerator.nextInt(lexicon.size());
				lexiconPanel.add(new JLabel(lexicon.get(rnd)));
				lexicon.remove(rnd);
			}
		}
    }
}
