package eu.scy.scymapper.impl.ui.notification;

import eu.scy.scymapper.api.IConceptFactory;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.palette.AddConceptButton;
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
		conceptButtonPane = new JPanel(new MigLayout("center,wrap 2", "[fill]"));
		descriptionLabel = new JTextPane();

		StyledDocument doc = descriptionLabel.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style s = doc.addStyle("bold", def);
		StyleConstants.setBold(s, true);

		descriptionLabel.setEditable(false);
		JPanel compound = new JPanel(new GridLayout(2, 0));
		compound.add(descriptionLabel);
		compound.add(conceptButtonPane);
		add(BorderLayout.CENTER, compound);
	}

	/**
	 * Suggests a keyword to be added to the concept map by displaying a list of available concept shapes
	 *
	 * @param keyword
	 * @param conceptFactories
	 */
	public void setSuggestion(String keyword, Collection<IConceptFactory> conceptFactories, ConceptMapPanel panel) {

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

		for (IConceptFactory factory : conceptFactories) {
			conceptButtonPane.add(createConceptButton(factory, keyword, panel));
		}
	}

	private Component createConceptButton(final IConceptFactory factory, final String keyword, final ConceptMapPanel panel) {
		final AddConceptButton button = new AddConceptButton(factory.create());
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

	Cursor createShapeCursor(IConceptFactory conceptFactory) {

		Toolkit tk = Toolkit.getDefaultToolkit();

		INodeModel node = conceptFactory.create();

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
