package eu.scy.agents.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PrologAgent extends AbstractForeignAgent {

    private String scriptName;

    private String goal;

    private static final String PROLOG_SRC_DIR = "prolog";

    private static final File prologTmpDir = unpackPrologJar();

    private static String prologExe;

    private static final boolean DEBUG = true;

    /**
     * Finds the absolute path to an executable file in the path. It is also possible to search for
     * several names, which is particularly useful if it should work for multiple operating systems.
     * 
     * @param exeNames
     *            an array containing all file names to be searched
     * @return the path to a given executable
     */
    private static File findExecutable(String... exeNames) {
        HashSet<String> exes = new HashSet<String>(Arrays.asList(exeNames));
        String pathVar = System.getenv("PATH");
        // System.out.println(pathVar);
        String[] pathComps = pathVar.split(File.pathSeparator);
        for (String p : pathComps) {
            File f = new File(p);
            if (f.listFiles() != null) {
                for (File f1 : f.listFiles()) {
                    if (exes.contains(f1.getName())) {
                        return f1;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method searches the prolog sources in the classpath and extracts them to a temp folder,
     * so the Prolog interpreter can find them.
     * 
     * @return the File which denotes the temp folder, where the files where extracted to
     * @throws IOException
     *             thrown if the Prolog sources were not found or some other problems occured during
     *             extraction
     */
    private static File unpackPrologJar() {
        try {
            URL url = PrologAgent.class.getResource("/" + PROLOG_SRC_DIR);
            if (url.getProtocol().equals("file")) {
                return new File(url.getFile().replaceAll("%20", " "));
            } else {
                String file = url.getFile();
                url = new URL(file);
                file = url.getFile();
                file = file.substring(0, file.lastIndexOf('!'));
                ZipFile zf = new ZipFile(file);
                Enumeration<? extends ZipEntry> entries = zf.entries();
                HashMap<String, String> prologSources = new HashMap<String, String>();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    if (zipEntry.getName().startsWith(PROLOG_SRC_DIR) && !zipEntry.isDirectory()) {
                        String sourceFileName = zipEntry.getName();
                        sourceFileName = sourceFileName.substring(PROLOG_SRC_DIR.length() + 1);
                        InputStream is = zf.getInputStream(zipEntry);
                        StringWriter sw = new StringWriter();
                        while (is.available() > 0) {
                            sw.append((char) is.read());
                        }
                        is.close();
                        prologSources.put(sourceFileName, sw.toString());
                    }
                }
                File prologTmpDir = new File(System.getProperty("java.io.tmpdir") + File.separator + PROLOG_SRC_DIR);
                prologTmpDir.mkdirs();
                prologTmpDir.deleteOnExit();
                for (String fileName : prologSources.keySet()) {
                    File tmp = new File(prologTmpDir.getAbsolutePath() + File.separator + fileName);
                    tmp.deleteOnExit();
                    tmp.getParentFile().mkdirs();
                    FileWriter fw = new FileWriter(tmp);
                    fw.write(prologSources.get(fileName));
                    fw.close();
                }
                // System.out.println("Prologdir is " + prologTmpDir);
                return prologTmpDir;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrologAgent(String agentName, String agentId, String scriptName) {
        this(agentName, agentId, scriptName, null);
    }

    public PrologAgent(String agentName, String agentId, String scriptName, String goal) {
        super(agentName, agentId);
        this.scriptName = scriptName;
        this.goal = goal;
    }

    @Override
    protected String[] getCommandline() {
        if (prologExe == null) {
            prologExe = findExecutable("swipl", "pl", "plcon.exe").getAbsolutePath();
        }
        if (goal == null) {
            return new String[] { prologExe, "-f", prologTmpDir.getAbsolutePath() + File.separatorChar + scriptName };
        } else {
            return new String[] { prologExe, "-f", prologTmpDir.getAbsolutePath() + File.separatorChar + scriptName, "-g", goal };
        }
    }

    @Override
    protected PrintStream routeErrTo() {
        if (DEBUG) {
            return System.err;
        } else {
            return super.routeErrTo();
        }
    }

    @Override
    protected PrintStream routeOutTo() {
        if (DEBUG) {
            return System.out;
        } else {
            return super.routeOutTo();
        }
    }
}
