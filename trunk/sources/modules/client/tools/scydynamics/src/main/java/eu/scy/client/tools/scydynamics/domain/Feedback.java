package eu.scy.client.tools.scydynamics.domain;

import java.util.HashSet;
import java.util.logging.Logger;

import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdLink;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;

import eu.scy.client.tools.scydynamics.domain.Concept.ConceptType;
import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.client.tools.scydynamics.model.QualitativeInfluenceType;

public class Feedback {

	private final static Logger debugLogger = Logger.getLogger(Feedback.class.getName());

	private int correctNames = 0;
	private int incorrectNames = 0;
	private int unnamed = 0;
	private int correctRelations = 0;
	private int incorrectRelations = 0;
	private int correctDirections = 0;
	private int incorrectDirections = 0;
	private int correctNodeType = 0;
	private int incorrectNodeType = 0;
	private int unknownNodeType = 0;
	private int unknownLinkTypes = 0;
	private int correctLinkTypes = 0;
	private int incorrectLinkTypes = 0;

	public Feedback(Model model, Domain domain) {
		if (model == null || domain == null) {
			createFakeFeedback();
		} else {
			// the nodes
			HashSet<String> correctNodeNames = new HashSet<String>();
			int tempIncorrectNodes = 0;
			int tempNoNameNodes = 0;
			for (String nodeName: model.getNodes().keySet()) {
				if (nodeName.toLowerCase().startsWith("stock_")
						||	nodeName.toLowerCase().startsWith("aux_")
						|| nodeName.toLowerCase().startsWith("const_"))
				{
					tempNoNameNodes++;
					unknownNodeType++;
				} else if (domain.proposeNames(nodeName) == null) {
					// nodeName is found in concept set
					ConceptType conceptType = null;
					String conceptName = domain.getConceptNameByTerm(nodeName);
					Concept concept = domain.getConceptSet().getConcept(conceptName);
					conceptType = concept.getType();
					if (conceptType != null && (conceptType.equals(ConceptType.DOMAIN_RELATED) || conceptType.equals(ConceptType.DOMAIN_UNRELATED))) {
						tempIncorrectNodes++;
						unknownNodeType++;
					} else {
						correctNodeNames.add(nodeName);
						JdObject node = model.getObjectOfName(nodeName);
						if ((node instanceof JdAux) && conceptType.equals(ConceptType.AUXILIARY)) {
							correctNodeType++;
						} else if ((node instanceof JdStock) && conceptType.equals(ConceptType.STOCK)) {
							correctNodeType++;
						} else if ((node instanceof JdConst) && conceptType.equals(ConceptType.CONSTANT)) {
							correctNodeType++;
						} else {
							incorrectNodeType++;
						}
					}
				} else {
					tempIncorrectNodes++;
					unknownNodeType++;
				}
			}
			this.setCorrectNames(correctNodeNames.size());
			this.setIncorrectNames(tempIncorrectNodes);
			this.setUnnamed(tempNoNameNodes);

			// the edges
			int tempCorrectRelations = 0;
			int tempIncorrectRelations = 0;
			int tempCorrectDirections = 0;
			int tempIncorrectDirections = 0;
			for (JdLink link: model.getLinks()) {
				if (link instanceof JdRelation) {
					if (isRelationInDomain(model, domain, link.getFigure1(), link.getFigure2())) {
						tempCorrectRelations++;
						tempCorrectDirections++;
						// candidate for correctLinkType or unknownLinkType
						String fromNode = (String) link.getFigure1().getProperties().get("label");
						String toNode = (String) link.getFigure2().getProperties().get("label");
						QualitativeInfluenceType type = QualitativeInfluenceType.fromInt(((JdRelation) link).getRelationType());
						if (fromNode != null && toNode != null && type.equals(QualitativeInfluenceType.UNSPECIFIED)) {
							unknownLinkTypes++;
						} else if (fromNode != null && toNode != null) {
							Edge edge = domain.getEdgeBetweenNodeTerms(fromNode.toLowerCase(), toNode.toLowerCase());
							String expression = edge.getExpression(type);
							if (expression == null || expression.isEmpty()) {
								// no expression could be found in the reference model -> incorrect relation type
								incorrectLinkTypes++;
							} else {
								// expression found -> correct relation type
								correctLinkTypes++;
							}
						}
					} else if (isRelationInDomain(model, domain, link.getFigure2(), link.getFigure1())) {
						tempCorrectRelations++;
						tempIncorrectDirections++;
						incorrectLinkTypes++;
					} else {
						tempIncorrectRelations++;
						incorrectLinkTypes++;
					}
				} else if (link instanceof JdFlow) {
					try {
						String fromNode = (String) link.getFigure1().getProperties().get("label");
						String toNode = (String) link.getFigure2().getProperties().get("label");
						if (fromNode != null && toNode != null) {
							if (isRelationInDomain(model, domain, link.getFigure1(), link.getFigure2())) {
								// flow edge found, direction correct
								tempCorrectRelations++;
								tempCorrectDirections++;
							} else if (isRelationInDomain(model, domain, link.getFigure2(), link.getFigure1())) {
								// flow edge found, direction incorrect
								tempCorrectRelations++;
								tempIncorrectDirections++;
							} else {
								// wrong flow edge
								tempIncorrectRelations++;
							}
						}
					} catch (Exception ex) {
						// TODO: we are also landing here is there's an anonymous stock
						// but this could be correct in a model
						tempIncorrectRelations++;
					}
				}
			}
			this.setCorrectRelations(tempCorrectRelations);
			this.setIncorrectRelations(tempIncorrectRelations);
			this.setCorrectDirections(tempCorrectDirections);
			this.setIncorrectDirections(tempIncorrectDirections);
		}
	}	private void createFakeFeedback() {
		debugLogger.warning("model or domain == null, creating a fake dataset.");
		this.setCorrectNames(1);
		this.setIncorrectNames(2);
		this.setUnnamed(3);
		this.setCorrectRelations(4);
		this.setIncorrectRelations(5);
		this.setCorrectDirections(6);
		this.setIncorrectDirections(7);
	}

