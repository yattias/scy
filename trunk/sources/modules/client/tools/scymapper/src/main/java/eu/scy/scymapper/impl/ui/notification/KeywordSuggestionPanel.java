package eu.scy.scymapper.impl.ui.notification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.Localization;

/**
 * @author bjoerge
 * @created 05.feb.2010 19:15:22
 */
public class KeywordSuggestionPanel extends JPanel {

    private static String sep = System.getProperty("file.separator");

    private static final String ADD_PROPOSAL_ICON_PATH = "src" + sep + "main" + sep + "resources/";

    private static final String ADD_PROPOSAL_ICON = "add-proposal.png";

    private JTextPane descriptionLabel;

    private JPanel conceptButtonPane;

    private JLabel titleLabel;

    private JList conceptList;

    private ArrayList<Integer> changedIndices;

    public KeywordSuggestionPanel() {
        this(true);
    }

    public KeywordSuggestionPanel(boolean init) {
        if (init) {
            initComponents();
        }
    }

	void initComponents() {
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        changedIndices = new ArrayList<Integer>();
        Icon icon = UIManager.getIcon("OptionPane.informationIcon");
        titleLabel = new JLabel(null, icon, SwingConstants.LEFT);
        add(BorderLayout.NORTH, titleLabel);
        conceptButtonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));// new MigLayout("wrap 4",
                                                                        // "[fill]"));
        descriptionLabel = new JTextPane();

        StyledDocument doc = descriptionLabel.getStyledDocument();
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style s = doc.addStyle("bold", def);
        StyleConstants.setBold(s, true);

        descriptionLabel.setEditable(false);

        conceptList = new JList(new DefaultListModel());
        conceptList.setCellRenderer(new IconAndTextCellRenderer());
        conceptList.setEnabled(true);
        conceptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conceptList.setLayoutOrientation(JList.VERTICAL);
        conceptList.setFixedCellHeight(30);
        conceptList.setVisibleRowCount(3);

        // JPanel compound = new JPanel(new GridLayout(2, 1));
        // compound.add(descriptionLabel);
        // compound.add(conceptList);
        JPanel compound = new JPanel(new BorderLayout());
        compound.add(descriptionLabel, BorderLayout.NORTH);
        compound.add(conceptList, BorderLayout.CENTER);

        // compound.add(BorderLayout.CENTER, conceptButtonPane);
        add(compound, BorderLayout.CENTER);
	}

	/**
     * Suggests a keyword to be added to the concept map by displaying a list of available concept
     * shapes
	 * 
	 * @param keyword
	 * @param nodeFactories
	 */
	public void setSuggestion(String keyword, Collection<INodeFactory> nodeFactories, ConceptMapPanel panel) {

		String[] text = {
				Localization.getString("Mainframe.KeywordSuggestion.OneSuggestion.Panel.1"),
				keyword,
				Localization.getString("Mainframe.KeywordSuggestion.OneSuggestion.Panel.2"),
				Localization.getString("Mainframe.KeywordSuggestion.OneSuggestion.Panel.3") };
		String[] styles = { "reqular", "bold", "regular", "regular" };

		StyledDocument doc = descriptionLabel.getStyledDocument();
		try {
			for (int i = 0; i < text.length; i++) {
				doc.insertString(doc.getLength(), text[i], doc.getStyle(styles[i]));
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		// following line is for broken Mac Java to have a correct layout ...
		descriptionLabel.getPreferredSize();		

		for (INodeFactory factory : nodeFactories) {
			conceptButtonPane.add(createConceptButton(factory, keyword, panel));
		}
	}

    public void setSuggestions(List<String> keywords, Collection<INodeFactory> nodeFactories, ConceptMapPanel panel) {
	    setSuggestions(keywords, nodeFactories, panel, "concepts");
	}
	
    /**
     * Sets a new title for the keyword suggestion panel.
     * @param text The new text
     */
    public void setTitle(String text) {
    	this.titleLabel.setText(text);
    }
    
	/**
     * Suggests a keyword to be added to the concept map by displaying a list of available concept
     * shapes
	 * 
	 * @param keywords
	 * @param nodeFactories
	 */
    public synchronized void setSuggestions(List<String> keywords, Collection<INodeFactory> nodeFactories, ConceptMapPanel panel, String type) {
        String text = null;
        System.err.println(type + ": " + keywords);
	        if (keywords.isEmpty()) {
	            text = Localization.getString("Mainframe.KeywordSuggestion.NoKeyword");
	        } else {
        		text = Localization.getString("Mainframe.KeywordSuggestion.Suggest");
	        }
	    
				
//		String[] text = {
//		        "A SCY-Agent has discovered that you may have missed relevant keywords in your concept map. ",
        // "Add suggested concepts by clicking the buttons below and selecting the shape you would like each of the concepts to have."
        // };

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
        Collections.sort(keywords);
		DefaultListModel model = (DefaultListModel) conceptList.getModel();
        changedIndices.clear();
        for (int i = 0; i < keywords.size(); i++) {
            if (!model.contains(keywords.get(i))) {
                changedIndices.add(i);
            }
        }
        model.clear();
        for (int i = 0; i < keywords.size(); i++) {
            model.addElement(keywords.get(i));
        }
    }
	
	class IconAndTextCellRenderer extends JLabel implements ListCellRenderer {
		ImageIcon icon = new ImageIcon(ADD_PROPOSAL_ICON_PATH + ADD_PROPOSAL_ICON);
	
		@Override
        public Component getListCellRendererComponent(JList list, // the list
				Object value,            // value to display
        final int index, // cell index
				boolean isSelected,      // is the cell selected
				boolean cellHasFocus) {    // does the cell have focus
            JLabel l = new JLabel();
			String s = value.toString();
            l.setText(s);
            l.setIcon(icon);
			if (isSelected) {
                l.setBackground(list.getSelectionBackground());
                l.setForeground(list.getSelectionForeground());
            } else {
                l.setBackground(list.getBackground());
                l.setForeground(list.getForeground());
            }
            l.setEnabled(list.isEnabled());
            l.setFont(list.getFont());
            l.setOpaque(true);
            if (changedIndices.contains(index)) {
                l.setBorder(new LineBorder(Color.RED, 2, true));
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            if (changedIndices.contains(index)) {
                                changedIndices.remove(changedIndices.indexOf(index));
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    
                };
                t.start();
			} else {
                l.setBorder(new EmptyBorder(2, 2, 2, 2));
			}
            return l;
		}
	}
	 
	private Component createConceptButton(final INodeFactory factory, final String keyword, final ConceptMapPanel panel) {
		final JToggleButton button = new JToggleButton(factory.getIcon());
		button.setText(keyword);
		button.setHorizontalAlignment(JButton.CENTER);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.getDiagramView().addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						INodeModel node = factory.create();
						int w = node.getWidth();
						int h = node.getHeight();
						node.setSize(new Dimension(w, h));
						Point loc = new Point(e.getPoint());
						loc.translate(w / -2, h / -2);
						node.setLocation(loc);
						node.setLabel(keyword);

						panel.getDiagramView().getController().add(node);
						panel.getDiagramView().removeMouseListener(this);
						panel.getDiagramView().setCursor(null);
						button.setSelected(false);
					}
				});
				panel.getDiagramView().setCursor(createShapeCursor(factory));
			}
		});
		return button;
	}

	Cursor createShapeCursor(INodeFactory nodeFactory) {

		Toolkit tk = Toolkit.getDefaultToolkit();

		INodeModel node = nodeFactory.create();

		Dimension size = tk.getBestCursorSize(node.getWidth(), node.getHeight());

		INodeStyle style = node.getStyle();
		INodeShape shape = node.getShape();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();

		// Create an image that supports arbitrary levels of transparency
		BufferedImage i = gc.createCompatibleImage(size.width, size.height, Transparency.BITMASK);

		Graphics2D g2d = (Graphics2D) i.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(style.getBackground());

		Rectangle rect = new Rectangle(0, 0, size.width, size.height);
		shape.setMode(style.isOpaque() ? INodeShape.FILL : INodeShape.DRAW);
		shape.paint(g2d, rect);

		return tk.createCustomCursor(i, new Point(size.width / 2, size.height / 2), "Place shape here");
	}
}
