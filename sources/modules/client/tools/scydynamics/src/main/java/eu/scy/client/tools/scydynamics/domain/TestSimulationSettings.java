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

public class TestSimulationSettings {

	private String fileName = "default_simulation_settings.xml";
	private SimulationSettings simulationSettings;

	public TestSimulationSettings() throws FileNotFoundException {
		//unmarshalReferenceModel();
		createSimulationSettings();
		marshalReferenceModel(System.out);
		marshalReferenceModel(new FileOutputStream(fileName));	
	}

	private void unmarshalReferenceModel() {
		try {
			JAXBContext context = JAXBContext.newInstance(ReferenceModel.class); 
			Unmarshaller um = context.createUnmarshaller();
			File f = new File(fileName);
			System.out.println(f.getAbsolutePath());
			System.out.println("exists: "+f.exists());
			simulationSettings = (SimulationSettings) um.unmarshal(new FileReader(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void marshalReferenceModel(OutputStream out) {
		try {
			JAXBContext context = JAXBContext.newInstance(SimulationSettings.class );
			Marshaller m = context.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			m.marshal(simulationSettings, out);
		} catch (Exception ex) {
			Logger.getLogger(TestSimulationSettings.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings("unused")
	private void createSimulationSettings() {
		simulationSettings = new SimulationSettings();
		simulationSettings.setStartTime(0.0);
		simulationSettings.setStopTime(100.0);
		simulationSettings.setStepSize(1.0);
		simulationSettings.setCalculationMethod("RungeKuttaFehlberg");
	}

	public static void main(String[] args) throws FileNotFoundException {
		new TestSimulationSettings();
	}


}