	public Feedback(String feedbackString) throws Exception {
		String[] feedback = feedbackString.split(",");
		setCorrectNames(Integer.parseInt(feedback[0]));
		setIncorrectNames(Integer.parseInt(feedback[1]));
		setUnnamed(Integer.parseInt(feedback[2]));
		setCorrectRelations(Integer.parseInt(feedback[3]));
		setIncorrectRelations(Integer.parseInt(feedback[4]));
		setCorrectDirections(Integer.parseInt(feedback[5]));
		setIncorrectDirections(Integer.parseInt(feedback[6]));
	}

	public int getCorrectNodeType() {
		return correctNodeType;
	}

	public int getIncorrectNodeType() {
		return incorrectNodeType;
	}
	
	public int getUnknownNodeType() {
		return unknownNodeType;
	}
	
	public int getCorrectNames() {
		return correctNames;
	}

	public void setCorrectNames(int correctNames) {
		this.correctNames = correctNames;
	}

	public int getIncorrectNames() {
		return incorrectNames;
	}

	public int getUnknownLinkTypes() {
		return unknownLinkTypes;
	}
	public int getCorrectLinkTypes() {
		return correctLinkTypes;
	}
	public int getIncorrectLinkTypes() {
		return incorrectLinkTypes;
	}
	public void setIncorrectNames(int incorrectNames) {
		this.incorrectNames = incorrectNames;
	}

	public int getUnnamed() {
		return unnamed;
	}

	public void setUnnamed(int unnamed) {
		this.unnamed = unnamed;
	}

	public int getCorrectRelations() {
		return correctRelations;
	}

	public void setCorrectRelations(int correctRelations) {
		this.correctRelations = correctRelations;
	}

	public int getIncorrectRelations() {
		return incorrectRelations;
	}

