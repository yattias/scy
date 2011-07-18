package eu.scy.client.tools.scydynamics.model;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JTextField;

import colab.um.draw.JdFigure;
import colab.um.draw.JdLink;
import colab.um.draw.JdRelation;
import eu.scy.client.tools.scydynamics.domain.Domain;

public class ModelUtils {
	
	public enum QualitativeInfluenceType {UNSPECIFIED, LINEAR_UP, LINEAR_DOWN, CURVE_UP, CURVE_DOWN, ASYMPTOTE_UP};
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

	public static String getQualitativeExpression(HashMap<JdFigure, QualitativeInfluenceType> qualitativeRelations, Model model) {
		String expression = "";
		for (JdFigure figure: qualitativeRelations.keySet()) {
			String varName = (String) figure.getProperties().get("label");
			String newExpression;
			switch (qualitativeRelations.get(figure)) {
			case LINEAR_UP:
				newExpression = varName;
				break;
			case LINEAR_DOWN:
				newExpression = "(-"+varName+")";
				break;
			case CURVE_UP:
				newExpression = varName+"*"+varName;
				break;
			case CURVE_DOWN:
				newExpression = "1/"+varName;
				break;
			case ASYMPTOTE_UP:
				newExpression = "(1-exp("+varName+"))";
				break;
			default:
				newExpression = " error ";
				break;
			}
			if (expression.isEmpty()) {
				expression = newExpression;
			} else {
				expression = expression+"*"+newExpression;
			}
		}
		return expression;
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
