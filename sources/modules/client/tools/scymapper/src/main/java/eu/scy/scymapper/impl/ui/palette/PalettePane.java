package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.IConceptFactory;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.ILinkFactory;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.controller.LinkConnectorController;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.SimpleLink;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.RichNodeView;
import eu.scy.scymapper.impl.ui.diagram.modes.DragMode;
import eu.scy.scymapper.impl.ui.diagram.modes.IDiagramMode;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * User: Bjoerge Naess
 * Date: 03.sep.2009
 * Time: 13:24:59
 */
public class PalettePane extends JToolBar {
	private final static Logger logger = Logger.getLogger(PalettePane.class);

	private ConceptMapPanel conceptMapPanel;
	private List<ILinkFactory> linkFactories;
	private List<IConceptFactory> conceptFactories;
	private AddLinkButton selectedButton;
	//private FillStyleCheckbox opaqueCheckbox;
	//private volatile NodeColorChooserPanel nodeColorChooser;

	public PalettePane(IConceptMap conceptMap, ISCYMapperToolConfiguration conf, ConceptMapPanel conceptMapPanel) {
		super("Palette");
		this.conceptMapPanel = conceptMapPanel;
		this.linkFactories = conf.getLinkFactories();
		this.conceptFactories = conf.getNodeFactories();
		initComponents();
	}

	private void initComponents() {
		//setLayout(new MigLayout("wrap, center", "[grow,fill]"));
		//setLayout(new GridLayout(0, 1));

		setOrientation(JToolBar.VERTICAL);

		for (final IConceptFactory conceptFactory : conceptFactories) {
			INodeModel concept = conceptFactory.create();
			final AddConceptButton button = new AddConceptButton(concept);
			button.setHorizontalAlignment(JButton.CENTER);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					conceptMapPanel.getDiagramView().addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							INodeModel node = conceptFactory.create();
							int w = node.getWidth();
							int h = node.getHeight();
							node.setSize(new Dimension(w, h));
							Point loc = new Point(e.getPoint());
							loc.translate(w / -2, h / -2);
							node.setLocation(loc);

							conceptMapPanel.getDiagramView().getController().add(node);
							conceptMapPanel.getDiagramView().removeMouseListener(this);
							conceptMapPanel.getDiagramView().setCursor(null);
							button.setSelected(false);
						}
					});
					conceptMapPanel.getDiagramView().setCursor(createShapeCursor(conceptFactory));
				}
			});
			add(button);
		}
		add(new Separator());
		for (final ILinkFactory linkFactory : linkFactories) {
			ILinkModel btnLink = linkFactory.create();

			final AddLinkButton button = new AddLinkButton(btnLink.getLabel(), btnLink.getShape());
			button.setHorizontalAlignment(JButton.CENTER);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					if (selectedButton != null && selectedButton.equals(e.getSource())) {
						selectedButton.setSelected(false);
						conceptMapPanel.getDiagramView().setMode(new DragMode(conceptMapPanel.getDiagramView()));
						selectedButton = null;
						return;
					}

					selectedButton = button;

					ILinkModel link = new SimpleLink();
					link.setTo(new Point(0, 0));
					ILinkModel model = linkFactory.create();
					link.setLabel(model.getLabel());
					link.setShape(model.getShape());

					conceptMapPanel.getDiagramView().setMode(new ConnectMode(conceptMapPanel.getDiagramView(), new LinkView(new LinkConnectorController(link), link)));

					conceptMapPanel.getDiagramView().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
			});
			add(button);
		}

		//add(nodeStylePanel);
		JScrollPane nodeScrollPane = new JScrollPane(this);
	}

	class ConnectMode implements IDiagramMode {

		private ConceptDiagramView view;
		LinkView connector = null;

		public ConnectMode(ConceptDiagramView view, LinkView connector) {
			this.view = view;
			this.connector = connector;
			this.view.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			view.add(connector);
			view.setComponentZOrder(connector, 0);
		}

		private INodeModel sourceNode;
		private final MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				RichNodeView comp = (RichNodeView) e.getSource();
				sourceNode = comp.getModel();
				comp.setBorder(BorderFactory.createLineBorder(Color.green, 1));
				Point relPoint = e.getPoint();
				Point loc = new Point(sourceNode.getLocation());
				loc.translate(relPoint.x, relPoint.y);
				connector.setFrom(loc);
				connector.setTo(loc);
				connector.setVisible(true);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				connector.setVisible(false);
				RichNodeView node = (RichNodeView) e.getSource();
				node.setBorder(BorderFactory.createEmptyBorder());

				if (currentTarget != null) {
					NodeLinkModel link = new NodeLinkModel(sourceNode, currentTarget);
					ILinkModel connectorLink = connector.getModel();
					link.setLabel(connectorLink.getLabel());
					link.setShape(connectorLink.getShape());
					link.setStyle(connectorLink.getStyle());
					view.getController().add(link);
					view.remove(connector);
					view.setMode(new DragMode(view));
					if (PalettePane.this.selectedButton != null) {
						PalettePane.this.selectedButton.setSelected(false);
						PalettePane.this.selectedButton = null;
					}
					node.setBorder(BorderFactory.createEmptyBorder());
					getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
				}
			}
		};
		private INodeModel currentTarget;
		private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				// The relative mouse position from the component x,y
				Point relPoint = e.getPoint();

				RichNodeView node = (RichNodeView) e.getSource();

				// Create the new location
				Point newLocation = node.getLocation();
				// Translate the newLocation with the relative point
				//newLocation.translate(relPoint.x, relPoint.y);
				newLocation.translate(relPoint.x, relPoint.y);
				connector.setTo(newLocation);

				INodeModel nodeAt = view.getModel().getNodeAt(newLocation);

				if (nodeAt != null && !nodeAt.equals(sourceNode)) {
					currentTarget = nodeAt;
					// Get the component for target node in order to paint its border
					getNodeViewForModel(currentTarget).setBorder(BorderFactory.createLineBorder(Color.green, 1));

					Point snap = currentTarget.getConnectionPoint(sourceNode.getCenterLocation());
					//targetSnap.translate(relCenter.x, relCenter.y);
					connector.setTo(snap);
					connector.setFrom(sourceNode.getConnectionPoint(snap));
				} else if (currentTarget != null) {
					getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
					connector.setTo(newLocation);
				} else {
					connector.setTo(newLocation);
				}
				if (nodeAt == null) currentTarget = null;
			}
		};

		private RichNodeView getNodeViewForModel(INodeModel node) {
			for (Component c : view.getComponents()) {
				if (c instanceof RichNodeView && ((RichNodeView) c).getModel().equals(node)) {
					return (RichNodeView) c;
				}
			}
			return null;
		}

		@Override
		public MouseListener getMouseListener() {
			return mouseListener;
		}

		@Override
		public MouseMotionListener getMouseMotionListener() {
			return mouseMotionListener;
		}

		@Override
		public FocusListener getFocusListener() {
			return new FocusAdapter() {
			};
		}
	}

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
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
