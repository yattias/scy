package eu.scy.client.tools.scydynamics.logging.parser;

import java.io.File;
import java.util.Properties;

import colab.um.JColab;
import colab.um.tools.JTools;

import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.model.ModelUtils;

public class LogParser {
	
	public LogParser(Properties props) {
		new JTools(JColab.JCOLABAPP_RESOURCES, JColab.JCOLABSYS_RESOURCES);
		Domain domain = ModelUtils.loadDomain(props);
		ParserModel model = new ParserModel(domain);
		ParserView view = new ParserView(model);	
		ParserControl control = new ParserControl(model, view, domain);
		model.setDirectory(new File(props.getProperty("parser.directory", ".")));
		view.updateView();
		view.setVisible(true);
	}

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("editor.reference_model", "/eu/scy/client/tools/scydynamics/resources/domains/fes/exp_reference_model.xml");
		props.put("editor.concept_set", "/eu/scy/client/tools/scydynamics/resources/domains/fes/exp_concept_set.xml");
		props.put("editor.simulation_settings", "/eu/scy/client/tools/scydynamics/resources/domains/fes/exp_simulation_settings.xml");
		
		//props.put("parser.directory", "D:\\media\\Dropbox\\FES_SCYDynamics\\data\\exp_sessies\\marianum");
		//props.put("parser.directory", "D:\\media\\Dropbox\\FES_SCYDynamics\\data\\exp_sessies\\lesgroep5");
		props.put("parser.directory", "D:\\media\\Dropbox\\FES_SCYDynamics\\data\\exp_sessies");
		//props.put("parser.directory", "."+System.getProperty("file.separator")+"data"+System.getProperty("file.separator")+"exp_sessies");
		LogParser parser = new LogParser(props);
	}
	
}
