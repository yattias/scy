package eu.scy.scymapper.impl.ui.notification;

import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * @author bjoerge
 * @created 05.feb.2010 19:15:22
 */
public class KeywordSuggestionPanel extends JPanel {
	private JTextPane descriptionLabel;
	private JPanel conceptButtonPane;

	public KeywordSuggestionPanel() {
		initComponents();
	}

	void initComponents() {
		setLayout(new BorderLayout());

		Icon icon = UIManager.getIcon("OptionPane.informationIcon");
		JLabel label = new JLabel("Keyword Suggestion", icon, SwingConstants.LEFT);
		add(BorderLayout.NORTH, label);
		conceptButtonPane = new JPanel(new MigLayout("wrap 4", "[fill]"));
		descriptionLabel = new JTextPane();

		StyledDocument doc = descriptionLabel.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style s = doc.addStyle("bold", def);
		StyleConstants.setBold(s, true);

		descriptionLabel.setEditable(false);
		JPanel compound = new JPanel(new BorderLayout());
		compound.add(BorderLayout.NORTH, descriptionLabel);
		compound.add(BorderLayout.CENTER, conceptButtonPane);
		add(BorderLayout.CENTER, compound);
	}

	/**
	 * Suggests a keyword to be added to the concept map by displaying a list of available concept shapes
	 *
	 * @param keyword
	 * @param nodeFactories
	 */
	public void setSuggestion(String keyword, Collection<INodeFactory> nodeFactories, ConceptMapPanel panel) {

		String[] text = {
				"The SCY-Troll has discovered that you may have missed a relevant keyword in your concept map. Would you like to add ",
				keyword,
				" as a concept to your concept map?\n\n",
				"To do so, select first a shape for this concept from below, then click in the diagram at the place you would like to add it."
		};
		String[] styles = {"reqular", "bold", "regular", "regular"};

		StyledDocument doc = descriptionLabel.getStyledDocument();
		try {
			for (int i = 0; i < text.length; i++) {
				doc.insertString(doc.getLength(), text[i], doc.getStyle(styles[i]));
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		for (INodeFactory factory : nodeFactories) {
			conceptButtonPane.add(createConceptButton(factory, keyword, panel));
		}
	}

	/**
	 * Suggests a keyword to be added to the concept map by displaying a list of available concept shapes
	 *
	 * @param keywords
	 * @param nodeFactories
	 */
	public void setSuggestions(java.util.List<String> keywords, Collection<INodeFactory> nodeFactories, ConceptMapPanel panel) {

		String[] text = {
				"The SCY-Troll has discovered that you may have missed relevant keywords in your concept map. ",
				"Add suggested concepts by clicking the buttons below and selecting the shape you would like each of the concepts to have."
		};
		String[] styles = {"reqular", "regular"};

		StyledDocument doc = descriptionLabel.getStyledDocument();
		try {
			for (int i = 0; i < text.length; i++) {
				doc.insertString(doc.getLength(), text[i], doc.getStyle(styles[i]));
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		for (String keyword : keywords) {
			final JPopupMenu popup = new JPopupMenu();
			popup.setLayout(new GridLayout(0, 2));
			popup.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.gray, 1), BorderFactory.createTitledBorder("Select the shape")));
			for (INodeFactory factory : nodeFactories) {
				popup.add(createConceptButton(factory, keyword, panel));
			}
			final JButton btn = new JButton(keyword);
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					popup.show(btn, 0, btn.getHeight());
				}
			});
			conceptButtonPane.add(btn);
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