	public void setIncorrectRelations(int incorrectRelations) {
		this.incorrectRelations = incorrectRelations;
	}

	public int getCorrectDirections() {
		return correctDirections;
	}

	public void setCorrectDirections(int correctDirections) {
		this.correctDirections = correctDirections;
	}

	public int getIncorrectDirections() {
		return incorrectDirections;
	}

	public void setIncorrectDirections(int incorrectDirections) {
		this.incorrectDirections = incorrectDirections;
	}

	private JdStock getIncomingStock(Model model, JdFlowCtr flowCtr) {
		for (JdStock stock : model.getStocks()) {
			if (((JdFlow) (flowCtr.getParent())).getFigure2() == stock) {
				return stock;
			}
		}
		return null;
	}

	private JdStock getOutgoingStock(Model model, JdFlowCtr flowCtr) {
		for (JdStock stock : model.getStocks()) {
			if (((JdFlow) (flowCtr.getParent())).getFigure1() == stock) {
				return stock;
			}
		}
		return null;
	}

	private boolean isRelationInDomain(Model model, Domain domain, JdFigure from, JdFigure to) {
		try {
			if (from == null || to == null) {
				return false;
			}

			if (from instanceof JdFlowCtr) {
				// System.out.println("an edge FROM a jdflowctr? this shouldn't happen, returning false.");
				return false;
			} else if (to instanceof JdFlowCtr) {
				JdFlowCtr flowCtr = (JdFlowCtr) to;
				JdStock incomingStock = getIncomingStock(model, flowCtr);
				JdStock outgoingStock = getOutgoingStock(model, flowCtr);
				if (incomingStock != null) {
					// situation:
					// "from" is a constant or aux
					// "to" is a flow-ctr that is incoming to a stock
					String incomingConcept = domain.getConceptNameByTerm(incomingStock.getProperties().get("label").toString());
					String incomingId = domain.getNodeByConcept(incomingConcept).getId();
					incomingId = incomingId + "_in";
					String nodeConcept = domain.getConceptNameByTerm(from.getProperties().get("label").toString());
					String nodeId = domain.getNodeByConcept(nodeConcept).getId();
					return domain.getEdgeBetweenNodeIds(nodeId, incomingId) != null;
				} else if (outgoingStock != null) {
					// situation:
					// "from" is a constant or aux
					// "to" is a flow-ctr that is outgoing from a stock
					String outgoingConcept = domain.getConceptNameByTerm(outgoingStock.getProperties().get("label").toString());
					String outgoingId = domain.getNodeByConcept(outgoingConcept).getId();
					outgoingId = outgoingId + "_out";
					String nodeConcept = domain.getConceptNameByTerm(from.getProperties().get("label").toString());
					String nodeId = domain.getNodeByConcept(nodeConcept).getId();
					return domain.getEdgeBetweenNodeIds(nodeId, outgoingId) != null;
				} else {
					return false;
				}
			} else {
				// "regular" relation, check with domain
				// System.out.println("***** regular relation, chechking domain.");
				String label1 = from.getProperties().get("label").toString();
				String label2 = to.getProperties().get("label").toString();
				return domain.getEdgeBetweenNodeTerms(label1, label2) != null;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public int getCorrectnessRatio() {
		int correctSum = getCorrectNames() + getCorrectRelations() + getCorrectDirections();
		int incorrectSum = getIncorrectNames() + getIncorrectRelations() + getIncorrectDirections();
		return correctSum - incorrectSum;
	}

	@Override
	public String toString() {
		String resultAsString = getCorrectNames() + ",";
		resultAsString = resultAsString.concat(getIncorrectNames() + ",");
		resultAsString = resultAsString.concat(getUnnamed() + ",");
		resultAsString = resultAsString.concat(getCorrectRelations() + ",");
		resultAsString = resultAsString.concat(getIncorrectRelations() + ",");
		resultAsString = resultAsString.concat(getCorrectDirections() + ",");
		resultAsString = resultAsString.concat(getIncorrectDirections() + "");
		return resultAsString;
	}

}
