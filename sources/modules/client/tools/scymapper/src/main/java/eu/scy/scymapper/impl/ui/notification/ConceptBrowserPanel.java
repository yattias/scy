package eu.scy.scymapper.impl.ui.notification;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
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

    private JList lexiconList;
    
//    private JLabel titleLabel;
    
    public ConceptBrowserPanel() {
        this(true);
    }

    public ConceptBrowserPanel(boolean init) {
//        Icon icon = UIManager.getIcon("OptionPane.informationIcon");
//        titleLabel = new JLabel("", icon, SwingConstants.LEFT);
        if (init) {
            initComponents();
        }
    }

    void initComponents() {
        setLayout(new BorderLayout());
        setDoubleBuffered(true);
        descriptionLabel = new JTextPane();
//        add(BorderLayout.NORTH, titleLabel);

        StyledDocument doc = descriptionLabel.getStyledDocument();
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style s = doc.addStyle("bold", def);
        StyleConstants.setBold(s, true);

        descriptionLabel.setEditable(false);

        lexiconList = new JList(new DefaultListModel());
        lexiconList.setLayoutOrientation(JList.VERTICAL);
        lexiconList.setFixedCellHeight(20);
        lexiconList.setVisibleRowCount(5);
        lexiconList.setEnabled(false);

        JPanel compound = new JPanel(new BorderLayout());
        compound.add(descriptionLabel, BorderLayout.NORTH);
        compound.add(lexiconList, BorderLayout.CENTER);
        add(compound, BorderLayout.CENTER);
    }
    
    public void readLexicon() {
        DefaultListModel model = (DefaultListModel) lexiconList.getModel();
    	model.clear();

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
				model.addElement(entry);
			}
		} else {
			int i = 0;
			Random rndGenerator = new Random();
			while (i < VISIBLE_ENTRIES) {
				int rnd = rndGenerator.nextInt(lexicon.size());
				if (!model.contains(lexicon.get(rnd))) {
					model.addElement(lexicon.get(rnd));
					i++;
				}
			}
		}
    }
}
