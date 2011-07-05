package eu.scy.client.tools.scydynamics.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TestConceptSet {

	private ConceptSet conceptSet;
	private String fileName = "concept_set_mulder_2009.xml";

	public TestConceptSet() throws FileNotFoundException {
		unmarshalConceptSet();
		//createConceptSet();
		marshalConceptSet(System.out);
		marshalConceptSet(new FileOutputStream("test_conceptSet.xml"));
	}

	@SuppressWarnings("unused")
	private void createConceptSet() {
		conceptSet = new ConceptSet();
		conceptSet.setName("testName");
		conceptSet.setWordOrder("any");
		
		Concept c = new Concept();
		c.setName("testConcept");
		Term t1 = new Term();
		t1.setTerm("testTerm1");
		t1.setType("testtype");
		Term t2 = new Term();
		t2.setTerm("testTerm2");
		t2.setType("testtype2");
		ArrayList<Term> termList = new ArrayList<Term>();
		termList.add(t1);
		termList.add(t2);
		c.setTerms(termList);
		
		ArrayList<Concept> conceptList = new ArrayList<Concept>();
		conceptList.add(c);
		
		conceptSet.setConcepts(conceptList);		
	}

	private void unmarshalConceptSet() {
		try {
			JAXBContext context = JAXBContext.newInstance(ConceptSet.class); 
			Unmarshaller um = context.createUnmarshaller();
			File f = new File(fileName);
			System.out.println(f.getAbsolutePath());
			System.out.println("exists: "+f.exists());
			conceptSet = (ConceptSet) um.unmarshal(new FileReader(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private void marshalConceptSet(OutputStream out) {
		try {
			JAXBContext context = JAXBContext.newInstance(ConceptSet.class );
			Marshaller m = context.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			m.marshal(conceptSet, out);
		} catch (Exception ex) {
			Logger.getLogger(TestConceptSet.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		new TestConceptSet();
	}


}
