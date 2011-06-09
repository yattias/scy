package eu.scy.scymapper.impl.ui.notification;

import java.awt.BorderLayout;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig.Help;
import eu.scy.scymapper.impl.logging.ConceptMapActionLogger;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.ProposalEntry;
import eu.scy.scymapper.impl.ui.ProposalList;

/**
 * @author bjoerge
 * @created 05.feb.2010 19:15:22
 */
public class KeywordSuggestionPanel extends JPanel {

    private static final String ADD_NODE_ICON = "add_node.png";

    private static final String ADD_EDGE_ICON = "add_edge.png";

    private static final String SPECIFY_EDGE_ICON = "specify_edge.png";

    protected ConceptMapActionLogger actionLogger;

    private JTextPane descriptionLabel;

    private JPanel conceptButtonPane;

    private JLabel titleLabel;

    protected ProposalList proposalList;

    private String[] categories;

    public KeywordSuggestionPanel(ConceptMapActionLogger actionLogger) {
        this(actionLogger, true);
    }

    public KeywordSuggestionPanel(ConceptMapActionLogger actionLogger, boolean init) {
        this.actionLogger = actionLogger;
        Icon icon = UIManager.getIcon("OptionPane.informationIcon");
        titleLabel = new JLabel("", icon, SwingConstants.LEFT);
        if (init) {
            initComponents();
        }
    }

    void initComponents() {
        setLayout(new BorderLayout());
        setDoubleBuffered(true);

        add(BorderLayout.NORTH, titleLabel);
        conceptButtonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));// new MigLayout("wrap 4",
                                                                        // "[fill]"));
        descriptionLabel = new JTextPane();

        StyledDocument doc = descriptionLabel.getStyledDocument();
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style s = doc.addStyle("bold", def);
        StyleConstants.setBold(s, true);

        descriptionLabel.setEditable(false);
        proposalList = new ProposalList(3);

        JPanel compound = new JPanel(new BorderLayout());
        compound.add(descriptionLabel, BorderLayout.NORTH);
        compound.add(proposalList, BorderLayout.CENTER);

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

        String[] text = { Localization.getString("Mainframe.KeywordSuggestion.OneSuggestion.Panel.1"), keyword, Localization.getString("Mainframe.KeywordSuggestion.OneSuggestion.Panel.2"), Localization.getString("Mainframe.KeywordSuggestion.OneSuggestion.Panel.3") };
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

    public void setActionLogger(ConceptMapActionLogger actionLogger) {
        this.actionLogger = actionLogger;
    }

    /**
     * Sets a new title for the keyword suggestion panel.
     * 
     * @param text
     *            The new text
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
    public synchronized void setSuggestions(String[] keywords, String[] categories, Collection<INodeFactory> nodeFactories, String type, boolean highlightChanged) {
        String text = null;
        if (type.equals("error")) {
            text = Localization.getString("Mainframe.KeywordSuggestion.NoText");
        } else if (keywords.length == 0) {
            text = Localization.getString("Mainframe.KeywordSuggestion.NoKeyword");
        } else {
            text = Localization.getString("Mainframe.KeywordSuggestion.Suggest");
        }

        this.categories = categories;

        StyledDocument doc = descriptionLabel.getStyledDocument();
        try {
            if (!text.equals(doc.getText(0, doc.getLength()))) {
                doc.remove(0, doc.getLength());
                doc.insertString(doc.getLength(), text, doc.getStyle("regular"));
            }

            // following line is for broken Mac Java to have a correct layout in single mode, but
            // will demolish layout in double keyword suggestion mode... amazing
            if (System.getProperty("java.vm.vendor").startsWith("Apple")) {
                descriptionLabel.getPreferredSize();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        Arrays.sort(keywords);

        ImageIcon iconAddNode = null;
        ImageIcon iconAddEdge = null;
        ImageIcon iconSpecifyEdge = null;
        try {
            iconAddNode = new ImageIcon(ImageIO.read(this.getClass().getResource("/" + ADD_NODE_ICON)));
            iconAddEdge = new ImageIcon(ImageIO.read(this.getClass().getResource("/" + ADD_EDGE_ICON)));
            iconSpecifyEdge = new ImageIcon(ImageIO.read(this.getClass().getResource("/" + SPECIFY_EDGE_ICON)));
        } catch (IllegalArgumentException e) {
            iconAddNode = new ImageIcon();
            iconAddEdge = new ImageIcon();
            iconSpecifyEdge = new ImageIcon();
            e.printStackTrace();
        } catch (IOException e) {
            iconAddNode = new ImageIcon();
            iconAddEdge = new ImageIcon();
            iconSpecifyEdge = new ImageIcon();
            e.printStackTrace();
        }

        List<ProposalEntry> pes = new ArrayList<ProposalEntry>();
        for (int i = 0; i < keywords.length; i++) {
            ProposalEntry pe;
            if (categories == null || categories[i].equals("concept_proposal")) {
                pe = createProposalEntry(iconAddNode, keywords[i], Localization.getString("Mainframe.KeywordSuggestion.SynonymLink"), Localization.getString("Mainframe.KeywordSuggestion.SynonymLink2"), true);
            } else if (categories[i].equals("relation_proposal")) {
            	String[] relation = keywords[i].split(",", 2);
                pe = createProposalEntry(iconAddEdge, relation[0] + ",", relation[1], null, false);
            } else {
                pe = createProposalEntry(iconSpecifyEdge, keywords[i], null, null, false);
            }
            pes.add(pe);
        }
        proposalList.clear();
        proposalList.addEntries(pes, SCYMapperStandaloneConfig.getInstance().getHelpMode() == Help.CONTINUOUS);
        updateUI();
    }

    protected ProposalEntry createProposalEntry(ImageIcon icon, String keyword, String text, String secondText, boolean isLink) {
    	return new ProposalEntry(icon, new JLabel(keyword));
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
