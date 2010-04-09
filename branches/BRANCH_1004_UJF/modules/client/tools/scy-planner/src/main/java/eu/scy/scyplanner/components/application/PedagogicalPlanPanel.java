package eu.scy.scyplanner.components.application;

import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.scymapper.api.diagram.model.*;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.impl.model.DefaultNode;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.shapes.links.Arrowhead;
import eu.scy.scymapper.impl.shapes.links.QuadCurvedLine;
import eu.scy.scymapper.impl.shapes.nodes.RoundRectangle;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramController;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramView;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceLinkModel;
import eu.scy.scyplanner.impl.shapes.NewELOShape;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 12:28:20
 */
public class PedagogicalPlanPanel extends JPanel implements IDiagramListener, INodeModelListener {
	private static Logger log = Logger.getLogger("eu.scy.scyplanner.components.application.PedagogicalPlanPanel");
	private IDiagramModel diagramModel;

	public PedagogicalPlanPanel(PedagogicalPlan pedagogicalPlan) {
		Mission mission = pedagogicalPlan.getMission();
		Scenario scenario = pedagogicalPlan.getScenario();
		log.info("Creating pedagogical plan based on mission: " + mission.getName() + " and scenario: " + scenario.getName());
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

			}
		});

		diagramModel = getInitialPedagogicalPlanModel(mission, scenario);

		diagramModel.addDiagramListener(this);

		SCYPlannerDiagramView view = new SCYPlannerDiagramView(new SCYPlannerDiagramController(diagramModel), diagramModel);
		tabbedPane.addTab(Strings.getString("Overview"), new JScrollPane(view));
		view.addObserver(new Observer() {
			public void update(Observable observable, Object object) {
				Object model = (((DefaultNode) object).getObject());

				if (model instanceof LearningActivitySpace) {
					addTab(tabbedPane, new LASOverviewPanel((LearningActivitySpace) model), model);
				} else if (model instanceof AnchorELO) {
					addTab(tabbedPane, new AnchorELOOverviewPanel((AnchorELO) model), model);
				} else {
					JOptionPane.showMessageDialog(SCYPlannerApplicationManager.getApplicationManager().getScyPlannerFrame(), Strings.getString("Error, I do not know how to handle objects of type ") + model.getClass().getName(), Strings.getString("Error"), JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
	}

	private void addTab(JTabbedPane tabbedPane, JComponent component, Object model) {
		tabbedPane.addTab(model.toString(), component);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
	}

	private IDiagramModel getInitialPedagogicalPlanModel(final Mission mission, final Scenario scenario) {
		SCYPlannerDiagramModel pedagogicalPlan = new SCYPlannerDiagramModel();
		LearningActivitySpace firstLas = scenario.getLearningActivitySpace();

		createLASElement(firstLas, firstLas.getXPos(), firstLas.getYPos(), pedagogicalPlan);

		return pedagogicalPlan;
	}


	private void addAnchorEloNode(INodeModel lasNode, LearningActivitySpace las, SCYPlannerDiagramModel pedagogicalPlan) {
		java.util.List<Activity> activities = las.getActivities();
		log.info("ADDING LAS: " + las.getName() + " has " + activities.size() + " activities");
		for (int i = 0; i < activities.size(); i++) {
			Activity activity = activities.get(i);
			if (activity.getAnchorELO() != null) {
				INodeModel anchorNodeModel = createELOElement(activity.getAnchorELO(), activity.getAnchorELO().getXPos(), activity.getAnchorELO().getYPos());
				anchorNodeModel.addListener(this);
				if (!pedagogicalPlan.checkIfNodeHasBeenAdded(anchorNodeModel)) {
					addNode(pedagogicalPlan, anchorNodeModel);
					connectNodes(pedagogicalPlan, lasNode, anchorNodeModel);
					if (activity.getAnchorELO().getInputTo() != null) {
						addLASNode(anchorNodeModel, activity.getAnchorELO(), pedagogicalPlan);
					}

				}

			} else {
				log.info("Activity: " + activity.getName() + " does note have any elos");
			}
		}
	}

	private void addLASNode(INodeModel anchorEloNode, AnchorELO elo, SCYPlannerDiagramModel pedagogicalPlan) {
		LearningActivitySpace las = elo.getInputTo();

		DefaultNode lasModel = createLASElement(las, las.getXPos(), las.getYPos(), pedagogicalPlan);
		if (!pedagogicalPlan.getIsAlreadyConnected(anchorEloNode, lasModel)) {
			connectNodes(pedagogicalPlan, anchorEloNode, lasModel);
			addNode(pedagogicalPlan, lasModel);
		}

		//java.util.List<Activity> activityList = las.getActivities();
		/*log.info("LAS: " + las.getName() + " has " + activityList.size() + " activities");
				for (int i = 0; i < activityList.size(); i++) {
					Activity activity = activityList.get(i);
					log.info("Activity: " + activity + " " + activity.getAnchorELO());
				} */

	}

	private void addNode(SCYPlannerDiagramModel pedagogicalPlan, INodeModel node) {
		if (node != null) {
			node.addListener(this);
			pedagogicalPlan.addNode(node);
		}

	}

	private DefaultNode createLASElement(LearningActivitySpace learningActivitySpace, Integer xPos, Integer yPos, SCYPlannerDiagramModel pedagogicalPlan) {
		DefaultNode las = new DefaultNode();
		las.setObject(learningActivitySpace);
		URL theurl = getClass().getResource("/eu/scy/scyplanner/impl/shapes/LASShape.svg");

		try {

			RoundRectangle rectangle = new RoundRectangle();

			INodeShape s = new RoundRectangle();//SVGConcept(theurl);
			las.setShape(s);
		} catch (Exception e) {
			System.err.println("File not found: /eu/scy/scyplanner/impl/shapes/LASShape.svg");
		}

		las.setLocation(new Point(xPos, yPos));
		las.setSize(new Dimension(142, 73));
		addNode(pedagogicalPlan, las);

		addAnchorEloNode(las, learningActivitySpace, pedagogicalPlan);
		return las;
	}

	private INodeModel createELOElement(AnchorELO elo, Integer xPos, Integer yPos) {

		DefaultNode eloNodeModel = new DefaultNode();
		eloNodeModel.setObject(elo);

		URL theurl = getClass().getResource("/eu/scy/scyplanner/impl/shapes/ELOShape.svg");

		try {
			INodeShape s = new NewELOShape();//new SVGConcept(theurl);
			eloNodeModel.setShape(s);
		} catch (Exception e) {
			System.err.println("File not found /eu/scy/scyplanner/impl/shapes/ELOShape.svg");
		}

		eloNodeModel.setLocation(new Point(xPos, yPos));
		eloNodeModel.setSize(new Dimension(75, 75));
		eloNodeModel.setLabelHidden(true);

		return eloNodeModel;
	}

	private void connectNodes(SCYPlannerDiagramModel diagramModel, INodeModel from, INodeModel to) {
		INodeLinkModel link = new LearningActivitySpaceLinkModel(from, to);
		link.setLabelHidden(true);
		link.getStyle().setBackground(new Color(0x4f81bc));
		link.getStyle().setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f));

		Arrowhead head = new Arrowhead();
		head.setLength(7);
		Arrow arrow = new Arrow();
		arrow.setLineShape(new QuadCurvedLine());
		arrow.setHead(head);

		link.setShape(arrow);
		diagramModel.addLink(link);
	}

	@Override
	public void linkAdded(ILinkModel link) {
	}

	@Override
	public void linkRemoved(ILinkModel link) {
	}

	@Override
	public void nodeAdded(INodeModel n) {
	}

	@Override
	public void nodeRemoved(INodeModel n) {
	}

	@Override
	public void updated(IDiagramModel diagramModel) {
	}

	@Override
	public void nodeSelected(INodeModel n) {
		log.info("PedagogicalPlanPanel.nodeSelected");
	}

	@Override
	public void moved(INodeModel node) {
	}

	@Override
	public void resized(INodeModel node) {
	}

	@Override
	public void labelChanged(INodeModel node) {
	}

	@Override
	public void shapeChanged(INodeModel node) {
	}

	@Override
	public void styleChanged(INodeModel node) {

	}

	@Override
	public void selectionChanged(INodeModel conceptNode) {
	}

	@Override
	public void deleted(INodeModel nodeModel) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}