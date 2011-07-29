package eu.scy.client.tools.scydynamics.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import eu.scy.client.tools.scydynamics.model.QualitativeInfluenceType;

public class TestReferenceModel {

	private String fileName = "test_referenceModel.xml";
	private ReferenceModel referenceModel;

	public TestReferenceModel() throws FileNotFoundException {
		unmarshalReferenceModel();
		//createReferenceModel();
		marshalReferenceModel(System.out);
		//marshalReferenceModel(new FileOutputStream("test_referenceModel.xml"));
		
		System.out.println(referenceModel.getNodes().size());
	}

	private void unmarshalReferenceModel() {
		try {
			JAXBContext context = JAXBContext.newInstance(ReferenceModel.class); 
			Unmarshaller um = context.createUnmarshaller();
			File f = new File(fileName);
			System.out.println(f.getAbsolutePath());
			System.out.println("exists: "+f.exists());
			referenceModel = (ReferenceModel) um.unmarshal(new FileReader(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void marshalReferenceModel(OutputStream out) {
		try {
			JAXBContext context = JAXBContext.newInstance(ReferenceModel.class );
			Marshaller m = context.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			m.marshal(referenceModel, out);
		} catch (Exception ex) {
			Logger.getLogger(TestReferenceModel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings("unused")
	private void createReferenceModel() {
		referenceModel = new ReferenceModel();
		
		Node node1 = new Node();
		node1.setId("a");
		node1.setFormula("100");
		node1.setType("stock");
//		ArrayList<String> node1Names = new ArrayList<String>();
//		node1Names.add("a1");
//		node1Names.add("a2");
//		node1Names.add("a3");
		//node1.setTerms(node1Names);
		
		Node node2 = new Node();
		node2.setId("b");
		node2.setType("stock");
//		ArrayList<String> node2Names = new ArrayList<String>();
//		node2Names.add("b1");
//		node2Names.add("b2");
//		node2Names.add("b3");
		//node2.setTerms(node2Names);
		
		Node node3 = new Node();
		node3.setId("c");
		node3.setType("aux");
		node3.setFormula("b*0.1");
//		ArrayList<String> node3Names = new ArrayList<String>();
//		node3Names.add("c1");
//		node3Names.add("c2");
//		node3Names.add("c3");
		//node3.setTerms(node3Names);

		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);

		Edge relation1 = new Edge();
		relation1.setFrom("a");
		relation1.setTo("b");
		//relation1.setQualitative("inverse");

		Edge relation2 = new Edge();
		relation2.setFrom("c");
		relation2.setTo("rel1");
		List<Expression> expressions = new ArrayList<Expression>();
		expressions.add(new Expression(QualitativeInfluenceType.LINEAR_UP, "test*bla"));
		expressions.add(new Expression(QualitativeInfluenceType.LINEAR_DOWN, "(-test*bla)"));
		relation2.setExpressions(expressions);
		//relation2.setQualitative("linear");

		ArrayList<Edge> relations = new ArrayList<Edge>();
		relations.add(relation1);
		relations.add(relation2);

		referenceModel.setNodes(nodes);
		referenceModel.setEdges(relations);
	}

	public static void main(String[] args) throws FileNotFoundException {
		new TestReferenceModel();
	}


}
