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
import org.apache.log4j.Logger;

public class ConceptMapAnalyser {

	private final static Logger logger = Logger.getLogger(ConceptMapAnalyser.class);
	
	private static final String DEFAULT_CSV_FILE_NAME = "analysis.csv";
	
	private static final String DEFAULT_STUDENT_DIR = File.separator  + "StudienDaten" + File.separator  + "schueler";
	
	private static final String DEFAULT_ACTION_LOG_DIR = File.separator + "StudienDaten";

	private String studentDirectory;
	
	private String actionLogDirectory;

	private List<Student> studentList;
	
	private int maxConceptCount = 0;
	
	public ConceptMapAnalyser(String studentDir, String actionLogDir) {
		if(!studentDir.endsWith(File.separator)) {
			studentDir = studentDir + File.separator;
		}
		if(!actionLogDir.endsWith(File.separator)) {
			actionLogDir = actionLogDir + File.separator;
		}

		String currentDir;
		if(new File(studentDir).isAbsolute()) {
			currentDir = studentDir;
		}
		else {
			currentDir = ".";
		}
		
		try {			
			currentDir = new File(".").getCanonicalPath();
		} catch (IOException ex) {
			logger.debug("Could not determine canonical path");
		}
		this.studentDirectory = currentDir + studentDir;
		this.actionLogDirectory = currentDir + actionLogDir;
		this.studentList = new ArrayList<Student>();
	}
	
	public List<Student> getStudentList() {
		return studentList;
	}
	
	public int getMaxConceptCount() {
		return maxConceptCount;
	}
	
	public void readStudentXmlFiles() {
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

					Student student = new Student(new File(dir), splittedFileName[1], helpMode, xmlToIConceptMap(file));
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
	
	public void readStudentActionLogs() {
		for(Student student : studentList) {
			int actionLogCount = 0;
			try {
				actionLogCount = student.readActionLogs(this.actionLogDirectory);
			} catch(Exception ex) {
				logger.debug("Could not read action log file for help mode: " + student.getActionLogFile(this.actionLogDirectory));
				return;
			}
			logger.debug("Successfully read " + actionLogCount + " log entries for user " + student.getName());			
		}
	}

	private List<String> getDirectories() {
		List<String> directories = new ArrayList<String>();

		File tempPath = new File(this.studentDirectory);
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
		File tempPath = new File(this.studentDirectory + File.separator + userDir);

		return tempPath.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return ((new File(dir, name)).isFile() && name.toLowerCase().startsWith("scymapper_") && name.toLowerCase().endsWith(".xml"));
			}			
		});
	}
	
	private String getHelpMode(String userDir) {
		File tempPath = new File(this.studentDirectory + File.separator + userDir);
		
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
			logger.debug("No student data loaded");
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
					fw.write("Directory,UserID,HelpMode,#Concepts,#Relations,#Lexicon called,#Concept help requests,#Relation help requests,#Synonyms added");
					fw.append(System.getProperty("line.separator"));

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
		String studentDir = "";
		String actionLogDir = "";
		if(args == null || args.length == 0) {
//			filepath = ".";
			studentDir = DEFAULT_STUDENT_DIR;
			actionLogDir = DEFAULT_ACTION_LOG_DIR;
		} else if(args[0].equals("--help")) {
			// System.out.println("Parameter: command <RelativeStudentDirectory> <ActionLogDirectory>");
			// System.out.println("Default Student Directory: " + DEFAULT_STUDENT_DIR);
			// System.out.println("Default ActionLog Directory: " + DEFAULT_ACTION_LOG_DIR);
			System.exit(0);
		} else {
			studentDir = args[0];
			actionLogDir = args[1];
		}
			
		ConceptMapAnalyser cmAnalyser = new ConceptMapAnalyser(studentDir, actionLogDir);
		cmAnalyser.readStudentXmlFiles();
		logger.debug("Read " + cmAnalyser.getStudentList().size() + " student concept maps out of " + cmAnalyser.getDirectories().size() + " directories." );

		logger.debug("Begin reading action logs. This may take some time!");
		cmAnalyser.readStudentActionLogs();
		logger.debug("Reading of action logs has been completed.");

		cmAnalyser.writeCsvFile(DEFAULT_CSV_FILE_NAME);
	}
}
