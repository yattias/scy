package eu.scy.client.tools.scydynamics.model;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JTextField;

import colab.um.draw.JdFigure;
import colab.um.draw.JdLink;
import colab.um.draw.JdRelation;
import eu.scy.client.tools.scydynamics.domain.Concept;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.domain.Edge;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;

public class ModelUtils {
	
	private final static Logger DEBUGLOGGER = Logger.getLogger(ModelUtils.class.getName());
	
	public static JdRelation getRelationBetween(Model model, String start, String end) {	
		JdLink link = model.getLink(start, end);
		if (link != null && link instanceof JdRelation)
			return (JdRelation) link;
		else {
			return null;
		}
	}
	
    public static Vector<String> getInputVariableNames(Model model, String target) {
        // gets the name of all JdFigures that are linked to (input-wise) this.figure
        Vector<String> listItems = new Vector<String>();
        Vector<JdLink> links = model.getLinks();
        for (JdLink link : links) {
            try {
                // some figures don't have a label (e.g. flows),
            	// catching a nullpointer here
                if (link.getFigure2().getProperties().get("label").equals(target)) {
                    listItems.add((String) link.getFigure1().getProperties().get("label"));
                }
            } catch (NullPointerException ex) {
                // do nothing
            }
        }
        return listItems;
    }

	public static String getQualitativeExpression(JdFigure figure, HashMap<JdFigure, QualitativeInfluenceType> qualitativeRelations, ModelEditor modelEditor) {
		String expression = "";
		for (JdFigure otherFigure: qualitativeRelations.keySet()) {		
			String newExpression = null;
			// trying to get expression from domain
			newExpression = getExpressionFromDomain(figure, otherFigure, qualitativeRelations, modelEditor);
			if (newExpression == null) {
				// no expression from domain, generate it
				newExpression = generateExpressionFromRelation((String) otherFigure.getProperties().get("label"), qualitativeRelations.get(otherFigure));
			}
			
			if (expression.isEmpty()) {
				expression = newExpression;
			} else {
				expression = expression+"*"+newExpression;
			}
		}
		return expression;
	}
	
	private static String getExpressionFromDomain(JdFigure thisFigure, JdFigure otherFigure, HashMap<JdFigure, QualitativeInfluenceType> qualitativeRelations, ModelEditor modelEditor) {
		try {
			String thisName = (String) thisFigure.getProperties().get("label");
			String otherName = (String) otherFigure.getProperties().get("label");
			Edge edge = modelEditor.getDomain().getEdgeBetweenNodeTerms(otherName.toLowerCase(), thisName.toLowerCase());
			String expression = edge.getExpression(qualitativeRelations.get(otherFigure));
			System.out.println("expression from domain: "+expression);
			expression = expression.replaceAll(modelEditor.getDomain().getConceptByTerm(otherName), otherName);
			System.out.println("expression adapted: "+expression);
			return expression;
		} catch (Exception ex) {
			return null;
		}
	}

	public static String generateExpressionFromRelation(String varName, QualitativeInfluenceType relationType) {
		switch (relationType) {
		case LINEAR_UP:
			return varName;
		case LINEAR_DOWN:
			return "(-"+varName+")";
		case CURVE_UP:
			return varName+"*"+varName;
		case CURVE_DOWN:
			return "1/"+varName;
		case ASYMPTOTE_UP:
			return "(1-exp("+varName+"))";
		default:
			return " error ";
		}
	}
	
	public static Domain loadDomain(Properties props) {
		Domain domain = null;
		String referenceModelFilename = props.getProperty("editor.reference_model");
		String conceptSetFilename = props.getProperty("editor.concept_set");
		String simulationSettingsFilename = props.getProperty("editor.simulation_settings");
		try {
			domain = new Domain(referenceModelFilename, conceptSetFilename, simulationSettingsFilename);
		} catch (Exception e) {
			DEBUGLOGGER.info("domain could not be loaded, will be ignored.");
			DEBUGLOGGER.info(e.getMessage());
		}
		return domain;
	}
	
	/*
	 * This method inserts the String "s" at the right position into the
	 * JTextField "field", taking a selection of text into account.
	 */
	public static void paste(String s, JTextField field) {
		int start = field.getSelectionStart();
		int end = field.getSelectionEnd();
		String oldText = field.getText();
		String newText = oldText.substring(0, start) + s
		+ oldText.substring(end, oldText.length());
		field.setText(newText);
	}

}
