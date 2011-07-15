package eu.scy.client.tools.scydynamics.domain;


public class TestDomain {

	public TestDomain() throws Exception {
		Domain domain = new Domain("reference_model_mulder_2009.xml", "concept_set_mulder_2009.xml", "default_simulation_settings.xml");
		System.out.println("number of nodes: "+domain.getReferenceModel().getNodes().size());
		System.out.println("number of concepts: "+domain.getConceptSet().getConcepts().size());
	}

	public static void main(String[] args) throws Exception {
		new TestDomain();
	}

}