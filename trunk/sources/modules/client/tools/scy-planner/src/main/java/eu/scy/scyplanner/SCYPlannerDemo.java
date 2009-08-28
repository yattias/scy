package eu.scy.scyplanner;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.impl.DiagramModel;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.shapes.concepts.*;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramView;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramController;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceNodeModel;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceLinkModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 12:28:20
 */
public class SCYPlannerDemo extends JFrame implements IDiagramModelObserver, INodeModelObserver {
	private IDiagramModel diagramModel;

	private INodeModel selectedNode;
	private JLabel selectedLabel;

	public SCYPlannerDemo() {
		super("SCYPlannerDemo Test");
	}

	public void start() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 900);

		diagramModel = new SCYPlannerDiagramModel();

		// Observe the diagramModel
		diagramModel.addObserver(this);

		SCYPlannerDiagramView view = new SCYPlannerDiagramView(new SCYPlannerDiagramController(diagramModel), diagramModel);

		JButton testButton = new JButton("Click me to change the shape, style and size of the last selected concept");
		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedNode != null) {
					selectedNode.setShape(new Star());
					selectedNode.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
					selectedNode.getStyle().setBackground(new Color(0xff9900));
				}
			}
		});
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(view, BorderLayout.CENTER);
		getContentPane().add(testButton, BorderLayout.PAGE_START);
		setVisible(true);

		selectedLabel = new JLabel("No node selected yet");
		getContentPane().add(selectedLabel, BorderLayout.PAGE_END);

		// So, lets add a few additional nodes AFTER the component is initialized, and see what happens
		testAddNodes2();
		// If things are done rigth, the component should now contain a circle and a star
	}

	public synchronized void testAddNodes2() {

		INodeModel redStar = new LearningActivitySpaceNodeModel();
		redStar.setStyle(new DefaultNodeStyle());
		redStar.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		redStar.getStyle().setBackground(new Color(0xcc0000));
		redStar.setShape(new Star());
		redStar.setLabel("I'm a LAS. Hurray!");
		redStar.setLocation(new Point(300, 150));
		redStar.setSize(new Dimension(200, 200));
		addNode(redStar);

		INodeModel ellipse = new LearningActivitySpaceNodeModel();
		ellipse.setStyle(new DefaultNodeStyle());
		ellipse.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
		ellipse.getStyle().setBackground(new Color(0x0099ff));
		ellipse.setLabel("I'm also a LAS. Hurray for me too!");
		ellipse.setLocation(new Point(300, 450));
		ellipse.setSize(new Dimension(150, 100));
		ellipse.setShape(new Ellipse());
		addNode(ellipse);

		IConceptLinkModel link = new LearningActivitySpaceLinkModel(redStar, ellipse);
		link.getStyle().setColor(new Color(0x444444));
		link.getStyle().setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[]{6.0f}, 0.0f));
		link.setShape(new Arrow());
		link.setLabel("I'm in between");
		diagramModel.addLink(link);

	}

	private void addNode(INodeModel node) {
		// subscribe to changes in this node
		node.addObserver(this);
		diagramModel.addNode(node);
	}

	@Override
	public void linkAdded(ILinkModel link) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void linkRemoved(ILinkModel link) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeAdded(INodeModel n) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeRemoved(INodeModel n) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void updated(IDiagramModel diagramModel) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeSelected(INodeModel n) {
		System.out.println("DiagramImplTest.nodeSelected");
	}

	@Override
	public void moved(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void resized(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void labelChanged(INodeModel node) {
	}

	@Override
	public void styleChanged(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void shapeChanged(INodeModel node) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void nodeSelected(NodeModel conceptNode) {
		selectedNode = conceptNode;
		selectedLabel.setText("You clicked: " + conceptNode.getLabel());
	}
}

