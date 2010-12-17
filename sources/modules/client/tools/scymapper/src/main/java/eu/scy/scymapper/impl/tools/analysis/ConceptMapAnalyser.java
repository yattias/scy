package eu.scy.scymapper.impl.tools.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;

public class ConceptMapAnalyser {
	
	private static final String DEFAULT_CSV_FILE_NAME = "analysis.csv";
	
	private static final String DEFAULT_FILE_PATH = "StudienDaten" + File.separator  + "schueler";

	private String filepath;

	private List<Student> studentList;
	
	private int maxConceptCount = 0;
	
	public ConceptMapAnalyser(String filepath) {
		this.filepath = filepath;
		this.studentList = new ArrayList<Student>();
	}
	
	public List<Student> getStudentList() {
		return studentList;
	}
	
	public int getMaxConceptCount() {
		return maxConceptCount;
	}
	
	public void readStudentList() {
		List<String> directories = getDirectories();
		if(directories == null) {
			return;
		}
		for(String dir : directories) {
			String helpMode = getHelpMode(dir);
			for(File file : getXmlFiles(dir)) {
				try {
					String[] splittedFileName = file.getName().split("_");
					if(splittedFileName.length < 2) {
						continue;
					}

					Student student = new Student(dir, splittedFileName[1], helpMode, xmlToIConceptMap(file));
					this.studentList.add(student);
					if(this.maxConceptCount < student.getConceptMap().getDiagram().getNodes().size()) {
						this.maxConceptCount = student.getConceptMap().getDiagram().getNodes().size();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	private List<String> getDirectories() {
		List<String> directories = new ArrayList<String>();

		File tempPath = new File(this.filepath);
		File[] listFiles = tempPath.listFiles();
		if(listFiles == null) {
			System.err.println("No directories found");
			return null;
		}
		for(File file : listFiles) {
			if(file.isDirectory()) {
				directories.add(file.getName());
			}
		}
		return directories;
	}

	private File[] getXmlFiles(String userDir) {
		File tempPath = new File(this.filepath + File.separator + userDir);

		return tempPath.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return ((new File(dir, name)).isFile() && name.toLowerCase().startsWith("scymapper_") && name.toLowerCase().endsWith(".xml"));
			}			
		});
	}
	
	private String getHelpMode(String userDir) {
		File tempPath = new File(this.filepath + File.separator + userDir);
		
		File[] listFiles = tempPath.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return ((new File(dir, name)).isFile() && name.equals(SCYMapperStandaloneConfig.LOCAL_CONFIG_FILE));
			}			
		});

		if(listFiles.length != 1) {
			return "Error";
		}

        if (listFiles[0].canRead()) {
            Reader reader = null;
            try {
            	Properties props = new Properties();
                reader = new FileReader(listFiles[0]);
                props.load(reader);
                
                return props.getProperty(SCYMapperStandaloneConfig.KEY_HELP_MODE, "Error");
            } catch (IOException e) {
                return "Error";
            } finally {
                try {
                	if(reader != null) {
                		reader.close();
                	}
                } catch (IOException e) {
                    // TODO
                }
            }
        }
        return "Error";
	}
	
	private IConceptMap xmlToIConceptMap(File xmlFile) throws FileNotFoundException {		
		FileReader fr = new FileReader(xmlFile);
        XStream xstream = new XStream(new DomDriver());
        return (IConceptMap) xstream.fromXML(fr);
	}
	
	private void writeCsvFile(String filename) {
		if(studentList.size() == 0) {
			System.out.println("No student data loaded");
			return;
		}
		File csv = new File(filename);
		try {
			if (!csv.exists()) {
				csv.createNewFile();
			}
			FileWriter fw = null;
			try {
				if(csv.canWrite()) {
					fw = new FileWriter(csv);
					for(Student student : studentList) {
						fw.write(student.getCsvLine());
						fw.append(System.getProperty("line.separator"));
					}
				}
			} finally {
				if(fw != null) {
					fw.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {

		String filepath;
		if(args == null || args.length == 0) {
//			filepath = ".";
			filepath = DEFAULT_FILE_PATH;
		} else {
			filepath = args[0];
		}
			
		ConceptMapAnalyser cmAnalyser = new ConceptMapAnalyser(filepath);
		cmAnalyser.readStudentList();
		System.out.println("Read " + cmAnalyser.getStudentList().size() + " student concept maps");

		cmAnalyser.writeCsvFile(DEFAULT_CSV_FILE_NAME);
	}
}
