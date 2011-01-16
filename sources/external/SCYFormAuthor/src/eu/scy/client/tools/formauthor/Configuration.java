package eu.scy.client.tools.formauthor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Configuration {
	// TODO CONFIGPATH WINDOWS UND MAC
	private static String CONFIGFILENAME = getWorkingDirectory() + "/"
			+ "config.xml";
	private final static String APPNAME = "DataCollector";
	private String UserName;
	private String ServerUrl;
	private String Password;
	private String Groupname;
	private String Language;

	public Configuration() {
		getConfigurationFromFile();
	}

	public void saveConfigurationToFile() {
		try {
			FileOutputStream fos = new FileOutputStream(
					new File(CONFIGFILENAME));
			// String langID = null;

			// if (getLanguage() == "Deutsch")
			// langID = "de";
			// if (getLanguage() == "English")
			// langID = "en";

			String xml = "<?xml version='1.0'?>" + "<configuration>"
					+ "<groupname>" + getGroupname() + "</groupname>"
					+ "<username>" + getUserName() + "</username>"
					+ "<password>" + getPassword() + "</password>"
					+ "<serverurl>" + getServerUrl() + "</serverurl>"
					+ "<language>" + getLanguage() + "</language>"
					+ "</configuration>";
			fos.write(xml.getBytes());
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public String getLanguage() {
		// TODO Auto-generated method stub
		return Language;
	}

	private void getConfigurationFromFile() {
		try {
			// File file = application.getDir("Configuration",
			// Context.MODE_WORLD_READABLE);
			FileInputStream fis = new FileInputStream(CONFIGFILENAME);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fis);
			doc.getDocumentElement().normalize();
			NodeList nl;
			nl = doc.getElementsByTagName("username");
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getFirstChild() != null)
					this.setUserName(nl.item(i).getFirstChild().getNodeValue());
				else
					setUserName("");
			}
			nl = doc.getElementsByTagName("serverurl");
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getFirstChild() != null)
					this
							.setServerUrl(nl.item(i).getFirstChild()
									.getNodeValue());
				else
					setServerUrl("");
			}
			nl = doc.getElementsByTagName("password");
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getFirstChild() != null)
					this.setPassword(nl.item(i).getFirstChild().getNodeValue());
				else
					setPassword("");
			}
			nl = doc.getElementsByTagName("groupname");
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getFirstChild() != null)
					this
							.setGroupname(nl.item(i).getFirstChild()
									.getNodeValue());
				else
					setGroupname("");
			}
			nl = doc.getElementsByTagName("language");
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getFirstChild() != null)
					this.setLanguage(nl.item(i).getFirstChild().getNodeValue());
				else
					setLanguage("");
			}

		} catch (Exception e) {
			try {
				if (e.getClass().equals(FileNotFoundException.class)) {
					File configFile = new File(CONFIGFILENAME);
					FileOutputStream fos = new FileOutputStream(configFile);

					String xml = "<?xml version='1.0'?><configuration><username></username><serverurl></serverurl><language>de</language></configuration>";
					fos.write(xml.getBytes());
					getConfigurationFromFile();
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}

	public void setLanguage(String language) {
		// TODO Auto-generated method stub
		// String langID = null;
		if (language == "Deutsch")
			language = "de";
		if (language == "English")
			language = "en";

		Language = language;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserName() {
		return UserName;
	}

	public void setServerUrl(String serverUrl) {
		ServerUrl = serverUrl;
	}

	public String getServerUrl() {
		return ServerUrl;
	}

	public String getPassword() {
		return Password;
	}

	public String getGroupname() {
		return Groupname;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public void setGroupname(String groupname) {
		Groupname = groupname;
	}

	/**
	 * Returns the appropriate working directory for storing application data.
	 * The result of this method is platform dependant: On linux, it will return
	 * ~/applicationName, on windows, the working directory will be located in
	 * the user's application data folder. For Mac OS systems, the working
	 * directory will be placed in the proper location in
	 * "Library/Application Support".
	 * <p/>
	 * This method will also make sure that the working directory exists. When
	 * invoked, the directory and all required subfolders will be created.
	 * 
	 * @param applicationName
	 *            Name of the application, used to determine the working
	 *            directory.
	 * @return the appropriate working directory for storing application data.
	 */
	public static File getWorkingDirectory() {
		final String userHome = System.getProperty("user.home", ".");
		File workingDirectory = null;

		final String sysName = System.getProperty("os.name").toLowerCase();
		if (sysName.contains("windows")) {
			final String applicationData = System.getenv("APPDATA");
			if (applicationData != null)
				workingDirectory = new File(applicationData, "." + APPNAME
						+ '/');
			else
				workingDirectory = new File(userHome, '.' + APPNAME + '/');
		} else if (sysName.contains("mac")) {
			workingDirectory = new File(userHome,
					"Library/Application Support/" + APPNAME);
		} else if (sysName.contains("linux")) {
		} /* ... */
		else if (sysName.contains("solaris")) {
			workingDirectory = new File(userHome, '.' + APPNAME + '/');
		} else {
		} /* ... */

		if (!workingDirectory.exists())
			if (!workingDirectory.mkdirs())
				throw new RuntimeException(
						"The working directory could not be created: "
								+ workingDirectory);
		return workingDirectory;
	}
}
